package TCOTS.items;

import com.google.common.base.Suppliers;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import java.util.function.Supplier;

public enum TCOTS_ToolMaterials implements Tier {

    GVALCHIR(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 9.0f, 3.0f, 20, () -> Ingredient.of(TCOTS_Items_Fabric.BULLVORE_HORN_FRAGMENT)),

    MOONBLADE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 9.0f, 3.0f, 20, () ->
            Ingredient.of(
                    FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                            BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "silver_ingot")) :
                    Items.GOLD_INGOT
            )),

    DYAEBL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 9.0f, 3.0f, 20, () ->
            Ingredient.of(
                    FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                            BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_ingot")) :
                    Items.IRON_INGOT)),

    WINTERS_BLADE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0f, 4.0f, 30, () ->
            Ingredient.of(
                    FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                            BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_steel_ingot")) :
                    Items.NETHERITE_INGOT)),

    ARDAENYE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1400, 9.0f, 4.0f, 20, () ->
            Ingredient.of(
                    FabricLoader.getInstance().isModLoaded("witcher_rpg")?
                            BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_steel_ingot")) :
                    Items.DIAMOND)),

    ANCHOR(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 9.0f, 8.0f, 5, () -> Ingredient.of(Items.IRON_BLOCK))

    ;


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
    public TagKey<Block> getIncorrectBlocksForDrops() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
