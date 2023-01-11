package gregtech.api.worldgen2.placement;

import gregtech.api.unification.ore.StoneType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Placer for an {@link IBlockState}
 */
public class BlockStatePlacer implements IWorldgenPlaceable {

    private final IBlockState state;
    private final Predicate<IBlockState> replacementPredicate;

    /**
     * @param state                the state to place
     * @param replacementPredicate a predicate for specifying replaceable blocks, otherwise allow all stone type blocks
     */
    public BlockStatePlacer(@Nonnull IBlockState state, @Nullable Predicate<IBlockState> replacementPredicate) {
        this.state = state;
        this.replacementPredicate = replacementPredicate;
    }

    @Nonnull
    public IBlockState getState() {
        return state;
    }

    @Nonnull
    @Override
    public IBlockState getStateToPlace(@Nonnull IBlockState existing, @Nonnull World world, @Nonnull BlockPos pos) {
        return this.state;
    }

    @Override
    public boolean isStateReplaceable(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        if (this.replacementPredicate == null) return StoneType.computeStoneType(state, world, pos) != null;
        return this.replacementPredicate.test(state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockStatePlacer that = (BlockStatePlacer) o;
        return state.equals(that.getState());
    }
}
