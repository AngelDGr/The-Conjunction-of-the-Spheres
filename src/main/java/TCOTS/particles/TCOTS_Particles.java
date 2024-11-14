package TCOTS.particles;

import TCOTS.TCOTS_Main;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Particles {

    public static final SimpleParticleType ROTFIEND_BLOOD_EXPLOSION = FabricParticleTypes.simple();
    public static final SimpleParticleType ROTFIEND_BLOOD_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType GRAVE_HAG_GREEN_SALIVA = FabricParticleTypes.simple();
    public static final SimpleParticleType GRAPESHOT_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType DANCING_STAR_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType DEVILS_PUFFBALL_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType GREEN_CLOUD = FabricParticleTypes.simple();
    public static final SimpleParticleType SAMUM_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType NORTHERN_WIND_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType YELLOW_CLOUD = FabricParticleTypes.simple();
    public static final SimpleParticleType DRAGONS_DREAM_EXPLOSION_EMITTER = FabricParticleTypes.simple();
    public static final SimpleParticleType DIMERITIUM_FLASH = FabricParticleTypes.simple();
    public static final SimpleParticleType MOON_DUST_EXPLOSION_EMITTER = FabricParticleTypes.simple();

    public static final SimpleParticleType FOGLET_FOG = FabricParticleTypes.simple();
    public static final SimpleParticleType FOGLET_FOG_AROUND = FabricParticleTypes.simple();

    public static final SimpleParticleType FALLING_BLOOD_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType LANDING_BLOOD_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType FALLING_BLACK_BLOOD_PARTICLE = FabricParticleTypes.simple();
    public static final SimpleParticleType LANDING_BLACK_BLOOD_PARTICLE = FabricParticleTypes.simple();

    public static void registerParticles() {

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "rotfiend_blood_explosion"),
                ROTFIEND_BLOOD_EXPLOSION);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "rotfiend_blood_emitter"),
                ROTFIEND_BLOOD_EMITTER);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "green_saliva"),
                GRAVE_HAG_GREEN_SALIVA);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "fog"),
                FOGLET_FOG);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "fog_around"),
                FOGLET_FOG_AROUND);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "grapeshot_explosion_emitter"),
                GRAPESHOT_EXPLOSION_EMITTER);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "dancing_explosion_emitter"),
                DANCING_STAR_EXPLOSION_EMITTER);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "devils_explosion_emitter"),
                DEVILS_PUFFBALL_EXPLOSION_EMITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "green_cloud"),
                GREEN_CLOUD);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "samum_explosion_emitter"),
                SAMUM_EXPLOSION_EMITTER);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "northern_explosion_emitter"),
                NORTHERN_WIND_EXPLOSION_EMITTER);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "dragons_dream_emitter"),
                DRAGONS_DREAM_EXPLOSION_EMITTER);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "yellow_cloud"),
                YELLOW_CLOUD);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "dimeritium_flash"),
                DIMERITIUM_FLASH);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "moon_dust_emitter"),
                MOON_DUST_EXPLOSION_EMITTER);


        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "falling_blood"),
                FALLING_BLOOD_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "landing_blood"),
                LANDING_BLOOD_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "falling_black_blood"),
                FALLING_BLACK_BLOOD_PARTICLE);
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "landing_black_blood"),
                LANDING_BLACK_BLOOD_PARTICLE);
    }

}
