package gregtech.api.worldgen2;

import com.google.common.base.Preconditions;
import gregtech.api.GTValues;
import gregtech.api.util.GTLog;
import gregtech.api.worldgen2.context.TestWorldgenContext;
import gregtech.api.worldgen2.definition.IWorldgenDefinition;
import gregtech.api.worldgen2.definition.TestWorldgenDefinition;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GregTechWorldGen {

    /**
     * Caches the random chunk aligned values.
     * Is equivalent to {@code Map<DimensionId, Map<BlockPos, RandomValue>>}, with BlockPos being stored with {@link BlockPos#toLong()}
     */
    private static final Int2ObjectMap<Long2IntFunction> randomValueCache = new Int2ObjectOpenHashMap<>();
    /**
     * Stores the chunks already generated in. This has entries removed when no longer needed.
     * Is equivalent to {@code Map<DimensionId, Set<BlockPos>>}, with BlockPos being stored with {@link BlockPos#toLong()}
     */
    private static final Int2ObjectMap<LongSet> generatedChunks = new Int2ObjectOpenHashMap<>();
    /**
     * Stores the grid size for each dimension.
     * Is equivalent to {@code Map<DimensionId, GridSize>}
     */
    private static final Int2ByteFunction dimensionGridSize = new Int2ByteOpenHashMap();


    private static GregTechWorldGen INSTANCE;
    /**
     * The random GT worldgen is based off of
     */
    private final Random random;

    /**
     * Stores the total definition weight by dimension.
     * Is equivalent to {@code Map<DimensionId, TotalWeight>}
     */
    private final Int2IntFunction totalWeight = new Int2IntOpenHashMap();
    /**
     * Stores the worldgen definitions for each dimension.
     * Is equivalent to {@code Map<DimensionId, List<IWorldgenDefinition<?>>>}
     */
    private final Int2ObjectMap<List<IWorldgenDefinition<?>>> definitions;

    protected GregTechWorldGen(@Nonnull Random random, @Nonnull Int2ObjectMap<List<IWorldgenDefinition<?>>> definitions) {
        this.random = random;
        Preconditions.checkArgument(!definitions.isEmpty(), "Definitions to generate must not be empty");
        this.definitions = definitions;
        for (Int2ObjectMap.Entry<List<IWorldgenDefinition<?>>> entry : definitions.int2ObjectEntrySet()) {
            totalWeight.put(entry.getIntKey(), entry.getValue().stream()
                    .mapToInt(o -> o.getContext().getWeight())
                    .sum());
        }
    }

    @Nonnull
    public static GregTechWorldGen getInstance() {
        return INSTANCE;
    }

    public static void init() {
        Int2ObjectMap<List<IWorldgenDefinition<?>>> definitions = new Int2ObjectOpenHashMap<>();
        List<IWorldgenDefinition<?>> list = new ArrayList<>();
        list.add(new TestWorldgenDefinition(new TestWorldgenContext(70, (short) 10, (short) 30, Blocks.IRON_BLOCK.getDefaultState())));
        list.add(new TestWorldgenDefinition(new TestWorldgenContext(20, (short) 30, (short) 50, Blocks.GOLD_BLOCK.getDefaultState())));
        list.add(new TestWorldgenDefinition(new TestWorldgenContext(10, (short) 50, (short) 70, Blocks.DIAMOND_BLOCK.getDefaultState())));
        definitions.put(0, list);

        INSTANCE = new GregTechWorldGen(GTValues.RNG, definitions);
        dimensionGridSize.put(0, (byte) 3);
        dimensionGridSize.put(Integer.MAX_VALUE, (byte) 3);

        MinecraftForge.ORE_GEN_BUS.register(getInstance());
    }

    /**
     * @param chunkCoord the chunk coordinate to convert
     * @param gridSize   the size of the grid
     * @return return the origin chunk coord in the chunk grid for the coordinate
     */
    public static int getChunkOrigin(int chunkCoord, byte gridSize) {
        if (chunkCoord >= 0) return (chunkCoord + gridSize / 2) / gridSize;
        else return (chunkCoord - gridSize / 2) / gridSize;
    }

    public static int toChunkCoordinate(int coordinate) {
        return coordinate / 16;
    }

    @SubscribeEvent
    public void generateOres(@Nonnull OreGenEvent.Pre event) {
        final World world = event.getWorld();
        final Random random = event.getRand();
        final BlockPos pos = event.getPos();

        final int dimension = world.provider.getDimension();

        final byte gridSize = dimensionGridSize.get(dimension);
        final int originX = getChunkOrigin(toChunkCoordinate(pos.getX()), gridSize);
        final int originZ = getChunkOrigin(toChunkCoordinate(pos.getZ()), gridSize);

        if (!generatedChunks.containsKey(dimension)) {
            generatedChunks.put(dimension, new LongOpenHashSet());
        }
        LongSet generatedPositions = generatedChunks.get(dimension);

        // already generated for this chunk
        long currentChunkPos = pos.toLong();
        if (!generatedPositions.contains(currentChunkPos)) {

            // generate the vein
            int weight = getGenerationWeightForPos(dimension, pos);
            boolean generated = false;
            for (IWorldgenDefinition<?> object : this.definitions.get(dimension)) {
                weight -= object.getContext().getWeight();
                if (weight <= 0) {
                    // offset by 8 blocks to prevent cascading
                    generated = object.generate(world, random, pos.getX() + 8, pos.getZ() + 8);
                    if (generated) break;
                }
            }
            if (!generated) {
                GTLog.logger.warn("Could not generate in chunk: [{}, {}]", originX, originZ);
            }

            // add this chunk as generated
            generatedPositions.add(currentChunkPos);
        }

        // cache cleaning

        final int radius = gridSize / 2;

        // make a collection of all the chunks for this grid area
        LongCollection visited = new LongOpenHashSet();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int chunkX = -radius; chunkX <= radius; chunkX++) {
            for (int chunkZ = -radius; chunkZ <= radius; chunkZ++) {
                mutableBlockPos.setPos((originX + chunkX) * 16, 0, (originZ + chunkZ) * 16);
                long chunkPos = mutableBlockPos.toLong();

                // if a chunk has not been generated yet, not ready to clear caches
                if (!generatedPositions.contains(chunkPos)) return;
                visited.add(chunkPos);
            }
        }

        // a full grid entry is completed, so clear it

        // remove the entire area from the generated chunk cache
        visited.forEach(generatedPositions::remove);
        // remove the entire area from the random value cache
        visited.forEach(randomValueCache.get(dimension)::remove);
    }

    /**
     * @param dimension the dimension to generate in
     * @param pos       the block pos to start generation in
     * @return the weight to use for selecting the world gen objects
     */
    private int getGenerationWeightForPos(int dimension, @Nonnull BlockPos pos) {
        final byte gridSize = dimensionGridSize.get(dimension);
        int x = getChunkOrigin(toChunkCoordinate(pos.getX()), gridSize);
        int z = getChunkOrigin(toChunkCoordinate(pos.getZ()), gridSize);

        long chunkPos = new BlockPos(x * 16, 0, z * 16).toLong();
        if (!randomValueCache.containsKey(dimension)) {
            randomValueCache.put(dimension, new Long2IntOpenHashMap());
        }

        Long2IntFunction randomCache = randomValueCache.get(dimension);
        if (!randomCache.containsKey(chunkPos)) {
            randomCache.put(chunkPos, random.nextInt(totalWeight.get(dimension)));
        }

        return randomCache.get(chunkPos);
    }
}
