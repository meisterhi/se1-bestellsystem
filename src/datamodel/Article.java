package datamodel;

/**
 * Represents an article/product in the system.
 */
public class Article {
    private final String id;
    private final String description;
    private final long unitPrice;
    private final Pricing.TAXRate taxRate;
    private final Pricing.PricingCategory category;


    /**
     * Create Emyt Arucle for testing only
     */
    public Article(){
        this.id = null;
        this.description = null;
        this.unitPrice = 0;
        this.category = null;
        this.taxRate = null;
    }


    /**
     * Creates a new Article with the specified attributes.
     */
    Article(String id, String description, long unitPrice, Pricing.PricingCategory category, Pricing.TAXRate taxRate) {
        this.id = id;
        this.description = description;
        this.unitPrice = unitPrice;
        this.category = category;
        this.taxRate = taxRate;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public long getUnitPrice() { return unitPrice; }
    public Pricing.TAXRate getTaxRate() { return taxRate; }
    public Pricing.PricingCategory getCategory() { return category; }
} 