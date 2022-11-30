package usecase;

import com.j256.ormlite.dao.Dao;
import database.Database;
import database.InMemoryDatabase;
import entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeSearcherTest {

    private RecipeManager manager;
    private RecipeSearcher<Ingredient> ingredientSearcher;
    private RecipeSearcher<Tool> toolSearcher;
    private RecipeSearcher<Tag> tagSearcher;
    private Dao<RecipeIngredient, Integer> recipeIngredients;

    private Dao<RecipeTool, Integer> recipeTools;

    private Dao<RecipeTag, Integer> recipeTags;


    @BeforeEach
    public void Setup() throws SQLException {
        Database database = new InMemoryDatabase();
        manager = new RecipeManager(database);
        ingredientSearcher = new RecipeSearcher<>(database);
        toolSearcher = new RecipeSearcher<>(database);
        tagSearcher = new RecipeSearcher<>(database);
        recipeIngredients = database.getDao(RecipeIngredient.class);
        recipeTools = database.getDao(RecipeTool.class);
        recipeTags = database.getDao(RecipeTag.class);
    }


    @Test
    public void SearchRecipeTestOneIngredient() throws SQLException {
        Recipe pie = new Recipe("Pie", 3, 120);
        Recipe tomatoSpaghetti = new Recipe("Tomato Spaghetti", 2, 20);
        Recipe steak = new Recipe("Steak", 1, 15);

        manager.createRecipe(pie);
        manager.createRecipe(tomatoSpaghetti);
        manager.createRecipe(steak);

        Ingredient beefSirloin = new Ingredient("Beef Sirloin");

        RecipeIngredient steakIngredient = new RecipeIngredient(steak, beefSirloin, "200g");
        try {
            recipeIngredients.create(steakIngredient);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(beefSirloin);

        List<Recipe> returnRecipe = ingredientSearcher.searchRecipe(ingredientList);
        List<Recipe> expected = new ArrayList<>();
        expected.add(steak);

        Assertions.assertEquals(expected, returnRecipe);
    }

    @Test
    public void SearchRecipeTestOneTool() throws SQLException {
        Recipe pie = new Recipe("Pie", 3, 120);
        Recipe tomatoSpaghetti = new Recipe("Tomato Spaghetti", 2, 20);
        Recipe steak = new Recipe("Steak", 1, 15);

        manager.createRecipe(pie);
        manager.createRecipe(tomatoSpaghetti);
        manager.createRecipe(steak);

        Tool pan = new Tool("Pan");

        RecipeTool steakTool = new RecipeTool(steak, pan);
        try {
            recipeTools.create(steakTool);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Tool> toolList = new ArrayList<>();
        toolList.add(pan);

        List<Recipe> returnRecipe = toolSearcher.searchRecipe(toolList);
        List<Recipe> expected = new ArrayList<>();
        expected.add(steak);

        Assertions.assertEquals(expected, returnRecipe);
    }

    @Test
    public void SearchRecipeTestOneTag() throws SQLException {
        Recipe pie = new Recipe("Pie", 3, 120);
        Recipe tomatoSpaghetti = new Recipe("Tomato Spaghetti", 2, 20);
        Recipe steak = new Recipe("Steak", 1, 15);

        manager.createRecipe(pie);
        manager.createRecipe(tomatoSpaghetti);
        manager.createRecipe(steak);

        Tag beef = new Tag("Beef");

        RecipeTag steakTag = new RecipeTag(steak, beef);
        try {
            recipeTags.create(steakTag);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Tag> tagList = new ArrayList<>();
        tagList.add(beef);

        List<Recipe> returnRecipe = tagSearcher.searchRecipe(tagList);
        List<Recipe> expected = new ArrayList<>();
        expected.add(steak);

        Assertions.assertEquals(expected, returnRecipe);
    }


}