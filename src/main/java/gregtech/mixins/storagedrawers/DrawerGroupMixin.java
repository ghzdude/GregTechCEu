package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;

import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
    }
}
