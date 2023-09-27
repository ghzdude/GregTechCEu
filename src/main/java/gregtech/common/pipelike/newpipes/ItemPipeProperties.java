package gregtech.common.pipelike.newpipes;

import gregtech.api.unification.material.properties.IMaterialProperty;
import gregtech.api.unification.material.properties.MaterialProperties;
import gregtech.api.unification.material.properties.PropertyKey;

import java.util.Objects;

public class ItemPipeProperties extends GenericPipeProperties {

    public ItemPipeProperties(int priority, float transferRate) {
        super(priority, transferRate);
    }

    public ItemPipeProperties() {
        super();
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
        return "ItemPipeProperties{" +
                "priority=" + getPriority() +
                ", transferRate=" + getTransferRate() +
                '}';
    }
}
