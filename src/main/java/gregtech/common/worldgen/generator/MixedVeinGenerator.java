package gregtech.common.worldgen.generator;

import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import gregtech.api.worldgen2.placement.IWorldgenPlaceable;
import gregtech.common.worldgen.context.MixedVeinContext;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
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
    public boolean generate(@Nonnull MixedVeinContext context, @Nonnull World world, @Nonnull Random random, int startX, int startZ) {
        final short startY = context.getMinY();
        final short endYOffset = (short) (context.getMaxY() - context.getMinY());
        List<Object2IntMap.Entry<IWorldgenPlaceable>> toGenerate = context.getToGenerate();
        final int totalWeight = toGenerate.stream().mapToInt(Object2IntMap.Entry::getIntValue).sum();
        final int density = context.getDensity();

        boolean generated = false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (short offsetY = 0; offsetY < endYOffset; offsetY++) {
            for (byte offsetX = 0; offsetX < 16; offsetX++) {
                for (byte offsetZ = 0; offsetZ < 16; offsetZ++) {
                    // check density
                    if (random.nextInt(100) >= density) continue;

                    int y = startY + offsetY;
                    int x = startX + offsetX;
                    int z = startZ + offsetZ;
                    pos.setPos(x, y, z);

                    // weighted selection for the block to place
                    boolean didGenerate;
                    int weight = random.nextInt(totalWeight);
                    for (Object2IntMap.Entry<IWorldgenPlaceable> entry : toGenerate) {
                        weight -= entry.getIntValue();
                        if (weight <= 0) {
                            didGenerate = entry.getKey().placeBlock(this, world, pos);
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
