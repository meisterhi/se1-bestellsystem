package tests.datamodel.article;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import datamodel.*;

class Article_500_PriceVAT_Tests {
    private final DataFactory factory = DataFactory.getInstance();

    @Test
    void test500_ArticleVATRateRegularCases() {
        var article = factory.createArticle(
            "Hut", 10000, Pricing.PricingCategory.BasePricing, Pricing.TAXRate.Regular).get();
        assertEquals(Pricing.TAXRate.Regular, 
            Pricing.PricingCategory.BasePricing.pricing().taxRate(article));
    }

    @Test
    void test501_ArticleVATRateRegularCases() {
        var article = factory.createArticle(
            "Orange", 49, Pricing.PricingCategory.BasePricing, Pricing.TAXRate.Reduced).get();
        assertEquals(Pricing.TAXRate.Reduced, 
            Pricing.PricingCategory.BasePricing.pricing().taxRate(article));
    }

    @Test
    void test502_ArticleVATRateRegularCases() {
        var article = factory.createArticle(
            "Whiskey", 49, Pricing.PricingCategory.BasePricing, Pricing.TAXRate.Excempt).get();
        assertEquals(Pricing.TAXRate.Excempt, 
            Pricing.PricingCategory.BasePricing.pricing().taxRate(article));
    }
}