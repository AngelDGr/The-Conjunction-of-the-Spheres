package TCOTS.mixin;

import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.utils.EntitiesUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow public abstract Item getItem();

    //TODO: Add specific colors to each oil tooltip, update when add more oils
    @Unique
    ItemStack THIS = (ItemStack) (Object) this;
    //Oil tooltip in weapons
    @SuppressWarnings("all")
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
    @Redirect(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;",
            ordinal = 5))
    private MutableText manticoreAttributeMaxToxicityColor(MutableText instance, Formatting formatting, @Local Map.Entry<EntityAttribute, EntityAttributeModifier> entry){
        if(entry.getKey() == TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY){
            return instance.formatted(Formatting.DARK_GREEN);
        }

        return instance.formatted(formatting);
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
