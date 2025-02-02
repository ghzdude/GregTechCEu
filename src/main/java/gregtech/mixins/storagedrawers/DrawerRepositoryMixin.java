package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;
import gregtech.api.storagedrawers.InsertionData;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemRepository;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = DrawerItemRepository.class, remap = false)
public abstract class DrawerRepositoryMixin {

    @Redirect(method = "insertItem",
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
}
