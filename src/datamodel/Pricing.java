package datamodel;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles pricing-related functionality including tax rates and currency information.
 * Class provides pricing models with country-specific properties including
 * currency and tax rates.
 * 
 */
public class Pricing {

    /**
     * Country for this pricing model.
     */
    private final Country country;

    /**
     * Currency for this pricing model.
     */
    private final Currency currency;

    /**
     * Countries supported by the pricing models.
     */
    public enum Country {
        /** Germany with country code "DE". */
        Germany("DE"),
        /** Switzerland with country code "CH". */
        Switzerland("CH"),
        /** United Kingdom with country code "UK". */
        UnitedKingdom("UK");

        private final String code;
        Country(String code) { this.code = code; }
        public String code() { return code; }
    }

    /**
     * Currencies supported by the pricing models with their codes and Unicode symbols.
     */
    public enum Currency {
        /** Euro with code "EUR" and symbol "€". */
        Euro("EUR", "€"),
        /** Swiss Franc with code "CHF". */
        SwissFranc("CHF", "CHF"),
        /** British Pound with code "GBP" and symbol "£". */
        BritishPound("GBP", "£");

        private final String code;
        private final String unicode;
        Currency(String code, String unicode) {
            this.code = code;
            this.unicode = unicode;
        }
        public String code() { return code; }
        public String unicode() { return unicode; }
    }

    /**
     * Available pricing categories with their specific pricing models:
     * <pre>
     * BasePricing        - Standard German pricing with EUR
     * BlackFridayPricing - German pricing with 20% discount
     * SwissPricing       - Swiss pricing with CHF
     * UKPricing          - UK pricing with GBP
     * </pre>
     */
    public enum PricingCategory {
        /** Standard German pricing model. */
        BasePricing(new Pricing(Country.Germany, Currency.Euro)),
        /** Black Friday sales pricing with 20% discount. */
        BlackFridayPricing(new Pricing(Country.Germany, Currency.Euro)),
        /** Swiss pricing model using Swiss Francs. */
        SwissPricing(new Pricing(Country.Switzerland, Currency.SwissFranc)),
        /** UK pricing model using British Pounds. */
        UKPricing(new Pricing(Country.UnitedKingdom, Currency.BritishPound));

        private final Pricing pricing;
        PricingCategory(Pricing pricing) { this.pricing = pricing; }
        public Pricing pricing() { return pricing; }
    }

    /**
     * Tax rates that can be applied to articles:
     * <pre>
     * Regular - standard tax rate
     * Reduced - reduced tax rate for specific items
     * Special - special tax rate (Switzerland)
     * Excempt - tax exempt items
     * </pre>
     */
    public enum TAXRate {
        /** Normal tax rate. */
        Regular,
        /** Reduced tax rate for books, food, etc. */
        Reduced,
        /** Special tax rate (Switzerland). */
        Special,
        /** Tax excempted. */
        Excempt
    }

    /**
     * Creates a new Pricing instance for the specified country and currency.
     * @param country country for this pricing model
     * @param currency currency for this pricing model
     */
    protected Pricing(Country country, Currency currency) {
        this.country = country;
        this.currency = currency;
    }

    /**
     * Adjusts the price by the given factor.
     * @param price price to adjust
     * @param factor factor to multiply with
     * @return adjusted price
     */
    private long adjustPrice(long price, double factor) {
        return Math.round(price * factor);
    }

    /**
     * Converts the price from the current country to the specified target country.
     * @param targetCountry the country to convert the price to
     * @param price the price to convert
     * @return the converted price
     */
    public long convertPriceToCountry(Country targetCountry, long price) {
        // Assuming there's a predefined conversion rate map
        Map<Country, Map<Country, Double>> conversionRates = getConversionRates();
        if (!conversionRates.containsKey(country) || !conversionRates.get(country).containsKey(targetCountry)) {
            throw new UnsupportedOperationException("Conversion rate not available for " + country + " to " + targetCountry);
        }
        double conversionFactor = conversionRates.get(country).get(targetCountry);
        return adjustPrice(price, conversionFactor);
    }

    /**
     * Returns a map of conversion rates between countries.
     * This is a placeholder for actual conversion rate logic.
     * @return a map of conversion rates
     */
    private Map<Country, Map<Country, Double>> getConversionRates() {
  
        Map<Country, Map<Country, Double>> conversionRates = new HashMap<>();
        conversionRates.put(Country.Germany, new HashMap<>());
        conversionRates.get(Country.Germany).put(Country.UnitedKingdom, 0.85);
        conversionRates.get(Country.Germany).put(Country.Switzerland, 1.15);
        // Add more conversion rates as needed
        return conversionRates;
    }

    /**
     * Returns the tax rate as a percentage for the given article.
     * <pre>
     * Germany:     regular: 19%, reduced: 7%
     * Switzerland: regular: 8.1%, reduced: 2.6%, special: 3.8%
     * UK:          regular: 20%, reduced: 5%, exempt: 0%
     * </pre>
     * @param article subject of tax rate request
     * @return tax rate as percent value that applies to article
     */
    public double taxRateAsPercent(Article article) {
        if (article == null) return 0.0;
        TAXRate rate = taxRate(article);
        
        switch (country) {
            case Germany:
                return rate == TAXRate.Regular ? 19.0 : 
                       rate == TAXRate.Reduced ? 7.0 : 0.0;
            case Switzerland:
                return rate == TAXRate.Regular ? 8.1 : 
                       rate == TAXRate.Reduced ? 2.6 : 
                       rate == TAXRate.Special ? 3.8 : 0.0;
            case UnitedKingdom:
                return rate == TAXRate.Regular ? 20.0 : 
                       rate == TAXRate.Reduced ? 5.0 : 
                       rate == TAXRate.Excempt ? 0.0 : 0.0;
            default:
                return 0.0;
        }
    }

    /**
     * Returns the default currency for this pricing.
     * @return the currency used in this pricing model
     */
    public Currency currency() {
        return currency;
    }

    /**
     * Returns the unit price for the specified article.
     * @param article article to get the price for
     * @return the unit price of the article
     */
    public long unitPrice(Article article) {
        return article.getUnitPrice();
    }

    /**
     * Returns the tax rate for the specified article.
     * @param article article to get the tax rate for
     * @return the tax rate applicable to the article
     */
    public TAXRate taxRate(Article article) {
        return article.getTaxRate();
    }
} 