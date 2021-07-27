package io.github.strikerrocker.jukebox.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

/**
 * Wrapper for {@link JukeboxTileEntity} to give access to record as an inventory
 *
 * Created by StrikerRocker on 4/7/2018.
 */
public class ItemHandlerJukeBox implements IItemHandlerModifiable {
    private final JukeboxTileEntity jukebox;

    ItemHandlerJukeBox(JukeboxTileEntity tileEntityJukebox) {
        this.jukebox = tileEntityJukebox;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
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
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        return jukebox.getRecord();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        if (stack.getItem() instanceof MusicDiscItem && getStackInSlot(slot).isEmpty()) {
            if (!simulate) {
                jukebox.setRecord(stack);
                setPlayState(jukebox, true);
                jukebox.getLevel().levelEvent(null, 1010, jukebox.getBlockPos(), Item.getId(stack.getItem()));
            }
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        ItemStack stack = jukebox.getRecord();
        if (!simulate) {
            jukebox.clearContent();
            setPlayState(jukebox, false);
            jukebox.getLevel().levelEvent(1010, jukebox.getBlockPos(), 0);
        }
        return stack;
    }

    private void setPlayState(JukeboxTileEntity jukebox, boolean b) {
        BlockState state = jukebox.getLevel().getBlockState(jukebox.getBlockPos());
        if (state.getBlock() instanceof JukeboxBlock) {
            jukebox.getLevel().setBlock(jukebox.getBlockPos(), state.setValue(JukeboxBlock.HAS_RECORD, b), 1);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof MusicDiscItem;
    }
}