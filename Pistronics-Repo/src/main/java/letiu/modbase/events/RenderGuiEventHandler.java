package letiu.modbase.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import letiu.modbase.events.ViewEventHandler;
import letiu.modbase.util.CompareUtil;
import letiu.pistronics.data.ItemData;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class RenderGuiEventHandler {
    private static final RenderItem renderItem = new RenderItem();

    @SubscribeEvent
    public void handleEvent(RenderGameOverlayEvent event) {
        ItemStack stack;
        if (event.type == RenderGameOverlayEvent.ElementType.HOTBAR && CompareUtil.compareIDs(stack = ViewEventHandler.subHitEvent.currentItem, ItemData.saw.item)) {
            MovingObjectPosition mop = ViewEventHandler.subHitEvent.target;
            NBTTagCompound nbt = stack.stackTagCompound;
            if (nbt != null) {
                int x = nbt.getInteger("xPos");
                int y = nbt.getInteger("yPos");
                int z = nbt.getInteger("zPos");
                if (x == mop.blockX && y == mop.blockY && z == mop.blockZ) {
                    byte p = nbt.getByte("progress");
                    System.out.println("RENDER PROGRESS BAR HERE!");
                    RenderHelper.enableGUIStandardItemLighting();
                }
            }
        }
    }
}