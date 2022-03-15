package gregtech.common.metatileentities.steam;

import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.capability.impl.RecipeLogicSteam;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ProgressWidget.MoveType;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity;
import gregtech.api.metatileentity.interfaces.IMetaTileEntity.IMTENeighborChanged;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.SteamMetaTileEntity;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.texture.Textures;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

public class SteamRockBreaker extends SteamMetaTileEntity implements IMTENeighborChanged {

    private boolean hasValidFluids;

    public SteamRockBreaker(ResourceLocation metaTileEntityId, boolean isHighPressure) {
        super(metaTileEntityId, RecipeMaps.ROCK_BREAKER_RECIPES, Textures.ROCK_BREAKER_OVERLAY, isHighPressure);
        this.workableHandler = new SteamRockBreakerRecipeLogic(this,
                workableHandler.getRecipeMap(), isHighPressure, steamFluidTank, 1.0);
        if (getWorld() != null && !getWorld().isRemote) {
            checkAdjacentFluids();
        }
    }

    @Override
    public IMetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new SteamRockBreaker(metaTileEntityId, isHighPressure);
    }

    @Override
    public void neighborChanged() {
        checkAdjacentFluids();
    }

    private void checkAdjacentFluids() {
        boolean hasLava = false;
        boolean hasWater = false;
        for (EnumFacing side : EnumFacing.VALUES) {
            if (hasLava && hasWater) {
                break;
            }

            if (side == frontFacing || side.getAxis().isVertical()) {
                continue;
            }

            Block block = getWorld().getBlockState(getPos().offset(side)).getBlock();
            if (block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
                hasLava = true;
            } else if (block == Blocks.FLOWING_WATER || block == Blocks.WATER) {
                hasWater = true;
            }
        }
        this.hasValidFluids = hasLava && hasWater;
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new NotifiableItemStackHandler(1, this, false);
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new NotifiableItemStackHandler(4, this, true);
    }

    @Override
    public ModularUI createUI(EntityPlayer player) {
        return createUITemplate(player)
                .slot(importItems, 0, 53, 34, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.DUST_OVERLAY_STEAM.get(isHighPressure))
                .progressBar(workableHandler::getProgressPercent, 79, 35, 21, 18,
                        GuiTextures.PROGRESS_BAR_MACERATE_STEAM.get(isHighPressure), MoveType.HORIZONTAL, workableHandler.getRecipeMap())
                .slot(exportItems, 0, 107, 25, true, false, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.CRUSHED_ORE_OVERLAY_STEAM.get(isHighPressure))
                .slot(exportItems, 1, 125, 25, true, false, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.CRUSHED_ORE_OVERLAY_STEAM.get(isHighPressure))
                .slot(exportItems, 2, 107, 43, true, false, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.CRUSHED_ORE_OVERLAY_STEAM.get(isHighPressure))
                .slot(exportItems, 3, 125, 43, true, false, GuiTextures.SLOT_STEAM.get(isHighPressure), GuiTextures.CRUSHED_ORE_OVERLAY_STEAM.get(isHighPressure))
                .build(getTileEntity(), player);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setBoolean("hasValidFluids", hasValidFluids);
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        if (data.hasKey("hasValidFluids")) {
            this.hasValidFluids = data.getBoolean("hasValidFluids");
        }
    }

    protected class SteamRockBreakerRecipeLogic extends RecipeLogicSteam {

        public SteamRockBreakerRecipeLogic(MetaTileEntity tileEntity, RecipeMap<?> recipeMap, boolean isHighPressure, IFluidTank steamFluidTank, double conversionRate) {
            super(tileEntity, recipeMap, isHighPressure, steamFluidTank, conversionRate);
        }

        @Override
        protected boolean shouldSearchForRecipes() {
            return hasValidFluids && super.shouldSearchForRecipes();
        }
    }
}
