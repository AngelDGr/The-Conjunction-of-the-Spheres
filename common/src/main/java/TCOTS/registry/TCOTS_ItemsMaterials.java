package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.utils.MiscUtil;
import com.google.common.base.Suppliers;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class TCOTS_ItemsMaterials {

    public static Supplier<Ingredient> repairItemMoonblade(){
        return () ->
                Ingredient.of(
                        MiscUtil.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "silver_ingot")) :
                                Items.GOLD_INGOT
                );
    }

    public static Supplier<Ingredient> repairItemDyaebl(){
        return () ->
                Ingredient.of(
                        MiscUtil.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_ingot")) :
                                Items.IRON_INGOT);
    }

    public static Supplier<Ingredient> repairItemWintersBlade(){
        return () ->
                Ingredient.of(
                        MiscUtil.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_steel_ingot")) :
                                Items.NETHERITE_INGOT);
    }

    public static Supplier<Ingredient> repairItemArdaenye(){
        return () ->
                Ingredient.of(
                        MiscUtil.isWitcherRPGLoaded() ?
                                BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_steel_ingot")) :
                                Items.DIAMOND);
    }

    public enum TCOTS_ToolMaterials implements Tier {

        GVALCHIR(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 0.0f, 3.0f, 20, () -> Ingredient.of(
                TCOTS_Registries.ITEMS.getRegistrar().get(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bullvore_horn_fragment")))),

        MOONBLADE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 0.0f, 3.0f, 20, TCOTS_ItemsMaterials.repairItemMoonblade()),

        DYAEBL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 0.0f, 3.0f, 20, TCOTS_ItemsMaterials.repairItemDyaebl()),

        WINTERS_BLADE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0f, 4.0f, 30, TCOTS_ItemsMaterials.repairItemWintersBlade()),

        ARDAENYE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1400, 9.0f, 4.0f, 20, TCOTS_ItemsMaterials.repairItemArdaenye()),

        ANCHOR(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 9.0f, 8.0f, 5, () -> Ingredient.of(Items.IRON_BLOCK));


        private final TagKey<Block> inverseTag;
        private final int itemDurability;
        private final float miningSpeed;
        private final float attackDamage;
        private final int enchantability;
        private final Supplier<Ingredient> repairIngredient;

        TCOTS_ToolMaterials(
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
    }

    public static Tier Gvalchir(){
        return TCOTS_ToolMaterials.GVALCHIR;
    }

    public static Tier Moonblade(){
        return TCOTS_ToolMaterials.MOONBLADE;
    }

    public static Tier Dyaebl(){
        return TCOTS_ToolMaterials.DYAEBL;
    }

    public static Tier WintersBlade(){
        return TCOTS_ToolMaterials.WINTERS_BLADE;
    }

    public static Tier Ardaenye(){
        return TCOTS_ToolMaterials.ARDAENYE;
    }

    public static Tier Anchor(){
        return TCOTS_ToolMaterials.ANCHOR;
    }

    public static RegistrySupplier<ArmorMaterial> WARRIORS_LEATHER;

    public static RegistrySupplier<ArmorMaterial> MANTICORE;

    public static RegistrySupplier<ArmorMaterial> RAVEN;

    public static RegistrySupplier<ArmorMaterial> TUNDRA;

    public static RegistrySupplier<ArmorMaterial> KNIGHT;

    public static void registerArmorMaterials(){

        WARRIORS_LEATHER= registerArmorMaterial("warriors_leather", () ->
                        new ArmorMaterial(
                                Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                                    map.put(ArmorItem.Type.BOOTS, 1);
                                    map.put(ArmorItem.Type.LEGGINGS, 4);
                                    map.put(ArmorItem.Type.CHESTPLATE, 5);
                                    map.put(ArmorItem.Type.HELMET, 2);
                                    map.put(ArmorItem.Type.BODY, 1);
                                }),
                                15, SoundEvents.ARMOR_EQUIP_LEATHER, ()->Ingredient.of(Items.LEATHER),
                                List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("warriors_leather"))), 0.0f, 0.0f));

        MANTICORE= registerArmorMaterial("manticore", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 2);
                            map.put(ArmorItem.Type.LEGGINGS, 5);
                            map.put(ArmorItem.Type.CHESTPLATE, 6);
                            map.put(ArmorItem.Type.HELMET, 2);
                            map.put(ArmorItem.Type.BODY, 1);
                        }),
                        20, SoundEvents.ARMOR_EQUIP_LEATHER, ()->Ingredient.of(TCOTS_Registries.ITEMS.getRegistrar().get(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cured_monster_leather"))),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("manticore"))), 1.0f, 0.0f));

        RAVEN= registerArmorMaterial("raven", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 3);
                            map.put(ArmorItem.Type.LEGGINGS, 6);
                            map.put(ArmorItem.Type.CHESTPLATE, 8);
                            map.put(ArmorItem.Type.HELMET, 3);
                            map.put(ArmorItem.Type.BODY, 1);
                        }),
                        25, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(TCOTS_Registries.ITEMS.getRegistrar().get(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cured_monster_leather"))),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("raven"))), 2.0f, 0.1f));

        TUNDRA= registerArmorMaterial("tundra", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 3);
                            map.put(ArmorItem.Type.LEGGINGS, 6);
                            map.put(ArmorItem.Type.CHESTPLATE, 8);
                            map.put(ArmorItem.Type.HELMET, 3);
                            map.put(ArmorItem.Type.BODY, 4);
                        }),
                        25, SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(Items.LEATHER),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("tundra"))), 0f, 0));

        KNIGHT= registerArmorMaterial("knight", () ->
                new ArmorMaterial(
                        Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                            map.put(ArmorItem.Type.BOOTS, 3);
                            map.put(ArmorItem.Type.LEGGINGS, 6);
                            map.put(ArmorItem.Type.CHESTPLATE, 8);
                            map.put(ArmorItem.Type.HELMET, 3);
                            map.put(ArmorItem.Type.BODY, 8);
                        }),
                        25, SoundEvents.ARMOR_EQUIP_IRON, () -> Ingredient.of(Items.IRON_INGOT),
                        List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace("knight"))), 0f, 0.1f));
    }

    public static RegistrySupplier<ArmorMaterial> Manticore(){
        return MANTICORE;
    }

    public static RegistrySupplier<ArmorMaterial> WarriorsLeather(){
        return WARRIORS_LEATHER;
    }

    public static RegistrySupplier<ArmorMaterial> Raven(){
        return RAVEN;
    }

    public static RegistrySupplier<ArmorMaterial> HorseTundra(){
        return TUNDRA;
    }

    public static RegistrySupplier<ArmorMaterial> HorseKnight(){
        return KNIGHT;
    }

    private static RegistrySupplier<ArmorMaterial> registerArmorMaterial(String id, Supplier<ArmorMaterial> armorMaterial){
        return TCOTS_Registries.ARMOR_MATERIALS.register(id, armorMaterial);
    }

}
