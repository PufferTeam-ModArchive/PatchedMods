package letiu.modbase.entities;

import letiu.modbase.render.UniversalEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class BaseEntity extends Entity {

	public BaseEntity(World world) {
		super(world);
	}

	public BaseEntity(World world, double x, double y, double z) {
		super(world);
	}

	protected void entityInit() {
	}

	public UniversalEntityRenderer getRenderer() {
		return null;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	public void onUpdate() {
	}

	protected void readEntityFromNBT(NBTTagCompound var1) {
	}

	protected void writeEntityToNBT(NBTTagCompound var1) {
	}
}
