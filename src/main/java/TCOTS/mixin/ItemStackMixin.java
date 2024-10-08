package TCOTS.mixin;

import TCOTS.interfaces.MaxToxicityIncreaser;
import TCOTS.items.armor.ManticoreArmorItem;
import TCOTS.utils.EntitiesUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    //TODO: Add specific colors to each oil tooltip, update when add more oils
    @Unique
    ItemStack THIS = (ItemStack) (Object) this;
    //Oil tooltip in weapons
    @ModifyVariable(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/nbt/NbtCompound;contains(Ljava/lang/String;I)Z", ordinal = 0))
    private List<Text> monsterOilTooltipInWeapons(List<Text> value){

        ItemStack thisObject = (ItemStack)(Object)this;
        if(thisObject.hasNbt()){
            NbtCompound nbt = thisObject.getNbt();
            assert nbt != null;
            if(nbt.contains("Monster Oil")){
                NbtCompound monsterOil = thisObject.getSubNbt("Monster Oil");
                assert monsterOil != null;
                if(monsterOil.contains("Id")){
                    switch (monsterOil.getInt("Id")){
                        case 0:
                            OilNamer(monsterOil, value, 0xb71520);
                            return value;
                        case 1:
                            OilNamer(monsterOil, value, 0xcc341d);
                            return value;
                        case 2:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 3:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 4:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 5:
                            OilNamer(monsterOil, value, 0x411106);
                            return value;
                        case 6:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 7:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 8:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 9:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 10:
                            OilNamer(monsterOil, value, 0xffffff);
                            return value;
                        case 11:
                            OilNamer(monsterOil, value, 0x00752b);
                            return value;
                        default:
                            return value;
                    }
                }
            }
        }

        return value;
    }

    @Unique
    private void OilNamer(NbtCompound monsterOil, List<Text> value, int color){
        value.add(ScreenTexts.EMPTY);
        MutableText OilName = (MutableText) Registries.ITEM.get(new Identifier(monsterOil.getString("Item"))).getName();
        OilName.styled(
                style -> style.withColor(color)
        );

        value.add(OilName);
        value.add(ScreenTexts.space().append(Text.translatable("tooltip.item.tcots-witcher.oils.uses", monsterOil.getInt("Uses")).formatted(Formatting.BLUE)));

    }



    //Manticore Armor
    @ModifyVariable(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lcom/google/common/collect/Multimap;entries()Ljava/util/Collection;",
            ordinal = 0))
    private List<Text> manticoreToxicityTooltip(List<Text> value){
        if(THIS.getItem() instanceof MaxToxicityIncreaser item){
            value.add(Text.translatable("tooltip.tcots-witcher.manticore_armor.toxicity", item.getExtraToxicity()).formatted(Formatting.DARK_GREEN));
        }

        return value;
    }

    @ModifyVariable(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V",
            ordinal = 0))
    private List<Text> manticoreSetBonusTooltip(List<Text> tooltip){

        if(MinecraftClient.getInstance()!=null && THIS.getItem() instanceof ManticoreArmorItem && (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) || InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT))){
            tooltip.add(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set1").formatted(Formatting.DARK_GREEN));

            tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set2").formatted(Formatting.DARK_GREEN, Formatting.ITALIC)));
            tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set3").formatted(Formatting.DARK_GREEN, Formatting.ITALIC)));
            tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set4").formatted(Formatting.DARK_GREEN, Formatting.ITALIC)));

        } else if(THIS.getItem() instanceof ManticoreArmorItem){

            tooltip.add(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set1").formatted(Formatting.GRAY));
            tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.manticore_armor.full_set.more").formatted(Formatting.GRAY, Formatting.ITALIC)));
        }

        return tooltip;
    }

    @Unique
    private PlayerEntity player;

    @Inject(method = "use", at = @At("HEAD"))
    private void getPlayer(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        this.player=user;
    }

    @Inject(method = "getMaxUseTime", at = @At("RETURN"), cancellable = true)
    private void reduceDrinkingTime(CallbackInfoReturnable<Integer> cir){
        if(player!=null && EntitiesUtil.isWearingManticoreArmor(player) && this.getItem() instanceof PotionItem){
            cir.setReturnValue(this.getItem().getMaxUseTime(THIS)/2);
        }
    }

}
