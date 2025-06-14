package TCOTS.entity.goals;

import TCOTS.items.concoctions.bombs.DimeritiumBomb;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class FleeWithDimeritium<T extends LivingEntity> extends FleeEntityGoal<T> {
    public FleeWithDimeritium(PathAwareEntity mob, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && !DimeritiumBomb.checkEffect(this.mob);
    }
}
