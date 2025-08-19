package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.advancements.criterion.DestroyMultipleMonsterNestsCriterion;
import TCOTS.advancements.criterion.GetTrollFollowerCriterion;
import TCOTS.advancements.criterion.TCOTS_CustomCriterion;
import TCOTS.items.concoctions.WitcherBombs_Base;
import TCOTS.items.concoctions.WitcherMonsterOil_Base;
import TCOTS.items.concoctions.WitcherPotions_Base;
import TCOTS.registry.TCOTS_Blocks;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class TCOTS_AdvancementsGenerator extends AdvancementProvider {

    //Good names for Advancements
    // Let's Cook!

    //Advancements:
    //Alchemy
    //x Craft the Alchemy Table (The Mother of all Sciences)
    //  x Create a potion (Strong Beverage)
    //      x Create a LV3 potion (Practicum in Advanced Alchemy)
    //      x Create a decoction (Taste of Monstrosity)
    //      x Achieve 100% toxicity (Can Quit Anytime I Want)
    //  x Create an Oil (Honing the Blade)
    //      x Create a LV3 Oil (A Powerful Wax)
    //      x Craft and use a Hanged Man's Venom (...Steel for Humans)
    //  x Create a bomb (Ka-boom!)
    //      x Create a LV3 bomb (Bombastic)
    //      x Destroy a nest using a bomb (Fire in the Hole)
    //          x Destroy 20 different nests (Pest Control)
    //      x Craft all the bombs (Bombardier)
    //      x Ignite a Dragon's Dream bomb using a burning opponent (That Is the Evilest Thing)
    //      x Use a moon dust on a creeper to disable its explosion forever (Successful Gardener)
    //  x Use an alchemy recipe (Let's Cook!)
    //  x Refill a concoction in the table or by sleeping (Deep Meditation)


    //Hunting
    //x Kill a monster (Silver for Monsters...)
    //x Find and defeat an Ice Giant (The Lord of Ice)
    //  x Kill a Bullvore (Moo-rderer)
    //      x Craft the G'valchir (Won't Hurt a Bit)
    //  x Kill a rotfiend/scurver without causing an explosion (Bomb Defusal)
    //  x Get a mutagen (Mutagenic)
    //  x Befriend a troll (Friend of Trolls)
    //      x Befriend an ice troll (Lots eats, lots drink)
    //          > Get the three types of Trolls following you at the same time (Troll Trouble)

    //  > Craft crossbows bolts (Marksman)

    //  > Craft the Raven armor (Tyen'sail)
    //  > Craft a horse armor
    //  > Get Aerondight (Embodiment of the Five Virtues)

    public TCOTS_AdvancementsGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider, final ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new Generator()));
    }

    private static final class Generator implements AdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(final HolderLookup.@NotNull Provider registries, final @NotNull Consumer<AdvancementHolder> consumer, final @NotNull ExistingFileHelper existingFileHelper) {
            //Start recipes
            this.generateBasicRecipeAdvancement("swallow_potion", consumer);
            this.generateBasicRecipeAdvancement("cat_potion", consumer);

            this.generateBasicRecipeAdvancement("samum", consumer);
            this.generateBasicRecipeAdvancement("grapeshot", consumer);

            this.generateBasicRecipeAdvancement("oil_necrophage", consumer);
            this.generateBasicRecipeAdvancement("oil_specter", consumer);

            this.generateBasicRecipeAdvancement("dwarven_spirit", consumer);
            this.generateBasicRecipeAdvancement("alcohest", consumer);


            final AdvancementHolder rootAdvancementWitcher = Advancement.Builder.advancement()
                    .display(
                            TCOTS_Items.WITCHER_BESTIARY.get(), // The display icon
                            Component.translatable("advancements.witcher.main.title"), // The title
                            Component.translatable("advancements.witcher.main.description"), // The description
                            ResourceLocation.parse("textures/gui/advancements/backgrounds/stone.png"), // Background image used
                            AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                            false, // Show toast top right
                            false, // Announce to chat
                            false // Hidden in the advancement tab
                    )
                    // The first string used in criterion is the name referenced by other advancements when they want to have 'requirements'
                    .addCriterion("start_mod", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE))
                    .save(consumer, TCOTS_Main.MOD_ID + "/root");

            //Hunting Branch
            {
                final AdvancementHolder rootHunting =
                        requireListedMobsKilled(Advancement.Builder.advancement(), MONSTERS)
                                .parent(rootAdvancementWitcher)
                                .display(TCOTS_Items.GRAVEIR_BONE.get(), // The display icon
                                        Component.translatable("advancements.witcher.start_killing.title"), // The title
                                        Component.translatable("advancements.witcher.start_killing.description"), // The description
                                        null,
                                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .save(consumer, TCOTS_Main.MOD_ID + "/hunting");


                Advancement.Builder.advancement()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.WINTERS_BLADE.get(), // The display icon
                                Component.translatable("advancements.witcher.kill_giant.title"), // The title
                                Component.translatable("advancements.witcher.kill_giant.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("kill_giant", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceGiant())))
                        .rewards(AdvancementRewards.Builder.experience(100))
                        .save(consumer, TCOTS_Main.MOD_ID + "/kill_giant");

                //Bullvore branch
                {
                    final AdvancementHolder killBullvore =
                            Advancement.Builder.advancement()
                                    .parent(rootHunting)
                                    .display(
                                            TCOTS_Items.BULLVORE_HORN_FRAGMENT.get(), // The display icon
                                            Component.translatable("advancements.witcher.kill_bullvore.title"), // The title
                                            Component.translatable("advancements.witcher.kill_bullvore.description"), // The description
                                            null,
                                            AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                            true, // Show toast top right
                                            true, // Announce to chat
                                            false // Hidden in the advancement tab
                                    )
                                    .addCriterion("kill_bullvore", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(TCOTS_Entities.Bullvore())))
                                    .save(consumer, TCOTS_Main.MOD_ID + "/kill_bullvore");

                    Advancement.Builder.advancement()
                            .parent(killBullvore)
                            .display(
                                    TCOTS_Items.GVALCHIR.get(), // The display icon
                                    Component.translatable("advancements.witcher.get_gvalchir.title"), // The title
                                    Component.translatable("advancements.witcher.get_gvalchir.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("get_gvalchir", InventoryChangeTrigger.TriggerInstance.hasItems(TCOTS_Items.GVALCHIR.get()))
                            .rewards(AdvancementRewards.Builder.experience(100))
                            .save(consumer, TCOTS_Main.MOD_ID + "/get_gvalchir");
                }


                Advancement.Builder.advancement()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.ROTFIEND_BLOOD.get(), // The display icon
                                Component.translatable("advancements.witcher.kill_rotfiend.title"), // The title
                                Component.translatable("advancements.witcher.kill_rotfiend.description"), // The description
                                null,
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("kill_rotfiend", TCOTS_CustomCriterion.Conditions.createKillRotfiendCriterion())
                        .save(consumer, TCOTS_Main.MOD_ID + "/kill_rotfiend");

                setHasItemCriteriaOR(Advancement.Builder.advancement(), MUTAGEN)
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.FOGLET_MUTAGEN.get(), // The display icon
                                Component.translatable("advancements.witcher.get_mutagen.title"), // The title
                                Component.translatable("advancements.witcher.get_mutagen.description"), // The description
                                null,
                                AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .save(consumer, TCOTS_Main.MOD_ID + "/get_mutagen");

                //Troll branch
                {
                    final AdvancementHolder befriendTroll =Advancement.Builder.advancement()
                            .parent(rootHunting)
                            .display(
                                    TCOTS_Items.VILLAGE_HERBAL.get(), // The display icon
                                    Component.translatable("advancements.witcher.befriend_troll.title"), // The title
                                    Component.translatable("advancements.witcher.befriend_troll.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("befriend_rock", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.RockTroll())))
                            .requirements(AdvancementRequirements.Strategy.OR)
                            .addCriterion("befriend_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceTroll())))
                            .requirements(AdvancementRequirements.Strategy.OR)
                            .addCriterion("befriend_forest", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.ForestTroll())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/befriend_troll");

                    final AdvancementHolder befriendIceTroll = Advancement.Builder.advancement()
                            .parent(befriendTroll)
                            .display(
                                    Blocks.PACKED_ICE, // The display icon
                                    Component.translatable("advancements.witcher.befriend_troll_ice.title"), // The title
                                    Component.translatable("advancements.witcher.befriend_troll_ice.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("befriend_troll_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceTroll())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/befriend_troll_ice");

                    Advancement.Builder.advancement()
                            .parent(befriendIceTroll)
                            .display(
                                    Blocks.OAK_SAPLING, // The display icon
                                    Component.translatable("advancements.witcher.befriend_all_troll.title"), // The title
                                    Component.translatable("advancements.witcher.befriend_all_troll.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion("befriend_rock", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.RockTroll())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion("befriend_ice", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.IceTroll())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion("befriend_forest", GetTrollFollowerCriterion.Conditions.create(EntityPredicate.Builder.entity().of(TCOTS_Entities.ForestTroll())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/befriend_all_troll");
                }

                Advancement.Builder.advancement()
                        .parent(rootHunting)
                        .display(
                                TCOTS_Items.RAVENS_ARMOR.get(), // The display icon
                                Component.translatable("advancements.witcher.get_ravens_armor.title"), // The title
                                Component.translatable("advancements.witcher.get_ravens_armor.description"), // The description
                                null,
                                AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion("get_ravens_armor", InventoryChangeTrigger.TriggerInstance.hasItems(TCOTS_Items.RAVENS_ARMOR.get(), TCOTS_Items.RAVENS_TROUSERS.get(), TCOTS_Items.RAVENS_BOOTS.get()))
                        .rewards(AdvancementRewards.Builder.experience(100))
                        .save(consumer, TCOTS_Main.MOD_ID + "/get_ravens_armor");
            }


            //Alchemy Branch
            {
                final AdvancementHolder rootAlchemy =
                        Advancement.Builder.advancement()
                                .parent(rootAdvancementWitcher)
                                .display(
                                        TCOTS_Items.ALCHEMY_TABLE_ITEM.get(), // The display icon
                                        Component.translatable("advancements.witcher.start_alchemy.title"), // The title
                                        Component.translatable("advancements.witcher.start_alchemy.description"), // The description
                                        null,
                                        AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                        true, // Show toast top right
                                        true, // Announce to chat
                                        false // Hidden in the advancement tab
                                )
                                .addCriterion("start_alchemy", InventoryChangeTrigger.TriggerInstance.hasItems(TCOTS_Blocks.AlchemyTable()))
                                .save(consumer, TCOTS_Main.MOD_ID + "/alchemy");

                //Potions branch
                {
                    final AdvancementHolder craftPotion = setHasItemCriteriaOR(Advancement.Builder.advancement(), POTIONS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.SWALLOW_POTION.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_potion.title"), // The title
                                    Component.translatable("advancements.witcher.craft_potion.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_potion");


                    setHasItemCriteriaOR(Advancement.Builder.advancement(), DECOCTIONS)
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.GRAVE_HAG_DECOCTION.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_decoction.title"), // The title
                                    Component.translatable("advancements.witcher.craft_decoction.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_decoction");

                    setHasItemCriteriaOR(Advancement.Builder.advancement(), POTION_LV3)
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.SWALLOW_POTION_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_potion_superior.title"), // The title
                                    Component.translatable("advancements.witcher.craft_potion_superior.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_potion_superior");

                    Advancement.Builder.advancement()
                            .parent(craftPotion)
                            .display(
                                    TCOTS_Items.NEST_SKULL_ITEM.get(), // The display icon
                                    Component.translatable("advancements.witcher.max_toxicity.title"), // The title
                                    Component.translatable("advancements.witcher.max_toxicity.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "max_toxicity",
                                    TCOTS_CustomCriterion.Conditions.createMaxToxicityCriterion())
                            .save(consumer, TCOTS_Main.MOD_ID + "/max_toxicity");
                }

                //Oils branch
                {
                    final AdvancementHolder craftOil = setHasItemCriteriaOR(Advancement.Builder.advancement(), OILS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.NECROPHAGE_OIL.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_oil.title"), // The title
                                    Component.translatable("advancements.witcher.craft_oil.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_oil");

                    setHasItemCriteriaOR(Advancement.Builder.advancement(), OILS_LV3)
                            .parent(craftOil)
                            .display(
                                    TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_oil_superior.title"), // The title
                                    Component.translatable("advancements.witcher.craft_oil_superior.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_oil_superior");

                    Advancement.Builder.advancement()
                            .parent(craftOil)
                            .display(
                                    Items.IRON_SWORD, // The display icon
                                    Component.translatable("advancements.witcher.kill_with_hanged.title"), // The title
                                    Component.translatable("advancements.witcher.kill_with_hanged.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "kill_with_hanged",
                                    TCOTS_CustomCriterion.Conditions.createKillWithHangedCriterion())
                            .save(consumer, TCOTS_Main.MOD_ID + "/kill_with_hanged");
                }

                //Bombs branch
                {
                    final AdvancementHolder craftBomb = setHasItemCriteriaOR(Advancement.Builder.advancement(), BOMBS)
                            .parent(rootAlchemy)
                            .display(
                                    TCOTS_Items.GRAPESHOT.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_bomb.title"), // The title
                                    Component.translatable("advancements.witcher.craft_bomb.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_bomb");

                    final AdvancementHolder craftBombLV3 = setHasItemCriteriaOR(Advancement.Builder.advancement(), BOMBS_LV3)
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.GRAPESHOT_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_bomb_superior.title"), // The title
                                    Component.translatable("advancements.witcher.craft_bomb_superior.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_bomb_superior");

                    Advancement.Builder.advancement()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.DIMERITIUM_BOMB.get(), // The display icon
                                    Component.translatable("advancements.witcher.craft_all_bomb.title"), // The title
                                    Component.translatable("advancements.witcher.craft_all_bomb.description"), // The description
                                    null,
                                    AdvancementType.GOAL, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )

                            .addCriterion(
                                    "craft_grapeshot",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.GRAPESHOT.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_samum",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.SAMUM.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_dancing",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DANCING_STAR.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_puffball",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DEVILS_PUFFBALL.get())))
                            .requirements(AdvancementRequirements.Strategy.AND)
                            .addCriterion(
                                    "craft_dragons",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DRAGONS_DREAM.get())))
                            .addCriterion(
                                    "craft_northern",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.NORTHERN_WIND.get())))
                            .addCriterion(
                                    "craft_dimeritium",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.DIMERITIUM_BOMB.get())))
                            .addCriterion(
                                    "craft_moon_dust",
                                    RecipeCraftedTrigger.TriggerInstance.craftedItem(BuiltInRegistries.ITEM.getKey(TCOTS_Items.MOON_DUST.get())))
                            .save(consumer, TCOTS_Main.MOD_ID + "/craft_all_bomb");

                    final AdvancementHolder explodeNest = Advancement.Builder.advancement()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Blocks.MonsterNest(), // The display icon
                                    Component.translatable("advancements.witcher.destroy_nest.title"), // The title
                                    Component.translatable("advancements.witcher.destroy_nest.description"), // The description
                                    null,
                                    AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "destroy_nest",
                                    TCOTS_CustomCriterion.Conditions.createDestroyNestCriterion())
                            .save(consumer, TCOTS_Main.MOD_ID + "/destroy_nest");

                    Advancement.Builder.advancement()
                            .parent(explodeNest)
                            .display(
                                    TCOTS_Blocks.MonsterNest(), // The display icon
                                    Component.translatable("advancements.witcher.destroy_nest_multiple.title"), // The title
                                    Component.translatable("advancements.witcher.destroy_nest_multiple.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "destroy_nest_multiple",
                                    DestroyMultipleMonsterNestsCriterion.Conditions.createMultipleDestroyNestCriterion(30))
                            .rewards(AdvancementRewards.Builder.experience(500))
                            .save(consumer, TCOTS_Main.MOD_ID + "/destroy_nest_multiple");

                    Advancement.Builder.advancement()
                            .parent(craftBomb)
                            .display(
                                    TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.dragons_dream_burning.title"), // The title
                                    Component.translatable("advancements.witcher.dragons_dream_burning.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "dragons_dream_burning",
                                    TCOTS_CustomCriterion.Conditions.createDragonsDreamBurningCriterion())
                            .rewards(AdvancementRewards.Builder.experience(150))
                            .save(consumer, TCOTS_Main.MOD_ID + "/dragons_dream_burning");

                    Advancement.Builder.advancement()
                            .parent(craftBombLV3)
                            .display(
                                    TCOTS_Items.MOON_DUST_SUPERIOR.get(), // The display icon
                                    Component.translatable("advancements.witcher.stop_creeper.title"), // The title
                                    Component.translatable("advancements.witcher.stop_creeper.description"), // The description
                                    null,
                                    AdvancementType.CHALLENGE, // Options: TASK, CHALLENGE, GOAL
                                    true, // Show toast top right
                                    true, // Announce to chat
                                    false // Hidden in the advancement tab
                            )
                            .addCriterion(
                                    "stop_creeper",
                                    TCOTS_CustomCriterion.Conditions.createStopCreeperCriterion())
                            .rewards(AdvancementRewards.Builder.experience(150))
                            .save(consumer, TCOTS_Main.MOD_ID + "/stop_creeper");
                }

                final AdvancementHolder useAlchemyFormula = Advancement.Builder.advancement()
                        .parent(rootAlchemy)
                        .display(
                                TCOTS_Items.ALCHEMY_FORMULA.get(), // The display icon
                                Component.translatable("advancements.witcher.use_formula.title"), // The title
                                Component.translatable("advancements.witcher.use_formula.description"), // The description
                                null, // Background image used
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion(
                                "use_formula",
                                ConsumeItemTrigger.TriggerInstance.usedItem(TCOTS_Items.ALCHEMY_FORMULA.get()))
                        .save(consumer, TCOTS_Main.MOD_ID + "/use_formula");

                Advancement.Builder.advancement()
                        .parent(rootAlchemy)
                        .display(
                                TCOTS_Items.ALCOHEST.get(), // The display icon
                                Component.translatable("advancements.witcher.refill_concoction.title"), // The title
                                Component.translatable("advancements.witcher.refill_concoction.description"), // The description
                                null, // Background image used
                                AdvancementType.TASK, // Options: TASK, CHALLENGE, GOAL
                                true, // Show toast top right
                                true, // Announce to chat
                                false // Hidden in the advancement tab
                        )
                        .addCriterion(
                                "refill_concoction",
                                TCOTS_CustomCriterion.Conditions.createRefillConcoctionCriterion())
                        .save(consumer, TCOTS_Main.MOD_ID + "/refill_concoction");
            }
        }

        @SuppressWarnings("all")
        private void generateBasicRecipeAdvancement(String id, Consumer<AdvancementHolder> consumer){
            Advancement.Builder.recipeAdvancement()
                    .addCriterion(RecipeProvider.getHasName(TCOTS_Items.ALCHEMY_TABLE_ITEM.get()), RecipeProvider.has(TCOTS_Items.ALCHEMY_TABLE_ITEM.get()))
                    .requirements(AdvancementRequirements.Strategy.OR)
                    .addCriterion(RecipeProvider.getHasName(TCOTS_Items.ALCHEMY_BOOK.get()), RecipeProvider.has(TCOTS_Items.ALCHEMY_BOOK.get()))
                    .rewards(AdvancementRewards.Builder.recipe(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id)))
                    .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)
                    .save(consumer, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"recipes/alchemy")+"/"+id);
        }

        private static final List<? extends EntityType<?>> MONSTERS =
                BuiltInRegistries.ENTITY_TYPE.entrySet().stream()
                        .filter(e -> {
                            ResourceLocation location =e.getKey().location();

                            return location.getNamespace().equals(TCOTS_Main.MOD_ID) &&
                                    !(location.getPath().contains("bolt")
                                            || location.getPath().contains("cloud")
                                            || location.getPath().contains("bomb")
                                            || location.getPath().contains("projectile")
                                            || location.getPath().contains("puddle")
                                            || location.getPath().contains("fogling")
                                            || location.getPath().contains("mud_ball")
                                            || location.getPath().contains("spike"));
                        })
                        .map(Map.Entry::getValue)
                        .toList();

        private static final List<Item> MUTAGEN =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID) && e.getKey().location().getPath().contains("mutagen"))
                        .map(Map.Entry::getValue)
                        .toList();

        private static final List<Item> BOMBS_LV3 =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherBombs_Base bomb && bomb.getLevel() >=2)
                        .toList();

        private static final List<Item> BOMBS =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherBombs_Base)
                        .toList();

        private static final List<Item> OILS_LV3 =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherMonsterOil_Base oil && oil.getLevel() >=3)
                        .toList();

        private static final List<Item> OILS =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherMonsterOil_Base)
                        .toList();

        private static final List<Item> POTION_LV3=
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherPotions_Base potion && potion.getStatusEffect().getAmplifier()>=2)
                        .toList();

        private static final List<Item> POTIONS =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherPotions_Base)
                        .toList();

        private static final List<Item> DECOCTIONS =
                BuiltInRegistries.ITEM.entrySet().stream()
                        .filter(e -> e.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                        .map(Map.Entry::getValue)
                        .filter(item -> item instanceof WitcherPotions_Base potion && potion.isDecoction())
                        .toList();

        @SuppressWarnings("all")
        private static Advancement.Builder requireListedMobsKilled(Advancement.Builder builder, List<? extends EntityType<?>> entityTypes) {
            entityTypes.forEach(type -> builder.addCriterion(
                            BuiltInRegistries.ENTITY_TYPE.getKey(type).toString(),
                            KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(type)))
                    .requirements(AdvancementRequirements.Strategy.OR));
            return builder;
        }

        private static Advancement.Builder setHasItemCriteriaOR(final Advancement.Builder builder, final List<Item> items) {
            items.forEach(item -> builder.addCriterion(
                            BuiltInRegistries.ITEM.getKey(item).toString(),
                            InventoryChangeTrigger.TriggerInstance.hasItems(item))
                    .requirements(AdvancementRequirements.Strategy.OR));
            return builder;
        }
    }
}
