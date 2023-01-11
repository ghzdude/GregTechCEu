package gregtech.api.worldgen2.context;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class BaseWorldgenContext implements IWorldgenContext {

    private final int weight;
    private final byte density;
    private final short minY;
    private final short maxY;

    protected BaseWorldgenContext(int weight, byte density, short minY, short maxY) {
        this.weight = weight;
        Preconditions.checkArgument(density > 0, "Density must be > 0");
        Preconditions.checkArgument(density <= 100, "Density must be <= 100");
        this.density = density;
        Preconditions.checkArgument(minY < maxY, "MinY must be < MaxY");
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public byte getDensity() {
        return this.density;
    }

    @Override
    public short getMinY() {
        return this.minY;
    }

    @Override
    public short getMaxY() {
        return this.maxY;
    }

    public static abstract class Builder<T extends BaseWorldgenContext> {

        protected final int weight;
        protected final byte density;
        protected final short minY;
        protected final short maxY;

        /**
         * @param weight the weight of the vein
         * @param density the density of the vein, from 1 to 100 (inclusive)
         * @param minY the minimum y value for the vein, inclusive
         * @param maxY the maximum y value for the vein, exclusive
         */
        public Builder(int weight, byte density, short minY, short maxY) {
            this.weight = weight;
            this.density = density;
            this.minY = minY;
            this.maxY = maxY;
        }

        @Nonnull
        public abstract T build();
    }
}
