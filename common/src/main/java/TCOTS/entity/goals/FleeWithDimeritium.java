package TCOTS.entity.goals;

import TCOTS.items.concoctions.bombs.DimeritiumBomb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class FleeWithDimeritium<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public FleeWithDimeritium(PathfinderMob mob, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !DimeritiumBomb.checkEffect(this.mob);
    }
}
