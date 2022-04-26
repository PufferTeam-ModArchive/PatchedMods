package letiu.pistronics.recipes;

import letiu.pistronics.recipes.PShapelessRecipe;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;

public class PRecipeRegistry implements IRecipe {

    private static PRecipeRegistry instance;

    public static PRecipeRegistry instance() {
        if (instance == null) instance = new PRecipeRegistry();
        return instance;
    }

    public static int recipeAmount() {
        return instance().shapelessRecipes.size();
    }

    private PRecipeRegistry() {}

    private ArrayList<PShapelessRecipe> shapelessRecipes = new ArrayList<PShapelessRecipe>();


    public static void registerShapelessRecipe(PShapelessRecipe recipe) {
        instance().shapelessRecipes.add(recipe);
    }

    public static ArrayList<PShapelessRecipe> getShapelessCraftingRecipesFor(ItemStack result) {
        ArrayList<PShapelessRecipe> recipes = new ArrayList<PShapelessRecipe>();

        for (PShapelessRecipe recipe : instance().shapelessRecipes) {
            if (recipe.isResult(result)) {
                recipes.add(recipe);
            }
        }

        return recipes;
    }

    public static ArrayList<PShapelessRecipe> getShapelessUsageRecipeFor(ItemStack ingridient) {
        ArrayList<PShapelessRecipe> recipes = new ArrayList<PShapelessRecipe>();

        for (PShapelessRecipe recipe : instance().shapelessRecipes) {
            if (recipe.isIngredient(ingridient)) {
                recipes.add(recipe);
            }
        }

        return recipes;
    }


    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        for (PShapelessRecipe recipe : shapelessRecipes) {
            if (recipe.matches(inv)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
         for (PShapelessRecipe recipe : shapelessRecipes) {
            if (recipe.matches(inv)) {
                return recipe.getResult();
            }
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
