package TCOTS.mixin;

import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.items.armor.ManticoreArmorItem;
import com.google.common.collect.ImmutableMultimap;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumMap;
import java.util.UUID;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    @Shadow @Final private static EnumMap<ArmorItem.Type, UUID> MODIFIERS;
    @Unique
    ArmorItem THIS = (ArmorItem) (Object) this;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMultimap$Builder;build()Lcom/google/common/collect/ImmutableMultimap;"))
    private void injectInConstructor(ArmorMaterial material, ArmorItem.Type type, Item.Settings settings, CallbackInfo ci,
                                     @Local ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder) {
        UUID uUID = MODIFIERS.get(type);
        if(THIS instanceof ManticoreArmorItem){
            builder.put(
                    TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY, new EntityAttributeModifier(uUID, "Armor toxicity", 10, EntityAttributeModifier.Operation.ADDITION)
            );
        }


    }


}
