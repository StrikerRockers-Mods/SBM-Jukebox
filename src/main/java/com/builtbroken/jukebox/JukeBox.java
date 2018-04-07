package com.builtbroken.jukebox;

import com.builtbroken.jukebox.block.JukeBoxProvider;
import net.minecraft.block.BlockJukebox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.builtbroken.jukebox.JukeBox.*;

@Mod.EventBusSubscriber(modid = ID)
@Mod(modid = ID, name = NAME, version = VERSION)
public class JukeBox {
    final static String ID = "jukebox";
    final static String NAME = "jukebox";
    final static String VERSION = "1.0";

    @Mod.Instance(ID)
    public static JukeBox INSTANCE;

    @SubscribeEvent
    public static void onAttachCap(AttachCapabilitiesEvent<TileEntity> event){
        if (event.getObject() instanceof BlockJukebox.TileEntityJukebox) {
            event.addCapability(new ResourceLocation("jukebox", "itemhandler"), new JukeBoxProvider((BlockJukebox.TileEntityJukebox) event.getObject()));
        }
    }

}
