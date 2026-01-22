package mors.tcots.mixin;

import mors.tcots.items.armor.set.HasTooltip;
import mors.tcots.registry.TCOTS_EntityAttributes;
import mors.tcots.items.armor.set.IsArmorSet;
import mors.tcots.items.weapons.SwordWithTooltip;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.items.AlchemyFormulaItem;
import mors.tcots.utils.TCOTS_Util;
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
    ItemStack tcots$THIS = (ItemStack) (Object) this;

    //Oil tooltip in weapons
    @Inject(method = "getTooltipLines", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 4))
    private void tcots$monsterOilTooltipInWeapons(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir, @Local final Consumer<Component> consumer){
        this.addToTooltip(TCOTS_Items.MonsterOilComponent(), context, consumer, type);
    }

    //Manticore Armor
    @ModifyArg(method = "addModifierTooltip", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/chat/MutableComponent;withStyle(Lnet/minecraft/ChatFormatting;)Lnet/minecraft/network/chat/MutableComponent;",
            ordinal = 1))
    private ChatFormatting tcots$manticoreAttributeMaxToxicityColor(final ChatFormatting formatting, @Local(argsOnly = true) final Holder<Attribute> attribute){
        if(attribute == TCOTS_EntityAttributes.WITCHER_MAX_TOXICITY){
            return ChatFormatting.DARK_GREEN;
        }

        return formatting;
    }

    @Inject(method = "getUseDuration", at = @At("RETURN"), cancellable = true)
    private void tcots$reduceDrinkingTime(final LivingEntity user, final CallbackInfoReturnable<Integer> cir){

        final var potionDrinkTimeAttribute = user.getAttribute(TCOTS_EntityAttributes.POTION_DRINK_TIME);
        double drinkTime = 1;
        if (potionDrinkTimeAttribute != null) drinkTime = potionDrinkTimeAttribute.getValue();

        if(this.getItem() instanceof PotionItem){
            cir.setReturnValue(Math.max((int) (this.getItem().getUseDuration(tcots$THIS,user) * drinkTime), 1));
        }
    }

    //Weapon Tooltips
    @Inject(method = "getTooltipLines", at = @At(value = "RETURN", ordinal = 1))
    private void tcots$customWeaponTooltip(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir){
        if(player!=null && tcots$THIS.getItem() instanceof final SwordWithTooltip sword)
            TCOTS_Util.setSpecialTooltip(tcots$THIS, cir.getReturnValue(), sword.tooltip, type);
    }

    //Armor Sets Tooltips
    @Inject(method = "getTooltipLines", at = @At(value = "RETURN", ordinal = 1))
    private void tcots$armorSetsTooltip(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir, @Local final List<Component> list){
        if(player==null) return;

        if(tcots$THIS.getItem() instanceof final HasTooltip hasTooltip)
            HasTooltip.addTooltip(list, player, tcots$THIS, hasTooltip.getTooltip());

        if(tcots$THIS.getItem() instanceof final IsArmorSet isArmorSet && isArmorSet.getSet()!=null)
            isArmorSet.getSet().addArmorSetTooltip(list, player, tcots$THIS);
    }

    //Alchemy Formula
    @Inject(method = "getTooltipLines", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/world/item/ItemStack;addToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V",
            ordinal = 4))
    private void tcots$customTooltipFormula(final Item.TooltipContext context, @Nullable final Player player, final TooltipFlag type, final CallbackInfoReturnable<List<Component>> cir, @Local final Consumer<Component> consumer){
        if(player!=null){
            AlchemyFormulaItem.appendTooltip(tcots$THIS, player.level(), consumer);
        }
    }

    @Inject(method = "getRarity", at = @At("HEAD"), cancellable = true)
    private void tcots$injectFormulaRarity(final CallbackInfoReturnable<Rarity> cir){
        if(tcots$THIS.is(TCOTS_Items.ALCHEMY_FORMULA.get()) && AlchemyFormulaItem.isDecoctionRecipe(tcots$THIS)){
            cir.setReturnValue(Rarity.RARE);
        }
    }

}
