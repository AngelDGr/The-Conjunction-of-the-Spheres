package TCOTS.items.weapons;

import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.items.TCOTS_Items;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ProjectileItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ScurverSpineItem extends Item  implements ProjectileItem {
    public ScurverSpineItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            ProjectileEntity scurverSpine = new ScurverSpineEntity(user, world, new ItemStack(TCOTS_Items.SCURVER_SPINE), null);
            user.getItemCooldownManager().set(this, 20);
            scurverSpine.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.0f, 1.0f);
            world.spawnEntity(scurverSpine);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }


    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ScurverSpineEntity scurverSpine = new ScurverSpineEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        scurverSpine.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return scurverSpine;
    }
}
