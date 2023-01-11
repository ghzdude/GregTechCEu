package gregtech.common.worldgen.generator;

import gregtech.api.worldgen2.generator.IGTWorldGenerator;
import gregtech.common.worldgen.context.LayeredVeinContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Generator for an {@link gregtech.common.worldgen.definition.MixedVeinDefinition}
 */
public final class LayeredVeinGenerator implements IGTWorldGenerator<LayeredVeinContext> {

    public static final LayeredVeinGenerator INSTANCE = new LayeredVeinGenerator();

    private LayeredVeinGenerator() {/**/}

    @Override
    public boolean generate(@Nonnull LayeredVeinContext context, @Nonnull World world, @Nonnull Random random, int startX, int startZ, short startY) {
        final short height = context.getHeight();

        final LayeredVeinContext.Entry top = context.getTop();
        final LayeredVeinContext.Entry middle = context.getMiddle();
        final LayeredVeinContext.Entry bottom = context.getBottom();
        final LayeredVeinContext.Entry spread = context.getSpread();
        final byte density = context.getDensity();
        final byte topDensity = top.getDensity();
        final byte middleDensity = middle.getDensity();
        final byte bottomDensity = bottom.getDensity();
        final byte spreadDensity = spread.getDensity();

        boolean generated = false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (short offsetY = 0; offsetY < height; offsetY++) {
            for (byte offsetX = 0; offsetX < 16; offsetX++) {
                for (byte offsetZ = 0; offsetZ < 16; offsetZ++) {
                    if (random.nextInt(100) >= density) continue;

                    short y = (short) (startY + offsetY);
                    int x = startX + offsetX;
                    int z = startZ + offsetZ;
                    pos.setPos(x, y, z);

                    boolean placed = false;
                    if (offsetY >= bottom.getMinY() && offsetY <= bottom.getMaxY()) {
                        if (random.nextInt(100) <= bottomDensity) {
                            placed = bottom.getPlaceable().placeBlock(this, world, pos);
                        }
                    }
                    if (!placed && offsetY >= middle.getMinY() && offsetY <= middle.getMaxY()) {
                        if (random.nextInt(100) <= middleDensity) {
                            placed = middle.getPlaceable().placeBlock(this, world, pos);
                        }
                    }
                    if (!placed && offsetY >= top.getMinY() && offsetY <= top.getMaxY()) {
                        if (random.nextInt(100) <= topDensity) {
                            placed = top.getPlaceable().placeBlock(this, world, pos);
                        }
                    }
                    if (!placed && offsetY >= spread.getMinY() && offsetY <= spread.getMaxY()) {
                        if (random.nextInt(100) <= spreadDensity) {
                            placed = spread.getPlaceable().placeBlock(this, world, pos);
                        }
                    }
                    generated = generated || placed;
                }
            }
        }

        return generated;
    }
}
