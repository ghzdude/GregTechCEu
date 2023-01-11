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
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Context for an {@link gregtech.common.worldgen.definition.MixedVeinDefinition}
 */
public class MixedVeinContext extends BaseWorldgenContext {

    private final List<MixedVeinContext.Entry> toGenerate;

    /**
     * @param weight     the weight of the vein
     * @param density    the density of the vein
     * @param minY       the minimum y for generation of the vein
     * @param maxY       the maximum y for generation of the vein
     * @param height     the height of the vein
     * @param toGenerate the entries to generate
     * @see MixedVeinContext.Builder
     */
    public MixedVeinContext(int weight, byte density, short minY, short maxY, short height, List<MixedVeinContext.Entry> toGenerate) {
        super(weight, density, minY, maxY, height);
        this.toGenerate = toGenerate;
    }

    @Nonnull
    public List<MixedVeinContext.Entry> getToGenerate() {
        return this.toGenerate;
    }

    /**
     * An entry for {@link MixedVeinContext}
     */
    public static class Entry {

        private final IWorldgenPlaceable placeable;
        private final int weight;

        protected Entry(@Nonnull IWorldgenPlaceable placeable, int weight) {
            this.placeable = placeable;
            this.weight = weight;
        }

        /**
         * @return the placeable for the entry
         */
        @Nonnull
        public IWorldgenPlaceable getPlaceable() {
            return placeable;
        }

        /**
         * @return the weight of the entry
         */
        public int getWeight() {
            return weight;
        }

        @Override
        public int hashCode() {
            return placeable.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Entry entry = (Entry) o;
            return weight == entry.getWeight() && placeable.equals(entry.getPlaceable());
        }
    }

    /**
     * A builder for {@link MixedVeinContext}
     */
    public static class Builder extends BaseWorldgenContext.Builder<MixedVeinContext> {

        private final Object2IntMap<IWorldgenPlaceable> toGenerate = new Object2IntOpenHashMap<>();

        /**
         * @param weight  the weight of the vein
         * @param density the density of the vein, from 1 to 100 (inclusive)
         * @param minY    the minimum y value for the vein, inclusive
         * @param maxY    the maximum y value for the vein, exclusive
         * @param height  the height of the vein
         */
        public Builder(int weight, byte density, short minY, short maxY, short height) {
            super(weight, density, minY, maxY, height);
        }

        /**
         * @param state  the state to place
         * @param weight the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull IBlockState state, int weight) {
            return entry(state, null, weight);
        }

        /**
         * @param state  the state to place
         * @param weight the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull IBlockState state, @Nullable Predicate<IBlockState> replacementPredicate, int weight) {
            IWorldgenPlaceable placeable = new BlockStatePlacer(state, replacementPredicate);
            return entry(placeable, weight);
        }

        /**
         * @param material the material of the ore to place
         * @param weight   the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull Material material, int weight) {
            IWorldgenPlaceable placer = new MaterialOreBlockPlacer(material);
            return entry(placer, weight);
        }

        /**
         * @param placeable the placeable
         * @param weight    the weight of this entry
         * @return this
         */
        @Nonnull
        public Builder entry(@Nonnull IWorldgenPlaceable placeable, int weight) {
            Preconditions.checkArgument(weight > 0, "Weight must be > 0");
            if (toGenerate.containsKey(placeable)) {
                throw new IllegalArgumentException("WorldgenPlaceable " + placeable + " already exists");
            }
            toGenerate.put(placeable, weight);
            return this;
        }

        @Nonnull
        @Override
        public MixedVeinContext build() {
            if (toGenerate.isEmpty())
                throw new IllegalStateException("Mixed Veins must have at least one entry to generate");
            return new MixedVeinContext(weight, density, minY, maxY, height,
                    toGenerate.object2IntEntrySet().stream()
                            .map(entry -> new MixedVeinContext.Entry(entry.getKey(), entry.getIntValue()))
                            .collect(Collectors.toList()));
        }
    }
}
