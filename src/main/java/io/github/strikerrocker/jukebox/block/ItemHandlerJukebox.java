package io.github.strikerrocker.jukebox.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.event.VanillaGameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Wrapper for {@link JukeboxBlockEntity} to give access to record as an inventory
 */
public class ItemHandlerJukebox implements IItemHandlerModifiable {
    private final JukeboxBlockEntity _jukebox;
    private boolean _isPlaying = false;
    private boolean _allowExtraction = false;


    //Default constructor needs to register self as an event handler, but perhaps i could make a seperate class that functions as the event handler and register it with that object here instead
    //That would be proper design but shush! this is the implementation to prove if this even works at all, will refactor at a later date (maybe if not overkill)
    ItemHandlerJukebox(JukeboxBlockEntity tileEntityJukebox) {
        this._jukebox = tileEntityJukebox;
        MinecraftForge.EVENT_BUS.register(this);
    }


    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        if (_jukebox != null && _jukebox.getRecord().isEmpty()) {
            _jukebox.setRecord(stack);
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

        return _jukebox.getRecord();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }

        if (stack.getItem() instanceof RecordItem && getStackInSlot(slot).isEmpty() && !_isPlaying) {
            if (!simulate) {
                _jukebox.setRecord(stack);
                setPlayState(_jukebox, true);

                _jukebox.getLevel().levelEvent(null, 1010, _jukebox.getBlockPos(), Item.getId(stack.getItem()));
                _jukebox.playRecord();
            }
            return ItemStack.EMPTY;
        }
        return stack;
    }
    @SubscribeEvent()
    public void onJukeboxStopDetected(@NotNull VanillaGameEvent possibleJukeboxEvent) {

        if(possibleJukeboxEvent.getVanillaEvent().getName().toLowerCase().contains("jukebox")) {
            System.out.printf("Got jukebox GameEvent in ItemHandlerJukebox, eventName -> %s, vect 3 -> %s%n", possibleJukeboxEvent.getVanillaEvent().getName(), possibleJukeboxEvent.getEventPosition().toString());
            if (isEventFromCurrentJukebox(possibleJukeboxEvent)) {
                if (isJukeboxStartEvent(possibleJukeboxEvent)) {
                    _isPlaying = true;
                    _allowExtraction = false;
                } else if (isJukeboxStopEvent(possibleJukeboxEvent)) {
                    _isPlaying = false;
                    _allowExtraction = true;
                } else if (isJukeboxBlockChangeEvent(possibleJukeboxEvent)) {
                    //check if jukebox is empty or not
                    _isPlaying = _jukebox.getBlockState().getValue(BlockStateProperties.HAS_RECORD);
                }
            }
        }
    }
    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != 0) {
            throw new IndexOutOfBoundsException();
        }
        ItemStack stack = _jukebox.getRecord();

        if (!_allowExtraction) {
            return ItemStack.EMPTY;
        }
        var position = _jukebox.getBlockPos();

        if (!simulate) {

            setPlayState(_jukebox, false);
            //This may trigger the stop/eject animation??? We shouldnt call this on load prolly.
            _jukebox.getLevel().levelEvent(1010, _jukebox.getBlockPos(), 0);
            _jukebox.clearContent();
            _allowExtraction = false;
        }

        return stack;
    }



    private boolean isJukeboxBlockChangeEvent(VanillaGameEvent possibleJukeboxEvent) {
        return possibleJukeboxEvent.getVanillaEvent().getName().equals("block_change");
    }

    private boolean isJukeboxStartEvent(VanillaGameEvent possibleJukeboxEvent) {
        return possibleJukeboxEvent.getVanillaEvent().getName().equals("jukebox_play");
    }

    private boolean isJukeboxStopEvent(VanillaGameEvent possibleJukeboxEvent) {
        return possibleJukeboxEvent.getVanillaEvent().getName().equals("jukebox_stop_play");
    }

    private boolean isEventFromCurrentJukebox(VanillaGameEvent possibleJukeboxEvent) {
        return arePositionsEqual(possibleJukeboxEvent.getEventPosition(), _jukebox.getBlockPos());
    }

    private boolean arePositionsEqual(Vec3 eventPosition, BlockPos blockPos) {
        return (Math.floor(eventPosition.x) == blockPos.getX()) && (Math.floor(eventPosition.y) == blockPos.getY()) && (Math.floor(eventPosition.z) == blockPos.getZ());
    }

    private void setPlayState(JukeboxBlockEntity jukebox, boolean b) {
        _isPlaying = b; //try to fix hopper immediately removing disc when inserted. We can set true at the insert event.
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