package TCOTS.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class FleeWithSilverSplinters<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public FleeWithSilverSplinters(final PathfinderMob mob, final Class<T> fleeFromType, final float distance, final double slowSpeed, final double fastSpeed) {
        super(mob, fleeFromType, distance, slowSpeed, fastSpeed);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.theConjunctionOfTheSpheres$hasSilverSplinters();
    }
}
