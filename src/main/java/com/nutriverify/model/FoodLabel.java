package com.nutriverify.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A food label captured from user input or a sample dataset.
 */
public class FoodLabel {
    private final String productName;
    private final String brand;
    private final String servingSize;
    private final List<Ingredient> ingredients;
    private final List<Claim> claims;
    private final double calories;
    private final double fat;
    private final double sugar;
    private final double sodium;
    private final double protein;
    private final double carbs;
    private final double fiber;
    private final LocalDateTime analyzedAt;

    public FoodLabel(String productName, String brand, String servingSize, List<Ingredient> ingredients,
                     List<Claim> claims, double calories, double fat, double sugar, double sodium,
                     double protein, double carbs, double fiber) {
        this(productName, brand, servingSize, ingredients, claims, calories, fat, sugar, sodium, protein, carbs, fiber, LocalDateTime.now());
    }

    public FoodLabel(String productName, String brand, String servingSize, List<Ingredient> ingredients,
                     List<Claim> claims, double calories, double fat, double sugar, double sodium,
                     double protein, double carbs, double fiber, LocalDateTime analyzedAt) {
        this.productName = productName;
        this.brand = brand;
        this.servingSize = servingSize;
        this.ingredients = new ArrayList<>(ingredients);
        this.claims = new ArrayList<>(claims);
        this.calories = calories;
        this.fat = fat;
        this.sugar = sugar;
        this.sodium = sodium;
        this.protein = protein;
        this.carbs = carbs;
        this.fiber = fiber;
        this.analyzedAt = analyzedAt;
    }

    public String getProductName() { return productName; }
    public String getBrand() { return brand; }
    public String getServingSize() { return servingSize; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public List<Claim> getClaims() { return claims; }
    public double getCalories() { return calories; }
    public double getFat() { return fat; }
    public double getSugar() { return sugar; }
    public double getSodium() { return sodium; }
    public double getProtein() { return protein; }
    public double getCarbs() { return carbs; }
    public double getFiber() { return fiber; }
    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
}
