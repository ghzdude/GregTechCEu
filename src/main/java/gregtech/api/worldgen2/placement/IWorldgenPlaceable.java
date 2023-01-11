package gregtech.api.worldgen2.placement;

import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Interface representing something placeable in a World by world generation
 */
public interface IWorldgenPlaceable {

    /**
     * @param existing the existing block state
     * @param world    the world to use
     * @param pos      the pos to place at
     * @return the state to place
     */
    @Nonnull
    IBlockState getStateToPlace(@Nonnull IBlockState existing, @Nonnull World world, @Nonnull BlockPos pos);

    /**
     * @param state the state to check
     * @return if the state is allowed to be replaced for ore generation
     */
    boolean isStateReplaceable(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos);

    /**
     * Place a block at a position
     *
     * @param generator the generator being used
     * @param world     the world to place in
     * @param pos       the position to place at
     * @return if the block was successfully placed
     */
    default boolean placeBlock(@Nonnull IGTWorldGenerator<?> generator, @Nonnull World world, @Nonnull BlockPos pos) {
        IBlockState existing = world.getBlockState(pos);
        if (!generator.canGenerateInAir() && existing.getBlock().isAir(existing, world, pos)) {
            return false;
        }

        IBlockState toPlace = getStateToPlace(existing, world, pos);
        if (existing.getBlock().isReplaceableOreGen(existing, world, pos, state -> isStateReplaceable(state, world, pos))) {
            world.setBlockState(pos, toPlace, 2 | 16);
            return true;
        }
        return false;
    }
}
