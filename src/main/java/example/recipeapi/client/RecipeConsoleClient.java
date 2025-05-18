package example.recipeapi.client;

import java.net.URI;
import java.net.http.*;
import java.util.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RecipeConsoleClient {

    private static final String API_URL = "http://localhost:8080/recipes";
    private static final String LOGIN_URL = "http://localhost:8080/authenticate/login";
    private static final String REGISTER_URL = "http://localhost:8080/authenticate/register";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private String jwtToken = null;
    private final ObjectMapper mapper = new ObjectMapper();
    

    public static void main(String[] args) {
        RecipeConsoleClient client = new RecipeConsoleClient();
        client.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\n--- Recipe Console ---");
            System.out.println("1. Login");
            System.out.println("2. Register User");
            System.out.println("3. Get Recipes (with pagination, sorting, filtering)");
            System.out.println("4. Create Recipe");
            System.out.println("5. Update Recipe");
            System.out.println("6. Delete Recipe");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            switch (scanner.nextLine()) {
                case "1" -> login();
                case "2" -> registerUser();
                case "3" -> { 
                    if (ensureLogin()) {
                        getAllRecipes();
                    }
                }
                case "4" -> {
                    if (ensureLogin()) {
                        createRecipe();
                    }
                }
                case "5" -> {
                    if (ensureLogin()) {
                        updateRecipe();
                    }
                }
                case "6" -> {
                    if (ensureLogin()) {
                        deleteRecipe();
                    }
                }
                case "7" -> {
                    System.out.println("Exiting console...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode jsonNode = mapper.readTree(response.body());
                jwtToken = jsonNode.get("token").asText();
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void registerUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String role = "USER";
        
        

        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"role\": \"%s\"}", username, password, role);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REGISTER_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Registration failed: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllRecipes() {
        Scanner scanner = new Scanner(System.in);

        try {
            StringBuilder query = new StringBuilder(API_URL);
            List<String> queryParams = new ArrayList<>();

            // Ask for filtering with strict yes/no validation
            String filter;
            while (true) {
                System.out.print("Do you want to filter recipes? (yes/no): ");
                filter = scanner.nextLine().trim().toLowerCase();
                if (filter.equals("yes") || filter.equals("no")) {
                    break;
                }
                System.out.println("Please enter only 'yes' or 'no'");
            }
            
            if (filter.equals("yes")) {
                System.out.print("Filter by title (leave empty for none): ");
                String title = scanner.nextLine().trim();
                if (!title.isEmpty()) {
                    queryParams.add("title=" + title);
                }
                
                System.out.print("Filter by category (leave empty for none): ");
                String category = scanner.nextLine().trim();
                if (!category.isEmpty()) {
                    queryParams.add("category=" + category);
                }
                
                // Adding /filter endpoint if filtering is needed
                query = new StringBuilder(API_URL + "/filter");
            }

            // Ask for pagination with strict yes/no validation
            String paginate;
            while (true) {
                System.out.print("Do you want pagination? (yes/no): ");
                paginate = scanner.nextLine().trim().toLowerCase();
                if (paginate.equals("yes") || paginate.equals("no")) {
                    break;
                }
                System.out.println("Please enter only 'yes' or 'no'");
            }
            
            if (paginate.equals("yes")) {
                System.out.print("Page number: ");
                int page = Integer.parseInt(scanner.nextLine());
                System.out.print("Page size: ");
                int size = Integer.parseInt(scanner.nextLine());
                queryParams.add("page=" + page);
                queryParams.add("size=" + size);
            }

            // Ask for sorting with strict yes/no validation
            String sorting;
            while (true) {
                System.out.print("Do you want sorting? (yes/no): ");
                sorting = scanner.nextLine().trim().toLowerCase();
                if (sorting.equals("yes") || sorting.equals("no")) {
                    break;
                }
                System.out.println("Please enter only 'yes' or 'no'");
            }
            
            if (sorting.equals("yes")) {
                System.out.print("Sort field (title/category/cookingTime): ");
                String sortField = scanner.nextLine().trim();
                while (!sortField.equals("title") && !sortField.equals("category") && !sortField.equals("cookingTime")) {
                    System.out.print("Invalid field. Choose (title/category/cookingTime): ");
                    sortField = scanner.nextLine().trim();
                }

                String sortDir;
                while (true) {
                    System.out.print("Sort direction (asc/desc): ");
                    sortDir = scanner.nextLine().trim().toLowerCase();
                    if (sortDir.equals("asc") || sortDir.equals("desc")) {
                        break;
                    }
                    System.out.println("Please enter only 'asc' or 'desc'");
                }

                queryParams.add("sortBy=" + sortField);
                queryParams.add("direction=" + sortDir);
            }

            // Final query construction
            if (!queryParams.isEmpty()) {
                query.append("?").append(String.join("&", queryParams));
            }

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(query.toString()))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                try {
                    // First try to parse as paginated response
                    JsonNode pageResponse = mapper.readTree(response.body());
                    if (pageResponse.has("content") && pageResponse.get("content").isArray()) {
                        // It's a paginated response
                        JsonNode content = pageResponse.get("content");
                        for (JsonNode recipe : content) {
                            printRecipe(recipe);
                        }
                        
                        System.out.println("\n--- Pagination Info ---");
                        System.out.println("Total Elements: " + pageResponse.get("totalElements").asInt());
                        System.out.println("Total Pages: " + pageResponse.get("totalPages").asInt());
                        System.out.println("Current Page: " + pageResponse.get("number").asInt());
                    } else if (pageResponse.isArray()) {
                        // It's a regular array response
                        for (JsonNode recipe : pageResponse) {
                            printRecipe(recipe);
                        }
                        
                        if (paginate.equals("yes")) {
                            System.out.println("\nNote: The API seems to return a plain array instead of " +
                                              "paginated response. Pagination information not available.");
                        }
                    } else {
                        // Single recipe response
                        printRecipe(pageResponse);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing response: " + e.getMessage());
                    System.out.println("Raw response: " + response.body());
                }
            } else {
                System.out.println("Error fetching recipes: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createRecipe() {
        Scanner scanner = new Scanner(System.in);

        try {
            ObjectNode recipe = mapper.createObjectNode();

            System.out.print("Title: ");
            recipe.put("title", scanner.nextLine());
            System.out.print("Category: ");
            recipe.put("category", scanner.nextLine());
            System.out.print("Cooking time (minutes): ");
            recipe.put("cookingTime", Integer.parseInt(scanner.nextLine()));

            
            List<String> ingredients = new ArrayList<>();
            System.out.println("Enter ingredients (type 'done' to finish):");
            while (true) {
                String ing = scanner.nextLine();
                if (ing.equalsIgnoreCase("done")) {
                    if (ingredients.isEmpty()) {
                        System.out.println("Ingredients cannot be empty. Please add at least one ingredient:");
                        continue;
                    }
                    break;
                }
                ingredients.add(ing);
            }
            recipe.putPOJO("ingredients", ingredients);

            List<String> instructions = new ArrayList<>();
            System.out.println("Enter instructions (type 'done' to finish):");
            while (true) {
                String inst = scanner.nextLine();
                if (inst.equalsIgnoreCase("done")) {
                    if (instructions.isEmpty()) {
                        System.out.println("Instructions cannot be empty. Please add at least one instruction:");
                        continue;
                    }
                    break;
                }
                instructions.add(inst);
            }
            recipe.putPOJO("instructions", instructions);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .POST(HttpRequest.BodyPublishers.ofString(recipe.toString()))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Create status: " + response.statusCode());
            System.out.println("Response: " + response.body());
            System.out.println("Recipe created successfully !");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateRecipe() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter recipe ID to update: ");
            String id = scanner.nextLine();
            
            
            if (!recipeExists(id)) {
                System.out.println("Recipe with ID " + id + " does not exist.");
                return;
            }

            ObjectNode recipe = mapper.createObjectNode();
            System.out.print("New title: ");
            recipe.put("title", scanner.nextLine());
            System.out.print("New category: ");
            recipe.put("category", scanner.nextLine());
            System.out.print("New cooking time: ");
            recipe.put("cookingTime", Integer.parseInt(scanner.nextLine()));

            
            List<String> ingredients = new ArrayList<>();
            System.out.println("Enter ingredients (type 'done' to finish):");
            while (true) {
                String ing = scanner.nextLine();
                if (ing.equalsIgnoreCase("done")) {
                    if (ingredients.isEmpty()) {
                        System.out.println("Ingredients cannot be empty. Please add at least one ingredient:");
                        continue;
                    }
                    break;
                }
                ingredients.add(ing);
            }
            recipe.putPOJO("ingredients", ingredients);

            
            List<String> instructions = new ArrayList<>();
            System.out.println("Enter instructions (type 'done' to finish):");
            while (true) {
                String inst = scanner.nextLine();
                if (inst.equalsIgnoreCase("done")) {
                    if (instructions.isEmpty()) {
                        System.out.println("Instructions cannot be empty. Please add at least one instruction:");
                        continue;
                    }
                    break;
                }
                instructions.add(inst);
            }
            recipe.putPOJO("instructions", instructions);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + jwtToken)
                .PUT(HttpRequest.BodyPublishers.ofString(recipe.toString()))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Update status: " + response.statusCode());
            System.out.println("Response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteRecipe() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter recipe ID to delete: ");
            String id = scanner.nextLine();
            
            if (!recipeExists(id)) {
                System.out.println("Recipe with ID " + id + " does not exist.");
                return;
            }

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .DELETE()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Delete status: " + response.statusCode());
            System.out.println("Recipe deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean recipeExists(String id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .header("Authorization", "Bearer " + jwtToken)
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void printRecipe(JsonNode recipe) {
        System.out.println("\n--- Recipe ---");
        System.out.println("ID: " + recipe.get("id").asText());
        System.out.println("Title: " + recipe.get("title").asText());
        System.out.println("Category: " + (recipe.has("category") && !recipe.get("category").isNull() ? 
                           recipe.get("category").asText() : "None"));
        System.out.println("Cooking Time: " + recipe.get("cookingTime").asInt() + " minutes");

        System.out.println("Ingredients:");
        for (JsonNode ing : recipe.get("ingredients")) {
            System.out.println(" - " + ing.asText());
        }

        System.out.println("Instructions:");
        for (JsonNode inst : recipe.get("instructions")) {
            System.out.println(" - " + inst.asText());
        }
    }

    private boolean ensureLogin() {
        if (jwtToken == null) {
            System.out.println("You must login first.");
            login();
            return jwtToken != null;
        }
        return true;
    }
}