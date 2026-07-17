package com.nutriverify.model;

/**
 * A single ingredient extracted from a food label.
 */
public class Ingredient {
    private final String name;
    private final IngredientCategory category;
    private final String note;

    public Ingredient(String name, IngredientCategory category, String note) {
        this.name = name;
        this.category = category;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public IngredientCategory getCategory() {
        return category;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return name + " [" + category + "]";
    }
}
