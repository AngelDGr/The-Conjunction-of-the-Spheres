package TCOTS.items.weapons;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.items.TCOTS_ToolMaterials;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SwordsAttributes {
    public static final ResourceLocation BASE_ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.withDefaultNamespace("base_attack_damage");
    public static final ResourceLocation BASE_ATTACK_SPEED_MODIFIER_ID = ResourceLocation.withDefaultNamespace("base_attack_speed");
    private static ItemAttributeModifiers.Builder createMainSwordAttributeModifiers(Tier material, int baseAttackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(
                                BASE_ATTACK_DAMAGE_MODIFIER_ID, (float)baseAttackDamage + material.getAttackDamageBonus(), AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                );
    }

    public static ItemAttributeModifiers createWintersBladeAttributeModifiers() {
        ItemAttributeModifiers.Builder builder =createMainSwordAttributeModifiers(TCOTS_ToolMaterials.WINTERS_BLADE, 4, -2.4f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .add(
                            TCOTS_EntityAttributes.AARD_INTENSITY,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade-aard"),
                                    4, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade-adrenaline"),
                                    0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade-spell_critical"),
                                    0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createArdaenyeAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.ARDAENYE, 3, -2.6f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ardaenye-adrenaline"),
                                    0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createDyaeblAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.DYAEBL,  2, -2.4f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "dyaebl-adrenaline"),
                                    0.075, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createMoonbladeAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.MOONBLADE, 2, -2.2f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .add(
                            TCOTS_EntityAttributes.SIGN_INTENSITY,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "moonblade-sign"),
                                    2.5, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.SPELL_CRITICAL_DAMAGE,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "moonblade-spell_critical"),
                                    0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

    public static ItemAttributeModifiers createGvalchirAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = createMainSwordAttributeModifiers(TCOTS_ToolMaterials.GVALCHIR, 3, -2.2f);

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder
                    .add(
                            TCOTS_EntityAttributes.SIGN_INTENSITY,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "gvalchir-sign"),
                                    2.5, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            TCOTS_EntityAttributes.ADRENALINE_GAIN,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "gvalchir-adrenaline"),
                                    0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND
                    );
        }

        return builder.build();
    }

}
