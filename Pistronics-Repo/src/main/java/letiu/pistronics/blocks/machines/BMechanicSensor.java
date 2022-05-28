package letiu.pistronics.blocks.machines;

import java.util.ArrayList;
import letiu.modbase.util.WorldUtil;
import letiu.pistronics.blocks.machines.BElementMachine;
import letiu.pistronics.data.PBlock;
import letiu.pistronics.data.PItem;
import letiu.pistronics.data.PTile;
import letiu.pistronics.itemblocks.BITexted;
import letiu.pistronics.render.PRenderManager;
import letiu.pistronics.tiles.TileElementHolder;
import letiu.pistronics.tiles.TileMechSensor;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;

public class BMechanicSensor extends BElementMachine {
    public BMechanicSensor() {
        this.name = "Mechanic Sensor";
        this.material = "wood";
        this.hardness = 3.0f;
        this.resistance = 10.0f;
        this.creativeTab = true;
        this.textures = new String[8];
        this.textures[0] = "sensor/side";
        this.textures[1] = "sensor/inner";
        this.textures[2] = "sensor/side_camou";
        this.textures[3] = "sensor/inner_camou";
        this.textures[4] = "sensor/side_active";
        this.textures[5] = "sensor/inner_active";
        this.textures[6] = "sensor/side_camou_active";
        this.textures[7] = "sensor/inner_camou_active";
        this.blockIcon = this.textures[0];
    }

    @Override
    public boolean hasTileEntity() {
        return true;
    }

    @Override
    public PTile createPTile() {
        return new TileMechSensor();
    }

    @Override
    public String getTextureIndex(PTile tile, int meta, int side) {
        int offset = 0;
        if (tile != null && tile instanceof TileMechSensor) {
            TileMechSensor tileMech = (TileMechSensor)tile;
            if (tileMech.camou) {
                if (tileMech.camouID != -1) {
                    return "[Block]x" + tileMech.camouID + "x" + tileMech.camouMeta;
                }
                offset += 2;
            }
            if (tileMech.shouldEmit()) {
                offset += 4;
            }
        }
        if (side == (meta &= 7) || side == (meta ^ 1)) {
            return this.textures[offset + 1];
        }
        return this.textures[offset];
    }

    @Override
    public PItem getItemBlock() {
        return new BITexted(EnumChatFormatting.ITALIC + "Hello I'm a Sensor! :D", EnumChatFormatting.BLUE + "I detect if a system can't move.");
    }

    @Override
    public int getRenderID() {
        return PRenderManager.getRenderID(PRenderManager.sensorRenderer);
    }

    @Override
    public ArrayList<AxisAlignedBB> getBoxes(IBlockAccess world, int x, int y, int z, int meta) {
        ArrayList<AxisAlignedBB> eBoxes;
        PBlock element;
        ArrayList<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        switch (meta & 7) {
            case 0:
            case 1: {
                boxes.add(AxisAlignedBB.getBoundingBox(0.0, 0.125, 0.0, 1.0, 0.875, 1.0));
                break;
            }
            case 2:
            case 3: {
                boxes.add(AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.125, 1.0, 1.0, 0.875));
                break;
            }
            case 4:
            case 5: {
                boxes.add(AxisAlignedBB.getBoundingBox(0.125, 0.0, 0.0, 0.875, 1.0, 1.0));
                break;
            }
            default: {
                return null;
            }
        }
        PTile tile = WorldUtil.getPTile(world, x, y, z);
        if (tile != null && tile instanceof TileElementHolder && ((TileElementHolder)tile).hasElement() && (element = ((TileElementHolder)tile).getPElement()) != null && !(element instanceof BElementMachine) && (eBoxes = element.getBoxes(world, x, y, z, meta)) != null) {
            boxes.addAll(eBoxes);
        }
        return boxes;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        switch (WorldUtil.getBlockFacing(world, x, y, z)) {
            case 0:
            case 1: {
                WorldUtil.setBlockBounds(world, x, y, z, 0.0f, 0.125f, 0.0f, 1.0f, 0.875f, 1.0f);
                break;
            }
            case 2:
            case 3: {
                WorldUtil.setBlockBounds(world, x, y, z, 0.0f, 0.0f, 0.125f, 1.0f, 1.0f, 0.875f);
                break;
            }
            case 4:
            case 5: {
                WorldUtil.setBlockBounds(world, x, y, z, 0.125f, 0.0f, 0.0f, 0.875f, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) {
        int facing = WorldUtil.getBlockFacing(world, x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side]);
        if (facing != side && facing != (side ^ 1)) {
            return super.shouldSideBeRendered(world, x, y, z, side);
        }
        return true;
    }

    @Override
    public boolean allowsExtension() {
        return false;
    }

    @Override
    public int getStrongPower(IBlockAccess world, int x, int y, int z, int side) {
        PTile tileRaw;
        int facing = WorldUtil.getBlockFacing(world, x, y, z);
        if (facing != side && facing != (side ^ 1) && (tileRaw = WorldUtil.getPTile(world, x, y, z)) != null && tileRaw instanceof TileMechSensor) {
            return ((TileMechSensor)tileRaw).shouldEmit() ? 15 : 0;
        }
        return 0;
    }

    @Override
    public int getWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return this.getStrongPower(world, x, y, z, side);
    }

    @Override
    public boolean shouldCheckWeakPower(IBlockAccess world, int x, int y, int z, int side) {
        return "1.7.10".equals("1.6.4");
    }
}