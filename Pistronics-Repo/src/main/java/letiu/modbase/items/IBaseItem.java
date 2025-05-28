package letiu.modbase.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import letiu.pistronics.data.PItem;

public interface IBaseItem {

    public PItem getData();

    public void setData(PItem data);

    public IIcon getIcon(ItemStack stack, int pass);
}
