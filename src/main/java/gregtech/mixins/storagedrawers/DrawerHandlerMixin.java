package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;

import net.minecraft.item.ItemStack;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DrawerItemHandler.class, remap = false)
public abstract class DrawerHandlerMixin {

    @Inject(method = "insertItemInternal",
            at = @At(value = "INVOKE_ASSIGN",
                     target = "Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawerGroup;getDrawer(I)Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawer;"))
    public void setData(int slot, ItemStack stack, boolean simulate,
                        CallbackInfoReturnable<ItemStack> cir,
                        @Local IDrawer drawer) {
        int inserted = drawer.isEmpty() ?
                drawer.getAcceptingMaxCapacity(stack) :
                drawer.getAcceptingRemainingCapacity();

        if (drawer instanceof IAuxData auxData) {
            var data = auxData.getOrCreateData().put(slot, inserted);
        }
    }
}
