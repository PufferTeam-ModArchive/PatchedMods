package letiu.pistronics.recipes.custom;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

import letiu.modbase.util.BlockItemUtil;
import letiu.modbase.util.CompareUtil;
import letiu.pistronics.data.BlockData;
import letiu.pistronics.data.ItemData;
import letiu.pistronics.recipes.PRecipeRegistry;
import letiu.pistronics.recipes.PShapelessRecipe;
import letiu.pistronics.util.SailUtil;

public class SailPartRecipes {

    public static void registerRecipes() {
        ArrayList<ItemStack> stacks = SailUtil.getAllSails();
        for (ItemStack stack : stacks) {
            ArrayList<PShapelessRecipe> recipes = getRecipesForCrafting(stack);
            for (PShapelessRecipe recipe : recipes) {
                PRecipeRegistry.registerShapelessRecipe(recipe);
            }
        }
    }

    public static ArrayList<PShapelessRecipe> getRecipesForCrafting(ItemStack result) {
        ArrayList<PShapelessRecipe> recipes = new ArrayList<PShapelessRecipe>();

        if (CompareUtil.compareIDs(result, BlockData.sailPart.block)) {

            ItemStack result1 = result.copy();
            result1.stackSize = 1;

            boolean camou = result.stackTagCompound.getBoolean("camou");

            if (camou) {
                ItemStack nonCamouStack = result1.copy();
                nonCamouStack.stackTagCompound.setBoolean("camou", false);

                recipes.add(new PShapelessRecipe(result1, nonCamouStack, BlockItemUtil.getStack(ItemData.camoupaste)));
            }
        }

        return recipes;
    }
}
