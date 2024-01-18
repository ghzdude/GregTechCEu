package gregtech.common.items.behaviors.filter;

import com.cleanroommc.modularui.factory.HandGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;

import com.cleanroommc.modularui.widgets.SlotGroupWidget;

import gregtech.common.covers.filter.FilterTypeRegistry;

public class OreDictFilterUIManager extends BaseFilterUIManager{

    @Override
    public ModularPanel buildUI(HandGuiData guiData, GuiSyncManager guiSyncManager) {
        var filter = FilterTypeRegistry.getItemFilterForStack(guiData.getUsedItemStack());
        return createBasePanel(filter.getContainerStack()).height(160)
                .child(filter.createWidgets(guiSyncManager).top(22).margin(7, 0))
                .child(SlotGroupWidget.playerInventory().bottom(7).left(7));
    }
}
