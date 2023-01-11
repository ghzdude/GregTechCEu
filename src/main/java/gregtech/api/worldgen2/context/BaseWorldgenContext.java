package gregtech.api.worldgen2.context;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class BaseWorldgenContext implements IWorldgenContext {

    private final int weight;
    private final byte density;
    private final short minY;
    private final short maxY;
    private final short height;

    /**
     * @param weight  the weight of the vein
     * @param density the density of the vein
     * @param minY    the minimum y for generation of the vein
     * @param maxY    the maximum y for generation of the vein
     * @param height  the height of the vein
     */
    protected BaseWorldgenContext(int weight, byte density, short minY, short maxY, short height) {
        this.weight = weight;
        Preconditions.checkArgument(density > 0, "Density must be > 0");
        Preconditions.checkArgument(density <= 100, "Density must be <= 100");
        this.density = density;
        Preconditions.checkArgument(minY >= 0, "MinY must be >= 0");
        Preconditions.checkArgument(maxY <= 255, "MinY must be <= 255");
        Preconditions.checkArgument(minY < maxY, "MinY must be < MaxY");
        this.minY = minY;
        this.maxY = maxY;
        Preconditions.checkArgument(height >= 0, "Height must be >= 0");
        Preconditions.checkArgument(height <= 255, "Height must be <= 255");
        this.height = height;
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

    @Override
    public short getHeight() {
        return this.height;
    }

    public static abstract class Builder<T extends BaseWorldgenContext> {

        protected final int weight;
        protected final byte density;
        protected final short minY;
        protected final short maxY;
        protected final short height;

        /**
         * @param weight the weight of the vein
         * @param density the density of the vein, from 1 to 100 (inclusive)
         */
        public Builder(int weight, byte density, short minY, short maxY, short height) {
            this.weight = weight;
            this.density = density;
            this.minY = minY;
            this.maxY = maxY;
            this.height = height;
        }

        @Nonnull
        public abstract T build();
    }
}
