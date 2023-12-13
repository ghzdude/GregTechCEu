package gregtech.api.metatileentity;

import gregtech.api.metatileentity.interfaces.INeighborCache;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public abstract class NeighborCacheTileEntityBase extends SyncedTileEntityBase implements INeighborCache {

    private final EnumMap<EnumFacing, TileEntity> neighbors = new EnumMap<>(EnumFacing.class);
    private boolean neighborsInvalidated = false;

    public NeighborCacheTileEntityBase() {
        invalidateNeighbors();
    }

    protected void invalidateNeighbors() {
        if (!this.neighborsInvalidated) {
            for (EnumFacing facing : EnumFacing.values()) {
                this.neighbors.put(facing, this);
            }
            this.neighborsInvalidated = true;
        }
    }

    @Override
    public void setWorld(@NotNull World worldIn) {
        super.setWorld(worldIn);
        invalidateNeighbors();
    }

    @Override
    public void setPos(@NotNull BlockPos posIn) {
        super.setPos(posIn);
        invalidateNeighbors();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        invalidateNeighbors();
    }

    @Override
    public @Nullable TileEntity getNeighbor(@NotNull EnumFacing facing) {
        if (world == null || pos == null) return null;
        var neighbor = this.neighbors.get(facing);
        if (neighbor == this || (neighbor != null && neighbor.isInvalid())) {
            neighbor = world.getTileEntity(pos.offset(facing));
            this.neighbors.put(facing, neighbor);
            this.neighborsInvalidated = false;
        }
        return neighbor;
    }

    public void onNeighborChanged(@NotNull EnumFacing facing) {
        this.neighbors.put(facing, this);
    }
}
