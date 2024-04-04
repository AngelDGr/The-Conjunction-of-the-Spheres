package TCOTS.items;

import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OrganicPasteItem extends Item {
    public OrganicPasteItem(Settings settings) {
        super(settings);
    }

    public static final String EFFECTS_KEY = "effects";

    public static void writeEffectsToPaste(ItemStack paste, List<SuspiciousStewIngredient.StewEffect> stewEffects) {
        NbtCompound nbtCompound = paste.getOrCreateNbt();
        SuspiciousStewIngredient.StewEffect.LIST_CODEC.encodeStart(NbtOps.INSTANCE, stewEffects).result().ifPresent(nbtElement -> nbtCompound.put(EFFECTS_KEY, nbtElement));
    }

    private static void forEachEffect(ItemStack stew, Consumer<SuspiciousStewIngredient.StewEffect> effectConsumer) {
        NbtCompound nbtCompound = stew.getNbt();
        if (nbtCompound != null && nbtCompound.contains(EFFECTS_KEY, NbtElement.LIST_TYPE)) {
            SuspiciousStewIngredient.StewEffect.LIST_CODEC.parse(NbtOps.INSTANCE, nbtCompound.getList(EFFECTS_KEY, NbtElement.COMPOUND_TYPE)).result().ifPresent(list -> list.forEach(effectConsumer));
        }
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack itemStack = super.finishUsing(stack, world, user);
        OrganicPasteItem.forEachEffect(itemStack, stewEffect ->
                {
                    int duration=1200;
                    if(stewEffect.effect().isInstant()){duration = 7;}

                    user.addStatusEffect(new StatusEffectInstance(stewEffect.effect(), duration));
                }
        );

        return itemStack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        ArrayList<StatusEffectInstance> list = new ArrayList<>();
        OrganicPasteItem.forEachEffect(stack, stewEffect ->
                {
                    int duration=1200;
                    if(stewEffect.effect().isInstant()){duration = 7;}

                    list.add(new StatusEffectInstance(stewEffect.effect(), duration));
                }
                );

        PotionUtil.buildTooltip(list, tooltip, 1.0f, world == null ? 20.0f : world.getTickManager().getTickRate());
    }
}
