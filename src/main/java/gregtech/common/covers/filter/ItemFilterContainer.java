package gregtech.common.covers.filter;

import com.cleanroommc.modularui.value.BoolValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.widgets.LabelWidget;
import gregtech.api.gui.widgets.ServerWidgetGroup;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.util.IDirtyNotifiable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandlers;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.layout.Column;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ItemFilterContainer implements INBTSerializable<NBTTagCompound> {

    private final ItemStackHandler filterInventory;
    private final ItemFilterWrapper filterWrapper;
    private final IDirtyNotifiable dirtyNotifiable;
    private boolean isBlacklistFilter = false;
    private int maxStackSize = 1;
    private ItemFilter currentItemFilter;
    private Runnable onFilterInstanceChange;
    private int maxStackSizeLimit = 1;
    private int transferStackSize;

    public ItemFilterContainer(IDirtyNotifiable dirtyNotifiable) {
        this.filterWrapper = new ItemFilterWrapper(this); // for compat
        this.dirtyNotifiable = dirtyNotifiable;
        this.filterInventory = new ItemStackHandler(1) {

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return FilterTypeRegistry.getItemFilterForStack(stack) != null;
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Override
            protected void onLoad() {
                onFilterSlotChange(false);
            }

            @Override
            protected void onContentsChanged(int slot) {
                onFilterSlotChange(true);
            }
        };
    }

    public ItemStackHandler getFilterInventory() {
        return filterInventory;
    }

    public ItemFilterWrapper getFilterWrapper() {
        return filterWrapper;
    }

    public int getMaxStackSize() {
        return maxStackSizeLimit;
    }

    public int getTransferStackSize() {
        if (!showGlobalTransferLimitSlider()) {
            return getMaxStackSize();
        }
        return transferStackSize;
    }

    public void setTransferStackSize(int transferStackSize) {
        this.transferStackSize = MathHelper.clamp(transferStackSize, 1, getMaxStackSize());
        this.maxStackSize = getTransferStackSize();
        onFilterInstanceChange();
        dirtyNotifiable.markAsDirty();
    }

    public void adjustTransferStackSize(int amount) {
        setTransferStackSize(transferStackSize + amount);
    }

    public void setBlacklistFilter(boolean blacklistFilter) {
        isBlacklistFilter = blacklistFilter;
        onFilterInstanceChange();
        dirtyNotifiable.markAsDirty();
    }

    public boolean isBlacklistFilter() {
        return isBlacklistFilter;
    }

    /** Deprecated, uses old builtin MUI*/
    public void initUI(int y, Consumer<gregtech.api.gui.Widget> widgetGroup) {
        widgetGroup.accept(new LabelWidget(10, y, "cover.conveyor.item_filter.title"));
        widgetGroup.accept(new SlotWidget(filterInventory, 0, 10, y + 15)
                .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.FILTER_SLOT_OVERLAY));

        this.initFilterUI(y + 38, widgetGroup);
        this.blacklistUI(y + 38, widgetGroup, () -> true);
    }

    public void initFilterUI(int y, Consumer<gregtech.api.gui.Widget> widgetGroup) {
        widgetGroup.accept(new WidgetGroupItemFilter(y, this::getItemFilter));
    }

    public void blacklistUI(int y, Consumer<gregtech.api.gui.Widget> widgetGroup, BooleanSupplier showBlacklistButton) {
        ServerWidgetGroup blacklistButton = new ServerWidgetGroup(() -> getItemFilter() != null);
        blacklistButton.addWidget(new ToggleButtonWidget(144, y, 20, 20, GuiTextures.BUTTON_BLACKLIST,
                this::isBlacklistFilter, this::setBlacklistFilter).setPredicate(showBlacklistButton)
                .setTooltipText("cover.filter.blacklist"));
        widgetGroup.accept(blacklistButton);
    }

    /** Uses Cleanroom MUI*/
    public ParentWidget<?> initUI(GuiSyncManager syncManager) {
        syncManager.registerSlotGroup("filter_slot", 1, 100);

        return new Column().padding(4).left(5).coverChildrenWidth()
                .child(IKey.lang("cover.conveyor.item_filter.title").asWidget())
                .child(new ItemSlot().slot(
                        SyncHandlers.itemSlot(filterInventory, 0)
                                .slotGroup("filter_slot")))
                .childIf(this::hasItemFilter, () -> getItemFilter().initUI().setEnabledIf(w -> hasItemFilter()))
                .childIf(this::hasItemFilter, () -> new CycleButtonWidget().setEnabledIf(w -> hasItemFilter())
                        .value(new BoolValue.Dynamic(this::isBlacklistFilter, this::setBlacklistFilter))
                        .tooltip(tooltip -> tooltip.addLine(IKey.lang("cover.filter.blacklist"))));
    }

    protected void onFilterSlotChange(boolean notify) {
        ItemStack filterStack = filterInventory.getStackInSlot(0);
        ItemFilter newItemFilter = FilterTypeRegistry.getItemFilterForStack(filterStack);
        ItemFilter currentItemFilter = getItemFilter();
        if (newItemFilter == null) {
            if (hasItemFilter()) {
                setItemFilter(null);
                setBlacklistFilter(false);
                if (notify) onFilterInstanceChange();
            }
        } else if (currentItemFilter == null ||
                newItemFilter.getClass() != currentItemFilter.getClass()) {
                    setItemFilter(newItemFilter);
                    if (notify) onFilterInstanceChange();
                }
    }

    public void setOnFilterInstanceChange(Runnable onFilterInstanceChange) {
        this.onFilterInstanceChange = onFilterInstanceChange;
    }

    public void onFilterInstanceChange() {
        this.maxStackSize = isBlacklistFilter ? 1 : getMaxStackSize();
        if (currentItemFilter != null) {
            currentItemFilter.setMaxStackSize(getMaxStackSize());
        }
        dirtyNotifiable.markAsDirty();
    }

    public void setMaxStackSize(int maxStackSizeLimit) {
        this.maxStackSizeLimit = maxStackSizeLimit;
        setTransferStackSize(transferStackSize);
    }

    public boolean showGlobalTransferLimitSlider() {
        return getMaxStackSize() > 1 && (isBlacklistFilter() || !hasItemFilter()|| currentItemFilter.showGlobalTransferLimitSlider());
    }

    public int getSlotTransferLimit(int slotIndex) {
        if (isBlacklistFilter() || currentItemFilter == null) {
            return getTransferStackSize();
        }
        return currentItemFilter.getSlotTransferLimit(slotIndex, getTransferStackSize());
    }

    public int getStackTransferLimit(ItemStack stack) {
        if (isBlacklistFilter() || currentItemFilter == null) {
            return getTransferStackSize();
        }
        return currentItemFilter.getStackTransferLimit(stack, getTransferStackSize());
    }

    public ItemFilter.MatchResult<Integer> matchItemStack(ItemStack itemStack) {
        return matchItemStack(itemStack, !isBlacklistFilter());
    }

    public ItemFilter.MatchResult<Integer> matchItemStack(ItemStack itemStack, boolean whitelist) {
        ItemFilter.MatchResult<Integer> originalResult;
        if (currentItemFilter == null) {
            originalResult = ItemFilter.EMPTY_MATCH;
        } else {
            originalResult = currentItemFilter.matchItemStack(itemStack);
        }
        if (!whitelist) {
            originalResult.flipMatch();
        }
        return originalResult;
    }

    public boolean testItemStack(ItemStack itemStack) {
        return matchItemStack(itemStack) != null;
    }

    public boolean testItemStack(ItemStack itemStack, boolean whitelist) {
        return matchItemStack(itemStack, whitelist).matched();
    }


    public void setItemFilter(ItemFilter itemFilter) {
        this.currentItemFilter = itemFilter;
        if (currentItemFilter != null) {
            currentItemFilter.setDirtyNotifiable(dirtyNotifiable);
        }
        if (onFilterInstanceChange != null) {
            this.onFilterInstanceChange.run();
        }
    }

    public ItemFilter getItemFilter() {
        return currentItemFilter;
    }

    public boolean hasItemFilter() {
        return currentItemFilter != null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setTag("FilterInventory", filterInventory.serializeNBT());
        tagCompound.setBoolean("IsBlacklist", filterWrapper.isBlacklistFilter());
        tagCompound.setInteger("MaxStackSize", maxStackSizeLimit);
        tagCompound.setInteger("TransferStackSize", transferStackSize);
        if (filterWrapper.getItemFilter() != null) {
            NBTTagCompound filterInventory = new NBTTagCompound();
            filterWrapper.getItemFilter().writeToNBT(filterInventory);
            tagCompound.setTag("Filter", filterInventory);
        }
        return tagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tagCompound) {
        this.filterInventory.deserializeNBT(tagCompound.getCompoundTag("FilterInventory"));
        this.filterWrapper.setBlacklistFilter(tagCompound.getBoolean("IsBlacklist"));
        setMaxStackSize(tagCompound.getInteger("MaxStackSize"));
        setTransferStackSize(tagCompound.getInteger("TransferStackSize"));
        if (filterWrapper.getItemFilter() != null) {
            this.filterWrapper.getItemFilter().readFromNBT(tagCompound.getCompoundTag("Filter"));
        }
    }
}
