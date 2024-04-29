package TCOTS.particles;

import TCOTS.TCOTS_Main;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Particles {

    public static final DefaultParticleType ROTFIEND_BLOOD_EXPLOSION = FabricParticleTypes.simple();
    public static final DefaultParticleType ROTFIEND_BLOOD_EMITTER = FabricParticleTypes.simple();
    public static final DefaultParticleType GRAVE_HAG_GREEN_SALIVA = FabricParticleTypes.simple();
    public static final DefaultParticleType GRAPESHOT_EXPLOSION_EMITTER = FabricParticleTypes.simple();


    public static final DefaultParticleType FOGLET_FOG = FabricParticleTypes.simple();
    public static final DefaultParticleType FOGLET_FOG_AROUND = FabricParticleTypes.simple();

    public static void registerParticles() {

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "rotfiend_blood_explosion"),
                ROTFIEND_BLOOD_EXPLOSION);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "rotfiend_blood_emitter"),
                ROTFIEND_BLOOD_EMITTER);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "green_saliva"),
                GRAVE_HAG_GREEN_SALIVA);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "fog"),
                FOGLET_FOG);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "fog_around"),
                FOGLET_FOG_AROUND);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "grapeshot_explosion_emitter"),
                GRAPESHOT_EXPLOSION_EMITTER);
    }

}
