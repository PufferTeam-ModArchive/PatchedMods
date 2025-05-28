package letiu.pistronics.piston;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import letiu.pistronics.util.Vector3;
import letiu.pistronics.util.VectorUtil;

public class MoveData {

    public int rotateDir;

    public int moveDir;

    public float angle;

    public float progress;

    public float rotateSpeed;

    public float moveSpeed;

    public Vector3 rotatePoint;

    public MoveData(int moveDir, float moveSpeed) {
        this.rotateDir = -1;
        this.moveDir = -1;
        this.moveDir = moveDir;
        this.moveSpeed = moveSpeed;
    }

    public MoveData(int rotateDir, float rotateSpeed, Vector3 rotatePoint) {
        this.rotateDir = -1;
        this.moveDir = -1;
        this.rotateDir = rotateDir;
        this.rotateSpeed = rotateSpeed;
        this.rotatePoint = rotatePoint;
    }

    public MoveData(NBTTagCompound nbt) {
        this.rotateDir = -1;
        this.moveDir = -1;
        readFromNBT(nbt);
    }

    public MoveData() {
        this.rotateDir = -1;
        this.moveDir = -1;
    }

    public void update() {
        if (!isDone()) if (isRotating()) {
            this.angle += this.rotateSpeed;
        } else if (isMoving()) {
            this.progress += this.moveSpeed;
        }
    }

    public boolean isDone() {
        return (this.angle >= 90.0F || this.progress >= 1.0F);
    }

    public boolean isMoving() {
        return (this.moveDir != -1 && this.moveSpeed != 0.0F);
    }

    public boolean isRotating() {
        return (this.rotateDir != -1 && this.rotateSpeed != 0.0F);
    }

    public float getValue() {
        return isMoving() ? this.progress : this.angle;
    }

    public void writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("rotateDir", this.rotateDir);
        nbt.setInteger("moveDir", this.moveDir);
        nbt.setFloat("angle", this.angle);
        nbt.setFloat("progress", this.progress);
        nbt.setFloat("rotateSpeed", this.rotateSpeed);
        nbt.setFloat("moveSpeed", this.moveSpeed);
        if (this.rotatePoint != null) nbt.setTag("rotatePoint", (NBTBase) VectorUtil.writeToNBT(this.rotatePoint));
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.rotateDir = nbt.getInteger("rotateDir");
        this.moveDir = nbt.getInteger("moveDir");
        this.angle = nbt.getFloat("angle");
        this.rotateSpeed = nbt.getFloat("rotateSpeed");
        this.moveSpeed = nbt.getFloat("moveSpeed");
        this.progress = nbt.getFloat("progress");
        if (nbt.hasKey("rotatePoint")) this.rotatePoint = VectorUtil.readFromNBT(nbt.getCompoundTag("rotatePoint"));
    }
}
