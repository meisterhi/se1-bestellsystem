package tests.datamodel.article;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import datamodel.*;

class Article_300_Description_Tests {
    private final DataFactory factory = DataFactory.getInstance();

    @Test
    void test300_ArticleDescriptionRegularCases() {
        var article = factory.createArticle(
            "Hut", 10000, Pricing.PricingCategory.BasePricing).get();
        assertEquals("Hut", article.getDescription());
    }

    @Test
    void test301_ArticleDescriptionRegularCases() {
        var article = factory.createArticle(
            "Bohrhammer", 4999, Pricing.PricingCategory.BasePricing).get();
        assertEquals("Bohrhammer", article.getDescription());
    }

    @Test
    void test310_ArticleDescriptionCornerCases() {
        var article = factory.createArticle(
            "X", 10000, Pricing.PricingCategory.BasePricing).get();
        assertEquals("X", article.getDescription());
    }

    @Test
    void test311_ArticleDescriptionCornerCases() {
        var article = factory.createArticle(
            "Blaue Wintermütze passend zum hellgrünen Pullover",
            10000, Pricing.PricingCategory.BasePricing).get();
        assertEquals("Blaue Wintermütze passend zum hellgrünen Pullover", 
            article.getDescription());
    }

    @Test
    void test320_ArticleDescriptionExceptionCases() {
        assertFalse(factory.createArticle(
            "", 10000, Pricing.PricingCategory.BasePricing).isPresent());
    }

    @Test
    void test321_ArticleDescriptionExceptionCases() {
        assertFalse(factory.createArticle(
            null, 10000, Pricing.PricingCategory.BasePricing).isPresent());
    }
} 