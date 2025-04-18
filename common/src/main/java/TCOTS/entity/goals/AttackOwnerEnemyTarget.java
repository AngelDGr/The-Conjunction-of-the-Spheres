package TCOTS.entity.goals;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class AttackOwnerEnemyTarget extends TargetGoal {
    private final Mob ownable;
    private LivingEntity attacking;
    private int lastAttackTime;
    public AttackOwnerEnemyTarget(Mob ownable) {
        super(ownable, false);
        this.ownable = ownable;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = (LivingEntity) ((TraceableEntity)(this.ownable)).getOwner();
        if (livingEntity == null) {
            return false;
        }
        this.attacking = livingEntity.getLastHurtMob();
        int i = livingEntity.getLastHurtMobTimestamp();
        return i != this.lastAttackTime && this.canAttack(this.attacking, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
        LivingEntity livingEntity = (LivingEntity) ((TraceableEntity)(this.ownable)).getOwner();
        if (livingEntity != null) {
            this.lastAttackTime = livingEntity.getLastHurtMobTimestamp();
        }
        super.start();
    }
}
