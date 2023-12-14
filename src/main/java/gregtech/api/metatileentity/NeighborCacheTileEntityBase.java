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

    private final EnumMap<EnumFacing, CacheData> neighbors = new EnumMap<>(EnumFacing.class);
    private boolean neighborsInvalidated = false;

    public NeighborCacheTileEntityBase() {
        invalidateNeighbors();
    }

    protected void invalidateNeighbors() {
        if (!this.neighborsInvalidated) {
            for (EnumFacing facing : EnumFacing.values()) {
                var cacheData = this.neighbors.computeIfAbsent(facing, k -> new CacheData());
                cacheData.clearCache();
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
        var cacheData = this.neighbors.get(facing);
        var neighbor = cacheData.getCache();
        if (!cacheData.isChecked() || (neighbor != null && neighbor.isInvalid())) {
            neighbor = world.getTileEntity(pos.offset(facing));
            cacheData.setCache(neighbor);
            cacheData.setChecked(true);
            this.neighborsInvalidated = false;
        }
        return neighbor;
    }

    public void onNeighborChanged(@NotNull EnumFacing facing) {
        this.neighbors.get(facing).clearCache();
    }

    private static class CacheData {
        private TileEntity cache = null;
        private boolean checked = false;

        public boolean isChecked () {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public TileEntity getCache() {
            return this.cache;
        }

        public void setCache(TileEntity cache) {
            if (this.cache != null) {
                this.cache.invalidate();
            }
            this.cache = cache;
        }

        public void clearCache() {
            this.cache = null;
            this.checked = false;
        }
    }
}
