package TCOTS.registry;

import TCOTS.TCOTS_Registries;
import net.minecraft.core.particles.SimpleParticleType;

public class TCOTS_Particles {

    private static final SimpleParticleType ROTFIEND_BLOOD_EXPLOSION = new SimpleParticleType(false){};
    private static final SimpleParticleType ROTFIEND_BLOOD_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType GRAVE_HAG_GREEN_SALIVA = new SimpleParticleType(false){};
    private static final SimpleParticleType GRAPESHOT_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType DANCING_STAR_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType DEVILS_PUFFBALL_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType GREEN_CLOUD = new SimpleParticleType(false){};
    private static final SimpleParticleType SAMUM_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType NORTHERN_WIND_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType YELLOW_CLOUD = new SimpleParticleType(false){};
    private static final SimpleParticleType DRAGONS_DREAM_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType DIMERITIUM_FLASH = new SimpleParticleType(false){};
    private static final SimpleParticleType MOON_DUST_EXPLOSION_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType FOGLET_FOG = new SimpleParticleType(false){};
    private static final SimpleParticleType FOGLET_FOG_AROUND = new SimpleParticleType(false){};
    private static final SimpleParticleType FALLING_BLOOD_PARTICLE = new SimpleParticleType(false){};
    private static final SimpleParticleType LANDING_BLOOD_PARTICLE = new SimpleParticleType(false){};
    private static final SimpleParticleType FALLING_BLACK_BLOOD_PARTICLE = new SimpleParticleType(false){};
    private static final SimpleParticleType LANDING_BLACK_BLOOD_PARTICLE = new SimpleParticleType(false){};
    private static final SimpleParticleType BLOEDZUIGER_BLOOD_EMITTER = new SimpleParticleType(false){};
    private static final SimpleParticleType CADAVERINE_CLOUD = new SimpleParticleType(false){};

    public static void initParticles() {
        TCOTS_Registries.PARTICLES.register("rotfiend_blood_explosion",
                ()-> ROTFIEND_BLOOD_EXPLOSION);

        TCOTS_Registries.PARTICLES.register("rotfiend_blood_emitter",
                ()-> ROTFIEND_BLOOD_EMITTER);

        TCOTS_Registries.PARTICLES.register("green_saliva",
                ()-> GRAVE_HAG_GREEN_SALIVA);

        TCOTS_Registries.PARTICLES.register("fog",
                ()-> FOGLET_FOG);

        TCOTS_Registries.PARTICLES.register("fog_around",
                ()-> FOGLET_FOG_AROUND);

        TCOTS_Registries.PARTICLES.register("grapeshot_explosion_emitter",
                ()-> GRAPESHOT_EXPLOSION_EMITTER);

        TCOTS_Registries.PARTICLES.register("dancing_explosion_emitter",
                ()-> DANCING_STAR_EXPLOSION_EMITTER);

        TCOTS_Registries.PARTICLES.register("devils_explosion_emitter",
                ()-> DEVILS_PUFFBALL_EXPLOSION_EMITTER);
        TCOTS_Registries.PARTICLES.register("green_cloud",
                ()-> GREEN_CLOUD);

        TCOTS_Registries.PARTICLES.register("samum_explosion_emitter",
                ()-> SAMUM_EXPLOSION_EMITTER);

        TCOTS_Registries.PARTICLES.register("northern_explosion_emitter",
                ()-> NORTHERN_WIND_EXPLOSION_EMITTER);

        TCOTS_Registries.PARTICLES.register("dragons_dream_emitter",
                ()-> DRAGONS_DREAM_EXPLOSION_EMITTER);
        TCOTS_Registries.PARTICLES.register("yellow_cloud",
                ()-> YELLOW_CLOUD);

        TCOTS_Registries.PARTICLES.register("dimeritium_flash",
                ()-> DIMERITIUM_FLASH);

        TCOTS_Registries.PARTICLES.register("moon_dust_emitter",
                ()-> MOON_DUST_EXPLOSION_EMITTER);


        TCOTS_Registries.PARTICLES.register("falling_blood",
                ()-> FALLING_BLOOD_PARTICLE);
        TCOTS_Registries.PARTICLES.register("landing_blood",
                ()-> LANDING_BLOOD_PARTICLE);

        TCOTS_Registries.PARTICLES.register("falling_black_blood",
                ()-> FALLING_BLACK_BLOOD_PARTICLE);
        TCOTS_Registries.PARTICLES.register("landing_black_blood",
                ()-> LANDING_BLACK_BLOOD_PARTICLE);

        TCOTS_Registries.PARTICLES.register("bloedzuiger_blood_emitter",
                ()-> BLOEDZUIGER_BLOOD_EMITTER);

        TCOTS_Registries.PARTICLES.register("cadaverine_cloud",
                ()-> CADAVERINE_CLOUD);
    }

    public static SimpleParticleType RotfiendBloodExplosion() {
        return ROTFIEND_BLOOD_EXPLOSION;
    }

    public static SimpleParticleType RotfiendBloodEmitter() {
        return ROTFIEND_BLOOD_EMITTER;
    }

    public static SimpleParticleType GraveHagGreenSaliva() {
        return GRAVE_HAG_GREEN_SALIVA;
    }

    public static SimpleParticleType GrapeshotExplosionEmitter() {
        return GRAPESHOT_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType DancingStarExplosionEmitter() {
        return DANCING_STAR_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType DevilsPuffballExplosionEmitter() {
        return DEVILS_PUFFBALL_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType GreenCloud() {
        return GREEN_CLOUD;
    }

    public static SimpleParticleType SamumExplosionEmitter() {
        return SAMUM_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType NorthernWindExplosionEmitter() {
        return NORTHERN_WIND_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType YellowCloud() {
        return YELLOW_CLOUD;
    }

    public static SimpleParticleType DragonsDreamExplosionEmitter() {
        return DRAGONS_DREAM_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType DimeritiumFlash() {
        return DIMERITIUM_FLASH;
    }

    public static SimpleParticleType MoonDustExplosionEmitter() {
        return MOON_DUST_EXPLOSION_EMITTER;
    }

    public static SimpleParticleType FogletFog() {
        return FOGLET_FOG;
    }

    public static SimpleParticleType FogletFogAround() {
        return FOGLET_FOG_AROUND;
    }

    public static SimpleParticleType FallingBloodParticle() {
        return FALLING_BLOOD_PARTICLE;
    }

    public static SimpleParticleType LandingBloodParticle() {
        return LANDING_BLOOD_PARTICLE;
    }

    public static SimpleParticleType FallingBlackBloodParticle() {
        return FALLING_BLACK_BLOOD_PARTICLE;
    }

    public static SimpleParticleType LandingBlackBloodParticle() {
        return LANDING_BLACK_BLOOD_PARTICLE;
    }

    public static SimpleParticleType BloedzuigerBloodEmitter() {
        return BLOEDZUIGER_BLOOD_EMITTER;
    }

    public static SimpleParticleType CadaverineCloud() {
        return CADAVERINE_CLOUD;
    }
}
