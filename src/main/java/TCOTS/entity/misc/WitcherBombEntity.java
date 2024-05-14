package TCOTS.entity.misc;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.potions.bombs.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.Objects;

public class WitcherBombEntity extends ThrownItemEntity implements FlyingItemEntity {

    //xTODO: Add Grapeshot
    //xTODO: Add Dancing Star
    //xTODO: Add Devil’s Puffball
    //xTODO: Add Samum
    //xTODO: Add Northern Wind
    //xTODO: Add Dragon's Dream
    //TODO: Add Dimeritium Bomb
    //TODO: Add Moon Dust

    private String bombId;
    private int level;

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
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getWorld().isClient) {
            return;
        }

        if (bombId!=null) {
            switch (bombId){
                case "grapeshot":
                    GrapeshotBomb.explosionLogic(this, hitResult.getType() == HitResult.Type.ENTITY ? ((EntityHitResult)hitResult).getEntity() : null);
                    break;

                case "dancing_star":
                    DancingStarBomb.explosionLogic(this);
                    break;

                case "devils_puffball":
                    DevilsPuffballBomb.explosionLogic(this);
                    break;

                case "samum":
                    SamumBomb.explosionLogic(this);
                    break;

                case "northern_wind":
                    NorthernWindBomb.explosionLogic(this);
                    break;

                case "dragons_dream":
                    DragonsDreamBomb.explosionLogic(this);
                    break;

                case "dimeritium_bomb":
                    DimeritiumBomb.explosionLogic(this);
                    break;

                default:
                    break;
            }

            this.discard();
        }
    }

    @Override
    public void handleStatus(byte status) {
        super.handleStatus(status);
        DancingStarBomb.handleStatus(this,status);
        DevilsPuffballBomb.handleStatus(this,status);
        SamumBomb.handleStatus(this,status);
        NorthernWindBomb.handleStatus(this,status);
        DragonsDreamBomb.handleStatus(this,status);
        DimeritiumBomb.handleStatus(this,status);
    }

    @Override
    public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancing_star") || Objects.equals(bombId, "samum")  ){
            return destroyableBlocks(state);
        }

        return false;
    }

    public boolean destroyableBlocks(BlockState state){
        return
                    state.getBlock() == TCOTS_Blocks.MONSTER_NEST
                || (state.getBlock() == TCOTS_Blocks.NEST_SLAB && !(state == TCOTS_Blocks.NEST_SLAB.getDefaultState().with(SlabBlock.TYPE, SlabType.DOUBLE)))
                || (state.getBlock() == TCOTS_Blocks.NEST_SKULL || state.getBlock() == TCOTS_Blocks.NEST_WALL_SKULL)
                ||  state.getBlock() == Blocks.SPAWNER;
    }
}
