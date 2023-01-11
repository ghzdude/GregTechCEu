package gregtech.api.worldgen2.placement;

import com.google.common.base.Preconditions;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.ore.StoneType;
import gregtech.api.worldgen2.WorldgenUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Placer for a {@link Material} ore block
 */
public class MaterialOreBlockPlacer implements IWorldgenPlaceable {

    private final Material material;

    public MaterialOreBlockPlacer(@Nonnull Material material) {
        Preconditions.checkArgument(material.hasProperty(PropertyKey.ORE), "Material must have ore property.");
        this.material = material;
    }

    @Nonnull
    public Material getMaterial() {
        return this.material;
    }

    @Nonnull
    @Override
    public IBlockState getStateToPlace(@Nonnull IBlockState existing, @Nonnull World world, @Nonnull BlockPos pos) {
        return WorldgenUtil.getOreForPos(this.material, existing, world, pos);
    }

    @Override
    public boolean isStateReplaceable(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return StoneType.computeStoneType(state, world, pos) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialOreBlockPlacer that = (MaterialOreBlockPlacer) o;
        return material.equals(that.getMaterial());
    }

    @Override
    public int hashCode() {
        return material.hashCode();
    }
}
