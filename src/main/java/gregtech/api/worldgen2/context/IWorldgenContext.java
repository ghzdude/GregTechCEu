package gregtech.api.worldgen2.context;

import gregtech.api.worldgen2.generator.IWorldGenerator;

/**
 * Context used for an {@link IWorldGenerator} to place blocks
 */
public interface IWorldgenContext {

    /**
     * @return the minimum y value for generation
     */
    short getMinY();

    /**
     * @return the maximum y value for generation
     */
    short getMaxY();

    /**
     * @return the weight for generation
     */
    int getWeight();
}
