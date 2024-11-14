package TCOTS.items;

import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class WaterHag_MudBallItem extends Item implements ProjectileItem {
    public WaterHag_MudBallItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), TCOTS_Sounds.WATER_HAG_MUD_BALL_LAUNCH, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            WaterHag_MudBallEntity mudballEntity = new WaterHag_MudBallEntity(world, user,1);
            mudballEntity.setItem(itemStack);
            mudballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 0.5f, 1.0f);
            world.spawnEntity(mudballEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        return Util.make(new WaterHag_MudBallEntity(world, pos.getX(), pos.getY(), pos.getZ()), entity -> entity.setItem(stack));
    }
}
