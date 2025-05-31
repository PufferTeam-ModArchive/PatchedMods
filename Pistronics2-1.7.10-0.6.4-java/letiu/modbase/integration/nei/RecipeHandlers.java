package letiu.modbase.integration.nei;

import codechicken.nei.api.API;

public class RecipeHandlers {

    public static void registerHandlers() {

        ShapelessHandler shapelessHandler = new ShapelessHandler();
        API.registerRecipeHandler(shapelessHandler);
        API.registerUsageHandler(shapelessHandler);
    }
}
