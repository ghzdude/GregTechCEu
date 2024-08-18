package gregtech.common.metatileentities.multi.multiblockpart;

import gregtech.api.capability.DualHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.AbilityInstances;
import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.mui.GTGuis;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.cleanroommc.modularui.widgets.slot.SlotGroup;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetaTileEntityDualHatch extends MetaTileEntityMultiblockNotifiablePart
                                     implements IMultiblockAbilityPart<IItemHandlerModifiable> {

    private DualHandler[] dualHandlers;

    public MetaTileEntityDualHatch(ResourceLocation metaTileEntityId, int tier, boolean isExportHatch) {
        super(metaTileEntityId, tier, isExportHatch);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDualHatch(metaTileEntityId, this.getTier(), this.isExportHatch);
    }

    @Override
    protected void initializeInventory() {
        dualHandlers = new DualHandler[2];
        for (int i = 0; i < dualHandlers.length; i++) {
            var itemHandler = new NotifiableItemStackHandler(this, 4, null, isExportHatch);
            var fluidHandler = new FluidTankList(false, createTanks());
            dualHandlers[i] = new DualHandler(itemHandler, fluidHandler, isExportHatch);
        }
        super.initializeInventory();
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new ItemHandlerList(Arrays.asList(dualHandlers));
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        List<IFluidTank> tanks = new ArrayList<>();
        for (var h : dualHandlers) tanks.addAll(h.getFluidTanks());
        return new FluidTankList(false, tanks);
    }

    private IFluidTank[] createTanks() {
        return new IFluidTank[] {
                new NotifiableFluidTank(16000, null, isExportHatch),
                new NotifiableFluidTank(16000, null, isExportHatch)
        };
    }

    @Override
    public boolean usesMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager) {
        return GTGuis.createPanel(this, 176, 180)
                .child(new Row()
                        .top(22)
                        .left(6)
                        .widthRel(1f)
                        .coverChildrenHeight()
                        .child(createSlotGrid(syncManager, 0)
                                .marginRight(4))
                        .child(createSlotGrid(syncManager, 1)))
                .child(SlotGroupWidget.playerInventory(7));
    }

    private Widget<?> createSlotGrid(PanelSyncManager syncManager, int handlerIdx) {
        var grid = new Grid()
                .coverChildren()
                .margin(0);
        var group = new SlotGroup("slot_group:" + handlerIdx, 2, true);
        syncManager.registerSlotGroup(group);

        var handler = dualHandlers[handlerIdx];
        for (int i = 0; i < 2; i++) {
            int idx = (i * 2);
            grid.row(new ItemSlot()
                    .slot(new ModularSlot(handler, idx)
                            .slotGroup(group)),
                    new ItemSlot()
                            .slot(new ModularSlot(handler, idx + 1)
                                    .slotGroup(group)));
        }

        grid.row(new FluidSlot()
                .syncHandler(handler.getTankAt(0)),
                new FluidSlot()
                        .syncHandler(handler.getTankAt(1)));

        return grid;
    }

    @Override
    public MultiblockAbility<IItemHandlerModifiable> getAbility() {
        return MultiblockAbility.IMPORT_ITEMS;
    }

    @Override
    public void registerAbilities(@NotNull AbilityInstances abilityInstances) {
        abilityInstances.addAll(Arrays.asList(this.dualHandlers));
    }
}
