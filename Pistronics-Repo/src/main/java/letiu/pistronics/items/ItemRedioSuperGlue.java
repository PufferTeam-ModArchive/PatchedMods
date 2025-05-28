package letiu.pistronics.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import letiu.pistronics.data.PItem;
import letiu.pistronics.reference.Textures;

public class ItemRedioSuperGlue extends PItem {

    public ItemRedioSuperGlue() {
        this.name = "Redioactive Super Glue";
        this.creativeTab = true;
        this.textures = new String[1];
        this.textures[0] = Textures.REDIO_SUPER_GLUE;
    }

    @Override
    public String getIcon(ItemStack stack, int pass) {
        return this.textures[0];
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean value) {

        list.add(EnumChatFormatting.BLUE + "Only works with redstone");
        list.add(EnumChatFormatting.BLUE + "and is sticky when powered.");
    }
}
