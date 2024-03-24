package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.blocks.AlchemyTableItem;
import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.blocks.NestSkullItem;
import TCOTS.potions.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

public class TCOTS_Items {

    //xTODO: Add new way to craft the potions
        //TODO: Add new alchemy ingredients (mushrooms, flowers?
        //and alcohol)
        //TODO: Add more Potions
            //Witcher 3 Potions
            //xTODO: Swallow: Add when added Drowners
            //xTODO: White Raffard's Decoction: Add when added Nekkers
            //xTODO: Killer Whale: Add when added Drowners
            //TODO: Cat: Can be added, crafted with Water essence
            //TODO: Black Blood: Add when added Ghouls
            //TODO: Maribor Forest: Add when added Alghouls
            //TODO: White Honey: Add when added toxicity mechanic

            //TODO: Petri's Philter: Add when added specters
            //TODO: Full Moon: Add when added Nightwraiths
            //TODO: Golden Oriole: Add when added Noonwraiths

            //TODO: Tawny Owl: Add when added Arachas
            //TODO: Thunderbolt: Add when added Endregas
            //TODO: Blizzard: Add when added Golems

            //Witcher 2 Potions
            //TODO: Rook: Increases damage with swords

            //Witcher 1 Potions
            //TODO: Willow: Makes you immune to knockback
            //TODO: Wolverine: Makes you stronger when you have less health?


    //TODO: Add use to the items
        //xTODO: Drowner Tongue usable for Killer Whale potion
            //xTODO: Add the Killer Whale effect, improves respiration and attack underwater
        //xTODO: Drowner Brain usable for a new potion, Swallow:
            //xTODO: Add the Swallow effect, works like regeneration, it's a lot slower but the potion it's more durable
        //TODO: Nekker eye usable for Hanged Man Oil
        //xTODO: Nekker Hearth usable for the White Raffard's Decoction
            //xTODO: Add the White Raffard's Decoction, works similar to the instant health, but works with percentage

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


    public static Item FOGLET_SPAWN_EGG;
    public static Item FOGLET_TEETH;
    public static Item FOGLET_MUTAGEN;


    public static Item NEKKER_SPAWN_EGG;
    public static Item NEKKER_HEART;
    public static Item NEKKER_EYE;

    //Decoctions
    public static Item EMPTY_MONSTER_DECOCTION;
    public static Item GRAVE_HAG_DECOCTION;
    public static Item WATER_HAG_DECOCTION;
    public static Item FOGLET_DECOCTION;

    //Potions
    public static WitcherAlcohol_Base DWARVEN_SPIRIT;
    public static Item SWALLOW_POTION;
    public static Item SWALLOW_POTION_ENHANCED;
    public static Item SWALLOW_POTION_SUPERIOR;
    public static Item WHITE_RAFFARDS_DECOCTION;
    public static Item WHITE_RAFFARDS_DECOCTION_ENHANCED;
    public static Item WHITE_RAFFARDS_DECOCTION_SUPERIOR;
    public static Item CAT_POTION;
    public static Item CAT_POTION_ENHANCED;
    public static Item CAT_POTION_SUPERIOR;
    public static Item KILLER_WHALE_POTION;

    public static WitcherAlcohol_Base ALCOHEST;
    public static Item AETHER;
    public static Item EMPTY_WITCHER_POTION;


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

    //Splash Potions
    public static Item SWALLOW_SPLASH;
    public static Item KILLER_WHALE_SPLASH;
    public static Item WHITE_RAFFARDS_DECOCTION_SPLASH;

    //Register Witcher Potion Items
    public static void registerPotions() {
        //Ingredients
        DWARVEN_SPIRIT = (WitcherAlcohol_Base) registerItem("dwarven_spirit",
                new DwarvenSpiritItem(new FabricItemSettings().maxCount(16), new StatusEffectInstance(StatusEffects.NAUSEA,200), 0, 2));

        ALCOHEST = (WitcherAlcohol_Base) registerItem("alcohest",
                new AlcohestItem(new FabricItemSettings().maxCount(16), new StatusEffectInstance(StatusEffects.NAUSEA, 600, 3), 0,4));

        AETHER = registerItem("aether",
                new Item(new FabricItemSettings())
        );


        //Oils
        EMPTY_OIL = registerItem("empty_oil",
                new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(1))
        );

        NECROPHAGE_OIL=registerItem("oil_necrophage",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.NECROPHAGES, 20,1)
        );
        ENHANCED_NECROPHAGE_OIL=registerItem("oil_necrophage_enhanced",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.NECROPHAGES, 40,2)
        );
        SUPERIOR_NECROPHAGE_OIL=registerItem("oil_necrophage_superior",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.NECROPHAGES, 60,3)
        );

        OGROID_OIL=registerItem("oil_ogroid",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.OGROIDS, 20,1)
        );
        ENHANCED_OGROID_OIL=registerItem("oil_ogroid_enhanced",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.OGROIDS, 40,2)
        );
        SUPERIOR_OGROID_OIL=registerItem("oil_ogroid_superior",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.OGROIDS, 60,3)
        );

        BEAST_OIL=registerItem("oil_beast",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.BEASTS, 20,1)
        );
        ENHANCED_BEAST_OIL=registerItem("oil_beast_enhanced",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.BEASTS, 40,2)
        );
        SUPERIOR_BEAST_OIL=registerItem("oil_beast_superior",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), TCOTS_Entities.BEASTS, 60,3)
        );

        HANGED_OIL=registerItem("oil_hanged",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), EntityGroup.ILLAGER, 20,1)
        );
        ENHANCED_HANGED_OIL=registerItem("oil_hanged_enhanced",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), EntityGroup.ILLAGER, 40,2)
        );
        SUPERIOR_HANGED_OIL=registerItem("oil_hanged_superior",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1), EntityGroup.ILLAGER, 60,3)
        );



        EMPTY_WITCHER_POTION = registerItem("empty_witcher_potion",
                new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(2))
        );

        //Potions
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


        KILLER_WHALE_POTION = registerItemPotion("killer_whale_potion",
                new FabricItemSettings().maxCount(3),
                TCOTS_Effects.KILLER_WHALE_EFFECT,
                15,
                90,
                0,
                false
        );



        //Splash Potions
        SWALLOW_SPLASH = registerSplashPotion("swallow_splash",
                new FabricItemSettings().maxCount(5),
                TCOTS_Effects.SWALLOW_EFFECT,
                10,
                18,
                0
        );

        WHITE_RAFFARDS_DECOCTION_SPLASH = registerSplashPotion("white_raffards_splash",
                new FabricItemSettings().maxCount(5),
                TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                15,
                1,
                0
        );

        KILLER_WHALE_SPLASH = registerSplashPotion("killer_whale_splash",
                new FabricItemSettings().maxCount(5),
                TCOTS_Effects.KILLER_WHALE_EFFECT,
                10,
                75,
                0
        );


        //Decoctions
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

        FOGLET_DECOCTION = registerItemPotion("foglet_decoction",
                new FabricItemSettings().maxCount(1),
                TCOTS_Effects.FOGLET_DECOCTION_EFFECT,
                50,
                1200,
                0,
                true
        );
    }

    //Register Drops from monsters
    public static void registerDrops() {
        DROWNER_SPAWN_EGG = registerItem("drowner_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.DROWNER, 0x8db1c0, 0x9fa3ae,
                        new FabricItemSettings()));
        DROWNER_TONGUE = registerItem("drowner_tongue",
                new Item(
                        new FabricItemSettings().maxCount(64)));
        DROWNER_BRAIN = registerItem("drowner_brain",
                new Item(
                        new FabricItemSettings().maxCount(16)));



        ROTFIEND_SPAWN_EGG = registerItem("rotfiend_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ROTFIEND, 0xb3867b, 0xe6e1bc,
                        new FabricItemSettings()));
        ROTFIEND_BLOOD = registerItem("rotfiend_blood",
                new Item(
                        new FabricItemSettings().maxCount(64)));


        GRAVE_HAG_SPAWN_EGG = registerItem("grave_hag_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GRAVE_HAG, 0xb6b692, 0x8e8480,
                        new FabricItemSettings()));
        GRAVE_HAG_MUTAGEN = registerItem("grave_hag_mutagen",
                new Item(
                        new FabricItemSettings().maxCount(8)));


        WATER_HAG_SPAWN_EGG = registerItem("water_hag_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.WATER_HAG, 0x8d93c0, 0x780b17,
                        new FabricItemSettings()));
        WATER_HAG_MUD_BALL = registerItem("water_hag_mud_ball",
                new WaterHag_MudBallItem(
                        new FabricItemSettings().maxCount(16)));
        WATER_HAG_MUTAGEN = registerItem("water_hag_mutagen",
                new Item(
                        new FabricItemSettings().maxCount(8)));

        FOGLET_SPAWN_EGG = registerItem("foglet_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.FOGLET, 0x4a3f3f, 0x211c1c,
                        new FabricItemSettings()));
        FOGLET_TEETH = registerItem("foglet_teeth",
                new Item(
                        new FabricItemSettings()));
        FOGLET_MUTAGEN = registerItem("foglet_mutagen",
                new Item(
                        new FabricItemSettings().maxCount(8)));

        NEKKER_SPAWN_EGG = registerItem("nekker_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.NEKKER, 0xa59292, 0x705c5c,
                        new FabricItemSettings()));

        NEKKER_EYE = registerItem("nekker_eye",
                new Item(
                        new FabricItemSettings().maxCount(64)));

        NEKKER_HEART = registerItem("nekker_heart",
                new Item(
                        new FabricItemSettings().maxCount(16)));
    }

    public static Item NEST_SLAB_ITEM;
    public static Item NEST_SKULL_ITEM;
    public static Item MONSTER_NEST_ITEM;
    public static Item ALCHEMY_TABLE_ITEM;
    public static Item WITCHER_BESTIARY;
    public static Item ALCHEMY_BOOK;

    public static void registerItemsMisc(){
        NEST_SLAB_ITEM  = registerBlockItem("nest_slab", TCOTS_Blocks.NEST_SLAB);

        NEST_SKULL_ITEM = registerItem("nest_skull", new NestSkullItem(TCOTS_Blocks.NEST_SKULL, TCOTS_Blocks.NEST_WALL_SKULL, new FabricItemSettings(), Direction.DOWN));

        MONSTER_NEST_ITEM = registerItem("monster_nest", new MonsterNestItem(TCOTS_Blocks.MONSTER_NEST, new FabricItemSettings()));

        ALCHEMY_TABLE_ITEM = registerItem("alchemy_table", new AlchemyTableItem(TCOTS_Blocks.ALCHEMY_TABLE, new FabricItemSettings()));

        WITCHER_BESTIARY = WitcherBestiaryItem.registerForBook(new Identifier(TCOTS_Main.MOD_ID, "witcher_bestiary"), new FabricItemSettings().maxCount(1));

        ALCHEMY_BOOK = AlchemyBookItem.registerForBook(new Identifier(TCOTS_Main.MOD_ID, "alchemy_book"), new FabricItemSettings().maxCount(1));
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
//            return null;
        }
    }

    private static Item registerSplashPotion(String name, Item.Settings settings, StatusEffect effect, int toxicity, int durationInSecs, int amplifier){
        try {
            Identifier identifier = new Identifier(TCOTS_Main.MOD_ID, name);

            WitcherPotions_Base witcherPotion = new WitcherPotionsSplash_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05),amplifier), toxicity);
            return Registry.register(Registries.ITEM, identifier, witcherPotion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error registering Splash potion");
        }
    }
}
