package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;
import gregtech.api.storagedrawers.InsertionData;

import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = StandardDrawerGroup.class, remap = false)
public abstract class DrawerGroupMixin {

    @Mixin(value = StandardDrawerGroup.DrawerData.class, remap = false)
    public abstract static class DrawerDataMixin implements IAuxData {

        @Shadow
        public abstract void setExtendedData(String key, Object data);

        @Shadow
        public abstract Object getExtendedData(String key);

        @Override
        public void gregTech$setData(String key, Object value) {
            setExtendedData(key, value);
        }

        @Override
        public @Nullable Object gregTech$getData(String key) {
            return getExtendedData(key);
        }

        @ModifyReturnValue(method = "getAcceptingRemainingCapacity", at = @At(value = "RETURN", ordinal = 1))
        public int modifyStoredItemCount(int original) {
            var data = getDataAncCast(KEY, InsertionData.class);
            if (data == null || !data.active) return original;
            else return original + data.inserted;
        }
    }
}
