package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemEntity.class)
public abstract class IronGolemMixin extends GolemEntity implements Angerable {
    protected IronGolemMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canTarget", at = @At("HEAD"), cancellable = true)
    private void injectNotAttackTrolls(EntityType<?> type, CallbackInfoReturnable<Boolean> cir){
        if (type == TCOTS_Entities.ROCK_TROLL) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "tryAttack", at = @At("TAIL"))
    private void injectOnAttacking(Entity target, CallbackInfoReturnable<Boolean> cir){
        this.onAttacking(target);
    }

}

