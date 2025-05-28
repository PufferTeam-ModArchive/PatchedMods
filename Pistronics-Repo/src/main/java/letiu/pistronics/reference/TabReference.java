package letiu.pistronics.reference;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import letiu.pistronics.blocks.BGear;
import letiu.pistronics.data.BlockData;
import letiu.pistronics.data.ItemData;

public class TabReference {

    public static ItemStack getCreativeTabIconStack() {
        ItemStack stack = new ItemStack(BlockData.gear.block);
        stack.stackTagCompound = BGear.getDefaultNBT();
        stack.stackTagCompound.setBoolean("rod", true);
        return stack;
    }

    public static Item getCreativeTabIconItem() {
        return (Item) ItemData.gear.item;
    }

}
