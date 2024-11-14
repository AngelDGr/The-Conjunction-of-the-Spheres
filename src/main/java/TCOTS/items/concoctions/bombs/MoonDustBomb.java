package TCOTS.items.concoctions.bombs;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.utils.BombsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class MoonDustBomb {
    private static final byte MOON_DUST_EXPLODES = 41;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE.value(), 1,1);

        bomb.getWorld().sendEntityStatus(bomb, MOON_DUST_EXPLODES);

        List<LivingEntity> list = bomb.getWorld().getEntitiesByClass(LivingEntity.class, bomb.getBoundingBox().expand(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                entity ->
                        !(entity instanceof WardenEntity) && !(entity instanceof ArmorStandEntity)
                                && entity.isAlive()
                                && entity != bomb.getOwner());


        Entity entityCause = bomb.getEffectCause();
        for(LivingEntity entity: list){
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.getPos(), bomb) == 0) continue;

            //Applies moon dust effect to entity
            entity.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.MOON_DUST_EFFECT, bomb.getLevel() < 1 ? 400 : 800, bomb.getLevel()), entityCause);
            //Gives you the advancement
            if(entity.getType() == EntityType.CREEPER && bomb.getLevel()>1 && bomb.getEffectCause() instanceof PlayerEntity player){
                if(player instanceof ServerPlayerEntity serverPlayer){
                    TCOTS_Criteria.STOP_CREEPER.trigger(serverPlayer);
                }
            }
        }
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== MOON_DUST_EXPLODES){
            bomb.getWorld().addParticle(TCOTS_Particles.MOON_DUST_EXPLOSION_EMITTER, bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static boolean checkEffect(LivingEntity entity){
        return MoonDustBomb.checkOnlyEffect(entity) || entity.theConjunctionOfTheSpheres$hasSilverSplinters();
    }

    public static boolean checkOnlyEffect(LivingEntity entity){
        return entity.hasStatusEffect(TCOTS_Effects.MOON_DUST_EFFECT);
    }

    public static boolean checkSilverSplinters(LivingEntity entity){
        return entity.theConjunctionOfTheSpheres$hasSilverSplinters();
    }

    public static void checkEffectMixin(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if(MoonDustBomb.checkEffect(entity))
            cir.setReturnValue(false);
    }

}
