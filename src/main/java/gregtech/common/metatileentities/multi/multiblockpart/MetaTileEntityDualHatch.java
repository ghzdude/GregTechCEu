package gregtech.common.metatileentities.multi.multiblockpart;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import com.cleanroommc.modularui.widget.Widget;

import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.layout.Row;

import gregtech.api.capability.DualHandler;
import gregtech.api.capability.impl.FluidTankList;
import gregtech.api.capability.impl.ItemHandlerList;
import gregtech.api.capability.impl.NotifiableFluidTank;
import gregtech.api.capability.impl.NotifiableItemStackHandler;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;

import gregtech.api.metatileentity.multiblock.IMultiblockAbilityPart;

import gregtech.api.metatileentity.multiblock.MultiblockAbility;

import gregtech.api.mui.GTGuis;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MetaTileEntityDualHatch extends MetaTileEntityMultiblockNotifiablePart implements IMultiblockAbilityPart<IItemHandlerModifiable> {

    private DualHandler dualHandler;

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
        dualHandler = new DualHandler(this.importItems, this.importFluids, isExportHatch);
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        List<IItemHandlerModifiable> list = new ArrayList<>();
        list.add(new NotifiableItemStackHandler(this, 4, null, isExportHatch));
        list.add(new NotifiableItemStackHandler(this, 4, null, isExportHatch));
        return new ItemHandlerList(list);
    }

    @Override
    protected FluidTankList createImportFluidHandler() {
        List<IFluidTank> list = new ArrayList<>();
        list.add(new NotifiableFluidTank(16000, null, isExportHatch));
        list.add(new NotifiableFluidTank(16000, null, isExportHatch));
        return new FluidTankList(false, list);
    }

    @Override
    public boolean usesMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager) {
        return GTGuis.createPanel(this, 176, 180)
                .child(new Row()
                        .widthRel(1f)
                        .coverChildrenHeight()
//                        .child(createSlotGrid(syncManager)
//                                .marginRight(4))
                );
    }

//    private Widget<?> createSlotGrid(PanelSyncManager syncManager) {
//        return new Grid().mapTo(2, this.dualHandler.unwrap(), (slot, item) -> {
//
//        });
//    }

    @Override
    public MultiblockAbility<IItemHandlerModifiable> getAbility() {
        return MultiblockAbility.IMPORT_ITEMS;
    }

    @Override
    public void registerAbilities(@NotNull MultiblockAbility<IItemHandlerModifiable> key,
                                  @NotNull List<IItemHandlerModifiable> abilities) {
        abilities.add(this.dualHandler);
    }
}
