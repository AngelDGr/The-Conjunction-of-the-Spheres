package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.blocks.AlchemyTableItem;
import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.blocks.NestSkullItem;
import TCOTS.potions.*;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import io.wispforest.lavender.client.LavenderBookScreen;
import io.wispforest.lavender.md.compiler.BookCompiler;
import io.wispforest.lavender.md.features.RecipeFeature;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TCOTS_Items {

    //xTODO: Add new way to craft the potions
        //TODO: Add new alchemy ingredients (mushrooms, flowers?
            //Plants
            //xTODO: Allspice
            //xTODO: Arenaria
            //xTODO: Balisse fruit - Sweet Berries
            //TODO: Beggartick blossoms - Rose Petals
            //xTODO: Berbercane fruit - Glow Berries
            //TODO: Bison grass - Bought
            //?xTODO: Bloodmoss - Moss Carpet
            //xTODO: Blowball - Dandelion
            //TODO: Bryonia
            //xTODO: Buckthorn - Kelp
            //xTODO: Celandine
            //xTODO: Cortinarius - Brown Mushroom
            //xTODO: Crow's eye
            //TODO: Ergot seeds - Poisonous potato like
            //TODO: Fool's parsley leaves - Azure Bluet Petals
            //TODO: Ginatia petals - Peony Petals
            //xTODO: Green mold - Moss Block
            //TODO: Han fiber
            //xTODO: Hellebore petals - Allium Petals
            //TODO: Honeysuckle
            //TODO: Hop umbels
            //xTODO: Hornwort - Lily pad
            //xTODO: Longrube - Red mushroom
            //TODO: Mandrake/ root
            //TODO: Mistletoe - Oxeye Daisy Petals
            //xTODO: Moleyarrow - Sunflower
            //xTODO: Nostrix - Glow Lichen
            //TODO: Puffball
            //xTODO: Pringrape - Flowering Azalea
            //xTODO: Ranogrin - Fern
            //xTODO: Ribleaf - Leaves?
            //TODO: Sewant mushrooms
            //TODO: Verbena
            //xTODO: White myrtle petals - Lily of the Valley Petals
            //TODO: Wolfsbane

        //and alcohol)
        //TODO: Add more Potions
            //Witcher 3 Potions
            //xTODO: Swallow: Add when added Drowners
            //xTODO: White Raffard's Decoction: Add when added Nekkers
            //xTODO: Killer Whale: Add when added Drowners
            //xTODO: Cat: Can be added, crafted with Water essence
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



    //Register Witcher Potion Items
    public static void registerPotions() {

        //Oils
        EMPTY_OIL = registerItem("empty_oil",
                new EmptyWitcherPotionItem(new FabricItemSettings().maxCount(1))
        );

        NECROPHAGE_OIL=registerItem("oil_necrophage",
                new MonsterOil_Base(new FabricItemSettings().maxCount(1).rarity(Rarity.UNCOMMON), TCOTS_Entities.NECROPHAGES, 20,1)
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

    //TODO: Add buy mechanic to alcohol
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

    //TODO: Make the herbs spawn in the world
    //Herbs
    public static Item ALLSPICE;
    public static Item ARENARIA;
    public static Item CELANDINE;
    public static Item LILY_OF_THE_VALLEY_PETALS;
    public static Item CROWS_EYE;
    public static Item ALLIUM_PETALS;

    public static void registerAlchemyIngredients(){

        //Ingredients
        ICY_SPIRIT = (WitcherAlcohol_Base) registerItem("icy_spirit",
                new WitcherAlcohol_Base(new FabricItemSettings().maxCount(64),
                        Arrays.asList(
                        new StatusEffectInstance(StatusEffects.NAUSEA,200, 0),
                        new StatusEffectInstance(StatusEffects.SATURATION,10, 1)),
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
                                new StatusEffectInstance(StatusEffects.REGENERATION,200,0)),
                        4));

        CHERRY_CORDIAL = (WitcherAlcohol_Base) registerItem("cherry_cordial",
                new WitcherAlcohol_Base(new FabricItemSettings().maxCount(16),
                        Arrays.asList(
                                new StatusEffectInstance(StatusEffects.NAUSEA, 200,1),
                                new StatusEffectInstance(StatusEffects.ABSORPTION, 200,0)),
                        2));

        MANDRAKE_CORDIAL = (WitcherAlcohol_Base) registerItem("mandrake_cordial",
                new WitcherAlcohol_Base(new FabricItemSettings().maxCount(8),
                        Arrays.asList(
                                new StatusEffectInstance(StatusEffects.NAUSEA, 400, 2),
                                new StatusEffectInstance(StatusEffects.RESISTANCE, 400, 0)),
                        6));


        AETHER = registerItem("aether",
                new Item(new FabricItemSettings()));

        VITRIOL = registerItem("vitriol",
                new Item(new FabricItemSettings()));


        //Plants
        ALLSPICE = registerItem("allspice",
                new Item(
                        new FabricItemSettings().maxCount(64)));

        ARENARIA = registerItem("arenaria",
                new AliasedBlockItem(TCOTS_Blocks.ARENARIA_BUSH,
                        new FabricItemSettings().maxCount(64)));

        CELANDINE = registerItem("celandine",
                new AliasedBlockItem(TCOTS_Blocks.CELANDINE_PLANT,
                        new FabricItemSettings().maxCount(64)));

        CROWS_EYE = registerItem("crows_eye",
                new AliasedBlockItem(TCOTS_Blocks.CROWS_EYE_FERN,
                        new FabricItemSettings().maxCount(64)));

        LILY_OF_THE_VALLEY_PETALS = registerItem("lily_of_the_valley_petals",
                new Item(
                        new FabricItemSettings().maxCount(64)));

        ALLIUM_PETALS = registerItem("allium_petals",
                new Item(
                        new FabricItemSettings().maxCount(64)));
    }

    public static void modifyLootTables(){
//        LootTableEvents.MODIFY.register( (resourceManager, lootManager, id, tableBuilder, source) ->{
//            if(Blocks.LILY_OF_THE_VALLEY.getLootTableId().equals(id) && source.isBuiltin()){
//                LootPool.Builder Poolbuilder = LootPool.builder()
//                        .rolls(ConstantLootNumberProvider.create(1))
//                        .conditionally(RandomChanceLootCondition.builder(1))
//                        .with(AlternativeEntry.builder(ItemEntry.builder(Items.LILY_OF_THE_VALLEY).conditionally(SurvivesExplosionLootCondition.builder())
//                                        .alternatively(
//                                                ItemEntry.builder(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS))
//                                                .conditionally(AnyOfLootCondition.builder(
//                                                        MatchToolLootCondition.builder(
//                                                                ItemPredicate.Builder.create().items(Items.SHEARS))))
//
//                                        )
//                        );
//
//                tableBuilder.modifyPools( builder -> {
//                    builder
//                            .rolls(ConstantLootNumberProvider.create(1))
//                            .with(
//                                    AlternativeEntry.builder(ItemEntry.builder(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS)))
//                            .conditionally(AnyOfLootCondition.builder(
//                                    MatchToolLootCondition.builder(
//                                            ItemPredicate.Builder.create().items(Items.SHEARS))))
//                            .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,4)))
//                    ;}
//                );
//            }
//        });
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

        //Register Recipe Previews
        LavenderBookScreen.registerRecipePreviewBuilder(new Identifier(TCOTS_Main.MOD_ID, "alchemy_book"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));

        LavenderBookScreen.registerRecipePreviewBuilder(new Identifier(TCOTS_Main.MOD_ID, "witcher_bestiary"), AlchemyTableRecipe.Type.INSTANCE, (alchemyTable_RecipePreviewBuilder));
    }

    private static final RecipeFeature.RecipePreviewBuilder<AlchemyTableRecipe> alchemyTable_RecipePreviewBuilder = new RecipeFeature.RecipePreviewBuilder<>() {
        @Override
        public @NotNull Component buildRecipePreview(BookCompiler.ComponentSource componentSource, RecipeEntry<AlchemyTableRecipe> recipeEntry) {
            Identifier TEXTURE_ID = new Identifier(TCOTS_Main.MOD_ID, "textures/gui/alchemy_book_gui.png");
            //Get the recipe
            var recipe = recipeEntry.value();

            //Makes how it's going to flow the content in root
            //Size horizontal       and      vertical
            var root = Containers.horizontalFlow(Sizing.content(), Sizing.fixed(41));
            root.verticalAlignment(VerticalAlignment.CENTER).horizontalAlignment(HorizontalAlignment.CENTER);

            //Makes how it's going to flow the content in resultContainer
            //Horizontal
            var resultContainer = Containers.horizontalFlow(Sizing.fixed(111), Sizing.fixed(41));
            resultContainer.horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER);

            root.child(resultContainer);

            //Add child for result item
            resultContainer.child(
                    //Makes a container of Item Stack
                    Containers.stack(Sizing.fixed(22), Sizing.fixed(22))
                            //Add as child the result
                            .child(Components.item(recipe.getResult(null)).showOverlay(true).setTooltipFromStack(true))
                            //Add as child the texture
                            .child(Components.texture(TEXTURE_ID,
                                    435, 144,
                                    22, 22,
                                    512, 256).blend(true))
                            //Put in the result specific place
                            .positioning(Positioning.absolute(36, 0))
                            //Align it
                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER));

            //Adds the texture
            resultContainer.child(
                    Containers.stack(Sizing.content(), Sizing.fixed(18))
                            .child(Components.texture(TEXTURE_ID,
                                    399, 167,
                                    111, 18,
                                    512, 256).blend(true))
                            .positioning(Positioning.absolute(0, 23))
                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER));


            //Loop to assign each ingredient to a container
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                //Get the ingredient stack
                ItemStack stack = recipe.getIngredients().get(i).getMatchingStacks()[0];
                //Get the quantity
                int count = recipe.getIngredientsCounts().get(i);
                //Creates the stack with the correct quantity
                ItemStack ingredientStack = new ItemStack(stack.getItem(), count);
                switch (i) {
                    case 0:
                        resultContainer.child(
                                Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                        .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                        .positioning(Positioning.absolute(38, 23))
                                        .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                        );
                        break;

                    case 1:
                        resultContainer.child(
                                Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                        .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                        .positioning(Positioning.absolute(19, 23))
                                        .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                        );
                        break;

                    case 2:
                        resultContainer.child(
                                Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                        .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                        .positioning(Positioning.absolute(57, 23))
                                        .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                        );
                        break;

                    case 3:
                        resultContainer.child(
                                Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                        .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                        .positioning(Positioning.absolute(0, 23))
                                        .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                        );
                        break;

                    case 4:
                        resultContainer.child(
                                Containers.stack(Sizing.fixed(18), Sizing.fixed(18))
                                        .child(Components.item(ingredientStack).showOverlay(true).setTooltipFromStack(true))
                                        .positioning(Positioning.absolute(76, 23))
                                        .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
                        );
                        break;
                    default:
                        break;
                }
            }

            //Base container
            resultContainer.child(
                    Containers.stack(Sizing.content(), Sizing.fixed(16))
                            .child(Components.item(recipe.getBaseItem()).showOverlay(true).setTooltipFromStack(true))
                            .positioning(Positioning.absolute(95, 24))
                            .horizontalAlignment(HorizontalAlignment.CENTER).verticalAlignment(VerticalAlignment.CENTER)
            );

            return root;
        }
    };

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
