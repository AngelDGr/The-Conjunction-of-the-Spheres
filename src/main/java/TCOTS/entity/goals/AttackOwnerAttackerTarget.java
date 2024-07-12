package TCOTS.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;

import java.util.EnumSet;

public class AttackOwnerAttackerTarget extends TrackTargetGoal {
    private final MobEntity ownable;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public AttackOwnerAttackerTarget(MobEntity ownable) {
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
        this.attacker = livingEntity.getAttacker();
        int i = livingEntity.getLastAttackedTime();
        return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT);
    }

    @Override
    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity livingEntity = (LivingEntity) ((Ownable)(this.ownable)).getOwner();
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.getLastAttackedTime();
        }
        super.start();
    }
}
