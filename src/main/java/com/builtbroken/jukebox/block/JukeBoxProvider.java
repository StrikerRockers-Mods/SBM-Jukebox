package com.builtbroken.jukebox.block;

import net.minecraft.block.BlockJukebox;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JukeBoxProvider implements ICapabilityProvider {

    private BlockJukebox.TileEntityJukebox jukebox;

    public JukeBoxProvider(BlockJukebox.TileEntityJukebox jukebox) {
        this.jukebox = jukebox;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            //noinspection SingleStatementInBlock
            return (T) new JukeBoxHandler(jukebox);
        }
        return null;
    }
}
