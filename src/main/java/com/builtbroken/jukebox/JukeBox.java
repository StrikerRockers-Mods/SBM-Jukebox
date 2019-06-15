package com.builtbroken.jukebox;

import com.builtbroken.jukebox.block.CapProviderJukeBox;
import net.minecraft.tileentity.JukeboxTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.builtbroken.jukebox.JukeBox.DOMAIN;

/**
 * Simple mod to improve usability of the jukebox
 *
 * @see <a href="https://github.com/BuiltBrokenModding/VoltzEngine/blob/development/license.md">License</a> for what you can and can't do with the code.
 * Created by StrikerRocker on 4/7/2018.
 */
@Mod.EventBusSubscriber()
@Mod(DOMAIN)
public class JukeBox
{
    public static final String DOMAIN = "jukebox";

    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String MC_VERSION = "@MC@";
    public static final String VERSION = MC_VERSION + "-" + MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<TileEntity> event)
    {
        if (event.getObject() instanceof JukeboxTileEntity)
        {
            event.addCapability(new ResourceLocation("jukebox", "itemhandler"), new CapProviderJukeBox((JukeboxTileEntity) event.getObject()));
        }
    }
}
