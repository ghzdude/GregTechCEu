package gregtech.api.mui.sync;

import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GuiSyncManager;
import com.cleanroommc.modularui.value.sync.PanelSyncHandler;
import org.jetbrains.annotations.NotNull;

public class SimplePanelSH extends PanelSyncHandler {

    private final UIFunction uiFunction;

    public SimplePanelSH(@NotNull ModularPanel mainPanel, @NotNull UIFunction uiFunction) {
        super(mainPanel);
        this.uiFunction = uiFunction;
    }

    @Override
    public ModularPanel createUI(ModularPanel mainPanel, GuiSyncManager syncManager) {
        return uiFunction.apply(mainPanel, syncManager);
    }

    /**
     * Opens and closes the panel as needed, and plays the click sound.
     *
     * @param mouseButton the mouse button pressed
     * @return if the action was successful
     * @see Interactable#onMousePressed(int)
     */
    public boolean openClose(@SuppressWarnings("unused") int mouseButton) {
        if (isPanelOpen()) {
            closePanel();
        } else {
            openPanel();
        }
        Interactable.playButtonClickSound();
        return true;
    }

    @FunctionalInterface
    public interface UIFunction {

        /**
         * @see PanelSyncHandler#createUI(ModularPanel, GuiSyncManager)
         */
        @NotNull
        ModularPanel apply(@NotNull ModularPanel mainPanel, @NotNull GuiSyncManager syncManager);
    }
}
