package letiu.pistronics.blocks.machines;

import letiu.modbase.core.ModClass;
import letiu.modbase.util.CompareUtil;
import letiu.modbase.util.WorldUtil;
import letiu.pistronics.blocks.machines.BElementMachine;
import letiu.pistronics.data.ItemData;
import letiu.pistronics.data.PItem;
import letiu.pistronics.data.PTile;
import letiu.pistronics.itemblocks.BITexted;
import letiu.pistronics.piston.PistonSystem;
import letiu.pistronics.tiles.TileMech;
import letiu.pistronics.tiles.TileRodFolder;
import letiu.pistronics.util.BlockProxy;
import letiu.pistronics.util.FacingUtil;
import letiu.pistronics.util.RedstoneUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BRodFolder extends BElementMachine {

	public BRodFolder() {
		this.name = "Rod Folder";
		this.material = "wood";
		this.hardness = 3.0f;
		this.resistance = 10.0f;
		this.creativeTab = true;
		this.textures = new String[6];
		this.textures[0] = "rod_folder/inner";
		this.textures[1] = "rod_folder/box";
		this.textures[2] = "rod_folder/side";
		this.textures[3] = "camou_machines/camou_rodfolder_inner";
		this.textures[4] = "camou_machines/camou_box";
		this.textures[5] = "camou_machines/camou_rodfolder_side";
		this.blockIcon = this.textures[0];
	}

	@Override
	public String getTextureIndex(PTile tile, int meta, int side) {
		int offset = 0;
		if (tile != null && tile instanceof TileMech) {
			TileMech tileMech = (TileMech)tile;
			if (tileMech.camou) {
				if (tileMech.camouID != -1) {
					return "[Block]x" + tileMech.camouID + "x" + tileMech.camouMeta;
				}
				offset += 3;
			}
		}
		meta = meta & 7;
		if (side == meta) {
			return this.textures[offset + 0];
		} else if (side == (meta ^ 1)) {
			return this.textures[offset + 1];
		} else {
			return this.textures[offset + 2];
		}
	}

	@Override
	public PItem getItemBlock() {
		return new BITexted(EnumChatFormatting.ITALIC + "Hello I'm a Rodfolder! :D", EnumChatFormatting.GOLD + "Rightclick " + EnumChatFormatting.BLUE + "me with a " + EnumChatFormatting.GOLD + "rod" + EnumChatFormatting.BLUE + " or open the inventory");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
		ItemStack stack = player.getCurrentEquippedItem();
		if (CompareUtil.compareIDs(stack, ItemData.tool.item)) {
			return false;
		}
		if (super.onBlockActivated(world, x, y, z, player, side, xHit, yHit, zHit)) {
			return true;
		}
		if (FacingUtil.getRelevantAxis(WorldUtil.getBlockFacing(world, x, y, z), xHit, yHit, zHit) <= 0.75f) {
			player.openGui(ModClass.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int fortune, EntityPlayer player) {
		super.onBlockHarvested(world, x, y, z, fortune, player);
		TileRodFolder tile = (TileRodFolder)WorldUtil.getPTile(world, x, y, z);
		tile.getPInventory().dropContent(world, x, y, z);
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public PTile createPTile() {
		return new TileRodFolder();
	}

	@Override
	public void getConnectedForSystem(PistonSystem system, BlockProxy proxy, boolean strongConnection) {
		BlockProxy neighbor;
		if (system.getSystemType() == PistonSystem.SystemType.REDSTONE) {
			if (this.isTransmitter(proxy)) {
				RedstoneUtil.connectAdjacentExtensions(system, proxy);
			} else {
				return;
			}
		}
		system.addSpecial(proxy);
		int facing = proxy.getFacing();
		TileRodFolder tile = (TileRodFolder) proxy.getPTile();

		if (tile.hasElement()) {
			BlockProxy neighbor = proxy.getNeighbor(facing);
			if (neighbor.connectsToSide(facing ^ 1, system.getSystemType())) {
				neighbor.getConnectedForSystem(system, false);
			}
		system.addElement(proxy, strongConnection);
	}

	@Override
	public boolean connectsToSide(BlockProxy proxy, int side, PistonSystem.SystemType type) {
		TileRodFolder tile = (TileRodFolder)proxy.getPTile();
		return tile.hasElement() && side == proxy.getFacing();
	}
}
