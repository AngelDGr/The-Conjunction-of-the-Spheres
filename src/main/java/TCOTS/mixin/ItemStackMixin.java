package TCOTS.mixin;

import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.AlchemyFormulaItem;
import TCOTS.utils.EntitiesUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Shadow protected abstract <T extends TooltipAppender> void appendTooltip(ComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type);

    @Unique
    ItemStack THIS = (ItemStack) (Object) this;

    //Oil tooltip in weapons
    @Inject(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
            ordinal = 4))
    private void monsterOilTooltipInWeapons(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local Consumer<Text> consumer){
        this.appendTooltip(TCOTS_Items.MONSTER_OIL_COMPONENT, context, consumer, type);
    }


    //Manticore Armor
    @Redirect(method = "appendAttributeModifierTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
            ordinal = 1))
    private MutableText manticoreAttributeMaxToxicityColor(MutableText instance, Formatting formatting, @Local(argsOnly = true) RegistryEntry<EntityAttribute> attribute){
        if(attribute == TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY){
            return instance.formatted(Formatting.DARK_GREEN);
        }

        return instance.formatted(formatting);
    }

    @Inject(method = "getMaxUseTime", at = @At("RETURN"), cancellable = true)
    private void reduceDrinkingTime(LivingEntity user, CallbackInfoReturnable<Integer> cir){
        if(EntitiesUtil.isWearingManticoreArmor(user) && this.getItem() instanceof PotionItem){
            cir.setReturnValue(this.getItem().getMaxUseTime(THIS,user)/2);
        }
    }

    //Alchemy Formula
    @Inject(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/item/ItemStack;appendTooltip(Lnet/minecraft/component/ComponentType;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/item/tooltip/TooltipType;)V",
            ordinal = 4))
    private void customTooltipFormula(Item.TooltipContext context, @Nullable PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local Consumer<Text> consumer){
        if(player!=null){
            AlchemyFormulaItem.appendTooltip(THIS, player.getWorld(), consumer);
        }
    }

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void injectFormulaRarity(CallbackInfoReturnable<Rarity> cir){
        if(THIS.isOf(TCOTS_Items.ALCHEMY_FORMULA) && AlchemyFormulaItem.isDecoctionRecipe(THIS)){
            cir.setReturnValue(Rarity.RARE);
        }
    }

}
