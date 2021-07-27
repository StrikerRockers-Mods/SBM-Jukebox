package io.github.strikerrocker.jukebox.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * Provider for {@link JukeboxBlockEntity}
 */
public class CapProviderJukebox implements ICapabilityProvider {
    private final LazyOptional inventoryHolder;

    public CapProviderJukebox(JukeboxBlockEntity jukebox) {
        this.inventoryHolder = LazyOptional.of(() -> new ItemHandlerJukebox(jukebox));
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap.orEmpty(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inventoryHolder);
    }
}
