package TCOTS.items;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WaterHag_MudBallItem extends Item implements ProjectileItem {
    public WaterHag_MudBallItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
        ItemStack itemStack = user.getItemInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), TCOTS_Sounds.getSoundEvent("water_hag_mud_ball_launch"), SoundSource.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClientSide) {
            WaterHag_MudBallEntity mudballEntity = new WaterHag_MudBallEntity(world, user,1);
            mudballEntity.setItem(itemStack);
            mudballEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0f, 0.5f, 1.0f);
            world.addFreshEntity(mudballEntity);
        }
        user.awardStat(Stats.ITEM_USED.get(this));
        if (!user.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level world, Position pos, @NotNull ItemStack stack, @NotNull Direction direction) {
        return Util.make(new WaterHag_MudBallEntity(world, pos.x(), pos.y(), pos.z()), entity -> entity.setItem(stack));
    }
}
