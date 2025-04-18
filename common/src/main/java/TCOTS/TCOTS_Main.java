package TCOTS;

import TCOTS.items.TCOTS_Items;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import io.wispforest.owo.network.OwoNetChannel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TCOTS_Main {
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final TCOTS.TCOTS_Config CONFIG = TCOTS.TCOTS_Config.createAndLoad();

    public static List<Tuple<ResourceLocation, JsonObject>> recipes= new ArrayList<>();
    public static final String MOD_ID = "tcots_witcher";
    public static final OwoNetChannel PACKETS_CHANNEL = OwoNetChannel.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"));

    public static void init(){
//        TCOTS_DynamicRecipes.init();
        TCOTS_Items.init();
    }

}
