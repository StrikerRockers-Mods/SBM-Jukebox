package io.github.strikerrocker.jukebox.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(JukeboxBlockEntity.class)
public abstract class MixinJukeboxBlockEntity implements Container {

    @Shadow
    private static boolean recordShouldStopPlaying(JukeboxBlockEntity $$0, RecordItem $$1) {
        return false;
    }

    @Shadow
    public abstract void playRecord();

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return instance().getRecord().isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return instance().getRecord();
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = instance().getRecord();
        if (stack.getItem() instanceof RecordItem && recordShouldStopPlaying(instance(), (RecordItem) stack.getItem())) {
            instance().clearContent();
            setPlayState(instance(), false, Items.AIR);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = instance().getRecord();
        if (stack.getItem() instanceof RecordItem && recordShouldStopPlaying(instance(), (RecordItem) stack.getItem())) {
            instance().clearContent();
            setPlayState(instance(), false, Items.AIR);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (stack.getItem() instanceof RecordItem && isEmpty()) {
            instance().setRecord(stack);
            playRecord();
            setPlayState(instance(), true, stack.getItem());
        }
    }

    @Override
    public void setChanged() {
        Level world = instance().getLevel();
        BlockPos pos = instance().getBlockPos();
        BlockState state = instance().getBlockState();
        if (world != null) {
            world.blockEntityChanged(pos);
            if (!state.isAir()) {
                world.updateNeighbourForOutputSignal(pos, state.getBlock());
            }
        }
    }


    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        instance().setRecord(ItemStack.EMPTY);
    }

    public JukeboxBlockEntity instance() {
        return ((JukeboxBlockEntity) (Object) this);
    }

    private void setPlayState(JukeboxBlockEntity jukebox, boolean hasRecord, Item itemToPlay) {
        BlockState state = jukebox.getLevel().getBlockState(jukebox.getBlockPos());
        instance().getLevel().levelEvent(LevelEvent.SOUND_PLAY_RECORDING, instance().getBlockPos(), Item.getId(itemToPlay));
        if (state.getBlock() instanceof JukeboxBlock) {
            jukebox.getLevel().setBlock(jukebox.getBlockPos(), state.setValue(JukeboxBlock.HAS_RECORD, hasRecord), 2);
        }
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return stack.getItem() instanceof RecordItem && isEmpty();
    }
}
