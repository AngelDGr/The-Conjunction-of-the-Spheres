package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.blocks.AlchemyTableItem;
import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.blocks.NestSkullItem;
import TCOTS.potions.DwarvenSpiritItem;
import TCOTS.potions.TCOTS_Effects;
import TCOTS.potions.WitcherPotionsSplash_Base;
import TCOTS.potions.WitcherPotions_Base;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Direction;

public class TCOTS_Items {

    //TODO: Add new way to craft the potions
        //TODO: Add new alchemy ingredients (mushrooms, flowers? and alcohol)


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


    public static Item NEKKER_SPAWN_EGG;
    public static Item NEKKER_HEART;
    public static Item NEKKER_EYE;

    //Potions
    public static Item DWARVEN_SPIRIT;
    public static Item SWALLOW_POTION;
    public static Item SWALLOW_POTION_ENHANCED;
    public static Item SWALLOW_POTION_SUPERIOR;
    public static Item WHITE_RAFFARDS_DECOCTION;
    public static Item WHITE_RAFFARDS_DECOCTION_ENHANCED;
    public static Item WHITE_RAFFARDS_DECOCTION_SUPERIOR;
    public static Item KILLER_WHALE_POTION;

    //Splash Potions
    public static Item SWALLOW_SPLASH;
    public static Item KILLER_WHALE_SPLASH;
    public static Item WHITE_RAFFARDS_DECOCTION_SPLASH;

    //Register Witcher Potion Items
    public static void registerPotions() {
        //Potions
        DWARVEN_SPIRIT = registerItem("dwarven_spirit",
                new DwarvenSpiritItem(new FabricItemSettings().maxCount(16), new StatusEffectInstance(StatusEffects.NAUSEA,200), 0));


        SWALLOW_POTION = registerItemPotion("swallow_potion",
                new FabricItemSettings().maxCount(3),
                TCOTS_Effects.SWALLOW_EFFECT,
                20,
                20,
                0
        );

        SWALLOW_POTION_ENHANCED = registerItemPotion("swallow_potion_enhanced",
                new FabricItemSettings().maxCount(4),
                TCOTS_Effects.SWALLOW_EFFECT,
                20,
                20,
                1
        );

        SWALLOW_POTION_SUPERIOR = registerItemPotion("swallow_potion_superior",
                new FabricItemSettings().maxCount(5),
                TCOTS_Effects.SWALLOW_EFFECT,
                20,
                20,
                2
        );

        WHITE_RAFFARDS_DECOCTION = registerItemPotion("white_raffards_decoction",
                new FabricItemSettings().maxCount(2),
                TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                25,
                1,
                0
        );

        WHITE_RAFFARDS_DECOCTION_ENHANCED = registerItemPotion("white_raffards_decoction_enhanced",
                new FabricItemSettings().maxCount(2),
                TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                25,
                1,
                1
        );

        WHITE_RAFFARDS_DECOCTION_SUPERIOR = registerItemPotion("white_raffards_decoction_superior",
                new FabricItemSettings().maxCount(3),
                TCOTS_Effects.WHITE_RAFFARDS_EFFECT,
                25,
                1,
                2
        );

        KILLER_WHALE_POTION = registerItemPotion("killer_whale_potion",
                new FabricItemSettings().maxCount(3),
                TCOTS_Effects.KILLER_WHALE_EFFECT,
                15,
                90,
                0
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

    public static void registerItemsMisc(){
        NEST_SLAB_ITEM  = registerBlockItem("nest_slab", TCOTS_Blocks.NEST_SLAB);

        NEST_SKULL_ITEM = registerItem("nest_skull", new NestSkullItem(TCOTS_Blocks.NEST_SKULL, TCOTS_Blocks.NEST_WALL_SKULL, new FabricItemSettings(), Direction.DOWN));

        MONSTER_NEST_ITEM = registerItem("monster_nest", new MonsterNestItem(TCOTS_Blocks.MONSTER_NEST, new FabricItemSettings()));

        ALCHEMY_TABLE_ITEM = registerItem("alchemy_table", new AlchemyTableItem(TCOTS_Blocks.ALCHEMY_TABLE, new FabricItemSettings()));
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), item);
    }

    private static Item registerItemPotion(String name, Item.Settings settings, StatusEffect effect, int toxicity, int durationInSecs, int amplifier) {
        try {
            WitcherPotions_Base witcherPotion = new WitcherPotions_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05), amplifier), toxicity);


            return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), witcherPotion);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private static Item registerSplashPotion(String name, Item.Settings settings, StatusEffect effect, int toxicity, int durationInSecs, int amplifier){
        try {
            WitcherPotions_Base witcherPotion = new WitcherPotionsSplash_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05),amplifier), toxicity);
            return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), witcherPotion);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
