package TCOTS.items.weapons;

import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ToolMaterials;
import TCOTS.items.geo.renderer.GiantAnchorItemRenderer;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.ContextAwareAnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GiantAnchorItem extends ToolItem implements GeoItem {
    //xTODO: Add attacks to the anchor (2)
    //The anchor it's going to return to you with right-click
    //The anchor can be launched
    private final AnimatableInstanceCache cache   = GeckoLibUtil.createInstanceCache(this);
    public boolean hidden = false;
    public GiantAnchorItem(Settings settings) {
        super(TCOTS_ToolMaterials.ANCHOR,settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {

            private final GiantAnchorItemRenderer renderer = new GiantAnchorItemRenderer();

            @Override
            public @NotNull BuiltinModelItemRenderer getGeoItemRenderer() {
                return this.renderer;
            }
        });
    }

    /**
    Move the anchor to the owner and plays the return sound
     */
    public static void retrieveAnchor(@NotNull LivingEntity thrower){
        AnchorProjectileEntity anchorProjectile= thrower.theConjunctionOfTheSpheres$getAnchor();
        if(anchorProjectile!=null) {
            anchorProjectile.pickupType= PersistentProjectileEntity.PickupPermission.ALLOWED;
            anchorProjectile.dealtDamage=true;
            anchorProjectile.setPosition(thrower.getBlockPos().toCenterPos());
            thrower.getWorld().playSound(null, thrower.getX(), thrower.getY(), thrower.getZ(), TCOTS_Sounds.ANCHOR_CHAIN, thrower.getSoundCategory(), 1.0f, 1.0f);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack anchorStack, @NotNull World world, LivingEntity user, int remainingUseTicks) {

        if(!world.isClient){
            if(user.theConjunctionOfTheSpheres$getAnchor()!=null){
                GiantAnchorItem.retrieveAnchor(user);
            } else {

                float pullProgress = BowItem.getPullProgress(this.getMaxUseTime(anchorStack, user) - remainingUseTicks);

                AnchorProjectileEntity anchorProjectile = new AnchorProjectileEntity(user, world);
                anchorProjectile.setEnchanted(anchorStack.hasGlint());
                anchorProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, pullProgress * 0.4f, 1.0f);

                if (user instanceof PlayerEntity player) {
                    anchorStack.damage(1, player, EquipmentSlot.MAINHAND);
                    player.incrementStat(Stats.USED.getOrCreateStat(this));
                }

                world.spawnEntity(anchorProjectile);
                world.playSound(null, user.getX(), user.getY(), user.getZ(), TCOTS_Sounds.ANCHOR_THROW, user.getSoundCategory(), 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + pullProgress * 0.5f);
            }
        }


    }
    @Override
    public TypedActionResult<ItemStack> use(World world, @NotNull PlayerEntity user, Hand hand) {
        ItemStack anchorStack= user.getStackInHand(hand);

        user.setCurrentHand(hand);
        return TypedActionResult.consume(anchorStack);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(@NotNull ItemStack stack) {
        if(wasLaunched(stack)){
            return UseAction.NONE;
        }

        return UseAction.SPEAR;
    }


    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity user) {
        GiantAnchorItem.retrieveAnchor(user);
        return true;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        GiantAnchorItem.retrieveAnchor(miner);
        return super.postMine(stack, world, state, pos, miner);
    }


    @Override
    public void inventoryTick(ItemStack anchorStack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof LivingEntity livingEntity){
            this.hidden = livingEntity.theConjunctionOfTheSpheres$getAnchor()!=null;
        }

        if(entity instanceof LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$getAnchor()!=null){
            anchorStack.set(TCOTS_Items.ANCHOR_RETRIEVE, true);
        } else if (entity instanceof LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$getAnchor()==null) {
            anchorStack.remove(TCOTS_Items.ANCHOR_RETRIEVE);
        }
    }

    public static boolean wasLaunched(ItemStack stack) {
        return stack.contains(TCOTS_Items.ANCHOR_RETRIEVE);
    }

    @Override
    public void registerControllers(ContextAwareAnimatableManager.ControllerRegistrar controllers) {
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
