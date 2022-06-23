package letiu.modbase.tiles;

import letiu.modbase.util.NBTUtil;
import letiu.pistronics.data.PTile;
import letiu.pistronics.data.TileData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class BaseTile extends TileEntity {
	public PTile data;

	public void updateEntity() {
		this.data.update();
	}

	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB box = this.data.getRenderBoundingBox();
		return (box == null) ? super.getRenderBoundingBox() : box;
	}

	public Packet getDescriptionPacket() {
		super.getDescriptionPacket();
		NBTTagCompound tag = NBTUtil.getNewCompound();
		writeToNBT(tag);
		return (Packet)new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, tag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		readFromNBT(packet.func_148857_g());
	}

	public void validate() {
		super.validate();
		this.data.postLoad();
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setString("key", this.data.getKey());
		this.data.writeToNBT(tagCompound);
	}

	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		this.data = TileData.getTile(tagCompound.getString("key"));
		this.data.tileEntity = this;
		this.data.readFromNBT(tagCompound);
	}
}