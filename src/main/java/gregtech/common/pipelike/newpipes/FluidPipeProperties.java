package gregtech.common.pipelike.newpipes;

import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.properties.PropertyKey;

public class FluidPipeProperties extends GenericPipeProperties {

    private final int tanks;

    private int maxFluidTemperature;
    private boolean gasProof;
    private boolean acidProof;
    private boolean cryoProof;
    private boolean plasmaProof;

    public FluidPipeProperties(int priority, int maxFluidTemperature, int transferRate, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof) {
        this(priority, maxFluidTemperature, transferRate, gasProof, acidProof, cryoProof, plasmaProof, 1);
    }

    /**
     * Should only be called from {@link gregtech.common.pipelike.fluidpipe.FluidPipeType#modifyProperties(gregtech.api.unification.material.properties.FluidPipeProperties)}
     */
    public FluidPipeProperties(int priority, int maxFluidTemperature, int transferRate, boolean gasProof, boolean acidProof, boolean cryoProof, boolean plasmaProof, int tanks) {
        super(priority, transferRate);
        this.maxFluidTemperature = maxFluidTemperature;
        this.gasProof = gasProof;
        this.acidProof = acidProof;
        this.cryoProof = cryoProof;
        this.plasmaProof = plasmaProof;
        this.tanks = tanks;
    }

    /**
     * Default property constructor.
     */
    public FluidPipeProperties() {
        this(300, 1, false, false, false, false);
    }
    @Override
    public void verifyProperty(MaterialProperties properties) {
        if (!properties.hasProperty(PropertyKey.WOOD)) {
            properties.ensureSet(PropertyKey.INGOT, true);
        }

        if (properties.hasProperty(PropertyKey.FLUID_PIPE)) {
            throw new IllegalStateException(
                    "Material " + properties.getMaterial() +
                            " has both Fluid and Item Pipe Property, which is not allowed!");
        }
    }

    @Override
    public String toString() {
        return "FluidPipeProperties{" +
                "priority=" + getPriority() +
                ", transferRate=" + getTransferRate() +
                '}';
    }
}
