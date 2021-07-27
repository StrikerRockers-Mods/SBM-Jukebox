package io.github.strikerrocker.jukebox;

import io.github.strikerrocker.jukebox.block.CapProviderJukebox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Simple mod to improve usability of the jukebox
 */
@Mod.EventBusSubscriber()
@Mod("jukebox")
public class Jukebox {

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<BlockEntity> event) {
        if (event.getObject() instanceof JukeboxBlockEntity) {
            event.addCapability(new ResourceLocation("jukebox", "itemhandler"), new CapProviderJukebox((JukeboxBlockEntity) event.getObject()));
        }
    }
}
