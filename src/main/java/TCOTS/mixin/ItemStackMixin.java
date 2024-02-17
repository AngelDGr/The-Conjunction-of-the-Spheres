package TCOTS.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @ModifyVariable(method = "getTooltip", at = @At(
            value = "INVOKE"
            ,target = "Lnet/minecraft/nbt/NbtCompound;contains(Ljava/lang/String;I)Z", ordinal = 0))
    private List<Text> MonsterOilTooltip(List<Text> value){

        ItemStack thisObject = (ItemStack)(Object)this;
        if(thisObject.hasNbt()){
            NbtCompound nbt = thisObject.getNbt();
            if(nbt.contains("Monster Oil")){
                NbtCompound monsterOil = thisObject.getSubNbt("Monster Oil");
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



}
