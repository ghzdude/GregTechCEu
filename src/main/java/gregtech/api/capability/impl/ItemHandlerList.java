package gregtech.api.capability.impl;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Efficiently delegates calls into multiple item handlers
 */
public class ItemHandlerList extends AbstractList<IItemHandler> implements IItemHandlerModifiable {

    private final Int2ObjectMap<IItemHandler> handlerBySlotIndex = new Int2ObjectOpenHashMap<>();
    private final Object2IntArrayMap<IItemHandler> baseIndexOffset = new Object2IntArrayMap<>();

    private final List<IItemHandler> handlerList = new ArrayList<>();

    public ItemHandlerList(List<? extends IItemHandler> itemHandlerList) {
        addAll(itemHandlerList);
    }

    @Override
    public int getSlots() {
        return handlerBySlotIndex.size();
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        IItemHandler itemHandler = handlerBySlotIndex.get(slot);
        if (!(itemHandler instanceof IItemHandlerModifiable))
            throw new UnsupportedOperationException("Handler " + itemHandler + " does not support this method");
        ((IItemHandlerModifiable) itemHandler).setStackInSlot(slot - baseIndexOffset.getInt(itemHandler), stack);
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        IItemHandler itemHandler = handlerBySlotIndex.get(slot);
        return itemHandler.getStackInSlot(slot - baseIndexOffset.getInt(itemHandler));
    }

    @Override
    public int getSlotLimit(int slot) {
        IItemHandler itemHandler = handlerBySlotIndex.get(slot);
        return itemHandler.getSlotLimit(slot - baseIndexOffset.getInt(itemHandler));
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        IItemHandler itemHandler = handlerBySlotIndex.get(slot);
        return itemHandler.insertItem(slot - baseIndexOffset.getInt(itemHandler), stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        IItemHandler itemHandler = handlerBySlotIndex.get(slot);
        return itemHandler.extractItem(slot - baseIndexOffset.getInt(itemHandler), amount, simulate);
    }

    @NotNull
    public Collection<IItemHandler> getBackingHandlers() {
        return Collections.unmodifiableCollection(handlerList);
    }

    @Override
    public int size() {
        return handlerList.size();
    }

    @Override
    public boolean add(IItemHandler handler) {
        int s = size();
        add(s, handler);
        return s != size();
    }

    @Override
    public void add(int index, IItemHandler element) {
//        Objects.checkIndex(index, size());
        if (handlerList.contains(element)) {
            throw new IllegalArgumentException("Attempted to add item handler " + element + " twice");
        }
        handlerList.add(index, element);
        int offset = handlerBySlotIndex.size();
        baseIndexOffset.put(element, offset);
        for (int slotIndex = 0; slotIndex < element.getSlots(); slotIndex++) {
            handlerBySlotIndex.put(offset + slotIndex, element);
        }
    }

    @Override
    public IItemHandler get(int index) {
        return handlerList.get(index);
    }

    @Override
    public IItemHandler remove(int index) {
//        Objects.checkIndex(index, size());
        var handler2 = get(index);
        int offset2 = baseIndexOffset.getInt(handler2);

        for (int i = index; i < size(); i++) {
            int offset = baseIndexOffset.removeInt(get(i));
            for (int j = 0; j < get(i).getSlots(); j++) {
                handlerBySlotIndex.remove(offset + j);
            }
        }

        var removed = handlerList.remove(index);
        for (var handler : handlerList) {
            if (baseIndexOffset.containsKey(handler))
                continue;

            int offset = handlerBySlotIndex.size();
            baseIndexOffset.put(handler, offset);
            for (int i = 0; i < handler.getSlots(); i++) {
                handlerBySlotIndex.put(offset + i, handler);
            }
        }
        return removed;
    }
}
