package io.github.strikerrocker.jukebox.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

/**
 * Wrapper for {@link JukeboxBlockEntity} to give access to record as an inventory
 */
public class ItemHandlerJukebox implements IItemHandlerModifiable {
    private final JukeboxBlockEntity jukebox;

    ItemHandlerJukebox(JukeboxBlockEntity tileEntityJukebox) {
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
        if (stack.getItem() instanceof RecordItem && getStackInSlot(slot).isEmpty()) {
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

    private void setPlayState(JukeboxBlockEntity jukebox, boolean b) {
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
        return stack.getItem() instanceof RecordItem;
    }
}