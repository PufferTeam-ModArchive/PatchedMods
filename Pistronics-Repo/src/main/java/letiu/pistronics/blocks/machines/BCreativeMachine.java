package letiu.pistronics.blocks.machines;

import letiu.modbase.core.ModClass;
import letiu.modbase.util.CompareUtil;
import letiu.modbase.util.ItemReference;
import letiu.modbase.util.WorldUtil;
import letiu.pistronics.blocks.machines.BElementMachine;
import letiu.pistronics.data.PItem;
import letiu.pistronics.data.PTile;
import letiu.pistronics.itemblocks.BITexted;
import letiu.pistronics.piston.PistonSystem;
import letiu.pistronics.tiles.TileCreativeMachine;
import letiu.pistronics.tiles.TileMech;
import letiu.pistronics.util.BlockProxy;
import letiu.pistronics.util.CMRedstoneCommand;
import letiu.pistronics.util.FacingUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BCreativeMachine extends BElementMachine {

	public BCreativeMachine() {
		this.name = "Creative Machine";
		this.material = "iron";
		this.hardness = 5.0f;
		this.resistance = 10.0f;
		this.creativeTab = true;
		this.textures = new String[6];
		this.textures[0] = "adv_piston/inner";
		this.textures[1] = "adv_piston/bottom";
		this.textures[2] = "adv_piston/side";
		this.textures[3] = "camou_machines/camou_adv_piston_inner";
		this.textures[4] = "camou_machines/camou_adv_piston_bottom";
		this.textures[5] = "camou_machines/camou_adv_piston_side";
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
				} else {
					offset += 3;
				}
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
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public PTile createPTile() {
		return new TileCreativeMachine();
	}

	@Override
	public PItem getItemBlock() {
		return new BITexted(EnumChatFormatting.LIGHT_PURPLE + "Creative only unless you enable the config option.");
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		if (!world.isRemote) {
			BlockProxy proxy = new BlockProxy(world, x, y, z);
			int facing = proxy.getFacing();
			TileCreativeMachine tile = (TileCreativeMachine)proxy.getPTile();
			if (tile.active) {
				if (!proxy.isPowered()) {
					tile.active = false;
				}
			} else if (!tile.isInMotion() && proxy.isPowered()) {
				tile.active = true;
				int strength = proxy.getPowerInput();
				CMRedstoneCommand command = tile.getCommand(strength);
				if (command != null) {
					if (!command.direction) {
						facing ^= 1;
					}
					if (command.mode == 1) {
						PistonSystem system = new PistonSystem(proxy, facing, command.getMoveSpeed(), PistonSystem.SystemType.MOVE);
						system.tryMove();
					} else if (command.mode == 2) {
						PistonSystem system = new PistonSystem(proxy, facing, command.getRotateSpeed(), PistonSystem.SystemType.ROTATE);
						system.tryRotate();
					}
				}
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
		boolean superResult = super.onBlockActivated(world, x, y, z, player, side, xHit, yHit, zHit);
		ItemStack stack = player.getCurrentEquippedItem();
		if (CompareUtil.compareIDs(stack, ItemReference.REDSTONE_TORCH)) {
			if (!world.isRemote) {
				CMRedstoneCommand command;
				BlockProxy proxy = new BlockProxy(world, x, y, z);
				int facing = proxy.getFacing();
				TileCreativeMachine tile = (TileCreativeMachine)proxy.getPTile();
				if (!tile.isInMotion() && !tile.active && (command = tile.getCommand(0)) != null) {
					if (!command.direction) {
						facing ^= 1;
					}
					if (command.mode == 1) {
						PistonSystem system = new PistonSystem(proxy, facing, command.getMoveSpeed(), PistonSystem.SystemType.MOVE);
						system.tryMove();
					} else if (command.mode == 2) {
						PistonSystem system = new PistonSystem(proxy, facing, command.getRotateSpeed(), PistonSystem.SystemType.ROTATE);
						system.tryRotate();
					}
				}
			}
			return true;
		}
		if (superResult) {
			return true;
		}
		if (FacingUtil.getRelevantAxis(WorldUtil.getBlockFacing(world, x, y, z), xHit, yHit, zHit) <= 0.75f) {
			player.openGui(ModClass.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}
}
