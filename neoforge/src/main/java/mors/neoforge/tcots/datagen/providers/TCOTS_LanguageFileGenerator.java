package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.TCOTS_Registries;
import mors.tcots.items.concoctions.WitcherBombs_Base;
import mors.tcots.items.concoctions.WitcherMonsterOil_Base;
import mors.tcots.items.concoctions.WitcherPotions_Base;
import mors.tcots.registry.TCOTS_Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TCOTS_LanguageFileGenerator extends LanguageProvider {

    public TCOTS_LanguageFileGenerator(final PackOutput output) {
        super(output, TCOTS_Main.MOD_ID, "en_us");
    }

    private static final Map<String, String> DICTIONARY = Map.ofEntries(
            Map.entry("ghoul_blood", "Ghoul's Blood"),
            Map.entry("white_raffards", "White Raffard's"),
            Map.entry("winters_blade_skeleton", "Warrior's Skeleton"),
            Map.entry("skeleton_block", "Old Skeleton"),
            Map.entry("crows_eye", "Crow's Eye"),
            Map.entry("stammelfords", "Stammelford's"),
            Map.entry("alchemists", "Alchemists'"),
            Map.entry("devils_puffball", "Devil's Puffball"),
            Map.entry("dragons_dream", "Dragon's Dream"),
            Map.entry("gvalchir", "G'valchir"),
            Map.entry("dyaebl","D'yaebl"),
            Map.entry("winters_blade","Winter's Blade"),
            Map.entry("ardaenye","Ard'aenye"),


            Map.entry("warriors_leather_head","Warrior's Leather Band"),
            Map.entry("warriors_leather_jacket","Warrior's Leather Suit"),
            Map.entry("warriors_leather_trousers","Warrior's Leather Pants"),
            Map.entry("warriors","Warrior's"),

            Map.entry("manticore_head","Manticore Band"),
            Map.entry("manticore_armor","Manticore Suit"),
            Map.entry("manticore_trousers","Manticore Pants"),

            Map.entry("ravens_head","Raven's Band"),
            Map.entry("ravens_armor","Raven's Suit"),
            Map.entry("ravens_trousers","Raven's Pants"),
            Map.entry("ravens","Raven's"),

            Map.entry("knight_errants","Knight Errant's"),
            Map.entry("hanged_oil","Hanged Man's Venom"),
            Map.entry("water_hag_mud_ball","Mudball"),
            Map.entry("empty_witcher_potion","Empty %s"),
            Map.entry("witcher_bestiary","Monster Bestiary"),
            Map.entry("alchemy_book","Alchemy Almanac")
    );

    private static final List<Map.Entry<String, String>> SORTED_DICTIONARY =
            DICTIONARY.entrySet().stream()
                    .sorted((a, b) -> Integer.compare(
                            b.getKey().split("_").length,
                            a.getKey().split("_").length
                    ))
                    .toList();

    @Override
    protected void addTranslations() {
        add("itemGroup.tcots_witcher.main", "The Conjunction of the Spheres");
        add("itemGroup.tcots_witcher.main.tab.combat", "TCOTS - Combat");
        add("itemGroup.tcots_witcher.main.tab.alchemy", "TCOTS - Alchemy");
        add("itemGroup.tcots_witcher.main.tab.formulae", "TCOTS - Formulae");
        add("itemGroup.tcots_witcher.main.button.github", "GitHub");
        add("itemGroup.tcots_witcher.main.button.curseforge", "CurseForge");
        add("itemGroup.tcots_witcher.main.button.modrinth", "Modrinth");

        TCOTS_Registries.ENTITY_TYPES.getRegistrar().entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                .forEach((entry)->addEntityTranslation(entry.getKey()));

        TCOTS_Registries.ITEMS.getRegistrar().entrySet().stream()
                .filter(entry ->
                        entry.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID)
                        && !(entry.getValue() instanceof BlockItem && !(entry.getValue() instanceof ItemNameBlockItem)))
                .forEach((entry)-> addItemTranslation(entry.getKey()));

        TCOTS_Registries.BLOCKS.getRegistrar().entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                .forEach((entry)-> addBlockTranslation(entry.getKey(), TCOTS_Blocks.NestWallSkull()));


        TCOTS_Registries.MOB_EFFECTS.getRegistrar().entrySet().stream()
                .filter(entry -> entry.getKey().location().getNamespace().equals(TCOTS_Main.MOD_ID))
                .forEach((entry)-> addEffectTranslation(entry.getKey()));

        //Tooltips
        {
            //Items & Blocks
            {
                add("item.tcots_witcher.witcher_bestiary.tooltip", "Learn about the dangers of this new world");
                add("item.tcots_witcher.alchemy_book.tooltip", "Information about herbs and alchemy");
                add("block.tcots_witcher.alchemy_table.tooltip", "Station to make Witcher Potions");
                add("block.tcots_witcher.alchemy_table.tooltip.book", "Put an Alchemy Almanac on it");
                add("block.tcots_witcher.herbal_table.tooltip", "Put organic matter in it");

                add("block.tcots_witcher.skeleton_block.tooltip1", "Creative Only");
                add("block.tcots_witcher.skeleton_block.tooltip2", "Change States with Debug Stick");
            }

            //Gui
            {
                add("effect.tcots_witcher.grave_hag_decoction.gui", "Grave Hag Vitality (%s kills)");
                add("gui.tcots_witcher.herbal_table.title", "Create herbal mixtures");

                add("gui.tcots_witcher.message.toxicity_warning", "You can't drink this! Too much toxicity");
                add("gui.tcots_witcher.message.toxicity_danger", "BEWARE! TOO MUCH TOXICITY!");
                add("gui.tcots_witcher.message.recipe_already_know", "You already know that recipe!");

                add("gui.tcots_witcher.message.troll_follows", "%1$s is now following you");
                add("gui.tcots_witcher.message.troll_waits", "%1$s will wait for you, guarding this position");
                add("gui.tcots_witcher.message.troll_wandering", "%1$s is not longer your follower");

                add("gui.tcots_witcher.recipe_book.tooltip.alchemy_table", "Recipe Book");
                add("gui.tcots_witcher.recipe_book.category.potions", "Potions");
                add("gui.tcots_witcher.recipe_book.category.decoctions", "Decoctions");
                add("gui.tcots_witcher.recipe_book.category.bombs_oils", "Bombs & Oils");
                add("gui.tcots_witcher.recipe_book.category.misc", "Ingredients");

                add("gui.tcots_witcher.tooltip.formula", "%s Formula");
                add("gui.tcots_witcher.tooltip.max_stack", "Max stack: %s");
                add("gui.tcots_witcher.tooltip.oil_damage", "Damage: +%s");

                add("gui.tcots_witcher.oil.tooltip.uses", "Uses: %s");

                add("gui.jei.tcots_witcher.requires_recipe", "Requires unlock recipe");
                add("gui.jei.tcots_witcher.for_herb", "+%ss for each extra herb");
            }

            //Weapons & Armor
            {
                add("item.tcots_witcher.tooltip.dyeable", "Dyeable");
                add("item.tcots_witcher.sword.tooltip.see_more", "[%1$s] to show details");

                //Weapons
                {
                    add("item.tcots_witcher.gvalchir.tooltip", "+%s%% Armor Penetration");

                    add("item.tcots_witcher.moonblade.tooltip", "+%s%% Damage Against monsters");

                    add("item.tcots_witcher.dyaebl.tooltip", "20% Chance to cause bleeding");

                    add("item.tcots_witcher.winters_blade.tooltip1", "+2 Attack Damage to fiery enemies");
                    add("item.tcots_witcher.winters_blade.tooltip2", " Extinguish flames from the user");
                    add("item.tcots_witcher.winters_blade.tooltip3", " 20% Chance to freeze enemies");

                    add("item.tcots_witcher.knight_crossbow.tooltip", "Very strong, with slow charging");

                    add("item.tcots_witcher.base_bolt.tooltip", "Stronger than an arrow");
                    add("item.tcots_witcher.blunt_bolt.tooltip", "Stronger than a regular bolt");
                    add("item.tcots_witcher.precision_bolt.tooltip", "Extra piercing");
                    add("item.tcots_witcher.exploding_bolt.tooltip", "Explodes on impact");
                    add("item.tcots_witcher.broadhead_bolt.tooltip", "Causes bleeding");
                }

                //Armor
                {
                    add("item.tcots_witcher.set.warriors_leather", "Warrior's Leather Armor (%s/%s)");
                    add("item.tcots_witcher.set.raven", "Raven's Armor (%s/%s)");
                    add("item.tcots_witcher.set.manticore", "Manticore School (%s/%s)");

                    add("item.tcots_witcher.armor_set", "(%s) Set: ");

                    add("item.tcots_witcher.set.raven.bonus_full",   "(%s) Set: Monsters below 50%% health are highlighted and take +25%% damage");

                    add("item.tcots_witcher.set.manticore.bonus_full", "(%s) Set: +50%% positive potions duration when drunk");

                    //Horse
                    {
                        add("item.tcots_witcher.tundra_horse_armor.tooltip1", "+20% Speed Movement in Snow and Ice");
                        add("item.tcots_witcher.tundra_horse_armor.tooltip2", " Can ride across Powder Snow");

                        add("item.tcots_witcher.knight_errants_horse_armor.tooltip1", "+40% Projectile Resistance");
                        add("item.tcots_witcher.knight_errants_horse_armor.tooltip2", "+20% Explosion Resistance");
                    }

                }
            }

            //Alchemy
            {
                add("item.tcots_witcher.potion.tooltip.toxicity", "Toxicity: %s");

                add("item.tcots_witcher.oil.against", "Against %s:");
                add("item.tcots_witcher.oil.attack", "+%s Attack Damage");
                add("item.tcots_witcher.oil.duration", "Duration:");
                add("item.tcots_witcher.oil.uses", "%s Hits");

                add("item.tcots_witcher.tooltip.alchemy_formula", "Formula: %s");
                add("item.tcots_witcher.empty_witcher_bottle.tooltip", "Refilled in the Alchemy Table or by sleeping, uses strong alcohol");
                add("item.tcots_witcher.bomb_powder.tooltip.name", "(%s)");
                add("item.tcots_witcher.bomb_powder.tooltip", "Remade in the Alchemy Table or by sleeping, uses strong alcohol");

                add("item.tcots_witcher.alcohol.tooltip.refill", "Refills:");
                add("item.tcots_witcher.alcohol.tooltip.slot", "%s Slot");
                add("item.tcots_witcher.alcohol.tooltip.slots", "%s Slots");
                add("item.tcots_witcher.alcohol.tooltip.slots2", "%s Slots");

                add("item.tcots_witcher.icy_spirit.tooltip", "Usable to create Dwarven Spirit");
                add("item.tcots_witcher.dwarven_spirit.tooltip", "Base to brew most potions");
                add("item.tcots_witcher.alcohest.tooltip", "Used to brew enhanced potions");
                add("item.tcots_witcher.white_gull.tooltip", "Used to brew superior potions");
                add("item.tcots_witcher.village_herbal.tooltip", "Relaxing beverage");
                add("item.tcots_witcher.cherry_cordial.tooltip", "Inspiring and sweet beverage");
                add("item.tcots_witcher.mandrake_cordial.tooltip", "Strong beverage");

                //Bombs
                {
                    add("item.tcots_witcher.bomb.tooltip.monster_nest", "Can destroy Monster Nests and Spawners.");

                    add("item.tcots_witcher.bomb.tooltip.grapeshot", "Creates a tiny explosion that ignites the target.");
                    add("item.tcots_witcher.bomb.tooltip.dancing_star", "Produces a fiery explosion, igniting opponents.");
                    add("item.tcots_witcher.bomb.tooltip.samum0", "Stuns opponents within its explosion radius.");
                    add("item.tcots_witcher.bomb.tooltip.samum1", "Stuns opponents within its explosion radius.");
                    add("item.tcots_witcher.bomb.tooltip.samum2", "Stuns opponents within its explosion radius. First hit it's critical.");
                    add("item.tcots_witcher.bomb.tooltip.devils_puffball", "Releases a cloud of poison when detonated.");
                    add("item.tcots_witcher.bomb.tooltip.northern_wind0", "Freezes foes. Hits on frozen foes deal additional damage.");
                    add("item.tcots_witcher.bomb.tooltip.northern_wind1", "Freezes foes. Hits on frozen foes deal additional damage.");
                    add("item.tcots_witcher.bomb.tooltip.northern_wind2", "Freezes foes. Hits on frozen foes deal additional damage. Chance of instant kill.");
                    add("item.tcots_witcher.bomb.tooltip.dragons_dream", "Releases a cloud of gas that explodes when ignited.");
                    add("item.tcots_witcher.bomb.tooltip.dimeritium_bomb", "Releases a cloud of dimeritium slivers that block magic.");
                    add("item.tcots_witcher.bomb.tooltip.moon_dust0", "Temporarily prevent monsters from transforming and regenerating.");
                    add("item.tcots_witcher.bomb.tooltip.moon_dust1", "Permanently prevent monsters from transforming and regenerating.");
                    add("item.tcots_witcher.bomb.tooltip.moon_dust2", "Permanently prevent monsters from transforming and regenerating. Stop Creepers from exploding");
                }

                //Effects
                {
                    add("effect.tcots_witcher.swallow.tooltip",  "Accelerates health regeneration but it consumes a lot of energy.");
                    add("effect.tcots_witcher.cat.tooltip",  "Grants sight in total darkness. It let you see your enemies.");
                    add("effect.tcots_witcher.white_raffards.tooltip",  "Immediately restores a large percentage of health");
                    add("effect.tcots_witcher.white_raffards.tooltip.special_attribute",  "%s%% Health Restored");
                    add("effect.tcots_witcher.killer_whale.tooltip",  "Increases breath supply and attack while underwater");
                    add("effect.tcots_witcher.killer_whale.tooltip.when_applied",  "In Water:");
                    add("effect.tcots_witcher.black_blood.tooltip0",  "Blood injures undead, necrophages and vampire creatures when they hit the user with a melee attack");
                    add("effect.tcots_witcher.black_blood.tooltip1",  "Blood injures and knocks back undead, necrophages and vampire creatures when they hit the user with a melee attack");
                    add("effect.tcots_witcher.black_blood.tooltip2",  "Blood injures and knocks back undead, necrophages and vampire creatures when they hit the user with a melee attack. Makes nearby monsters bleed");
                    add("effect.tcots_witcher.black_blood.tooltip.when_applied",  "In Monster Melee Hit:");
                    add("effect.tcots_witcher.black_blood.tooltip.special_attribute",  "%s%% Reflected Damage");
                    add("effect.tcots_witcher.maribor_forest.tooltip",  "Regenerates hunger for the time it last, improve consumed food");
                    add("effect.tcots_witcher.wolf.tooltip",  "Improves focus, causing major damage with critical hits");
                    add("effect.tcots_witcher.wolf.tooltip.special_attribute",  "+%s%% Critical Damage");
                    add("effect.tcots_witcher.bindweed.tooltip",  "Reduces damage from poison, wither, and other damage-over-time effects");
                    add("effect.tcots_witcher.rook.tooltip",  "Increases dexterity, increasing damage dealt with swords");
                    add("effect.tcots_witcher.rook.tooltip.when_applied",  "When Using Swords:");
                    add("effect.tcots_witcher.rook.tooltip.special_attribute",  "+%s Attack Damage");
                    add("effect.tcots_witcher.white_honey.tooltip",  "Clears Toxicity and cancels all active potion effects");

                    add("effect.tcots_witcher.water_hag_ferocity.tooltip",  "Increases attack when the user has full health");
                    add("effect.tcots_witcher.water_hag_ferocity.tooltip.when_applied",  "At Full Health:");
                    add("effect.tcots_witcher.grave_hag_vitality.gui", "Grave Hag Vitality (%s kills)");
                    add("effect.tcots_witcher.grave_hag_vitality.tooltip",  "Increases regeneration for each enemy killed during combat");
                    add("effect.tcots_witcher.alghoul_hungry.tooltip",  "Restores hunger in combat until a successful enemy hit");
                    add("effect.tcots_witcher.foglet_foggy_resistance.tooltip",  "Reduces damage taken by half only if it is raining");
                    add("effect.tcots_witcher.nekker_warrior_leadership.tooltip",  "Increases damage and speed when using a mount");
                    add("effect.tcots_witcher.nekker_warrior_leadership.tooltip.when_applied",  "When Mounted:");
                    add("effect.tcots_witcher.nekker_warrior_leadership.tooltip.special_attribute",  "+%s%% Mount Speed");
                    add("effect.tcots_witcher.troll_healing.tooltip",  "Increases regeneration during and outside combat");

                }
            }
        }

        //Subtitles
        {
            //Generic
            {
                add("subtitles.tcots_witcher.entity.generic.monster_emerging", "Monster emerges from ground");
                add("subtitles.tcots_witcher.entity.generic.monster_digging", "Monster return to ground");
                add("subtitles.tcots_witcher.entity.generic.wet_monster_emerging", "Monster emerges from ground");
                add("subtitles.tcots_witcher.entity.generic.wet_monster_digging", "Monster return to ground");
                add("subtitles.tcots_witcher.entity.generic.ground_punch", "Something punch the ground");
                add("subtitles.tcots_witcher.entity.generic.medium_impact", "Something impacts");
                add("subtitles.tcots_witcher.entity.generic.big_impact", "Something big impacts");
            }

            //Necrophages
            {
                add("subtitles.tcots_witcher.entity.necrophage.drowner.footstep", "Footsteps");
                add("subtitles.tcots_witcher.entity.necrophage.drowner.attack", "Drowner attacks");
                add("subtitles.tcots_witcher.entity.necrophage.drowner.hurt", "Drowner hurts");
                add("subtitles.tcots_witcher.entity.necrophage.drowner.idle", "Drowner groans");
                add("subtitles.tcots_witcher.entity.necrophage.drowner.death", "Drowner dies");
                add("subtitles.tcots_witcher.entity.necrophage.drowner.lunge", "Drowner lunges");
                add("subtitles.tcots_witcher.entity.necrophage.drowner.scream", "Drowner screams");

                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.attack", "Rotfiend attacks");
                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.hurt", "Rotfiend hurts");
                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.idle", "Rotfiend groans");
                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.death", "Rotfiend dies");
                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.lunge", "Rotfiend lunges");
                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.exploding", "Rotfiend exploding");
                add("subtitles.tcots_witcher.entity.necrophage.rotfiend.blood_explosion", "Explosion");

                add("subtitles.tcots_witcher.entity.necrophage.grave_hag.attack", "Grave Hag attacks");
                add("subtitles.tcots_witcher.entity.necrophage.grave_hag.hurt", "Grave Hag hurts");
                add("subtitles.tcots_witcher.entity.necrophage.grave_hag.idle", "Grave Hag groans");
                add("subtitles.tcots_witcher.entity.necrophage.grave_hag.death", "Grave Hag dies");
                add("subtitles.tcots_witcher.entity.necrophage.grave_hag.tongue_attack", "Grave Hag attacks with tongue");
                add("subtitles.tcots_witcher.entity.necrophage.grave_hag.run", "Grave Hag screams");

                add("subtitles.tcots_witcher.entity.necrophage.water_hag.attack", "Water Hag attacks");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.hurt", "Water Hag hurts");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.idle", "Water Hag groans");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.death", "Water Hag dies");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.emerging", "Monster emerges from ground");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.digging", "Monster return to ground");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.mud_ball_hit", "Mudball hits");
                add("subtitles.tcots_witcher.entity.necrophage.water_hag.mud_ball_launch", "Mudball flies");

                add("subtitles.tcots_witcher.entity.necrophage.foglet.attack", "Foglet attacks");
                add("subtitles.tcots_witcher.entity.necrophage.foglet.hurt", "Foglet hurts");
                add("subtitles.tcots_witcher.entity.necrophage.foglet.idle", "Foglet groans");
                add("subtitles.tcots_witcher.entity.necrophage.foglet.death", "Foglet dies");
                add("subtitles.tcots_witcher.entity.necrophage.foglet.fog", "Foglet creates fog");
                add("subtitles.tcots_witcher.entity.necrophage.foglet.fogling_disappear", "Foglet illusion vanish");

                add("subtitles.tcots_witcher.entity.necrophage.ghoul.attack", "Ghoul attacks");
                add("subtitles.tcots_witcher.entity.necrophage.ghoul.hurt", "Ghoul hurts");
                add("subtitles.tcots_witcher.entity.necrophage.ghoul.idle", "Ghoul groans");
                add("subtitles.tcots_witcher.entity.necrophage.ghoul.death", "Ghoul dies");
                add("subtitles.tcots_witcher.entity.necrophage.ghoul.lunge", "Ghoul lunges");
                add("subtitles.tcots_witcher.entity.necrophage.ghoul.scream", "Ghoul screams");
                add("subtitles.tcots_witcher.entity.necrophage.ghoul.regen", "Ghoul regenerates");

                add("subtitles.tcots_witcher.entity.necrophage.alghoul.attack", "Alghoul attacks");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.hurt", "Alghoul hurts");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.idle", "Alghoul groans");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.death", "Alghoul dies");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.lunge", "Alghoul lunges");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.scream", "Alghoul screams");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.regen", "Alghoul regenerates");
                add("subtitles.tcots_witcher.entity.necrophage.alghoul.spikes", "Alghoul spikes");

                add("subtitles.tcots_witcher.entity.necrophage.scurver.attack", "Scurver attacks");
                add("subtitles.tcots_witcher.entity.necrophage.scurver.hurt", "Scurver hurts");
                add("subtitles.tcots_witcher.entity.necrophage.scurver.idle", "Scurver groans");
                add("subtitles.tcots_witcher.entity.necrophage.scurver.death", "Scurver dies");
                add("subtitles.tcots_witcher.entity.necrophage.scurver.lunge", "Scurver lunges");
                add("subtitles.tcots_witcher.entity.necrophage.scurver.exploding", "Scurver exploding");

                add("subtitles.tcots_witcher.entity.necrophage.devourer.attack", "Devourer attacks");
                add("subtitles.tcots_witcher.entity.necrophage.devourer.hurt", "Devourer hurts");
                add("subtitles.tcots_witcher.entity.necrophage.devourer.idle", "Devourer groans");
                add("subtitles.tcots_witcher.entity.necrophage.devourer.death", "Devourer dies");
                add("subtitles.tcots_witcher.entity.necrophage.devourer.jump", "Devourer jumps");

                add("subtitles.tcots_witcher.entity.necrophage.bloedzuiger.attack", "Bloedzuiger attacks");
                add("subtitles.tcots_witcher.entity.necrophage.bloedzuiger.hurt", "Bloedzuiger hurts");
                add("subtitles.tcots_witcher.entity.necrophage.bloedzuiger.idle", "Bloedzuiger slurps");
                add("subtitles.tcots_witcher.entity.necrophage.bloedzuiger.death", "Bloedzuiger dies");
                add("subtitles.tcots_witcher.entity.necrophage.bloedzuiger.exploding", "Bloedzuiger exploding");

                add("subtitles.tcots_witcher.entity.necrophage.graveir.attack", "Graveir attacks");
                add("subtitles.tcots_witcher.entity.necrophage.graveir.hurt", "Graveir hurts");
                add("subtitles.tcots_witcher.entity.necrophage.graveir.idle", "Graveir groans");
                add("subtitles.tcots_witcher.entity.necrophage.graveir.death", "Graveir dies");
                add("subtitles.tcots_witcher.entity.necrophage.graveir.ground_punch", "Graveir charges a punch");

                add("subtitles.tcots_witcher.entity.necrophage.bullvore.attack", "Bullvore attacks");
                add("subtitles.tcots_witcher.entity.necrophage.bullvore.hurt", "Bullvore hurts");
                add("subtitles.tcots_witcher.entity.necrophage.bullvore.idle", "Bullvore roars");
                add("subtitles.tcots_witcher.entity.necrophage.bullvore.death", "Bullvore dies");
                add("subtitles.tcots_witcher.entity.necrophage.bullvore.charge", "Bullvore charges");
            }

            //Ogroids
            {
                add("subtitles.tcots_witcher.entity.ogroid.nekker.attack", "Nekker attacks");
                add("subtitles.tcots_witcher.entity.ogroid.nekker.hurt", "Nekker hurts");
                add("subtitles.tcots_witcher.entity.ogroid.nekker.idle", "Nekker groans");
                add("subtitles.tcots_witcher.entity.ogroid.nekker.death", "Nekker dies");
                add("subtitles.tcots_witcher.entity.ogroid.nekker.lunge", "Nekker lunges");

                add("subtitles.tcots_witcher.entity.ogroid.nekker_warrior.attack", "Nekker Warrior attacks");
                add("subtitles.tcots_witcher.entity.ogroid.nekker_warrior.hurt", "Nekker Warrior hurts");
                add("subtitles.tcots_witcher.entity.ogroid.nekker_warrior.idle", "Nekker Warrior groans");
                add("subtitles.tcots_witcher.entity.ogroid.nekker_warrior.death", "Nekker Warrior dies");
                add("subtitles.tcots_witcher.entity.ogroid.nekker_warrior.lunge", "Nekker Warrior lunges");

                add("subtitles.tcots_witcher.entity.ogroid.cyclops.attack", "Cyclops attacks");
                add("subtitles.tcots_witcher.entity.ogroid.cyclops.hurt", "Cyclops hurts");
                add("subtitles.tcots_witcher.entity.ogroid.cyclops.idle", "Cyclops groans");
                add("subtitles.tcots_witcher.entity.ogroid.cyclops.death", "Cyclops dies");
                add("subtitles.tcots_witcher.entity.ogroid.cyclops.punch", "Cyclops punches");

                add("subtitles.tcots_witcher.entity.ogroid.troll.attack", "Troll attacks");
                add("subtitles.tcots_witcher.entity.ogroid.troll.hurt", "Troll hurts");
                add("subtitles.tcots_witcher.entity.ogroid.troll.idle", "Troll groans");
                add("subtitles.tcots_witcher.entity.ogroid.troll.death", "Troll dies");
                add("subtitles.tcots_witcher.entity.ogroid.troll.block_impact", "Troll blocks");
                add("subtitles.tcots_witcher.entity.ogroid.troll.block_impact_break", "Troll block breaks");
                add("subtitles.tcots_witcher.entity.ogroid.troll.rock_projectile_impact", "Rock impacts");
                add("subtitles.tcots_witcher.entity.ogroid.troll.rock_projectile_throws", "Rock projectile flies");
                add("subtitles.tcots_witcher.entity.ogroid.troll.barter", "Troll barters");
                add("subtitles.tcots_witcher.entity.ogroid.troll.follow", "Troll gonna follow");
                add("subtitles.tcots_witcher.entity.ogroid.troll.waiting", "Troll gonna wait");
                add("subtitles.tcots_witcher.entity.ogroid.troll.dismiss", "Troll dismisses");
                add("subtitles.tcots_witcher.entity.ogroid.troll.furious", "Troll furious");
                add("subtitles.tcots_witcher.entity.ogroid.troll.grunt", "Troll grunts");

                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.attack", "Ice Giant attacks");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.hurt", "Ice Giant hurts");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.idle", "Ice Giant groans");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.death", "Ice Giant dies");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.punch", "Ice Giant punches");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.charge", "Ice Giant charges");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.snore", "Ice Giant snore");
                add("subtitles.tcots_witcher.entity.ogroid.ice_giant.wake_up", "Ice Giant wakes Up");

                add("subtitles.tcots_witcher.item.anchor.anchor_throw", "Anchor threw");
                add("subtitles.tcots_witcher.item.anchor.anchor_chain", "Anchor returns");
                add("subtitles.tcots_witcher.item.anchor.anchor_impact", "Anchor impacts");
            }

            //Gui
            add("subtitles.tcots_witcher.gui.alchemy.potion_refill", "Potion refilled");
            add("subtitles.tcots_witcher.gui.alchemy.oil_applied", "Monster oil applied");
            add("subtitles.tcots_witcher.gui.alchemy.oil_ran_out", "Monster oil ran out");
            add("subtitles.tcots_witcher.gui.alchemy.black_blood_hit", "Blood spills");

            //Misc
            add("subtitles.tcots_witcher.entity.villager.work_herbalist", "Herbalist works");
            add("subtitles.tcots_witcher.block.generic.ingredient_pops", "Ingredients pop");

        }

        //Advancements
        {
            add("advancements.witcher.main.title", "The Conjunction of the Spheres");
            add("advancements.witcher.main.description", "Explore this new world, try to buy a Monster Bestiary from a librarian for more information!");

            add("advancements.witcher.start_alchemy.title", "The Mother of all Sciences");
            add("advancements.witcher.start_alchemy.description", "Start the path of alchemy crafting an Alchemy Table, try to buy an Alchemy Almanac from a herbalist too!");

            add("advancements.witcher.use_formula.title", "Let's Cook!");
            add("advancements.witcher.use_formula.description", "Learn a new Alchemy Recipe");

            add("advancements.witcher.craft_potion.title", "Strong Beverage");
            add("advancements.witcher.craft_potion.description", "Get your first post-conjunction potion");

            add("advancements.witcher.craft_decoction.title", "Taste of Monstrosity");
            add("advancements.witcher.craft_decoction.description", "Get a decoction");

            add("advancements.witcher.craft_potion_superior.title", "Practicum in Advanced Alchemy");
            add("advancements.witcher.craft_potion_superior.description", "Get a Superior level potion");

            add("advancements.witcher.max_toxicity.title", "Can Quit Anytime I Want");
            add("advancements.witcher.max_toxicity.description", "Reach high levels of toxicity");

            add("advancements.witcher.craft_oil.title", "Honing the Blade");
            add("advancements.witcher.craft_oil.description", "Get your first Monster Oil");

            add("advancements.witcher.craft_oil_superior.title", "A Powerful Wax");
            add("advancements.witcher.craft_oil_superior.description", "Get a Superior level Monster Oil");

            add("advancements.witcher.kill_with_hanged.title", "...Steel for Humans");
            add("advancements.witcher.kill_with_hanged.description", "Kill an humanoid using Hanged Man's Venom");

            add("advancements.witcher.craft_bomb.title", "Ka-Boom!");
            add("advancements.witcher.craft_bomb.description", "Get your first bomb");

            add("advancements.witcher.craft_bomb_superior.title", "Bombastic");
            add("advancements.witcher.craft_bomb_superior.description", "Get a Superior level bomb");

            add("advancements.witcher.destroy_nest.title", "Fire in the Hole");
            add("advancements.witcher.destroy_nest.description", "Destroy a monster nest using a bomb");

            add("advancements.witcher.destroy_nest_multiple.title", "Pest Control");
            add("advancements.witcher.destroy_nest_multiple.description", "Destroy 30 monster nests");

            add("advancements.witcher.craft_all_bomb.title", "Bombardier");
            add("advancements.witcher.craft_all_bomb.description", "Craft all the kinds of bombs");

            add("advancements.witcher.dragons_dream_burning.title", "That Is the Evilest Thing");
            add("advancements.witcher.dragons_dream_burning.description", "Ignite a Dragon's Dream bomb using a burning opponent");

            add("advancements.witcher.stop_creeper.title", "Successful Gardener");
            add("advancements.witcher.stop_creeper.description", "Use a Moon Dust on a Creeper to disable its explosion forever");

            add("advancements.witcher.refill_concoction.title", "Deep Meditation");
            add("advancements.witcher.refill_concoction.description", "Refill a concoction using alcohol in an Alchemy Table or by sleeping");

            add("advancements.witcher.start_killing.title", "Silver for Monsters...");
            add("advancements.witcher.start_killing.description", "Kill a post-conjunction monster");

            add("advancements.witcher.kill_giant.title", "The Lord of Ice");
            add("advancements.witcher.kill_giant.description", "Find and kill an Ice Giant, time to reclaim the Winter's Blade from its lair!");

            add("advancements.witcher.kill_bullvore.title", "Moo-rderer");
            add("advancements.witcher.kill_bullvore.description", "Kill a powerful Bullvore");

            add("advancements.witcher.get_gvalchir.title", "Won't Hurt a Bit");
            add("advancements.witcher.get_gvalchir.description", "Get the powerful sword G'valchir");

            add("advancements.witcher.kill_rotfiend.title", "Bomb Defusal");
            add("advancements.witcher.kill_rotfiend.description", "Kill a Rotfiend or a Scurver without causing an explosion");

            add("advancements.witcher.get_mutagen.title", "Mutagenic");
            add("advancements.witcher.get_mutagen.description", "Get a monster mutagen");

            add("advancements.witcher.befriend_troll.title", "Friend of Trolls");
            add("advancements.witcher.befriend_troll.description", "Become friend with a troll and get it as follower");

            add("advancements.witcher.befriend_troll_ice.title", "Lots Eats, Lots Drink");
            add("advancements.witcher.befriend_troll_ice.description", "Become friend with an Ice Troll and get it as follower");

            add("advancements.witcher.befriend_all_troll.title", "Troll Trouble");
            add("advancements.witcher.befriend_all_troll.description", "Get each Troll type as follower");

            add("advancements.witcher.get_ravens_armor.title", "Tyen'sail");
            add("advancements.witcher.get_ravens_armor.description", "Get a full set of Raven's Armor");

        }

        //Config
        {
            add("modmenu.nameTranslation.tcots_witcher", "The Conjunction of the Spheres");
            add("modmenu.descriptionTranslation.tcots_witcher", "New monsters, armors, weapons, potions and challenges from the Witcher Universe, beware, The Conjunction of the Spheres!");

            add("text.config.tcots_config.title", "TCOTS Config");

            add("text.config.tcots_config.category.monsters", "Monsters - Add a monster in a monster category");
            add("text.config.tcots_config.category.monsters.tooltip", "Add mobs to work with the respective monster\noils and monster resistances/damage");
            add("text.config.tcots_config.option.monsters.Necrophages", "Necrophage Category");
            add("text.config.tcots_config.option.monsters.Ogroids", "Ogroid Category");
            add("text.config.tcots_config.option.monsters.Beasts", "Beast Category");
            add("text.config.tcots_config.option.monsters.Humanoids", "Humanoid Category");

            add("text.config.tcots_config.option.monsters.Specters", "Specter Category");
            add("text.config.tcots_config.option.monsters.Specters.tooltip", "Upcoming type\nCurrently covering Ghasts");

            add("text.config.tcots_config.option.monsters.Vampires", "Vampire Category");
            add("text.config.tcots_config.option.monsters.Vampires.tooltip", "Upcoming type");

            add("text.config.tcots_config.option.monsters.Insectoids", "Insectoid Category");
            add("text.config.tcots_config.option.monsters.Insectoids.tooltip", "Upcoming type\nCurrently covering Arthropods");

            add("text.config.tcots_config.option.monsters.Elementa", "Elementa Category");
            add("text.config.tcots_config.option.monsters.Elementa.tooltip", "Upcoming type\nCurrently covering Golems, Blazes-like, Allays-like and Slime-like");

            add("text.config.tcots_config.option.monsters.Hybrids", "Hybrid Category");
            add("text.config.tcots_config.option.monsters.Hybrids.tooltip", "Upcoming type");

            add("text.config.tcots_config.option.monsters.Cursed_Ones", "Cursed One Category");
            add("text.config.tcots_config.option.monsters.Cursed_Ones.tooltip", "Upcoming type\nCurrently covering Creepers and Ravagers");

            add("text.config.tcots_config.option.monsters.Draconids", "Draconid Category");
            add("text.config.tcots_config.option.monsters.Draconids.tooltip", "Upcoming type\nCurrently covering the Ender Dragon (duh)");

            add("text.config.tcots_config.option.monsters.Relicts", "Relict Category");
            add("text.config.tcots_config.option.monsters.Relicts.tooltip", "Upcoming type\nCurrently covering Endermen, Guardians and the Warden");

            add("text.config.tcots_config.category.hud", "Toxicity - HUD Options");
            add("text.config.tcots_config.category.hud.tooltip", "Modify the toxicity hud position");
            add("text.config.tcots_config.option.hud.Hud_Y", "Y Offset");
            add("text.config.tcots_config.option.hud.Hud_X", "X Offset");
            add("text.config.tcots_config.option.hud.anchor", "Anchor");
            add("text.config.tcots_config.enum.aNCHORS.center_down", "CENTER DOWN");
            add("text.config.tcots_config.enum.aNCHORS.center_up", "CENTER UP");
            add("text.config.tcots_config.enum.aNCHORS.left_up", "LEFT UP");
            add("text.config.tcots_config.enum.aNCHORS.left_down", "LEFT DOWN");
            add("text.config.tcots_config.enum.aNCHORS.right_up", "RIGHT UP");
            add("text.config.tcots_config.enum.aNCHORS.right_down", "RIGHT DOWN");

            add("text.config.tcots_config.category.witcher_eyes", "Witcher Cosmetics");
            add("text.config.tcots_config.category.witcher_eyes.tooltip", "Options to add Witcher Eyes\nand Toxicity face to the player");
            add("text.config.tcots_config.option.witcher_eyes.activateEyes", "Activate Witcher Eyes");
            add("text.config.tcots_config.option.witcher_eyes.activateToxicity", "Activate Toxicity Face");

            add("text.config.tcots_config.option.witcher_eyes.eyeShape", "Eye Shape");
            add("text.config.tcots_config.option.witcher_eyes.eyeShape.tooltip", "Select the shape that better\nfit your skin's pupils");
            add("text.config.tcots_config.enum.eYE_SHAPE.normal", "Normal (1x1)");
            add("text.config.tcots_config.enum.eYE_SHAPE.tall", "Tall (1x2)");
            add("text.config.tcots_config.enum.eYE_SHAPE.tall_shadow", "Tall (1x2)-With Shadow");
            add("text.config.tcots_config.enum.eYE_SHAPE.tall_center", "Tall (1x2)-For 2x3 Eyes");
            add("text.config.tcots_config.enum.eYE_SHAPE.derp", "Derp (1x1)-On Sides");
            add("text.config.tcots_config.enum.eYE_SHAPE.enderman", "Enderman (1x1)-Center");
            add("text.config.tcots_config.enum.eYE_SHAPE.allay", "Allay (1x3)");
            add("text.config.tcots_config.enum.eYE_SHAPE.long", "Long (2x1)");
            add("text.config.tcots_config.enum.eYE_SHAPE.big", "Big (2x2)");

            add("text.config.tcots_config.option.witcher_eyes.eyeSeparation", "Eye Separation");
            add("text.config.tcots_config.option.witcher_eyes.eyeSeparation.tooltip", "Select the separation between\nthe two pupils");
            add("text.config.tcots_config.enum.eYE_SEPARATION.zero", "0px");
            add("text.config.tcots_config.enum.eYE_SEPARATION.one", "1px");
            add("text.config.tcots_config.enum.eYE_SEPARATION.two", "2px");
            add("text.config.tcots_config.enum.eYE_SEPARATION.three", "3px");
            add("text.config.tcots_config.enum.eYE_SEPARATION.four", "4px");
            add("text.config.tcots_config.enum.eYE_SEPARATION.five", "5px");
            add("text.config.tcots_config.enum.eYE_SEPARATION.six", "6px");

            add("text.config.tcots_config.option.witcher_eyes.XEyePos", "Eye X Pos");
            add("text.config.tcots_config.option.witcher_eyes.XEyePos.tooltip", "Modify the X position");

            add("text.config.tcots_config.option.witcher_eyes.YEyePos", "Eye YPos");
            add("text.config.tcots_config.option.witcher_eyes.YEyePos.tooltip", "Modify the Y position");

            add("text.config.tcots_config.option.witcher_eyes.eyeMoves", "Pupils Move");
            add("text.config.tcots_config.option.witcher_eyes.eyeMoves.tooltip", "Make the pupils move synced\nwith animated eyes\n(Fresh Moves/Just Expressions)\nDoesn't work with Allay, Big or Long eyes");

            add("text.config.tcots_config.option.hasRPGTextures", "Swords 32x32 Texture");
            add("text.config.tcots_config.option.hasRPGTextures.tooltip", "Changes Swords to a 32x32 texture, for parity with\nWitcher (More RPG Classes), automatically enabled\nwith the mod loaded");
        }

        //Misc
        {
            add("entity.tcots_witcher.rock_troll_rabid", "Rabid Rock Troll");
            add("entity.tcots_witcher.ice_troll_rabid", "Rabid Ice Troll");
            add("entity.tcots_witcher.forest_troll_rabid", "Rabid Forest Troll");


                //Lavender/Patchouli
            {
                add("item.tcots_witcher.witcher_bestiary.patchouli", "    Monster Bestiary");
                add("patchouli.tcots_witcher.witcher_bestiary.landing_page", "\"The Conjunction of the Spheres\", this name, so mysterious to a commoner's ear, could be replaced with a much simpler alternative: When the Worlds Collided.$(br2)Learn everything you can about this post-conjunction world if you want to survive, fellow warrior.");

                add("item.tcots_witcher.alchemy_book.patchouli", "     Alchemy Almanac");
                add("patchouli.tcots_witcher.alchemy_book.landing_page", "To understand alchemy, you must understand two great truths.$(li)First truth: \"As above, so below.\"$(li)Second truth: \"Everything is one.\"$(br2)These truths teach that alchemical substances are everywhere around you, this is because alchemy is life, and life is alchemy.");

                add("lavender.alchemy_book.recipes", "Compendium of recipes for potions, decoctions, monster oils, bombs, and alchemical ingredients.");
            }

            //Attributes
            {
                add("attribute.name.witcher_rpg.adrenaline_modifier", "Adrenaline Gain");
                add("attribute.name.witcher_rpg.sign_intensity", "Sign Intensity");
                add("attribute.name.witcher_rpg.aard_intensity", "Aard Sign Intensity");
                add("attribute.name.witcher_rpg.axii_intensity", "Axii Sign Intensity");
                add("attribute.name.witcher_rpg.igni_intensity", "Igni Sign Intensity");
                add("attribute.name.witcher_rpg.quen_intensity", "Quen Sign Intensity");
                add("attribute.name.witcher_rpg.yrden_intensity", "Yrden Sign Intensity");
                add("attribute.name.tcots_witcher.max_witcher_toxicity", "Max Toxicity");
                add("attribute.name.tcots_witcher.resistance_against_monsters", "Resistance Against Monsters");
                add("attribute.name.tcots_witcher.damage_against_monsters", "Damage Against Monsters");
                add("attribute.name.tcots_witcher.bomb_cooldown", "Bomb Cooldown");
                add("attribute.name.tcots_witcher.extra_alcohol_refill", "Refills With Alcohol");
                add("attribute.name.tcots_witcher.potion_drink_time", "Potion Drink Time");

                add("item.witcher_rpg.relic_witcher_swords.description_1", "Relic Sword");
            }

            //Deaths
            {
                add("death.attack.bleeding", "%1$s bled to death");
                add("death.attack.bleeding.player", "%1$s bled to death while fighting %2$s");

                add("death.attack.cadaverine", "%1$s flesh was dissolved");
                add("death.attack.cadaverine.player", "%1$s flesh was dissolved while fighting %2$s");

                add("death.attack.potionToxicity", "%1$s had too much toxicity in the blood");
                add("death.attack.potionToxicity.player", "%1$s had too much toxicity in the blood while fighting %2$s");
            }

            //Tags
            {
                add("tag.item.tcots_witcher.monster_blood", "Monster Blood");
                add("tag.item.tcots_witcher.decaying_flesh", "Decaying Flesh");
            }

            add("filled_map.giant_cave", "Giant Cave Explorer Map");

            add("entity.minecraft.villager.tcots_witcher.herbalist_witcher", "Herbalist");
            add("entity.minecraft.villager.herbalist_witcher", "Herbalist");

            add("entity.tcots_witcher.group.necrophages", "Necrophages");
            add("entity.tcots_witcher.group.ogroids", "Ogroids");
            add("entity.tcots_witcher.group.specters", "Specters");
            add("entity.tcots_witcher.group.vampires", "Vampires");
            add("entity.tcots_witcher.group.insectoids", "Insectoids");
            add("entity.tcots_witcher.group.beasts", "Beasts");
            add("entity.tcots_witcher.group.elementa", "Elementa");
            add("entity.tcots_witcher.group.cursed_ones", "Cursed Ones");
            add("entity.tcots_witcher.group.hybrids", "Hybrids");
            add("entity.tcots_witcher.group.draconids", "Draconids");
            add("entity.tcots_witcher.group.relicts", "Relicts");
            add("entity.tcots_witcher.group.humanoids", "Illagers & Humans");
        }
    }

    private void addEntityTranslation(final ResourceKey<EntityType<?>> item) {
        final String path = item.location().getPath();
        final String translation = translate(path);
        add(BuiltInRegistries.ENTITY_TYPE.get(item.location()), translation);
    }

    private void addItemTranslation(final ResourceKey<Item> item) {

        final String path = item.location().getPath();
        final String translation = translate(path);

        //Potions namer
        if(BuiltInRegistries.ITEM.get(item.location()) instanceof final WitcherPotions_Base potionsBase && !potionsBase.isDecoction()){
            add(BuiltInRegistries.ITEM.get(item.location()), translate(getRearrangedWitcherPotionName(path)));
            return;
        }

        //Oil namer
        if(BuiltInRegistries.ITEM.get(item.location()) instanceof WitcherMonsterOil_Base){
            add(BuiltInRegistries.ITEM.get(item.location()), translate(getRearrangedOilName(path)));
            return;
        }

        //Bombs namer
        if(BuiltInRegistries.ITEM.get(item.location()) instanceof WitcherBombs_Base){
            add(BuiltInRegistries.ITEM.get(item.location()), translate(getRearrangedBombName(path)));
            return;
        }

        //Handles descriptions and names for banner patterns
        if(BuiltInRegistries.ITEM.get(item.location()) instanceof final BannerPatternItem bannerPatternItem){
            final String resourceKeyPath= bannerPatternItem.getBannerPattern().location().getPath();
            final String[] resourceKeySplitted = resourceKeyPath.split("/");
            //Name (All are named "Banner Pattern")
            add(BuiltInRegistries.ITEM.get(item.location()), "Banner Pattern");

            final String translationBanner = translate(resourceKeySplitted[1]);
            //Description
            add(BuiltInRegistries.ITEM.get(item.location()).getDescriptionId()+".desc", translationBanner);
            return;
        }

        //Default
        add(BuiltInRegistries.ITEM.get(item.location()), translation);
    }

    private static @NotNull String getRearrangedWitcherPotionName(final String path) {
        final String[] split= path.split("_");

        StringBuilder name= new StringBuilder();

        String level= "";

        if(split.length>0){
            //FOR to get the complete name
            for (int i=0; i<split.length; i++){
                final String v= split[i];

                if(v.equals("potion") || v.equals("splash") || v.equals("superior") || v.equals("enhanced")) break;

                if(i==0){
                    name = new StringBuilder(split[i]);
                } else {
                    name.append("_").append(split[i]);
                }
            }
        }

        final String v =split[split.length-1];
        //If the last isn't "potion" then it is a level
        if(v.equals("enhanced") || v.equals("superior") || v.equals("splash"))
            level= v;


        return (!level.isEmpty()? level+"_": "")+name;
    }

    private static @NotNull String getRearrangedOilName(final String path) {
        final String[] split= path.split("_");

        String oil= "";

        String type= "";

        String level= "";

        if(split.length>=2){
            type= split[1];
        }

        if(split.length>0){
            oil= split[0];
        }

        if(split.length>=3){
            level= split[2];
        }


        return (!level.isEmpty()? level+"_": "") +type+"_"+oil;
    }

    private static @NotNull String getRearrangedBombName(final String path) {
        final String[] split= path.split("_");

        StringBuilder name= new StringBuilder();

        String level= "";

        if(split.length>0){
            //FOR to get the complete name
            for (int i=0; i<split.length; i++){
                if(split[i].equals("enhanced") || split[i].equals("superior")) break;
                if(i==0){
                    name = new StringBuilder(split[i]);
                } else {
                    name.append("_").append(split[i]);
                }
            }
        }

        if(split[split.length-1].equals("enhanced") || split[split.length-1].equals("superior"))
            level= split[split.length-1];


        return (!level.isEmpty()? level+"_": "") +name;
    }

    private void addBlockTranslation(final ResourceKey<Block> block, final Block... exceptions) {
        //To hande manually certain translations
        for (final Block exception : exceptions) {
            if (BuiltInRegistries.BLOCK.get(block.location()).equals(exception)) return;
        }

        final String path = block.location().getPath();
        final String translation = translate(path);

        add(BuiltInRegistries.BLOCK.get(block.location()), translation);
    }

    @SuppressWarnings("all")
    private void addEffectTranslation(ResourceKey<MobEffect> effectResourceKey) {
        String path = effectResourceKey.location().getPath();
        String translation = translate(path);
        add(BuiltInRegistries.MOB_EFFECT.get(effectResourceKey.location()), translation);
    }

    private String translate(final String path) {
        final String[] parts = path.split("_");

        for (int i = 0; i < parts.length; i++) {
            for (final Map.Entry<String, String> entry : SORTED_DICTIONARY) {
                // Match multi-word override
                final String key = entry.getKey();
                final String[] keyParts = key.split("_");
                if (i + keyParts.length <= parts.length) {
                    boolean match = true;
                    for (int j = 0; j < keyParts.length; j++) {
                        if (!parts[i + j].equals(keyParts[j])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        parts[i] = entry.getValue(); // Replace first part
                        // Remove remaining matched parts
                        for (int j = 1; j < keyParts.length; j++) parts[i + j] = "";
                    }
                }
            }
        }

        return Arrays.stream(parts)
                .filter(s -> !s.isEmpty())
                .map(word -> Character.isUpperCase(word.charAt(0)) ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining(" "));
    }
}
