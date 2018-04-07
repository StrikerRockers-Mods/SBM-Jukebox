package com.builtbroken.jukebox;

import com.builtbroken.jukebox.block.JukeBoxProvider;
import net.minecraft.block.BlockJukebox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.builtbroken.jukebox.JukeBox.*;

@Mod.EventBusSubscriber()
@Mod(modid = DOMAIN, name = NAME, version = VERSION)
public class JukeBox {
    
    public static final String DOMAIN = "jukebox";
    public static final String NAME = "[SBM] Jukebox";
    public static final String PREFIX = DOMAIN + ":";

    public static final String MAJOR_VERSION = "@MAJOR@";
    public static final String MINOR_VERSION = "@MINOR@";
    public static final String REVISION_VERSION = "@REVIS@";
    public static final String BUILD_VERSION = "@BUILD@";
    public static final String MC_VERSION = "@MC@";
    public static final String VERSION = MC_VERSION + "-" + MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION_VERSION + "." + BUILD_VERSION;

    @Mod.Instance()
    public static JukeBox INSTANCE;

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<TileEntity> event) {
        if (event.getObject() instanceof BlockJukebox.TileEntityJukebox) {
            event.addCapability(new ResourceLocation("jukebox", "itemhandler"), new JukeBoxProvider((BlockJukebox.TileEntityJukebox) event.getObject()));
        }
    }
}
