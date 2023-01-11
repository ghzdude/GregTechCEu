package gregtech.common.worldgen.definition;

import gregtech.api.worldgen2.definition.IWorldgenDefinition;
import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import gregtech.common.worldgen.context.MixedVeinContext;
import gregtech.common.worldgen.generator.MixedVeinGenerator;

import javax.annotation.Nonnull;

/**
 * A worldgen definition for a vein of any number of weighted blocks
 */
public class MixedVeinDefinition implements IWorldgenDefinition<MixedVeinContext> {

    private final MixedVeinContext context;

    public MixedVeinDefinition(@Nonnull MixedVeinContext context) {
        this.context = context;
    }

    @Nonnull
    @Override
    public IGTWorldGenerator<MixedVeinContext> getGenerator() {
        return MixedVeinGenerator.INSTANCE;
    }

    @Nonnull
    @Override
    public MixedVeinContext getContext() {
        return this.context;
    }
}
