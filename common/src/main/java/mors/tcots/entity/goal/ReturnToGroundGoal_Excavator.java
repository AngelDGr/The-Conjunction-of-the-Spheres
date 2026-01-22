package mors.tcots.entity.goal;

import mors.tcots.registry.TCOTS_Entities;
import mors.tcots.entity.interfaces.ExcavatorMob;
import mors.tcots.entity.misc.DrownerPuddleEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ReturnToGroundGoal_Excavator extends Goal {
    private final ExcavatorMob excavatorMob;

    private final PathfinderMob mob;
    int ticks=35;
    private final boolean generatesPuddle;

    public ReturnToGroundGoal_Excavator(final PathfinderMob mob) {
        this(mob, false);
    }

    public ReturnToGroundGoal_Excavator(final PathfinderMob mob, final boolean generatesPuddle) {
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("ReturnToGroundGoal requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.mob=mob;
        this.generatesPuddle=generatesPuddle;
    }
    @Override
    public boolean canUse() {
        return excavatorMob.getInGround();
    }
    @Override
    public boolean canContinueToUse(){
        return excavatorMob.getInGround();
    }
    @Override
    public void start(){
        ticks=35;
        if(generatesPuddle) {
            if (!excavatorMob.getSpawnedPuddleDataTracker() && (!mob.isInWater() && !mob.isUnderWater()) && !mob.getBlockStateOn().is(Blocks.MUD)) {
                spawnPuddle(mob.level(), mob);
            }
        }
        mob.playSound(excavatorMob.getDiggingSound(),1.0F,1.0F);
        mob.getNavigation().stop();
        mob.getLookControl().setLookAt(0,0,0);
    }


    public void spawnPuddle(final Level world, final LivingEntity entity){
        final DrownerPuddleEntity puddle=new DrownerPuddleEntity(TCOTS_Entities.DrownerPuddle(),world, entity.getX(), entity.getY(), entity.getZ(), mob);
        excavatorMob.setPuddle(puddle);
        if (!world.isClientSide) {
            world.addFreshEntity(puddle);
            excavatorMob.setSpawnedPuddleDataTracker(true);
        }

    }
}
