package gregtech.api.worldgen2.generator;

import gregtech.api.worldgen2.context.IWorldgenContext;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * A generator which places blocks in the world for an {@link gregtech.api.worldgen.config.IWorldgenDefinition}
 * @param <T> the context used to generate with
 */
@FunctionalInterface
public interface IGTWorldGenerator<T extends IWorldgenContext> {

    /**
     * Generate an ore at a position. Use {@link gregtech.api.worldgen2.placement.IWorldgenPlaceable} to place blocks.
     *
     * @param context the context for generation
     * @param world   the world to generate in
     * @param random  the random to use
     * @param startX  the starting X coordinate to generate in, already offset to prevent cascading
     * @param startZ  the starting Z coordinate to generate in, already offset to prevent cascading
     * @return if generation was successful
     */
    boolean generate(@Nonnull T context, @Nonnull World world, @Nonnull Random random, int startX, int startZ);

    /**
     * @return if this generator is allowed to replace air blocks
     */
    default boolean canGenerateInAir() {
        return false;
    }
}
