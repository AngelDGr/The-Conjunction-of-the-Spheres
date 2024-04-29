package TCOTS.entity.misc;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import TCOTS.particles.TCOTS_Particles;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.*;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class WitcherBombEntity extends ThrownItemEntity implements FlyingItemEntity {

    //TODO: Add Grapeshot
    //TODO: Add Dancing Star
    //TODO: Add Devil’s Puffball
    //TODO: Add Samum
    //TODO: Add Northern Wind
    //TODO: Add Dragon's Dream
    //TODO: Add Dimeritium Bomb
    //TODO: Add Moon Dust

    private String bombId;
    private int level;

    //BombID
        //0 = Grapeshot

    public WitcherBombEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);

    }

    public WitcherBombEntity(World world, LivingEntity owner, String bombId, int level) {
        super(TCOTS_Entities.WITCHER_BOMB, owner, world);
        this.bombId=bombId;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    protected Item getDefaultItem() {
        return TCOTS_Items.GRAPESHOT;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if(!this.getWorld().isClient){
            switch (bombId){
                case "grapeshot":
                    this.grapeShotBehavior(entity);

                case "dancingStar":

                default:
            }
        }

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            switch (bombId){
                case "grapeshot":
                    this.grapeShotBehavior(null);

                case "dancingStar":

                default:
            }

            this.discard();
        }
    }

    private void grapeShotBehavior(@Nullable Entity entity){
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
                1.25f+(getLevel()*0.25f),
                false,
                World.ExplosionSourceType.BLOCK,
                TCOTS_Particles.GRAPESHOT_EXPLOSION_EMITTER,
                TCOTS_Particles.GRAPESHOT_EXPLOSION_EMITTER,
                SoundEvents.ENTITY_GENERIC_EXPLODE
                );

        if(entity!=null){
            //Level 0 -> 5s
            //Level 1 -> 8s
            //Level 2 -> 11s
            entity.setOnFireFor(5+(getLevel()*3));
        }
    }

    @Override
    public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancingStar") || Objects.equals(bombId, "samum")  ){
            return state.getBlock() == TCOTS_Blocks.MONSTER_NEST
                    || (state.getBlock() == TCOTS_Blocks.NEST_SLAB && !(state == TCOTS_Blocks.NEST_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE)))
                    || state.getBlock() == TCOTS_Blocks.NEST_SKULL || state.getBlock() == TCOTS_Blocks.NEST_WALL_SKULL
                    || state.getBlock() == Blocks.SPAWNER;
        }
        return false;
    }
}
