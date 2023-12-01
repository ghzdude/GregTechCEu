package gregtech.common.entities;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.metatileentities.miner.Miner;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Section of the mining pipe. The pipe should be divided into multiple segments due to a naive algorithm used in AABB
 * checks making exceptionally large bounding boxes not work properly
 */
public class MiningPipeEntity<MTE extends MetaTileEntity & Miner> extends Entity {

    @Nullable
    private final MTE mte;
    private final BlockPos origin;

    public int y;
    public int length;
    public boolean end;

    private int prevLength = -1;

    protected MiningPipeEntity(@NotNull World world, @Nullable MTE mte, @NotNull BlockPos origin) {
        super(world);
        this.setSize(.5f, 0);
        this.setNoGravity(true);
        this.noClip = true;
        this.preventEntitySpawning = true;
        this.setEntityInvulnerable(true);

        this.mte = mte;
        this.origin = origin;
    }

    @SuppressWarnings("unused")
    public MiningPipeEntity(@NotNull World world) {
        this(world, null, BlockPos.ORIGIN);
    }

    public MiningPipeEntity(@NotNull MTE mte, @NotNull BlockPos origin) {
        this(mte.getWorld(), mte, origin.toImmutable());
        this.setPosition(this.origin.getX() + .5, this.origin.getY(), this.origin.getZ() + .5);
    }

    @Nullable
    public MTE getMTE() {
        return mte;
    }

    @NotNull
    public BlockPos getOrigin() {
        return origin;
    }

    @Override
    protected void entityInit() {}

    @Override
    public void onUpdate() {
        if (this.mte == null || !this.mte.isValid()) {
            setDead();
            return;
        }

        int length = this.length;
        if (length != this.prevLength) {
            this.prevLength = length;
            setPosition(this.posX, this.y - length, this.posZ);
            setSize(.5f, length);
        }

        this.firstUpdate = false;
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @NotNull
    @Override
    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBox(@NotNull Entity entity) {
        return entity.canBePushed() ? entity.getEntityBoundingBox() : null;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public Entity changeDimension(int dim, @NotNull ITeleporter teleporter) {
        return this;
    }

    @Override
    protected void readEntityFromNBT(@NotNull NBTTagCompound tag) {}

    @Override
    protected void writeEntityToNBT(@NotNull NBTTagCompound tag) {}
}
