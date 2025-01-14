package datamodel.order;

import java.util.ArrayList;
import java.util.List;

import datamodel.*;
import datamodel.Pricing.PricingCategory;

/**
 * Represents an order in the system.
 */
public class Order {
    private final long id;
    private final Customer customer;
    private final List<OrderItem> items;
    private final PricingCategory category;

    /**
     * Represents an item in an order with its article and quantity.
     */
    public record OrderItem(Article article, int quantity) { }

    /**
     * Creates a new Order.
     * 
     * @param id unique identifier for this order
     * @param customer the customer placing the order
     * @param pricing the pricing rules for this order
     * @param created the date and time when the order was created
     * id, customer, pricing, created, items
     */
    protected Order(long id, Customer customer, List<OrderItem> items, PricingCategory category) {
        this.id = id;
        this.customer = customer;
        this.items = items;
        this.category = category;
    }

    /**
     * Returns the unique identifier of this order.
     * @return the order's ID
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the customer who placed this order.
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the list of items in this order.
     * @return list of order items
     */
    public List<OrderItem> getItems() {
        return new ArrayList<>(items); // Return a copy of the items list
    }

    public PricingCategory getCategory() {
        return this.category;
    }



}