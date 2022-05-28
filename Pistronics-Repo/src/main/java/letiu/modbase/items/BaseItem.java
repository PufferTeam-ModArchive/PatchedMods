package letiu.modbase.items;

import java.util.List;

import java.util.List;
import letiu.modbase.items.IBaseItem;
import letiu.modbase.render.TextureMapper;
import letiu.modbase.util.BlockItemUtil;
import letiu.pistronics.data.PItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;


public class BaseItem extends Item implements IBaseItem {

	public PItem data;

	@Override
	public PItem getData() {
		return this.data;
	}

	@Override
	public void setData(PItem data) {
		this.data = data;
	}

	public void registerIcons(IIconRegister iconRegister) {
		for (String path : this.data.textures) {
			TextureMapper.iconMap.put(path, iconRegister.registerIcon("pistronics:" + path));
		}
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		String iconKey = this.data.getIcon(stack, pass);
		if (iconKey.startsWith("[Block]")) {
			String[] parts = iconKey.split("x");
			int blockID = Integer.valueOf(parts[1]);
			int blockMeta = Integer.valueOf(parts[2]);
			return BlockItemUtil.getBlockByID(blockID).getIcon(pass, blockMeta);
		}
		return TextureMapper.iconMap.get(iconKey);
	}

	public IIcon getIconIndex(ItemStack stack) {
		return this.getIcon(stack, 0);
	}

	public String getUnlocalizedName() {
		return this.data.name;
	}

	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return this.data.name;
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xHit, float yHit, float zHit) {
		return this.data.onItemUse(stack, player, world, x, y, z, side, xHit, yHit, zHit);
	}

	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		return this.data.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		return this.data.onItemRightClick(stack, world, player);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean value) {
		this.data.addInformation(stack, player, list, value);
	}

	public int getSpriteNumber() {
		return this.data.getSpriteNumber();
	}

	public boolean getHasSubtypes() {
		return this.data.getHasSubtypes();
	}

	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		return this.data.onLeftClickEntity(stack, player, entity);
	}
}
