package tests.datamodel.article;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import datamodel.*;

class Article_200_Id_Tests {
    private final DataFactory factory = DataFactory.getInstance();
    private final Pattern idPattern = Pattern.compile("SKU-[1-9][0-9]{5}");

    @Test
    void test200_ArticleIdPattern() {
        var article = factory.createArticle(
            "Test", 100, Pricing.PricingCategory.BasePricing).get();
        String id = article.getId();
        System.out.println("\n\n\n" + id + "\n\n\n");
        assertTrue(idPattern.matcher(id).matches(),
            "Article ID should match pattern SKU-XXXXXX where X is digit and first digit is not 0 " + id);
        int num = Integer.parseInt(id.substring(4));
        assertTrue(num >= 100000,
            "Article ID number should be >= 100000 " + id);
    }

    @Test
    void test210_ArticleIdUniqueness() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            var article = factory.createArticle(
                "Test" + i, 100, Pricing.PricingCategory.BasePricing).get();
            String id = article.getId();
            assertTrue(idPattern.matcher(id).matches(),
                "Each ID should match pattern SKU-XXXXXX");
            assertTrue(ids.add(id),
                "Each ID should be unique");
        }
    }
} 

