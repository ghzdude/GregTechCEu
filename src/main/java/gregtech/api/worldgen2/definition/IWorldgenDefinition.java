package gregtech.api.worldgen2.definition;

import gregtech.api.worldgen2.context.IWorldgenContext;
import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * A definition of an object which can be generated in the world
 * @param <T> the context used for this type of definition
 */
public interface IWorldgenDefinition<T extends IWorldgenContext> {

    /**
     * @return the generator for this definition
     */
    @Nonnull
    IGTWorldGenerator<T> getGenerator();

    /**
     * @return the context for generation
     */
    @Nonnull
    T getContext();

    /**
     * Default helper method for having this definition generate
     *
     * @param world  the world to generate in
     * @param random the random to generate with
     * @param startX the starting x coordinate, adjusted for cascading
     * @param startZ the starting z coordinate, adjusted for cascading
     * @param startY the starting y coordinate
     * @return if generation was successful
     */
    default boolean generate(@Nonnull World world, @Nonnull Random random, int startX, int startZ, short startY) {
        return getGenerator().generate(getContext(), world, random, startX, startZ, startY);
    }
}
