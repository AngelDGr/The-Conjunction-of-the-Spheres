package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.blocks.AlchemyTableItem;
import TCOTS.items.blocks.HerbalTableItem;
import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.blocks.NestSkullItem;
import TCOTS.items.concoctions.*;
import TCOTS.items.weapons.BoltItem;
import TCOTS.items.weapons.GvalchirSword;
import TCOTS.items.weapons.KnightCrossbow;
import TCOTS.items.weapons.ScurverSpineItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class TCOTS_Items {

    //xTODO: Add Bombs
    //xTODO: Add Bombs Entry
    //xTODO: Add new way to craft the potions
        // xTODO: Add new alchemy ingredients (mushrooms, flowers)
            //Plants
            //xTODO: Allspice = Moleyarrow = Mandrake root
            //xTODO: Arenaria
            //xTODO: Balisse fruit - Sweet Berries
            //xTODO: Beggartick blossoms - Poppy Petals
            //xTODO: Berbercane fruit - Glow Berries
            //xTODO: Blowball - Dandelion
            //xTODO: Bryonia
            //xTODO: Buckthorn - Kelp
            //xTODO: Celandine
            //xTODO: Cortinarius - Brown Mushroom
            //xTODO: Crow's eye
            //xTODO: Ergot seeds - Poisonous potato like
            //xTODO: Fool's parsley leaves - Azure Bluet Petals
            //xTODO: Ginatia petals - Cornflower
            //xTODO: Green mold && Bloodmoss- Moss Block
            //xTODO: Han fiber
            //xTODO: Hellebore petals - Allium
            //xTODO: Honeysuckle - Honey Bottle
            //xTODO: Hop umbels && Bison Grass - Beetroot
            //xTODO: Longrube - Red mushroom
            //xTODO: Mistletoe - Oxeye Daisy Petals
            //xTODO: Nostrix && Hornwort- Glow Lichen
            //xTODO: Puffball
            //xTODO: Pringrape - Flowering Azalea
            //xTODO: Ranogrin - Fern
            //xTODO: Ribleaf - Bunch of Leaves
            //xTODO: Sewant mushrooms
            //xTODO: Verbena
            //xTODO: White myrtle petals - Lily of the Valley Petals
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

            //TODO: Petri's Philter: Add when added specters
            //TODO: Full Moon: Add when added Nightwraiths
            //TODO: Golden Oriole: Add when added Noonwraiths

            //TODO: Tawny Owl: Add when added Arachas
            //TODO: Thunderbolt: Add when added Endregas
            //TODO: Blizzard: Add when added Golems

            //Witcher 2 Potions
            //xTODO: Rook: Increases damage with swords
            //TODO: Brock: Makes possible apply different effects to mobs, but any damage it's received double
            //TODO: Gadwall: Increases a lot the regeneration and health boost but also weakness
            //TODO: Stammelford's Philtre: Something???

            //Witcher 1 Potions
            //xTODO: Wolf: Makes critical hits stronger
            //TODO: Willow: Makes you immune to knockback
            //TODO: De Vries' Extract: Gives glowing to near invisible enemies
            //TODO: Wolverine: Makes you stronger when you have less health?


    //xTODO: Add use to the items
        //xTODO: Drowner Tongue usable for Killer Whale potion
            //xTODO: Add the Killer Whale effect, improves respiration and attack underwater
        //xTODO: Drowner Brain usable for a new potion, Swallow:
            //xTODO: Add the Swallow effect, works like regeneration, it's a lot slower but the potion it's more durable
        //xTODO: Nekker eye usable for Hanged Man Oil
        //xTODO: Nekker Hearth usable for the White Raffard's Decoction
            //xTODO: Add the White Raffard's Decoction, works similar to the instant health, but works with percentage

    public static final TagKey<Item> DECAYING_FLESH = TagKey.of(RegistryKeys.ITEM, new Identifier(TCOTS_Main.MOD_ID, "decaying_flesh"));


    //TODO: Fix the achievement to mobs works with Monster Hunter
    public static Item DROWNER_SPAWN_EGG;
    public static Item DROWNER_TONGUE;
    public static Item DROWNER_BRAIN;

    public static Item ROTFIEND_SPAWN_EGG;
    public static Item ROTFIEND_BLOOD;


    public static Item GRAVE_HAG_SPAWN_EGG;
    public static Item GRAVE_HAG_MUTAGEN;

    public static Item WATER_HAG_SPAWN_EGG;
    public static Item WATER_HAG_MUD_BALL;
    public static Item WATER_HAG_MUTAGEN;
    public static Item WATER_ESSENCE;


    public static Item FOGLET_SPAWN_EGG;
    public static Item FOGLET_TEETH;
    public static Item FOGLET_MUTAGEN;

    public static Item GHOUL_SPAWN_EGG;
    public static Item GHOUL_BLOOD;

    public static Item ALGHOUL_SPAWN_EGG;
    public static Item ALGHOUL_BONE_MARROW;

    public static Item SCURVER_SPAWN_EGG;
    public static Item SCURVER_SPINE;

    public static Item DEVOURER_SPAWN_EGG;
    public static Item DEVOURER_TEETH;

    public static Item GRAVEIR_SPAWN_EGG;
    public static Item CADAVERINE;
    // xTODO: Add Cadaverine drops to:
    //  xGhoul
    //  xAlghoul
    //  xRotfiend
    //  xFoglet
    //  xScurver
    //  xDevourer
    //TODO: Add more uses to Cadaverine
    public static Item GRAVEIR_BONE;
    //TODO: Add more uses to Graveir Bone

    public static Item BULLVORE_SPAWN_EGG;
    public static Item BULLVORE_HORN_FRAGMENT;

    public static Item NEKKER_SPAWN_EGG;
    public static Item NEKKER_HEART;
    public static Item NEKKER_EYE;



    //Register Drops from monsters
    public static void registerDrops() {
        DROWNER_SPAWN_EGG = registerItem("drowner_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.DROWNER, 0x8db1c0, 0x9fa3ae, new FabricItemSettings()));
        DROWNER_TONGUE = registerItem("drowner_tongue",
                new Item(new FabricItemSettings()));
        DROWNER_BRAIN = registerItem("drowner_brain",
                new Item(new FabricItemSettings().maxCount(16)));


        ROTFIEND_SPAWN_EGG = registerItem("rotfiend_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ROTFIEND, 0xb3867b, 0xe6e1bc, new FabricItemSettings()));
        ROTFIEND_BLOOD = registerItem("rotfiend_blood",
                new Item(new FabricItemSettings()));


        GRAVE_HAG_SPAWN_EGG = registerItem("grave_hag_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GRAVE_HAG, 0xb6b692, 0x8e8480, new FabricItemSettings()));
        GRAVE_HAG_MUTAGEN = registerItem("grave_hag_mutagen",
                new Item(new FabricItemSettings().maxCount(8)));


        WATER_HAG_SPAWN_EGG = registerItem("water_hag_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.WATER_HAG, 0x8d93c0, 0x780b17, new FabricItemSettings()));
        WATER_HAG_MUD_BALL = registerItem("water_hag_mud_ball",
                new WaterHag_MudBallItem(new FabricItemSettings().maxCount(16)));
        WATER_HAG_MUTAGEN = registerItem("water_hag_mutagen",
                new Item(new FabricItemSettings().maxCount(8)));
        WATER_ESSENCE = registerItem("water_essence",
                new Item(new FabricItemSettings()));


        FOGLET_SPAWN_EGG = registerItem("foglet_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.FOGLET, 0x4a3f3f, 0x211c1c, new FabricItemSettings()));
        FOGLET_TEETH = registerItem("foglet_teeth",
                new Item(new FabricItemSettings()));
        FOGLET_MUTAGEN = registerItem("foglet_mutagen",
                new Item(new FabricItemSettings().maxCount(8)));


        GHOUL_SPAWN_EGG = registerItem("ghoul_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GHOUL, 0xd69d76, 0x0e0a07, new FabricItemSettings()));
        GHOUL_BLOOD = registerItem("ghoul_blood",
                new Item(new FabricItemSettings()));


        ALGHOUL_SPAWN_EGG = registerItem("alghoul_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ALGHOUL, 0x513e3d, 0x000000,
                        new FabricItemSettings()));
        ALGHOUL_BONE_MARROW = registerItem("alghoul_bone_marrow",
                new Item(new FabricItemSettings()));


        SCURVER_SPAWN_EGG = registerItem("scurver_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.SCURVER, 0xc0887a, 0x661f1f, new FabricItemSettings()));

        SCURVER_SPINE = registerItem("scurver_spine",
                new ScurverSpineItem(new FabricItemSettings().maxCount(16)));


        DEVOURER_SPAWN_EGG = registerItem("devourer_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.DEVOURER, 0x606c68, 0x1f1f1f, new FabricItemSettings()));
        DEVOURER_TEETH = registerItem("devourer_teeth",
                new Item(new FabricItemSettings()));

        GRAVEIR_SPAWN_EGG = registerItem("graveir_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GRAVEIR, 0xab706d, 0x882925, new FabricItemSettings()));
        CADAVERINE = registerItem("cadaverine",
                new Item(new FabricItemSettings()));
        GRAVEIR_BONE = registerItem("graveir_bone",
                new Item(new FabricItemSettings()));

        BULLVORE_SPAWN_EGG = registerItem("bullvore_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.BULLVORE, 0xdad29a, 0xb1816d, new FabricItemSettings()));
        BULLVORE_HORN_FRAGMENT = registerItem("bullvore_horn_fragment",
                new Item(new FabricItemSettings()));

        NEKKER_SPAWN_EGG = registerItem("nekker_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.NEKKER, 0xa59292, 0x705c5c,
                        new FabricItemSettings()));

        NEKKER_EYE = registerItem("nekker_eye",
                new Item(
                        new FabricItemSettings()));

        NEKKER_HEART = registerItem("nekker_heart",
                new Item(
                        new FabricItemSettings().maxCount(16)));


    }

    public static Item ALCHEMY_FORMULA;
    //Decoctions
    public static Item EMPTY_MONSTER_DECOCTION;
    public static Item GRAVE_HAG_DECOCTION;
    public static Item WATER_HAG_DECOCTION;
    public static Item FOGLET_DECOCTION;
    public static Item ALGHOUL_DECOCTION;

    //Potions

    //W1
    public static Item WOLF_POTION;
    public static Item WOLF_POTION_ENHANCED;
    public static Item WOLF_POTION_SUPERIOR;

    //W2
    public static Item ROOK_POTION;
    public static Item ROOK_POTION_ENHANCED;
    public static Item ROOK_POTION_SUPERIOR;

    //W3
    public static Item SWALLOW_POTION;
    public static Item SWALLOW_POTION_ENHANCED;
    public static Item SWALLOW_POTION_SUPERIOR;
    public static Item WHITE_RAFFARDS_DECOCTION;
    public static Item WHITE_RAFFARDS_DECOCTION_ENHANCED;
    public static Item WHITE_RAFFARDS_DECOCTION_SUPERIOR;
    public static Item CAT_POTION;
    public static Item CAT_POTION_ENHANCED;
    public static Item CAT_POTION_SUPERIOR;
    public static Item BLACK_BLOOD_POTION;
    public static Item BLACK_BLOOD_POTION_ENHANCED;
    public static Item BLACK_BLOOD_POTION_SUPERIOR;
    public static Item MARIBOR_FOREST_POTION;
    public static Item MARIBOR_FOREST_POTION_ENHANCED;
    public static Item MARIBOR_FOREST_POTION_SUPERIOR;
    public static Item KILLER_WHALE_POTION;
    public static Item WHITE_HONEY_POTION;
    public static Item WHITE_HONEY_POTION_ENHANCED;
    public static Item WHITE_HONEY_POTION_SUPERIOR;


    public static Item EMPTY_WITCHER_POTION_2;
    public static Item EMPTY_WITCHER_POTION_3;
    public static Item EMPTY_WITCHER_POTION_4;
    public static Item EMPTY_WITCHER_POTION_5;


    //Splash Potions
    public static Item SWALLOW_SPLASH;
    public static Item KILLER_WHALE_SPLASH;
    public static Item WHITE_RAFFARDS_DECOCTION_SPLASH;

    public static Item NECROPHAGE_OIL;
    public static Item ENHANCED_NECROPHAGE_OIL;
    public static Item SUPERIOR_NECROPHAGE_OIL;

    public static Item OGROID_OIL;
    public static Item ENHANCED_OGROID_OIL;
    public static Item SUPERIOR_OGROID_OIL;

    public static Item BEAST_OIL;
    public static Item ENHANCED_BEAST_OIL;
    public static Item SUPERIOR_BEAST_OIL;

    public static Item HANGED_OIL;
    public static Item ENHANCED_HANGED_OIL;
    public static Item SUPERIOR_HANGED_OIL;

    public static Item EMPTY_OIL;

    //Bombs
    public static Item EMPTY_BOMB_POWDER_2;
    public static Item EMPTY_BOMB_POWDER_3;
    public static Item EMPTY_BOMB_POWDER_4;

    public static Item GRAPESHOT;
    public static Item GRAPESHOT_ENHANCED;
    public static Item GRAPESHOT_SUPERIOR;

    public static Item DANCING_STAR;
    public static Item DANCING_STAR_ENHANCED;
    public static Item DANCING_STAR_SUPERIOR;

    public static Item DEVILS_PUFFBALL;
    public static Item DEVILS_PUFFBALL_ENHANCED;
    public static Item DEVILS_PUFFBALL_SUPERIOR;

    public static Item SAMUM;
    public static Item SAMUM_ENHANCED;
    public static Item SAMUM_SUPERIOR;

    public static Item NORTHERN_WIND;
    public static Item NORTHERN_WIND_ENHANCED;
    public static Item NORTHERN_WIND_SUPERIOR;

    public static Item DRAGONS_DREAM;
    public static Item DRAGONS_DREAM_ENHANCED;
    public static Item DRAGONS_DREAM_SUPERIOR;

    public static Item DIMERITIUM_BOMB;
    public static Item DIMERITIUM_BOMB_ENHANCED;
    public static Item DIMERITIUM_BOMB_SUPERIOR;

    public static Item MOON_DUST;
    public static Item MOON_DUST_ENHANCED;
    public static Item MOON_DUST_SUPERIOR;

    public static void registerAlchemyConcoctions() {

        ALCHEMY_FORMULA = registerItem("alchemy_formula",
                new AlchemyFormulaItem(new FabricItemSettings().maxCount(8))
        );

        //Potions
        {
            {
                EMPTY_WITCHER_POTION_2 = registerItem("empty_witcher_potion2",
                        new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(2))
                );

                EMPTY_WITCHER_POTION_3 = registerItem("empty_witcher_potion3",
                        new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(3))
                );

                EMPTY_WITCHER_POTION_4 = registerItem("empty_witcher_potion4",
                        new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(4))
                );

                EMPTY_WITCHER_POTION_5 = registerItem("empty_witcher_potion5",
                        new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(5))
                );

                //Swallow
                {
                    SWALLOW_POTION = registerItemPotion("swallow_potion",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            0,
                            false
                    );

                    SWALLOW_POTION_ENHANCED = registerItemPotion("swallow_potion_enhanced",
                            new FabricItemSettings().maxCount(4),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            1,
                            false
                    );

                    SWALLOW_POTION_SUPERIOR = registerItemPotion("swallow_potion_superior",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            2,
                            false
                    );

                    SWALLOW_SPLASH = registerSplashPotion("swallow_splash",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            10,
                            18
                    );
                }

                //Cat
                {
                    CAT_POTION = registerItemPotion("cat_potion",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            60,
                            0,
                            false
                    );

                    CAT_POTION_ENHANCED = registerItemPotion("cat_potion_enhanced",
                            new FabricItemSettings().maxCount(4),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            120,
                            1,
                            false
                    );

                    CAT_POTION_SUPERIOR = registerItemPotion("cat_potion_superior",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            180,
                            2,
                            false
                    );
                }

                //White Raffard's
                {
                    WHITE_RAFFARDS_DECOCTION = registerItemPotion("white_raffards_decoction",
                            new FabricItemSettings().maxCount(2),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            0,
                            false
                    );

                    WHITE_RAFFARDS_DECOCTION_ENHANCED = registerItemPotion("white_raffards_decoction_enhanced",
                            new FabricItemSettings().maxCount(2),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            1,
                            false
                    );

                    WHITE_RAFFARDS_DECOCTION_SUPERIOR = registerItemPotion("white_raffards_decoction_superior",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            2,
                            false
                    );


                    WHITE_RAFFARDS_DECOCTION_SPLASH = registerSplashPotion("white_raffards_splash",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            15,
                            1
                    );
                }

                //Killer Whale
                {
                    KILLER_WHALE_POTION = registerItemPotion("killer_whale_potion",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.KILLER_WHALE_EFFECT,
                            15,
                            90,
                            0,
                            false
                    );

                    KILLER_WHALE_SPLASH = registerSplashPotion("killer_whale_splash",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.KILLER_WHALE_EFFECT,
                            10,
                            75
                    );
                }

                //Black Blood
                {
                    BLACK_BLOOD_POTION = registerItemPotion("black_blood_potion",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    BLACK_BLOOD_POTION_ENHANCED = registerItemPotion("black_blood_potion_enhanced",
                            new FabricItemSettings().maxCount(4),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            45,
                            1,
                            false
                    );

                    BLACK_BLOOD_POTION_SUPERIOR = registerItemPotion("black_blood_potion_superior",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            60,
                            2,
                            false
                    );
                }

                //Maribor Forest
                {
                    MARIBOR_FOREST_POTION = registerItemPotion("maribor_forest_potion",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            30,
                            0,
                            false
                    );

                    MARIBOR_FOREST_POTION_ENHANCED = registerItemPotion("maribor_forest_potion_enhanced",
                            new FabricItemSettings().maxCount(4),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            60,
                            1,
                            false
                    );

                    MARIBOR_FOREST_POTION_SUPERIOR = registerItemPotion("maribor_forest_potion_superior",
                            new FabricItemSettings().maxCount(5),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            90,
                            2,
                            false
                    );
                }

                //Wolf
                {
                    WOLF_POTION = registerItemPotion("wolf_potion",
                            new FabricItemSettings().maxCount(2),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    WOLF_POTION_ENHANCED = registerItemPotion("wolf_potion_enhanced",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            45,
                            1,
                            false
                    );

                    WOLF_POTION_SUPERIOR = registerItemPotion("wolf_potion_superior",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            60,
                            2,
                            false
                    );
                }

                //Rook
                {
                    ROOK_POTION = registerItemPotion("rook_potion",
                            new FabricItemSettings().maxCount(2),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            45,
                            0,
                            false
                    );

                    ROOK_POTION_ENHANCED = registerItemPotion("rook_potion_enhanced",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            60,
                            1,
                            false
                    );

                    ROOK_POTION_SUPERIOR = registerItemPotion("rook_potion_superior",
                            new FabricItemSettings().maxCount(3),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            90,
                            2,
                            false
                    );
                }

                //White Honey
                {
                    WHITE_HONEY_POTION = registerItem("white_honey_potion",
                            new WitcherWhiteHoney(new FabricItemSettings().maxCount(1))
                    );

                    WHITE_HONEY_POTION_ENHANCED = registerItem("white_honey_potion_enhanced",
                            new WitcherWhiteHoney(new FabricItemSettings().maxCount(2))
                    );

                    WHITE_HONEY_POTION_SUPERIOR = registerItem("white_honey_potion_superior",
                            new WitcherWhiteHoney(new FabricItemSettings().maxCount(5))
                    );
                }
            }

            //Decoctions
            {
                EMPTY_MONSTER_DECOCTION = registerItem("empty_monster_decoction",
                        new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(1))
                );

                GRAVE_HAG_DECOCTION = registerItemPotion("grave_hag_decoction",
                        new FabricItemSettings().maxCount(1),
                        TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT,
                        50,
                        1200,
                        0,
                        true
                );

                WATER_HAG_DECOCTION = registerItemPotion("water_hag_decoction",
                        new FabricItemSettings().maxCount(1),
                        TCOTS_Effects.WATER_HAG_DECOCTION_EFFECT,
                        50,
                        1200,
                        0,
                        true
                );

                ALGHOUL_DECOCTION = registerItemPotion("alghoul_decoction",
                        new FabricItemSettings().maxCount(1),
                        TCOTS_Effects.ALGHOUL_DECOCTION_EFFECT,
                        50,
                        1200,
                        0,
                        true
                );

                FOGLET_DECOCTION = registerItemPotion("foglet_decoction",
                        new FabricItemSettings().maxCount(1),
                        TCOTS_Effects.FOGLET_DECOCTION_EFFECT,
                        50,
                        1200,
                        0,
                        true
                );
            }
        }

        //Bombs
        {
            EMPTY_BOMB_POWDER_2 = registerItem("bomb_powder_2",
                    new EmptyBombPowderItem(new FabricItemSettings().maxCount(2)));

            EMPTY_BOMB_POWDER_3 = registerItem("bomb_powder_3",
                    new EmptyBombPowderItem(new FabricItemSettings().maxCount(3)));

            EMPTY_BOMB_POWDER_4 = registerItem("bomb_powder_4",
                    new EmptyBombPowderItem(new FabricItemSettings().maxCount(4)));


            GRAPESHOT = registerItem("grapeshot",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "grapeshot", 0));

            GRAPESHOT_ENHANCED = registerItem("grapeshot_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "grapeshot", 1));

            GRAPESHOT_SUPERIOR = registerItem("grapeshot_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "grapeshot", 2));


            SAMUM = registerItem("samum",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "samum", 0));

            SAMUM_ENHANCED = registerItem("samum_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "samum", 1));

            SAMUM_SUPERIOR = registerItem("samum_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "samum", 2));


            DANCING_STAR = registerItem("dancing_star",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "dancing_star", 0));

            DANCING_STAR_ENHANCED = registerItem("dancing_star_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "dancing_star", 1));

            DANCING_STAR_SUPERIOR = registerItem("dancing_star_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "dancing_star", 2));


            DEVILS_PUFFBALL = registerItem("devils_puffball",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "devils_puffball", 0));

            DEVILS_PUFFBALL_ENHANCED = registerItem("devils_puffball_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "devils_puffball", 1));

            DEVILS_PUFFBALL_SUPERIOR = registerItem("devils_puffball_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "devils_puffball", 2));


            NORTHERN_WIND = registerItem("northern_wind",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "northern_wind", 0));

            NORTHERN_WIND_ENHANCED = registerItem("northern_wind_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "northern_wind", 1));

            NORTHERN_WIND_SUPERIOR = registerItem("northern_wind_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "northern_wind", 2));


            DRAGONS_DREAM = registerItem("dragons_dream",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "dragons_dream", 0));

            DRAGONS_DREAM_ENHANCED = registerItem("dragons_dream_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "dragons_dream", 1));

            DRAGONS_DREAM_SUPERIOR = registerItem("dragons_dream_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "dragons_dream", 2));


            DIMERITIUM_BOMB = registerItem("dimeritium_bomb",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "dimeritium_bomb", 0));

            DIMERITIUM_BOMB_ENHANCED = registerItem("dimeritium_bomb_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "dimeritium_bomb", 1));

            DIMERITIUM_BOMB_SUPERIOR = registerItem("dimeritium_bomb_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "dimeritium_bomb", 2));


            MOON_DUST = registerItem("moon_dust",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(2), "moon_dust", 0));

            MOON_DUST_ENHANCED = registerItem("moon_dust_enhanced",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(3), "moon_dust", 1));

            MOON_DUST_SUPERIOR = registerItem("moon_dust_superior",
                    new WitcherBombs_Base(new FabricItemSettings().maxCount(4), "moon_dust", 2));

        }

        //Oils
        {
            EMPTY_OIL = registerItem("empty_oil",
                    new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(1))
            );

            NECROPHAGE_OIL = registerItem("oil_necrophage",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON), TCOTS_Entities.NECROPHAGES, 20, 1)
            );
            ENHANCED_NECROPHAGE_OIL = registerItem("oil_necrophage_enhanced",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.NECROPHAGES, 40, 2)
            );
            SUPERIOR_NECROPHAGE_OIL = registerItem("oil_necrophage_superior",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.NECROPHAGES, 60, 3)
            );

            OGROID_OIL = registerItem("oil_ogroid",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.OGROIDS, 20, 1)
            );
            ENHANCED_OGROID_OIL = registerItem("oil_ogroid_enhanced",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.OGROIDS, 40, 2)
            );
            SUPERIOR_OGROID_OIL = registerItem("oil_ogroid_superior",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.OGROIDS, 60, 3)
            );

            BEAST_OIL = registerItem("oil_beast",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.BEASTS, 20, 1)
            );
            ENHANCED_BEAST_OIL = registerItem("oil_beast_enhanced",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.BEASTS, 40, 2)
            );
            SUPERIOR_BEAST_OIL = registerItem("oil_beast_superior",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.BEASTS, 60, 3)
            );

            HANGED_OIL = registerItem("oil_hanged",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), EntityGroup.ILLAGER, 20, 1)
            );
            ENHANCED_HANGED_OIL = registerItem("oil_hanged_enhanced",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), EntityGroup.ILLAGER, 40, 2)
            );
            SUPERIOR_HANGED_OIL = registerItem("oil_hanged_superior",
                    new WitcherMonsterOil_Base(new FabricItemSettings().maxCount(1), EntityGroup.ILLAGER, 60, 3)
            );
        }




    }

    public static Item KNIGHT_CROSSBOW;
    public static Item BASE_BOLT;
    public static Item BLUNT_BOLT;
    public static Item PRECISION_BOLT;
    public static Item EXPLODING_BOLT;
    public static Item BROADHEAD_BOLT;
    public static Item GVALCHIR;

    public static void registerWeapons_Armors(){

        //Swords
        {
            GVALCHIR = registerItem("gvalchir",
                    new GvalchirSword(TCOTS_Materials.GVALCHIR, 3, -2.2f, new FabricItemSettings().rarity(Rarity.UNCOMMON)));
        }

        //Crossbows
        {
            KNIGHT_CROSSBOW = registerItem("knight_crossbow",
                    new KnightCrossbow(new FabricItemSettings().maxCount(1).maxDamage(600)));

            BASE_BOLT = registerItem("base_bolt", new BoltItem(new FabricItemSettings(), "base_bolt"));

            BLUNT_BOLT = registerItem("blunt_bolt", new BoltItem(new FabricItemSettings().maxCount(32), "blunt_bolt"));

            PRECISION_BOLT = registerItem("precision_bolt", new BoltItem(new FabricItemSettings().maxCount(32), "precision_bolt"));

            EXPLODING_BOLT = registerItem("exploding_bolt", new BoltItem(new FabricItemSettings().maxCount(32), "exploding_bolt"));

            BROADHEAD_BOLT = registerItem("broadhead_bolt", new BoltItem(new FabricItemSettings().maxCount(32), "broadhead_bolt"));
        }


    }

    //xTODO: Add buy mechanic to alcohol

    //Alcohol
    public static WitcherAlcohol_Base ICY_SPIRIT;
    public static WitcherAlcohol_Base DWARVEN_SPIRIT;
    public static WitcherAlcohol_Base ALCOHEST;
    public static WitcherAlcohol_Base WHITE_GULL;
    public static WitcherAlcohol_Base VILLAGE_HERBAL;
    public static WitcherAlcohol_Base CHERRY_CORDIAL;
    public static WitcherAlcohol_Base MANDRAKE_CORDIAL;


    //Substances
    public static Item AETHER;
    public static Item VITRIOL;
    public static Item VERMILION;
    public static Item HYDRAGENUM;
    public static Item QUEBRITH;
    public static Item RUBEDO;
    public static Item REBIS;
    public static Item NIGREDO;
    public static Item STAMMELFORDS_DUST;
    public static Item ALCHEMISTS_POWDER;
    public static Item ALCHEMY_PASTE;
    public static Item MONSTER_FAT;

    //xTODO: Make the herbs spawn in the world
    //Herbs
    public static Item ALLSPICE;
    public static Item ARENARIA;
    public static Item CELANDINE;
    public static Item BRYONIA;
    public static Item CROWS_EYE;
    public static Item VERBENA;
    public static Item HAN_FIBER;
    public static Item PUFFBALL;
    public static Item PUFFBALL_MUSHROOM_BLOCK_ITEM;
    public static Item SEWANT_MUSHROOMS;
    public static Item SEWANT_MUSHROOM_BLOCK_ITEM;
    public static Item SEWANT_MUSHROOM_STEM_ITEM;
    public static Item ERGOT_SEEDS;
    public static Item LILY_OF_THE_VALLEY_PETALS;
    public static Item ALLIUM_PETALS;
    public static Item POPPY_PETALS;
    public static Item PEONY_PETALS;
    public static Item AZURE_BLUET_PETALS;
    public static Item OXEYE_DAISY_PETALS;
    public static Item BUNCH_OF_LEAVES;

    public static void registerAlchemyIngredients(){

        //Alcohol
        {
            ICY_SPIRIT = (WitcherAlcohol_Base) registerItem("icy_spirit",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(64),
                            Arrays.asList(
                                    new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0),
                                    new StatusEffectInstance(StatusEffects.SATURATION, 10, 1)),
                            1));

            DWARVEN_SPIRIT = (WitcherAlcohol_Base) registerItem("dwarven_spirit",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(16),
                            List.of(new StatusEffectInstance(StatusEffects.NAUSEA, 1, 200)),
                            2));

            ALCOHEST = (WitcherAlcohol_Base) registerItem("alcohest",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(16),
                            List.of(new StatusEffectInstance(StatusEffects.NAUSEA, 600, 2)),
                            4));

            WHITE_GULL = (WitcherAlcohol_Base) registerItem("white_gull",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(8),
                            Arrays.asList(
                                    new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 3),
                                    new StatusEffectInstance(StatusEffects.POISON, 40, 1)),
                            8));


            VILLAGE_HERBAL = (WitcherAlcohol_Base) registerItem("village_herbal",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(8),
                            Arrays.asList(
                                    new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1),
                                    new StatusEffectInstance(StatusEffects.REGENERATION, 200, 0)),
                            4));

            CHERRY_CORDIAL = (WitcherAlcohol_Base) registerItem("cherry_cordial",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(16),
                            Arrays.asList(
                                    new StatusEffectInstance(StatusEffects.NAUSEA, 200, 1),
                                    new StatusEffectInstance(StatusEffects.ABSORPTION, 200, 0)),
                            2));

            MANDRAKE_CORDIAL = (WitcherAlcohol_Base) registerItem("mandrake_cordial",
                    new WitcherAlcohol_Base(new FabricItemSettings().maxCount(8),
                            Arrays.asList(
                                    new StatusEffectInstance(StatusEffects.NAUSEA, 400, 2),
                                    new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0)),
                            6));
        }


        //Substances
        {
            AETHER = registerItem("aether",
                    new Item(new FabricItemSettings()));

            VITRIOL = registerItem("vitriol",
                    new Item(new FabricItemSettings()));

            VERMILION = registerItem("vermilion",
                    new Item(new FabricItemSettings()));

            HYDRAGENUM = registerItem("hydragenum",
                    new Item(new FabricItemSettings()));

            QUEBRITH = registerItem("quebrith",
                    new Item(new FabricItemSettings()));

            RUBEDO = registerItem("rubedo",
                    new Item(new FabricItemSettings()));

            REBIS = registerItem("rebis",
                    new Item(new FabricItemSettings()));

            NIGREDO = registerItem("nigredo",
                    new Item(new FabricItemSettings()));

            ALCHEMY_PASTE = registerItem("alchemy_paste",
                    new Item(new FabricItemSettings()));

            MONSTER_FAT = registerItem("monster_fat",
                    new Item(new FabricItemSettings()));

            STAMMELFORDS_DUST = registerItem("stammelfords_dust",
                    new Item(new FabricItemSettings()));

            ALCHEMISTS_POWDER = registerItem("alchemists_powder",
                    new Item(new FabricItemSettings()));
        }

        //Plants
        {
            ALLSPICE = registerItem("allspice",
                    new Item(new FabricItemSettings()));

            ARENARIA = registerItem("arenaria",
                    new AliasedBlockItem(TCOTS_Blocks.ARENARIA_BUSH,
                            new FabricItemSettings()));

            CELANDINE = registerItem("celandine",
                    new AliasedBlockItem(TCOTS_Blocks.CELANDINE_PLANT,
                            new FabricItemSettings()));

            CROWS_EYE = registerItem("crows_eye",
                    new AliasedBlockItem(TCOTS_Blocks.CROWS_EYE_FERN,
                            new FabricItemSettings().food(new FoodComponent.Builder().hunger(4).saturationModifier(1.2f).statusEffect(new StatusEffectInstance(StatusEffects.POISON, 100, 0), 0.8f).build())));

            BRYONIA = registerItem("bryonia",
                    new AliasedBlockItem(TCOTS_Blocks.BRYONIA_VINE,
                            new FabricItemSettings()));

            VERBENA = registerItem("verbena",
                    new AliasedBlockItem(TCOTS_Blocks.VERBENA_FLOWER,
                            new FabricItemSettings()));

            HAN_FIBER = registerItem("han_fiber",
                    new AliasedBlockItem(TCOTS_Blocks.HAN_FIBER_PLANT,
                            new FabricItemSettings()));

            PUFFBALL = registerItem("puffball",
                    new AliasedBlockItem(TCOTS_Blocks.PUFFBALL_MUSHROOM,
                            new FabricItemSettings()));

            SEWANT_MUSHROOMS = registerItem("sewant_mushrooms",
                    new AliasedBlockItem(TCOTS_Blocks.SEWANT_MUSHROOMS_PLANT,
                            new FabricItemSettings()));


            ERGOT_SEEDS = registerItem("ergot_seeds",
                    new Item(new FabricItemSettings()));

            LILY_OF_THE_VALLEY_PETALS = registerItem("lily_of_the_valley_petals",
                    new Item(new FabricItemSettings()));

            ALLIUM_PETALS = registerItem("allium_petals",
                    new Item(new FabricItemSettings()));

            POPPY_PETALS = registerItem("poppy_petals",
                    new Item(new FabricItemSettings()));

            PEONY_PETALS = registerItem("peony_petals",
                    new Item(new FabricItemSettings()));

            AZURE_BLUET_PETALS = registerItem("azure_bluet_petals",
                    new Item(new FabricItemSettings()));

            OXEYE_DAISY_PETALS = registerItem("oxeye_daisy_petals",
                    new Item(new FabricItemSettings()));

            BUNCH_OF_LEAVES = registerItem("bunch_of_leaves",
                    new Item(new FabricItemSettings()));
        }
    }



    public static void modifyLootTables(){
        LootTableEvents.MODIFY.register( (resourceManager, lootManager, id, tableBuilder, source) ->{
            if(Blocks.WHEAT.getLootTableId().equals(id) && source.isBuiltin()){
                LootPool.Builder ergotSeeds = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(TCOTS_Items.ERGOT_SEEDS))
                        .conditionally(RandomChanceLootCondition.builder(0.05f))
                        .conditionally(BlockStatePropertyLootCondition.builder(Blocks.WHEAT)
                                        .properties(StatePredicate.Builder.create()
                                                .exactMatch(CropBlock.AGE,7)))
                        .apply(ExplosionDecayLootFunction.builder());

                tableBuilder.pool(ergotSeeds.build());
            }

            if(EntityType.RAVAGER.getLootTableId().equals(id) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(TCOTS_Items.MONSTER_FAT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3f,8f))))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.8f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

            if((EntityType.HOGLIN.getLootTableId().equals(id) || EntityType.ZOGLIN.getLootTableId().equals(id)) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(TCOTS_Items.MONSTER_FAT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2f,4f))))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.4f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

            if(EntityType.POLAR_BEAR.getLootTableId().equals(id) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(TCOTS_Items.MONSTER_FAT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2f,3f))))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.3f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

            if(EntityType.PIGLIN_BRUTE.getLootTableId().equals(id) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(TCOTS_Items.MONSTER_FAT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1f,2f))))
                        .conditionally(KilledByPlayerLootCondition.builder())
                        .conditionally(RandomChanceWithLootingLootCondition.builder(0.25f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

        });
    }

    public static Item NEST_SLAB_ITEM;
    public static Item NEST_SKULL_ITEM;
    public static Item MONSTER_NEST_ITEM;
    public static Item ALCHEMY_TABLE_ITEM;

    public static Item HERBAL_TABLE_ITEM;

    public static Item ORGANIC_PASTE;
    public static Item WITCHER_BESTIARY;
    public static Item ALCHEMY_BOOK;

    public static void registerItemsMisc(){

        PUFFBALL_MUSHROOM_BLOCK_ITEM  = registerBlockItem("puffball_mushroom_block", TCOTS_Blocks.PUFFBALL_MUSHROOM_BLOCK);

        SEWANT_MUSHROOM_BLOCK_ITEM  = registerBlockItem("sewant_mushroom_block", TCOTS_Blocks.SEWANT_MUSHROOM_BLOCK);

        SEWANT_MUSHROOM_STEM_ITEM  = registerBlockItem("sewant_mushroom_stem", TCOTS_Blocks.SEWANT_MUSHROOM_STEM);

        NEST_SLAB_ITEM  = registerBlockItem("nest_slab", TCOTS_Blocks.NEST_SLAB);

        NEST_SKULL_ITEM = registerItem("nest_skull", new NestSkullItem(TCOTS_Blocks.NEST_SKULL, TCOTS_Blocks.NEST_WALL_SKULL, new FabricItemSettings(), Direction.DOWN));

        MONSTER_NEST_ITEM = registerItem("monster_nest", new MonsterNestItem(TCOTS_Blocks.MONSTER_NEST, new FabricItemSettings()));

        ALCHEMY_TABLE_ITEM = registerItem("alchemy_table", new AlchemyTableItem(TCOTS_Blocks.ALCHEMY_TABLE, new FabricItemSettings()));

        HERBAL_TABLE_ITEM = registerItem("herbal_table", new HerbalTableItem(TCOTS_Blocks.HERBAL_TABLE, new FabricItemSettings()));

        ORGANIC_PASTE = registerItem("organic_paste", new OrganicPasteItem(new FabricItemSettings().food(new FoodComponent.Builder().hunger(4).saturationModifier(0.3f).alwaysEdible().build())));


        WITCHER_BESTIARY = WitcherBestiaryItem.registerForBook(new Identifier(TCOTS_Main.MOD_ID, "witcher_bestiary"), new FabricItemSettings().maxCount(1));

        ALCHEMY_BOOK = AlchemyBookItem.registerForBook(new Identifier(TCOTS_Main.MOD_ID, "alchemy_book"), new FabricItemSettings().maxCount(1));
    }

    @SuppressWarnings("unused")
    public static void registerCompostableItems(){
        float f = 0.3f;
        float g = 0.5f;
        float h = 0.65f;
        float i = 0.85f;
        float j = 1.0f;
        ComposterBlock.registerCompostableItem(0.5f, TCOTS_Items.ALLIUM_PETALS);
        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.ARENARIA);
        ComposterBlock.registerCompostableItem(0.3f, TCOTS_Items.ALLSPICE);
        ComposterBlock.registerCompostableItem(0.5f, TCOTS_Items.AZURE_BLUET_PETALS);

        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.BRYONIA);
        ComposterBlock.registerCompostableItem(0.15f, TCOTS_Items.BUNCH_OF_LEAVES);

        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.CELANDINE);
        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.CROWS_EYE);
        ComposterBlock.registerCompostableItem(0.85f, TCOTS_Items.CADAVERINE);

        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.HAN_FIBER);

        ComposterBlock.registerCompostableItem(0.5f, TCOTS_Items.LILY_OF_THE_VALLEY_PETALS);

        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.PUFFBALL);
        ComposterBlock.registerCompostableItem(0.5f, TCOTS_Items.PEONY_PETALS);
        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.POPPY_PETALS);
        ComposterBlock.registerCompostableItem(0.85f, TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM);


        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.SEWANT_MUSHROOMS);
        ComposterBlock.registerCompostableItem(0.85f, TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM);
        ComposterBlock.registerCompostableItem(0.85f, TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM);

        ComposterBlock.registerCompostableItem(0.65f, TCOTS_Items.VERBENA);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), item);
    }

    private static Item registerItemPotion(String name, Item.Settings settings, StatusEffect effect, int toxicity, int durationInSecs, int amplifier, boolean decoction) {
        try {
            Identifier identifier = new Identifier(TCOTS_Main.MOD_ID, name);

            WitcherPotions_Base witcherPotion = new WitcherPotions_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05), amplifier), toxicity, decoction);

            return Registry.register(Registries.ITEM, identifier, witcherPotion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error registering potion");
        }
    }

    private static Item registerSplashPotion(String name, Item.Settings settings, StatusEffect effect, int toxicity, int durationInSecs){
        try {
            Identifier identifier = new Identifier(TCOTS_Main.MOD_ID, name);

            WitcherPotions_Base witcherPotion = new WitcherPotionsSplash_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05), 0), toxicity);
            return Registry.register(Registries.ITEM, identifier, witcherPotion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error registering Splash potion");
        }
    }

    public static DispenserBehavior getSplashBehavior(){
        return new DispenserBehavior(){
            @Override
            public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
                return new ProjectileDispenserBehavior(){

                    @Override
                    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                        return Util.make(new PotionEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
                    }

                    @Override
                    protected float getVariation() {
                        return super.getVariation() * 0.5f;
                    }

                    @Override
                    protected float getForce() {
                        return super.getForce() * 1.25f;
                    }
                }.dispense(blockPointer, itemStack);
            }
        };
    }


}
