package TCOTS.mixin;

import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.items.weapons.SwordWithTooltip;
import TCOTS.registry.TCOTS_Items;
import TCOTS.utils.EntitiesUtil;
import TCOTS.items.AlchemyFormulaItem;
import TCOTS.utils.MiscUtil;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

@Mixin(value = ItemStack.class, priority = 20000)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    @Shadow protected abstract <T extends TooltipProvider> void addToTooltip(DataComponentType<T> componentType, Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type);

    @Unique
    ItemStack THIS = (ItemStack) (Object) this;

    //Oil tooltip in weapons
    @Inject(method = "getTooltipLines", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 4))
    private void monsterOilTooltipInWeapons(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir, @Local final Consumer<Component> consumer){
        this.addToTooltip(TCOTS_Items.MonsterOilComponent(), context, consumer, type);
    }


    //Manticore Armor
    @ModifyArg(method = "addModifierTooltip", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 1))
    private ChatFormatting manticoreAttributeMaxToxicityColor(final ChatFormatting formatting, @Local(argsOnly = true) final Holder<Attribute> attribute){
        if(attribute == TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY){
            return ChatFormatting.DARK_GREEN;
        }

        return formatting;
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void reduceDrinkingTime(final LivingEntity user, final CallbackInfoReturnable<Integer> cir){
        if(EntitiesUtil.isWearingManticoreArmor(user) && this.getItem() instanceof PotionItem){
            cir.setReturnValue(this.getItem().getUseDuration(THIS,user)/2);
        }
    }

    //Weapon Tooltips
    @Inject(method = "getTooltipLines", at = @At(value = "RETURN", ordinal = 1))
    private void customWeaponTooltip(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir){
        if(player!=null && THIS.getItem() instanceof final SwordWithTooltip sword){
            MiscUtil.setSpecialTooltip(Component.translatable("tooltip.tcots_witcher.generic_tooltip.special_abilities"), THIS, cir.getReturnValue(), sword.tooltip, type);
        }
    }

    //Alchemy Formula
    @Inject(method = "getTooltipLines", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 4))
    private void customTooltipFormula(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir, @Local final Consumer<Component> consumer){
        if(player!=null){
            AlchemyFormulaItem.appendTooltip(THIS, player.level(), consumer);
        }
    }

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void injectFormulaRarity(final CallbackInfoReturnable<Rarity> cir){
        if(THIS.is(TCOTS_Items.ALCHEMY_FORMULA.get()) && AlchemyFormulaItem.isDecoctionRecipe(THIS)){
            cir.setReturnValue(Rarity.RARE);
        }
    }

}
