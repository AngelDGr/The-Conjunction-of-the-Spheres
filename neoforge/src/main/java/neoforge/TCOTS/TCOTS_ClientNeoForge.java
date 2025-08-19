package neoforge.TCOTS;

import TCOTS.TCOTS_Client;
import TCOTS.TCOTS_Main;
import TCOTS.blocks.geo.renderer.*;
import TCOTS.entity.geo.renderer.AnchorProjectileRenderer;
import TCOTS.entity.geo.renderer.necrophages.*;
import TCOTS.entity.geo.renderer.ogroids.*;
import TCOTS.entity.misc.renderers.*;
import TCOTS.entity.witcher_cosmetics.toxicity_face.ToxicityFaceModel;
import TCOTS.entity.witcher_cosmetics.witcher_eyes.WitcherEyesModel;
import TCOTS.particles.*;
import TCOTS.particles.bombEmitters.*;
import TCOTS.registry.*;
import TCOTS.screen.AlchemyTableScreen;
import TCOTS.screen.HerbalTableScreen;
import TCOTS.screen.ToxicityHudOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.common.NeoForge;

import java.util.function.Supplier;

@EventBusSubscriber(modid = TCOTS_Main.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@Mod(value = TCOTS_Main.MOD_ID, dist = Dist.CLIENT)
public class TCOTS_ClientNeoForge {

    public TCOTS_ClientNeoForge(final IEventBus modEventBus){
        //Send data when join
        NeoForge.EVENT_BUS.addListener((final ClientPlayerNetworkEvent.LoggingIn evt) -> {
            {
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(new TCOTS_Main.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeMoves()
                        )
                );
            }
        });
    }

    @SubscribeEvent
    public static void registerHUDLayers(final RegisterGuiLayersEvent event){
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "toxicity_overlay"),
                (drawContext, tickCounter) -> ToxicityHudOverlay.onHudRender(drawContext, tickCounter.getGameTimeDeltaPartialTick(true)));
    }

    @SubscribeEvent
    public static void registerClientEvent(final FMLClientSetupEvent event){
        TCOTS_Client.initItemPropertiesRegistry();

        //Send client-packets to server
        {
            TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateEyes(activate ->
            {
                if (Minecraft.getInstance().getConnection() == null) return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.WitcherEyesFullPacket(
                                activate,
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeMoves()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeSeparation(eye_separation ->
            {
                if (Minecraft.getInstance().getConnection() == null) return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                eye_separation.ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeMoves()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeShape(eye_shape ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                eye_shape.ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeMoves()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToXEyePos(xEyePos ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                xEyePos,
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeMoves()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToYEyePos(yEyePos ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                yEyePos,
                                TCOTS_Main.CONFIG.witcher_eyes.eyeMoves()));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeMoves(eyeMoves ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.WitcherEyesFullPacket(
                                TCOTS_Main.CONFIG.witcher_eyes.activateEyes(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeShape().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.eyeSeparation().ordinal(),
                                TCOTS_Main.CONFIG.witcher_eyes.XEyePos(),
                                TCOTS_Main.CONFIG.witcher_eyes.YEyePos(),
                                eyeMoves));
            });

            TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivateToxicity(activateToxicity ->
            {
                if(Minecraft.getInstance().getConnection()==null)return;
                TCOTS_Main.PACKETS_CHANNEL.clientHandle().send(
                        new TCOTS_Main.ToxicityFacePacket(activateToxicity));
            });
        }

        registerBlockRenderers();
    }

    @SubscribeEvent
    public static void registerColorBlocks(final RegisterColorHandlersEvent.Block event){
        //Grass Colors
        event.register((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) {
                        return GrassColor.getDefaultColor();
                    }
                    return BiomeColors.getAverageGrassColor(world, pos);}, TCOTS_Blocks.HanFiberPlant(), TCOTS_Blocks.ArenariaBush(),
                TCOTS_Blocks.CelandinePlant(), TCOTS_Blocks.CrowsEyeFern(), TCOTS_Blocks.VerbenaFlower(), TCOTS_Blocks.PottedBryoniaFlower());

        //Leaves Colors
        event.register((state, world, pos, tintIndex) -> {
                    if (world == null || pos == null) {
                        return FoliageColor.getDefaultColor();

                    }
                    return BiomeColors.getAverageGrassColor(world, pos);},
                TCOTS_Blocks.BryoniaVine());
    }

    @SubscribeEvent
    public static void registerColorItems(final RegisterColorHandlersEvent.Item event){
        event.register((stack, tintIndex) -> tintIndex > 0 ? -1: DyedItemColor.getOrDefault(stack, -6265536), TCOTS_Items.KNIGHT_CROSSBOW.get());
    }

    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        //Monsters
        event.registerEntityRenderer(TCOTS_Entities.Drowner(), DrownerRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.DrownerPuddle(), DrownerPuddleRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Rotfiend(), RotfiendRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.GraveHag(), GraveHagRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.WaterHag(), WaterHagRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.WaterHagMudBall(), ThrownItemRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Foglet(), FogletRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.Fogling(), FoglingRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Ghoul(), GhoulRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Alghoul(), AlghoulRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Scurver(), ScurverRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.ScurverSpine(), ScurverSpineRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Devourer(), DevourerRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Bloedzuiger(), BloedzuigerRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Graveir(), GraveirRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Bullvore(), BullvoreRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Nekker(), NekkerRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.NekkerWarrior(), NekkerWarriorRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.Cyclops(), CyclopsRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.RockTroll(), RockTrollRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.TrollRockProjectile(), context -> new ThrownItemRenderer<>(context, 2.0f, true));

        event.registerEntityRenderer(TCOTS_Entities.IceTroll(), IceTrollRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.ForestTroll(), ForestTrollRenderer::new);

        event.registerEntityRenderer(TCOTS_Entities.IceGiant(), IceGiantRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.AnchorProjectile(), AnchorProjectileRenderer::new);

        //Bomb
        event.registerEntityRenderer(TCOTS_Entities.WitcherBomb(), ThrownItemRenderer::new);
        //Crossbow bolts
        event.registerEntityRenderer(TCOTS_Entities.BaseBolt(), BaseBoltEntityRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.BluntBolt(), BluntBoltEntityRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.PrecisionBolt(), PrecisionBoltEntityRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.ExplodingBolt(), ExplodingBoltEntityRenderer::new);
        event.registerEntityRenderer(TCOTS_Entities.BroadheadBolt(), BroadheadBoltEntityRenderer::new);
    }

    @SuppressWarnings("deprecation")
    public static void registerBlockRenderers(){
        //Blocks
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.ArenariaBush(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.CelandinePlant(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.CrowsEyeFern(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.BryoniaVine(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.VerbenaFlower(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.HanFiberPlant(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PuffballMushroom(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.SewantMushroomsPlant(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PottedVerbenaFlower(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PottedCelandineFlower(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PottedHanFiber(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PottedPuffballMushroom(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PottedSewantMushrooms(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.PottedBryoniaFlower(), RenderType.cutout());

        ItemBlockRenderTypes.setRenderLayer(TCOTS_Blocks.FrostedSnow(), RenderType.translucent());

        //BlockEntity
        BlockEntityRenderers.register(TCOTS_Blocks.SkullNestEntity(), NestSkullBlockRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.MonsterNestBlockEntity(), MonsterNestRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.AlchemyTableBlockEntity(), AlchemyTableRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.HerbalTableEntity(), HerbalTableRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.GiantAnchorEntity(), GiantAnchorRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.WintersBladeSkeletonBlockEntity(), WintersBladeSkeletonRenderer::new);
        BlockEntityRenderers.register(TCOTS_Blocks.SkeletonBlockEntity(), SkeletonBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticleProviders(final RegisterParticleProvidersEvent event) {
        // There are multiple ways to register providers, all differing in the functional type they provide in the
        // second parameter. For example, #registerSpriteSet represents a Function<SpriteSet, ParticleProvider<?>>:
        event.registerSpriteSet(TCOTS_Particles.RotfiendBloodExplosion(), Rotfiend_BloodExplosionParticle.Factory::new);

        event.registerSpecial(TCOTS_Particles.RotfiendBloodEmitter(), new Rotfiend_BloodEmitterParticle.Factory());
        event.registerSpriteSet(TCOTS_Particles.GraveHagGreenSaliva(), GraveHag_GreenSaliva.Provider::new);
        event.registerSpriteSet(TCOTS_Particles.FogletFog(), Foglet_FogParticle.FogFactory::new);
        event.registerSpriteSet(TCOTS_Particles.FogletFogAround(), Foglet_FogParticleAround.FogFactory::new);
        event.registerSpecial(TCOTS_Particles.GrapeshotExplosionEmitter(), new Grapeshot_ExplosionEmitterParticle.Factory());
        event.registerSpecial(TCOTS_Particles.DancingStarExplosionEmitter(), new DancingStar_ExplosionEmitterParticle.Factory());
        event.registerSpecial(TCOTS_Particles.DevilsPuffballExplosionEmitter(), new DevilsPuffball_ExplosionEmitterParticle.Factory());
        event.registerSpriteSet(TCOTS_Particles.GreenCloud(), CloudParticleColor.GreenCloudFactory::new);
        event.registerSpecial(TCOTS_Particles.SamumExplosionEmitter(), new Samum_ExplosionEmitterParticle.Factory());
        event.registerSpecial(TCOTS_Particles.NorthernWindExplosionEmitter(), new NorthernWind_ExplosionEmitterParticle.Factory());
        event.registerSpecial(TCOTS_Particles.DragonsDreamExplosionEmitter(), new DragonsDream_ExplosionEmitterParticle.Factory());
        event.registerSpriteSet(TCOTS_Particles.YellowCloud(), CloudParticleColor.YellowCloudFactory::new);
        event.registerSpriteSet(TCOTS_Particles.DimeritiumFlash(), DimeritiumFlash.FlashFactory::new);
        event.registerSpecial(TCOTS_Particles.MoonDustExplosionEmitter(), new MoonDust_ExplosionEmitterParticle.Factory());

        event.registerSpecial(TCOTS_Particles.BloedzuigerBloodEmitter(), new Bloedzuiger_BloodEmitterParticle.Factory());

        event.registerSpriteSet(TCOTS_Particles.CadaverineCloud(), CloudParticleColor.CadaverineCloudFactory::new);

        event.registerSpriteSet(TCOTS_Particles.FallingBloodParticle(),
                (spriteProvider) ->
                        (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                new BloodParticle.Factory(spriteProvider,
                                        BloodParticle.createFallingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ, TCOTS_Particles.LandingBloodParticle()))
                                        .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );
        event.registerSpriteSet(TCOTS_Particles.LandingBloodParticle(),
                (spriteProvider) ->
                        (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                new BloodParticle.Factory(spriteProvider,
                                        BloodParticle.createLandingBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                        .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );


        event.registerSpriteSet(TCOTS_Particles.FallingBlackBloodParticle(),
                (spriteProvider) ->
                        (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                                new BloodParticle.Factory(spriteProvider,
                                        BloodParticle.createFallingBlackBlood(type, world, x, y, z, velocityX, velocityY, velocityZ, TCOTS_Particles.LandingBlackBloodParticle()))
                                        .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );
        event.registerSpriteSet(TCOTS_Particles.LandingBlackBloodParticle(),                 (spriteProvider) ->
                (type, world, x, y, z, velocityX, velocityY, velocityZ) ->
                        new BloodParticle.Factory(spriteProvider,
                                BloodParticle.createLandingBlackBlood(type, world, x, y, z, velocityX, velocityY, velocityZ))
                                .createParticle(type, world, x, y, z, velocityX, velocityY, velocityZ)
        );

        // Other methods include #registerSprite, which is essentially a Supplier<TextureSheetParticle>,
        // and #registerSpecial, which maps to a Supplier<Particle>. See the source code of the event for further info.
    }

    @SubscribeEvent
    public static void registerMenuScreen(final RegisterMenuScreensEvent event){
        event.register(TCOTS_ScreenHandlersAndRecipes.AlchemyTableScreenHandler(), AlchemyTableScreen::new);
        event.register(TCOTS_ScreenHandlersAndRecipes.HerbalTableScreenHandler(), HerbalTableScreen::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TCOTS_Client.WITCHER_EYES_LAYER, WitcherEyesModel_createModelData());
        event.registerLayerDefinition(TCOTS_Client.TOXICITY_FACE_LAYER, ToxicityFaceModel_createModelData());
    }

    public static Supplier<LayerDefinition> WitcherEyesModel_createModelData(){
        return () -> LayerDefinition.create(
                WitcherEyesModel.getModelData(new CubeDeformation(0)),
                96, 64);
    }

    public static Supplier<LayerDefinition> ToxicityFaceModel_createModelData(){
        return () -> LayerDefinition.create(
                ToxicityFaceModel.getModelData(new CubeDeformation(0)),
                96, 64);
    }

}
