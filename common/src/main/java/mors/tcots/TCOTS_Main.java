package mors.tcots;

import mors.tcots.items.weapons.GiantAnchorItem;
import com.mojang.logging.LogUtils;
import io.wispforest.owo.network.OwoNetChannel;
import mors.tcots.registry.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public class TCOTS_Main {
    public static final Logger LOGGER = LogUtils.getLogger();
    //TODO: Modify textures
    public static final mors.tcots.TCOTS_Config CONFIG = mors.tcots.TCOTS_Config.createAndLoad();

    public static final String MOD_ID = "tcots_witcher";
    public static final OwoNetChannel PACKETS_CHANNEL = OwoNetChannel.create(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"));

    public static void initCommon(){
        //Blocks
        TCOTS_Blocks.initBlocks(); TCOTS_Registries.BLOCKS.register();
        TCOTS_Blocks.initBlockEntities(); TCOTS_Registries.BLOCK_ENTITY_TYPES.register();

        //Effects
        TCOTS_Effects.initEffects(); TCOTS_Registries.MOB_EFFECTS.register();

        //Entities
        TCOTS_Entities.initEntities(); TCOTS_Registries.ENTITY_TYPES.register();

        //Items Materials
        TCOTS_ItemsMaterials.Armor.init(); TCOTS_Registries.ARMOR_MATERIALS.register();

        //Items
        TCOTS_Items.initAllItems(); TCOTS_Registries.ITEMS.register(); TCOTS_Registries.LOOT_FUNCTIONS.register();
        TCOTS_Items.initGroupItems(); TCOTS_Registries.CREATIVE_MODE_TABS.register();

        TCOTS_MapIcons.init(); TCOTS_Registries.MAP_ICONS.register();

        TCOTS_ScreenHandlersAndRecipes.initScreenHandlersAndRecipes(); TCOTS_Registries.MENU.register(); TCOTS_Registries.RECIPE_TYPE.register(); TCOTS_Registries.RECIPE_SERIALIZER.register();

        TCOTS_Sounds.initSounds(); TCOTS_Registries.SOUND_EVENTS.register();

        TCOTS_Particles.initParticles(); TCOTS_Registries.PARTICLES.register();
        TCOTS_Criteria.initCriteria(); TCOTS_Registries.CRITERIA.register();

        TCOTS_WorldGen.initFeatures(); TCOTS_Registries.FEATURE.register();

        TCOTS_WorldGen.initVegetation();

        TCOTS_Villagers.registerVillagers(); TCOTS_Registries.VILLAGER_PROFESSIONS.register(); TCOTS_Registries.POI_TYPES.register();

        //OwO Networking receivers
        {
            //Witcher Eyes
            {
                //Receive the packet from the client
                TCOTS_Main.PACKETS_CHANNEL.registerServerbound(TCOTS_Main.WitcherEyesFullPacket.class, ((message, access) ->
                {
                    final ServerPlayer player = access.player();

                    player.tcots$setWitcherEyesActivated(message.activate());

                    player.tcots$setEyeShape(message.shape());

                    player.tcots$setEyeSeparation(message.separation());

                    player.tcots$getEyesPivot().setComponent(0, message.eyePosX());

                    player.tcots$getEyesPivot().setComponent(1, -1*message.eyePosY());

                    player.tcots$setEyeMoves(message.moves());

                    //Send another packet to the client, for full sync ->
                    TCOTS_Main.PACKETS_CHANNEL.serverHandle(player).send(new TCOTS_Main.WitcherEyesFullPacket(
                            message.activate(), message.shape(), message.separation(), message.eyePosX(), message.eyePosY(), message.moves()));
                }));


                //Receive the packet from the server
                TCOTS_Main.PACKETS_CHANNEL.registerClientbound(TCOTS_Main.WitcherEyesFullPacket.class, ((message, access) ->
                {
                    final LocalPlayer player = access.player();

                    player.tcots$setWitcherEyesActivated(message.activate());

                    player.tcots$setEyeShape(message.shape());

                    player.tcots$setEyeSeparation(message.separation());

                    player.tcots$getEyesPivot().setComponent(0, message.eyePosX());

                    player.tcots$getEyesPivot().setComponent(1, -1*message.eyePosY());

                    player.tcots$setEyeMoves(message.moves());

                }));
            }

            //Toxicity Face
            {

                //Receive the packet from the client
                TCOTS_Main.PACKETS_CHANNEL.registerServerbound(TCOTS_Main.ToxicityFacePacket.class, ((message, access) ->
                {
                    final ServerPlayer player = access.player();

                    player.tcots$setToxicityActivated(message.activate());


                    //Send another packet to the client, for full sync ->
                    TCOTS_Main.PACKETS_CHANNEL.serverHandle(player).send(new TCOTS_Main.ToxicityFacePacket(message.activate()));
                }));


                //Receive the packet from the server
                TCOTS_Main.PACKETS_CHANNEL.registerClientbound(TCOTS_Main.ToxicityFacePacket.class, ((message, access) ->
                {
                    final LocalPlayer player = access.player();

                    player.tcots$setToxicityActivated(message.activate());

                }));
            }

            //Anchor
            {
                TCOTS_Main.PACKETS_CHANNEL.registerServerbound(TCOTS_Main.RetrieveAnchorPacket.class, ((message, access) ->
                        GiantAnchorItem.retrieveAnchor(access.player())));
            }
        }


    }

    public record WitcherEyesFullPacket(Boolean activate, int shape, int separation, float eyePosX, float eyePosY, boolean moves) {
        public Boolean activate(){return this.activate;}
        public int shape(){return this.shape;}
        public int separation(){return this.separation;}
        public float eyePosX(){return this.eyePosX;}
        public float eyePosY(){return this.eyePosY;}
        public boolean moves(){return this.moves;}
    }

    public record RetrieveAnchorPacket() {}

    public record ToxicityFacePacket(Boolean activate){
        public Boolean activate(){return this.activate;}
    }

    public static ResourceLocation id(final String id){
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id);
    }

    public static ResourceLocation id_WitcherRPG(final String id){
        return ResourceLocation.fromNamespaceAndPath("witcher_rpg", id);
    }

    public static ResourceLocation id_RPG_Series(final String id){
        return ResourceLocation.fromNamespaceAndPath("rpg_series", id);
    }

    public static ResourceLocation id_Spell_Engine(final String id){
        return ResourceLocation.fromNamespaceAndPath("spell_engine", id);
    }
}
