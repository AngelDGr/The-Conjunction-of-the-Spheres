package TCOTS.items.weapons;

import TCOTS.entity.misc.bolts.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class BoltItem extends ArrowItem {

    private final String id;
    public BoltItem(final Item.Properties settings, final String id) {
        super(settings);
        this.id=id;
    }

    @Override
    public void appendHoverText(@NotNull final ItemStack stack, @NotNull final TooltipContext context, final List<Component> tooltip, @NotNull final TooltipFlag type) {
        tooltip.add(Component.translatable("tooltip.tcots_witcher."+id).withStyle(ChatFormatting.GRAY));
    }

    public String getId() {
        return id;
    }


    @Override
    public @NotNull AbstractArrow createArrow(@NotNull final Level world, @NotNull final ItemStack stack, @NotNull final LivingEntity shooter, @Nullable final ItemStack shotFrom) {
        return switch (id) {
            case "blunt_bolt"     -> new BluntBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
            case "precision_bolt" -> new PrecisionBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
            case "exploding_bolt" -> new ExplodingBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
            case "broadhead_bolt" -> new BroadheadBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
            default               -> new BaseBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
        };
    }
}
