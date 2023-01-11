package gregtech.api.worldgen2.generator;

import com.google.common.base.Predicate;
import gregtech.api.worldgen2.context.IWorldgenContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * A generator which places blocks in the world for an {@link gregtech.api.worldgen.config.IWorldgenDefinition}
 * @param <T> the context used to generate with
 */
@FunctionalInterface
public interface IWorldGenerator<T extends IWorldgenContext> {

    /**
     * Attempts to place a block in world
     *
     * @param world                the world to place the block in
     * @param pos                  the position to place at
     * @param toPlace              the state to place
     * @param replacementPredicate tests whether the block currently at the position is valid to replace
     * @return if placement was successful
     */
    default boolean placeBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState toPlace, @SuppressWarnings("Guava") @Nonnull com.google.common.base.Predicate<IBlockState> replacementPredicate) {
        IBlockState existing = world.getBlockState(pos);
        if (!canGenerateInAir() && existing.getBlock().isAir(existing, world, pos)) {
            return false;
        }

        if (existing.getBlock().isReplaceableOreGen(existing, world, pos, replacementPredicate)) {
            world.setBlockState(pos, toPlace, 2 | 16);
            return true;
        }
        return false;
    }

    /**
     * Generate an ore at a position. Use {@link IWorldGenerator#placeBlock(World, BlockPos, IBlockState, Predicate)} to place blocks.
     *
     * @param context the context for generation
     * @param world   the world to generate in
     * @param rand    the random to use
     * @param startX  the starting X coordinate to generate in, already offset to prevent cascading
     * @param startZ  the starting Z coordinate to generate in, already offset to prevent cascading
     * @return if generation was successful
     */
    boolean generate(@Nonnull T context, @Nonnull World world, @Nonnull Random rand, int startX, int startZ);

    /**
     * @return if this generator is allowed to replace air blocks
     */
    default boolean canGenerateInAir() {
        return false;
    }
}
