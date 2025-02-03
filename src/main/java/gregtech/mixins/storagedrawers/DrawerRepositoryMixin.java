package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;
import gregtech.api.storagedrawers.InsertionData;

import net.minecraft.item.ItemStack;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemRepository;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(value = DrawerItemRepository.class, remap = false)
public abstract class DrawerRepositoryMixin {

    @Inject(method = "insertItem",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawerGroup;getDrawer(I)Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawer;"),
            cancellable = true)
    public void readData(ItemStack stack, boolean simulate, Predicate<ItemStack> predicate,
                         CallbackInfoReturnable<ItemStack> cir,
                         @Local IDrawer drawer) {
        if (drawer instanceof IAuxData auxData) {
            var data = auxData.getOrCreateData(IAuxData.KEY, InsertionData::new);
            if (simulate && data.inserted + drawer.getStoredItemCount() == drawer.getMaxCapacity()) {
                cir.setReturnValue(stack);
            }
        }
    }
}
