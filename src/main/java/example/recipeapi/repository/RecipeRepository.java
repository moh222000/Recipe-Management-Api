package example.recipeapi.repository;

import example.recipeapi.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    
    // Basic filtered searches with pagination
    Page<Recipe> findByTitleContaining(String title, Pageable pageable);
    Page<Recipe> findByCategoryContaining(String category, Pageable pageable);
    Page<Recipe> findByTitleContainingAndCategoryContaining(String title, String category, Pageable pageable);
    
    // Ingredient search
    Page<Recipe> findByIngredientsContaining(String ingredient, Pageable pageable);
    
    // Cooking time search
    Page<Recipe> findByCookingTimeLessThanEqual(int maxTime, Pageable pageable);
    
    // Advanced search queries
    @Query("{'title': {$regex: ?0, $options: 'i'}, 'category': {$regex: ?1, $options: 'i'}, 'cookingTime': {$lte: ?2}}")
    Page<Recipe> findAdvanced(String title, String category, int maxCookingTime, Pageable pageable);
    
    @Query("{'title': {$regex: ?0, $options: 'i'}, 'category': {$regex: ?1, $options: 'i'}, " +
           "'cookingTime': {$lte: ?2}, 'ingredients': {$regex: ?3, $options: 'i'}}")
    Page<Recipe> findAdvancedWithIngredient(String title, String category, int maxCookingTime, 
                                           String ingredient, Pageable pageable);
}
