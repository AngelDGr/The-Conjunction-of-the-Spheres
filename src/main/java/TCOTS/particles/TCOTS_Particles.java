package TCOTS.particles;

import TCOTS.TCOTS_Main;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.fluid.WaterFluid;

public class TCOTS_Particles {
    public static final DefaultParticleType DROWNER_PUDDLE_PARTICLE = FabricParticleTypes.simple();

    public static final DefaultParticleType ROTFIEND_BLOOD_EXPLOSION = FabricParticleTypes.simple();

    public static final DefaultParticleType ROTFIEND_BLOOD_EMITTER = FabricParticleTypes.simple();

    public static void registerParticles() {

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "drowner_puddle_particle"),
                DROWNER_PUDDLE_PARTICLE);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "rotfiend_blood_explosion"),
                ROTFIEND_BLOOD_EXPLOSION);

        Registry.register(Registries.PARTICLE_TYPE, new Identifier(TCOTS_Main.MOD_ID, "rotfiend_blood_emitter"),
                ROTFIEND_BLOOD_EMITTER);
    }

}
