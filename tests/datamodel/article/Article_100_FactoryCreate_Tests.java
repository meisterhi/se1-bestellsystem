package tests.datamodel.article;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import datamodel.*;

class Article_100_FactoryCreate_Tests {
    private final DataFactory factory = DataFactory.getInstance();

    @Test
    void test100_RegularArticleCreation() {
        var article = factory.createArticle(
            "Laptop", 125000, Pricing.PricingCategory.BasePricing);
        
        assertTrue(article.isPresent());
        assertEquals("Laptop", article.get().getDescription());
        assertEquals(125000, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article.get()));
        assertEquals(Pricing.TAXRate.Regular, 
            Pricing.PricingCategory.BasePricing.pricing().taxRate(article.get()));
    }

    @Test
    void test101_RegularArticleCreation() {
        var article = factory.createArticle(
            "Sneaker", 9999, Pricing.PricingCategory.BasePricing);
        
        assertTrue(article.isPresent());
        assertEquals("Sneaker", article.get().getDescription());
        assertEquals(9999, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article.get()));
    }

    @Test
    void test102_RegularArticleCreation() {
        var article = factory.createArticle(
            "Butter", 239, Pricing.PricingCategory.BasePricing, Pricing.TAXRate.Reduced);
        
        assertTrue(article.isPresent());
        assertEquals("Butter", article.get().getDescription());
        assertEquals(239, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article.get()));
        assertEquals(Pricing.TAXRate.Reduced, 
            Pricing.PricingCategory.BasePricing.pricing().taxRate(article.get()));
    }

    @Test
    void test110_ArticleCreationCornerCases() {
        var article = factory.createArticle(
            "X", 1, Pricing.PricingCategory.BasePricing);
        
        assertTrue(article.isPresent());
        assertEquals("X", article.get().getDescription());
        assertEquals(1, 
            Pricing.PricingCategory.BasePricing.pricing().unitPrice(article.get()));
    }
} 