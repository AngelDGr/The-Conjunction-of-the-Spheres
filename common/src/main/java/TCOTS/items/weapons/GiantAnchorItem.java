package TCOTS.items.weapons;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Items;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.items.geo.renderer.GiantAnchorItemRenderer;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.ContextAwareAnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class GiantAnchorItem extends TieredItem implements GeoItem {
    //xTODO: Add attacks to the anchor (2)
    //The anchor it's going to return to you with right-click
    //The anchor can be launched
    private final AnimatableInstanceCache cache   = GeckoLibUtil.createInstanceCache(this);
    public boolean hidden = false;
    public GiantAnchorItem(final Tier tier, final Properties settings) {
        super(tier,settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {

            private final GiantAnchorItemRenderer renderer = new GiantAnchorItemRenderer();

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                return this.renderer;
            }
        });
    }

    /**
    Move the anchor to the owner and plays the return sound
     */
    public static void retrieveAnchor(@NotNull final LivingEntity thrower){
        final AnchorProjectileEntity anchorProjectile= (AnchorProjectileEntity) thrower.theConjunctionOfTheSpheres$getAnchor();
        if(anchorProjectile!=null) {
            anchorProjectile.pickupType= AbstractArrow.Pickup.ALLOWED;
            anchorProjectile.dealtDamage=true;
            anchorProjectile.setPos(thrower.blockPosition().getCenter());
            thrower.level().playSound(null, thrower.getX(), thrower.getY(), thrower.getZ(), TCOTS_Sounds.getSoundEvent("anchor_chain"), thrower.getSoundSource(), 1.0f, 1.0f);
        }
    }

    @Override
    public void releaseUsing(@NotNull final ItemStack anchorStack, @NotNull final Level world, @NotNull final LivingEntity user, final int remainingUseTicks) {

        if(!world.isClientSide){
            if(user.theConjunctionOfTheSpheres$getAnchor()!=null){
                GiantAnchorItem.retrieveAnchor(user);
            } else {

                final float pullProgress = BowItem.getPowerForTime(this.getUseDuration(anchorStack, user) - remainingUseTicks);

                final AnchorProjectileEntity anchorProjectile = new AnchorProjectileEntity(user, world);
                anchorProjectile.setEnchanted(anchorStack.hasFoil());
                anchorProjectile.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, pullProgress * 0.4f, 1.0f);

                if (user instanceof final Player player) {
                    anchorStack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }

                world.addFreshEntity(anchorProjectile);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), TCOTS_Sounds.getSoundEvent("anchor_throw"), user.getSoundSource(), 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
            }
        }


    }
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull final Level world, @NotNull final Player user, @NotNull final InteractionHand hand) {
        final ItemStack anchorStack= user.getItemInHand(hand);

        user.startUsingItem(hand);
        return InteractionResultHolder.consume(anchorStack);
    }

    @Override
    public int getUseDuration(@NotNull final ItemStack stack, @NotNull final LivingEntity user) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull final ItemStack stack) {
        if(wasLaunched(stack)){
            return UseAnim.NONE;
        }

        return UseAnim.SPEAR;
    }


    @Override
    public boolean hurtEnemy(@NotNull final ItemStack stack, @NotNull final LivingEntity target, @NotNull final LivingEntity user) {
        GiantAnchorItem.retrieveAnchor(user);
        return true;
    }

    @Override
    public void postHurtEnemy(final ItemStack stack, @NotNull final LivingEntity target, @NotNull final LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean mineBlock(@NotNull final ItemStack stack, @NotNull final Level world, @NotNull final BlockState state, @NotNull final BlockPos pos, @NotNull final LivingEntity miner) {
        GiantAnchorItem.retrieveAnchor(miner);
        return super.mineBlock(stack, world, state, pos, miner);
    }


    @Override
    public void inventoryTick(@NotNull final ItemStack anchorStack, @NotNull final Level world, @NotNull final Entity entity, final int slot, final boolean selected) {
        if(entity instanceof final LivingEntity livingEntity){
            this.hidden = livingEntity.theConjunctionOfTheSpheres$getAnchor()!=null;
        }

        if(entity instanceof final LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$getAnchor()!=null){
            anchorStack.set(TCOTS_Items.AnchorRetrieve(), true);
        } else if (entity instanceof final LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$getAnchor()==null) {
            anchorStack.remove(TCOTS_Items.AnchorRetrieve());
        }
    }

    public static boolean wasLaunched(final ItemStack stack) {
        return stack.has(TCOTS_Items.AnchorRetrieve());
    }

    @Override
    public void registerControllers(final ContextAwareAnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GeoControllersUtil.genericIdleController(this).triggerableAnim("idle", GeoControllersUtil.IDLE));
    }

    @Override
    public boolean isPerspectiveAware() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
