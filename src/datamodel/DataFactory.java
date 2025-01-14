package datamodel;

import java.util.Optional;

import datamodel.Pricing.PricingCategory;
import datamodel.order.OrderBuilder;

/**
 * Factory class for creating domain objects in the system.
 * Implements the Singleton pattern to ensure only one factory instance exists.
 * All domain objects (Customer, Article, Order) should be created through this factory.
 */
public class DataFactory {
    private static final DataFactory instance = new DataFactory();
    private final sID sID = new sID();  // Counter for IDs
    private final sID articleId = new sID(100000);

    private DataFactory() {}  // private constructor for singleton

    /**
     * Returns the singleton instance of the DataFactory.
     * @return the singleton instance
     */
    public static DataFactory getInstance() {
        return instance;
    }

    /**
     * Creates a new Customer with the specified name and contact information.
     * 
     * @param name the customer's name (required, non-empty)
     * @param contact the customer's initial contact information (optional)
     * @return Optional containing the created Customer if successful, empty Optional if invalid input
     */
    public Optional<Customer> createCustomer(String name, String contact) {
        if (name == null || name.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new Customer(sID.nextLong(), name, contact));
    }

    /**
     * Creates a new Article with the specified details.
     * 
     * @param description the article's description (required, non-empty)
     * @param unitPrice the price per unit in smallest currency unit (e.g., cents)
     * @param category the pricing category for the article
     * @param taxRate optional tax rate, defaults to Regular if not specified
     * @return Optional containing the created Article if successful, empty Optional if invalid input
     */
    public Optional<Article> createArticle(String description, long unitPrice,Pricing.PricingCategory category, Pricing.TAXRate... taxRate) {
        
          // Start from 100000 for articles
        if (description == null || description.trim().isEmpty() || unitPrice < 0 || taxRate == null) {
            return Optional.empty();
        }
        Pricing.TAXRate tax = taxRate.length > 0 ? taxRate[0] : Pricing.TAXRate.Regular;
        String id = articleId.nextString("SKU-");
        return Optional.of(new Article(id, description, unitPrice, category, tax));
    }


//    private interface IOrder<T,R> {
//        Optional<R> appy(T spec);
//    }
//    
//    public record ArticelSpec(String description) {}
//
//    private Optional<Article> ArticleSpec(IOrder<ArticelSpec,Article> order){
//        return Optional.of(order.apply(new Article()))
//    }

    public Optional<OrderBuilder> createOrderBuilder(PricingCategory basepricing, Customer customer, Article... article) {
        if(customer == null){return Optional.empty();}

        var ob =  Optional.of(new OrderBuilder()
            .withCustomer(customer)
            .withCategory(basepricing)
        );

        for (var i:article) {
            ob.get().addItem(i);
        };
        return ob;
    }

    /**
     * Creates a new Order for the specified customer.
     * 
     * @param customer the customer placing the order (required)
     * @return Optional containing the created Order if successful, empty Optional if invalid input
     */
     // public Optional<Order> createOrder(Customer customer) {
    //     if (customer == null) {
    //         return Optional.empty();
    //     }
    //     return Optional.of(new Order(sID.nextLong(), customer, Pricing.PricingCategory.BasePricing.pricing(), LocalDateTime.now()));
    // }
} 