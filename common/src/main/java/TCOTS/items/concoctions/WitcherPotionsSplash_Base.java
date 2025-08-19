package TCOTS.items.concoctions;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WitcherPotionsSplash_Base extends WitcherPotions_Base implements ProjectileItem {

    public WitcherPotionsSplash_Base(final Item.Properties settings, final MobEffectInstance effect, final int toxicity) {
        super(settings, effect, toxicity, false);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull final Level world, @NotNull final Player user, @NotNull final InteractionHand hand) {
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.SPLASH_POTION_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

        final ItemStack itemStack = user.getItemInHand(hand);
        if (!world.isClientSide) {
            final ThrownPotion potionEntity = new ThrownPotion(world, user);
            potionEntity.setItem(itemStack);
            potionEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), -20.0F, 0.5F, 1.0F);
            world.addFreshEntity(potionEntity);
        }

        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1);

            final ItemStack stack_Empty = getStackEmptyBottle(this);
            if (user instanceof final Player playerEntity) {
                if (!playerEntity.getAbilities().instabuild) {
                    if (!playerEntity.getInventory().add(stack_Empty)) {
                        playerEntity.drop(stack_Empty, false);
                    }
                }
            }
        }

        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public Projectile asProjectile(final Level world, final Position pos, final ItemStack stack, final Direction direction) {
        return Util.make(new ThrownPotion(world, pos.x(), pos.y(), pos.z()), entity -> entity.setItem(stack));
    }
}
