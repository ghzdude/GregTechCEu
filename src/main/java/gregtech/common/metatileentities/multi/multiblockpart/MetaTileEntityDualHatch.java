package gregtech.common.metatileentities.multi.multiblockpart;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.FluidSlot;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.capability.DualHandler;
import gregtech.api.capability.IMultipleTankHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;

import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;

import gregtech.api.metatileentity.multiblock.MultiblockAbility;

import gregtech.api.mui.GTGuis;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityDualHatch extends MetaTileEntityMultiblockNotifiablePart implements IMultiblockAbilityPart<IItemHandlerModifiable> {

    private List<DualHandler> dualHandlers;
    private List<IMultipleTankHandler> fluidTanks;
    private List<IItemHandlerModifiable> itemHandlers;

    public MetaTileEntityDualHatch(ResourceLocation metaTileEntityId, int tier, boolean isExportHatch) {
        super(metaTileEntityId, tier, isExportHatch);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityDualHatch(metaTileEntityId, 5, false);
    }

    @Override
    protected void initializeInventory() {
        super.initializeInventory();
        dualHandlers = new ArrayList<>();
        for (int i = 0; i < itemHandlers.size(); i++) {
            var handler = itemHandlers.get(i);
            dualHandlers.add(new DualHandler(handler, fluidTanks.get(i), isExportHatch));
        }
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        itemHandlers = new ArrayList<>();
        itemHandlers.add(new NotifiableItemStackHandler(this, 4, null, isExportHatch));
        itemHandlers.add(new NotifiableItemStackHandler(this, 4, null, isExportHatch));
        return new ItemHandlerList(itemHandlers);
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        fluidTanks = new ArrayList<>();
        fluidTanks.add(new FluidTankList(false, createTanks()));
        fluidTanks.add(new FluidTankList(false, createTanks()));
        return new FluidTankList(false, fluidTanks.get(0), fluidTanks.get(1).getFluidTanks().toArray(new IFluidTank[0]));
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
                        .child(createSlotGrid(syncManager, 1))
                )
                .child(SlotGroupWidget.playerInventory(7));
    }

    private Widget<?> createSlotGrid(PanelSyncManager syncManager, int handlerIdx) {
        var grid = new Grid()
                .coverChildren()
                .margin(0);

        for (int i = 0; i < 2; i++) {
            int idx = (i * 2);
            var handler = itemHandlers.get(handlerIdx);
            grid.row(new ItemSlot()
                            .slot(handler, idx),
                    new ItemSlot()
                            .slot(handler, idx + 1)
            );
        }

        grid.row(new FluidSlot()
                        .syncHandler(fluidTanks.get(handlerIdx).getTankAt(0)),
                new FluidSlot()
                        .syncHandler(fluidTanks.get(handlerIdx).getTankAt(1))
        );

        return grid;
    }

    @Override
    public MultiblockAbility<IItemHandlerModifiable> getAbility() {
        return MultiblockAbility.IMPORT_ITEMS;
    }

    @Override
    public void registerAbilities(@NotNull MultiblockAbility<IItemHandlerModifiable> key,
                                  @NotNull List<IItemHandlerModifiable> abilities) {
        abilities.addAll(this.dualHandlers);
    }
}
