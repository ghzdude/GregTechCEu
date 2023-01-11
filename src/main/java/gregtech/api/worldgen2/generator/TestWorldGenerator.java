package gregtech.api.worldgen2.generator;

import gregtech.api.worldgen2.context.TestWorldgenContext;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class TestWorldGenerator implements IWorldGenerator<TestWorldgenContext> {

    public static final TestWorldGenerator INSTANCE = new TestWorldGenerator();

    @SuppressWarnings("Guava")
    private final com.google.common.base.Predicate<IBlockState> canGenerate = state -> true;

    protected TestWorldGenerator() {/**/}

    @Override
    public boolean generate(@Nonnull TestWorldgenContext context, @Nonnull World world, @Nonnull Random rand, int startX, int startZ) {
        final short startY = context.getMinY();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (short offsetY = 0; offsetY < context.getMaxY(); offsetY++) {
            for (byte offsetX = 0; offsetX < 16; offsetX++) {
                for (byte offsetZ = 0; offsetZ < 16; offsetZ++) {
                    int y = startY + offsetY;
                    int x = startX + offsetX;
                    int z = startZ + offsetZ;

                    pos.setPos(x, y, z);
                    placeBlock(world, pos, context.getToGenerate(), canGenerate);
                }
            }
        }

        return true;
    }
}
