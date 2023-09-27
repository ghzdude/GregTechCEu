package gregtech.common.pipelike.newpipes;

import gregtech.api.unification.material.properties.IMaterialProperty;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.properties.PropertyKey;

import java.util.Objects;

public abstract class GenericPipeProperties implements IMaterialProperty {

    /**
     * Items will try to take the path with the lowest priority
     */
    private int priority;

    /**
     * rate in items or liters per sec
     */
    private int transferRate;

    private final PipeType type;

    public GenericPipeProperties(int priority, int transferRate, PipeType type) {
        this.priority = priority;
        this.transferRate = transferRate;
        this.type = type;
    }

    /**
     * Default property constructor.
     */
    public GenericPipeProperties() {
        this(1, 16, PipeType.ITEM);
    }

    /**
     * Retrieves the priority of the item pipe
     *
     * @return The item pipe priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the Priority of the item pipe
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Retrieve the transfer rate of the item pipe
     *
     * @return The transfer rate of the item pipe
     */
    public float getTransferRate() {
        return transferRate;
    }

    /**
     * Sets the transfer rate of the pipe
     *
     * @param transferRate The transfer rate
     */
    public void setTransferRate(int transferRate) {
        this.transferRate = transferRate;
    }

    @Override
    public void verifyProperty(MaterialProperties properties) {
        if (!properties.hasProperty(PropertyKey.WOOD)) {
            properties.ensureSet(PropertyKey.INGOT, true);
        }

        if ((getType() == PipeType.ITEM && properties.hasProperty(PropertyKey.FLUID_PIPE)) ||
            (getType() == PipeType.FLUID && properties.hasProperty(PropertyKey.ITEM_PIPE))
        ) {
            throw new IllegalStateException(
                    "Material " + properties.getMaterial() +
                            " has both Fluid and Item Pipe Property, which is not allowed!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericPipeProperties that = (GenericPipeProperties) o;
        if (that.getType() != this.getType()) return false;
        return priority == that.priority && Float.compare(that.transferRate, transferRate) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(priority, transferRate);
    }

    public PipeType getType() {
        return type;
    }

    public enum PipeType {
        FLUID,
        ITEM
    }
}
