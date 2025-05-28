package letiu.pistronics.render;

import net.minecraft.item.ItemStack;

import letiu.modbase.render.UniversalItemRenderer;

public abstract class PItemRenderer extends UniversalItemRenderer {

    public abstract boolean handleRenderType(ItemStack item, ItemRenderType type);

    public abstract boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper);

    public abstract void renderItem(ItemRenderType type, ItemStack item, Object... data);

}
