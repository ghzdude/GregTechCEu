package gregtech.common.covers.filter;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.Widget;
import gregtech.api.gui.widgets.ServerWidgetGroup;
import gregtech.api.gui.widgets.ToggleButtonWidget;
import gregtech.api.util.IDirtyNotifiable;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class BigItemFilterWrapper extends ItemFilterWrapper {
    public BigItemFilterWrapper(IDirtyNotifiable dirtyNotifiable) {
        super(dirtyNotifiable);
    }

    @Override
    public void initUI(int y, Consumer<Widget> widgetGroup) {
        widgetGroup.accept(new WidgetGroupItemFilter(y, this::getItemFilter));
    }

    @Override
    public void blacklistUI(int y, Consumer<Widget> widgetGroup, BooleanSupplier showBlacklistButton) {
        ServerWidgetGroup blacklistButton = new ServerWidgetGroup(() -> getItemFilter() != null);
        blacklistButton.addWidget(new ToggleButtonWidget(144, y - 22, 20, 20, GuiTextures.BUTTON_BLACKLIST,
                this::isBlacklistFilter, this::setBlacklistFilter).setPredicate(showBlacklistButton).setTooltipText("cover.filter.blacklist"));
        widgetGroup.accept(blacklistButton);
    }
}
