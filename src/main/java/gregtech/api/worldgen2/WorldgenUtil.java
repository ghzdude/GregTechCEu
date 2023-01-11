package gregtech.api.worldgen2;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.ore.StoneType;
import gregtech.api.unification.ore.StoneTypes;
import gregtech.common.blocks.BlockOre;
import gregtech.common.blocks.MetaBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Common utilities for world generation
 */
public final class WorldgenUtil {

    private WorldgenUtil() {/**/}

    /**
     * @param material  the material to get the ore for
     * @param stoneType the stonetype of the ore
     * @return an IBlockState for an ore given a stonetype
     */
    @Nonnull
    public static IBlockState getOre(@Nonnull Material material, @Nullable StoneType stoneType) {
        //TODO find a way to remove the reference to MetaBlocks to prevent common in api
        Optional<BlockOre> optional = MetaBlocks.ORES.stream().filter(ore -> ore.material == material).findFirst();
        if (optional.isPresent()) {
            return optional.get().getOreBlock(stoneType == null ? StoneTypes.STONE : stoneType);
        }
        throw new IllegalArgumentException("Material " + material + " has no ore");
    }

    /**
     * @param material the material to get the ore for
     * @param existing the existing state in the world to replace
     * @param access   the access containing the existing state
     * @param pos      the position of the existing state
     * @return the IBlockState for an ore with the correct stonetype for a position
     */
    @Nonnull
    public static IBlockState getOreForPos(@Nonnull Material material, @Nonnull IBlockState existing, @Nonnull IBlockAccess access, @Nonnull BlockPos pos) {
        return getOre(material, StoneType.computeStoneType(existing, access, pos));
    }
}
