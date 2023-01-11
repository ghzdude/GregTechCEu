package gregtech.api.worldgen2.context;

import gregtech.api.worldgen2.generator.IGTWorldGenerator;

/**
 * Context used for an {@link IGTWorldGenerator} to place blocks
 */
public interface IWorldgenContext {

    /**
     * @return the weight for generation
     */
    int getWeight();

    /**
     * @return the density for generation
     */
    byte getDensity();

    /**
     * @return the min y value for generation
     */
    short getMinY();

    /**
     * @return the max y value for generation
     */
    short getMaxY();

    /**
     * @return the height for generation
     */
    short getHeight();
}
