package com.nutriverify.model;

/**
 * Aggregated risk information for a specific ingredient category.
 */
public class IngredientRisk {
    private final IngredientCategory category;
    private final long count;

    public IngredientRisk(IngredientCategory category, long count) {
        this.category = category;
        this.count = count;
    }

    public IngredientCategory getCategory() {
        return category;
    }

    public long getCount() {
        return count;
    }
}
