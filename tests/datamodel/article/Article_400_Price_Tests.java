package tests.datamodel.article;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import datamodel.*;

class Article_400_Price_Tests {
    private final DataFactory factory = DataFactory.getInstance();

    @Test
    void test400_ArticlePriceRegularCases() {
        var article = factory.createArticle(
            "Hut", 10000, Pricing.PricingCategory.BasePricing).get();
        assertEquals(10000, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article));
    }

    @Test
    void test401_ArticlePriceRegularCases() {
        var article = factory.createArticle(
            "Rad", 4999, Pricing.PricingCategory.BasePricing).get();
        assertEquals(4999, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article));
    }

    @Test
    void test410_ArticlePriceCornerCases() {
        var article = factory.createArticle(
            "Hut", 1, Pricing.PricingCategory.BasePricing).get();
        assertEquals(1, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article));
    }

    @Test
    void test411_ArticlePriceCornerCases() {
        var article = factory.createArticle(
            "Hut", 0, Pricing.PricingCategory.BasePricing).get();
        assertEquals(0, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article));
    }

    @Test
    void test412_ArticlePriceCornerCases() {
        var article = factory.createArticle(
            "Hut", 9999999999L, Pricing.PricingCategory.BasePricing).get();
        assertEquals(9999999999L, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article));
    }

    @Test
    void test420_ArticlePriceExceptionCases() {
        assertFalse(factory.createArticle(
            "Hut", -1, Pricing.PricingCategory.BasePricing).isPresent());
    }

    @Test
    void test421_ArticlePriceExceptionCases() {
        assertFalse(factory.createArticle(
            "Hut", -10000, Pricing.PricingCategory.BasePricing).isPresent());
    }
} 