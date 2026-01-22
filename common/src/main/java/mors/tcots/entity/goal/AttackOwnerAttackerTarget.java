package mors.tcots.entity.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class AttackOwnerAttackerTarget extends TargetGoal {
    private final Mob ownable;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public AttackOwnerAttackerTarget(final Mob ownable) {
        super(ownable, false);
        this.ownable = ownable;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        final LivingEntity livingEntity = (LivingEntity) ((TraceableEntity)(this.ownable)).getOwner();
        if (livingEntity == null) {
            return false;
        }
        this.attacker = livingEntity.getLastHurtByMob();
        final int i = livingEntity.getLastHurtByMobTimestamp();
        return i != this.lastAttackedTime && this.canAttack(this.attacker, TargetingConditions.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        final LivingEntity livingEntity = (LivingEntity) ((TraceableEntity)(this.ownable)).getOwner();
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.getLastHurtByMobTimestamp();
        }
        super.start();
    }
}
