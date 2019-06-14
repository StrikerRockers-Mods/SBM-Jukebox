package com.builtbroken.jukebox.block;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * Wrapper for {@link BlockJukebox.TileEntityJukebox} to give access to record as an inventory
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by StrikerRocker on 4/7/2018.
 */
public class ItemHandlerJukeBox implements IItemHandlerModifiable
{
    private final JukeboxTileEntity jukebox;

    public ItemHandlerJukeBox(JukeboxTileEntity tileEntityJukebox)
    {
        this.jukebox = tileEntityJukebox;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        if (jukebox != null && jukebox.getRecord().isEmpty())
        {
            jukebox.setRecord(stack);
        }
    }

    @Override
    public int getSlots()
    {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        return jukebox.getRecord();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        if (stack.getItem() instanceof MusicDiscItem && getStackInSlot(slot).isEmpty())
        {
            if (!simulate)
            {
                //Set record
                jukebox.setRecord(stack);

                //Set state
                setPlayState(jukebox, true);

                //Trigger record to play
                jukebox.getWorld().playEvent(null, 1010, jukebox.getPos(), Item.getIdFromItem(stack.getItem()));
            }
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        ItemStack stack = jukebox.getRecord();
        if (!simulate)
        {
            //Set to empty
            jukebox.setRecord(ItemStack.EMPTY);

            //Set state
            setPlayState(jukebox, false);

            //Update audio
            jukebox.getWorld().playEvent(1010, jukebox.getPos(), 0);
        }
        return stack;
    }

    protected void setPlayState(JukeboxTileEntity jukebox, boolean b)
    {
        //Set state
        BlockState state = jukebox.getWorld().getBlockState(jukebox.getPos());
        if (state.getBlock() instanceof JukeboxBlock)
        {
            jukebox.getWorld().setBlockState(jukebox.getPos(), state.with(JukeboxBlock.HAS_RECORD, Boolean.valueOf(b)), 2);
        }
    }

    @Override
    public int getSlotLimit(int slot)
    {
        if (slot != 0)
        {
            throw new IndexOutOfBoundsException();
        }
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        return stack.getItem() instanceof MusicDiscItem;
    }
}