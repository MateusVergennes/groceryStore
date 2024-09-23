# Grocery Store API

## Overview

This API was created to simulate the operations of a grocery store, allowing users to manage their shopping carts and process transactions efficiently. The API automates the retrieval of tax rates on a daily basis and simulates payment processing to determine whether transactions are approved.

## Features

- **Scheduled Automated Tax Rate Retrieval**: The API fetches the latest tax rate from an endpoint automatically on a daily schedule, ensuring that sales are always compliant with current regulations.
  
- **Simulated Payment Processing**: It simulates API calls to a payment gateway, checking if payments are approved before finalizing the shopping cart.

- **Flexible Payment Methods**: Users can choose from various payment methods and register them within the system. The API allows users to specify whether these methods require a simulated payment approval.

- **Multi-Cart Support**: Each product request must include a `cartToken`, enabling multiple shopping carts (or registers) to utilize the API simultaneously.

- **Product Information Retrieval**: Products are sourced from a local "mockAPI," which provides details such as pricing and promotions. When a product is manipulated, the API calculates valid promotions, discount amounts, tax amounts, and the total price.

- **Checkout Option**: Users have the option to finalize their purchases, streamlining the shopping experience.

## Future Work

There are several potential enhancements for this API, including:

- Integration with a real payment gateway for live transaction processing.
- Implementing user authentication and authorization for added security.
- Expanding product management features, such as inventory tracking and order history.
- Providing detailed analytics for sales and customer behavior.

## Follow-Up Questions

1. **How long did you spend on the test? What would you add if you had more time?**  
   I spent approximately [12hours/3days] on the test. If I had more time, I would enhance the error handling mechanisms and implement unit tests to ensure robust functionality.

2. **What was the most useful feature that was added to the latest version of your chosen language? Please include a snippet of code that shows how you've used it.**  
   The introduction of records in Java 16 has been immensely useful for creating simple data transfer objects. Here’s a snippet demonstrating its use in the `OrdersRequestoDto`:
   ```java
   public record OrdersRequestoDto(Long id, int totalTaxes, int totalDiscount, int totalPrice, Long paymentMethodId, boolean isFinished, String cartToken, List<ProductOrderRequestDto> productOrders) {
   }
3. **What did you find most difficult?**  
   The most challenging aspect was ensuring seamless integration between the different components, particularly managing the dependencies and ensuring that the simulated payment processing worked as intended.

4. **What mechanism did you put in place to track down issues in production on this code? If you didn’t put anything, write down what you could do.**  
   Currently, I have implemented basic logging using [logging framework, e.g., SLF4J, Log4j]. In the future, I could enhance this by integrating an error tracking service like Sentry or implementing more detailed logging levels to track down specific issues in production.

5. **The Wiremock represents one source of information. We should be prepared to integrate with more sources. List the steps that we would need to take to add more sources of items with different formats and promotions.**  
   - **Develop Integration Logic**: Write service classes to handle the communication with the new data sources, including methods to fetch and process the data.
   - **Implement Promotion Logic**: Adapt the existing promotion calculation logic to accommodate new formats and rules.
   - **Testing**: Thoroughly test the integration with both unit tests and integration tests to ensure correctness.
   - **Documentation**: Update the API documentation to reflect the new sources and their formats.

##Simplified logic
![diagram](https://github.com/user-attachments/assets/3d542035-7725-47f1-8578-13e43bb5abc0)

##Simulated APIs
![taxAPI](https://github.com/user-attachments/assets/aae34251-911c-4df2-9800-8c93ce8a7452)
![paymentAPI](https://github.com/user-attachments/assets/8c424f36-39ef-4fb2-b6fb-5f584327e2a1)

