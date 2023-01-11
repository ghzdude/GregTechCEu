package gregtech.common.worldgen.definition;

import gregtech.api.worldgen2.definition.IWorldgenDefinition;
import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import gregtech.common.worldgen.context.LayeredVeinContext;
import gregtech.common.worldgen.generator.LayeredVeinGenerator;

import javax.annotation.Nonnull;

/**
 * A worldgen definition for a vein of any number of weighted blocks
 */
public class LayeredVeinDefinition implements IWorldgenDefinition<LayeredVeinContext> {

    private final LayeredVeinContext context;

    public LayeredVeinDefinition(@Nonnull LayeredVeinContext context) {
        this.context = context;
    }

    @Nonnull
    @Override
    public IGTWorldGenerator<LayeredVeinContext> getGenerator() {
        return LayeredVeinGenerator.INSTANCE;
    }

    @Nonnull
    @Override
    public LayeredVeinContext getContext() {
        return this.context;
    }
}
