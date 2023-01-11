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
     * @return the minimum y value for generation
     */
    short getMinY();

    /**
     * @return the maximum y value for generation
     */
    short getMaxY();
}
