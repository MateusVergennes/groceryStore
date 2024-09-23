package com.qikserve.qikservetest.module.shop.services;

import com.qikserve.qikservetest.module.shop.dtos.requests.OrdersRequestoDto;
import com.qikserve.qikservetest.module.shop.dtos.requests.ProductOrderRequestDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.OrdersResponseDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.PaymentMethodsResponseDto;
import com.qikserve.qikservetest.module.shop.dtos.responses.ProductOrderResponseDto;
import com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos.ProductDetailDto;
import com.qikserve.qikservetest.module.systems_integration.mocky_api.dtos.PromotionDto;
import com.qikserve.qikservetest.module.systems_integration.mocky_api.services.MockyApiService;
import com.qikserve.qikservetest.module.systems_integration.payment_approvation.service.PaymentService;
import com.qikserve.qikservetest.module.systems_integration.tax.entities.Tax;
import com.qikserve.qikservetest.module.systems_integration.tax.services.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ShoppingService {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentMethodsService paymentMethodsService;
    @Autowired
    private MockyApiService mockyApiService;
    @Autowired
    private TaxService taxService;

    private int calculateQuantityOfProducts(OrdersResponseDto cartOpened, ProductDetailDto productWithDiscount, int quantity) {
        int totalQuantity = quantity;
        if (cartOpened != null) {
            totalQuantity += cartOpened.productOrders().stream()
                    .filter(productOrder -> productOrder.productId().equals(productWithDiscount.id()))
                    .mapToInt(ProductOrderResponseDto::quantity)
                    .sum();
        }
        return totalQuantity;
    }

    private int discount_qtyBasedPriceOverride(OrdersResponseDto cartOpened, ProductDetailDto productWithDiscount, PromotionDto promotion, int quantity) {
        int totalDiscount = 0;
        int quantityOfProducts = calculateQuantityOfProducts(cartOpened, productWithDiscount, quantity);

        if (promotion.required_qty() != 0) {
            int quantityOfProductDiscountCanBeApplied = quantityOfProducts / promotion.required_qty();
            totalDiscount = ((productWithDiscount.price() * promotion.required_qty()) - (promotion.price())) * quantityOfProductDiscountCanBeApplied;
        }

        return totalDiscount;
    }

    private int discount_buyXGetYFree(OrdersResponseDto cartOpened, ProductDetailDto productWithDiscount, PromotionDto promotion, int quantity) {
        int totalDiscount = 0;
        int quantityOfProducts = calculateQuantityOfProducts(cartOpened, productWithDiscount, quantity);

        if (promotion.required_qty() != 0 && promotion.free_qty() != 0) {
            int quantityOfProductDiscountCanBeApplied = quantityOfProducts / promotion.required_qty();
            totalDiscount = quantityOfProductDiscountCanBeApplied * productWithDiscount.price() * promotion.free_qty();
        }

        return totalDiscount;
    }

    private int discount_flatPercent(OrdersResponseDto cartOpened, ProductDetailDto productWithDiscount, PromotionDto promotion, int quantity) {
        int totalDiscount = 0;
        int quantityOfProducts = calculateQuantityOfProducts(cartOpened, productWithDiscount, quantity);

        if (promotion.amount() != 0) {
            totalDiscount = (productWithDiscount.price() * promotion.amount() / 100) * quantityOfProducts;
        }

        return totalDiscount;
    }

    private int discountAvailable(OrdersResponseDto cartOpened, ProductDetailDto productWithDiscount, int quantity, String productId) {
        AtomicInteger totalDiscount = new AtomicInteger();
        productWithDiscount.promotions().forEach(promotion -> {
            int initialQuantity = productWithDiscount.id().equals(productId) ? quantity : 0;
            switch (promotion.type()) {
                case "QTY_BASED_PRICE_OVERRIDE":
                    totalDiscount.addAndGet(discount_qtyBasedPriceOverride(cartOpened, productWithDiscount, promotion, initialQuantity));
                    break;

                case "BUY_X_GET_Y_FREE":
                    totalDiscount.addAndGet(discount_buyXGetYFree(cartOpened, productWithDiscount, promotion, initialQuantity));
                    break;

                case "FLAT_PERCENT":
                    totalDiscount.addAndGet(discount_flatPercent(cartOpened, productWithDiscount, promotion, initialQuantity));
                    break;

                default:
                    totalDiscount.addAndGet(0);
                    break;
            }
        });
        return totalDiscount.get();
    }

    private void saveShop(AtomicInteger totalPrice, int totalTaxes, int totalDiscount, int quantity, ProductDetailDto productToInsert, OrdersResponseDto cartOpened, String cartToken) {
        ProductOrderRequestDto productToAdd = new ProductOrderRequestDto(0L, productToInsert.id(), quantity, productToInsert.price(), productToInsert.promotions().getFirst().type());
        if (cartOpened == null) {
            OrdersRequestoDto orderToSave = new OrdersRequestoDto(0L, totalTaxes, totalDiscount, totalPrice.get(), 0L, false, cartToken, Collections.singletonList(productToAdd));
            ordersService.save(Collections.singletonList(orderToSave));
        } else {
            OrdersResponseDto orderWithNewProduct = ordersService.addProductToOrder(cartOpened.id(), productToAdd);
            List<ProductOrderRequestDto> productOrdersRequestList = orderWithNewProduct.productOrders().stream()
                    .map(productOrderResponse -> new ProductOrderRequestDto(
                            productOrderResponse.id(),
                            productOrderResponse.productId(),
                            productOrderResponse.quantity(),
                            productOrderResponse.priceProduct(),
                            productOrderResponse.promotion()))
                    .toList();

            OrdersRequestoDto orderToUpdate = new OrdersRequestoDto(orderWithNewProduct.id(), totalTaxes, totalDiscount, totalPrice.get(), orderWithNewProduct.paymentMethodId(),
                    orderWithNewProduct.isFinished(), orderWithNewProduct.cartToken(), productOrdersRequestList);

            ordersService.update(orderToUpdate);
        }
    }

    private void shopCalculus(OrdersResponseDto cartOpened, int quantity, String productId, int totalDiscount, String cartToken) {
        AtomicInteger totalPrice = new AtomicInteger();
        int totalTaxes = 0;

        ProductDetailDto productToInsert = mockyApiService.getProductById(productId);
        totalPrice.addAndGet(productToInsert.price()*quantity);

        if (cartOpened != null && cartOpened.productOrders() != null) {
            cartOpened.productOrders().forEach(order -> {
                totalPrice.addAndGet(order.priceProduct()*order.quantity());
            });
        }

        totalPrice.addAndGet(-totalDiscount);

        Optional<Tax> tax = taxService.findByName("Food");//type of tax
        if (tax.isPresent()) {
            double discountPercentage = tax.get().getDiscountPercentage();
            totalTaxes = (int) Math.round(totalPrice.get() * (discountPercentage / 100.0));
            totalPrice.addAndGet(totalTaxes);
        }

        saveShop(totalPrice, totalTaxes, totalDiscount, quantity, productToInsert, cartOpened, cartToken);
    }


    public void shop(String cartToken, String productId, int quantity) {
        OrdersResponseDto cartOpened = ordersService.findByCartTokenAndNotFinished(cartToken);
        ProductDetailDto productSearched = mockyApiService.getProductById(productId);

        if (productSearched == null) {
            // Produto não encontrado
            return;
        }

        Set<String> productIds = new HashSet<>();// Set para garantir que não haja duplicatas
        productIds.add(productId);

        if (cartOpened != null) {// If the cart already exists (is not null), adds the IDs of the existing products in the cart
            List<String> cartProductIds = cartOpened.productOrders()
                    .stream()
                    .map(ProductOrderResponseDto::productId)
                    .collect(Collectors.toList());

            productIds.addAll(cartProductIds);
        }

        List<String> uniqueProductIds = new ArrayList<>(productIds);// Converts the Set back to a List (without duplicates)

        List<ProductDetailDto> products = mockyApiService.getProductsByIds(uniqueProductIds);// Busca todos os produtos únicos no mockyApi

        List<ProductDetailDto> productsWithDiscount = products.stream()// Filter products with promotions
                .filter(product -> product.promotions() != null && !product.promotions().isEmpty())
                .collect(Collectors.toList());

        int totalDiscount = 0;
        for (ProductDetailDto productWithDiscount : productsWithDiscount) {// Calls the discountAvailable function for each product with promotions
            totalDiscount += discountAvailable(cartOpened, productWithDiscount, quantity, productId);
        }

        shopCalculus(cartOpened, quantity, productId, totalDiscount, cartToken);
    }

    public boolean finishShop(String cartToken, Long paymentMethodId, int value){
        PaymentMethodsResponseDto paymentMethodData = paymentMethodsService.findById(paymentMethodId);
        boolean result = false;
        if (paymentMethodData != null) {
            boolean approved = false;
            if (paymentMethodData.needAuthorization()) {
                approved = paymentService.processPayment(value);
            }else{
                approved = true;
            }
            if (approved){
                result = true;
                OrdersResponseDto orderOpened = ordersService.findByCartTokenAndNotFinished(cartToken);
                List<ProductOrderRequestDto> productOrdersRequestList = orderOpened.productOrders().stream()
                        .map(productOrderResponse -> new ProductOrderRequestDto(
                                productOrderResponse.id(),
                                productOrderResponse.productId(),
                                productOrderResponse.quantity(),
                                productOrderResponse.priceProduct(),
                                productOrderResponse.promotion()))
                        .toList();

                OrdersRequestoDto orderToFinish = new OrdersRequestoDto(orderOpened.id(), orderOpened.totalTaxes(), orderOpened.totalDiscount(),
                        orderOpened.totalPrice(), orderOpened.paymentMethodId(), result, orderOpened.cartToken(), productOrdersRequestList);

                ordersService.update(orderToFinish);
            }
        }
        return result;
    }

}
