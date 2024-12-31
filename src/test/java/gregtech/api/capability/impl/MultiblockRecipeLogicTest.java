package gregtech.api.capability.impl;

import gregtech.Bootstrap;
import gregtech.api.GTValues;
import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.api.recipes.Recipe;
import gregtech.api.recipes.RecipeMaps;
import gregtech.util.MultiblockTestUtils;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;

public class MultiblockRecipeLogicTest {

    @BeforeAll
    public static void bootstrap() {
        Bootstrap.perform();
    }

    @Test
    public void trySearchNewRecipe() {
        RecipeMaps.BLAST_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Blocks.COBBLESTONE))
                .outputs(new ItemStack(Blocks.STONE))
                .EUt(1).duration(1)
                .blastFurnaceTemp(1)
                .buildAndRegister();

        RecipeMapMultiblockController mbt = MultiblockTestUtils.createMultiblock(RecipeMaps.BLAST_RECIPES);

        MultiblockTestUtils.builder(mbt)
                .defaultSuite()
                .initializeAbilities();

        MultiblockRecipeLogic mbl = MultiblockTestUtils.createRecipeLogic(mbt);

        mbl.isOutputsFull = false;
        mbl.invalidInputsForRecipes = false;
        mbl.trySearchNewRecipe();

        // no recipe found
        MatcherAssert.assertThat(mbt.isDistinct(), is(false));
        MatcherAssert.assertThat(mbl.invalidInputsForRecipes, is(true));
        MatcherAssert.assertThat(mbl.isActive, is(false));
        MatcherAssert.assertThat(mbl.previousRecipe, nullValue());

        // put an item in the inventory that will trigger recipe recheck
        mbl.getInputInventory().insertItem(0, new ItemStack(Blocks.COBBLESTONE, 16), false);
        // Inputs change. did we detect it ?
        MatcherAssert.assertThat(mbl.hasNotifiedInputs(), is(true));
        mbl.trySearchNewRecipe();
        MatcherAssert.assertThat(mbl.invalidInputsForRecipes, is(false));
        MatcherAssert.assertThat(mbl.previousRecipe, notNullValue());
        MatcherAssert.assertThat(mbl.isActive, is(true));
        MatcherAssert.assertThat(mbl.getInputInventory().getStackInSlot(0).getCount(), is(15));

        // Save a reference to the old recipe so we can make sure it's getting reused
        Recipe prev = mbl.previousRecipe;

        // Finish the recipe, the output should generate, and the next iteration should begin
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.previousRecipe, is(prev));
        MatcherAssert.assertThat(AbstractRecipeLogic.areItemStacksEqual(mbl.getOutputInventory().getStackInSlot(0),
                new ItemStack(Blocks.STONE, 1)), is(true));
        MatcherAssert.assertThat(mbl.isActive, is(true));

        // Complete the second iteration, but the machine stops because its output is now full
        mbl.getOutputInventory().setStackInSlot(0, new ItemStack(Blocks.STONE, 63));
        mbl.getOutputInventory().setStackInSlot(1, new ItemStack(Blocks.STONE, 64));
        mbl.getOutputInventory().setStackInSlot(2, new ItemStack(Blocks.STONE, 64));
        mbl.getOutputInventory().setStackInSlot(3, new ItemStack(Blocks.STONE, 64));
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.isActive, is(false));
        MatcherAssert.assertThat(mbl.isOutputsFull, is(true));

        // Try to process again and get failed out because of full buffer.
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.isActive, is(false));
        MatcherAssert.assertThat(mbl.isOutputsFull, is(true));

        // Some room is freed in the output bus, so we can continue now.
        mbl.getOutputInventory().setStackInSlot(1, ItemStack.EMPTY);
        MatcherAssert.assertThat(mbl.hasNotifiedOutputs(), is(true));
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.isActive, is(true));
        MatcherAssert.assertThat(mbl.isOutputsFull, is(false));
        mbl.completeRecipe();
        MatcherAssert.assertThat(AbstractRecipeLogic.areItemStacksEqual(mbl.getOutputInventory().getStackInSlot(0),
                new ItemStack(Blocks.STONE, 1)), is(true));
    }

    @Test
    public void trySearchNewRecipeDistinct() {
        RecipeMaps.BLAST_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Blocks.COBBLESTONE))
                .outputs(new ItemStack(Blocks.STONE))
                .EUt(1).duration(1)
                .blastFurnaceTemp(1)
                .buildAndRegister();

        RecipeMapMultiblockController mbt = MultiblockTestUtils.createMultiblock(RecipeMaps.BLAST_RECIPES, true);

        // Controller and isAttachedToMultiBlock need the world so we fake it here.
        MultiblockTestUtils.builder(mbt)
                .item(GTValues.LV, false)
                .defaultSuite()
                .initializeAbilities();

        MultiblockRecipeLogic mbl = MultiblockTestUtils.createRecipeLogic(mbt);

        MatcherAssert.assertThat(mbt.isDistinct(), is(true));

        mbl.isOutputsFull = false;
        mbl.invalidInputsForRecipes = false;
        mbl.trySearchNewRecipe();

        // no recipe found
        MatcherAssert.assertThat(mbt.isDistinct(), is(true));
        MatcherAssert.assertThat(mbl.invalidatedInputList.containsAll(mbl.getInputBuses()), is(true));
        MatcherAssert.assertThat(mbl.isActive, is(false));
        MatcherAssert.assertThat(mbl.previousRecipe, nullValue());

        // put an item in the first input bus that will trigger recipe recheck

        IItemHandlerModifiable firstBus = mbl.getInputBuses().get(0);
        firstBus.insertItem(0, new ItemStack(Blocks.COBBLESTONE, 16), false);

        // extract the specific notified item handler, as it's not the entire bus
        IItemHandlerModifiable notified = null;
        for (IItemHandler h : ((ItemHandlerList) firstBus).getBackingHandlers()) {
            if (h.getSlots() == 4 && h instanceof IItemHandlerModifiable) {
                notified = (IItemHandlerModifiable) h;
            }
        }

        // Inputs change. did we detect it ?
        MatcherAssert.assertThat(mbl.hasNotifiedInputs(), is(true));
        MatcherAssert.assertThat(mbl.getMetaTileEntity().getNotifiedItemInputList(), hasItem(notified));
        MatcherAssert.assertThat(mbl.canWorkWithInputs(), is(true));
        mbl.trySearchNewRecipe();
        MatcherAssert.assertThat(mbl.invalidatedInputList, not(hasItem(firstBus)));
        MatcherAssert.assertThat(mbl.previousRecipe, notNullValue());
        MatcherAssert.assertThat(mbl.isActive, is(true));
        MatcherAssert.assertThat(firstBus.getStackInSlot(0).getCount(), is(15));

        // Save a reference to the old recipe so we can make sure it's getting reused
        Recipe prev = mbl.previousRecipe;

        // Finish the recipe, the output should generate, and the next iteration should begin
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.previousRecipe, is(prev));
        MatcherAssert.assertThat(AbstractRecipeLogic.areItemStacksEqual(mbl.getOutputInventory().getStackInSlot(0),
                new ItemStack(Blocks.STONE, 1)), is(true));
        MatcherAssert.assertThat(mbl.isActive, is(true));

        // Complete the second iteration, but the machine stops because its output is now full
        mbl.getOutputInventory().setStackInSlot(0, new ItemStack(Blocks.STONE, 63));
        mbl.getOutputInventory().setStackInSlot(1, new ItemStack(Blocks.STONE, 64));
        mbl.getOutputInventory().setStackInSlot(2, new ItemStack(Blocks.STONE, 64));
        mbl.getOutputInventory().setStackInSlot(3, new ItemStack(Blocks.STONE, 64));
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.isActive, is(false));
        MatcherAssert.assertThat(mbl.isOutputsFull, is(true));

        // Try to process again and get failed out because of full buffer.
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.isActive, is(false));
        MatcherAssert.assertThat(mbl.isOutputsFull, is(true));

        // Some room is freed in the output bus, so we can continue now.
        mbl.getOutputInventory().setStackInSlot(1, ItemStack.EMPTY);
        MatcherAssert.assertThat(mbl.hasNotifiedOutputs(), is(true));
        mbl.updateWorkable();
        MatcherAssert.assertThat(mbl.isActive, is(true));
        MatcherAssert.assertThat(mbl.isOutputsFull, is(false));
        mbl.completeRecipe();
        MatcherAssert.assertThat(AbstractRecipeLogic.areItemStacksEqual(mbl.getOutputInventory().getStackInSlot(0),
                new ItemStack(Blocks.STONE, 1)), is(true));
    }

    @Test
    public void testMaintenancePenalties() {
        RecipeMapMultiblockController mbt = MultiblockTestUtils.createMultiblock(RecipeMaps.BLAST_RECIPES, false, true);

        MultiblockTestUtils.builder(mbt)
                .item(GTValues.LV, false)
                .item(GTValues.LV, true)
                .maintanence(false)
                .energy(GTValues.LV, 2, false)
                .initializeAbilities();

        MultiblockRecipeLogic mbl = MultiblockTestUtils.createRecipeLogic(mbt);

        RecipeMaps.BLAST_RECIPES.recipeBuilder()
                .inputs(new ItemStack(Blocks.CRAFTING_TABLE))
                .outputs(new ItemStack(Blocks.STONE))
                .EUt(10).duration(10)
                .blastFurnaceTemp(1)
                .buildAndRegister();

        // start off as fixed
        for (int i = 0; i < 6; i++) {
            mbt.setMaintenanceFixed(i);
        }

        // cause one problem
        mbt.causeMaintenanceProblems();

        MatcherAssert.assertThat(mbt.getNumMaintenanceProblems(), is(1));

        IItemHandlerModifiable firstBus = mbl.getInputBuses().get(0);
        firstBus.insertItem(0, new ItemStack(Blocks.CRAFTING_TABLE, 1), false);
        mbl.trySearchNewRecipe();

        // 1 problem is 10% slower. 10 * 1.1 = 11
        MatcherAssert.assertThat(mbl.maxProgressTime, is(11));

        mbl.completeRecipe();

        // fix old problems
        for (int i = 0; i < 6; i++) {
            mbt.setMaintenanceFixed(i);
        }

        firstBus.insertItem(0, new ItemStack(Blocks.CRAFTING_TABLE, 1), false);
        mbl.trySearchNewRecipe();

        // 0 problems should have the regular duration of 10
        MatcherAssert.assertThat(mbl.maxProgressTime, is(10));
    }
}
