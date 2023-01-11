package gregtech.api.worldgen;

import gregtech.Bootstrap;
import gregtech.api.worldgen2.GregTechWorldGen;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static gregtech.api.worldgen2.GregTechWorldGen.getChunkOrigin;

public class WorldgenTest {

    @BeforeEach
    public void prepare() {
        Bootstrap.perform();
        GregTechWorldGen.init();
    }

    @Test
    public void testChunkOrigin() {
        // grid size 3
        for (int x = -1; x <= 1; x++) {
            int originX = getChunkOrigin(x, (byte) 3);

            MatcherAssert.assertThat(originX, CoreMatchers.is(0));
        }

        // these are part of different grids
        MatcherAssert.assertThat(getChunkOrigin(-2, (byte) 3), CoreMatchers.is(-1));
        MatcherAssert.assertThat(getChunkOrigin(2, (byte) 3), CoreMatchers.is(1));

        // grid size 5
        for (int x = -2; x <= 2; x++) {
            int originX = getChunkOrigin(x, (byte) 5);

            MatcherAssert.assertThat(originX, CoreMatchers.is(0));
        }

        // these are part of different grids
        MatcherAssert.assertThat(getChunkOrigin(-3, (byte) 5), CoreMatchers.is(-1));
        MatcherAssert.assertThat(getChunkOrigin(3, (byte) 5), CoreMatchers.is(1));
    }

//    @Test
//    public void simulateWorldgen() {
//        GregTechWorldGen worldGen = GregTechWorldGen.getInstance();
//
//        World world = new DummyWorld();
//        Random random = GTValues.RNG;
//        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
//
//        for (int x = -1; x <= 1; x++) {
//            for (int z = -1; z <= 1; z++) {
//                pos.setPos(x * 16, 0, z * 16);
//                worldGen.generateOres(new OreGenEvent.Pre(world, random, pos));
//            }
//        }
//    }
}
