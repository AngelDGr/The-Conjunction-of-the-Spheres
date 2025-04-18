package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.TCOTS_EntityAttributes;
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
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class TCOTS_Items_Fabric {
    //Item Components
    public static final DataComponentType<MonsterOilComponent> MONSTER_OIL_COMPONENT = TCOTS_Items_Fabric.register("monster_oil", builder -> builder.persistent(MonsterOilComponent.CODEC));
    public static final DataComponentType<Boolean> ANCHOR_RETRIEVE = TCOTS_Items_Fabric.register("anchor_retrieve", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
    public static final DataComponentType<RecipeTeacherComponent> RECIPE_TEACHER_COMPONENT = TCOTS_Items_Fabric.register("recipe_teacher", builder -> builder.persistent(RecipeTeacherComponent.CODEC));
    public static final DataComponentType<CustomEffectsComponent> CUSTOM_EFFECTS_COMPONENT = TCOTS_Items_Fabric.register("custom_effects", builder -> builder.persistent(CustomEffectsComponent.CODEC));
    public static final DataComponentType<String> REFILL_RECIPE = TCOTS_Items_Fabric.register("refill_recipe", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));

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

            //TODO: W3.- 2nd Update
            //  Petri's Philter: Add when added specters
            //  Full Moon: Add when added Nightwraiths
            //  Golden Oriole: Add when added Noonwraiths
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
    //xTODO: Add more uses to Cadaverine
    public static Item GRAVEIR_BONE;
    //xTODO: Add more uses to Graveir Bone

    public static Item BULLVORE_SPAWN_EGG;
    public static Item BULLVORE_HORN_FRAGMENT;

    public static Item NEKKER_SPAWN_EGG;
    public static Item NEKKER_HEART;
    public static Item NEKKER_EYE;

    public static Item NEKKER_WARRIOR_SPAWN_EGG;
    public static Item NEKKER_WARRIOR_MUTAGEN;

    public static Item CYCLOPS_SPAWN_EGG;

    public static Item ROCK_TROLL_SPAWN_EGG;
    public static Item CAVE_TROLL_LIVER;
    public static Item TROLL_MUTAGEN;

    public static Item ICE_TROLL_SPAWN_EGG;

    public static Item FOREST_TROLL_SPAWN_EGG;

    public static Item ICE_GIANT_SPAWN_EGG;


    //Register Drops from monsters
    public static void registerDrops() {
        DROWNER_SPAWN_EGG = registerItem("drowner_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.DROWNER, 0x8db1c0, 0x9fa3ae, new Item.Properties()));
        DROWNER_TONGUE = registerItem("drowner_tongue",
                new Item(new Item.Properties()));
        DROWNER_BRAIN = registerItem("drowner_brain",
                new Item(new Item.Properties().stacksTo(16)));


        ROTFIEND_SPAWN_EGG = registerItem("rotfiend_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ROTFIEND, 0xb3867b, 0xe6e1bc, new Item.Properties()));
        ROTFIEND_BLOOD = registerItem("rotfiend_blood",
                new Item(new Item.Properties()));


        GRAVE_HAG_SPAWN_EGG = registerItem("grave_hag_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GRAVE_HAG, 0xb6b692, 0x8e8480, new Item.Properties()));
        GRAVE_HAG_MUTAGEN = registerItem("grave_hag_mutagen",
                new Item(new Item.Properties().stacksTo(8)));


        WATER_HAG_SPAWN_EGG = registerItem("water_hag_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.WATER_HAG, 0x8d93c0, 0x780b17, new Item.Properties()));
        WATER_HAG_MUD_BALL = registerItem("water_hag_mud_ball",
                new WaterHag_MudBallItem(new Item.Properties().stacksTo(16)));
        WATER_HAG_MUTAGEN = registerItem("water_hag_mutagen",
                new Item(new Item.Properties().stacksTo(8)));
        WATER_ESSENCE = registerItem("water_essence",
                new Item(new Item.Properties()));


        FOGLET_SPAWN_EGG = registerItem("foglet_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.FOGLET, 0x4a3f3f, 0x211c1c, new Item.Properties()));
        FOGLET_TEETH = registerItem("foglet_teeth",
                new Item(new Item.Properties()));
        FOGLET_MUTAGEN = registerItem("foglet_mutagen",
                new Item(new Item.Properties().stacksTo(8)));


        GHOUL_SPAWN_EGG = registerItem("ghoul_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GHOUL, 0xd69d76, 0x0e0a07, new Item.Properties()));
        GHOUL_BLOOD = registerItem("ghoul_blood",
                new Item(new Item.Properties()));


        ALGHOUL_SPAWN_EGG = registerItem("alghoul_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ALGHOUL, 0x513e3d, 0x000000,
                        new Item.Properties()));
        ALGHOUL_BONE_MARROW = registerItem("alghoul_bone_marrow",
                new Item(new Item.Properties()));


        SCURVER_SPAWN_EGG = registerItem("scurver_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.SCURVER, 0xc0887a, 0x661f1f, new Item.Properties()));

        SCURVER_SPINE = registerItem("scurver_spine",
                new ScurverSpineItem(new Item.Properties().stacksTo(16)));


        DEVOURER_SPAWN_EGG = registerItem("devourer_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.DEVOURER, 0x606c68, 0x1f1f1f, new Item.Properties()));
        DEVOURER_TEETH = registerItem("devourer_teeth",
                new Item(new Item.Properties()));

        GRAVEIR_SPAWN_EGG = registerItem("graveir_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.GRAVEIR, 0xab706d, 0x882925, new Item.Properties()));
        CADAVERINE = registerItem("cadaverine",
                new Item(new Item.Properties()));
        GRAVEIR_BONE = registerItem("graveir_bone",
                new Item(new Item.Properties()));

        BULLVORE_SPAWN_EGG = registerItem("bullvore_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.BULLVORE, 0xdad29a, 0xb1816d, new Item.Properties()));
        BULLVORE_HORN_FRAGMENT = registerItem("bullvore_horn_fragment",
                new Item(new Item.Properties()));

        NEKKER_SPAWN_EGG = registerItem("nekker_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.NEKKER, 0xa59292, 0x705c5c,
                        new Item.Properties()));
        NEKKER_EYE = registerItem("nekker_eye",
                new Item(new Item.Properties()));
        NEKKER_HEART = registerItem("nekker_heart",
                new Item(new Item.Properties().stacksTo(16)));

        NEKKER_WARRIOR_SPAWN_EGG = registerItem("nekker_warrior_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.NEKKER_WARRIOR, 0x97a592, 0xb12022,
                        new Item.Properties()));
        NEKKER_WARRIOR_MUTAGEN = registerItem("nekker_warrior_mutagen",
                new Item(new Item.Properties().stacksTo(8)));

        CYCLOPS_SPAWN_EGG = registerItem("cyclops_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.CYCLOPS, 0xceb6b6, 0x3c4433,
                        new Item.Properties()));

        ROCK_TROLL_SPAWN_EGG = registerItem("rock_troll_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ROCK_TROLL, 0x90acb1, 0xeeb19a,
                        new Item.Properties()));
        CAVE_TROLL_LIVER = registerItem("cave_troll_liver",
                new Item(new Item.Properties()));
        TROLL_MUTAGEN = registerItem("troll_mutagen",
                new Item(new Item.Properties().stacksTo(8)));

        ICE_TROLL_SPAWN_EGG = registerItem("ice_troll_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ICE_TROLL, 0xaadde6, 0xffd8c9,
                        new Item.Properties()));

        FOREST_TROLL_SPAWN_EGG = registerItem("forest_troll_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.FOREST_TROLL, 0x265558, 0xcfcfb4,
                        new Item.Properties()));

        ICE_GIANT_SPAWN_EGG = registerItem("ice_giant_spawn_egg",
                new SpawnEggItem(TCOTS_Entities.ICE_GIANT, 0x93b7b7, 0x1d1919,
                        new Item.Properties()));
    }

    public static Item ALCHEMY_FORMULA;
    //Decoctions
    public static Item EMPTY_MONSTER_DECOCTION;
    public static Item GRAVE_HAG_DECOCTION;
    public static Item WATER_HAG_DECOCTION;
    public static Item FOGLET_DECOCTION;
    public static Item ALGHOUL_DECOCTION;
    public static Item NEKKER_WARRIOR_DECOCTION;
    public static Item TROLL_DECOCTION;

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


    public static Item EMPTY_WITCHER_POTION;
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
    public static Item EMPTY_BOMB_POWDER;
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
                new AlchemyFormulaItem(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON))
        );



        //Potions
        {
            {
                EMPTY_WITCHER_POTION = registerItem("empty_witcher_potion",
                        new EmptyWitcherPotionItem(new Item.Properties())
                );

                EMPTY_WITCHER_POTION_2 = registerItem("empty_witcher_potion2",
                        new EmptyWitcherPotionItem(new Item.Properties().stacksTo(2))
                );

                EMPTY_WITCHER_POTION_3 = registerItem("empty_witcher_potion3",
                        new EmptyWitcherPotionItem(new Item.Properties().stacksTo(3))
                );

                EMPTY_WITCHER_POTION_4 = registerItem("empty_witcher_potion4",
                        new EmptyWitcherPotionItem(new Item.Properties().stacksTo(4))
                );

                EMPTY_WITCHER_POTION_5 = registerItem("empty_witcher_potion5",
                        new EmptyWitcherPotionItem(new Item.Properties().stacksTo(5))
                );

                //Swallow
                {
                    SWALLOW_POTION = registerItemPotion("swallow_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            0,
                            false
                    );

                    SWALLOW_POTION_ENHANCED = registerItemPotion("swallow_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            1,
                            false
                    );

                    SWALLOW_POTION_SUPERIOR = registerItemPotion("swallow_potion_superior",
                            new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            20,
                            20,
                            2,
                            false
                    );

                    SWALLOW_SPLASH = registerSplashPotion("swallow_splash",
                            new Item.Properties().stacksTo(5),
                            TCOTS_Effects.SWALLOW_EFFECT,
                            10,
                            18
                    );
                }

                //Cat
                {
                    CAT_POTION = registerItemPotion("cat_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            60,
                            0,
                            false
                    );

                    CAT_POTION_ENHANCED = registerItemPotion("cat_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.CAT_EFFECT,
                            15,
                            120,
                            1,
                            false
                    );

                    CAT_POTION_SUPERIOR = registerItemPotion("cat_potion_superior",
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
                    WHITE_RAFFARDS_DECOCTION = registerItemPotion("white_raffards_decoction",
                            new Item.Properties().stacksTo(2),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            0,
                            false
                    );

                    WHITE_RAFFARDS_DECOCTION_ENHANCED = registerItemPotion("white_raffards_decoction_enhanced",
                            new Item.Properties().stacksTo(2).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            1,
                            false
                    );

                    WHITE_RAFFARDS_DECOCTION_SUPERIOR = registerItemPotion("white_raffards_decoction_superior",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            25,
                            1,
                            2,
                            false
                    );


                    WHITE_RAFFARDS_DECOCTION_SPLASH = registerSplashPotion("white_raffards_splash",
                            new Item.Properties().stacksTo(5),
                            TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                            15,
                            1
                    );
                }

                //Killer Whale
                {
                    KILLER_WHALE_POTION = registerItemPotion("killer_whale_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.KILLER_WHALE_EFFECT,
                            15,
                            90,
                            0,
                            false
                    );

                    KILLER_WHALE_SPLASH = registerSplashPotion("killer_whale_splash",
                            new Item.Properties().stacksTo(5),
                            TCOTS_Effects.KILLER_WHALE_EFFECT,
                            10,
                            75
                    );
                }

                //Black Blood
                {
                    BLACK_BLOOD_POTION = registerItemPotion("black_blood_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    BLACK_BLOOD_POTION_ENHANCED = registerItemPotion("black_blood_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.BLACK_BLOOD_EFFECT,
                            25,
                            45,
                            1,
                            false
                    );

                    BLACK_BLOOD_POTION_SUPERIOR = registerItemPotion("black_blood_potion_superior",
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
                    MARIBOR_FOREST_POTION = registerItemPotion("maribor_forest_potion",
                            new Item.Properties().stacksTo(3),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            30,
                            0,
                            false
                    );

                    MARIBOR_FOREST_POTION_ENHANCED = registerItemPotion("maribor_forest_potion_enhanced",
                            new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.MARIBOR_FOREST_EFFECT,
                            20,
                            60,
                            1,
                            false
                    );

                    MARIBOR_FOREST_POTION_SUPERIOR = registerItemPotion("maribor_forest_potion_superior",
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
                    WOLF_POTION = registerItemPotion("wolf_potion",
                            new Item.Properties().stacksTo(2),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            30,
                            0,
                            false
                    );

                    WOLF_POTION_ENHANCED = registerItemPotion("wolf_potion_enhanced",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.WOLF_EFFECT,
                            25,
                            45,
                            1,
                            false
                    );

                    WOLF_POTION_SUPERIOR = registerItemPotion("wolf_potion_superior",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
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
                            new Item.Properties().stacksTo(2),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            45,
                            0,
                            false
                    );

                    ROOK_POTION_ENHANCED = registerItemPotion("rook_potion_enhanced",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
                            TCOTS_Effects.ROOK_EFFECT,
                            25,
                            60,
                            1,
                            false
                    );

                    ROOK_POTION_SUPERIOR = registerItemPotion("rook_potion_superior",
                            new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON),
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
                            new WitcherWhiteHoney(new Item.Properties().stacksTo(1))
                    );

                    WHITE_HONEY_POTION_ENHANCED = registerItem("white_honey_potion_enhanced",
                            new WitcherWhiteHoney(new Item.Properties().stacksTo(2).rarity(Rarity.UNCOMMON))
                    );

                    WHITE_HONEY_POTION_SUPERIOR = registerItem("white_honey_potion_superior",
                            new WitcherWhiteHoney(new Item.Properties().stacksTo(5).rarity(Rarity.UNCOMMON))
                    );
                }
            }

            //Decoctions
            {
                EMPTY_MONSTER_DECOCTION = registerItem("empty_monster_decoction",
                        new EmptyWitcherPotionItem(new Item.Properties().stacksTo(1))
                );

                GRAVE_HAG_DECOCTION = registerItemPotion("grave_hag_decoction",
                        TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT
                );

                WATER_HAG_DECOCTION = registerItemPotion("water_hag_decoction",
                        TCOTS_Effects.WATER_HAG_DECOCTION_EFFECT
                );

                ALGHOUL_DECOCTION = registerItemPotion("alghoul_decoction",
                        TCOTS_Effects.ALGHOUL_DECOCTION_EFFECT
                );

                FOGLET_DECOCTION = registerItemPotion("foglet_decoction",
                        TCOTS_Effects.FOGLET_DECOCTION_EFFECT
                );

                NEKKER_WARRIOR_DECOCTION = registerItemPotion("nekker_warrior_decoction",
                        TCOTS_Effects.NEKKER_WARRIOR_DECOCTION_EFFECT
                );

                TROLL_DECOCTION = registerItemPotion("troll_decoction",
                        TCOTS_Effects.TROLL_DECOCTION_EFFECT
                );
            }
        }

        //Bombs
        {
            EMPTY_BOMB_POWDER = registerItem("bomb_powder",
                    new EmptyBombPowderItem(new Item.Properties()));

            EMPTY_BOMB_POWDER_2 = registerItem("bomb_powder_2",
                    new EmptyBombPowderItem(new Item.Properties().stacksTo(2)));

            EMPTY_BOMB_POWDER_3 = registerItem("bomb_powder_3",
                    new EmptyBombPowderItem(new Item.Properties().stacksTo(3)));

            EMPTY_BOMB_POWDER_4 = registerItem("bomb_powder_4",
                    new EmptyBombPowderItem(new Item.Properties().stacksTo(4)));


            GRAPESHOT = registerItem("grapeshot",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "grapeshot", 0));

            GRAPESHOT_ENHANCED = registerItem("grapeshot_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "grapeshot", 1));

            GRAPESHOT_SUPERIOR = registerItem("grapeshot_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "grapeshot", 2));


            SAMUM = registerItem("samum",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "samum", 0));

            SAMUM_ENHANCED = registerItem("samum_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "samum", 1));

            SAMUM_SUPERIOR = registerItem("samum_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "samum", 2));


            DANCING_STAR = registerItem("dancing_star",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "dancing_star", 0));

            DANCING_STAR_ENHANCED = registerItem("dancing_star_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "dancing_star", 1));

            DANCING_STAR_SUPERIOR = registerItem("dancing_star_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "dancing_star", 2));


            DEVILS_PUFFBALL = registerItem("devils_puffball",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "devils_puffball", 0));

            DEVILS_PUFFBALL_ENHANCED = registerItem("devils_puffball_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "devils_puffball", 1));

            DEVILS_PUFFBALL_SUPERIOR = registerItem("devils_puffball_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "devils_puffball", 2));


            NORTHERN_WIND = registerItem("northern_wind",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "northern_wind", 0));

            NORTHERN_WIND_ENHANCED = registerItem("northern_wind_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "northern_wind", 1));

            NORTHERN_WIND_SUPERIOR = registerItem("northern_wind_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "northern_wind", 2));


            DRAGONS_DREAM = registerItem("dragons_dream",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "dragons_dream", 0));

            DRAGONS_DREAM_ENHANCED = registerItem("dragons_dream_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "dragons_dream", 1));

            DRAGONS_DREAM_SUPERIOR = registerItem("dragons_dream_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "dragons_dream", 2));


            DIMERITIUM_BOMB = registerItem("dimeritium_bomb",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "dimeritium_bomb", 0));

            DIMERITIUM_BOMB_ENHANCED = registerItem("dimeritium_bomb_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "dimeritium_bomb", 1));

            DIMERITIUM_BOMB_SUPERIOR = registerItem("dimeritium_bomb_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "dimeritium_bomb", 2));


            MOON_DUST = registerItem("moon_dust",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(2), "moon_dust", 0));

            MOON_DUST_ENHANCED = registerItem("moon_dust_enhanced",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(3).rarity(Rarity.UNCOMMON), "moon_dust", 1));

            MOON_DUST_SUPERIOR = registerItem("moon_dust_superior",
                    new WitcherBombs_Base(new Item.Properties().stacksTo(4).rarity(Rarity.UNCOMMON), "moon_dust", 2));

        }

        //Oils
        {
            EMPTY_OIL = registerItem("empty_oil",
                    new EmptyWitcherPotionItem(new Item.Properties().stacksTo(1))
            );

            NECROPHAGE_OIL = registerItem("oil_necrophage",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.NECROPHAGES,
                            20,
                            1)
            );
            ENHANCED_NECROPHAGE_OIL = registerItem("oil_necrophage_enhanced",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.NECROPHAGES,
                            40,
                            2)
            );
            SUPERIOR_NECROPHAGE_OIL = registerItem("oil_necrophage_superior",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.NECROPHAGES,
                            60,
                            3)
            );

            OGROID_OIL = registerItem("oil_ogroid",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.OGROIDS,
                            20,
                            1)
            );
            ENHANCED_OGROID_OIL = registerItem("oil_ogroid_enhanced",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.OGROIDS,
                            40,
                            2)
            );
            SUPERIOR_OGROID_OIL = registerItem("oil_ogroid_superior",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.OGROIDS,
                            60,
                            3)
            );

            BEAST_OIL = registerItem("oil_beast",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.BEASTS,
                            20,
                            1)
            );
            ENHANCED_BEAST_OIL = registerItem("oil_beast_enhanced",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.BEASTS,
                            40,
                            2)
            );
            SUPERIOR_BEAST_OIL = registerItem("oil_beast_superior",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.BEASTS,
                            60,
                            3)
            );

            HANGED_OIL = registerItem("oil_hanged",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON),
                            WitcherMonsterOil_Base.MonsterOilType.HUMANOID,
                            20,
                            1)
            );
            ENHANCED_HANGED_OIL = registerItem("oil_hanged_enhanced",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.HUMANOID,
                            40,
                            2)
            );
            SUPERIOR_HANGED_OIL = registerItem("oil_hanged_superior",
                    new WitcherMonsterOil_Base(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON),
                            WitcherMonsterOil_Base.MonsterOilType.HUMANOID,
                            60,
                            3)
            );
        }
    }

    public static Item CURED_MONSTER_LEATHER;
    //Weapons
    public static Item KNIGHT_CROSSBOW;
    public static Item BASE_BOLT;
    public static Item BLUNT_BOLT;
    public static Item PRECISION_BOLT;
    public static Item EXPLODING_BOLT;
    public static Item BROADHEAD_BOLT;
    public static Item GVALCHIR;
    public static Item MOONBLADE;
    public static Item WINTERS_BLADE;
    public static Item ARDAENYE;
    public static Item DYAEBL;
    public static Item GIANT_ANCHOR;

    //Armors
    public static Item WARRIORS_LEATHER_JACKET;
    public static Item WARRIORS_LEATHER_TROUSERS;
    public static Item WARRIORS_LEATHER_BOOTS;

    public static Item MANTICORE_ARMOR;
    public static Item MANTICORE_TROUSERS;
    public static Item MANTICORE_BOOTS;

    public static Item RAVENS_ARMOR;
    public static Item RAVENS_TROUSERS;
    public static Item RAVENS_BOOTS;

    //Horse Armor
    public static Item TUNDRA_HORSE_ARMOR;
    public static Item KNIGHT_ERRANTS_HORSE_ARMOR;


    public static void registerWeapons_Armors(){

        //Ingredients
        {
            CURED_MONSTER_LEATHER=registerItem("cured_monster_leather", new Item(new Item.Properties()));
        }

        //Crossbows
        {
            KNIGHT_CROSSBOW = registerItem("knight_crossbow",
                    new KnightCrossbow(new Item.Properties().stacksTo(1).durability(600)));

            BASE_BOLT = registerItem("base_bolt", new BoltItem(new Item.Properties(), "base_bolt"));

            BLUNT_BOLT = registerItem("blunt_bolt", new BoltItem(new Item.Properties().stacksTo(32), "blunt_bolt"));

            PRECISION_BOLT = registerItem("precision_bolt", new BoltItem(new Item.Properties().stacksTo(32), "precision_bolt"));

            EXPLODING_BOLT = registerItem("exploding_bolt", new BoltItem(new Item.Properties().stacksTo(32), "exploding_bolt"));

            BROADHEAD_BOLT = registerItem("broadhead_bolt", new BoltItem(new Item.Properties().stacksTo(32), "broadhead_bolt"));
        }

        //Swords
        {
            GVALCHIR = registerItem("gvalchir",
                    new SwordWithTooltip(TCOTS_ToolMaterials.GVALCHIR, new Item.Properties().rarity(Rarity.UNCOMMON)
                            .attributes(
                                    SwordsAttributes.createGvalchirAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.gvalchir").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.gvalchir.extra").withStyle(ChatFormatting.DARK_GREEN))
                    ));

            MOONBLADE = registerItem("moonblade",
                    new SwordWithTooltip(TCOTS_ToolMaterials.MOONBLADE, new Item.Properties().rarity(Rarity.UNCOMMON)
                            .attributes(
                                    SwordsAttributes.createMoonbladeAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.moonblade").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.moonblade.extra").withStyle(ChatFormatting.DARK_GREEN))
                    ));

            DYAEBL = registerItem("dyaebl",
                    new SwordWithTooltip(TCOTS_ToolMaterials.DYAEBL, new Item.Properties().rarity(Rarity.UNCOMMON)
                            .attributes(
                                    SwordsAttributes.createDyaeblAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.dyaebl").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.dyaebl.extra").withStyle(ChatFormatting.DARK_RED))
                    ));

            WINTERS_BLADE = registerItem("winters_blade",
                    new SwordWithTooltip(TCOTS_ToolMaterials.WINTERS_BLADE, new Item.Properties().rarity(Rarity.RARE)
                            .attributes(
                                    SwordsAttributes.createWintersBladeAttributeModifiers()),
                            Component.translatable("tooltip.tcots_witcher.winters_blade").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.winters_blade.extra").withColor(0x007b77),
                                    Component.translatable("tooltip.tcots_witcher.winters_blade.extra2").withColor(0x007b77),
                                    Component.translatable("tooltip.tcots_witcher.winters_blade.extra3").withColor(0x007b77))
                    ));

            ARDAENYE = registerItem("ardaenye",
                    new SwordItem(TCOTS_ToolMaterials.ARDAENYE, new Item.Properties()
                            .attributes(
                                    SwordsAttributes.createArdaenyeAttributeModifiers())));
        }

        //Armor
        {
            MANTICORE_ARMOR = registerItem("manticore_armor", new ManticoreArmorItem(
                    TCOTS_ArmorMaterials.MANTICORE,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.CHESTPLATE.getDurability(20))
                            .attributes(addManticoreArmorAttributes(TCOTS_ArmorMaterials.MANTICORE, ArmorItem.Type.CHESTPLATE, 10, 0.075)))
            );

            MANTICORE_TROUSERS = registerItem("manticore_trousers", new ManticoreArmorItem(
                            TCOTS_ArmorMaterials.MANTICORE,
                            ArmorItem.Type.LEGGINGS,
                            new Item.Properties().rarity(Rarity.UNCOMMON)
                                    .durability(ArmorItem.Type.LEGGINGS.getDurability(20))
                                    .attributes(addManticoreArmorAttributes(TCOTS_ArmorMaterials.MANTICORE, ArmorItem.Type.LEGGINGS, 10, 0.075)))
            );

            MANTICORE_BOOTS = registerItem("manticore_boots", new ManticoreArmorItem(
                    TCOTS_ArmorMaterials.MANTICORE,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.BOOTS.getDurability(20))
                            .attributes(addManticoreArmorAttributes(TCOTS_ArmorMaterials.MANTICORE, ArmorItem.Type.BOOTS, 10, 0.075)))
            );

            WARRIORS_LEATHER_JACKET = registerItem("warriors_leather_jacket", new WarriorsLeatherArmorItem(
                    TCOTS_ArmorMaterials.WARRIORS_LEATHER,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties()
                            .durability(ArmorItem.Type.CHESTPLATE.getDurability(15))
                            .attributes(addArmorWithAdrenalineAttributes(TCOTS_ArmorMaterials.WARRIORS_LEATHER, ArmorItem.Type.CHESTPLATE, 0.02))));
            WARRIORS_LEATHER_TROUSERS = registerItem("warriors_leather_trousers", new WarriorsLeatherArmorItem(
                    TCOTS_ArmorMaterials.WARRIORS_LEATHER,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties()
                            .durability(ArmorItem.Type.LEGGINGS.getDurability(15))
                            .attributes(addArmorWithAdrenalineAttributes(TCOTS_ArmorMaterials.WARRIORS_LEATHER, ArmorItem.Type.LEGGINGS, 0.02))));
            WARRIORS_LEATHER_BOOTS = registerItem("warriors_leather_boots", new WarriorsLeatherArmorItem(
                    TCOTS_ArmorMaterials.WARRIORS_LEATHER,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties()
                            .durability(ArmorItem.Type.BOOTS.getDurability(15))
                            .attributes(addArmorWithAdrenalineAttributes(TCOTS_ArmorMaterials.WARRIORS_LEATHER, ArmorItem.Type.BOOTS, 0.02))));

            RAVENS_ARMOR = registerItem("ravens_armor", new RavensArmorItem(
                    TCOTS_ArmorMaterials.RAVEN,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.CHESTPLATE.getDurability(30))
                            .attributes(addArmorWithAdrenalineAttributes(TCOTS_ArmorMaterials.RAVEN, ArmorItem.Type.BODY, 0.10))));
            RAVENS_TROUSERS = registerItem("ravens_trousers", new RavensArmorItem(
                    TCOTS_ArmorMaterials.RAVEN,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.LEGGINGS.getDurability(30))
                            .attributes(addArmorWithAdrenalineAttributes(TCOTS_ArmorMaterials.RAVEN, ArmorItem.Type.LEGGINGS, 0.10))));
            RAVENS_BOOTS = registerItem("ravens_boots", new RavensArmorItem(
                    TCOTS_ArmorMaterials.RAVEN,
                    ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.UNCOMMON)
                            .durability(ArmorItem.Type.BOOTS.getDurability(30))
                            .attributes(addArmorWithAdrenalineAttributes(TCOTS_ArmorMaterials.RAVEN, ArmorItem.Type.BOOTS, 0.10))));
        }

        //Horse Armor
        {
            TUNDRA_HORSE_ARMOR = registerItem("tundra_horse_armor",
                    new WitcherHorseArmorItem(TCOTS_ArmorMaterials.TUNDRA, "tundra", new Item.Properties().stacksTo(1),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.tundra_horse_armor").withStyle(ChatFormatting.AQUA),
                                    Component.translatable("tooltip.tcots_witcher.tundra_horse_armor2").withStyle(ChatFormatting.AQUA))));

            KNIGHT_ERRANTS_HORSE_ARMOR = registerItem("knight_errants_horse_armor",
                    new WitcherHorseArmorItem(TCOTS_ArmorMaterials.KNIGHT, "knight_errants", new Item.Properties().stacksTo(1),
                            List.of(
                                    Component.translatable("tooltip.tcots_witcher.knight_errants_horse_armor").withStyle(ChatFormatting.DARK_GREEN),
                                    Component.translatable("tooltip.tcots_witcher.knight_errants_horse_armor2").withStyle(ChatFormatting.DARK_GREEN))));
        }

        //Misc
        {
            GIANT_ANCHOR=registerItem("giant_anchor", new GiantAnchorItem(new
                    Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .attributes(SwordItem.createAttributes(TCOTS_ToolMaterials.ANCHOR, 1, -3.4f))));

        }
    }

    @SuppressWarnings("unused")
    public static ItemAttributeModifiers addExtraToxicity(double toxicity, EquipmentSlotGroup slot, String id){
       return ItemAttributeModifiers.builder()
                .add(
                        TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY,
                        new AttributeModifier(
                                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id),
                                toxicity,
                                AttributeModifier.Operation.ADD_VALUE
                        ),
                        slot
                )
                .build();
    }

    public static ItemAttributeModifiers addArmorWithAdrenalineAttributes(Holder<ArmorMaterial> material, ArmorItem.Type type, double adrenaline){
        int i = material.value().getDefense(type);
        float f = material.value().toughness();
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup attributeModifierSlot = EquipmentSlotGroup.bySlot(type.getSlot());
        ResourceLocation identifier = ResourceLocation.withDefaultNamespace("armor." + type.getName());

        builder.add(
                Attributes.ARMOR, new AttributeModifier(identifier, i, AttributeModifier.Operation.ADD_VALUE), attributeModifierSlot
        );
        builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(identifier, f, AttributeModifier.Operation.ADD_VALUE),
                attributeModifierSlot
        );
        float g = material.value().knockbackResistance();
        if (g > 0.0F) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(identifier, g, AttributeModifier.Operation.ADD_VALUE),
                    attributeModifierSlot
            );
        }

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder.add(
                    TCOTS_EntityAttributes.ADRENALINE_GAIN,
                    new AttributeModifier(
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                            adrenaline,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    attributeModifierSlot
            );
        }

        return builder.build();
    }


    public static ItemAttributeModifiers addManticoreArmorAttributes(Holder<ArmorMaterial> material, ArmorItem.Type type, int toxicity, double adrenaline){
        int i = material.value().getDefense(type);
        float f = material.value().toughness();
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup attributeModifierSlot = EquipmentSlotGroup.bySlot(type.getSlot());
        ResourceLocation identifier = ResourceLocation.withDefaultNamespace("armor." + type.getName());

        builder.add(
                Attributes.ARMOR, new AttributeModifier(identifier, i, AttributeModifier.Operation.ADD_VALUE), attributeModifierSlot
        );
        builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(identifier, f, AttributeModifier.Operation.ADD_VALUE),
                attributeModifierSlot
        );
        float g = material.value().knockbackResistance();
        if (g > 0.0F) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(identifier, g, AttributeModifier.Operation.ADD_VALUE),
                    attributeModifierSlot
            );
        }
        builder.add(
                TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY,
                new AttributeModifier(
                        ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                        toxicity,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                attributeModifierSlot
        );

        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            builder.add(
                    TCOTS_EntityAttributes.ADRENALINE_GAIN,
                    new AttributeModifier(
                            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "armor."+ type.getName()),
                            adrenaline,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    attributeModifierSlot
            );
        }

        return builder.build();
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

    public static void registerAlchemyIngredients(){

        //Alcohol
        {
            ICY_SPIRIT = (WitcherAlcohol_Base) registerItem("icy_spirit",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(64),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 200, 0),
                                    new MobEffectInstance(MobEffects.SATURATION, 10, 1)),
                            1));

            DWARVEN_SPIRIT = (WitcherAlcohol_Base) registerItem("dwarven_spirit",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(16),
                            List.of(new MobEffectInstance(MobEffects.CONFUSION, 1, 200)),
                            2));

            ALCOHEST = (WitcherAlcohol_Base) registerItem("alcohest",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(16),
                            List.of(new MobEffectInstance(MobEffects.CONFUSION, 600, 2)),
                            4));

            WHITE_GULL = (WitcherAlcohol_Base) registerItem("white_gull",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(8),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 1200, 3),
                                    new MobEffectInstance(MobEffects.POISON, 40, 1)),
                            8));


            VILLAGE_HERBAL = (WitcherAlcohol_Base) registerItem("village_herbal",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(8),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 200, 1),
                                    new MobEffectInstance(MobEffects.REGENERATION, 200, 0)),
                            4));

            CHERRY_CORDIAL = (WitcherAlcohol_Base) registerItem("cherry_cordial",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(16),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 200, 1),
                                    new MobEffectInstance(MobEffects.ABSORPTION, 200, 0)),
                            2));

            MANDRAKE_CORDIAL = (WitcherAlcohol_Base) registerItem("mandrake_cordial",
                    new WitcherAlcohol_Base(new Item.Properties().stacksTo(8),
                            Arrays.asList(
                                    new MobEffectInstance(MobEffects.CONFUSION, 400, 2),
                                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 400, 0)),
                            6));
        }


        //Substances
        {
            ALCHEMY_PASTE = registerItem("alchemy_paste",
                    new Item(new Item.Properties()));

            MONSTER_FAT = registerItem("monster_fat",
                    new Item(new Item.Properties()));

            STAMMELFORDS_DUST = registerItem("stammelfords_dust",
                    new Item(new Item.Properties()));

            ALCHEMISTS_POWDER = registerItem("alchemists_powder",
                    new Item(new Item.Properties()));

            AETHER = registerItem("aether",
                    new Item(new Item.Properties()));

            VITRIOL = registerItem("vitriol",
                    new Item(new Item.Properties()));

            VERMILION = registerItem("vermilion",
                    new Item(new Item.Properties()));

            HYDRAGENUM = registerItem("hydragenum",
                    new Item(new Item.Properties()));

            QUEBRITH = registerItem("quebrith",
                    new Item(new Item.Properties()));

            RUBEDO = registerItem("rubedo",
                    new Item(new Item.Properties()));

            REBIS = registerItem("rebis",
                    new Item(new Item.Properties()));

            NIGREDO = registerItem("nigredo",
                    new Item(new Item.Properties()));
        }

        //Plants
        {
            ALLSPICE = registerItem("allspice",
                    new Item(new Item.Properties()));

            ARENARIA = registerItem("arenaria",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.ARENARIA_BUSH,
                            new Item.Properties()));

            CELANDINE = registerItem("celandine",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.CELANDINE_PLANT,
                            new Item.Properties()));

            CROWS_EYE = registerItem("crows_eye",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.CROWS_EYE_FERN,
                            new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1.2f).effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 0.8f).build())));

            BRYONIA = registerItem("bryonia",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.BRYONIA_VINE,
                            new Item.Properties()));

            VERBENA = registerItem("verbena",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.VERBENA_FLOWER,
                            new Item.Properties()));

            HAN_FIBER = registerItem("han_fiber",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.HAN_FIBER_PLANT,
                            new Item.Properties()));

            PUFFBALL = registerItem("puffball",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.PUFFBALL_MUSHROOM,
                            new Item.Properties()));

            SEWANT_MUSHROOMS = registerItem("sewant_mushrooms",
                    new ItemNameBlockItem(TCOTS_Blocks_Fabric.SEWANT_MUSHROOMS_PLANT,
                            new Item.Properties()));


            ERGOT_SEEDS = registerItem("ergot_seeds",
                    new Item(new Item.Properties()));


        }
    }



    public static void modifyLootTables(){


        LootTableEvents.MODIFY.register( (id, tableBuilder, source, wrapperLookup) -> {

            if(Blocks.WHEAT.getLootTable().equals(id) && source.isBuiltin()){
                LootPool.Builder ergotSeeds = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ERGOT_SEEDS))
                        .when(LootItemRandomChanceCondition.randomChance(0.05f))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.WHEAT)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(CropBlock.AGE,7)))
                        .apply(ApplyExplosionDecay.explosionDecay());

                tableBuilder.pool(ergotSeeds.build());
            }

            if(EntityType.RAVAGER.getDefaultLootTable().equals(id) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MONSTER_FAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(3f,8f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup,0.8f, 0.1f));



                tableBuilder.pool(monsterFat.build());
            }

            if((EntityType.HOGLIN.getDefaultLootTable().equals(id) || EntityType.ZOGLIN.getDefaultLootTable().equals(id)) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MONSTER_FAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2f,4f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup ,0.4f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

            if(EntityType.POLAR_BEAR.getDefaultLootTable().equals(id) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MONSTER_FAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2f,3f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup,0.3f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

            if(EntityType.PIGLIN_BRUTE.getDefaultLootTable().equals(id) && source.isBuiltin()){
                LootPool.Builder monsterFat = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MONSTER_FAT).apply(SetItemCountFunction.setCount(UniformGenerator.between(1f,2f))))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(wrapperLookup, 0.25f, 0.1f));

                tableBuilder.pool(monsterFat.build());
            }

            //Formulae/Alcohol
            {
                if (BuiltInLootTables.ABANDONED_MINESHAFT.equals(id) && source.isBuiltin()) {

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .when(LootItemRandomChanceCondition.randomChance(0.15f));

                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                if (BuiltInLootTables.ANCIENT_CITY.equals(id) && source.isBuiltin()) {

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(new AlchemyRecipeRandomlyLootFunction.Builder().add(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.9f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                //Bastion
                {
                    if (BuiltInLootTables.BASTION_BRIDGE.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.4f));

                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.05f));

                        tableBuilder.pool(witcher_alcohol.build());
                        tableBuilder.pool(alchemy_formulae.build());
                    }

                    if (BuiltInLootTables.BASTION_OTHER.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.3f));

                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.05f));

                        tableBuilder.pool(witcher_alcohol.build());
                        tableBuilder.pool(alchemy_formulae.build());
                    }
                }

                if(BuiltInLootTables.DESERT_PYRAMID.equals(id) && source.isBuiltin()){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.1f));

                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                if(BuiltInLootTables.IGLOO_CHEST.equals(id) && source.isBuiltin()){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(1f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,5))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ICY_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3))))
                            .when(LootItemRandomChanceCondition.randomChance(0.8f));

                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                if(BuiltInLootTables.JUNGLE_TEMPLE.equals(id) && source.isBuiltin()){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.8f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.pool(witcher_alcohol.build());

                    tableBuilder.pool(alchemy_formulae.build());
                }

                if(BuiltInLootTables.NETHER_BRIDGE.equals(id) && source.isBuiltin()){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.6f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }


                if(BuiltInLootTables.PILLAGER_OUTPOST.equals(id) && source.isBuiltin()){

                    LootPool.Builder extra_loot_oils = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.HANGED_OIL).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ENHANCED_HANGED_OIL).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.05f));

                    LootPool.Builder crossbow_bolts = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,4))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.BASE_BOLT).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,12))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.BLUNT_BOLT).setWeight(5).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.PRECISION_BOLT).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,6))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.BROADHEAD_BOLT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.EXPLODING_BOLT).setWeight(2).apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.5f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))

                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ICY_SPIRIT).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.CHERRY_CORDIAL).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.VILLAGE_HERBAL).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MANDRAKE_CORDIAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.25f));

                    tableBuilder.pool(extra_loot_oils.build());
                    tableBuilder.pool(crossbow_bolts.build());
                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                //Shipwreck
                {
                    if (BuiltInLootTables.SHIPWRECK_SUPPLY.equals(id) && source.isBuiltin()) {
                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1, 4))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))

                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ICY_SPIRIT).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.CHERRY_CORDIAL).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 4))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.VILLAGE_HERBAL).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 4))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MANDRAKE_CORDIAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2))))

                                .when(LootItemRandomChanceCondition.randomChance(0.6f));

                        tableBuilder.pool(witcher_alcohol.build());
                    }

                    if (BuiltInLootTables.SHIPWRECK_MAP.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.6f));

                        tableBuilder.pool(alchemy_formulae.build());
                    }
                }

                if(BuiltInLootTables.SIMPLE_DUNGEON.equals(id) && source.isBuiltin()){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.7f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                //Stronghold
                {
                    if (BuiltInLootTables.STRONGHOLD_CORRIDOR.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 3))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.6f));

                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(1,2))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.3f));

                        tableBuilder.pool(witcher_alcohol.build());
                        tableBuilder.pool(alchemy_formulae.build());
                    }

                    if (BuiltInLootTables.STRONGHOLD_CROSSING.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.4f));

                        tableBuilder.pool(alchemy_formulae.build());
                    }

                    if (BuiltInLootTables.STRONGHOLD_LIBRARY.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 4))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(1f));

                        tableBuilder.pool(alchemy_formulae.build());
                    }
                }

                //Underwater
                {
                    if (BuiltInLootTables.UNDERWATER_RUIN_BIG.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.3f));

                        tableBuilder.pool(alchemy_formulae.build());
                    }

                    if (BuiltInLootTables.UNDERWATER_RUIN_SMALL.equals(id) && source.isBuiltin()) {

                        LootPool.Builder alchemy_formulae = LootPool.lootPool()
                                .setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                                .when(LootItemRandomChanceCondition.randomChance(0.2f));

                        tableBuilder.pool(alchemy_formulae.build());
                    }
                }

                if(BuiltInLootTables.WOODLAND_MANSION.equals(id) && source.isBuiltin()){
                    LootPool.Builder extra_loot_oils = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.HANGED_OIL).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ENHANCED_HANGED_OIL).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.SUPERIOR_HANGED_OIL).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.05f));

                    LootPool.Builder witcher_books = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.WITCHER_BESTIARY).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_BOOK).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.05f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(14).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))

                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ICY_SPIRIT).setWeight(4).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.CHERRY_CORDIAL).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.VILLAGE_HERBAL).setWeight(8).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MANDRAKE_CORDIAL).setWeight(18).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.1f));

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.6f));

                    tableBuilder.pool(extra_loot_oils.build());
                    tableBuilder.pool(witcher_books.build());
                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());
                }

                //Village
                {
                    if(BuiltInLootTables.FARMER_GIFT.equals(id) && source.isBuiltin()){
                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(15).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.VILLAGE_HERBAL).setWeight(20).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                .when(LootItemRandomChanceCondition.randomChance(0.5f));

                        tableBuilder.pool(witcher_alcohol.build());
                    }

                    if((BuiltInLootTables.VILLAGE_PLAINS_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_DESERT_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_SAVANNA_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_TAIGA_HOUSE.equals(id)
                            || BuiltInLootTables.VILLAGE_SNOWY_HOUSE.equals(id))

                            && source.isBuiltin()){
                        LootPool.Builder witcher_alcohol = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(11).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))

                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ICY_SPIRIT).setWeight(6).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.CHERRY_CORDIAL).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.VILLAGE_HERBAL).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.MANDRAKE_CORDIAL).setWeight(10).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1))))
                                .when(LootItemRandomChanceCondition.randomChance(0.15f));

                        LootPool.Builder witcher_bestiary = LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(TCOTS_Items_Fabric.WITCHER_BESTIARY).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                                .when(LootItemRandomChanceCondition.randomChance(0.05f));
                        
                        tableBuilder.pool(witcher_bestiary.build());
                        tableBuilder.pool(witcher_alcohol.build());
                    }

                }


                if(BuiltInLootTables.SPAWN_BONUS_CHEST.equals(id) && source.isBuiltin()){

                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0,2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA)).apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.5f));

                    LootPool.Builder witcher_alcohol = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1,3))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST).setWeight(12).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,3))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT).setWeight(15).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,4))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ICY_SPIRIT).setWeight(18).apply(SetItemCountFunction.setCount(UniformGenerator.between(0,5))))
                            .when(LootItemRandomChanceCondition.randomChance(0.3f));

                    LootPool.Builder witcher_Books = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.WITCHER_BESTIARY).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_BOOK).setWeight(1).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1))))
                            .when(LootItemRandomChanceCondition.randomChance(0.1f));

                    tableBuilder.withPool(witcher_Books);
                    tableBuilder.pool(witcher_alcohol.build());
                    tableBuilder.pool(alchemy_formulae.build());

                }

            }

            //Sniffer Digging
            {
                if (BuiltInLootTables.SNIFFER_DIGGING.equals(id) && source.isBuiltin()) {

                    LootPool.Builder allspice = LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALLSPICE))
                            .when(LootItemRandomChanceCondition.randomChance(0.4f));

                    tableBuilder.pool(allspice.build());
                }
            }
        });


        LootTableEvents.REPLACE.register((id, lootTable, source, wrapperLookup) -> {

            //Witcher Grave - Disable Winters Blade
            {
                if(id.location().equals(ResourceLocation.fromNamespaceAndPath("witcher_rpg","chests/witcher_grave"))){

                    LootTable.Builder newLootTable = new LootTable.Builder();

                    LootPool.Builder ingredientsPool = LootPool.lootPool().setRolls(UniformGenerator.between(3,5))
                            .setBonusRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Items.LEATHER)
                                    .setWeight(10)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,5)))
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("more_rpg_classes", "hardened_leather")))
                                    .setWeight(7)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2)))
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "meteorite_ingot")))
                                    .setWeight(5)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_iron_ingot")))
                                    .setWeight(5)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,3)))
                            )
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.CURED_MONSTER_LEATHER)
                                    .setWeight(2)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,2)))
                            )
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DWARVEN_SPIRIT)
                                    .setWeight(1)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4)))
                            )
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCOHEST)
                                    .setWeight(1)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1,4)))
                            );

                    LootPool.Builder diagramsPool = LootPool.lootPool().setRolls(UniformGenerator.between(1,1))
                            .setBonusRolls(ConstantValue.exactly(1))
                            .add(EmptyLootItem.emptyItem()
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_feline_diagram")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_griffin_diagram")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_ursine_diagram")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "enhanced_wolven_diagram")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_feline_diagram")))
                                    .setWeight(1)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_griffin_diagram")))
                                    .setWeight(1)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_ursine_diagram")))
                                    .setWeight(1)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "superior_wolven_diagram")))
                                    .setWeight(1)
                            );

                    LootPool.Builder weaponsPool = LootPool.lootPool().setRolls(UniformGenerator.between(1,1))
                            .setBonusRolls(ConstantValue.exactly(1))
                            .add(EmptyLootItem.emptyItem()
                                    .setWeight(10)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "silver_witcher_sword")))
                                    .setWeight(10)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "iron_witcher_sword")))
                                    .setWeight(10)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "steel_witcher_sword")))
                                    .setWeight(10)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "diamond_witcher_sword")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "meteorite_witcher_sword")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "dark_iron_witcher_sword")))
                                    .setWeight(5)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "ultimatum_sword")))
                                    .setWeight(3)
                            )
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.DYAEBL)
                                    .setWeight(3)
                            )
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ARDAENYE)
                                    .setWeight(1)
                            )
                            .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "aerondight_sword")))
                                    .setWeight(1)
                            );


                    LootPool.Builder alchemy_formulae = LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 2))
                            .add(LootItem.lootTableItem(TCOTS_Items_Fabric.ALCHEMY_FORMULA))
                            .apply(AlchemyRecipeRandomlyLootFunction.builder())
                            .when(LootItemRandomChanceCondition.randomChance(0.45f));


                    newLootTable.withPool(ingredientsPool);
                    newLootTable.withPool(diagramsPool);
                    newLootTable.withPool(weaponsPool);
                    newLootTable.withPool(alchemy_formulae);

                    if(FabricLoader.getInstance().isModLoaded("witcher_medallions")){
                        LootPool.Builder medallionsPool = LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(EmptyLootItem.emptyItem()
                                        .setWeight(8)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "wolf-medallion-off")))
                                        .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "cat-medallion-off")))
                                        .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "bear-medallion-off")))
                                        .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "griffin-medallion-off")))
                                        .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "viper-medallion-off")))
                                        .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "manticore-medallion-off")))
                                        .setWeight(5)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "wolf-medallion")))
                                        .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "cat-medallion")))
                                        .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "bear-medallion")))
                                        .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "griffin-medallion")))
                                        .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "viper-medallion")))
                                        .setWeight(1)
                                )
                                .add(LootItem.lootTableItem(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("witcher-medallions", "manticore-medallion")))
                                        .setWeight(1)
                                );

                        newLootTable.withPool(medallionsPool);
                    }

                    return newLootTable.build();
                }
            }

            return lootTable;
        });
    }

    public static Item NEST_SLAB_ITEM;
    public static Item NEST_SKULL_ITEM;
    public static Item MONSTER_NEST_ITEM;
    public static Item ALCHEMY_TABLE_ITEM;

    public static Item HERBAL_TABLE_ITEM;

    public static Item GIANT_ANCHOR_BLOCK_ITEM;
    public static Item WINTERS_BLADE_SKELETON_ITEM;
    public static Item SKELETON_BLOCK_ITEM;

    public static Item HERBAL_MIXTURE;
    public static Item WITCHER_BESTIARY;
    public static Item ALCHEMY_BOOK;

    public static void registerItemsMisc(){

        PUFFBALL_MUSHROOM_BLOCK_ITEM  = registerBlockItem("puffball_mushroom_block", TCOTS_Blocks_Fabric.PUFFBALL_MUSHROOM_BLOCK);

        SEWANT_MUSHROOM_BLOCK_ITEM  = registerBlockItem("sewant_mushroom_block", TCOTS_Blocks_Fabric.SEWANT_MUSHROOM_BLOCK);

        SEWANT_MUSHROOM_STEM_ITEM  = registerBlockItem("sewant_mushroom_stem", TCOTS_Blocks_Fabric.SEWANT_MUSHROOM_STEM);

        NEST_SLAB_ITEM  = registerBlockItem("nest_slab", TCOTS_Blocks_Fabric.NEST_SLAB);

        NEST_SKULL_ITEM = registerItem("nest_skull", new NestSkullItem(TCOTS_Blocks_Fabric.NEST_SKULL, TCOTS_Blocks_Fabric.NEST_WALL_SKULL, new Item.Properties(), Direction.DOWN));

        MONSTER_NEST_ITEM = registerItem("monster_nest", new MonsterNestItem(TCOTS_Blocks_Fabric.MONSTER_NEST, new Item.Properties()));

        ALCHEMY_TABLE_ITEM = registerItem("alchemy_table", new AlchemyTableItem(TCOTS_Blocks_Fabric.ALCHEMY_TABLE, new Item.Properties()));

        HERBAL_TABLE_ITEM = registerItem("herbal_table", new HerbalTableItem(TCOTS_Blocks_Fabric.HERBAL_TABLE, new Item.Properties()));

        HERBAL_MIXTURE = registerItem("herbal_mixture", new HerbalMixture(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.2f).alwaysEdible().build()).stacksTo(2)));


        GIANT_ANCHOR_BLOCK_ITEM = registerItem("giant_anchor_block", new GiantAnchorBlockItem(TCOTS_Blocks_Fabric.GIANT_ANCHOR, new Item.Properties()));

        WINTERS_BLADE_SKELETON_ITEM = registerItem("winters_blade_skeleton", new WintersBladeSkeletonItem(TCOTS_Blocks.WINTERS_BLADE_SKELETON, new Item.Properties()));

        SKELETON_BLOCK_ITEM = registerItem("skeleton_block", new SkeletonBlockItem(TCOTS_Blocks.SKELETON_BLOCK, new Item.Properties()));


        WITCHER_BESTIARY = WitcherBestiaryItem.registerForBook(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_bestiary"), new Item.Properties().stacksTo(1));

        ALCHEMY_BOOK = AlchemyBookItem.registerForBook(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alchemy_book"), new Item.Properties().stacksTo(1));
    }

    public static final LootItemFunctionType<? extends LootItemConditionalFunction> RANDOMIZE_FORMULA = register("randomize_formula", AlchemyRecipeRandomlyLootFunction.CODEC);

    @SuppressWarnings("all")
    private static LootItemFunctionType<? extends LootItemConditionalFunction> register(String id, MapCodec<? extends LootItemFunction> codec) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id), new LootItemFunctionType(codec));
    }

    @SuppressWarnings("unused")
    public static void registerCompostableItems(){
        float f = 0.3f;
        float g = 0.5f;
        float h = 0.65f;
        float i = 0.85f;
        float j = 1.0f;
        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.ARENARIA);
        ComposterBlock.add(0.3f, TCOTS_Items_Fabric.ALLSPICE);

        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.BRYONIA);

        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.CELANDINE);
        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.CROWS_EYE);
        ComposterBlock.add(0.85f, TCOTS_Items_Fabric.CADAVERINE);

        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.HAN_FIBER);


        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.PUFFBALL);
        ComposterBlock.add(0.85f, TCOTS_Items_Fabric.PUFFBALL_MUSHROOM_BLOCK_ITEM);


        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.SEWANT_MUSHROOMS);
        ComposterBlock.add(0.85f, TCOTS_Items_Fabric.SEWANT_MUSHROOM_STEM_ITEM);
        ComposterBlock.add(0.85f, TCOTS_Items_Fabric.SEWANT_MUSHROOM_BLOCK_ITEM);

        ComposterBlock.add(0.65f, TCOTS_Items_Fabric.VERBENA);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name), new BlockItem(block, new Item.Properties()));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name)), item);
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
    private static Item registerItemPotion(String name, Item.Properties settings, Holder<MobEffect> effect, int toxicity, int durationInSecs, int amplifier, boolean decoction) {
        try {
            ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name);
            MobEffectInstance effectInstance = new MobEffectInstance(effect, (int)(durationInSecs/0.05), amplifier);

            WitcherPotions_Base witcherPotion = new WitcherPotions_Base(settings, effectInstance, toxicity, decoction);

            return Registry.register(BuiltInRegistries.ITEM, identifier, witcherPotion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error registering potion");
        }
    }

    /**
    Creates a witcher potion of type decoction
     @param name Name of the potion
     @param effect Decoction effect
    */
    private static Item registerItemPotion(String name, Holder<MobEffect> effect) {
        return registerItemPotion(name, new Item.Properties().stacksTo(1), effect, 50, 600, 0, true);
    }

    private static Item registerSplashPotion(String name, Item.Properties settings, Holder<MobEffect> effect, int toxicity, int durationInSecs){
        try {
            ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name);

            WitcherPotions_Base witcherPotion = new WitcherPotionsSplash_Base(settings, new MobEffectInstance(effect, (int)(durationInSecs/0.05), 0), toxicity);
            return Registry.register(BuiltInRegistries.ITEM, identifier, witcherPotion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error registering Splash potion");
        }
    }

    private static <T> DataComponentType<T> register(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id, builderOperator.apply(DataComponentType.builder()).build());
    }
}
