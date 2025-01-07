package application;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import application.Runtime.Bean;
import datamodel.*;
import datamodel.Pricing.*;


/**
 * Driver class for the <i>d34-ordertable</i> assignment. Class creates
 * {@link Customer}, {@link Article} and {@link Order} objects and prints
 * tables of objects.
 * <br>
 * Class implements the {@link Runtime.Runnable} interface.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
@Bean(priority=12)
public class Application_E12 implements Runtime.Runnable {

    /**
     * Reference to the {@link DataFactory} singleton.
     */
    private final DataFactory dataFactory = DataFactory.getInstance();

    /**
     * Map of {@link Customer} objects indexed by {@code id}.
     */
    private final Map<Long, Customer> customers = new HashMap<>();

    /**
     * Map of {@link Article} objects indexed by {@code id}.
     */
    private final Map<String, Article> articles = new HashMap<>();

    /**
     * Map of {@link Order} objects indexed by {@code id}.
     */
    private final Map<Long, Order> orders = new HashMap<>();


    /**
     * Default constructor fills maps 'customers' and 'articles'.
     */
    public Application_E12() { }

    /**
     * Method of the {@link Runtime.Runnable} interface called by {@link Runtime}.
     * Program execution starts here.
     * @param properties properties from the {@code application.properties} file
     * @param args arguments passed from the command line
     */
    @Override
    public void run(Properties properties, String[] args) {
        // 
        // create Customer objects and collect in 'customers' map
        List.of(
            dataFactory.createCustomer("Eric Meyer", "eric98@yahoo.com").map(c -> c.addContact("eric98@yahoo.com").addContact("(030) 3945-642298")),
            dataFactory.createCustomer("Anne Bayer", "anne24@yahoo.de").map(c -> c.addContact("(030) 3481-23352").addContact("fax: (030)23451356")),
            dataFactory.createCustomer("Schulz-Mueller, Tim", "tim2346@gmx.de"),
            dataFactory.createCustomer("Blumenfeld, Nadine-Ulla", "+49 152-92454"),
            dataFactory.createCustomer("Khaled Saad Mohamed Abdelalim", "+49 1524-12948210"),
            // 
            // invalid email address and name, no objects are created
            dataFactory.createCustomer("Mandy Mondschein", "locomandy<>gmx.de").map(c -> c.addContact("+49 030-3956256")),
            dataFactory.createCustomer("", "nobody@gmx.de")
        ).stream()
            // .filter(o -> o.isPresent())
            // .map(o -> o.get())
            // .flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
            .flatMap(Optional::stream)
            .forEach(customer -> customers.put(customer.getId(), customer));
        // 
        // create Article objects and collect in 'articles' map
        List.of(
            dataFactory.createArticle("Tasse",         299, PricingCategory.BasePricing),
            dataFactory.createArticle("Becher",        149, PricingCategory.BasePricing),
            dataFactory.createArticle("Kanne",        1999, PricingCategory.BasePricing),
            dataFactory.createArticle("Teller",        649, PricingCategory.BasePricing),
            dataFactory.createArticle("Buch 'Java'",  4990, PricingCategory.BasePricing, TAXRate.Reduced),
            dataFactory.createArticle("Buch 'UML'",   7995, PricingCategory.BasePricing, TAXRate.Reduced),
            dataFactory.createArticle("Pfanne",       4999, PricingCategory.BasePricing),
            dataFactory.createArticle("Fahrradhelm", 16900, PricingCategory.BasePricing),
            dataFactory.createArticle("Fahrradkarte",  695, PricingCategory.BasePricing, TAXRate.Reduced)
        ).stream()
            .flatMap(Optional::stream)
            .forEach(article -> articles.put(article.getId(), article));
        // 
        // 
        // create OrderBuilder with DataFactory for BasePricing
        final var orderBuilder = dataFactory.createOrderBuilder(
            PricingCategory.BasePricing,
            // 
            // provide function to look up customer in 'customers' map matching 'id', 'last' or 'first' name
            customerSpec -> findCustomerBySpec(customerSpec),
            // 
            // provide function to look up article in 'articles' map matching 'id' or 'description'
            articleSpec -> findArticleBySpec(articleSpec));
        // 
        // build Order objects using OrderBuilder and collect in 'orders' map
        List.of(
            // 
            // Eric's 1st order using buildOrder()
            orderBuilder.buildOrder("Eric", buildState -> buildState
                .item( 4, "Teller")         // + item: 4 Teller, 4x 6.49 €
                .item( 8, "Becher")         // + item: 8 Becher, 8x 1.49 €
                .item( 1, "SKU-425378")     // + item: 1 Buch "UML", 1x 79.95 €, 7% MwSt (5.23€)
                .item( 4, "Tasse")          // + item: 4 Tassen, 4x 2.99 €
            )
        ).stream()
            .flatMap(Optional::stream)
            .forEach(order -> orders.put(order.getId(), order));
        // 
        // 
        // print numbers of objects in collections
        System.out.println(String.format(
            "(%d) Customer objects built.\n" +
            "(%d) Article objects built.\n" +
            "(%d) Order objects built.\n---",
            customers.size(), articles.size(), orders.size()));

        // print Order table from values of 'orders' map
        // maintain sequence in which orders were created in DataFactory
        if(orders.size() > 1) {
            // ids as allocated from IdPool<Order> in DataFactory in buildOrders()
            var ids = Arrays.asList(
                8592356245L, 3563561357L, 5234968294L, 6135735635L, 6173043537L,
                7372561535L, 4450305661L
            );
            // select orders from 'orders' map in sequence of 'ids'
            var orderList = ids.stream()
                .map(id -> orders.get(id))
                .filter(o -> o != null)     // collect as mutable List
                .collect(Collectors.toCollection(ArrayList::new));
            //
            // add remaining orders
            orderList.addAll(orders.values().stream()
                .filter(o -> ! ids.contains(o.getId()))
                .toList()
            );
            StringBuilder sb3 = printOrders(orderList);
            System.out.println(sb3.insert(0, "Bestellungen:\n").toString());
        }
    }

    /**
     * Find {@link Customer} object in {@link customers} map by a specification,
     * which is the first match by {@code id} or in the {@code lastName} or
     * in the {@code firstName} attribute (in that order).
     * @param customerSpec specification of a customer by id or by name
     * @return {@link Customer} object or empty Optional
     */
    public Optional<Customer> findCustomerBySpec(String customerSpec) {
        return customerSpec==null? Optional.empty() :
            customers.values().stream()
                .filter(c -> Long.toString(c.getId()).equals(customerSpec) ||
                    c.getLastName().contains(customerSpec) ||
                    c.getFirstName().contains(customerSpec)
                )
                .findAny();
    }

    /**
     * Find {@link Article} object in {@link articles} map by a specification,
     * which is the first match by {@code id} or in the {@code description}
     * attribute (in that order).
     * @param articleSpec specification of an article by id or by description
     * @return {@link Article} object or empty Optional
     */
    public Optional<Article> findArticleBySpec(String articleSpec) {
        var article = Optional.ofNullable(articleSpec != null? articles.get(articleSpec) : null);
        if(article.isEmpty()) {
            article = articles.values().stream()
                .filter(a -> a.getDescription().contains(articleSpec))
                .findAny();
        }
        return article;
    }

    /**
     * Print objects of class {@link Order} as table row into a {@link StringBuilder}.
     * @param orders orders to print as row into table
     * @return StringBuilder with orders rendered in table format
     * @throws IllegalArgumentException with null arguments
     */
    public StringBuilder printOrders(Collection<Order> orders) {
        // print empty content
        return new StringBuilder();
    }
}