package mors.tcots.registry;

import mors.tcots.TCOTS_Main;
import mors.tcots.TCOTS_Registries;
import mors.tcots.utils.TCOTS_Util;
import com.google.common.base.Suppliers;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class TCOTS_ItemsMaterials {

    public enum Tool implements Tier {

        GVALCHIR(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 0.0f, 3.0f, 20,
                () -> Ingredient.of(
                        TCOTS_Registries.ITEMS.getRegistrar().get(TCOTS_Main.id("bullvore_horn_fragment")))),

        MOONBLADE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 0.0f, 3.0f, 20,
                () -> Ingredient.of(
                        TCOTS_Util.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(TCOTS_Main.id_WitcherRPG( "silver_ingot")) :
                                Items.GOLD_INGOT
                )),

        DYAEBL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 0.0f, 3.0f, 20,
                () -> Ingredient.of(
                        TCOTS_Util.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(TCOTS_Main.id_WitcherRPG( "steel_ingot")) :
                                Items.IRON_INGOT)),

        WINTERS_BLADE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0f, 4.0f, 30,
                () -> Ingredient.of(
                        TCOTS_Util.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(TCOTS_Main.id_WitcherRPG( "dark_steel_ingot")) :
                                Items.NETHERITE_INGOT)),

        ARDAENYE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1400, 9.0f, 4.0f, 20,
                () -> Ingredient.of(
                        TCOTS_Util.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(TCOTS_Main.id_WitcherRPG( "dark_steel_ingot")) :
                                Items.DIAMOND)),

        ANCHOR(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 9.0f, 8.0f, 5,
                () -> Ingredient.of(
                        Items.IRON_BLOCK));


        private final TagKey<Block> inverseTag;
        private final int itemDurability;
        private final float miningSpeed;
        private final float attackDamage;
        private final int enchantability;
        private final Supplier<Ingredient> repairIngredient;

        Tool(
                final TagKey<Block> inverseTag,
                final int itemDurability,
                final float miningSpeed,
                final float attackDamage,
                final int enchantability,
                final Supplier<Ingredient> repairIngredient
        ) {
            this.inverseTag = inverseTag;
            this.itemDurability = itemDurability;
            this.miningSpeed = miningSpeed;
            this.attackDamage = attackDamage;
            this.enchantability = enchantability;
            this.repairIngredient = Suppliers.memoize(repairIngredient::get);
        }

        @Override
        public int getUses() {
            return this.itemDurability;
        }

        @Override
        public float getSpeed() {
            return this.miningSpeed;
        }

        @Override
        public float getAttackDamageBonus() {
            return this.attackDamage;
        }

        @Override
        public @NotNull TagKey<Block> getIncorrectBlocksForDrops() {
            return this.inverseTag;
        }

        @Override
        public int getEnchantmentValue() {
            return this.enchantability;
        }

        @Override
        public @NotNull Ingredient getRepairIngredient() {
            return this.repairIngredient.get();
        }

        public String getName() {
            return this.name().toLowerCase();
        }
    }

    public static class Armor {

        public static final RegistrySupplier<ArmorMaterial> WARRIORS_LEATHER = registerArmorMaterial("warriors_leather", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 2);
                            map.put(ArmorItem.Type.LEGGINGS, 4);
                            map.put(ArmorItem.Type.CHESTPLATE, 5);
                            map.put(ArmorItem.Type.HELMET, 2);
                            map.put(ArmorItem.Type.BODY, 1);
                        }),
                        15, SoundEvents.ARMOR_EQUIP_LEATHER,
                        () -> Ingredient.of(Items.LEATHER),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("warriors_leather"))),
                        0.0f, 0.0f
                )
        );
        public static final RegistrySupplier<ArmorMaterial> MANTICORE = registerArmorMaterial("manticore_grandmaster", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 3);
                            map.put(ArmorItem.Type.LEGGINGS, 4);
                            map.put(ArmorItem.Type.CHESTPLATE, 5);
                            map.put(ArmorItem.Type.HELMET, 2);
                            map.put(ArmorItem.Type.BODY, 1);
                        }),
                        20, SoundEvents.ARMOR_EQUIP_LEATHER,
                        () -> Ingredient.of(
                                TCOTS_Registries.ITEMS.getRegistrar()
                                        .get(TCOTS_Main.id("cured_monster_leather"))
                        ),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("manticore"))),
                        1.0f, 0.0f
                )
        );
        public static final RegistrySupplier<ArmorMaterial> RAVEN = registerArmorMaterial("raven_grandmaster", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 3);
                            map.put(ArmorItem.Type.LEGGINGS, 6);
                            map.put(ArmorItem.Type.CHESTPLATE, 8);
                            map.put(ArmorItem.Type.HELMET, 2);
                            map.put(ArmorItem.Type.BODY, 1);
                        }),
                        25, SoundEvents.ARMOR_EQUIP_LEATHER,
                        () -> Ingredient.of(
                                TCOTS_Registries.ITEMS.getRegistrar()
                                        .get(TCOTS_Main.id("cured_monster_leather"))
                        ),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("raven"))),
                        1.0f, 0.0f
                )
        );

        public static class Horse {

            public static final RegistrySupplier<ArmorMaterial> TUNDRA = registerArmorMaterial("tundra", () ->
                    new ArmorMaterial(
                            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 3);
                                map.put(ArmorItem.Type.LEGGINGS, 6);
                                map.put(ArmorItem.Type.CHESTPLATE, 8);
                                map.put(ArmorItem.Type.HELMET, 3);
                                map.put(ArmorItem.Type.BODY, 4);
                            }),
                            25, SoundEvents.ARMOR_EQUIP_LEATHER,
                            () -> Ingredient.of(Items.LEATHER),
                            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("tundra"))),
                            0f, 0f
                    )
            );
            public static final RegistrySupplier<ArmorMaterial> KNIGHT = registerArmorMaterial("knight", () ->
                    new ArmorMaterial(
                            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                map.put(ArmorItem.Type.BOOTS, 3);
                                map.put(ArmorItem.Type.LEGGINGS, 6);
                                map.put(ArmorItem.Type.CHESTPLATE, 8);
                                map.put(ArmorItem.Type.HELMET, 3);
                                map.put(ArmorItem.Type.BODY, 8);
                            }),
                            25, SoundEvents.ARMOR_EQUIP_IRON,
                            () -> Ingredient.of(Items.IRON_INGOT),
                            List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("knight"))),
                            0f, 0.1f
                    )
            );
        }

        public static void init(){}
    }

    private static RegistrySupplier<ArmorMaterial> registerArmorMaterial(final String id, final Supplier<ArmorMaterial> armorMaterial){
        return TCOTS_Registries.ARMOR_MATERIALS.register(id, armorMaterial);
    }
}
