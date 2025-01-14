package datamodel;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a customer in the system.
 * Contains customer's personal information including name and contact details.
 */
public class Customer {
    private long id;
    private String firstName;
    private String lastName;
    private Set<String> contacts;

    /**
     * Creates a new Customer with the specified name and initial contact information.
     * 
     * @param name the full name of the customer
     * @param contact the initial contact information
     */
    public Customer(long id, String name, String contact) {
        this.id = id;
        this.contacts = new HashSet<>();
        parseName(name);
        if (contact != null) {
            addContact(contact);
        }
    }

    /**
     * Parses the full name into first name and last name components.
     * Handles names in formats: "lastName, firstName" or "firstName lastName"
     * 
     * @param name the full name to parse
     */
    private void parseName(String name) {
        String[] parts = name.split(",");
        if (parts.length > 1) {
            this.lastName = parts[0].trim();
            this.firstName = parts[1].trim();
        } else {
            parts = name.split(" ");
            this.firstName = parts[0].trim();
            this.lastName = parts.length > 1 ? parts[parts.length - 1].trim() : "";
        }
    }

    /**
     * Adds a new contact information to this customer.
     * 
     * @param contact the contact information to add
     * @return this Customer instance for method chaining
     */
    public Customer addContact(String contact) {
        if (contact != null) {
            contacts.add(contact);
        }
        return this;
    }

    /**
     * Returns the unique identifier of this customer.
     * @return the customer's ID
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the first name of this customer.
     * @return the customer's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of this customer.
     * @return the customer's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns all contact information for this customer.
     * @return set of contact information
     */
    public Set<String> getContacts() {
        return contacts;
    }

    /**
     * Returns the number of contacts this customer has.
     * @return the count of contact information entries
     */
    public long contactsCount() {
        return contacts.size();
    }
} 