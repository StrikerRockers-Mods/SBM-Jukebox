package com.builtbroken.jukebox.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class JukeBoxHandler implements IItemHandlerModifiable {

    private BlockJukebox.TileEntityJukebox jukebox;

    public JukeBoxHandler(BlockJukebox.TileEntityJukebox tileEntityJukebox) {
        this.jukebox = tileEntityJukebox;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot != 0) throw new IndexOutOfBoundsException();
        if (jukebox != null && jukebox.getRecord().isEmpty()) {
            jukebox.setRecord(stack);
        }
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot != 0) throw new IndexOutOfBoundsException();
        return jukebox.getRecord();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot != 0) throw new IndexOutOfBoundsException();
        if (stack.getItem() instanceof ItemRecord && getStackInSlot(slot).isEmpty()) {
            if (!simulate) {
                jukebox.setRecord(stack);
                jukebox.getWorld().playEvent(null, 1010, jukebox.getPos(), Item.getIdFromItem(stack.getItem()));
            }
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0) throw new IndexOutOfBoundsException();
        ItemStack stack = jukebox.getRecord();
        if (!simulate) {
            jukebox.setRecord(ItemStack.EMPTY);
            jukebox.getWorld().playEvent(1010, jukebox.getPos(), 0);
            jukebox.getWorld().playRecord(jukebox.getPos(), null);
        }
        return stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        return 1;
    }
}