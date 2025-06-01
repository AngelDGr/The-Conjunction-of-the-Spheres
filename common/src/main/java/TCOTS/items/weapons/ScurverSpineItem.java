package TCOTS.items.weapons;

import TCOTS.registry.TCOTS_Items;
import TCOTS.entity.misc.ScurverSpineEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ScurverSpineItem extends Item  implements ProjectileItem {
    public ScurverSpineItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClientSide) {
            Projectile scurverSpine = new ScurverSpineEntity(user, world, new ItemStack(TCOTS_Items.SCURVER_SPINE.get()), null);
            user.getCooldowns().addCooldown(this, 20);
            scurverSpine.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, 1.0f, 1.0f);
            world.addFreshEntity(scurverSpine);
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }


    @Override
    public @NotNull Projectile asProjectile(@NotNull Level world, Position pos, ItemStack stack, @NotNull Direction direction) {
        ScurverSpineEntity scurverSpine = new ScurverSpineEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        scurverSpine.pickup = AbstractArrow.Pickup.ALLOWED;
        return scurverSpine;
    }
}
