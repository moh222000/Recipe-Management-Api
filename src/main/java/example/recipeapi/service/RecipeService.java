package example.recipeapi.service;

import example.recipeapi.model.Recipe;
import example.recipeapi.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    // Existing method to get all recipes as a List
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    // NEW METHOD: Get all recipes with pagination support
    public Page<Recipe> getAllRecipesPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return recipeRepository.findAll(pageable);
    }

    public Recipe createRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public Optional<Recipe> getRecipeById(String id) {
        return recipeRepository.findById(id);
    }

    public Recipe updateRecipe(String id, Recipe recipeDetails) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            Recipe existingRecipe = recipe.get();
            existingRecipe.setTitle(recipeDetails.getTitle());
            existingRecipe.setCategory(recipeDetails.getCategory());
            existingRecipe.setCookingTime(recipeDetails.getCookingTime());
            existingRecipe.setIngredients(recipeDetails.getIngredients());
            existingRecipe.setInstructions(recipeDetails.getInstructions());
            return recipeRepository.save(existingRecipe);
        } else {
            throw new RuntimeException("Recipe not found with id " + id);
        }
    }

    public void deleteRecipe(String id) {
        recipeRepository.deleteById(id);
    }
    
    // These methods are already implemented in your service for other endpoints
    public Page<Recipe> getFilteredRecipes(String title, String category, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (!title.isEmpty() && !category.isEmpty()) {
            return recipeRepository.findByTitleContainingAndCategoryContaining(title, category, pageable);
        } else if (!title.isEmpty()) {
            return recipeRepository.findByTitleContaining(title, pageable);
        } else if (!category.isEmpty()) {
            return recipeRepository.findByCategoryContaining(category, pageable);
        } else {
            return recipeRepository.findAll(pageable);
        }
    }
    
    public Page<Recipe> searchByIngredient(String ingredient, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return recipeRepository.findByIngredientsContaining(ingredient, pageable);
    }
    
    public Page<Recipe> searchByMaxCookingTime(int maxTime, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return recipeRepository.findByCookingTimeLessThanEqual(maxTime, pageable);
    }
    
    public Page<Recipe> advancedSearch(String title, String category, int maxCookingTime, String ingredient, 
                                      int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        if (!ingredient.isEmpty()) {
            return recipeRepository.findAdvancedWithIngredient(
                title, category, maxCookingTime, ingredient, pageable);
        } else {
            return recipeRepository.findAdvanced(title, category, maxCookingTime, pageable);
        }
    }
}
    