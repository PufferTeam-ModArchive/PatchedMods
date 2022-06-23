package letiu.pistronics.tiles;

import java.util.Iterator;
import java.util.List;
import letiu.modbase.util.BlockItemUtil;
import letiu.modbase.util.NBTUtil;
import letiu.modbase.util.WorldUtil;
import letiu.pistronics.data.PBlock;
import letiu.pistronics.data.PTile;
import letiu.pistronics.piston.ControllerData;
import letiu.pistronics.piston.ControllerRegistry;
import letiu.pistronics.piston.ISpecialRotator;
import letiu.pistronics.piston.MoveData;
import letiu.pistronics.piston.SystemController;
import letiu.pistronics.render.PRenderManager;
import letiu.pistronics.render.PTileRenderer;
import letiu.pistronics.util.BlockProxy;
import letiu.pistronics.util.RotateUtil;
import letiu.pistronics.util.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileMotion extends TileElementHolder implements ISpecialRenderTile, IMover {

	public ControllerData ctrData = new ControllerData();

	private NBTTagCompound tempNBTCompound = null;

	public void update() {
		if (this.ctrData.controller == null)
			this.ctrData.controller = new SystemController(new MoveData());

		this.ctrData.updateController();
		MoveData moveData = this.ctrData.controller.getMoveData();

		if (moveData.isMoving())
			pushEntities();

		if (moveData.isDone())
			if (moveData.isMoving()) {
				pushEntitiesFinal();
				loadBlock();
				postMove();
			} else if (moveData.isRotating()) {
				loadBlock();
				postRotate();
			}
	}
	
	@Override
	public void setProgress(float progress) {}

	public AxisAlignedBB getBoxForPush(float pushSpace) {
		AxisAlignedBB box;
		MoveData moveData = this.ctrData.controller.getMoveData();
		World world = this.tileEntity.getWorldObj();
		int x = this.tileEntity.xCoord;
		int y = this.tileEntity.yCoord;
		int z = this.tileEntity.zCoord;
		if (getPElement() instanceof letiu.pistronics.blocks.BPartblock) {
			box = AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1));
		} else {
			box = BlockItemUtil.getBoundingBox(world, x, y, z, this.element);
			if (box == null)
				box = AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1));
		}
		switch (moveData.moveDir) {
			case 0:
				box.maxY = (y + 1);
				break;
			case 1:
				box.minY = y;
				break;
			case 2:
				box.maxZ = (z + 1);
				break;
			case 3:
				box.minZ = z;
				break;
			case 4:
				box.maxX = (x + 1);
				break;
			case 5:
				box.minX = x;
				break;
		}
		float antiProgress = 1.0F - moveData.progress + pushSpace;
		return box.getOffsetBoundingBox((antiProgress * Facing.offsetsXForSide[moveData.moveDir]), (antiProgress * Facing.offsetsYForSide[moveData.moveDir]), (antiProgress * Facing.offsetsZForSide[moveData.moveDir]));
	}

		private void pushEntities() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			World world = this.tileEntity.getWorldObj();
			int x = this.tileEntity.xCoord;
			int y = this.tileEntity.yCoord;
			int z = this.tileEntity.zCoord;
			float moveAmt = 1.0F * moveData.moveSpeed;
			PBlock block = WorldUtil.getPBlock((IBlockAccess)world, x + Facing.offsetsXForSide[moveData.moveDir], y + Facing.offsetsYForSide[moveData.moveDir], z + Facing.offsetsZForSide[moveData.moveDir]);
			if (block != null && block instanceof letiu.pistronics.blocks.BMotionblock) {
				List list = this.tileEntity.getWorldObj().getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(x, y, z, (x + 1), (y + 1), (z + 1)));
				Iterator<Entity> itr = list.iterator();
				while (itr.hasNext()) {
					Entity entity = itr.next();
					double vX = 0.1D * (entity.posX - x + 0.5D);
					double vY = 0.1D * (entity.posY - y + 0.5D);
					double vZ = 0.1D * (entity.posZ - z + 0.5D);
					entity.motionX = vX;
					entity.motionY = vY;
					entity.motionZ = vZ;
				}
			} else {
				List list = this.tileEntity.getWorldObj().getEntitiesWithinAABBExcludingEntity((Entity)null, getBoxForPush(0.25F));
				Iterator<Entity> itr = list.iterator();
				while (itr.hasNext()) {
					Entity entity = itr.next();
					entity.moveEntity((moveAmt * Facing.offsetsXForSide[moveData.moveDir]), (moveAmt * Facing.offsetsYForSide[moveData.moveDir]), (moveAmt * Facing.offsetsZForSide[moveData.moveDir]));
					switch (moveData.moveDir) {
						case 0:
							if (entity.motionY > 0.0D)
								entity.motionY = 0.0D;
						case 1:
							if (entity.motionY < 0.0D)
								entity.motionY = 0.0D;
						case 2:
							if (entity.motionZ > 0.0D)
								entity.motionZ = 0.0D;
						case 3:
							if (entity.motionZ < 0.0D)
								entity.motionZ = 0.0D;
						case 4:
							if (entity.motionX > 0.0D)
								entity.motionX = 0.0D;
						case 5:
							if (entity.motionX < 0.0D)
								entity.motionX = 0.0D;
					}
				}
			}
		}

		private void pushEntitiesFinal() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			List list = this.tileEntity.getWorldObj().getEntitiesWithinAABBExcludingEntity((Entity)null, getBoxForPush(0.0F));
			Iterator<Entity> itr = list.iterator();
			while (itr.hasNext()) {
				Entity entity = itr.next();
				switch (moveData.moveDir) {
					case 1:
						entity.posY = Math.ceil(entity.posY);
						entity.motionY = 0.25D;
					case 2:
						entity.motionZ = -0.25D;
					case 3:
						entity.motionZ = 0.25D;
					case 4:
						entity.motionX = -0.25D;
					case 5:
						entity.motionX = 0.25D;
				}
			}
		}

		public boolean tryToMoveOn() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			int x = this.tileEntity.xCoord + Facing.offsetsXForSide[moveData.moveDir];
			int y = this.tileEntity.yCoord + Facing.offsetsYForSide[moveData.moveDir];
			int z = this.tileEntity.zCoord + Facing.offsetsZForSide[moveData.moveDir];
			PTile tile = WorldUtil.getPTile((IBlockAccess)this.tileEntity.getWorldObj(), x, y, z);
			if (tile != null && tile instanceof TileMotion) {
				moveData.progress = 0.0F;
				NBTTagCompound nbt = new NBTTagCompound();
				writeToNBT(nbt);
				tile.readFromNBT(nbt);
				moveData.moveDir = -1;
				this.element = null;
				return true;
			}
			return false;
		}

		public String getKey() {
			return "tlMotion";
		}

		public PTileRenderer getRenderer() {
			return PRenderManager.motionRenderer;
		}

		public int getRotateDir() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.rotateDir;
		}

		public int getMoveDir() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.moveDir;
		}

		public float getAngle() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.angle;
		}

		public float getProgress() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.progress;
		}

		public float getAngleForRender(float ticktime) {
			MoveData moveData = this.ctrData.controller.getMoveData();
			if (ticktime > 1.0F)
				ticktime = 1.0F;
			return moveData.angle + moveData.rotateSpeed * ticktime - 90.0F;
		}

		public float getProgressForRender(float ticktime) {
			MoveData moveData = this.ctrData.controller.getMoveData();
			if (ticktime > 1.0F)
				ticktime = 1.0F;
			return moveData.progress + moveData.moveSpeed * ticktime;
		}

		public float getRotateSpeed() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.rotateSpeed;
		}

		public float getMoveSpeed() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.moveSpeed;
		}

		public Vector3 getRotatePoint() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return moveData.rotatePoint;
		}

		public boolean isRotating() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return (moveData.rotateDir != -1 && moveData.rotateSpeed != 0.0F);
		}

		public boolean isMoving() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			return (moveData.moveDir != -1 && moveData.moveSpeed != 0.0F);
		}

		public boolean isInMotion() {
			return (isMoving() || isRotating());
		}

		public void rotate(int rotateDir, float speed) {
			rotate(rotateDir, speed, new Vector3(this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord));
		}

		public void rotate(int rotateDir, float speed, Vector3 rotatePoint) {
			if (this.ctrData.controller == null) {
				MoveData moveData = new MoveData(rotateDir, speed, rotatePoint);
				if (this.ctrData.key != null && !this.ctrData.key.equals("default")) {
					this.ctrData.controller = ControllerRegistry.create(this.ctrData.key, moveData, (this.tileEntity.getWorldObj()).isRemote);
					this.ctrData.key = this.ctrData.controller.getKey();
				} else {
					SystemController ctr = new SystemController(moveData);
					ControllerRegistry.register(ctr, new BlockProxy((TileEntity)this.tileEntity));
					this.ctrData.controller = ctr;
					this.ctrData.key = ctr.getKey();
				}
			}
		}

		public void move(int moveDir, float speed) {
			if (this.ctrData.controller == null) {
				MoveData moveData = new MoveData(moveDir, speed);
				if (this.ctrData.key != null && !this.ctrData.key.equals("default")) {
					this.ctrData.controller = ControllerRegistry.create(this.ctrData.key, moveData, (this.tileEntity.getWorldObj()).isRemote);
					this.ctrData.key = this.ctrData.controller.getKey();
				} else {
					SystemController ctr = new SystemController(moveData);
					ControllerRegistry.register(ctr, new BlockProxy((TileEntity)this.tileEntity));
					this.ctrData.controller = ctr;
					this.ctrData.key = ctr.getKey();
				}
			}
		}

		public void loadBlock() {
			int x = this.tileEntity.xCoord, y = this.tileEntity.yCoord, z = this.tileEntity.zCoord;
			World world = this.tileEntity.getWorldObj();
			if (this.element != null) {
				WorldUtil.setBlock(world, x, y, z, this.element, this.elementMeta, 5);
				if (this.elementTile != null) {
					TileEntity tile = WorldUtil.getTileEntity((IBlockAccess)world, x, y, z);
					NBTTagCompound nbt = NBTUtil.getNewCompound();
					this.elementTile.writeToNBT(nbt);
					if (tile == null) {
						this.elementTile.setWorldObj(world);
						this.elementTile.xCoord = x;
						this.elementTile.yCoord = y;
						this.elementTile.zCoord = z;
						WorldUtil.setTileEntity(world, x, y, z, this.elementTile);
						WorldUtil.setBlock(world, x, y, z, this.element, this.elementMeta, 5);
					}
					tile = WorldUtil.getTileEntity((IBlockAccess)world, x, y, z);
					if (tile != null) {
						tile.readFromNBT(nbt);
						tile.setWorldObj(world);
						tile.xCoord = x;
						tile.yCoord = y;
						tile.zCoord = z;
					}
				}
			} else {
				WorldUtil.setBlockToAir(world, x, y, z);
			}
			WorldUtil.updateBlock(world, x, y, z);
		}

		private void postRotate() {
			MoveData moveData = this.ctrData.controller.getMoveData();
			int x = this.tileEntity.xCoord, y = this.tileEntity.yCoord, z = this.tileEntity.zCoord;
			World world = this.tileEntity.getWorldObj();
			PBlock block = WorldUtil.getPBlock((IBlockAccess)world, x, y, z);
			if (block instanceof ISpecialRotator) {
				((ISpecialRotator)block).postRotate(world, x, y, z, moveData.rotateDir, moveData.rotateSpeed, moveData.rotatePoint);
			} else {
				RotateUtil.rotateVanillaBlocks(world, x, y, z, this.elementMeta, moveData.rotateDir);
			}
		}

		private void postMove() {
			WorldUtil.setBlockMeta(this.tileEntity.getWorldObj(), this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord, this.elementMeta, 3);
		}

		public float getOffsetX(float ticktime) {
			MoveData moveData = this.ctrData.controller.getMoveData();
			if (!isMoving())
				return 0.0F;
			return Facing.offsetsXForSide[moveData.moveDir ^ 0x1] * (1.0F - getProgressForRender(ticktime));
		}

		public float getOffsetY(float ticktime) {
			MoveData moveData = this.ctrData.controller.getMoveData();
			if (!isMoving())
				return 0.0F;
			return Facing.offsetsYForSide[moveData.moveDir ^ 0x1] * (1.0F - getProgressForRender(ticktime));
		}

		public float getOffsetZ(float ticktime) {
			MoveData moveData = this.ctrData.controller.getMoveData();
			if (!isMoving())
				return 0.0F;
			return Facing.offsetsZForSide[moveData.moveDir ^ 0x1] * (1.0F - getProgressForRender(ticktime));
		}

		public void writeToNBT(NBTTagCompound tagCompound) {
			super.writeToNBT(tagCompound);
			if (this.ctrData.key == null && this.ctrData.controller != null)
				this.ctrData.key = this.ctrData.controller.getKey();
			if (this.ctrData.key == null)
				this.ctrData.key = "default";
			tagCompound.setString("ctrKey", this.ctrData.key);
			if (this.ctrData.controller != null) {
				MoveData moveData = this.ctrData.controller.getMoveData();
				moveData.writeToNBT(tagCompound);
			}
		}

		public void readFromNBT(NBTTagCompound tagCompound) {
			super.readFromNBT(tagCompound);
			this.ctrData.key = tagCompound.getString("ctrKey");
			if (this.ctrData.controller == null && !this.ctrData.key.equals("default")) {
				MoveData moveData = new MoveData(tagCompound);
				if (this.tileEntity != null && this.tileEntity.getWorldObj() != null) {
					boolean isRemote = (this.tileEntity.getWorldObj()).isRemote;
					this.ctrData.controller = ControllerRegistry.create(this.ctrData.key, moveData, isRemote);
				}
			}
			this.tempNBTCompound = tagCompound;
		}

		public void postLoad() {
			super.postLoad();
			if (this.ctrData.controller == null && this.tempNBTCompound != null) {
				MoveData moveData = new MoveData(this.tempNBTCompound);
				if (this.tileEntity != null && this.tileEntity.getWorldObj() != null) {
					boolean isRemote = (this.tileEntity.getWorldObj()).isRemote;
					this.ctrData.controller = ControllerRegistry.create(this.ctrData.key, moveData, isRemote);
				}
			}
		}
	}


