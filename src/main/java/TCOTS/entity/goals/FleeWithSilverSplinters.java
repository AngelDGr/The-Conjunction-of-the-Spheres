package TCOTS.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class FleeWithSilverSplinters<T extends LivingEntity> extends FleeEntityGoal<T> {
    public FleeWithSilverSplinters(PathAwareEntity mob, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canStart() {
        return super.canStart() && mob.theConjunctionOfTheSpheres$hasSilverSplinters();
    }
}
