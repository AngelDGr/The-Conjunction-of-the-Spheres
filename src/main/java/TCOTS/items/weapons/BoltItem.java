package TCOTS.items.weapons;

import TCOTS.entity.misc.bolts.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BoltItem extends ArrowItem {

    private final String id;
    public BoltItem(Item.Settings settings, String id) {
        super(settings);
        this.id=id;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher."+id).formatted(Formatting.GRAY));
    }

    public String getId() {
        return id;
    }


    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return switch (id) {
            default               -> new BaseBoltProjectile     (world, shooter, stack.copyWithCount(1), shotFrom);
            case "blunt_bolt"     -> new BluntBoltProjectile    (world, shooter, stack.copyWithCount(1), shotFrom);
            case "precision_bolt" -> new PrecisionBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
            case "exploding_bolt" -> new ExplodingBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
            case "broadhead_bolt" -> new BroadheadBoltProjectile(world, shooter, stack.copyWithCount(1), shotFrom);
        };
    }
}
