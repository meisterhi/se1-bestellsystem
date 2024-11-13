# C4: *DataFactory*

The assignment demonstrates the patterns: *Singleton* and *Factory*.

Class *DataFactory* centralizes the creation of objects of classes
of package `datamodel`, e.g. of class *Customer*.

*DataFactory* implements the *"lazy"*
[*Singleton*](https://en.wikipedia.org/wiki/Singleton_pattern)
pattern and provides
[*Factory*](https://refactoring.guru/design-patterns/factory-method)
methods for the creation of *Customer* objects.

The change allows to restructure class *Customer* into an
*immutable* class.

Topics:

1. [Create a Singleton Class: *DataFactory*](#1-create-a-singleton-class-datafactory)
1. [Immutalize Class *Customer*](#2-immutalize-class-customer)
1. [Run Example Code](#3-run-example-code)
1. [Commit and Push Changes](#4-commit-and-push-changes)


&nbsp;

## 1. Create a Singleton Class: *DataFactory*

Creating objects of *data classes* from the package [datamodel](src/datamodel)
with *new()* (constructors) has disadvantages:

- objects can be created with invalid attributes, e.g. Customer objects
    with *name:* `null` or *name:* `""`,

- uniqueness of *id* attributes cannot be guaranteed across objects,

- *data classes* contain logic, e.g. the resolution of single-String names
    into first- and last name attributes.

Furthermore, attributes of objects of *data classes* should not arbitrarily
be modified after creation, e.g. names set to null (*setName(null)*).

A common approach to address these problems is to centralize *object creation*
at a central place: `DataFactory.java`.

Create a class `DataFactory.java` in the package [datamodel](src/datamodel)
that:

1. implements the *"lazy"* [*Singleton*](https://en.wikipedia.org/wiki/Singleton_pattern)
    pattern with a public accessor method:

    ```java
    /**
     * Static accessor method to {@link DataFactory} <i>Singleton</i> instance.
     * @return singleton {@link DataFactory} instance
     */
    public static DataFactory getInstance() { ... }
    ```

1. implements the [*Factory*](https://refactoring.guru/design-patterns/factory-method)
    for the creation of *Customer* objects. Factory methods centralize
    *id*-generation and validate parameters: *name*, *contact*.
    
    A *Customer* object is only created if parameters are valid. If no object can
    be created, an empty *Optional* is returned:

    ```java
    /**
     * <i>Factory</i> method to create an object of class {@link Customer}
     * from validated parameters. The <i>id</i> attribute os provided from
     * the {@link customerIdGenerator}. No object is created when one
     * parameter is not valid.
     * @param name single-String name parameter, valid if: not null and not empty,
     *          example: "Eric Meyer" or "Meyer, Eric"
     * @param contact mandatory contact parameter that is validated either as an
     *          email address containing '@' or a phone number
     * @param customer accessor function called on created object
     * @return created {@link Customer} object with valid parameters or empty
     */
    public Optional<Customer> createCustomer(String name, String contact, Function<Customer, Customer> customer) {
        var n = splitName(name);
        if(n.isPresent()) {
            long id = customerIdGenerator.next();
            var validContact = validateContact(null, contact);
            if(validContact.isPresent()) {
                // only create Customer when all conditions are met
                Customer c = new Customer(id, n.get().first(), n.get().last());
                c.contacts.add(validContact.get());
                if(customer != null) {
                    customer.apply(c);
                }
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Optional<Customer> createCustomer(String name, String contact) {
        return createCustomer(name, contact, null);
    }
    ```

Supplement class `DataFactory.java` with code:

```java
/*
 * random generator
 */
private final Random rand = new Random();

/*
 * internal <i>id</i>-generator for {@link Customer} objects
 */
private IdGenerator<Long> customerIdGenerator = new IdGenerator<>(
    () -> (long)(100000 + rand.nextInt(900000)), // id supplier
    Arrays.asList(  // inital id pool
        892474L, 643270L, 286516L, 412396L, 456454L, 651286L)
);

/*
 * regular expression used to validate email address, source:
 * https://stackoverflow.com/questions/8204680/java-regex-email
 */
private final Pattern emailRegex =
    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

/*
 * regular expression used to validate phone or fax number
 */
private final Pattern phoneRegex =
    Pattern.compile("^(phone:|fax:|\\+[0-9]+|){0,1}\\s*[\\s0-9()-]*", Pattern.CASE_INSENSITIVE);

/**
 * Validate contact and return valid contact or empty result. If contact
 * is valid and customer object is provided, the contact is added.
 * @param customer customer object to which contact is added
 * @param contact contact to validate
 * @return valid and possibly modified, e.g. trimmed contact or empty result
 */
Optional<String> validateContact(Customer customer, String contact) {
    String cont = null;
    if(contact != null) {
        cont = trim(contact);
        final int minLength = 6;
        boolean valid = cont.length() >= minLength;
        valid = valid && (
            emailRegex.matcher(cont).matches() ||
            phoneRegex.matcher(cont).matches()
        );
        // add contact to customer object, if not null
        cont = customer==null? (valid? cont : null) : (
            ( valid && ! customer.contacts.contains(cont)? customer.contacts.add(cont) : false)?
                null :      // contact has been added: return empty contact to avoid duplicate
                (valid? cont : null)    // contact was not added, return valid contact
        );
    }
    return Optional.ofNullable(cont);
}

/**
 * Internal class to manage pool of unique {@code ids} of type {@code T}.
 */
private class IdGenerator<T> {
    final Supplier<T> supplier;
    final List<T> idPool;
    int i=0;
    int cap=0;

    IdGenerator(Supplier<T> supplier, List<T> initializer) {
        this.supplier = supplier;
        this.idPool = initializer==null? new ArrayList<>() : new ArrayList<>(initializer);
    }

    T next() {
        if(i >= cap) {
            cap += Stream.generate(supplier)
                    .filter(n -> ! idPool.contains(n))
                    .limit(10)  // add 10 supplied ids to the pool
                    .peek(n -> idPool.add(n))
                    .count();
        }
        return idPool.get(i++);
    }
};

/**
 * Record with first(name) and last(name) parts.
 */
record Name(String first, String last) { }

/**
 * Split single-String name to first(name) and last(name) parts,
 * e.g. "Meyer, Eric" to first: "Eric" and last: "Meyer"
 * @param name single-String name
 * @return first(name) and last(name) parts
 */
private Optional<Name> splitName(String name) {
    if(name != null && name.length() > 0) {
        String first="", last="";
        String[] spl1 = name.split("[,;]");
        if(spl1.length > 1) {
            // two-section name with last name first
            last = spl1[0];
            first = spl1[1];    // ignore higher splitters in first names
        } else {
            // no separator [,;] -> split by white spaces;
            for(String s : name.split("\\s+")) {
                if( last.length() > 0 ) {
                    // collect firstNames in order and lastName as last
                    first += (first.length()==0? "" : " ") + last;
                }
                last = s;
            }
        }
        if(last.length() > 0) {
            return Optional.of(new Name(trim(first), trim(last)));
        }
    }
    return Optional.empty();
}

/**
 * Trim leading and trailing white spaces, commata {@code [,;]} and
 * quotes {@code ["']} from a String.
 * @param s String to trim
 * @return trimmed String
 */
private String trim(String s) {
    s = s.replaceAll("^[\\s\"',;]*", "");   // trim leading white spaces[\s], commata[,;] and quotes['"]
    s = s.replaceAll( "[\\s\"',;]*$", "");  // trim trailing.
    return s;
}
```


&nbsp;

## 2. Immutalize Class *Customer*

An *immutable* class *Customer* does not allow changes to attributes.
*DataFactory* is the only class that can create new *Customer*
objects from validated parameters.

Making class *Customer* immutable means:

1. Make all attributes: `final`.

1. Remove *setter()* methods.

1. Remove all constructors, except one used by *DataFactory* with
    visibility: `protected` that prevents creation of *Customer*
    objects from other packages:

    ```java
    /**
    * Immutable class of entity type <i>Customer</i>.
    * <p>
    * Customer is a person who creates and holds (owns) orders in the system.
    * </p><p>
    * An *immutable* class *Customer* does not allow changes to attributes.
    * {@link DataFactory} is the only class that can create new {@link Customer}
    * objects from validated parameters.
    * </p>
    * @version <code style=color:green>{@value application.package_info#Version}</code>
    * @author <code style=color:blue>{@value application.package_info#Author}</code>
    */
    public class Customer {

        /**
        * unique Customer id attribute
        */
        private final long id;

        /**
        * surname attribute
        */
        private final String lastName;

        /**
        * none-surname name parts
        */
        private final String firstName;

        /**
        * contact information with multiple entries, e.g. email addresses
        * or phone numbers, attribute is exposed to {@link DataFactory}
        * in the same package
        */
        final List<String> contacts = new ArrayList<>();


        /**
        * None-public constructor used by {@link DataFactory}.
        * @param id id generated by {@link DataFactory}
        * @param firstName first name attribute
        * @param lastName last name attribute
        */
        Customer(long id, String firstName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        /**
        * Id attribute getter.
        * @return customer id
        */
        public long getId() {
            return id;
        }

        /**
        * LastName attribute getter.
        * @return value of lastName attribute
        */
        public String getLastName() {
            return lastName;
        }

        /**
        * FirstName attribute getter.
        * @return value of firstName attribute
        */
        public String getFirstName() {
            return firstName;
        }

        /**
        * Return the number of contacts.
        * @return number of contacts
        */
        public int contactsCount() {
            return contacts.size();
        }

        /**
        * Contacts getter (as immutable {@link Iterable<String>}).
        * @return contacts (as immutable {@link Iterable<String>})
        */
        public Iterable<String> getContacts() {
            return contacts;
        }

        /**
        * Add new contact validated through {@link DataFactory}. Method has
        * no effect if contact is not valid.
        * @param contact contact added validated through {@link DataFactory}
        * @return chainable self-reference
        */
        public Customer addContact(String contact) {
            DataFactory.getInstance().validateContact(this, contact);
            return this;
        }

        /**
        * Delete the i-th contact with {@code i >= 0} and {@code i < contactsCount()},
        * method has no effect when {@code i} is outside these bounds.
        * @param i index of contact to delete
        */
        public void deleteContact(int i) {
            if( i >= 0 && i < contacts.size() ) {
                contacts.remove(i);
            }
        }

        /**
        * Delete all contacts.
        */
        public void deleteAllContacts() {
            contacts.clear();
        }
    }
    ```


&nbsp;

## 3. Run Example Code

Compare the previous method of creating *Customer* objects from *Application_C2*:

```java
@Override
public void run(Properties properties, String[] args) {

    final Customer eric = new Customer("Eric Meyer")
        .setId(892474L)     // set id, first time
        .setId(947L)        // ignored, since id can only be set once
        .addContact("eric98@yahoo.com")
        .addContact("eric98@yahoo.com") // ignore duplicate contact
        .addContact("(030) 3945-642298");

    final Customer anne = new Customer("Anne Bayer")
        .setId(643270L)
        .addContact("anne24@yahoo.de")
        .addContact("(030) 3481-23352")
        .addContact("fax: (030)23451356");

    final Customer tim = new Customer("Tim Schulz-Mueller")
        .setId(286516L)
        .addContact("tim2346@gmx.de");

    final Customer nadine = new Customer("Nadine-Ulla Blumenfeld")
        .setId(412396L)
        .addContact("+49 152-92454");

    final Customer khaled = new Customer()
        .setName("Khaled Saad Mohamed Abdelalim")
        .setId(456454L)
        .addContact("+49 1524-12948210");

    List<Customer> customers = List.of(eric, anne, tim, nadine, khaled);
    //
    // print customer list
    customers.stream()
        // .sorted((c1, c2) -> c1.getLastName().compareTo(c2.getLastName()))
        .map(c -> print(c))     // .map(this::print)
        .forEach(System.out::println);
}
```

The new method creates *Customer* objects through *DataFactory*.
Creation of object by directly invoking the constructor is no longer
possible outside the `datamodel` package.

With the new method, *Customer* objects are only created from
valid parameters. If parameters are not valid, e.g. with an
invalid email contact such as: `"locomandy<>gmx.de"`.

```java
/*
 * reference to {@link DataFactory} singleton
 */
private final DataFactory dataFactory = DataFactory.getInstance();


@Override
public void run(Properties properties, String[] args) {
    // 
    // previous method to create Customer object:
    // final Customer eric = new Customer(892474L, "Eric", "Meyer");
    // 
    List<Customer> customers = List.of(
        // 
        dataFactory.createCustomer(
            "Eric Meyer", "eric98@yahoo.com", c -> c.addContact("eric98@yahoo.com").addContact("(030) 3945-642298")
        ),
        dataFactory.createCustomer(
            "Anne Bayer", "anne24@yahoo.de", c -> c.addContact("(030) 3481-23352").addContact("fax: (030)23451356")
        ),
        dataFactory.createCustomer("Schulz-Mueller, Tim", "tim2346@gmx.de"),
        dataFactory.createCustomer("Blumenfeld, Nadine-Ulla", "+49 152-92454"),
        dataFactory.createCustomer("Khaled Saad Mohamed Abdelalim", "+49 1524-12948210"),
        dataFactory.createCustomer("Mandy Mondschein", "locomandy<>gmx.de"), // invalid email address, no object is created
        dataFactory.createCustomer("", "nobody@gmx.de")     // invalid name, no object is created
    //
    ).stream()
        .filter(c -> c.isPresent())
        .map(c -> c.get())
        .toList();

    final TableFormatter tf = new TableFormatter("|%-6s", "| %-32s", "| %-32s |")
        .line()
        .row("ID", "NAME", "CONTACTS")  // table header
        .line();

    // fill rows into Customer table
    customers.stream()
        // .sorted((c1, c2) -> c1.getLastName().compareTo(c2.getLastName()))
        .forEach(c -> {
            String id=String.format("%d", c.getId());
            String name=fmtCustomerName(c, 0);
            String contact=fmtCustomerContacts(c, 1);
            // 
            tf.row(id, name, contact);  // write row into table
        });
    //
    tf.line();

    // print Customer table
    System.out.println(tf.get().toString());
}
```

Running the program outputs the *Customer* table:

```
+------+---------------------------------+----------------------------------+
|ID    | NAME                            | CONTACTS                         |
+------+---------------------------------+----------------------------------+
|892474| Meyer, Eric                     | eric98@yahoo.com, (+1 contacts)  |
|643270| Bayer, Anne                     | anne24@yahoo.de, (+2 contacts)   |
|286516| Schulz-Mueller, Tim             | tim2346@gmx.de                   |
|412396| Blumenfeld, Nadine-Ulla         | +49 152-92454                    |
|456454| Abdelalim, Khaled Saad Mohamed  | +49 1524-12948210                |
+------+---------------------------------+----------------------------------+
```

Modify code to print the *Customer* table sorted alphabetically by last name:

```
+------+---------------------------------+----------------------------------+
|ID    | NAME                            | CONTACTS                         |
+------+---------------------------------+----------------------------------+
|456454| Abdelalim, Khaled Saad Mohamed  | +49 1524-12948210                |
|643270| Bayer, Anne                     | anne24@yahoo.de, (+2 contacts)   |
|412396| Blumenfeld, Nadine-Ulla         | +49 152-92454                    |
|892474| Meyer, Eric                     | eric98@yahoo.com, (+1 contacts)  |
|286516| Schulz-Mueller, Tim             | tim2346@gmx.de                   |
+------+---------------------------------+----------------------------------+
```


&nbsp;

## 4. Commit and Push Changes

Commit and push changes to your remote repository.

<!-- 
<img src="https: //raw.githubusercontent.com/sgra64/se1-bestellsystem/refs/heads/markup/c2-customer/Customer.png" alt="drawing" width="600"/>
 -->
