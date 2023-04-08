package gregtech.common.covers.filter;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.Widget;
import gregtech.api.gui.widgets.PhantomSlotWidget;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.util.LargeStackSizeItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.function.Consumer;

public class BigItemFilter extends SimpleItemFilter {

    private static final int MAX_MATCH_SLOTS = 9 * 3;
    protected final ItemStackHandler itemFilterSlots;


    public BigItemFilter() {
        this.itemFilterSlots = new LargeStackSizeItemStackHandler(MAX_MATCH_SLOTS) {
            @Override
            public int getSlotLimit(int slot) {
                return getMaxStackSize();
            }
        };
    }

    @Override
    public void initUI(Consumer<Widget> widgetGroup) {
        int COLUMN = 9;
        for (int i = 0; i < MAX_MATCH_SLOTS; i++) {
            widgetGroup.accept(new PhantomSlotWidget(itemFilterSlots, i, 7 + 18 * (i % COLUMN), 18 * (Math.floorDiv(i, COLUMN))).setBackgroundTexture(GuiTextures.SLOT));
        }
        widgetGroup.accept(new ToggleButtonWidget(74, -22, 20, 20, GuiTextures.BUTTON_FILTER_DAMAGE,
                () -> ignoreDamage, this::setIgnoreDamage).setTooltipText("cover.item_filter.ignore_damage"));
        widgetGroup.accept(new ToggleButtonWidget(99, -22, 20, 20, GuiTextures.BUTTON_FILTER_NBT,
                () -> ignoreNBT, this::setIgnoreNBT).setTooltipText("cover.item_filter.ignore_nbt"));

    }
}
