package gregtech.api.worldgen2.context;

import com.google.common.base.Preconditions;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

public class TestWorldgenContext implements IWorldgenContext {

    private final int weight;
    private final short minY;
    private final short maxY;
    private final IBlockState toGenerate;

    public TestWorldgenContext(int weight, short minY, short maxY, @Nonnull IBlockState toGenerate) {
        this.weight = weight;
        Preconditions.checkArgument(minY < maxY, "MinY must be < MaxY");
        this.minY = minY;
        this.maxY = maxY;
        this.toGenerate = toGenerate;
    }

    @Override
    public short getMinY() {
        return this.minY;
    }

    @Override
    public short getMaxY() {
        return this.maxY;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Nonnull
    public IBlockState getToGenerate() {
        return this.toGenerate;
    }
}
