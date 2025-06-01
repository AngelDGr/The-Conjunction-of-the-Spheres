package TCOTS.entity.misc;

import TCOTS.registry.TCOTS_Blocks;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.items.concoctions.bombs.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class WitcherBombEntity extends ThrowableItemProjectile implements ItemSupplier {

    //xTODO: Add Grapeshot
    //xTODO: Add Dancing Star
    //xTODO: Add Devil’s Puffball
    //xTODO: Add Samum
    //xTODO: Add Northern Wind
    //xTODO: Add Dragon's Dream
    //xTODO: Add Dimeritium Bomb
    //xTODO: Add Moon Dust

    private String bombId;
    private int level;

    public WitcherBombEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    public WitcherBombEntity(Level world, LivingEntity owner, String bombId, int level) {
        super(TCOTS_Entities.WitcherBomb(), owner, world);
        this.bombId=bombId;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return TCOTS_Items.GRAPESHOT.get();
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        super.onHit(hitResult);
        if (this.level().isClientSide) {
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

                case "moon_dust":
                    MoonDustBomb.explosionLogic(this);
                    break;

                default:
                    break;
            }

            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte status) {
        super.handleEntityEvent(status);
        DancingStarBomb.handleStatus(this,status);
        DevilsPuffballBomb.handleStatus(this,status);
        SamumBomb.handleStatus(this,status);
        NorthernWindBomb.handleStatus(this,status);
        DragonsDreamBomb.handleStatus(this,status);
        DimeritiumBomb.handleStatus(this,status);
        MoonDustBomb.handleStatus(this, status);
    }

    @Override
    public boolean shouldBlockExplode(@NotNull Explosion explosion, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull BlockState state, float explosionPower) {
        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancing_star") || Objects.equals(bombId, "samum")  ){
            return destroyableBlocks(state);
        }

        return false;
    }

    public boolean destroyableBlocks(@NotNull BlockState state){
        return
                    state.getBlock() == TCOTS_Blocks.MonsterNest()
                || (state.getBlock() == TCOTS_Blocks.NestSlab() && !(state == TCOTS_Blocks.NestSlab().defaultBlockState().setValue(SlabBlock.TYPE, SlabType.DOUBLE)))
                || (state.getBlock() == TCOTS_Blocks.NestSkull() || state.getBlock() == TCOTS_Blocks.NestWallSkull())
                ||  state.getBlock() == Blocks.SPAWNER;
    }
}
