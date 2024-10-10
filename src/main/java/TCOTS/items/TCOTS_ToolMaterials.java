package TCOTS.items;

import net.fabricmc.yarn.constants.MiningLevels;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum TCOTS_ToolMaterials implements ToolMaterial {

    GVALCHIR(MiningLevels.DIAMOND, 800, 9.0f, 3.0f, 20, () -> Ingredient.ofItems(TCOTS_Items.BULLVORE_HORN_FRAGMENT)),

    MOONBLADE(MiningLevels.DIAMOND, 800, 9.0f, 3.0f, 20, () -> Ingredient.ofItems(Items.GOLD_INGOT)),

    WINTERS_BLADE(MiningLevels.NETHERITE, 2031, 9.0f, 4.0f, 30, () -> Ingredient.ofItems(Items.NETHERITE_INGOT))
    ;



    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    TCOTS_ToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
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
    public int getMiningLevel() {
        return this.miningLevel;
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
