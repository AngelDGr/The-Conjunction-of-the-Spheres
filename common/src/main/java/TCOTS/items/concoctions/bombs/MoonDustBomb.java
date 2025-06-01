package TCOTS.items.concoctions.bombs;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.registry.TCOTS_Criteria;
import TCOTS.registry.TCOTS_Particles;
import TCOTS.registry.TCOTS_Effects;
import TCOTS.utils.BombsUtil;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;

public class MoonDustBomb {
    private static final byte MOON_DUST_EXPLODES = 41;

    public static void explosionLogic(WitcherBombEntity bomb){

        bomb.playSound(SoundEvents.GENERIC_EXPLODE.value(), 1,1);

        bomb.level().broadcastEntityEvent(bomb, MOON_DUST_EXPLODES);

        List<LivingEntity> list = bomb.level().getEntitiesOfClass(LivingEntity.class, bomb.getBoundingBox().inflate(3+(bomb.getLevel()*2),2,3+(bomb.getLevel()*2)),
                entity ->
                        !(entity instanceof ArmorStand)
                                && entity.isAlive()
                                && entity != bomb.getOwner());


        Entity entityCause = bomb.getEffectSource();
        for(LivingEntity entity: list){
            //To not apply effect across walls
            if(BombsUtil.getExposure(entity.position(), bomb) == 0) continue;

            //Applies moon dust effect to entity
            entity.addEffect(new MobEffectInstance(TCOTS_Effects.MoonDustEffect(), bomb.getLevel() < 2 ? 200 : 400, bomb.getLevel()), entityCause);
            //Gives you the advancement
            if(entity.getType() == EntityType.CREEPER && bomb.getLevel()>1 && bomb.getEffectSource() instanceof Player player){
                if(player instanceof ServerPlayer serverPlayer){
                    TCOTS_Criteria.StopCreeper().trigger(serverPlayer);
                }
            }
        }
    }

    public static void handleStatus(WitcherBombEntity bomb, byte status) {
        if(status== MOON_DUST_EXPLODES){
            bomb.level().addParticle(TCOTS_Particles.MoonDustExplosionEmitter(), bomb.getX(), bomb.getY(), bomb.getZ(), 0.0, 0.0, 0.0);
        }
    }

    public static boolean checkEffectAndSplinters(LivingEntity entity){
        return MoonDustBomb.checkOnlyEffect(entity) || entity.theConjunctionOfTheSpheres$hasSilverSplinters();
    }

    public static boolean checkOnlyEffect(LivingEntity entity){
        return entity.hasEffect(TCOTS_Effects.MoonDustEffect());
    }

    public static boolean checkSilverSplinters(LivingEntity entity){
        return entity.theConjunctionOfTheSpheres$hasSilverSplinters();
    }


    public static void checkEffectAndSplintersMixin(LivingEntity entity, CallbackInfoReturnable<Boolean> cir){
        if(MoonDustBomb.checkEffectAndSplinters(entity))
            cir.setReturnValue(false);
    }


}
