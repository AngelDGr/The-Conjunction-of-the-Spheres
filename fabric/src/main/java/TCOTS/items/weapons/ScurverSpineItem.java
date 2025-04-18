package TCOTS.items.weapons;

import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.items.TCOTS_Items_Fabric;
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

public class ScurverSpineItem extends Item  implements ProjectileItem {
    public ScurverSpineItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClientSide) {
            Projectile scurverSpine = new ScurverSpineEntity(user, world, new ItemStack(TCOTS_Items_Fabric.SCURVER_SPINE), null);
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
    public Projectile asProjectile(Level world, Position pos, ItemStack stack, Direction direction) {
        ScurverSpineEntity scurverSpine = new ScurverSpineEntity(world, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
        scurverSpine.pickup = AbstractArrow.Pickup.ALLOWED;
        return scurverSpine;
    }
}
