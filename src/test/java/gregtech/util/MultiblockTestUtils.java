package gregtech.util;

import gregtech.api.GTValues;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerHandler;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMap;
import gregtech.api.util.world.DummyWorld;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityEnergyHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityFluidHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityItemBus;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiFluidHatch;
import gregtech.common.metatileentities.multi.multiblockpart.MetaTileEntityMultiblockPart;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gregtech.api.util.GTUtility.gregtechId;

public class MultiblockTestUtils {

    public static Builder builder(MultiblockControllerBase mbt) {
        return new Builder(mbt);
    }

    public static RecipeMapMultiblockController createMultiblock(RecipeMap<?> map, boolean isDistinct) {
        // super function calls the world, which equal null in test
        var mbt = new RecipeMapMultiblockController(gregtechId("multi_test:" + map.unlocalizedName), map) {

            @Override
            public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
                return this;
            }

            @Override
            public boolean hasMaintenanceMechanics() {
                return false;
            }

            @Override
            public void reinitializeStructurePattern() {}

            @Override
            protected @NotNull BlockPattern createStructurePattern() {
                return FactoryBlockPattern.start().build();
            }

            @Override
            public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
                return Textures.HEAT_PROOF_CASING;
            }

            @Override
            public boolean isDistinct() {
                return isDistinct;
            }

            @Override
            public boolean canBeDistinct() {
                return true;
            }

            // function checks for the temperature of the recipe against the coils
            @Override
            public boolean checkRecipe(@NotNull Recipe recipe, boolean consumeIfSuccess) {
                return true;
            }
        };

        // isValid() check in the dirtying logic requires both a metatileentity and a holder
        try {
            Field holder = MetaTileEntity.class.getDeclaredField("holder");
            holder.setAccessible(true);
            holder.set(mbt, new MetaTileEntityHolder());

            Field mte = MetaTileEntityHolder.class.getDeclaredField("metaTileEntity");
            mte.setAccessible(true);
            mte.set(mbt.getHolder(), mbt);
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}

        ((MetaTileEntityHolder) mbt.getHolder()).setWorld(DummyWorld.INSTANCE);
        return mbt;
    }

    public static MultiblockRecipeLogic createRecipeLogic(RecipeMapMultiblockController mbt) {
        return new MultiblockRecipeLogic(mbt);
    }

    private static IEnergyContainer createEnergyHandler(MetaTileEntity mte, long voltage, int amps, boolean isExport) {
        return new EnergyContainerHandler(mte, Integer.MAX_VALUE,
                !isExport ? voltage : 0, !isExport ? amps : 0,
                isExport ? voltage : 0, isExport ? amps : 0) {

            @Override
            public long getEnergyStored() {
                return Integer.MAX_VALUE;
            }

            @Override
            public void setEnergyStored(long energyStored) {
                if (energyStored > this.energyStored) {
                    energyInputPerSec += energyStored - this.energyStored;
                } else {
                    energyOutputPerSec += this.energyStored - energyStored;
                }
                this.energyStored = getEnergyStored();
                notifyEnergyListener(false);
            }
        };
    }

    public static class Builder {

        MultiblockControllerBase mbt;
        Map<MultiblockAbility<Object>, List<Object>> multiblockAbilities;

        private Builder(MultiblockControllerBase mbt) {
            this.mbt = mbt;
            try {
                var abilities = MultiblockControllerBase.class.getDeclaredField("multiblockAbilities");
                abilities.setAccessible(true);
                // noinspection unchecked
                multiblockAbilities = (Map<MultiblockAbility<Object>, List<Object>>) abilities.get(mbt);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }

        @SuppressWarnings("unchecked")
        private Builder register(MetaTileEntityMultiblockPart mte) {
            if (mte instanceof IMultiblockAbilityPart<?>part)
                registerAbility((IMultiblockAbilityPart<Object>) part);
            return this;
        }

        private void registerAbility(IMultiblockAbilityPart<Object> part) {
            List<Object> list = multiblockAbilities.computeIfAbsent(part.getAbility(),
                    objectMultiblockAbility -> new ArrayList<>());
            part.registerAbilities(list);
        }

        /**
         * adds a {@link MetaTileEntityItemBus} to the controller
         * 
         * @param tier     tier of the item bus
         * @param isExport - is export
         */
        public Builder item(int tier, boolean isExport) {
            var bus = new MetaTileEntityItemBus(gregtechId("item"), tier, isExport) {

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }
            };
            return register(bus);
        }

        public Builder fluid(int tier, boolean isExport) {
            var hatch = new MetaTileEntityFluidHatch(gregtechId("fluid"), tier, isExport) {

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }
            };
            return register(hatch);
        }

        public Builder quadFluid(int tier, boolean isExport) {
            return multiFluid(tier, 4, isExport);
        }

        public Builder nonupleFluid(int tier, boolean isExport) {
            return multiFluid(tier, 9, isExport);
        }

        private Builder multiFluid(int tier, int tanks, boolean isExport) {
            var multi = new MetaTileEntityMultiFluidHatch(gregtechId("multi_fluid:" + tanks), tier, tanks, isExport) {

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }
            };
            return register(multi);
        }

        public Builder energy(int tier, int amps, boolean isExport) {
            var energy = new MetaTileEntityEnergyHatch(gregtechId("energy"), tier, amps, isExport) {

                final IEnergyContainer energyContainer = MultiblockTestUtils.createEnergyHandler(this, GTValues.V[tier],
                        amps, isExport);

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }

                @Override
                public void registerAbilities(List<IEnergyContainer> abilityList) {
                    abilityList.add(energyContainer);
                }
            };
            return register(energy);
        }

        public Builder initializeAbilities() {
            if (mbt instanceof RecipeMapMultiblockController controller) {
                try {
                    var method = RecipeMapMultiblockController.class.getDeclaredMethod("initializeAbilities");
                    method.setAccessible(true);
                    method.invoke(controller);
                } catch (ReflectiveOperationException ignored) {}
            }
            return this;
        }
    }
}
