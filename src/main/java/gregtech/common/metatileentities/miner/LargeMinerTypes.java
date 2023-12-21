package gregtech.common.metatileentities.miner;

import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.pattern.TraceabilityPredicate;
import gregtech.api.unification.material.Materials;
import gregtech.client.model.miningpipe.MiningPipeModel;
import gregtech.client.model.miningpipe.MiningPipeModels;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockMetalCasing;
import gregtech.common.blocks.MetaBlocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum LargeMinerTypes implements LargeMinerType {

    STEEL,
    TITANIUM,
    TUNGSTEN_STEEL;

    @NotNull
    public TraceabilityPredicate getCasing() {
        return MultiblockControllerBase.states(switch (this) {
            case STEEL -> MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.STEEL_SOLID);
            case TITANIUM -> MetaBlocks.METAL_CASING.getState(BlockMetalCasing.MetalCasingType.TITANIUM_STABLE);
            case TUNGSTEN_STEEL -> MetaBlocks.METAL_CASING
                    .getState(BlockMetalCasing.MetalCasingType.TUNGSTENSTEEL_ROBUST);
        });
    }

    @NotNull
    public TraceabilityPredicate getFrame() {
        return MultiblockControllerBase.frames(switch (this) {
            case STEEL -> Materials.Steel;
            case TITANIUM -> Materials.Titanium;
            case TUNGSTEN_STEEL -> Materials.TungstenSteel;
        });
    }

    @NotNull
    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getBaseTexture(@Nullable IMultiblockPart sourcePart) {
        return switch (this) {
            case STEEL -> Textures.SOLID_STEEL_CASING;
            case TITANIUM -> Textures.STABLE_TITANIUM_CASING;
            case TUNGSTEN_STEEL -> Textures.ROBUST_TUNGSTENSTEEL_CASING;
        };
    }

    @NotNull
    @Override
    public MiningPipeModel getMiningPipeModel() {
        return switch (this) {
            case STEEL -> MiningPipeModels.STEEL;
            case TITANIUM -> MiningPipeModels.TITANIUM;
            case TUNGSTEN_STEEL -> MiningPipeModels.TUNGSTEN_STEEL;
        };
    }

    @NotNull
    @Override
    @SideOnly(Side.CLIENT)
    public ICubeRenderer getFrontOverlay() {
        return switch (this) {
            case STEEL -> Textures.LARGE_MINER_OVERLAY_BASIC;
            case TITANIUM -> Textures.LARGE_MINER_OVERLAY_ADVANCED;
            case TUNGSTEN_STEEL -> Textures.LARGE_MINER_OVERLAY_ADVANCED_2;
        };
    }
}
