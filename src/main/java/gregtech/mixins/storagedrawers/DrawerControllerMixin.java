package gregtech.mixins.storagedrawers;

import gregtech.api.storagedrawers.IAuxData;
import gregtech.api.storagedrawers.SlotGroupAccessor;

import net.minecraft.item.ItemStack;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityController;
import com.jaquadro.minecraft.storagedrawers.capabilities.DrawerItemRepository;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(value = TileEntityController.class, remap = false)
public abstract class DrawerControllerMixin {

    @Mixin(targets = "com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityController$ItemRepository",
           remap = false)
    public static abstract class RepositoryMixin extends DrawerItemRepository {

        @Shadow
        @Final
        TileEntityController this$0;

        @Shadow
        protected abstract boolean hasAccess(IDrawerGroup group, IDrawer drawer);

        public RepositoryMixin(IDrawerGroup group) {
            super(group);
        }

        // should probably use unreflect for this
        @Unique
        Method gregTech$getGroupForSlotRecord = null;

        @Unique
        private IDrawerGroup gregTech$getDrawerGroup(TileEntityController controller, SlotGroupAccessor o) {
            try {
                if (gregTech$getGroupForSlotRecord == null) {
                    gregTech$getGroupForSlotRecord = TileEntityController.class
                            .getDeclaredMethod("getGroupForSlotRecord", o.gregTech$getType());
                    gregTech$getGroupForSlotRecord.setAccessible(true);
                }

                return (IDrawerGroup) gregTech$getGroupForSlotRecord.invoke(controller, o);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Inject(method = "insertItem",
                at = @At(value = "INVOKE_ASSIGN",
                         target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;"),
                cancellable = true)
        public void readData(ItemStack stack, boolean simulate, Predicate<ItemStack> predicate,
                             CallbackInfoReturnable<ItemStack> cir,
                             @Local Iterator<SlotGroupAccessor> iterator,
                             @Local LocalIntRef amount,
                             @Local Set<Integer> checkedSlots) {
            while (iterator.hasNext()) {
                SlotGroupAccessor record = iterator.next();
                IDrawerGroup candidateGroup = gregTech$getDrawerGroup(this$0, record);
                if (candidateGroup != null) {
                    IDrawer drawerx = candidateGroup.getDrawer(record.gregTech$getSlot());
                    if (!drawerx.isEmpty() && this.testPredicateInsert(drawerx, stack, predicate) &&
                            this.hasAccess(candidateGroup, drawerx)) {
                        if (simulate && drawerx instanceof IAuxData auxData) {
                            int inserted = auxData.getOrCreateData().get(record.gregTech$getSlot());
                            if (inserted + drawerx.getStoredItemCount() == drawerx.getMaxCapacity()) {
                                continue;
                            }
                        }
                        amount.set(simulate ?
                                Math.max(amount.get() - drawerx.getAcceptingRemainingCapacity(), 0) :
                                drawerx.adjustStoredItemCount(amount.get()));
                        if (amount.get() == 0) {
                            cir.setReturnValue(ItemStack.EMPTY);
                        }

                        if (simulate) {
                            checkedSlots.add(record.gregTech$getIndex());
                        }
                    }
                }
            }
        }
    }

    @Mixin(targets = "com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityController$SlotRecord", remap = false)
    private static abstract class SlotRecordMixin implements SlotGroupAccessor {

        @Shadow
        public int slot;

        @Shadow
        public int index;

        @Override
        public int gregTech$getSlot() {
            return slot;
        }

        @Override
        public int gregTech$getIndex() {
            return index;
        }

        @Override
        public Class<?> gregTech$getType() {
            return getClass();
        }
    }
}
