# C4: *DataFactory*

The assignment demonstrates two basic
[*Software Design Patterns*](https://refactoring.guru/design-patterns):
*Singleton* and *Factory*.

- A new class *DataFactory* centralizes the creation of objects of classes
    from package `datamodel`, e.g. objects of class *Customer*.

- *DataFactory* implements the *"lazy"*
    [*Singleton*](https://en.wikipedia.org/wiki/Singleton_pattern)
    pattern and provides
    [*Factory*](https://refactoring.guru/design-patterns/factory-method)
    methods for the creation of *Customer* objects.

- *Customer* objects can no longer be created from outside the `datamodel`
    package from constructors with *new*. *DataFactory's* *create()* methods
    will be used instead.

The change allows restructuring class *Customer* into an *immutable* class,
which means its attributes can no longer be altered or changed.
*Immutable* classes have no *setter*-methods.

Steps:

1. [Create a Singleton Class: *DataFactory*](#1-create-a-singleton-class-datafactory)
1. [Immutalize Class *Customer*](#2-immutalize-class-customer)
1. [Run Driver Code](#3-run-driver-code)
1. [Update JUnit-Tests](#4-update-junit-tests)
1. [Commit and Push Changes](#5-commit-and-push-changes)


&nbsp;

## 1. Create a Singleton Class: *DataFactory*

The direct creation of objects of *data classes* from the package `datamodel`
with *new()* (constructors) has disadvantages:

- Objects can be created with invalid attributes, e.g. Customer objects
    with *name:* `null` or an empty *name:* `""`. A constructor will always
    create an object, unless it throws an Exception such as
    [*IllegalArgumentException*](https://docs.oracle.com/javase/8/docs/api/java/lang/IllegalArgumentException.html), which will terminate the program if not
    caught.

- Uniqueness of *id*-attributes cannot be guaranteed across objects, if *id*
    are supplied as arguments and can arbitrarily be set.

- Validating arguments in methods introduces logic into "data classes* that
    should be be there. *Data classes* should represent a data model, not logic.

    An example is the separation of single-String names in class *Customer*
    into first- and last name attributes. Rules of name separation and code
    should not be in the data class *Customer*.

Furthermore, attributes of objects of *data classes* should not arbitrarily
be modified after creation, e.g. *id* set to other values changing the identity
of an object or names set to null.

A common approach to address these problems is to centralize *object creation*
at a central place: `DataFactory.java`. Purpose if a factory is to create objects.

- Class *DataFactory* exists as a *singleton*, which means there exists at most
    one instance of the class. This is indicated by stereotype: `<<singleton>>`
    in the UML class diagram.

- *DataFactory* provides unique *id* attributes when new *Customer* objects are
    created. An internal `IdPool<T>` guarantees the uniqueness of *id* of a
    generic type `<T>` (`T`: *Long* is used for *Customer* *id*, class *Article*
    will use *String* for *id*).

    Each data model class has its own *IdPool*. Each pool is provided with some
    initial *id* and expands by random-generated *id* as objects are created by
    *DataFactory*.

    ```java
    /**
     * {@link IdPool} for {@link Customer} objects with 6-digit random numbers.
     */
    private final IdPool<Long> customerIdPool = new IdPool<>(
        () -> 100000L + rand.nextLong(900000L),
        Arrays.asList(  // initial Customer ids
            892474L, 643270L, 286516L, 412396L, 456454L, 651286L
        )
    );
    ```

- Furthermore, splitting single-String names, e.g. `"Eric Meyer"` into
    *last name:* `"Meyer"` and *first name:* `"Eric"` has been moved
    out of class *Customer* into class *DataFactory*.

    First and last name parts are represented by a Java structure
    [*Record*](https://medium.com/@mak0024/a-comprehensive-guide-to-java-records-2e8edcbd9c75):

    ```java
    /**
     * Record of first and last name parts of a name.
     * @param first first name parts
     * @param last last name parts
     */
    public record NameParts(String first, String last) { }
    ```

    Factory-method: `validateSplitName(String name)` splits a single-String name and vaidates
    name parts. With valid first- and last name parts, `NameParts` is returned in an Optional,
    otherwise an empty Optional is returned.

    ```java
    /**
     * Split single-String name into first and last name parts and validate parts,
     * e.g. "Meyer, Eric" is split into first: "Eric" and last name: "Meyer".
     */
    public Optional<NameParts> validateSplitName(String name) {
        ...
    }
    ```

The Javadoc of class *DataFactory* can be found at
[*DataFactory.html*](https://sgra64.github.io/se1-bestellsystem/c4-datafactory/se1.bestellsystem/datamodel/DataFactory.html)
defining validation rules.

The UML Class Diagram shows the new *DataFactory* class with inner class: *IdPool*
(*composition:* black diamond) and Record *NameParts:*

<img src="DataFactory.png" alt="drawing" width="800"/>


Create a class `DataFactory.java` in the package `datamodel` such that it:

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
     * from validated parameters. The <i>id</i> attribute is internally
     * provided. No object is created when arguments are not valid.
     * @param name single-String name parameter
     * @param contact contact parameter validated as an email address
     *  containing '@' or a phone number, invalid if null or empty
     * @return created {@link Customer} object with valid parameters or empty
     */
    public Optional<Customer> createCustomer(String name, String contact) {
        var nameParts = validateSplitName(name);
        if(nameParts.isPresent()) {
            long id = customerIdPool.next();
            var validContact = validateContact(contact);
            if(validContact.isPresent()) {
                // only create Customer when all conditions are met
                Customer c = new Customer(id, nameParts.get().first(), nameParts.get().last());
                c.addContact(validContact.get());
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
    ```

1. Supplement class `DataFactory.java` with code for the generation of unique *id's*:

    ```java
    /**
     * Random generator.
     */
    private final Random rand = new Random();

    /**
     * {@link IdPool} for {@link Customer} objects with 6-digit random numbers.
     */
    private final IdPool<Long> customerIdPool = new IdPool<>(
        () -> 100000L + rand.nextLong(900000L),
        Arrays.asList(  // initial Customer ids
            892474L, 643270L, 286516L, 412396L, 456454L, 651286L
        )
    );

    /**
     * Internal class to manage pool of unique {@code ids} of type {@code T}.
     * param T generic type of id
      * @param <T> The type of {@code ids} maintained in the pool.
      */
    private class IdPool<T> {

        /** supplier for id's of type {@code T} */
        private final Supplier<T> supplier;

        /** pool of used or available id */
        private final List<T> pool;

        /** [0..i-1]: used id, [i..cap-1]: available id */
        private int i=0;

        /** pool capacity */
        private int capacity;

        /**
         * Constructor of id pool of {@code T}.
         * @param supplier external supplier of id to fill the pool
         * @param initialIds id to initialize the pool
         */
        IdPool(Supplier<T> supplier, List<T> initialIds) {
            this.supplier = supplier;
            this.pool = new ArrayList<>(Optional.ofNullable(initialIds).orElse(List.of()));
            this.capacity = this.pool.size();
        }

        /**
         * Return next id from the pool, resupply pool if capacity is exceeded.
         * @return next id of type {@code T}
         */
        T next() {
            if(i >= capacity) {  // add 10 supplied ids to the pool
                capacity += Stream.generate(supplier)
                        .filter(n -> ! pool.contains(n))
                        .limit(10)
                        .peek(n -> pool.add(n))
                        .count();
            }
            return pool.get(i++);
        }
    }
    ```

1. Supplement class `DataFactory.java` with regular expressions for
    *name* and *contact* validation:

    ```java
    /**
     * Regular expression to validate a name or name parts. A valid name must
     * start with a letter, followed by a combination of letters, "-", "." or
     * white spaces. Valid names are: "E", "E.", "Eric", "Ulla-Nadine",
     * "Eric Meyer", "von-Blumenfeld". Names do not include numbers or other
     * special characters.
     * For the use of regular expressions, see
     * https://stackoverflow.com/questions/8204680/java-regex-email
     */
    private final Pattern nameRegex =
        Pattern.compile("^[A-Za-z][A-Za-z-\\s.]*$");

    /**
     * Regular expression to validate an email address.
     */
    private final Pattern emailRegex =
        Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z0-9_]+$", Pattern.CASE_INSENSITIVE);

    /**
     * Regular expression to validate a phone or fax number.
     */
    private final Pattern phoneRegex =
        Pattern.compile("^(phone:|fax:|\\+[0-9]+){0,1}\\s*[\\s0-9()][\\s0-9()-]*", Pattern.CASE_INSENSITIVE);
    ```

1. Supplement class `DataFactory.java` with code for contact validation:

    ```java
    /**
     * Validate contact for acceptable email address or phone number and
     * return contact or empty result.
     * <br>
     * Rules for validating a <i>email</i> addresses and <i>phone</i>
     * numbers are defined by regular expressions:
     * <ul>
     * <li> <i>email address:</i> {@code "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z0-9_]+$"}.
     * <li> <i>phone number:</i> {@code "^(phone:|fax:|\\+[0-9]+){0,1}\\s*[\\s0-9()][\\s0-9()-]*"}.
     * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]}
     *      and quotes {@code ["']} are trimmed from contacts before validation.
     * </ul>
     * @param contact contact to validate
     * @return possibly modified (e.g. dequoted, trimmed) valid contact or empty result
     */
    public Optional<String> validateContact(String contact) {
        if(contact != null) {
            var cont = trimQuotesAndWhiteSpaces(contact);
            final int minLength = 6;
            boolean valid = cont.length() >= minLength;
            if(valid && (
                emailRegex.matcher(cont).matches() ||
                phoneRegex.matcher(cont).matches()
            )) {
                return Optional.of(cont);
            }
        }
        return Optional.empty();
    }
    ```

1. Supplement class `DataFactory.java` with code for splitting single-String
    names (e.g. `"Eric Meyer"`) into first and last names (first: `"Eric"`,
    first: `"Meyer"`) with name validation:

    ```java
    /**
     * Validate name and return name or empty result. A valid name must
     * start with a letter, followed by a combination of letters, "-",
     * "." or white spaces. Valid names are: "E", "E.", "Eric",
     * "Ulla-Nadine", "Eric Meyer", "von-Blumenfeld".
     * Names do not include numbers or other special characters.
     * <br>
     * Rules for validating a <i>name</i> are defined by a regular expression:
     * <ul>
     * <li> {@code "^[A-Za-z][A-Za-z-\\s.]*$"}.
     * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]} and
     *      quotes {@code ["']} are trimmed from names before validation, e.g.
     *      {@code "  'Schulz-Müller, Tim Anton'  "}.
     * </ul>
     * <pre>
     * Examples:
     * +------------------------------------+---------------------------------------+
     * |name to validate                    |valid, possibly modified name          |
     * +------------------------------------+---------------------------------------+
     * |"Eric"                              |"Eric"                                 |
     * |"Ulla-Nadine"                       |"Ulla-Nadine"                          |
     * |"E", "E.", "von-A"                  |"E", "E.", "von-A"                     |
     * +------------------------------------+---------------------------------------+
     *
     * Trim leading, trailing white spaces and quotes:
     * +------------------------------------+---------------------------------------+
     * |"  Anne  "   (lead/trailing spaces) |"Anne"                                 |
     * |"  'Meyer'  "   (quotes)            |"Meyer"                                |
     * +------------------------------------+---------------------------------------+
     * </pre>
     * @param name name to validate
     * @param acceptEmptyName accept empty ("") name, e.g. as first name
     * @return valid, possibly modified (e.g. dequoted, trimmed) name or empty result
     */
    public Optional<String> validateName(String name, boolean acceptEmptyName) {
        if(name != null) {
            name = trimQuotesAndWhiteSpaces(name);
            if(nameRegex.matcher(name).matches() || (name.length()==0 && acceptEmptyName))
                return Optional.of(name);
        }
        return Optional.empty();
    }

    /**
     * Record of first and last name parts of a name.
     * @param first first name parts
     * @param last last name parts
     * @hidden exclude from documentation
     */
    public record NameParts(String first, String last) { }

    /**
     * Split single-String name into first and last name parts and
     * validate parts, e.g. "Meyer, Eric" is split into first: "Eric"
     * and last name: "Meyer".
     * <br>
     * Rules of splitting a single-String name into last- and first name parts:
     * <ul>
     * <li> if a name contains no seperators (comma or semicolon {@code [,;]}), the trailing
     *      consecutive part is the last name, all prior parts are first name parts, e.g.
     *      {@code "Tim Anton Schulz-Müller"}, splits into <i>first name:</i>
     *      {@code "Tim Anton"} and <i>last name:</i> {@code "Schulz-Müller"}.
     * <li> names with seperators (comma or semicolon {@code [,;]}) split into a last name
     *      part before the seperator and a first name part after the seperator, e.g.
     *      {@code "Schulz-Müller, Tim Anton"} splits into <i>first name:</i>
     *      {@code "Tim Anton"} and <i>last name:</i> {@code "Schulz-Müller"}.
     * <li> leading and trailing white spaces {@code [\s]}, commata {@code [,;]} and quotes
     *      {@code ["']} are trimmed from names before validation, e.g.
     *      {@code "  'Schulz-Müller, Tim Anton'  "}.
     * <li> interim white spaces between name parts are removed, e.g.
     *      {@code "Schulz-Müller, <white-spaces> Tim <white-spaces> Anton <white-spaces> "}.
     * </ul>
     * <pre>
     * Examples:
     * +------------------------------------+-------------------+-------------------+
     * |Single-String name                  |first name parts   |last name parts    |
     * +------------------------------------+-------------------+-------------------+
     * |"Eric Meyer"                        |"Eric"             |"Meyer"            |
     * |"Meyer, Anne"                       |"Anne"             |"Meyer"            |
     * |"Meyer; Anne"                       |"Anne"             |"Meyer"            |
     * |"Tim Schulz‐Mueller"                |"Tim"              |"Schulz‐Mueller"   |
     * |"Nadine Ulla Blumenfeld"            |"Nadine Ulla"      |"Blumenfeld"       |
     * |"Nadine‐Ulla Blumenfeld"            |"Nadine‐Ulla"      |"Blumenfeld"       |
     * |"Khaled Mohamed Abdelalim"          |"Khaled Mohamed"   |"Abdelalim"        |
     * +------------------------------------+-------------------+-------------------+
     *
     * Trim leading, trailing and interim white spaces and quotes:
     * +------------------------------------+-------------------+-------------------+
     * |" 'Eric Meyer'  "                   |"Eric"             |"Meyer"            |
     * |"Nadine     Ulla     Blumenfeld"    |"Nadine Ulla"      |"Blumenfeld"       |
     * +------------------------------------+-------------------+-------------------+
     * </pre>
     * @param name single-String name to split into first- and last name parts
     * @return record with valid, possibly modified (e.g. dequoted, trimmed) first and last name parts or empty result
     */
    public Optional<NameParts> validateSplitName(String name) {
        if(name != null && name.length() > 0) {
            String first="", last="";
            String[] spl1 = name.split("[,;]");
            if(spl1.length > 1) {
                // two-part name with last name first
                last = spl1[0];
                first = spl1[1];
            } else {
                // no separator [,;] -> split by white spaces;
                for(String s : name.split("\\s+")) {
                    if(last.length() > 0) {
                        // collect firstNames in order and lastName as last
                        first += (first.length()==0? "" : " ") + last;
                    }
                    last = s;
                }
            }
            var lastName = validateName(last, false);
            if(lastName.isPresent()) {
                var firstName = validateName(first, true);
                if(firstName.isPresent()) {
                    return Optional.of(new NameParts(firstName.get(), lastName.get()));
                }
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
    private String trimQuotesAndWhiteSpaces(String s) {
        s = s.replaceAll("^[\\s\"',;]*", "");   // trim leading white spaces[\s], commata[,;] and quotes['"]
        s = s.replaceAll( "[\\s\"',;]*$", "");  // trim trailing white spaces[\s], commata[,;] and quotes['"]
        s = s.replaceAll( "[\\s]+", " ");       // remove white spaces sequences, "Eric  Meyer" -> "Eric Meyer"
        return s;
    }
    ```

Class *DataFactory* should compile at this stage.


&nbsp;

## 2. Immutalize Class *Customer*

An *immutable* class *Customer* does not allow changes to attributes.
*DataFactory* is the only class that can create new *Customer*
objects from validated parameters.

Making class *Customer* immutable means:

1. Make all attributes: `private` and `final`.

1. Remove *setter()* methods.

1. Remove all constructors, except one used by *DataFactory* with
    visibility: `protected` to prevent creation of *Customer*
    objects from other packages.

The remaining class has no *setter()* methods and hence is called *immutable*,
which is shown in the UML class diagram by stereotype: `<<immutable>>`.

<img src="Customer.png" alt="drawing" width="400"/>


```java
/**
 * Immutable entity class representing a <i>Customer</i>, a person who creates
 * and holds (owns) orders in the system.
 * <br>
 * An <i>immutable</i> class does not allow changes to attributes.
 * {@link DataFactory} is the only class that creates {@link Customer}
 * objects from validated arguments.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public final class Customer {

    /**
     * Unique Customer id attribute. Must be {@code > 0}).
     */
    private final long id;

    /**
     * Customer surname attribute. Must not be {@code null} and not empty {@code ""}.
     */
    private final String lastName;

    /**
     * Customer none-surname parts. Must not be {@code null}, can be empty {@code ""}.
     */
    private final String firstName;

    /**
     * Contact information with multiple entries, e.g. email addresses
     * or phone numbers. The attribute is exposed to {@link DataFactory}
     * in the same package.
     */
    private final List<String> contacts = new ArrayList<>();


    /**
     * None-public constructor used by {@link DataFactory} preventing object
     * creation outside this package.
     * @param id customer identifier supplied by {@link DataFactory}
     * @param firstName first name attribute, must not be {@code null}, can be empty {@code ""}
     * @param lastName last name attribute, must not be {@code null} and not empty {@code ""}.
     * @throws IllegalArgumentException if {@code id} is negative, firstName is {@code null}
     *      or lastName is {@code null} or empty {@code ""}
     */
    protected Customer(long id, String firstName, String lastName) {
        if(id < 0L)
            throw new IllegalArgumentException("id negative");
        if(lastName==null || lastName.length()==0)
            throw new IllegalArgumentException("lastName null or empty");
        //
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
        DataFactory.getInstance().validateContact(contact)
            .filter(cont -> ! contacts.contains(contact))
            .ifPresent(c -> ((List<String>)contacts).add(c));
        return this;
    }

    /**
     * Delete the i-th contact with {@code i >= 0} and {@code i < contactsCount()}.
     * Method has no effect for {@code i} outside valid bounds.
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

## 3. Run Driver Code

Compare the previous method of creating *Customer* objects using *new*
in *Application_C2.java*:

```java
/**
 * Method of the {@link Runtime.Runnable} interface called on an instance
 * of this class created by the {@link Runtime}. Program execution starts here.
 * @param properties properties from the {@code application.properties} file
 * @param args arguments passed from the command line
 */
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
        .map(c -> print(c))     // .map(this::print)
        .forEach(System.out::println);
}
```

*Customer* objects can no longer be created directly with *new* causing
compile errors.

The new method of creating *Customer* objects through *DataFactory*
is shown below. Arguments of `createCustomer(...)` are now validated.
In case of invalid arguments, empty Optionals are returned:

```java
/**
 * Reference to {@link DataFactory} singleton.
 */
private final DataFactory dataFactory = DataFactory.getInstance();


/**
 * Method of the {@link Runtime.Runnable} interface called by {@link Runtime}.
 * Program execution starts here.
 * @param properties properties from the {@code application.properties} file
 * @param args arguments passed from the command line
 */
@Override
public void run(Properties properties, String[] args) {
    //
    List<Customer> customers = List.of(
        // 
        dataFactory.createCustomer("Eric Meyer", "eric98@yahoo.com")
            .map(c -> c.addContact("eric98@yahoo.com").addContact("(030) 3945-642298")),
        // 
        dataFactory.createCustomer("Anne Bayer", "anne24@yahoo.de")
            .map(c -> c.addContact("(030) 3481-23352").addContact("fax: (030)23451356")),
        // 
        dataFactory.createCustomer("Schulz-Mueller, Tim", "tim2346@gmx.de"),
        dataFactory.createCustomer("Blumenfeld, Nadine-Ulla", "+49 152-92454"),
        dataFactory.createCustomer("Khaled Saad Mohamed Abdelalim", "+49 1524-12948210"),
        // 
        // attempts to create Customer objects from invalid arguments
        // invalid email address, no object is created
        dataFactory.createCustomer("Mandy Mondschein", "locomandy<>gmx.de")
            .map(c -> c.addContact("+49 030-3956256")), // and no other (valid) contact is added
        dataFactory.createCustomer("", "nobody@gmx.de") // invalid name, no object is created
    //
    ).stream()
        .filter(c -> c.isPresent())
        .map(c -> c.get())
        .toList();

    // define table for output
    final TableFormatter tf = new TableFormatter(
            "| %8s ", "| %-32s", "| %-31s |")   // column specification
        .line()
        .row("Kund.-ID", "Name", "Kontakt")     // table header
        .line();

    // fill objects as rows into Customer table
    customers.stream()
        .forEach(c -> {
            String id=String.format("%d", c.getId());
            String name=fmtCustomerName(c, 0);
            String contact=fmtCustomerContacts(c, 1);
            // 
            tf.row(id, name, contact);  // write row into table
        });
    //
    tf.line();

    // print numbers of objects in collections
    System.out.println(String.format(
        "(%d) Customer objects built.\n---",
        customers.size()));

    // print Customer table
    System.out.println(tf.get().toString());
}
```

Fetch the complete driver class
[*Application_C4.java*](Application_C4.java)
and install in the `application` package.

Running the program outputs the table of *Customer* objects that could be
created from valid parameters.

```
(5) Customer objects built.
---
+----------+---------------------------------+---------------------------------+
| Kund.-ID | Name                            | Kontakt                         |
+----------+---------------------------------+---------------------------------+
|   892474 | Meyer, Eric                     | eric98@yahoo.com, (+1 contacts) |
|   643270 | Bayer, Anne                     | anne24@yahoo.de, (+2 contacts)  |
|   286516 | Schulz-Mueller, Tim             | tim2346@gmx.de                  |
|   412396 | Blumenfeld, Nadine-Ulla         | +49 152-92454                   |
|   456454 | Abdelalim, Khaled Saad Mohamed  | +49 1524-12948210               |
+----------+---------------------------------+---------------------------------+
```

Modify code to fix *"Mandy Mondschein's"* email to valid: `locomandy@gmx.de`
and print the *Customer* table sorted alphabetically by last name:

```
(6) Customer objects built.
---
+----------+---------------------------------+---------------------------------+
| Kund.-ID | Name                            | Kontakt                         |
+----------+---------------------------------+---------------------------------+
|   456454 | Abdelalim, Khaled Saad Mohamed  | +49 1524-12948210               |
|   643270 | Bayer, Anne                     | anne24@yahoo.de, (+2 contacts)  |
|   412396 | Blumenfeld, Nadine-Ulla         | +49 152-92454                   |
|   892474 | Meyer, Eric                     | eric98@yahoo.com, (+1 contacts) |
|   651286 | Mondschein, Mandy               | locomandy@gmx.de, (+1 contacts) |
|   286516 | Schulz-Mueller, Tim             | tim2346@gmx.de                  |
+----------+---------------------------------+---------------------------------+
```


&nbsp;

## 4. Update JUnit-Tests

*Customer* JUnit Tests no longer compile with the *DataFactory* changes.

Replace tests in: `tests/datamodel`. Download file:
[*c4-datafactory-tests.tar*](https://github.com/sgra64/se1-bestellsystem/blob/markup/c4-datafactory)
to the project directory and replace tests:

```sh
rm -rf tests/datamodel              # remove old tests
tar xvf c4-datafactory-tests.tar    # install new tests
```

Tests compile and run with the new code:

```sh
mk compile compile-tests run-tests
```
```
Test run finished after 486 ms
[        62 tests successful      ]
[         0 tests failed          ]
```


&nbsp;

## 5. Commit and Push Changes

With all tests passing, commit and push changes to your remote repository.

```sh
git commit -m "c4: DataFactory, immutable Customer class, tests update"
git push                        # push new commit to your upstream remote repository
```
