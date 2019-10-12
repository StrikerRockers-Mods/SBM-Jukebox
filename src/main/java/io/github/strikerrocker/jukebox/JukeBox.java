package io.github.strikerrocker.jukebox;

import io.github.strikerrocker.jukebox.block.CapProviderJukeBox;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Simple mod to improve usability of the jukebox
 *
 * Created by StrikerRocker on 4/7/2018.
 */
@Mod.EventBusSubscriber()
@Mod("jukebox")
public class JukeBox {

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof JukeboxTileEntity) {
            event.addCapability(new ResourceLocation("jukebox", "itemhandler"), new CapProviderJukeBox((JukeboxTileEntity) event.getObject()));
        }
    }
}
