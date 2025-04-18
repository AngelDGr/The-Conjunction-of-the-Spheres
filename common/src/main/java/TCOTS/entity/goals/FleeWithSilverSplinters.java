package TCOTS.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class FleeWithSilverSplinters<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public FleeWithSilverSplinters(PathfinderMob mob, Class<T> fleeFromType, float distance, double slowSpeed, double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.theConjunctionOfTheSpheres$hasSilverSplinters();
    }
}
