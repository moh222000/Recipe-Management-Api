package example.recipeapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Document(collection = "recipes")
public class Recipe {
    @Id
    private String id;
    
    @NotBlank
    private String title;
    
    private List<String> ingredients;
    private List<String> instructions;
    private int cookingTime;
    private String category;
    
    // Default no-args constructor
    public Recipe() {
        // Empty constructor required by Spring Data
    }
    
    // Full constructor
    public Recipe(String title, List<String> ingredients, List<String> instructions, int cookingTime, String category) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.cookingTime = cookingTime;
        this.category = category;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<String> getIngredients() {
        return ingredients;
    }
    
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    
    public List<String> getInstructions() {
        return instructions;
    }
    
    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
    
    public int getCookingTime() {
        return cookingTime;
    }
    
    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
}
