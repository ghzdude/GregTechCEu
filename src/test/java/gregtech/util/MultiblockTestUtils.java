package gregtech.util;

import gregtech.api.GTValues;
import gregtech.api.capability.IEnergyContainer;
import gregtech.api.capability.impl.EnergyContainerHandler;
import gregtech.api.capability.impl.MultiblockRecipeLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.metatileentity.multiblock.MultiblockControllerBase;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import gregtech.api.util.world.DummyWorld;
import gregtech.common.metatileentities.multi.electric.MetaTileEntityElectricBlastFurnace;
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

    public static RecipeMapMultiblockController createMultiblock() {
                // super function calls the world, which equal null in test
        var mbt = new MetaTileEntityElectricBlastFurnace(gregtechId("electric_blast_furnace")) {

            @Override
            public boolean hasMufflerMechanics() {
                return false;
            }

            // ignore maintenance problems
            @Override
            public boolean hasMaintenanceMechanics() {
                return false;
            }

            @Override
            public void reinitializeStructurePattern() {}

            @Override
            public boolean isDistinct() {
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
        Field controllerTile;
        Map<MultiblockAbility<Object>, List<Object>> multiblockAbilities;

        private Builder(MultiblockControllerBase mbt) {
            this.mbt = mbt;
            try {
                controllerTile = MetaTileEntityMultiblockPart.class.getDeclaredField("controllerTile");
                controllerTile.setAccessible(true);
                var abilities = MultiblockControllerBase.class.getDeclaredField("multiblockAbilities");
                abilities.setAccessible(true);
                //noinspection unchecked
                multiblockAbilities = (Map<MultiblockAbility<Object>, List<Object>>) abilities.get(mbt);
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}
        }

        @SuppressWarnings("unchecked")
        private void setController(MetaTileEntityMultiblockPart mte) {
            try {
                controllerTile.set(mte, mbt);
            } catch (IllegalAccessException ignored) {}
            if (mte instanceof IMultiblockAbilityPart<?> part)
                registerAbility((IMultiblockAbilityPart<Object>) part);
        }

        private void registerAbility(IMultiblockAbilityPart<Object> part) {
            List<Object> list = multiblockAbilities.computeIfAbsent(part.getAbility(), objectMultiblockAbility -> new ArrayList<>());
            part.registerAbilities(list);
        }

        /**
         * adds a {@link MetaTileEntityItemBus} to the controller
         * @param tier tier of the item bus
         * @param isExport - is export
         */
        public Builder item(int tier, boolean isExport) {
           setController(new MetaTileEntityItemBus(gregtechId("item"), tier, isExport) {

               @Override
               public MultiblockControllerBase getController() {
                   return mbt;
               }
           });
           return this;
        }

        public Builder fluid(int tier, boolean isExport) {
            setController(new MetaTileEntityFluidHatch(gregtechId("fluid"), tier, isExport) {

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }
            });
            return this;
        }

        public Builder quadFluid(int tier, boolean isExport) {
            setController(new MetaTileEntityMultiFluidHatch(gregtechId("quad"), tier, 4, isExport) {

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }
            });
            return this;
        }

        public Builder nonupleFluid(int tier, boolean isExport) {
            setController(new MetaTileEntityMultiFluidHatch(gregtechId("nonuple"), tier, 9, isExport) {

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }
            });
            return this;
        }

        public Builder energy(int tier, int amps, boolean isExport) {
            setController(new MetaTileEntityEnergyHatch(gregtechId("energy"), tier, amps, isExport) {
                final IEnergyContainer energyContainer = MultiblockTestUtils.createEnergyHandler(this, GTValues.V[tier], amps, isExport);

                @Override
                public MultiblockControllerBase getController() {
                    return mbt;
                }

                @Override
                public void registerAbilities(List<IEnergyContainer> abilityList) {
                    abilityList.add(energyContainer);
                }
            });
            return this;
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
