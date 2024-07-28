package gregtech.api.metatileentity.multiblock;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface IMultiblockAbilityPart extends IMultiblockPart {

    MultiblockAbility<?> getAbility();

    default @NotNull List<MultiblockAbility<?>> getAbilities() {
        return Collections.singletonList(getAbility());
    }

    <T> void registerAbilities(@NotNull MultiblockAbility<T> key, @NotNull List<T> abilities);
}
