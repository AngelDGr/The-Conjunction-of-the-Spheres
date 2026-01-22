package mors.tcots.entity.goal;

import mors.tcots.items.concoctions.bombs.DimeritiumBomb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class FleeWithDimeritium<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public FleeWithDimeritium(final PathfinderMob mob, final Class<T> fleeFromType, final float distance, final double slowSpeed, final double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !DimeritiumBomb.checkEffect(this.mob);
    }
}
