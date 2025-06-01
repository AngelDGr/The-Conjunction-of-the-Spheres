package TCOTS.entity.misc;

import TCOTS.TCOTS_Tags;
import TCOTS.registry.TCOTS_Criteria;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DragonsDreamCloud extends AreaEffectCloud {
    //xTODO: Make it explode with flaming arrows and flaming entities
    public DragonsDreamCloud(EntityType<? extends AreaEffectCloud> entityType, Level world) {
        super(entityType, world);
    }

    private int level;

    public DragonsDreamCloud(Level world, double x, double y, double z, int level) {
        this(EntityType.AREA_EFFECT_CLOUD, world);
        this.setPos(x, y, z);
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    //To explode in contact with igniting blocks
    @Override
    protected void onInsideBlock(@NotNull BlockState state) {
        super.onInsideBlock(state);
        if(state.is(TCOTS_Tags.IGNITING_BLOCKS) || CampfireBlock.isLitCampfire(state)){
            createExplosion();
        }
    }

    @Override
    public void tick() {
        super.tick();
        checkInsideBlocks();
        //To explode in contact with entities on fire or Igniting Entities
        List<Entity> entitiesList = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox());
        for(Entity entity: entitiesList){
            if(entity.isOnFire() || entity.getType().is(TCOTS_Tags.IGNITING_ENTITIES)){

                if(entity.isOnFire() && this.getOwner()!=null && this.getOwner() instanceof Player && entity instanceof LivingEntity){
                    if(this.getOwner() instanceof ServerPlayer serverPlayer){
                        TCOTS_Criteria.DragonsDreamBurning().trigger(serverPlayer);
                    }
                }

                createExplosion();
            }
        }
        //To explode in contact with lava
        if(this.isInLava()){
            createExplosion();
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0f, 1.5f);
    }

    private void createExplosion(){
        this.level().explode(
                this,
                null,
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                //Level 0 -> 1.25
                //Level 1 -> 1.50
                //Level 2 -> 1.75
                2f+(getLevel()*0.5f),
                true,
                Level.ExplosionInteraction.BLOCK,
                ParticleTypes.EXPLOSION_EMITTER,
                ParticleTypes.EXPLOSION_EMITTER,
                SoundEvents.GENERIC_EXPLODE
        );
        this.discard();
    }

}
