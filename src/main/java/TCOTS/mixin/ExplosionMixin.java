package TCOTS.mixin;

import TCOTS.entity.misc.RotfiendExplosionBehavior;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;
import java.util.Objects;

//@Environment(EnvType.CLIENT)
@Debug(export = true) // Enables exporting for the targets of this mixin
@Mixin(Explosion.class)
public abstract class ExplosionMixin {

//    @Accessor("entity")
//    public abstract void setparticle(ParticleEffect particle);
//

    @Shadow @Final private @Nullable Entity entity;
    @Shadow @Final private ExplosionBehavior behavior;

    @Shadow public abstract List<BlockPos> getAffectedBlocks();

//    //Lnet/minecraft/world/explosion/Explosion;affectWorld
//    //Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V

    @Unique
    private boolean conditionRotfiend(){
        BlockPos check = new BlockPos(0,0,0);
        if(this.behavior.getClass() == RotfiendExplosionBehavior.class)
        {
            return true;
        };

        if(this.getAffectedBlocks().size()==5) {
            return Objects.equals(this.getAffectedBlocks().get(0), check) &&
                    Objects.equals(this.getAffectedBlocks().get(1), check) &&
                    Objects.equals(this.getAffectedBlocks().get(2), check) &&
                    Objects.equals(this.getAffectedBlocks().get(3), check) &&
                    Objects.equals(this.getAffectedBlocks().get(4), check);
        }
            return false;
    }
    @ModifyArgs(method = "affectWorld", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    public void injectRotfiendSound(Args args) {

//        System.out.println(this.behavior);
        if(conditionRotfiend()){
//        System.out.println("TriggeredMixin");
        args.set(3, TCOTS_Sounds.ROTFIEND_BLOOD_EXPLOSION);
//        args.set(4, SoundCategory.HOSTILE);
        }
        else{
            args.set(3, SoundEvents.ENTITY_GENERIC_EXPLODE);
//            args.set(4,  SoundCategory.BLOCKS);
        }
    }
//
    @ModifyArgs(method = "affectWorld", at =@At(value = "INVOKE",
    target= "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private void injectRotfiendParticles1(Args args){
        if(conditionRotfiend()){
        args.set(0, TCOTS_Particles.ROTFIEND_BLOOD_EXPLOSION);}
        else{
            args.set(0, ParticleTypes.EXPLOSION);
        }
    }

    @ModifyArgs(method = "affectWorld", at =@At(value = "INVOKE",
            target= "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 1))
    private void injectRotfiendParticles2(Args args){
        if(conditionRotfiend()){
            args.set(0, TCOTS_Particles.ROTFIEND_BLOOD_EMITTER);}
        else{
            args.set(0, ParticleTypes.EXPLOSION_EMITTER);
        }
    }
}
