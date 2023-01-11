package gregtech.api.worldgen2.definition;

import gregtech.api.worldgen2.context.TestWorldgenContext;
import gregtech.api.worldgen2.generator.IWorldGenerator;
import gregtech.api.worldgen2.generator.TestWorldGenerator;

import javax.annotation.Nonnull;

public class TestWorldgenDefinition implements IWorldgenDefinition<TestWorldgenContext> {

    private final TestWorldgenContext context;

    public TestWorldgenDefinition(@Nonnull TestWorldgenContext context) {
        this.context = context;
    }

    @Nonnull
    @Override
    public IWorldGenerator<TestWorldgenContext> getGenerator() {
        return TestWorldGenerator.INSTANCE;
    }

    @Nonnull
    @Override
    public TestWorldgenContext getContext() {
        return this.context;
    }
}
