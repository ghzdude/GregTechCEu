package gregtech.common.worldgen.context;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.Material;
import gregtech.api.worldgen2.context.BaseWorldgenContext;
import gregtech.api.worldgen2.placement.BlockStatePlacer;
import gregtech.api.worldgen2.placement.IWorldgenPlaceable;
import gregtech.api.worldgen2.placement.MaterialOreBlockPlacer;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Context for an {@link gregtech.common.worldgen.definition.MixedVeinDefinition}
 */
public class MixedVeinContext extends BaseWorldgenContext {

    private final List<Object2IntMap.Entry<IWorldgenPlaceable>> toGenerate;

    public MixedVeinContext(int weight, byte density, short minY, short maxY, List<Object2IntMap.Entry<IWorldgenPlaceable>> toGenerate) {
        super(weight, density, minY, maxY);
        this.toGenerate = toGenerate;
    }

    @Nonnull
    public List<Object2IntMap.Entry<IWorldgenPlaceable>> getToGenerate() {
        return this.toGenerate;
    }

    public static class Builder extends BaseWorldgenContext.Builder<MixedVeinContext> {

        private final Object2IntMap<IWorldgenPlaceable> toGenerate = new Object2IntOpenHashMap<>();

        public Builder(int weight, byte density, short minY, short maxY) {
            super(weight, density, minY, maxY);
        }

        /**
         * @param state the state to place
         * @param weight the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull IBlockState state, int weight) {
            return entry(state, null, weight);
        }

        /**
         * @param state the state to place
         * @param weight the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull IBlockState state, @Nullable Predicate<IBlockState> replacementPredicate, int weight) {
            Preconditions.checkArgument(weight > 0, "Weight must be > 0");
            IWorldgenPlaceable placer = new BlockStatePlacer(state, replacementPredicate);
            if (toGenerate.containsKey(placer)) throw new IllegalArgumentException("BlockPlacer for BlockState " + state + " is already present");
            toGenerate.put(placer, weight);
            return this;
        }

        /**
         * @param material the material of the ore to place
         * @param weight the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull Material material, int weight) {
            Preconditions.checkArgument(weight > 0, "Weight must be > 0");
            IWorldgenPlaceable placer = new MaterialOreBlockPlacer(material);
            if (toGenerate.containsKey(placer)) throw new IllegalArgumentException("BlockPlacer for Material " + material + " is already present");
            toGenerate.put(placer, weight);
            return this;
        }

        @Nonnull
        @Override
        public MixedVeinContext build() {
            if (toGenerate.isEmpty()) throw new IllegalStateException("Mixed Veins must have at least one entry to generate");
            return new MixedVeinContext(weight, density, minY, maxY, new ArrayList<>(toGenerate.object2IntEntrySet()));
        }
    }
}
