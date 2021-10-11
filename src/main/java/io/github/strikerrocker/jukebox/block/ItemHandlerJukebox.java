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

    /*
     Jukebox cannot have a record.
     ItemStack should not be empty.
     Used for hoppers and other pushers to push items inside the jukebox.
      */
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (jukebox != null && !stack.isEmpty() && jukebox.getRecord().isEmpty()) {
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
        return jukebox.getRecord();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
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
        if (!simulate) {
            jukebox.clearContent();
            setPlayState(jukebox, false);
            jukebox.getLevel().levelEvent(1010, jukebox.getBlockPos(), 0);
        }
        return jukebox.getRecord();
    }

    private void setPlayState(JukeboxBlockEntity jukebox, boolean flag) {
        BlockState state = jukebox.getLevel().getBlockState(jukebox.getBlockPos());
        if (state.getBlock() instanceof JukeboxBlock) {
            jukebox.getLevel().setBlock(jukebox.getBlockPos(), state.setValue(JukeboxBlock.HAS_RECORD, flag), 1);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof RecordItem;
    }


}