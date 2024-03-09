package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class ReturnToGroundGoal_Excavator extends Goal {
    private final ExcavatorMob excavatorMob;

    private final PathAwareEntity mob;
    int ticks=35;
    private final boolean generatesPuddle;

    public ReturnToGroundGoal_Excavator(PathAwareEntity mob) {
        this(mob, false);
    }

    public ReturnToGroundGoal_Excavator(PathAwareEntity mob, boolean generatesPuddle) {
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("ReturnToGroundGoal requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.mob=mob;
        this.generatesPuddle=generatesPuddle;
    }
    @Override
    public boolean canStart() {
        return excavatorMob.getInGroundDataTracker();
    }
    @Override
    public boolean shouldContinue(){
        return excavatorMob.getInGroundDataTracker();
    }
    @Override
    public void start(){
        ticks=35;
        if(generatesPuddle) {
            if (!excavatorMob.getSpawnedPuddleDataTracker() && (!mob.isTouchingWater() && !mob.isSubmergedInWater()) && !mob.getSteppingBlockState().isOf(Blocks.MUD)) {
                spawnPuddle(mob.getWorld(), mob);
            }
        }
        mob.playSound(excavatorMob.getDiggingSound(),1.0F,1.0F);
        mob.getNavigation().stop();
        mob.getLookControl().lookAt(0,0,0);
    }


    public void spawnPuddle(World world, LivingEntity entity){

        excavatorMob.setPuddle(new DrownerPuddleEntity(world, entity.getX(), entity.getY(), entity.getZ(), mob));
        if (!world.isClient) {
            world.spawnEntity(excavatorMob.getPuddle());
            excavatorMob.getPuddle().setSpawnControlDataTracker(true);
            excavatorMob.setSpawnedPuddleDataTracker(true);
        }

    }
}
