package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;
import gregtech.api.storagedrawers.InsertionData;

import net.minecraft.item.ItemStack;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemHandler;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DrawerItemHandler.class, remap = false)
public class DrawerHandlerMixin {

    @Redirect(method = "insertItemInternal",
              at = @At(value = "INVOKE",
                       target = "Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawerGroup;getDrawer(I)Lcom/jaquadro/minecraft/storagedrawers/api/storage/IDrawer;"))
    public IDrawer updateData(IDrawerGroup instance, int i, @Local(argsOnly = true) boolean simulate) {
        IDrawer drawer = instance.getDrawer(i);
        if (drawer instanceof IAuxData auxData) {
            var data = auxData.getOrCreateData(IAuxData.KEY, InsertionData::new);
            data.simulate = simulate;
            data.active = true;
        }
        return drawer;
    }

    @Inject(method = "insertItemInternal", at = @At("RETURN"))
    public void finishData(int slot, ItemStack stack, boolean simulate, CallbackInfoReturnable<ItemStack> cir,
                           @Local IDrawer drawer) {
        if (drawer instanceof IAuxData auxData && auxData.gregTech$hasData(IAuxData.KEY)) {
            var data = auxData.getDataAncCast(IAuxData.KEY, InsertionData.class);
            data.active = false;
            if (!simulate) data.inserted = 0;
        }
    }
}
