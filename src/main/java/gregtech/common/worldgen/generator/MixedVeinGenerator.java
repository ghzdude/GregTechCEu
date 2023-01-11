package gregtech.common.worldgen.generator;

import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import gregtech.common.worldgen.context.MixedVeinContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * Generator for an {@link gregtech.common.worldgen.definition.MixedVeinDefinition}
 */
public final class MixedVeinGenerator implements IGTWorldGenerator<MixedVeinContext> {

    public static final MixedVeinGenerator INSTANCE = new MixedVeinGenerator();

    private MixedVeinGenerator() {/**/}

    @Override
    public boolean generate(@Nonnull MixedVeinContext context, @Nonnull World world, @Nonnull Random random, int startX, int startZ, short startY) {
        final short height = context.getHeight();

        List<MixedVeinContext.Entry> toGenerate = context.getToGenerate();
        final int totalWeight = toGenerate.stream().mapToInt(MixedVeinContext.Entry::getWeight).sum();
        final byte density = context.getDensity();

        boolean generated = false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (short offsetY = 0; offsetY < height; offsetY++) {
            for (byte offsetX = 0; offsetX < 16; offsetX++) {
                for (byte offsetZ = 0; offsetZ < 16; offsetZ++) {
                    // check density
                    if (random.nextInt(100) >= density) continue;

                    short y = (short) (startY + offsetY);
                    int x = startX + offsetX;
                    int z = startZ + offsetZ;
                    pos.setPos(x, y, z);

                    // weighted selection for the block to place
                    boolean didGenerate;
                    int weight = random.nextInt(totalWeight);
                    for (MixedVeinContext.Entry entry : toGenerate) {
                        weight -= entry.getWeight();
                        if (weight <= 0) {
                            didGenerate = entry.getPlaceable().placeBlock(this, world, pos);
                            generated = generated || didGenerate;
                            if (didGenerate) break;
                        }
                    }
                }
            }
        }

        return generated;
    }
}
