package datamodel.order;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import datamodel.*;
import datamodel.Pricing.PricingCategory;

/**
 * Builder class for creating Order instances.
 */
public class OrderBuilder {
    private long id;
    private Customer customer;
    private PricingCategory category; 
    private List<Order.OrderItem> items = new ArrayList<>();
    private Set<Long> idSet = new HashSet<>();

    // Method to set the ID
    public OrderBuilder withId(long id) {

        if (idSet.add(id)) {id = genID();}

        this.id = id;
        return this; // Return the builder for method chaining
    }

    private long genID(){
        return genID(0L);
    }
    private long genID(long seed){
        long newId = Math.abs(new Random().nextLong());
        return idSet.add(newId)? newId:genID(id);
    }

    // Method to set the customer
    public OrderBuilder withCustomer(Customer customer) {
        this.customer = customer;
        return this; // Return the builder for method chaining
    }
    // Method to set the Category
    public OrderBuilder withCategory(PricingCategory category) {
        this.category = category;
        return this;// Return the builder for method chaining
    }

    // Method to add an item to the order
    public OrderBuilder addItem(Article item) {
        this.items.add(new Order.OrderItem(item, 1));
        return this; // Return the builder for method chaining
    }

    public OrderBuilder addItem(Article item, int quant) {
        this.items.add(new Order.OrderItem(item, quant));
        return this; // Return the builder for method chaining
    }

    // Build method to create the Order object
    public Optional<Order> build() {
        if (customer == null) {return Optional.empty();}    // Check if has customer
        if (id == 0) {this.withId(10000000);}            // Check if has id
        if (items.isEmpty())  {return Optional.empty();}      // check if has items

        return Optional.of(new Order(id, customer,items,category)); // Create a new Order using the builder
    }

}