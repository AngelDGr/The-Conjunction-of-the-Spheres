package TCOTS.entity.misc;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.List;

public class DragonsDreamCloud extends AreaEffectCloudEntity {
    //xTODO: Make it explode with flaming arrows and flaming entities
    public DragonsDreamCloud(EntityType<? extends AreaEffectCloudEntity> entityType, World world) {
        super(entityType, world);
    }

    private int level;

    public DragonsDreamCloud(World world, double x, double y, double z, int level) {
        this(EntityType.AREA_EFFECT_CLOUD, world);
        this.setPosition(x, y, z);
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    //To explode in contact with igniting blocks
    @Override
    protected void onBlockCollision(BlockState state) {
        super.onBlockCollision(state);
        if(state.isIn(TCOTS_Blocks.IGNITING_BLOCKS) || CampfireBlock.isLitCampfire(state)){
            createExplosion();
        }
    }

    @Override
    public void tick() {
        super.tick();
        checkBlockCollision();
        //To explode in contact with entities on fire or Igniting Entities
        List<Entity> entitiesList = this.getWorld().getNonSpectatingEntities(Entity.class, this.getBoundingBox());
        for(Entity entity: entitiesList){
            if(entity.isOnFire() || entity.getType().isIn(TCOTS_Entities.IGNITING_ENTITIES)){

                if(entity.isOnFire() && this.getOwner()!=null && this.getOwner() instanceof PlayerEntity && entity instanceof LivingEntity){
                    if(this.getOwner() instanceof ServerPlayerEntity serverPlayer){
                        TCOTS_Criteria.DRAGONS_DREAM_BURNING.trigger(serverPlayer);
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
    public EntityDimensions getDimensions(EntityPose pose) {
        return EntityDimensions.changing(this.getRadius() * 2.0f, 1.5f);
    }

    private void createExplosion(){
        this.getWorld().createExplosion(
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
                World.ExplosionSourceType.BLOCK
        );
        this.discard();
    }

}
