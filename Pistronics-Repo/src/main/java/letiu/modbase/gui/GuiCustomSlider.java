package letiu.modbase.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiCustomSlider extends GuiButton
{
    /** The value of this slider control. */
    public float sliderValue = 1.0f;
    /** Is this slider control being dragged. */
    public boolean dragging;
    public int options;
    public int option;
    public String name;
    private float step;

    public GuiCustomSlider(int ID, int x, int y, String displayString, float value) {
        super(ID, x, y, 150, 20, displayString);
        this.sliderValue = value;
        this.width = 20;
        this.name = displayString;
    }

    public GuiCustomSlider(int ID, int x, int y, int width, int height, String displayString, float value) {
        this(ID, x, y, displayString, value);
        this.width = width;
        this.height = height;
        this.options = -1;
        this.option = -1;
        this.name = displayString;
    }

    public GuiCustomSlider(int ID, int x, int y, int width, int height, int options, int start, String displayString) {
        this(ID, x, y, displayString, 0.0f);
        this.width = width;
        this.height = height;
        this.options = options - 1;
        this.option = start - 1;
        this.name = displayString;
        this.step = 1.0f / (float)this.options;
        this.updateValue();
        this.updateDisplayString();
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    @Override
    public int getHoverState(boolean par1) {
        return 0;
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    @Override
    protected void mouseDragged(Minecraft par1Minecraft, int par2, int par3) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
                if (this.sliderValue < 0.0f) {
                    this.sliderValue = 0.0f;
                }
                if (this.sliderValue > 1.0f) {
                    this.sliderValue = 1.0f;
                }
                if (this.options != -1) {
                    this.option = Math.round(this.sliderValue / this.step);
                    this.sliderValue = (float)this.option * this.step;
                    this.updateDisplayString();
                }
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.func_73729_b(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.func_73729_b(this.xPosition + (int)(this.sliderValue * (float)(this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    @Override
    public boolean mousePressed(Minecraft par1Minecraft, int par2, int par3) {
        if (super.mousePressed(par1Minecraft, par2, par3)) {
            this.sliderValue = (float)(par2 - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderValue < 0.0f) {
                this.sliderValue = 0.0f;
            }
            if (this.sliderValue > 1.0f) {
                this.sliderValue = 1.0f;
            }
            if (this.options != -1) {
                this.option = Math.round(this.sliderValue / this.step);
                this.sliderValue = (float)this.option * this.step;
                this.updateDisplayString();
            }
            this.dragging = true;
            return true;
        }
        return false;
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }

    public void increment() {
        if (this.options != -1 && this.option < this.options) {
            this.option++;
            this.updateValue();
            this.updateDisplayString();
        }
    }

    public void decrement() {
        if (this.options != -1 && this.option != 0) {
            --this.option;
            this.updateValue();
            this.updateDisplayString();
        }
    }

    public void setFloatValue(float value) {
        this.option = Math.round(value / this.step);
        this.sliderValue = (float)this.option * this.step;
        this.updateDisplayString();
    }

    public void setOption(int option) {
        this.option = option;
        this.sliderValue = (float)option * this.step;
        this.updateDisplayString();
    }

    public void updateValue() {
        if (this.options != -1) {
            this.sliderValue = (float)this.option * this.step;
        }
    }

    public void updateDisplayString() {
        if (this.options != -1) {
            this.displayString = this.name + ": " + (this.option + 1);
        }
    }
}