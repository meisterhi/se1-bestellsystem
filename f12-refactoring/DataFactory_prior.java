package components;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import datamodel.Customer;
import datamodel.Article;
import datamodel.Order;
import datamodel.Pricing.PricingCategory;
import datamodel.Pricing.TAXRate;
import datamodel.ProtectedFactory;
import datamodel.ProtectedFactory.ArticleCreator;
import datamodel.ProtectedFactory.CustomerCreator;
import datamodel.ProtectedFactory.OrderCreator;


/**
 * <i>Factory</i> class with <i>create()-</i>methods to instantiate objects
 * of classes of the <i>datamodel</i> package from validated parameters.
 * Objects are only created from valid parameters (e.g. names, email or
 * phone contacts).
 * <br>
 * <i>DataFactory</i> itself exists as a <i>("lazy") singleton</i> instance.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class DataFactory {

    /**
     * Static {@link DataFactory} <i>Singleton</i> instance (<i>lazy</i> pattern).
     */
    private static DataFactory dataFactory = null;

    /**
     * Creator functions injected by {@link ProtectedFactory}
     */
    private Optional<CustomerCreator> customerCreator = Optional.empty();
    private Optional<ArticleCreator> articleCreator = Optional.empty();
    private Optional<OrderCreator> orderCreator = Optional.empty();

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

    /*
     * {@link IdPool} for {@link Article} objects with 6-digit numbers prefixed
     * with "SKU-" (stock-keeping unit).
     */
    private IdPool<String> articleIdPool = new IdPool<>(
        () -> String.format("SKU-%d", 100000 + rand.nextInt(900000)),
        Arrays.asList(  // initial Article ids
            "SKU-458362", "SKU-693856", "SKU-518957", "SKU-638035", "SKU-278530",
            "SKU-425378", "SKU-300926", "SKU-663942", "SKU-583978"
        )
    );

    /*
     * {@link IdPool} for {@link Customer} objects with 10-digit random numbers.
     */
    private IdPool<Long> orderIdPool = new IdPool<>(
        () -> 1000000000L + rand.nextLong(9000000000L),
        Arrays.asList(  // initial Order ids
            8592356245L, 3563561357L, 5234968294L, 6135735635L, 6173043537L,
            7372561535L, 4450305661L
        )
    );

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

    /*
     * Lower bound of valid order creation date: {@code "Jan 01, 2020 00:00:00"}
     */
    private final LocalDateTime lowerOrderCreationDate = LocalDateTime.of(2020,1,1,0,0,0);

    /*
     * Upper bound of valid order creation date: {@code "Dec 31, 2099 23:59:59"}
     */
    private final LocalDateTime upperOrderCreationDate = LocalDateTime.of(2099,12,31,23,59,59);


    /**
     * Private constructor as part of the <i>Singleton</i> pattern.
     */
    private DataFactory() { }

    /**
     * Static accessor method to {@link DataFactory} <i>Singleton</i> instance.
     * @return singleton {@link DataFactory} instance
     */
    public static DataFactory getInstance() {
        if(dataFactory==null) {
            dataFactory = new DataFactory();
            ProtectedFactory.inject(dataFactory, (c, a, o) -> {
                dataFactory.customerCreator = Optional.of(c);
                dataFactory.articleCreator = Optional.of(a);
                dataFactory.orderCreator = Optional.of(o);
            });
        }
        return dataFactory;
    }

    /**
     * <i>Factory</i> method to create an object of class {@link Customer}
     * from validated parameters. The <i>id</i> attribute is internally
     * provided. No object is created when arguments are not valid.
     * @param name single-String name parameter, invalid if null or empty,
     *          example: "Eric Meyer" or "Meyer, Eric"
     * @param contact contact parameter validated as an email address
     *          containing '@' or a phone number, invalid if null or empty
     * @return created {@link Customer} object with valid parameters or empty
     */
    public Optional<Customer> createCustomer(String name, String contact) {
        var nameParts = validateSplitName(name);
        if(nameParts.isPresent()) {
            long id = customerIdPool.next();
            var validContact = validateContact(contact);
            if(validContact.isPresent()) {
                // only create Customer when all conditions are met
                // 
                // replace constructor invocation with calling the creator function:
                // Customer c = new Customer(id, nameParts.get().first(), nameParts.get().last());
                if(customerCreator.isPresent()) {
                    Customer c = customerCreator.get().createCustomer(id, nameParts.get().first(), nameParts.get().last());
                    c.addContact(validContact.get());
                    return Optional.of(c);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * <i>Factory</i> method to create an object of class {@link Article}
     * from validated arguments. The <i>id</i> attribute is internally
     * provided. No object is created when arguments are not valid.
     * @param description brief article description, e.g. "Tasse"
     * @param unitPrice price of one unit (in cent)
     * @param pricingCategory pricing table associated with this article
     * @param taxRate rate according to {@link TAXRate} ({@code TAXRate.Regular} is default)
     * @return {@link Article} object created from valid arguments or empty
     */
    public Optional<Article> createArticle(
        String description,
        long unitPrice,
        PricingCategory pricingCategory,
        TAXRate... taxRate
    ) {
        TAXRate tax_rate = taxRate.length > 0? taxRate[0] : TAXRate.Regular;
        boolean valid = description != null && description.length() > 0;
        valid = valid && pricingCategory != null && tax_rate != null;
        valid = valid && unitPrice >= 0L;
        if(valid && articleCreator.isPresent()) {
            // replace constructor invocation with calling the creator function:
            String id = articleIdPool.next();
            // Article article = new Article(id, description);
            Article article = articleCreator.get().createArticle(id, description);
            var pricing = pricingCategory.pricing();
            pricing.put(article, unitPrice, tax_rate);
            return Optional.of(article);
        }
        return Optional.empty();
    }

    /**
     * Create {@link OrderBuilder} object used to build {@link Order} objects.
     * @param pricingCategory pricing category used to build order
     * @param customerFetcher function to fetch {@link Customer} object from spec-String matching customer id, first or last name.
     * @param articleFetcher function to fetch {@link Article} object from spec-String matching article id or description.
     * @return {@link OrderBuilder} object used to build {@link Order} objects
     * @throws IllegalArgumentException thrown by {@link OrderBuilder} constructor if arguments are null
     */
    public OrderBuilder createOrderBuilder(
        PricingCategory pricingCategory,
        Function<String, Optional<Customer>> customerFetcher,
        Function<String, Optional<Article>> articleFetcher
    ) {
        return new OrderBuilder(this, pricingCategory, customerFetcher, articleFetcher);
    }

    /**
     * Non-public <i>Factory</i> method to create an object of class {@link Order}
     * from validated arguments. The <i>id</i> attribute is internally
     * provided. No object is created when arguments are not valid.
     * Method is used by {@link OrderBuilder} and no longer publicly exposed.
     * @param category pricing category that applies to {@link Order}
     * @param customer owner of the order (foreign-key relaion)
     * @return {@link Order} object created from valid arguments or empty
     */
    Optional<Order> createOrder(PricingCategory category, Optional<Customer> customer) {
        if(category==null)
            throw new IllegalArgumentException("argument category: null");
        if(customer==null)
            throw new IllegalArgumentException("argument customer: null");
        //
        if(customer.isPresent() && orderCreator.isPresent()) {
            LocalDateTime created = LocalDateTime.now();
            // var order = new Order(orderIdPool.next(), customer.get(), category.pricing(), created);
            Order order = orderCreator.get().createOrder(orderIdPool.next(), customer.get(), category.pricing(), created);
            return Optional.of(order);
        } else {
            return Optional.empty();
        }
    }

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

    /**
     * Validate order creation date against bounds {@link lowerOrderCreationDate}
     * ({@code "Jan 01, 2020 00:00"}) and {@link upperOrderCreationDate}
     * ({@code "Dec 31, 2099 23:59"}).
     * @param date date to validate
     * @return validated date or empty result
     */
    public Optional<LocalDateTime> validateOrderCreationDate(LocalDateTime date) {
        if(date != null) {
            boolean valid = date.isAfter(lowerOrderCreationDate) && date.isBefore(upperOrderCreationDate);
            // second test to match bounds is only performed when first test was invalid
            valid = valid || date.isEqual(lowerOrderCreationDate) || date.isEqual(upperOrderCreationDate);
            if(valid) {
                return Optional.of(date);
            }
        }
        return Optional.empty();
    }

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
}