package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.items.AlchemyFormulaItem;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.items.HerbalMixture;
import TCOTS.items.WaterHag_MudBallItem;
import TCOTS.items.armor.ManticoreArmorItem;
import TCOTS.items.armor.RavensArmorItem;
import TCOTS.items.armor.WarriorsLeatherArmorItem;
import TCOTS.items.armor.WitcherHorseArmorItem;
import TCOTS.items.blocks.*;
import TCOTS.items.components.CustomEffectsComponent;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.items.concoctions.*;
import TCOTS.items.weapons.*;
import TCOTS.utils.AlchemyFormulaUtil;
import TCOTS.utils.MiscUtil;
import TCOTS.utils.SwordsAndArmorAttributes;
import com.mojang.serialization.MapCodec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import io.wispforest.owo.client.texture.AnimatedTextureDrawable;
import io.wispforest.owo.client.texture.SpriteSheetMetadata;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupButton;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TCOTS_Items {


    //xTODO: Add Bombs
    //xTODO: Add Bombs Entry
    //xTODO: Add new way to craft the potions
    // xTODO: Add new alchemy ingredients (mushrooms, flowers)
    //Plants
    //xTODO: Allspice = Moleyarrow = Mandrake root
    //xTODO: Arenaria
    //xTODO: Balisse fruit - Sweet Berries
    //xTODO: Beggartick blossoms - Poppy
    //xTODO: Berbercane fruit - Glow Berries
    //xTODO: Blowball - Dandelion
    //xTODO: Bryonia
    //xTODO: Buckthorn - Kelp
    //xTODO: Celandine
    //xTODO: Cortinarius - Brown Mushroom
    //xTODO: Crow's eye
    //xTODO: Ergot seeds - Poisonous potato like
    //xTODO: Fool's parsley leaves - Azure Bluet
    //xTODO: Ginatia petals - Cornflower
    //xTODO: Green mold && Bloodmoss- Moss Block
    //xTODO: Han fiber
    //xTODO: Hellebore petals - Allium
    //xTODO: Honeysuckle - Honey Bottle
    //xTODO: Hop umbels && Bison Grass - Beetroot
    //xTODO: Longrube - Red mushroom
    //xTODO: Mistletoe && Ribleaf - Oxeye Daisy
    //xTODO: Nostrix && Hornwort- Glow Lichen
    //xTODO: Puffball
    //xTODO: Pringrape - Flowering Azalea
    //xTODO: Ranogrin - Fern
    //xTODO: Sewant mushrooms
    //xTODO: Verbena
    //xTODO: White myrtle petals - Lily of the Valley
    //TODO: Wolfsbane

    //and alcohol)
    //TODO: Add more Potions
    //Witcher 3 Potions
    //xTODO: Swallow: Add when added Drowners
    //xTODO: White Raffard's Decoction: Add when added Nekkers
    //xTODO: Killer Whale: Add when added Drowners
    //xTODO: Cat: Can be added, crafted with Water essence
    //xTODO: Black Blood: Add when added Ghouls
    //xTODO: Maribor Forest: Add when added Alghouls
    //xTODO: White Honey: Add when added toxicity mechanic

    //  Golden Oriole: Add when added Noonwraiths


    //TODO: W3.- 2nd Update
    //  Petri's Philter: Add when added specters
    //  Full Moon: Add when added Nightwraiths
    //TODO: W3.- 3rd Update
    //  Tawny Owl: Add when added Arachas
    //  Thunderbolt: Add when added Endregas
    //  Blizzard: Add when added Golems

    //Witcher 2 Potions
    //xTODO: Rook: Increases damage with swords
    //TODO:
    //  Brock: Makes possible apply different effects to mobs, but any damage it's received double
    //  Gadwall: Increases a lot the regeneration and health boost but also weakness
    //  Stammelford's Philtre: Something???

    //Witcher 1 Potions
    //xTODO: Wolf: Makes critical hits stronger
    //TODO:
    //  Bindweed: Reduces damage taken from wither, poison and cadaverine (And any other hurting effect?)
    //  Willow: Makes you immune to knockback
    //  De Vries' Extract: Gives glowing to near invisible enemies
    //  Wolverine: Makes you stronger when you have less health?

    //xTODO: Armors to add
    //Raven's Armor   (W2 Style)
    //Kaer Morhen Armor (W3 Style)
    //Manticore Armor (W1/W3 Style)

    //TODO: Weapons to add
    //  * Witcher gear swords from W3
    //  * Aerondight: Add with some quest/ritual involving the Lady of the Lake
    //  * Addan Deith: Extra damage to Specters, found in some special place
    //D'yaebl: Makes enemies bleed
    //Ard'aenye: Massive damage
    //Winter's Blade: Can freeze
    //Moonblade: Massive damage to monsters


    //xTODO: Add Horse Armors
    // Caed Myrkvid Armor??
    //Knight errant's armor
    //Undvik Armor
    //xTODO: Add crafting and bonus

    //xTODO: Add use to the items
    //xTODO: Drowner Tongue usable for Killer Whale potion
    //xTODO: Add the Killer Whale effect, improves respiration and attack underwater
    //xTODO: Drowner Brain usable for a new potion, Swallow:
    //xTODO: Add the Swallow effect, works like regeneration, it's a lot slower but the potion it's more durable
    //xTODO: Nekker eye usable for Hanged Man Oil
    //xTODO: Nekker Hearth usable for the White Raffard's Decoction
    //xTODO: Add the White Raffard's Decoction, works similar to the instant health, but works with percentage

    //TODO: Fix the achievement to mobs works with Monster Hunter?? Probably impossible

    public static final RegistrySupplier<LootItemFunctionType<? extends LootItemConditionalFunction>> RANDOMIZE_FORMULA = registerLootFunction("randomize_formula", AlchemyRecipeRandomlyLootFunction.CODEC);



    //Alchemy Ingredients
    //Alcohol
    public static RegistrySupplier<WitcherAlcohol_Base> ICY_SPIRIT;
    public static RegistrySupplier<WitcherAlcohol_Base> DWARVEN_SPIRIT;
    public static RegistrySupplier<WitcherAlcohol_Base> ALCOHEST;
    public static RegistrySupplier<WitcherAlcohol_Base> WHITE_GULL;
    public static RegistrySupplier<WitcherAlcohol_Base> VILLAGE_HERBAL;
    public static RegistrySupplier<WitcherAlcohol_Base> CHERRY_CORDIAL;
    public static RegistrySupplier<WitcherAlcohol_Base> MANDRAKE_CORDIAL;
    // Substances
    public static RegistrySupplier<Item> AETHER;
    public static RegistrySupplier<Item> VITRIOL;
    public static RegistrySupplier<Item> VERMILION;
    public static RegistrySupplier<Item> HYDRAGENUM;
    public static RegistrySupplier<Item> QUEBRITH;
    public static RegistrySupplier<Item> RUBEDO;
    public static RegistrySupplier<Item> REBIS;
    public static RegistrySupplier<Item> NIGREDO;
    public static RegistrySupplier<Item> STAMMELFORDS_DUST;
    public static RegistrySupplier<Item> ALCHEMISTS_POWDER;
    public static RegistrySupplier<Item> ALCHEMY_PASTE;
    public static RegistrySupplier<Item> MONSTER_FAT;
    // Herbs
    public static RegistrySupplier<Item> ALLSPICE;
    public static RegistrySupplier<Item> ARENARIA;
    public static RegistrySupplier<Item> CELANDINE;
    public static RegistrySupplier<Item> BRYONIA;
    public static RegistrySupplier<Item> CROWS_EYE;
    public static RegistrySupplier<Item> VERBENA;
    public static RegistrySupplier<Item> HAN_FIBER;
    public static RegistrySupplier<Item> PUFFBALL;
    public static RegistrySupplier<Item> PUFFBALL_MUSHROOM_BLOCK_ITEM;
    public static RegistrySupplier<Item> SEWANT_MUSHROOMS;
    public static RegistrySupplier<Item> SEWANT_MUSHROOM_BLOCK_ITEM;
    public static RegistrySupplier<Item> SEWANT_MUSHROOM_STEM_ITEM;
    public static RegistrySupplier<Item> ERGOT_SEEDS;

    public static void initAlchemyIngredients() {
        //Alcohol
        {

            TCOTS_Items.ICY_SPIRIT = registerAlcohol("icy_spirit",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(64),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 200, 0),
                                    new MobEffectInstance(MobEffects.SATURATION, 10, 1)),
                            1));

            TCOTS_Items.DWARVEN_SPIRIT = registerAlcohol("dwarven_spirit",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(16),
                            List.of(new MobEffectInstance(MobEffects.CONFUSION, 1, 200)),
                            2));

            TCOTS_Items.ALCOHEST = registerAlcohol("alcohest",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(16),
                            List.of(new MobEffectInstance(MobEffects.CONFUSION, 600, 2)),
                            4));

            TCOTS_Items.WHITE_GULL = registerAlcohol("white_gull",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(8),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 1200, 3),
                                    new MobEffectInstance(MobEffects.POISON, 40, 1)),
                            8));


            TCOTS_Items.VILLAGE_HERBAL = registerAlcohol("village_herbal",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(8),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 200, 1),
                                    new MobEffectInstance(MobEffects.REGENERATION, 200, 0)),
                            4));

            TCOTS_Items.CHERRY_CORDIAL = registerAlcohol("cherry_cordial",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(16),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 200, 1),
                                    new MobEffectInstance(MobEffects.ABSORPTION, 200, 0)),
                            2));

            TCOTS_Items.MANDRAKE_CORDIAL = registerAlcohol("mandrake_cordial",
                    () -> new WitcherAlcohol_Base(new Item.Properties().stacksTo(8),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 400, 2),
                                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0)),
                            6));
        }

        //Substances
        {
            TCOTS_Items.ALCHEMY_PASTE = registerItem("alchemy_paste",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.MONSTER_FAT = registerItem("monster_fat",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.STAMMELFORDS_DUST = registerItem("stammelfords_dust",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.ALCHEMISTS_POWDER = registerItem("alchemists_powder",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.AETHER = registerItem("aether",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.VITRIOL = registerItem("vitriol",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.VERMILION = registerItem("vermilion",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.HYDRAGENUM = registerItem("hydragenum",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.QUEBRITH = registerItem("quebrith",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.RUBEDO = registerItem("rubedo",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.REBIS = registerItem("rebis",
                    () -> new Item(new Item.Properties()));

            TCOTS_Items.NIGREDO = registerItem("nigredo",
                    () -> new Item(new Item.Properties()));
        }

        //Plants
        {
            TCOTS_Items.ALLSPICE = registerItem("allspice",
                    ()-> new Item(new Item.Properties()));

            TCOTS_Items.ARENARIA = registerItem("arenaria",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.ArenariaBush(),
                            new Item.Properties()));

            TCOTS_Items.CELANDINE = registerItem("celandine",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.CelandinePlant(),
                            new Item.Properties()));

            TCOTS_Items.CROWS_EYE = registerItem("crows_eye",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.CrowsEyeFern(),
                            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1.2f).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.8f).build())));

            TCOTS_Items.BRYONIA = registerItem("bryonia",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.BryoniaVine(),
                            new Item.Properties()));

            TCOTS_Items.VERBENA = registerItem("verbena",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.VerbenaFlower(),
                            new Item.Properties()));

            TCOTS_Items.HAN_FIBER = registerItem("han_fiber",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.HanFiberPlant(),
                            new Item.Properties()));

            TCOTS_Items.PUFFBALL = registerItem("puffball",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.PuffballMushroom(),
                            new Item.Properties()));

            TCOTS_Items.SEWANT_MUSHROOMS = registerItem("sewant_mushrooms",
                    () -> new ItemNameBlockItem(TCOTS_Blocks.SewantMushroomsPlant(),
                            new Item.Properties()));


            TCOTS_Items.ERGOT_SEEDS = registerItem("ergot_seeds",
                    ()-> new Item(new Item.Properties()));

        }
    }

    //Drops
    public static RegistrySupplier<Item> DROWNER_SPAWN_EGG;
    public static RegistrySupplier<Item> DROWNER_TONGUE;
    public static RegistrySupplier<Item> DROWNER_BRAIN;
    public static RegistrySupplier<Item> ROTFIEND_SPAWN_EGG;
    public static RegistrySupplier<Item> ROTFIEND_BLOOD;
    public static RegistrySupplier<Item> GRAVE_HAG_SPAWN_EGG;
    public static RegistrySupplier<Item> GRAVE_HAG_MUTAGEN;
    public static RegistrySupplier<Item> WATER_HAG_SPAWN_EGG;
    public static RegistrySupplier<Item> WATER_HAG_MUD_BALL;
    public static RegistrySupplier<Item> WATER_HAG_MUTAGEN;
    public static RegistrySupplier<Item> WATER_ESSENCE;
    public static RegistrySupplier<Item> FOGLET_SPAWN_EGG;
    public static RegistrySupplier<Item> FOGLET_TEETH;
    public static RegistrySupplier<Item> FOGLET_MUTAGEN;
    public static RegistrySupplier<Item> GHOUL_SPAWN_EGG;
    public static RegistrySupplier<Item> GHOUL_BLOOD;
    public static RegistrySupplier<Item> ALGHOUL_SPAWN_EGG;
    public static RegistrySupplier<Item> ALGHOUL_BONE_MARROW;
    public static RegistrySupplier<Item> SCURVER_SPAWN_EGG;
    public static RegistrySupplier<Item> SCURVER_SPINE;
    public static RegistrySupplier<Item> DEVOURER_SPAWN_EGG;
    public static RegistrySupplier<Item> DEVOURER_TEETH;
    public static RegistrySupplier<Item> BLOEDZUIGER_SPAWN_EGG;
    public static RegistrySupplier<Item> BLOEDZUIGER_BLOOD;
    public static RegistrySupplier<Item> GRAVEIR_SPAWN_EGG;
    public static RegistrySupplier<Item> CADAVERINE;
    public static RegistrySupplier<Item> GRAVEIR_BONE;
    public static RegistrySupplier<Item> BULLVORE_SPAWN_EGG;
    public static RegistrySupplier<Item> BULLVORE_HORN_FRAGMENT;
    public static RegistrySupplier<Item> NEKKER_SPAWN_EGG;
    public static RegistrySupplier<Item> NEKKER_HEART;
    public static RegistrySupplier<Item> NEKKER_EYE;
    public static RegistrySupplier<Item> NEKKER_WARRIOR_SPAWN_EGG;
    public static RegistrySupplier<Item> NEKKER_WARRIOR_MUTAGEN;
    public static RegistrySupplier<Item> CYCLOPS_SPAWN_EGG;
    public static RegistrySupplier<Item> ROCK_TROLL_SPAWN_EGG;
    public static RegistrySupplier<Item> CAVE_TROLL_LIVER;
    public static RegistrySupplier<Item> TROLL_MUTAGEN;
    public static RegistrySupplier<Item> ICE_TROLL_SPAWN_EGG;
    public static RegistrySupplier<Item> FOREST_TROLL_SPAWN_EGG;
    public static RegistrySupplier<Item> ICE_GIANT_SPAWN_EGG;

    public static void initDrops(){
        TCOTS_Items.DROWNER_SPAWN_EGG = registerItem("drowner_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Drowner(), 0x8db1c0, 0x9fa3ae, new Item.Properties()));
        TCOTS_Items.DROWNER_TONGUE = registerItem("drowner_tongue",
                ()->new Item(new Item.Properties()));
        TCOTS_Items.DROWNER_BRAIN = registerItem("drowner_brain",
                ()->new Item(new Item.Properties().stacksTo(16)));


        TCOTS_Items.ROTFIEND_SPAWN_EGG = registerItem("rotfiend_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Rotfiend(), 0xb3867b, 0xe6e1bc, new Item.Properties()));
        TCOTS_Items.ROTFIEND_BLOOD = registerItem("rotfiend_blood",
                ()->new Item(new Item.Properties()));


        TCOTS_Items.GRAVE_HAG_SPAWN_EGG = registerItem("grave_hag_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.GraveHag(), 0xb6b692, 0x8e8480, new Item.Properties()));
        TCOTS_Items.GRAVE_HAG_MUTAGEN = registerItem("grave_hag_mutagen",
                ()->new Item(new Item.Properties().stacksTo(8)));


        TCOTS_Items.WATER_HAG_SPAWN_EGG = registerItem("water_hag_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.WaterHag(), 0x8d93c0, 0x780b17, new Item.Properties()));
        TCOTS_Items.WATER_HAG_MUD_BALL = registerItem("water_hag_mud_ball",
                ()->new WaterHag_MudBallItem(new Item.Properties().stacksTo(16)));
        TCOTS_Items.WATER_HAG_MUTAGEN = registerItem("water_hag_mutagen",
                ()->new Item(new Item.Properties().stacksTo(8)));
        TCOTS_Items.WATER_ESSENCE = registerItem("water_essence",
                ()->new Item(new Item.Properties()));


        TCOTS_Items.FOGLET_SPAWN_EGG = registerItem("foglet_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Foglet(), 0x4a3f3f, 0x211c1c, new Item.Properties()));
        TCOTS_Items.FOGLET_TEETH = registerItem("foglet_teeth",
                ()->new Item(new Item.Properties()));
        TCOTS_Items.FOGLET_MUTAGEN = registerItem("foglet_mutagen",
                ()->new Item(new Item.Properties().stacksTo(8)));


        TCOTS_Items.GHOUL_SPAWN_EGG = registerItem("ghoul_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Ghoul(), 0xd69d76, 0x0e0a07, new Item.Properties()));
        TCOTS_Items.GHOUL_BLOOD = registerItem("ghoul_blood",
                ()->new Item(new Item.Properties()));


        TCOTS_Items.ALGHOUL_SPAWN_EGG = registerItem("alghoul_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Alghoul(), 0x513e3d, 0x000000,
                        new Item.Properties()));
        TCOTS_Items.ALGHOUL_BONE_MARROW = registerItem("alghoul_bone_marrow",
                ()->new Item(new Item.Properties()));


        TCOTS_Items.SCURVER_SPAWN_EGG = registerItem("scurver_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Scurver(), 0xc0887a, 0x661f1f, new Item.Properties()));

        TCOTS_Items.SCURVER_SPINE = registerItem("scurver_spine",
                ()->new ScurverSpineItem(new Item.Properties().stacksTo(16)));


        TCOTS_Items.DEVOURER_SPAWN_EGG = registerItem("devourer_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Devourer(), 0x606c68, 0x1f1f1f, new Item.Properties()));
        TCOTS_Items.DEVOURER_TEETH = registerItem("devourer_teeth",
                ()->new Item(new Item.Properties()));

        TCOTS_Items.BLOEDZUIGER_SPAWN_EGG = registerItem("bloedzuiger_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Bloedzuiger(), 0x21363a, 0x352b2c, new Item.Properties()));
        TCOTS_Items.BLOEDZUIGER_BLOOD = registerItem("bloedzuiger_blood",
                ()->new Item(new Item.Properties()));

        TCOTS_Items.GRAVEIR_SPAWN_EGG = registerItem("graveir_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Graveir(), 0xab706d, 0x882925, new Item.Properties()));
        TCOTS_Items.CADAVERINE = registerItem("cadaverine",
                ()->new Item(new Item.Properties()));
        TCOTS_Items.GRAVEIR_BONE = registerItem("graveir_bone",
                ()->new Item(new Item.Properties()));

        TCOTS_Items.BULLVORE_SPAWN_EGG = registerItem("bullvore_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Bullvore(), 0xdad29a, 0xb1816d, new Item.Properties()));
        TCOTS_Items.BULLVORE_HORN_FRAGMENT = registerItem("bullvore_horn_fragment",
                ()->new Item(new Item.Properties()));

        TCOTS_Items.NEKKER_SPAWN_EGG = registerItem("nekker_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Nekker(), 0xa59292, 0x705c5c,
                        new Item.Properties()));
        TCOTS_Items.NEKKER_EYE = registerItem("nekker_eye",
                ()->new Item(new Item.Properties()));
        TCOTS_Items.NEKKER_HEART = registerItem("nekker_heart",
                ()->new Item(new Item.Properties().stacksTo(16)));

        TCOTS_Items.NEKKER_WARRIOR_SPAWN_EGG = registerItem("nekker_warrior_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.NekkerWarrior(), 0x97a592, 0xb12022,
                        new Item.Properties()));
        TCOTS_Items.NEKKER_WARRIOR_MUTAGEN = registerItem("nekker_warrior_mutagen",
                ()->new Item(new Item.Properties().stacksTo(8)));

        TCOTS_Items.CYCLOPS_SPAWN_EGG = registerItem("cyclops_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.Cyclops(), 0xceb6b6, 0x3c4433,
                        new Item.Properties()));

        TCOTS_Items.ROCK_TROLL_SPAWN_EGG = registerItem("rock_troll_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.RockTroll(), 0x90acb1, 0xeeb19a,
                        new Item.Properties()));
        TCOTS_Items.CAVE_TROLL_LIVER = registerItem("cave_troll_liver",
                ()->new Item(new Item.Properties()));
        TCOTS_Items.TROLL_MUTAGEN = registerItem("troll_mutagen",
                ()->new Item(new Item.Properties().stacksTo(8)));

        TCOTS_Items.ICE_TROLL_SPAWN_EGG = registerItem("ice_troll_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.IceTroll(), 0xaadde6, 0xffd8c9,
                        new Item.Properties()));

        TCOTS_Items.FOREST_TROLL_SPAWN_EGG = registerItem("forest_troll_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.ForestTroll(), 0x265558, 0xcfcfb4,
                        new Item.Properties()));

        TCOTS_Items.ICE_GIANT_SPAWN_EGG = registerItem("ice_giant_spawn_egg",
                ()->new SpawnEggItem(TCOTS_Entities.IceGiant(), 0x93b7b7, 0x1d1919,
                        new Item.Properties()));
    }


    //Alchemy Concoctions
    public static RegistrySupplier<Item> ALCHEMY_FORMULA;
    //Decoctions
    public static RegistrySupplier<Item> EMPTY_MONSTER_DECOCTION;
    public static RegistrySupplier<Item> GRAVE_HAG_DECOCTION;
    public static RegistrySupplier<Item> WATER_HAG_DECOCTION;
    public static RegistrySupplier<Item> FOGLET_DECOCTION;
    public static RegistrySupplier<Item> ALGHOUL_DECOCTION;
    public static RegistrySupplier<Item> NEKKER_WARRIOR_DECOCTION;
    public static RegistrySupplier<Item> TROLL_DECOCTION;
    public static RegistrySupplier<Item> EMPTY_WITCHER_POTION;
    //W1
    public static RegistrySupplier<Item> WOLF_POTION;
    public static RegistrySupplier<Item> WOLF_POTION_ENHANCED;
    public static RegistrySupplier<Item> WOLF_POTION_SUPERIOR;
    public static RegistrySupplier<Item> BINDWEED_POTION;
    public static RegistrySupplier<Item> BINDWEED_POTION_ENHANCED;
    public static RegistrySupplier<Item> BINDWEED_POTION_SUPERIOR;
    //W2
    public static RegistrySupplier<Item> ROOK_POTION;
    public static RegistrySupplier<Item> ROOK_POTION_ENHANCED;
    public static RegistrySupplier<Item> ROOK_POTION_SUPERIOR;
    //W3
    public static RegistrySupplier<Item> SWALLOW_POTION;
    public static RegistrySupplier<Item> SWALLOW_POTION_ENHANCED;
    public static RegistrySupplier<Item> SWALLOW_POTION_SUPERIOR;
    public static RegistrySupplier<Item> WHITE_RAFFARDS_DECOCTION;
    public static RegistrySupplier<Item> WHITE_RAFFARDS_DECOCTION_ENHANCED;
    public static RegistrySupplier<Item> WHITE_RAFFARDS_DECOCTION_SUPERIOR;
    public static RegistrySupplier<Item> CAT_POTION;
    public static RegistrySupplier<Item> CAT_POTION_ENHANCED;
    public static RegistrySupplier<Item> CAT_POTION_SUPERIOR;
    public static RegistrySupplier<Item> BLACK_BLOOD_POTION;
    public static RegistrySupplier<Item> BLACK_BLOOD_POTION_ENHANCED;
    public static RegistrySupplier<Item> BLACK_BLOOD_POTION_SUPERIOR;
    public static RegistrySupplier<Item> MARIBOR_FOREST_POTION;
    public static RegistrySupplier<Item> MARIBOR_FOREST_POTION_ENHANCED;
    public static RegistrySupplier<Item> MARIBOR_FOREST_POTION_SUPERIOR;
    public static RegistrySupplier<Item> KILLER_WHALE_POTION;
    public static RegistrySupplier<Item> WHITE_HONEY_POTION;
    public static RegistrySupplier<Item> WHITE_HONEY_POTION_ENHANCED;
    public static RegistrySupplier<Item> WHITE_HONEY_POTION_SUPERIOR;
    //Splash Potions
    public static RegistrySupplier<Item> SWALLOW_SPLASH;
    public static RegistrySupplier<Item> KILLER_WHALE_SPLASH;
    public static RegistrySupplier<Item> WHITE_RAFFARDS_DECOCTION_SPLASH;
    //Oils
    public static RegistrySupplier<Item> NECROPHAGE_OIL;
    public static RegistrySupplier<Item> ENHANCED_NECROPHAGE_OIL;
    public static RegistrySupplier<Item> SUPERIOR_NECROPHAGE_OIL;
    public static RegistrySupplier<Item> OGROID_OIL;
    public static RegistrySupplier<Item> ENHANCED_OGROID_OIL;
    public static RegistrySupplier<Item> SUPERIOR_OGROID_OIL;
    public static RegistrySupplier<Item> BEAST_OIL;
    public static RegistrySupplier<Item> ENHANCED_BEAST_OIL;
    public static RegistrySupplier<Item> SUPERIOR_BEAST_OIL;
    public static RegistrySupplier<Item> HANGED_OIL;
    public static RegistrySupplier<Item> ENHANCED_HANGED_OIL;
    public static RegistrySupplier<Item> SUPERIOR_HANGED_OIL;
    public static RegistrySupplier<Item> EMPTY_OIL;
    //Bombs
    public static RegistrySupplier<Item> EMPTY_BOMB_POWDER;
    public static RegistrySupplier<Item> GRAPESHOT;
    public static RegistrySupplier<Item> GRAPESHOT_ENHANCED;
    public static RegistrySupplier<Item> GRAPESHOT_SUPERIOR;
    public static RegistrySupplier<Item> DANCING_STAR;
    public static RegistrySupplier<Item> DANCING_STAR_ENHANCED;
    public static RegistrySupplier<Item> DANCING_STAR_SUPERIOR;
    public static RegistrySupplier<Item> DEVILS_PUFFBALL;
    public static RegistrySupplier<Item> DEVILS_PUFFBALL_ENHANCED;
    public static RegistrySupplier<Item> DEVILS_PUFFBALL_SUPERIOR;
    public static RegistrySupplier<Item> SAMUM;
    public static RegistrySupplier<Item> SAMUM_ENHANCED;
    public static RegistrySupplier<Item> SAMUM_SUPERIOR;
    public static RegistrySupplier<Item> NORTHERN_WIND;
    public static RegistrySupplier<Item> NORTHERN_WIND_ENHANCED;
    public static RegistrySupplier<Item> NORTHERN_WIND_SUPERIOR;
    public static RegistrySupplier<Item> DRAGONS_DREAM;
    public static RegistrySupplier<Item> DRAGONS_DREAM_ENHANCED;
    public static RegistrySupplier<Item> DRAGONS_DREAM_SUPERIOR;
    public static RegistrySupplier<Item> DIMERITIUM_BOMB;
    public static RegistrySupplier<Item> DIMERITIUM_BOMB_ENHANCED;
    public static RegistrySupplier<Item> DIMERITIUM_BOMB_SUPERIOR;
    public static RegistrySupplier<Item> MOON_DUST;
    public static RegistrySupplier<Item> MOON_DUST_ENHANCED;
    public static RegistrySupplier<Item> MOON_DUST_SUPERIOR;

    public static void initAlchemyConcoctions() {

        TCOTS_Items.ALCHEMY_FORMULA = registerItem("alchemy_formula",
                ()-> new AlchemyFormulaItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON))
        );

        //Potions
        {
            //Regular
            {
                TCOTS_Items.EMPTY_WITCHER_POTION = registerItem("empty_witcher_potion",
                        ()-> new EmptyWitcherPotionItem(new Item.Properties())
                );

                //Swallow
                {

                    TCOTS_Items.SWALLOW_POTION = registerItemPotion("swallow_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            0,
                            false
                    );

                    TCOTS_Items.SWALLOW_POTION_ENHANCED = registerItemPotion("swallow_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            1,
                            false
                    );

                    TCOTS_Items.SWALLOW_POTION_SUPERIOR = registerItemPotion("swallow_potion_superior",
                            new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            2,
                            false
                    );

                    TCOTS_Items.SWALLOW_SPLASH = registerSplashPotion("swallow_splash",
                            new Item.Properties().stacksTo(5),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            10,
                            18
                    );
                }

                //Cat
                {
                    TCOTS_Items.CAT_POTION = registerItemPotion("cat_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            60,
                            0,
                            false
                    );

                    TCOTS_Items.CAT_POTION_ENHANCED = registerItemPotion("cat_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            120,
                            1,
                            false
                    );

                    TCOTS_Items.CAT_POTION_SUPERIOR = registerItemPotion("cat_potion_superior",
                            new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            180,
                            2,
                            false
                    );
                }

                //White Raffard's
                {
                    TCOTS_Items.WHITE_RAFFARDS_DECOCTION = registerItemPotion("white_raffards_decoction",
                            new Item.Properties().stacksTo(2),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            0,
                            false
                    );

                    TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED = registerItemPotion("white_raffards_decoction_enhanced",
                            new Item.Properties().stacksTo(2).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            1,
                            false
                    );

                    TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR = registerItemPotion("white_raffards_decoction_superior",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            2,
                            false
                    );


                    TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH = registerSplashPotion("white_raffards_splash",
                            new Item.Properties().stacksTo(5),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            15,
                            1
                    );
                }

                //Killer Whale
                {
                    TCOTS_Items.KILLER_WHALE_POTION = registerItemPotion("killer_whale_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.KILLER_WHALE_EFFECT,
                            15,
                            90,
                            0,
                            false
                    );

                    TCOTS_Items.KILLER_WHALE_SPLASH = registerSplashPotion("killer_whale_splash",
                            new Item.Properties().stacksTo(5),
                            TCOTS_Effects.KILLER_WHALE_EFFECT,
                            10,
                            75
                    );
                }

                //Black Blood
                {
                    TCOTS_Items.BLACK_BLOOD_POTION = registerItemPotion("black_blood_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED = registerItemPotion("black_blood_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            45,
                            1,
                            false
                    );

                    TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR = registerItemPotion("black_blood_potion_superior",
                            new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            60,
                            2,
                            false
                    );
                }

                //Maribor Forest
                {
                    TCOTS_Items.MARIBOR_FOREST_POTION = registerItemPotion("maribor_forest_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            30,
                            0,
                            false
                    );

                    TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED = registerItemPotion("maribor_forest_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            60,
                            1,
                            false
                    );

                    TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR = registerItemPotion("maribor_forest_potion_superior",
                            new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            90,
                            2,
                            false
                    );
                }

                //Wolf
                {
                    TCOTS_Items.WOLF_POTION = registerItemPotion("wolf_potion",
                            new Item.Properties().stacksTo(2),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    TCOTS_Items.WOLF_POTION_ENHANCED = registerItemPotion("wolf_potion_enhanced",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            45,
                            1,
                            false
                    );

                    TCOTS_Items.WOLF_POTION_SUPERIOR = registerItemPotion("wolf_potion_superior",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            60,
                            2,
                            false
                    );
                }

                //Bindweed
                {
                    TCOTS_Items.BINDWEED_POTION = registerItemPotion("bindweed_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.BINDWEED_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    TCOTS_Items.BINDWEED_POTION_ENHANCED = registerItemPotion("bindweed_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.BINDWEED_EFFECT,
                            25,
                            60,
                            1,
                            false
                    );

                    TCOTS_Items.BINDWEED_POTION_SUPERIOR = registerItemPotion("bindweed_potion_superior",
                            new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.BINDWEED_EFFECT,
                            25,
                            90,
                            2,
                            false
                    );
                }

                //Rook
                {
                    TCOTS_Items.ROOK_POTION = registerItemPotion("rook_potion",
                            new Item.Properties().stacksTo(2),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            45,
                            0,
                            false
                    );

                    TCOTS_Items.ROOK_POTION_ENHANCED = registerItemPotion("rook_potion_enhanced",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            60,
                            1,
                            false
                    );

                    TCOTS_Items.ROOK_POTION_SUPERIOR = registerItemPotion("rook_potion_superior",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            90,
                            2,
                            false
                    );
                }

                //White Honey
                {
                    TCOTS_Items.WHITE_HONEY_POTION = registerItem("white_honey_potion",
                            ()->new WitcherWhiteHoney(new Item.Properties().stacksTo(1))
                    );

                    TCOTS_Items.WHITE_HONEY_POTION_ENHANCED = registerItem("white_honey_potion_enhanced",
                            ()->new WitcherWhiteHoney(new Item.Properties().stacksTo(2).rarity(Rarity.UNCOMMON))
                    );

                    TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR = registerItem("white_honey_potion_superior",
                            ()->new WitcherWhiteHoney(new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON))
                    );
                }
            }

            //Decoctions
            {

                TCOTS_Items.EMPTY_MONSTER_DECOCTION = registerItem("empty_monster_decoction",
                        ()->new EmptyWitcherPotionItem(new Item.Properties().stacksTo(1))
                );

                TCOTS_Items.GRAVE_HAG_DECOCTION = registerItemPotion("grave_hag_decoction",
                        TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT
                );

                TCOTS_Items.WATER_HAG_DECOCTION = registerItemPotion("water_hag_decoction",
                        TCOTS_Effects.WATER_HAG_DECOCTION_EFFECT
                );

                TCOTS_Items.ALGHOUL_DECOCTION = registerItemPotion("alghoul_decoction",
                        TCOTS_Effects.ALGHOUL_DECOCTION_EFFECT
                );

                TCOTS_Items.FOGLET_DECOCTION = registerItemPotion("foglet_decoction",
                        TCOTS_Effects.FOGLET_DECOCTION_EFFECT
                );

                TCOTS_Items.NEKKER_WARRIOR_DECOCTION = registerItemPotion("nekker_warrior_decoction",
                        TCOTS_Effects.NEKKER_WARRIOR_DECOCTION_EFFECT
                );

                TCOTS_Items.TROLL_DECOCTION = registerItemPotion("troll_decoction",
                        TCOTS_Effects.TROLL_DECOCTION_EFFECT
                );
            }
        }

        //Bombs
        {
            TCOTS_Items.EMPTY_BOMB_POWDER = registerItem("bomb_powder",
                    ()->new EmptyBombPowderItem(new Item.Properties()));

            TCOTS_Items.GRAPESHOT = registerItem("grapeshot",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "grapeshot", 0));

            TCOTS_Items.GRAPESHOT_ENHANCED = registerItem("grapeshot_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "grapeshot", 1));

            TCOTS_Items.GRAPESHOT_SUPERIOR = registerItem("grapeshot_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "grapeshot", 2));


            TCOTS_Items.SAMUM = registerItem("samum",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "samum", 0));

            TCOTS_Items.SAMUM_ENHANCED = registerItem("samum_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "samum", 1));

            TCOTS_Items.SAMUM_SUPERIOR = registerItem("samum_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "samum", 2));


            TCOTS_Items.DANCING_STAR = registerItem("dancing_star",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "dancing_star", 0));

            TCOTS_Items.DANCING_STAR_ENHANCED = registerItem("dancing_star_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "dancing_star", 1));

            TCOTS_Items.DANCING_STAR_SUPERIOR = registerItem("dancing_star_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "dancing_star", 2));


            TCOTS_Items.DEVILS_PUFFBALL = registerItem("devils_puffball",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "devils_puffball", 0));

            TCOTS_Items.DEVILS_PUFFBALL_ENHANCED = registerItem("devils_puffball_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "devils_puffball", 1));

            TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR = registerItem("devils_puffball_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "devils_puffball", 2));


            TCOTS_Items.NORTHERN_WIND = registerItem("northern_wind",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "northern_wind", 0));

            TCOTS_Items.NORTHERN_WIND_ENHANCED = registerItem("northern_wind_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "northern_wind", 1));

            TCOTS_Items.NORTHERN_WIND_SUPERIOR = registerItem("northern_wind_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "northern_wind", 2));


            TCOTS_Items.DRAGONS_DREAM = registerItem("dragons_dream",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "dragons_dream", 0));

            TCOTS_Items.DRAGONS_DREAM_ENHANCED = registerItem("dragons_dream_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "dragons_dream", 1));

            TCOTS_Items.DRAGONS_DREAM_SUPERIOR = registerItem("dragons_dream_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "dragons_dream", 2));


            TCOTS_Items.DIMERITIUM_BOMB = registerItem("dimeritium_bomb",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "dimeritium_bomb", 0));

            TCOTS_Items.DIMERITIUM_BOMB_ENHANCED = registerItem("dimeritium_bomb_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "dimeritium_bomb", 1));

            TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR = registerItem("dimeritium_bomb_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "dimeritium_bomb", 2));


            TCOTS_Items.MOON_DUST = registerItem("moon_dust",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(2), "moon_dust", 0));

            TCOTS_Items.MOON_DUST_ENHANCED = registerItem("moon_dust_enhanced",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "moon_dust", 1));

            TCOTS_Items.MOON_DUST_SUPERIOR = registerItem("moon_dust_superior",
                    ()->new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "moon_dust", 2));

        }

        //Oils
        {
            TCOTS_Items.EMPTY_OIL = registerItem("empty_oil",
                    ()->new EmptyWitcherPotionItem(new Item.Properties().stacksTo(1))
            );

            TCOTS_Items.NECROPHAGE_OIL = registerItem("oil_necrophage",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.NECROPHAGES,
                            20,
                            1)
            );
            TCOTS_Items.ENHANCED_NECROPHAGE_OIL = registerItem("oil_necrophage_enhanced",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.NECROPHAGES,
                            40,
                            2)
            );
            TCOTS_Items.SUPERIOR_NECROPHAGE_OIL = registerItem("oil_necrophage_superior",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.NECROPHAGES,
                            60,
                            3)
            );

            TCOTS_Items.OGROID_OIL = registerItem("oil_ogroid",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.OGROIDS,
                            20,
                            1)
            );
            TCOTS_Items.ENHANCED_OGROID_OIL = registerItem("oil_ogroid_enhanced",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.OGROIDS,
                            40,
                            2)
            );
            TCOTS_Items.SUPERIOR_OGROID_OIL = registerItem("oil_ogroid_superior",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.OGROIDS,
                            60,
                            3)
            );

            TCOTS_Items.BEAST_OIL = registerItem("oil_beast",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.BEASTS,
                            20,
                            1)
            );
            TCOTS_Items.ENHANCED_BEAST_OIL = registerItem("oil_beast_enhanced",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.BEASTS,
                            40,
                            2)
            );
            TCOTS_Items.SUPERIOR_BEAST_OIL = registerItem("oil_beast_superior",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.BEASTS,
                            60,
                            3)
            );

            TCOTS_Items.HANGED_OIL = registerItem("oil_hanged",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.HUMANOID,
                            20,
                            1)
            );
            TCOTS_Items.ENHANCED_HANGED_OIL = registerItem("oil_hanged_enhanced",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.HUMANOID,
                            40,
                            2)
            );
            TCOTS_Items.SUPERIOR_HANGED_OIL = registerItem("oil_hanged_superior",
                    ()->new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.HUMANOID,
                            60,
                            3)
            );
        }
    }

    //Weapons & Armors
    public static RegistrySupplier<Item> CURED_MONSTER_LEATHER;
    //Weapons
    public static RegistrySupplier<Item> KNIGHT_CROSSBOW;
    public static RegistrySupplier<Item> BASE_BOLT;
    public static RegistrySupplier<Item> BLUNT_BOLT;
    public static RegistrySupplier<Item> PRECISION_BOLT;
    public static RegistrySupplier<Item> EXPLODING_BOLT;
    public static RegistrySupplier<Item> BROADHEAD_BOLT;
    public static RegistrySupplier<Item> GVALCHIR;
    public static RegistrySupplier<Item> MOONBLADE;
    public static RegistrySupplier<Item> WINTERS_BLADE;
    public static RegistrySupplier<Item> ARDAENYE;
    public static RegistrySupplier<Item> DYAEBL;
    public static RegistrySupplier<Item> GIANT_ANCHOR;
    //Armors
    public static RegistrySupplier<Item> WARRIORS_LEATHER_JACKET;
    public static RegistrySupplier<Item> WARRIORS_LEATHER_TROUSERS;
    public static RegistrySupplier<Item> WARRIORS_LEATHER_BOOTS;
    public static RegistrySupplier<Item> MANTICORE_ARMOR;
    public static RegistrySupplier<Item> MANTICORE_TROUSERS;
    public static RegistrySupplier<Item> MANTICORE_BOOTS;
    public static RegistrySupplier<Item> RAVENS_ARMOR;
    public static RegistrySupplier<Item> RAVENS_TROUSERS;
    public static RegistrySupplier<Item> RAVENS_BOOTS;
    //Horse Armor
    public static RegistrySupplier<Item> TUNDRA_HORSE_ARMOR;
    public static RegistrySupplier<Item> KNIGHT_ERRANTS_HORSE_ARMOR;

    public static void initWeaponsArmors(){
        //Ingredients
        {
            TCOTS_Items.CURED_MONSTER_LEATHER=registerItem("cured_monster_leather", ()->new Item(new Item.Properties()));
        }

        //Crossbows
        {
            TCOTS_Items.KNIGHT_CROSSBOW = registerItem("knight_crossbow",
                    ()->new KnightCrossbow(new Item.Properties().stacksTo(1).durability(600)));

            TCOTS_Items.BASE_BOLT = registerItem("base_bolt", ()->new BoltItem(new Item.Properties(), "base_bolt"));

            TCOTS_Items.BLUNT_BOLT = registerItem("blunt_bolt", ()->new BoltItem(new Item.Properties().stacksTo(32), "blunt_bolt"));

            TCOTS_Items.PRECISION_BOLT = registerItem("precision_bolt", ()->new BoltItem(new Item.Properties().stacksTo(32), "precision_bolt"));

            TCOTS_Items.EXPLODING_BOLT = registerItem("exploding_bolt", ()->new BoltItem(new Item.Properties().stacksTo(32), "exploding_bolt"));

            TCOTS_Items.BROADHEAD_BOLT = registerItem("broadhead_bolt", ()->new BoltItem(new Item.Properties().stacksTo(32), "broadhead_bolt"));
        }

        //Swords
        {
            TCOTS_Items.GVALCHIR = registerItem("gvalchir",
                    ()->
                            new SwordWithTooltip(TCOTS_ItemsMaterials.Gvalchir(), new Item.Properties().rarity(Rarity.UNCOMMON)
                            .attributes(
                                    SwordsAndArmorAttributes.createGvalchirAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.gvalchir").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.gvalchir.extra", (int)(MiscUtil.gvalchir_penetration*100)).withStyle(ChatFormatting.DARK_GREEN))
                    )
            );

            TCOTS_Items.MOONBLADE = registerItem("moonblade",
                    ()->
                            new SwordWithTooltip(TCOTS_ItemsMaterials.Moonblade(), new Item.Properties().rarity(Rarity.UNCOMMON)
                            .attributes(
                                    SwordsAndArmorAttributes.createMoonbladeAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.moonblade").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.moonblade.extra", (int)(MiscUtil.moonblade_bonus*100)).withStyle(ChatFormatting.DARK_GREEN))
                    )
            );

            TCOTS_Items.DYAEBL = registerItem("dyaebl",
                    ()->
                            new SwordWithTooltip(TCOTS_ItemsMaterials.Dyaebl(), new Item.Properties().rarity(Rarity.UNCOMMON)
                            .attributes(
                                    SwordsAndArmorAttributes.createDyaeblAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.dyaebl").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.dyaebl.extra").withStyle(ChatFormatting.DARK_RED))
                    )
            );

            TCOTS_Items.WINTERS_BLADE = registerItem("winters_blade",
                    ()->
                            new SwordWithTooltip(TCOTS_ItemsMaterials.WintersBlade(), new Item.Properties().rarity(Rarity.RARE)
                            .attributes(
                                    SwordsAndArmorAttributes.createWintersBladeAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.winters_blade").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.winters_blade.extra").withColor(0x007b77),
                                    Component.translatable("tooltip.tcots_witcher.winters_blade.extra2").withColor(0x007b77),
                                    Component.translatable("tooltip.tcots_witcher.winters_blade.extra3").withColor(0x007b77))
                    )
            );

            TCOTS_Items.ARDAENYE = registerItem("ardaenye",
                    ()->
                            new SwordItem(TCOTS_ItemsMaterials.Ardaenye(), new Item.Properties()
                            .attributes(
                                    SwordsAndArmorAttributes.createArdaenyeAttributeModifiers()))
            );
        }

        //Armor
        {
            TCOTS_Items.MANTICORE_ARMOR = registerItem("manticore_armor", ()->new ManticoreArmorItem(
                    TCOTS_ItemsMaterials.Manticore(),
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.CHESTPLATE.getDurability(20))
                            .attributes(SwordsAndArmorAttributes.addManticoreArmorAttributes(TCOTS_ItemsMaterials.Manticore(), ArmorItem.Type.CHESTPLATE, 10, 0.075))
            ));
            TCOTS_Items.MANTICORE_TROUSERS = registerItem("manticore_trousers", ()->new ManticoreArmorItem(
                    TCOTS_ItemsMaterials.Manticore(),
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.LEGGINGS.getDurability(20))
                            .attributes(SwordsAndArmorAttributes.addManticoreArmorAttributes(TCOTS_ItemsMaterials.Manticore(), ArmorItem.Type.LEGGINGS, 10, 0.075))
            ));
            TCOTS_Items.MANTICORE_BOOTS = registerItem("manticore_boots", ()->new ManticoreArmorItem(
                    TCOTS_ItemsMaterials.Manticore(),
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.BOOTS.getDurability(20))
                            .attributes(SwordsAndArmorAttributes.addManticoreArmorAttributes(TCOTS_ItemsMaterials.Manticore(), ArmorItem.Type.BOOTS, 10, 0.075))
            ));

            TCOTS_Items.WARRIORS_LEATHER_JACKET = registerItem("warriors_leather_jacket", ()->new WarriorsLeatherArmorItem(
                    TCOTS_ItemsMaterials.WarriorsLeather(),
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()
                            .durability(ArmorItem.Type.CHESTPLATE.getDurability(15))
                            .attributes(SwordsAndArmorAttributes.addArmorWithAdrenalineAttributes(TCOTS_ItemsMaterials.WarriorsLeather(), ArmorItem.Type.CHESTPLATE, 0.02))
            ));
            TCOTS_Items.WARRIORS_LEATHER_TROUSERS = registerItem("warriors_leather_trousers", ()->new WarriorsLeatherArmorItem(
                    TCOTS_ItemsMaterials.WarriorsLeather(),
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
                            .durability(ArmorItem.Type.LEGGINGS.getDurability(15))
                            .attributes(SwordsAndArmorAttributes.addArmorWithAdrenalineAttributes(TCOTS_ItemsMaterials.WarriorsLeather(), ArmorItem.Type.LEGGINGS, 0.02))
            ));

            TCOTS_Items.WARRIORS_LEATHER_BOOTS = registerItem("warriors_leather_boots", ()->new WarriorsLeatherArmorItem(
                    TCOTS_ItemsMaterials.WarriorsLeather(),
                    ArmorItem.Type.BOOTS,
                    new Item.Properties()
                            .durability(ArmorItem.Type.BOOTS.getDurability(15))
                            .attributes(SwordsAndArmorAttributes.addArmorWithAdrenalineAttributes(TCOTS_ItemsMaterials.WarriorsLeather(), ArmorItem.Type.BOOTS, 0.02))
            ));

            TCOTS_Items.RAVENS_ARMOR = registerItem("ravens_armor", ()->new RavensArmorItem(
                    TCOTS_ItemsMaterials.Raven(),
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.CHESTPLATE.getDurability(30))
                            .attributes(SwordsAndArmorAttributes.addArmorWithAdrenalineAttributes(TCOTS_ItemsMaterials.Raven(), ArmorItem.Type.BODY, 0.10))
            ));
            TCOTS_Items.RAVENS_TROUSERS = registerItem("ravens_trousers", ()->new RavensArmorItem(
                    TCOTS_ItemsMaterials.Raven(),
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.LEGGINGS.getDurability(30))
                            .attributes(SwordsAndArmorAttributes.addArmorWithAdrenalineAttributes(TCOTS_ItemsMaterials.Raven(), ArmorItem.Type.LEGGINGS, 0.10))
            ));
            TCOTS_Items.RAVENS_BOOTS = registerItem("ravens_boots", ()->new RavensArmorItem(
                    TCOTS_ItemsMaterials.Raven(),
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.BOOTS.getDurability(30))
                            .attributes(SwordsAndArmorAttributes.addArmorWithAdrenalineAttributes(TCOTS_ItemsMaterials.Raven(), ArmorItem.Type.BOOTS, 0.10))
            ));
        }

        //Horse Armor
        {
            TCOTS_Items.TUNDRA_HORSE_ARMOR = registerItem("tundra_horse_armor",
                    () -> new WitcherHorseArmorItem(TCOTS_ItemsMaterials.HorseTundra(), "tundra", new Item.Properties().stacksTo(1),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.tundra_horse_armor").withStyle(ChatFormatting.AQUA),
                                    Component.translatable("tooltip.tcots_witcher.tundra_horse_armor2").withStyle(ChatFormatting.AQUA))));

            TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR = registerItem("knight_errants_horse_armor",
                    () -> new WitcherHorseArmorItem(TCOTS_ItemsMaterials.HorseKnight(), "knight_errants", new Item.Properties().stacksTo(1),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.knight_errants_horse_armor").withStyle(ChatFormatting.DARK_GREEN),
                                    Component.translatable("tooltip.tcots_witcher.knight_errants_horse_armor2").withStyle(ChatFormatting.DARK_GREEN))));
        }


        //Misc
        {
            TCOTS_Items.GIANT_ANCHOR=registerItem("giant_anchor", ()->
                    new GiantAnchorItem(TCOTS_ItemsMaterials.Anchor(), new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .attributes(SwordItem.createAttributes(TCOTS_ItemsMaterials.Anchor(), 1, -3.4f)))
            );
        }
    }

    // Misc
    public static RegistrySupplier<Item> NEST_SLAB_ITEM;
    public static RegistrySupplier<Item> NEST_SKULL_ITEM;
    public static RegistrySupplier<Item> MONSTER_NEST_ITEM;
    public static RegistrySupplier<Item> ALCHEMY_TABLE_ITEM;
    public static RegistrySupplier<Item> HERBAL_TABLE_ITEM;
    public static RegistrySupplier<Item> GIANT_ANCHOR_BLOCK_ITEM;
    public static RegistrySupplier<Item> WINTERS_BLADE_SKELETON_ITEM;
    public static RegistrySupplier<Item> SKELETON_BLOCK_ITEM;
    public static RegistrySupplier<Item> HERBAL_MIXTURE;
    public static RegistrySupplier<Item> WITCHER_BESTIARY;
    public static RegistrySupplier<Item> ALCHEMY_BOOK;

    public static void initItemsMisc(){
        TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM  = registerBlockItem("puffball_mushroom_block", TCOTS_Blocks::PuffballMushroomBlock);

        TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM  = registerBlockItem("sewant_mushroom_block", TCOTS_Blocks::SewantMushroomBlock);

        TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM  = registerBlockItem("sewant_mushroom_stem", TCOTS_Blocks::SewantMushroomStem);

        TCOTS_Items.NEST_SLAB_ITEM  = registerBlockItem("nest_slab", TCOTS_Blocks::NestSlab);

        TCOTS_Items.NEST_SKULL_ITEM = registerItem("nest_skull", ()-> new NestSkullItem(TCOTS_Blocks.NestSkull(), TCOTS_Blocks.NestWallSkull(), new Item.Properties(), Direction.DOWN));

        TCOTS_Items.MONSTER_NEST_ITEM = registerItem("monster_nest", ()-> new MonsterNestItem(TCOTS_Blocks.MonsterNest(), new Item.Properties()));

        TCOTS_Items.ALCHEMY_TABLE_ITEM = registerItem("alchemy_table", ()-> new AlchemyTableItem(TCOTS_Blocks.AlchemyTable(), new Item.Properties()));

        TCOTS_Items.HERBAL_TABLE_ITEM = registerItem("herbal_table", ()-> new HerbalTableItem(TCOTS_Blocks.HerbalTable(), new Item.Properties()));

        TCOTS_Items.HERBAL_MIXTURE = registerItem("herbal_mixture", ()-> new HerbalMixture(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build()).stacksTo(2)));


        TCOTS_Items.GIANT_ANCHOR_BLOCK_ITEM = registerItem("giant_anchor_block", ()-> new GiantAnchorBlockItem(TCOTS_Blocks.GiantAnchor(), new Item.Properties()));

        TCOTS_Items.WINTERS_BLADE_SKELETON_ITEM = registerItem("winters_blade_skeleton", ()-> new WintersBladeSkeletonItem(TCOTS_Blocks.WintersBladeSkeleton(), new Item.Properties()));

        TCOTS_Items.SKELETON_BLOCK_ITEM = registerItem("skeleton_block", ()-> new SkeletonBlockItem(TCOTS_Blocks.SkeletonBlock(), new Item.Properties()));


        TCOTS_Items.WITCHER_BESTIARY = registerItem("witcher_bestiary", TCOTS_Items::createWitcherBestiary);

        TCOTS_Items.ALCHEMY_BOOK = registerItem("alchemy_book", TCOTS_Items::createAlchemyAlmanac);
    }

    @ExpectPlatform
    public static Item createWitcherBestiary(){ throw new AssertionError();}

    @ExpectPlatform
    public static Item createAlchemyAlmanac(){ throw new AssertionError();}


    @ExpectPlatform
    public static DataComponentType<String> RefillRecipe() {
        throw new AssertionError();
    }
    @ExpectPlatform
    public static DataComponentType<MonsterOilComponent> MonsterOilComponent() { throw new AssertionError(); }
    @ExpectPlatform
    public static DataComponentType<Boolean> AnchorRetrieve() { throw new AssertionError(); }
    @ExpectPlatform
    public static DataComponentType<RecipeTeacherComponent> RecipeTeacher() { throw new AssertionError();}
    @ExpectPlatform
    public static DataComponentType<CustomEffectsComponent> CustomEffects() { throw new AssertionError();}

    @ExpectPlatform
    public static void initDataComponents(){ throw new AssertionError();}

    public static void initAllItems(){
        TCOTS_Items.initAlchemyIngredients();
        TCOTS_Items.initDrops();
        TCOTS_Items.initAlchemyConcoctions();
        TCOTS_Items.initWeaponsArmors();
        TCOTS_Items.initItemsMisc();
    }

    public static RegistrySupplier<OwoItemGroup> owoItemGroup;


    public static void initGroupItems() {

//        if(!Platform.isFabric()) {
            owoItemGroup = TCOTS_Registries.CREATIVE_MODE_TABS.register("main",
                    () -> createOwOGroup(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "main"),
                            owoItemGroup -> {

                                owoItemGroup.addCustomTab(Icon.of(TCOTS_Items.GVALCHIR.get()), "combat", TCOTS_Items::displayCombatTab, true);

                                owoItemGroup.addCustomTab(Icon.of(TCOTS_Items.ALCHEMY_BOOK.get()), "alchemy", TCOTS_Items::displayAlchemyTab, true);

                                owoItemGroup.addCustomTab(Icon.of(TCOTS_Items.ALCHEMY_FORMULA.get()), "formulae", TCOTS_Items::displayFormulaeTab, true);

                                owoItemGroup.addButton(ItemGroupButton.curseforge(owoItemGroup, "https://www.curseforge.com/members/tenebris_mors/projects"));
                                owoItemGroup.addButton(ItemGroupButton.modrinth(owoItemGroup, "https://modrinth.com/user/Tenebris_Mors"));
                                owoItemGroup.addButton(ItemGroupButton.github(owoItemGroup, "https://github.com/AngelDGr/The-Conjunction-of-the-Spheres"));
                            }, () ->
                            createAnimatedIcon(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/tab_icon_sheet.png"))
                    ));
//        }

//        if(Platform.isFabric())
//            TCOTS_Registries.CREATIVE_MODE_TABS.register("main", ()->
//                CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
//                .title(Component.translatable("itemGroup.tcots_witcher.main"))
//                .icon(() -> new ItemStack(TCOTS_Items.WITCHER_BESTIARY.get()))
//                .displayItems(TCOTS_Items::displayAll)
//                .build());
    }

    public static OwoItemGroup createOwOGroup(final ResourceLocation id,
                                              final Consumer<OwoItemGroup> initializer,
                                              final Supplier<Icon> iconSupplier) {

        return createOwOGroup(id, initializer, iconSupplier, 4, 4, null, null, null, true, false, true);
    }

    public static OwoItemGroup createOwOGroup(final ResourceLocation id,
                                              final Consumer<OwoItemGroup> initializer,
                                              final Supplier<Icon> iconSupplier,
                                              final int tabStackHeight, final int buttonStackHeight,
                                              @Nullable final ResourceLocation backgroundTexture,
                                              @Nullable final OwoItemGroup.ScrollerTextures scrollerTextures,
                                              @Nullable final OwoItemGroup.TabTextures tabTextures,
                                              final boolean useDynamicTitle,
                                              final boolean displaySingleTab,
                                              final boolean allowMultiSelect) {

        return new OwoItemGroup(id, initializer, iconSupplier, tabStackHeight, buttonStackHeight, backgroundTexture, scrollerTextures, tabTextures, useDynamicTitle, displaySingleTab, allowMultiSelect) {};
    }

    static Icon createAnimatedIcon(final ResourceLocation texture) {
        final var widget = new AnimatedTextureDrawable(
                0, 0,
                96, 96,
                texture,
                new SpriteSheetMetadata(1344, 1248, 96, 96), 200, true);

        return (context, x, y, mouseX, mouseY, delta) -> {
            context.getMatrixStack().pushPose();

            //Scales down to fit a 16x16 texture
            final float scaleValue= (float) 16 /96;

            //Moves the texture
            final double translationX=x* ((1/scaleValue));
            final double translationY=y* ((1/scaleValue));

            context.getMatrixStack().scale(scaleValue,scaleValue,1);
            context.getMatrixStack().translate(translationX,translationY,0);

            widget.render(context, mouseX, mouseY, delta);


            context.getMatrixStack().popPose();
        };
    }

    public static void displayCombatTab(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output entries){
        //Book
        entries.accept(TCOTS_Items.WITCHER_BESTIARY.get());

        //Spawn Eggs
        {
            entries.accept(TCOTS_Items.DROWNER_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.GHOUL_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.ALGHOUL_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.ROTFIEND_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.FOGLET_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.WATER_HAG_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.GRAVE_HAG_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.SCURVER_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.DEVOURER_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.BLOEDZUIGER_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.GRAVEIR_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.BULLVORE_SPAWN_EGG.get());

            entries.accept(TCOTS_Items.NEKKER_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.NEKKER_WARRIOR_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.CYCLOPS_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.ROCK_TROLL_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.ICE_TROLL_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.FOREST_TROLL_SPAWN_EGG.get());
            entries.accept(TCOTS_Items.ICE_GIANT_SPAWN_EGG.get());


            entries.accept(TCOTS_Items.GIANT_ANCHOR_BLOCK_ITEM.get());
            entries.accept(TCOTS_Items.GIANT_ANCHOR.get());
        }

        //Other Ingredients
        {
            entries.accept(TCOTS_Items.CURED_MONSTER_LEATHER.get());
        }

        //Weapons
        {

            entries.accept(TCOTS_Items.GVALCHIR.get());
            entries.accept(TCOTS_Items.MOONBLADE.get());
            entries.accept(TCOTS_Items.DYAEBL.get());
            entries.accept(TCOTS_Items.ARDAENYE.get());
            entries.accept(TCOTS_Items.WINTERS_BLADE.get());


            entries.accept(TCOTS_Items.KNIGHT_CROSSBOW.get());
            entries.accept(TCOTS_Items.BASE_BOLT.get());
            entries.accept(TCOTS_Items.BLUNT_BOLT.get());
            entries.accept(TCOTS_Items.PRECISION_BOLT.get());
            entries.accept(TCOTS_Items.EXPLODING_BOLT.get());
            entries.accept(TCOTS_Items.BROADHEAD_BOLT.get());
        }

        //Armors
        {
            entries.accept(TCOTS_Items.WARRIORS_LEATHER_JACKET.get());
            entries.accept(TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get());
            entries.accept(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get());

            entries.accept(TCOTS_Items.MANTICORE_ARMOR.get());
            entries.accept(TCOTS_Items.MANTICORE_TROUSERS.get());
            entries.accept(TCOTS_Items.MANTICORE_BOOTS.get());

            entries.accept(TCOTS_Items.RAVENS_ARMOR.get());
            entries.accept(TCOTS_Items.RAVENS_TROUSERS.get());
            entries.accept(TCOTS_Items.RAVENS_BOOTS.get());

            //Horse Armors
            entries.accept(TCOTS_Items.TUNDRA_HORSE_ARMOR.get());
            entries.accept(TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR.get());
        }

        //Misc/Blocks
        {
            entries.accept(TCOTS_Items.NEST_SLAB_ITEM.get());
            entries.accept(TCOTS_Items.NEST_SKULL_ITEM.get());
            entries.accept(TCOTS_Items.MONSTER_NEST_ITEM.get());
            entries.accept(TCOTS_Items.WINTERS_BLADE_SKELETON_ITEM.get());
            entries.accept(TCOTS_Items.SKELETON_BLOCK_ITEM.get());
        }
    }


    public static void displayAlchemyTab(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output entries){
        //Book
        entries.accept(TCOTS_Items.ALCHEMY_BOOK.get());

        //Alchemy
        {
            //Ingredients
            {
                entries.accept(TCOTS_Items.ALCHEMY_TABLE_ITEM.get());
                entries.accept(TCOTS_Items.HERBAL_TABLE_ITEM.get());
                entries.accept(TCOTS_Items.HERBAL_MIXTURE.get());

                entries.accept(TCOTS_Items.ALLSPICE.get());
                entries.accept(TCOTS_Items.ARENARIA.get());
                entries.accept(TCOTS_Items.CELANDINE.get());
                entries.accept(TCOTS_Items.BRYONIA.get());
                entries.accept(TCOTS_Items.CROWS_EYE.get());
                entries.accept(TCOTS_Items.VERBENA.get());
                entries.accept(TCOTS_Items.HAN_FIBER.get());
                entries.accept(TCOTS_Items.PUFFBALL.get());
                entries.accept(TCOTS_Items.SEWANT_MUSHROOMS.get());

                entries.accept(TCOTS_Items.ERGOT_SEEDS.get());

                entries.accept(TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM.get());
                entries.accept(TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM.get());
                entries.accept(TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM.get());

                entries.accept(TCOTS_Items.ICY_SPIRIT.get().getDefaultInstance());
                entries.accept(TCOTS_Items.CHERRY_CORDIAL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.VILLAGE_HERBAL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.MANDRAKE_CORDIAL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DWARVEN_SPIRIT.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ALCOHEST.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WHITE_GULL.get().getDefaultInstance());

                entries.accept(TCOTS_Items.MONSTER_FAT.get());
                entries.accept(TCOTS_Items.ALCHEMY_PASTE.get());
                entries.accept(TCOTS_Items.STAMMELFORDS_DUST.get());
                entries.accept(TCOTS_Items.ALCHEMISTS_POWDER.get());

                entries.accept(TCOTS_Items.AETHER.get());
                entries.accept(TCOTS_Items.HYDRAGENUM.get());
                entries.accept(TCOTS_Items.NIGREDO.get());
                entries.accept(TCOTS_Items.QUEBRITH.get());
                entries.accept(TCOTS_Items.REBIS.get());
                entries.accept(TCOTS_Items.RUBEDO.get());
                entries.accept(TCOTS_Items.VERMILION.get());
                entries.accept(TCOTS_Items.VITRIOL.get());
            }

            //Drops
            {
                //Necrophages
                entries.accept(TCOTS_Items.DROWNER_TONGUE.get());
                entries.accept(TCOTS_Items.DROWNER_BRAIN.get());
                entries.accept(TCOTS_Items.GHOUL_BLOOD.get());
                entries.accept(TCOTS_Items.ALGHOUL_BONE_MARROW.get());
                entries.accept(TCOTS_Items.ROTFIEND_BLOOD.get());
                entries.accept(TCOTS_Items.FOGLET_TEETH.get());
                entries.accept(TCOTS_Items.WATER_ESSENCE.get());
                entries.accept(TCOTS_Items.WATER_HAG_MUD_BALL.get());
                entries.accept(TCOTS_Items.SCURVER_SPINE.get());
                entries.accept(TCOTS_Items.DEVOURER_TEETH.get());
                entries.accept(TCOTS_Items.BLOEDZUIGER_BLOOD.get());
                entries.accept(TCOTS_Items.CADAVERINE.get());
                entries.accept(TCOTS_Items.GRAVEIR_BONE.get());
                entries.accept(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get());

                //Ogroids
                entries.accept(TCOTS_Items.NEKKER_EYE.get());
                entries.accept(TCOTS_Items.NEKKER_HEART.get());
                entries.accept(TCOTS_Items.CAVE_TROLL_LIVER.get());

                //Mutagens
                {
                    entries.accept(TCOTS_Items.FOGLET_MUTAGEN.get());
                    entries.accept(TCOTS_Items.WATER_HAG_MUTAGEN.get());
                    entries.accept(TCOTS_Items.GRAVE_HAG_MUTAGEN.get());
                    entries.accept(TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get());
                    entries.accept(TCOTS_Items.TROLL_MUTAGEN.get());
                }
            }

            //Potions
            {
                entries.accept(TCOTS_Items.SWALLOW_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SWALLOW_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SWALLOW_POTION_SUPERIOR.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SWALLOW_SPLASH.get().getDefaultInstance());

                entries.accept(TCOTS_Items.CAT_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.CAT_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.CAT_POTION_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.get().getDefaultInstance());

                entries.accept(TCOTS_Items.KILLER_WHALE_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.KILLER_WHALE_SPLASH.get().getDefaultInstance());

                entries.accept(TCOTS_Items.BLACK_BLOOD_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.MARIBOR_FOREST_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.get().getDefaultInstance());

                // W1
                entries.accept(TCOTS_Items.WOLF_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WOLF_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WOLF_POTION_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.BINDWEED_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.BINDWEED_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.BINDWEED_POTION_SUPERIOR.get().getDefaultInstance());

                // W2
                entries.accept(TCOTS_Items.ROOK_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ROOK_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ROOK_POTION_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.WHITE_HONEY_POTION.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.get().getDefaultInstance());

                //Decoctions
                {
                    entries.accept(TCOTS_Items.WATER_HAG_DECOCTION.get());
                    entries.accept(TCOTS_Items.GRAVE_HAG_DECOCTION.get());
                    entries.accept(TCOTS_Items.ALGHOUL_DECOCTION.get());
                    entries.accept(TCOTS_Items.FOGLET_DECOCTION.get());
                    entries.accept(TCOTS_Items.NEKKER_WARRIOR_DECOCTION.get());
                    entries.accept(TCOTS_Items.TROLL_DECOCTION.get());
                }
            }

            // Bombs
            {
                entries.accept(TCOTS_Items.GRAPESHOT.get().getDefaultInstance());
                entries.accept(TCOTS_Items.GRAPESHOT_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.GRAPESHOT_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.SAMUM.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SAMUM_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SAMUM_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.DANCING_STAR.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DANCING_STAR_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DANCING_STAR_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.DEVILS_PUFFBALL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.DRAGONS_DREAM.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DRAGONS_DREAM_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.NORTHERN_WIND.get().getDefaultInstance());
                entries.accept(TCOTS_Items.NORTHERN_WIND_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.NORTHERN_WIND_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.DIMERITIUM_BOMB.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR.get().getDefaultInstance());

                entries.accept(TCOTS_Items.MOON_DUST.get().getDefaultInstance());
                entries.accept(TCOTS_Items.MOON_DUST_ENHANCED.get().getDefaultInstance());
                entries.accept(TCOTS_Items.MOON_DUST_SUPERIOR.get().getDefaultInstance());
            }

            // Monster Oils
            {
                entries.accept(TCOTS_Items.NECROPHAGE_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get().getDefaultInstance());

                entries.accept(TCOTS_Items.OGROID_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ENHANCED_OGROID_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SUPERIOR_OGROID_OIL.get().getDefaultInstance());

                entries.accept(TCOTS_Items.BEAST_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ENHANCED_BEAST_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SUPERIOR_BEAST_OIL.get().getDefaultInstance());

                entries.accept(TCOTS_Items.HANGED_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.ENHANCED_HANGED_OIL.get().getDefaultInstance());
                entries.accept(TCOTS_Items.SUPERIOR_HANGED_OIL.get().getDefaultInstance());
            }
        }
    }

    public static void displayFormulaeTab(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output entries){
        //Formulae
        addFormulaeEntries(entries);
    }

    public static void displayAll(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output entries){
        displayCombatTab(itemDisplayParameters, entries);

        displayAlchemyTab(itemDisplayParameters, entries);

        displayFormulaeTab(itemDisplayParameters, entries);
    }

    @SuppressWarnings("unused")
    public static void displayAllItems(final CreativeModeTab.ItemDisplayParameters itemDisplayParameters, final CreativeModeTab.Output entries){
        //Combat Tab
        displayCombatTab(itemDisplayParameters, entries);

        //Alchemy Tab
        displayAlchemyTab(itemDisplayParameters, entries);

        //Formulae
        displayFormulaeTab(itemDisplayParameters, entries);
    }

    private static void addFormulaeEntries(final CreativeModeTab.Output entries){
        final List<Item> listPotions = new ArrayList<>();
        final List<Item> listBombs = new ArrayList<>();
        final List<Item> listOils = new ArrayList<>();

        final List<Item> listMisc = new ArrayList<>();

        BuiltInRegistries.ITEM.forEach(item ->
                {
                    if(item instanceof WitcherPotions_Base && !(item instanceof WitcherAlcohol_Base)) listPotions.add(item);

                    if(item instanceof WitcherBombs_Base) listBombs.add(item);

                    if(item instanceof WitcherMonsterOil_Base) listOils.add(item);

                    if(AlchemyFormulaUtil.isMiscItem(item))
                        listMisc.add(item);
                }
        );


        for(final Item potion: listPotions){
            if(potion instanceof final WitcherPotions_Base witcherPotion && !(potion instanceof WitcherPotionsSplash_Base))
                entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(potion), witcherPotion.isDecoction()));

            if(potion instanceof WitcherPotionsSplash_Base)
                entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(potion)));
        }

        for(final Item bomb: listBombs){
            entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(bomb)));
        }

        for(final Item oil: listOils){
            entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(oil)));
        }

        for(final Item misc: listMisc){
            entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(misc)));
        }

    }

    public static RegistrySupplier<Item> registerItem(final String name, final Supplier<Item> item) {
        return TCOTS_Registries.ITEMS.register(name, item);
    }

    private static RegistrySupplier<WitcherAlcohol_Base> registerAlcohol(final String name, final Supplier<WitcherAlcohol_Base> item) {
        return TCOTS_Registries.ITEMS.register(name, item);
    }

    /**
     Creates a witcher potion of type decoction
     @param name Name of the potion
     @param effect Decoction effect
     */
    private static RegistrySupplier<Item> registerItemPotion(final String name, final ResourceLocation effect) {
        return registerItemPotion(name, new Item.Properties().stacksTo(1), effect, 50, 600, 0, true);
    }

    /**
     Creates a witcher potion
     @param name Name of the potion
     @param effect Decoction effect
     @param  settings FabricSettings
     @param toxicity Toxicity points
     @param durationInSecs Duration in seconds of the effect
     @param amplifier Amplifier of the effect
     */
    private static RegistrySupplier<Item> registerItemPotion(final String name, final Item.Properties settings, final ResourceLocation effect, final int toxicity, final int durationInSecs, final int amplifier, final boolean decoction) {
        try {

            final Supplier<WitcherPotions_Base> witcherPotion = () -> new WitcherPotions_Base(settings, effect, toxicity, durationInSecs, amplifier, decoction);

            return TCOTS_Registries.ITEMS.register(name,  witcherPotion);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error registering potion");
        }
    }

    private static RegistrySupplier<Item> registerSplashPotion(final String name, final Item.Properties settings, final ResourceLocation effect, final int toxicity, final int durationInSecs){
        try {
//            ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name);

            final Supplier<WitcherPotions_Base> witcherPotion = ()-> new WitcherPotionsSplash_Base(settings, new MobEffectInstance(TCOTS_Effects.getHolder(effect), (int) (durationInSecs / 0.05), 0), toxicity);
            return TCOTS_Registries.ITEMS.register(name,  witcherPotion);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error registering Splash potion");
        }
    }

    private static RegistrySupplier<Item> registerBlockItem(final String name, final Supplier<Block> block){
        return TCOTS_Registries.ITEMS.register(name, ()-> new BlockItem(block.get(), new Item.Properties()));
    }

    @SuppressWarnings("all")
    private static RegistrySupplier<LootItemFunctionType<? extends LootItemConditionalFunction>> registerLootFunction(String id, MapCodec<? extends LootItemFunction> codec) {
        return TCOTS_Registries.LOOT_FUNCTIONS.register(id, ()-> new LootItemFunctionType(codec));
    }
}
