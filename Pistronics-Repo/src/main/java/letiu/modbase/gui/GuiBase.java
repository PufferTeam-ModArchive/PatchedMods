package letiu.modbase.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

public class GuiBase extends GuiScreen {

	public void drawIcon(IIcon icon, int x, int y, float scaling) {
		float antiScaling = 1.0f / scaling;

		GL11.glScalef(scaling, scaling, scaling);

		RenderItem rItem = new RenderItem();
		int localX = (int)((float)x * antiScaling);
		int localY = (int)((float)y * antiScaling);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		rItem.renderIcon(localX, localY, icon, 16, 16);

		GL11.glScalef(antiScaling, antiScaling, antiScaling);
	}
}
