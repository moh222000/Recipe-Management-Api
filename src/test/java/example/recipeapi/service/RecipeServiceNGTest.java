/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/EmptyTestNGTest.java to edit this template
 */
package example.recipeapi.service;

import example.recipeapi.model.Recipe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import static org.testng.Assert.*;

/**
 *
 * @author admin
 */
public class RecipeServiceNGTest {
    
    public RecipeServiceNGTest() {
    }

    @org.testng.annotations.BeforeClass
    public static void setUpClass() throws Exception {
    }

    @org.testng.annotations.AfterClass
    public static void tearDownClass() throws Exception {
    }

    @org.testng.annotations.BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @org.testng.annotations.AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of createRecipe method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testCreateRecipe() {
        System.out.println("createRecipe");
        Recipe recipe = null;
        RecipeService instance = new RecipeService();
        Recipe expResult = null;
        Recipe result = instance.createRecipe(recipe);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAllRecipes method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testGetAllRecipes() {
        System.out.println("getAllRecipes");
        RecipeService instance = new RecipeService();
        List expResult = null;
        List result = instance.getAllRecipes();
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRecipeById method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testGetRecipeById() {
        System.out.println("getRecipeById");
        String id = "";
        RecipeService instance = new RecipeService();
        Optional expResult = null;
        Optional result = instance.getRecipeById(id);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRecipe method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testUpdateRecipe() {
        System.out.println("updateRecipe");
        String id = "";
        Recipe recipeDetails = null;
        RecipeService instance = new RecipeService();
        Recipe expResult = null;
        Recipe result = instance.updateRecipe(id, recipeDetails);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteRecipe method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testDeleteRecipe() {
        System.out.println("deleteRecipe");
        String id = "";
        RecipeService instance = new RecipeService();
        instance.deleteRecipe(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFilteredRecipes method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testGetFilteredRecipes() {
        System.out.println("getFilteredRecipes");
        String title = "";
        String category = "";
        int page = 0;
        int size = 0;
        String sortBy = "";
        String direction = "";
        RecipeService instance = new RecipeService();
        Page expResult = null;
        Page result = instance.getFilteredRecipes(title, category, page, size, sortBy, direction);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByIngredient method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testSearchByIngredient() {
        System.out.println("searchByIngredient");
        String ingredient = "";
        int page = 0;
        int size = 0;
        String sortBy = "";
        String direction = "";
        RecipeService instance = new RecipeService();
        Page expResult = null;
        Page result = instance.searchByIngredient(ingredient, page, size, sortBy, direction);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of searchByMaxCookingTime method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testSearchByMaxCookingTime() {
        System.out.println("searchByMaxCookingTime");
        int maxTime = 0;
        int page = 0;
        int size = 0;
        String sortBy = "";
        String direction = "";
        RecipeService instance = new RecipeService();
        Page expResult = null;
        Page result = instance.searchByMaxCookingTime(maxTime, page, size, sortBy, direction);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of advancedSearch method, of class RecipeService.
     */
    @org.testng.annotations.Test
    public void testAdvancedSearch() {
        System.out.println("advancedSearch");
        String title = "";
        String category = "";
        int maxCookingTime = 0;
        String ingredient = "";
        int page = 0;
        int size = 0;
        String sortBy = "";
        String direction = "";
        RecipeService instance = new RecipeService();
        Page expResult = null;
        Page result = instance.advancedSearch(title, category, maxCookingTime, ingredient, page, size, sortBy, direction);
        assertEquals(result, expResult);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
