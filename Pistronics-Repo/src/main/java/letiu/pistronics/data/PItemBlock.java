package letiu.pistronics.data;

import net.minecraft.item.ItemStack;

import letiu.pistronics.render.PItemRenderer;

public abstract class PItemBlock extends PItem {

    public String getUnlocalizedName(String defaultName, ItemStack stack) {
        return defaultName;
    }

    public PItemRenderer getSpecialRenderer() {
        return null;
    }

    public int getSpriteNumber() {
        return 0;
    }
}
