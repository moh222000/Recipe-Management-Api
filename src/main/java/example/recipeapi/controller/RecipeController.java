package example.recipeapi.controller;

import example.recipeapi.model.Recipe;
import example.recipeapi.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    // Create a new recipe
    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe newRecipe = recipeService.createRecipe(recipe);
        return new ResponseEntity<>(newRecipe, HttpStatus.CREATED);
    }

    // Get all recipes - MODIFIED: Now supports pagination
    @GetMapping
    public ResponseEntity<Page<Recipe>> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<Recipe> recipes = recipeService.getAllRecipesPaginated(page, size, sortBy, direction);
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    // Get a specific recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable String id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        return recipe.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                     .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update an existing recipe
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable String id, @RequestBody Recipe recipeDetails) {
        try {
            Recipe updatedRecipe = recipeService.updateRecipe(id, recipeDetails);
            return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a specific recipe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        recipeService.deleteRecipe(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/filter")
    public ResponseEntity<Page<Recipe>> getFilteredRecipes(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Page<Recipe> result = recipeService.getFilteredRecipes(title, category, page, size, sortBy, direction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/ingredient")
    public ResponseEntity<Page<Recipe>> searchByIngredient(
            @RequestParam String ingredient,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<Recipe> result = recipeService.searchByIngredient(ingredient, page, size, sortBy, direction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/time")
    public ResponseEntity<Page<Recipe>> searchByMaxCookingTime(
            @RequestParam int maxTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "cookingTime") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<Recipe> result = recipeService.searchByMaxCookingTime(maxTime, page, size, sortBy, direction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<Page<Recipe>> advancedSearch(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "999") int maxCookingTime,
            @RequestParam(defaultValue = "") String ingredient,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<Recipe> result = recipeService.advancedSearch(title, category, maxCookingTime, ingredient, page, size, sortBy, direction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
