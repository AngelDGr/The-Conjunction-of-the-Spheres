package TCOTS.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;

public class AttackOwnerEnemyTarget extends TrackTargetGoal {
    private final MobEntity ownable;
    private LivingEntity attacking;
    private int lastAttackTime;
    public AttackOwnerEnemyTarget(MobEntity ownable) {
        super(ownable, false);
        this.ownable = ownable;
        this.setControls(EnumSet.of(Goal.Control.TARGET));
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = (LivingEntity) ((Ownable)(this.ownable)).getOwner();
        if (livingEntity == null) {
            return false;
        }
        this.attacking = livingEntity.getAttacking();
        int i = livingEntity.getLastAttackTime();
        return i != this.lastAttackTime && this.canTrack(this.attacking, TargetPredicate.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacking);
        LivingEntity livingEntity = (LivingEntity) ((Ownable)(this.ownable)).getOwner();
        if (livingEntity != null) {
            this.lastAttackTime = livingEntity.getLastAttackTime();
        }
        super.start();
    }
}
