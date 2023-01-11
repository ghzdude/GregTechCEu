package gregtech.common.worldgen.context;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.Material;
import gregtech.api.worldgen2.context.BaseWorldgenContext;
import gregtech.api.worldgen2.placement.BlockStatePlacer;
import gregtech.api.worldgen2.placement.IWorldgenPlaceable;
import gregtech.api.worldgen2.placement.MaterialOreBlockPlacer;
import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;

public class LayeredVeinContext extends BaseWorldgenContext {

    private final Entry top;
    private final Entry middle;
    private final Entry bottom;
    private final Entry spread;

    /**
     * @param weight  the weight of the vein
     * @param density the density of the vein
     * @param minY    the minimum y for generation of the vein
     * @param maxY    the maximum y for generation of the vein
     * @param height  the height of the vein
     * @param top     the entry to generate on top
     * @param middle  the entry to generate on middle
     * @param bottom  the entry to generate on bottom
     * @param spread  the entry to generate throughout
     * @see LayeredVeinContext.Builder
     */
    protected LayeredVeinContext(int weight, byte density, short minY, short maxY, short height, @Nonnull Entry top,
                                 @Nonnull Entry middle, @Nonnull Entry bottom, @Nonnull Entry spread) {
        super(weight, density, minY, maxY, height);
        this.top = top;
        this.middle = middle;
        this.bottom = bottom;
        this.spread = spread;
    }

    @Nonnull
    public Entry getTop() {
        return top;
    }

    @Nonnull
    public Entry getMiddle() {
        return middle;
    }

    @Nonnull
    public Entry getBottom() {
        return bottom;
    }

    @Nonnull
    public Entry getSpread() {
        return spread;
    }

    /**
     * Types of values for {@link LayeredVeinContext}
     */
    public enum Type {
        TOP,
        MIDDLE,
        BOTTOM,
        SPREAD
    }

    /**
     * Entry for {@link LayeredVeinContext}
     */
    public static class Entry {

        private final Type type;
        private final IWorldgenPlaceable placeable;
        private final short minY;
        private final short maxY;
        private final byte density;

        protected Entry(@Nonnull Type type, @Nonnull IWorldgenPlaceable placeable, short minY, short maxY, byte density) {
            this.type = type;
            this.placeable = placeable;
            if (minY < 0 && maxY < 0) {
                switch (type) {
                    case TOP: {
                        this.minY = 4;
                        this.maxY = 6;
                        break;
                    }
                    case MIDDLE: {
                        this.minY = 2;
                        this.maxY = 4;
                        break;
                    }
                    case BOTTOM: {
                        this.minY = 0;
                        this.maxY = 2;
                        break;
                    }
                    case SPREAD: {
                        this.minY = 0;
                        this.maxY = 6;
                        break;
                    }
                    default:
                        throw new IllegalStateException("Type was not a valid state");
                }
            } else {
                Preconditions.checkArgument(maxY - minY != 0, "Height cannot be zero.");
                this.minY = minY;
                this.maxY = maxY;
            }
            Preconditions.checkArgument(density != 0, "Density must be != 0");
            if (density < 0) {
                switch (type) {
                    case TOP: {
                        this.density = 25;
                        break;
                    }
                    case MIDDLE: {
                        this.density = 15;
                        break;
                    }
                    case BOTTOM: {
                        this.density = 20;
                        break;
                    }
                    case SPREAD: {
                        this.density = 5;
                        break;
                    }
                    default:
                        throw new IllegalStateException("Type was not a valid state");
                }
            } else {
                this.density = density;
            }
        }

        @Nonnull
        public Type getType() {
            return this.type;
        }

        @Nonnull
        public IWorldgenPlaceable getPlaceable() {
            return this.placeable;
        }

        public short getMinY() {
            return this.minY;
        }

        public short getMaxY() {
            return this.maxY;
        }

        public byte getDensity() {
            return this.density;
        }

        public static class Builder {

            private final Type type;
            private IWorldgenPlaceable placeable;
            private short minY = -1;
            private short maxY = -1;
            private byte density = -1;

            /**
             * @param type the type of the entry
             */
            public Builder(@Nonnull Type type) {
                this.type = type;
            }

            /**
             * @param minY the min y within the vein for this entry
             * @param maxY the max y within the vein for this entry
             * @return this
             */
            @Nonnull
            public Builder height(short minY, short maxY) {
                Preconditions.checkArgument(minY > 0, "MinY must be > 0");
                Preconditions.checkArgument(minY <= 255, "MinY must be <= 255");
                Preconditions.checkArgument(maxY > 0, "MaxY must be > 0");
                Preconditions.checkArgument(maxY <= 255, "MaxY must be <= 255");
                Preconditions.checkArgument(minY < maxY, "MinY must be < MaxY");
                this.minY = minY;
                this.maxY = maxY;
                return this;
            }

            /**
             * @param density the density of this entry
             * @return this
             */
            public Builder density(byte density) {
                Preconditions.checkArgument(density > 0, "Height must be > 0");
                Preconditions.checkArgument(density <= 100, "Height must be <= 100");
                this.density = density;
                return this;
            }

            /**
             * @param state the state to place
             * @return this
             */
            @Nonnull
            public Builder placeable(@Nonnull IBlockState state) {
                return placeable(state, null);
            }

            /**
             * @param state                the state to place
             * @param replacementPredicate the predicate for specifying the blocks which can be replaced
             * @return this
             */
            @Nonnull
            public Builder placeable(@Nonnull IBlockState state, @Nullable Predicate<IBlockState> replacementPredicate) {
                return placeable(new BlockStatePlacer(state, replacementPredicate));
            }

            /**
             * @param material the material whose ore should be placed
             * @return this
             */
            @Nonnull
            public Builder placeable(@Nonnull Material material) {
                return placeable(new MaterialOreBlockPlacer(material));
            }

            /**
             * @param placeable the placeable to place
             * @return this
             */
            @Nonnull
            public Builder placeable(@Nonnull IWorldgenPlaceable placeable) {
                this.placeable = placeable;
                return this;
            }

            @Nonnull
            public Entry build() {
                return new Entry(type, placeable, minY, maxY, density);
            }

            public int getDensity() {
                return density;
            }

            @Nonnull
            public Type getType() {
                return this.type;
            }
        }
    }

    /**
     * Builder for {@link LayeredVeinContext}
     */
    public static class Builder extends BaseWorldgenContext.Builder<LayeredVeinContext> {

        protected Entry.Builder top;
        protected Entry.Builder middle;
        protected Entry.Builder bottom;
        protected Entry.Builder spread;

        /**
         * @param weight  the weight of the vein
         * @param density the density of the vein, from 1 to 100 (inclusive)
         */
        public Builder(int weight, byte density, short minY, short maxY) {
            super(weight, density, minY, maxY, (short) 0); // height will be calculated at the end
        }

        /**
         * Add an entry to this layered vein
         * @param entry the entry to add
         * @return this
         * @see Entry.Builder
         */
        @Nonnull
        public Builder entry(@Nonnull Entry.Builder entry) {
            switch (entry.getType()) {
                case TOP: {
                    this.top = entry;
                    break;
                }
                case MIDDLE: {
                    this.middle = entry;
                    break;
                }
                case BOTTOM: {
                    this.bottom = entry;
                    break;
                }
                case SPREAD: {
                    this.spread = entry;
                    break;
                }
                default: throw new IllegalStateException("Type was not a valid state");
            }
            return this;
        }

        @Nonnull
        @Override
        public LayeredVeinContext build() {
            Preconditions.checkNotNull(top, "Top must not be null");
            Preconditions.checkNotNull(middle, "Middle must not be null");
            Preconditions.checkNotNull(bottom, "Bottom must not be null");
            Preconditions.checkNotNull(spread, "Spread must not be null");
            final Entry top = this.top.build();
            final Entry middle = this.middle.build();
            final Entry bottom = this.bottom.build();
            final Entry spread = this.spread.build();

            final byte density = (byte) Math.min(100, top.getDensity() + middle.getDensity() + bottom.getDensity() + spread.getDensity());

            final short height = (short) (top.getMaxY() - top.getMinY() +
                    middle.getMaxY() - middle.getMinY() +
                    bottom.getMaxY() - bottom.getMinY() +
                    spread.getMaxY() - spread.getMinY());
            return new LayeredVeinContext(weight, density, minY, maxY, height, top, middle, bottom, spread);
        }
    }
}
