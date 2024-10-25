package TCOTS.items;

import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.items.geo.renderer.GiantAnchorItemRenderer;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.ContextAwareAnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GiantAnchorItem extends ToolItem implements GeoItem {
    //xTODO: Add attacks to the anchor (2)
    //The anchor it's going to return to you with right-click
    //The anchor can be launched
    private final AnimatableInstanceCache cache   = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);
    public boolean hidden = false;
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;
    public GiantAnchorItem(Settings settings) {
        super(TCOTS_ToolMaterials.ANCHOR,settings);

        float attackDamage = TCOTS_ToolMaterials.ANCHOR.getAttackDamage();

        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", attackDamage, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.4f, EntityAttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();

        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void createRenderer(@NotNull Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {

            private final GiantAnchorItemRenderer renderer = new GiantAnchorItemRenderer();

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
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

                float pullProgress = BowItem.getPullProgress(this.getMaxUseTime(anchorStack) - remainingUseTicks);

                AnchorProjectileEntity anchorProjectile = new AnchorProjectileEntity(user, world);
                anchorProjectile.setEnchanted(anchorStack.hasGlint());
                anchorProjectile.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, pullProgress * 0.4f, 1.0f);

                if (user instanceof PlayerEntity player) {
                    anchorStack.damage(1, user, p -> p.sendToolBreakStatus(player.getActiveHand()));
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
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }
    @Override
    public UseAction getUseAction(@NotNull ItemStack stack) {
        if(stack.hasNbt()){
            assert stack.getNbt() != null;
            NbtCompound nbt = stack.getNbt().getCompound("State");
            if(nbt.getBoolean("nextRetrieve")){
                return UseAction.NONE;
            }
        }

        return UseAction.SPEAR;
    }
    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            return this.attributeModifiers;
        }
        return super.getAttributeModifiers(slot);
    }
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (state.getHardness(world, pos) != 0.0f) {
            stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }


    @Override
    public void inventoryTick(ItemStack anchorStack, World world, Entity entity, int slot, boolean selected) {
        if(entity instanceof LivingEntity livingEntity){
            this.hidden = livingEntity.theConjunctionOfTheSpheres$getAnchor()!=null;
        }

        if(entity instanceof LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$getAnchor()!=null){
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean("nextRetrieve", true);
            anchorStack.getOrCreateNbt().put("State", nbt);
        } else if (entity instanceof LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$getAnchor()==null) {
            anchorStack.removeSubNbt("State");
        }
    }

    public static boolean wasLaunched(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        return nbtCompound != null && nbtCompound.get("State")!=null;
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
