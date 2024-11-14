package TCOTS.items;

import com.google.common.base.Suppliers;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

public enum TCOTS_ToolMaterials implements ToolMaterial {

    GVALCHIR(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 9.0f, 3.0f, 20, () -> Ingredient.ofItems(TCOTS_Items.BULLVORE_HORN_FRAGMENT)),

    MOONBLADE(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 9.0f, 3.0f, 20, () -> Ingredient.ofItems(Items.GOLD_INGOT)),

    DYAEBL(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 800, 9.0f, 3.0f, 20, () -> Ingredient.ofItems(Items.IRON_INGOT)),

    WINTERS_BLADE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0f, 4.0f, 30, () -> Ingredient.ofItems(Items.NETHERITE_INGOT)),

    ARDAENYE(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1400, 9.0f, 4.0f, 20, () -> Ingredient.ofItems(Items.DIAMOND)),

    ANCHOR(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 100, 9.0f, 8.0f, 5, () -> Ingredient.ofItems(Items.IRON_BLOCK))

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
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
