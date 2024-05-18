package TCOTS.items.weapons;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WitcherBaseCrossbow extends CrossbowItem {
    public WitcherBaseCrossbow(Settings settings) {
        super(settings);
    }

    private boolean charged = false;
    private boolean loaded = false;

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (CrossbowItem.isCharged(itemStack)) {
            CrossbowItem.shootAll(world, user, hand, itemStack, this.getProjectileSpeed(itemStack), 1.0f);
            CrossbowItem.setCharged(itemStack, false);
            return TypedActionResult.consume(itemStack);
        }
        if (!user.getProjectileType(itemStack).isEmpty()) {
            if (!CrossbowItem.isCharged(itemStack)) {
                this.charged = false;
                this.loaded = false;
                user.setCurrentHand(hand);
            }
            return TypedActionResult.consume(itemStack);
        }
        return TypedActionResult.fail(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
            SoundEvent soundEvent = this.getQuickChargeSound(i);
            SoundEvent soundEvent2 = i == 0 ? SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float) (stack.getMaxUseTime() - remainingUseTicks) / (float) this.getCrossbowPullTime(stack);
            if (f < 0.2f) {
                this.charged = false;
                this.loaded = false;
            }
            if (f >= 0.2f && !this.charged) {
                this.charged = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
            if (f >= 0.5f && soundEvent2 != null && !this.loaded) {
                this.loaded = true;
                world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5f, 1.0f);
            }
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return this.getCrossbowPullTime(stack) + 3;
    }

    private SoundEvent getQuickChargeSound(int stage) {
        return switch (stage) {
            case 1 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default -> SoundEvents.ITEM_CROSSBOW_LOADING_START;
        };
    }

    protected float getProjectileSpeed(ItemStack stack) {
        if (CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET)) {
            return 1.6f;
        }
        return 3.15f;
    }

    protected int getCrossbowPullTime(ItemStack stack) {
        int i = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
        return i == 0 ? 25 : 25 - 5 * i;
    }

    public static boolean hasBoltProjectile(ItemStack crossbow) {
        return WitcherBaseCrossbow.getProjectiles(crossbow).stream().anyMatch(s -> s.getItem() instanceof BoltItem);
    }

    private static final String CHARGED_PROJECTILES_KEY = "ChargedProjectiles";
    private static List<ItemStack> getProjectiles(ItemStack crossbow) {
        NbtList nbtList;
        ArrayList<ItemStack> list = Lists.newArrayList();
        NbtCompound nbtCompound = crossbow.getNbt();
        if (nbtCompound != null && nbtCompound.contains(CHARGED_PROJECTILES_KEY, NbtElement.LIST_TYPE) && (nbtList = nbtCompound.getList(CHARGED_PROJECTILES_KEY, NbtElement.COMPOUND_TYPE)) != null) {
            for (int i = 0; i < nbtList.size(); ++i) {
                NbtCompound nbtCompound2 = nbtList.getCompound(i);
                list.add(ItemStack.fromNbt(nbtCompound2));
            }
        }
        return list;
    }
}
