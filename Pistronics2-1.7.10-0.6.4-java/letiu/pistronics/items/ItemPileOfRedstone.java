package letiu.pistronics.items;

import net.minecraft.item.ItemStack;

import letiu.pistronics.data.PItem;
import letiu.pistronics.reference.Textures;

public class ItemPileOfRedstone extends PItem {

    public ItemPileOfRedstone() {
        this.name = "Pile of Redstone";
        this.creativeTab = true;
        this.textures = new String[1];
        this.textures[0] = Textures.PILE_OF_REDSTONE;
    }

    @Override
    public String getIcon(ItemStack stack, int pass) {
        return this.textures[0];
    }
}
