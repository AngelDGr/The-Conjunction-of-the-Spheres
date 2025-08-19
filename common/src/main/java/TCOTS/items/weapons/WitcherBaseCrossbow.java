package TCOTS.items.weapons;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class WitcherBaseCrossbow extends CrossbowItem {

    public WitcherBaseCrossbow(final Item.Properties settings) {
        super(settings);
    }

    private boolean charged = false;
    private boolean loaded = false;

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull final Level world, final Player user, @NotNull final InteractionHand hand) {
        final ItemStack itemStack = user.getItemInHand(hand);
        final ChargedProjectiles chargedProjectilesComponent = itemStack.get(DataComponents.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            this.performShooting(world, user, hand, itemStack, getShootingPower(chargedProjectilesComponent), 1.0F, null);
            return InteractionResultHolder.consume(itemStack);
        } else if (!user.getProjectile(itemStack).isEmpty()) {
            this.charged = false;
            this.loaded = false;
            user.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        } else {
            return InteractionResultHolder.fail(itemStack);
        }
    }

    private static final CrossbowItem.ChargingSounds DEFAULT_LOADING_SOUNDS = new CrossbowItem.ChargingSounds(
            Optional.of(SoundEvents.CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.CROSSBOW_LOADING_END)
    );

    CrossbowItem.ChargingSounds getChargingSounds(final ItemStack stack) {
        return EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.CROSSBOW_CHARGING_SOUNDS)
                .orElse(DEFAULT_LOADING_SOUNDS);
    }
    @Override
    public void onUseTick(final Level world, @NotNull final LivingEntity user, @NotNull final ItemStack stack, final int remainingUseTicks) {
        if (!world.isClientSide) {
            final CrossbowItem.ChargingSounds loadingSounds = this.getChargingSounds(stack);
            final float f = (float)(stack.getUseDuration(user) - remainingUseTicks) / (float)getChargeDuration(stack, user);
            if (f < 0.2F) {
                this.charged = false;
                this.loaded = false;
            }

            if (f >= 0.2F && !this.charged) {
                this.charged = true;
                loadingSounds.start()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }

            if (f >= 0.5F && !this.loaded) {
                this.loaded = true;
                loadingSounds.mid()
                        .ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), sound.value(), SoundSource.PLAYERS, 0.5F, 1.0F));
            }
        }
    }

    @Override
    public void releaseUsing(@NotNull final ItemStack stack, @NotNull final Level world, @NotNull final LivingEntity user, final int remainingUseTicks) {
        final int i = this.getUseDuration(stack, user) - remainingUseTicks;
        final float f = getPowerForTime(i, stack, user);
        if (f >= 1.0F && !isCharged(stack) && tryLoadProjectiles(user, stack)) {
            final CrossbowItem.ChargingSounds loadingSounds = this.getChargingSounds(stack);
            loadingSounds.end()
                    .ifPresent(
                            sound -> world.playSound(
                                    null,
                                    user.getX(),
                                    user.getY(),
                                    user.getZ(),
                                    sound.value(),
                                    user.getSoundSource(),
                                    1.0F,
                                    1.0F / (world.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F
                            )
                    );
        }
    }

    private static boolean tryLoadProjectiles(final LivingEntity shooter, final ItemStack crossbow) {
        final List<ItemStack> list = draw(crossbow, shooter.getProjectile(crossbow), shooter);
        if (!list.isEmpty()) {
            crossbow.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
            return true;
        } else {
            return false;
        }
    }
    private float getPowerForTime(final int useTicks, final ItemStack stack, final LivingEntity user) {
        float f = (float)useTicks / (float)getCrossbowPullTime(stack, user);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(@NotNull final ItemStack stack, @NotNull final LivingEntity user) {
        return this.getCrossbowPullTime(stack, user) + 3;
    }

    protected float getShootingPower(final ChargedProjectiles stack) {
        return stack.contains(Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    public int getCrossbowPullTime(final ItemStack stack, final LivingEntity user) {
        final float f = EnchantmentHelper.modifyCrossbowChargingTime(stack, user, 1.25F);
        return Mth.floor(f * 20.0F);
    }
}
