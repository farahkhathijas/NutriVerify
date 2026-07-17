package com.nutriverify.repository;

import com.nutriverify.config.AppConfig;
import com.nutriverify.exception.DatasetLoadException;
import com.nutriverify.model.Ingredient;
import com.nutriverify.model.IngredientCategory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Loads the bundled ingredient dataset from classpath resources.
 */
public class IngredientDatasetRepository {
    public List<Ingredient> loadIngredients() throws DatasetLoadException {
        List<Ingredient> ingredients = new ArrayList<>();
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("ingredients.csv")) {
            if (stream == null) {
                throw new DatasetLoadException("Unable to find ingredient dataset at " + AppConfig.DATASET_PATH);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                boolean header = true;
                while ((line = reader.readLine()) != null) {
                    if (header) {
                        header = false;
                        continue;
                    }
                    String[] parts = line.split(",");
                    if (parts.length < 3) {
                        continue;
                    }
                    String name = parts[0].trim();
                    IngredientCategory category = parseCategory(parts[1].trim());
                    String note = parts[2].trim();
                    ingredients.add(new Ingredient(name, category, note));
                }
            }
        } catch (IOException ex) {
            throw new DatasetLoadException("Unable to read ingredient dataset: " + ex.getMessage());
        }
        return ingredients;
    }

    public List<Ingredient> search(String query) throws DatasetLoadException {
        List<Ingredient> matches = new ArrayList<>();
        String needle = query.toLowerCase(Locale.ROOT);
        for (Ingredient ingredient : loadIngredients()) {
            if (ingredient.getName().toLowerCase(Locale.ROOT).contains(needle)) {
                matches.add(ingredient);
            }
        }
        return matches;
    }

    private IngredientCategory parseCategory(String raw) {
        return switch (raw.toUpperCase(Locale.ROOT)) {
            case "NATURAL" -> IngredientCategory.NATURAL;
            case "ARTIFICIAL" -> IngredientCategory.ARTIFICIAL;
            case "PRESERVATIVE" -> IngredientCategory.PRESERVATIVE;
            case "SWEETENER" -> IngredientCategory.SWEETENER;
            case "ALLERGEN" -> IngredientCategory.ALLERGEN;
            case "CONTROVERSIAL" -> IngredientCategory.CONTROVERSIAL;
            case "GMO_DERIVED" -> IngredientCategory.GMO_DERIVED;
            default -> IngredientCategory.UNKNOWN;
        };
    }
}
