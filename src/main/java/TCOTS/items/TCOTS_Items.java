package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.potions.DwarvenSpiritItem;
import TCOTS.potions.TCOTS_Effects;
import TCOTS.potions.WitcherPotionsSplash_Base;
import TCOTS.potions.WitcherPotions_Base;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Items {

    //TODO: Add use to the items
        //xTODO: Drowner Tongue usable for water breathing potion
        //xTODO: Drowner Brain usable for a new potion, Swallow:
            //TODO: Add the Swallow effect, works like regeneration, it's a lot slower but the potion it's more durable

    public static Item DROWNER_SPAWN_EGG;
    public static Item DROWNER_TONGUE;
    public static Item DROWNER_BRAIN;
    //Potions
    public static Item DWARVEN_SPIRIT;
    public static Item SWALLOW_POTION;
    public static Item SWALLOW_POTION_ENHANCED;
    public static Item SWALLOW_POTION_SUPERIOR;
    public static Item KILLER_WHALE_POTION;
    //Splash Potions
    public static Item SWALLOW_SPLASH;
    public static Item KILLER_WHALE_SPLASH;

    //Register Witcher Potion Items
    public static void registerPotions() {
        //Potions
        DWARVEN_SPIRIT = registerItem("dwarven_spirit",
                new DwarvenSpiritItem(new FabricItemSettings().maxCount(16), new StatusEffectInstance(StatusEffects.NAUSEA,200)));


        SWALLOW_POTION = registerItemPotion("swallow_potion",
                new FabricItemSettings().maxCount(3),
                TCOTS_Effects.SWALLOW_EFFECT,
                20,
                0
        );

        SWALLOW_POTION_ENHANCED = registerItemPotion("swallow_potion_enhanced",
                new FabricItemSettings().maxCount(4),
                TCOTS_Effects.SWALLOW_EFFECT,
                20,
                1
        );

        SWALLOW_POTION_SUPERIOR = registerItemPotion("swallow_potion_superior",
                new FabricItemSettings().maxCount(5),
                TCOTS_Effects.SWALLOW_EFFECT,
                20,
                2
        );


        KILLER_WHALE_POTION = registerItemPotion("killer_whale_potion",
                new FabricItemSettings().maxCount(3),
                TCOTS_Effects.KILLER_WHALE_EFFECT,
                90,
                0
        );





        //Splash Potions
        SWALLOW_SPLASH = registerSplashPotion("swallow_splash",
                new FabricItemSettings().maxCount(1),
                TCOTS_Effects.SWALLOW_EFFECT,
                18,
                0
        );

        KILLER_WHALE_SPLASH = registerSplashPotion("killer_whale_splash",
                new FabricItemSettings().maxCount(1),
                TCOTS_Effects.KILLER_WHALE_EFFECT,
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
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), item);
    }

    private static Item registerItemPotion(String name, Item.Settings settings, StatusEffect effect, int durationInSecs, int amplifier) {
        try {
            WitcherPotions_Base witcherPotion = new WitcherPotions_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05), amplifier));


            return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), witcherPotion);
        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }
    }

    private static Item registerSplashPotion(String name, Item.Settings settings, StatusEffect effect, int durationInSecs, int amplifier){
        try {
            WitcherPotions_Base witcherPotion = new WitcherPotionsSplash_Base(settings, new StatusEffectInstance(effect, (int)(durationInSecs/0.05),amplifier));
            return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), witcherPotion);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
