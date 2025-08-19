package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.registry.TCOTS_Blocks;
import TCOTS.registry.TCOTS_Items;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class TCOTS_ItemModelGenerator extends ItemModelProvider {

    public TCOTS_ItemModelGenerator(final PackOutput output, final ExistingFileHelper existingFileHelper) {
        super(output, TCOTS_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simpleBlockItem(TCOTS_Blocks.ALCHEMY_TABLE.get());
        this.simpleBlockItem(TCOTS_Blocks.HERBAL_TABLE.get());
        this.simpleBlockItem(TCOTS_Blocks.MONSTER_NEST.get());
        this.simpleBlockItem(TCOTS_Blocks.NEST_SKULL.get());
        this.simpleBlockItem(TCOTS_Blocks.NEST_SLAB.get());
        this.simpleBlockItem(TCOTS_Blocks.SKELETON_BLOCK.get());
        this.simpleBlockItem(TCOTS_Blocks.WINTERS_BLADE_SKELETON.get());
        this.complexBlock(TCOTS_Blocks.SEWANT_MUSHROOM_BLOCK.get());
        this.complexBlock(TCOTS_Blocks.SEWANT_MUSHROOM_STEM.get());
        this.complexBlock(TCOTS_Blocks.PUFFBALL_MUSHROOM_BLOCK.get());
        
        final ResourceLocation id = Objects.requireNonNull(TCOTS_Registries.ITEMS.getRegistrar().getId(TCOTS_Items.GIANT_ANCHOR_BLOCK_ITEM.get()));
        withExistingParent(id.toString(), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/giant_anchor"));


        //Monster Drops
        {
            monsterDrop(TCOTS_Items.DROWNER_TONGUE.get());
            monsterDrop(TCOTS_Items.DROWNER_BRAIN.get());
            monsterDrop(TCOTS_Items.ROTFIEND_BLOOD.get());
            monsterDrop(TCOTS_Items.WATER_ESSENCE.get());
            monsterDrop(TCOTS_Items.FOGLET_TEETH.get());
            monsterDrop(TCOTS_Items.GHOUL_BLOOD.get());
            monsterDrop(TCOTS_Items.ALGHOUL_BONE_MARROW.get());
            monsterDrop(TCOTS_Items.SCURVER_SPINE.get());
            monsterDrop(TCOTS_Items.DEVOURER_TEETH.get());
            monsterDrop(TCOTS_Items.BLOEDZUIGER_BLOOD.get());
            monsterDrop(TCOTS_Items.CADAVERINE.get());
            monsterDrop(TCOTS_Items.GRAVEIR_BONE.get());
            monsterDrop(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get());
            monsterDrop(TCOTS_Items.NEKKER_HEART.get());
            monsterDrop(TCOTS_Items.NEKKER_EYE.get());
            monsterDrop(TCOTS_Items.CAVE_TROLL_LIVER.get());
        }

        //Weapons
        {
            weapon(TCOTS_Items.GVALCHIR.get(), true);
            weapon(TCOTS_Items.MOONBLADE.get(), true);
            weapon(TCOTS_Items.WINTERS_BLADE.get(), true);
            weapon(TCOTS_Items.ARDAENYE.get(), true);
            weapon(TCOTS_Items.DYAEBL.get(), true);
        }

        //Armor
        {
            armor(TCOTS_Items.WARRIORS_LEATHER_JACKET.get());
            armor(TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get());
            armor(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get());

            armor(TCOTS_Items.MANTICORE_ARMOR.get());
            armor(TCOTS_Items.MANTICORE_TROUSERS.get());
            armor(TCOTS_Items.MANTICORE_BOOTS.get());

            armor(TCOTS_Items.RAVENS_ARMOR.get());
            armor(TCOTS_Items.RAVENS_TROUSERS.get());
            armor(TCOTS_Items.RAVENS_BOOTS.get());

            armor(TCOTS_Items.TUNDRA_HORSE_ARMOR.get());
            armor(TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR.get());
        }

        //Alchemy
        {
            //Potions
            {
                //Normal
                {
                    potion(TCOTS_Items.EMPTY_WITCHER_POTION.get());

                    potion(TCOTS_Items.WOLF_POTION.get());
                    potion(TCOTS_Items.WOLF_POTION_ENHANCED.get());
                    potion(TCOTS_Items.WOLF_POTION_SUPERIOR.get());

                    potion(TCOTS_Items.BINDWEED_POTION.get());
                    potion(TCOTS_Items.BINDWEED_POTION_ENHANCED.get());
                    potion(TCOTS_Items.BINDWEED_POTION_SUPERIOR.get());

                    potion(TCOTS_Items.ROOK_POTION.get());
                    potion(TCOTS_Items.ROOK_POTION_ENHANCED.get());
                    potion(TCOTS_Items.ROOK_POTION_SUPERIOR.get());

                    potion(TCOTS_Items.SWALLOW_POTION.get());
                    potion(TCOTS_Items.SWALLOW_POTION_ENHANCED.get());
                    potion(TCOTS_Items.SWALLOW_POTION_SUPERIOR.get());
                    potion(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get());
                    potion(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get());
                    potion(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.get());
                    potion(TCOTS_Items.CAT_POTION.get());
                    potion(TCOTS_Items.CAT_POTION_ENHANCED.get());
                    potion(TCOTS_Items.CAT_POTION_SUPERIOR.get());
                    potion(TCOTS_Items.BLACK_BLOOD_POTION.get());
                    potion(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get());
                    potion(TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.get());
                    potion(TCOTS_Items.MARIBOR_FOREST_POTION.get());
                    potion(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get());
                    potion(TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.get());
                    potion(TCOTS_Items.KILLER_WHALE_POTION.get());
                    potion(TCOTS_Items.WHITE_HONEY_POTION.get());
                    potion(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get());
                    potion(TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.get());

                    potion(TCOTS_Items.SWALLOW_SPLASH.get());
                    potion(TCOTS_Items.KILLER_WHALE_SPLASH.get());
                    potion(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.get());
                }

                //Decoctions
                {
                    decoction(TCOTS_Items.EMPTY_MONSTER_DECOCTION.get());
                    decoction(TCOTS_Items.GRAVE_HAG_DECOCTION.get());
                    decoction(TCOTS_Items.WATER_HAG_DECOCTION.get());
                    decoction(TCOTS_Items.FOGLET_DECOCTION.get());
                    decoction(TCOTS_Items.ALGHOUL_DECOCTION.get());
                    decoction(TCOTS_Items.NEKKER_WARRIOR_DECOCTION.get());
                    decoction(TCOTS_Items.TROLL_DECOCTION.get());
                }
            }

            //Oils
            {
                oil(TCOTS_Items.NECROPHAGE_OIL.get());
                oil(TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get());
                oil(TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get());
                oil(TCOTS_Items.OGROID_OIL.get());
                oil(TCOTS_Items.ENHANCED_OGROID_OIL.get());
                oil(TCOTS_Items.SUPERIOR_OGROID_OIL.get());
                oil(TCOTS_Items.BEAST_OIL.get());
                oil(TCOTS_Items.ENHANCED_BEAST_OIL.get());
                oil(TCOTS_Items.SUPERIOR_BEAST_OIL.get());
                oil(TCOTS_Items.HANGED_OIL.get());
                oil(TCOTS_Items.ENHANCED_HANGED_OIL.get());
                oil(TCOTS_Items.SUPERIOR_HANGED_OIL.get());
                oil(TCOTS_Items.EMPTY_OIL.get());
            }

            //Bombs
            {
                bomb(TCOTS_Items.EMPTY_BOMB_POWDER.get());
                bomb(TCOTS_Items.GRAPESHOT.get());
                bomb(TCOTS_Items.GRAPESHOT_ENHANCED.get());
                bomb(TCOTS_Items.GRAPESHOT_SUPERIOR.get());
                bomb(TCOTS_Items.DANCING_STAR.get());
                bomb(TCOTS_Items.DANCING_STAR_ENHANCED.get());
                bomb(TCOTS_Items.DANCING_STAR_SUPERIOR.get());
                bomb(TCOTS_Items.DEVILS_PUFFBALL.get());
                bomb(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get());
                bomb(TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR.get());
                bomb(TCOTS_Items.SAMUM.get());
                bomb(TCOTS_Items.SAMUM_ENHANCED.get());
                bomb(TCOTS_Items.SAMUM_SUPERIOR.get());
                bomb(TCOTS_Items.NORTHERN_WIND.get());
                bomb(TCOTS_Items.NORTHERN_WIND_ENHANCED.get());
                bomb(TCOTS_Items.NORTHERN_WIND_SUPERIOR.get());
                bomb(TCOTS_Items.DRAGONS_DREAM.get());
                bomb(TCOTS_Items.DRAGONS_DREAM_ENHANCED.get());
                bomb(TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get());
                bomb(TCOTS_Items.DIMERITIUM_BOMB.get());
                bomb(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get());
                bomb(TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR.get());
                bomb(TCOTS_Items.MOON_DUST.get());
                bomb(TCOTS_Items.MOON_DUST_ENHANCED.get());
                bomb(TCOTS_Items.MOON_DUST_SUPERIOR.get());
            }

            //Ingredients
            {
                //Substances
                {
                    ingredient(TCOTS_Items.AETHER.get());
                    ingredient(TCOTS_Items.VITRIOL.get());
                    ingredient(TCOTS_Items.VERMILION.get());
                    ingredient(TCOTS_Items.HYDRAGENUM.get());
                    ingredient(TCOTS_Items.QUEBRITH.get());
                    ingredient(TCOTS_Items.RUBEDO.get());
                    ingredient(TCOTS_Items.REBIS.get());
                    ingredient(TCOTS_Items.NIGREDO.get());
                    ingredient(TCOTS_Items.STAMMELFORDS_DUST.get());
                    ingredient(TCOTS_Items.ALCHEMISTS_POWDER.get());
                    ingredient(TCOTS_Items.ALCHEMY_PASTE.get());
                    ingredient(TCOTS_Items.MONSTER_FAT.get());
                }
                //Alcohol
                {
                    ingredient(TCOTS_Items.ICY_SPIRIT.get());
                    ingredient(TCOTS_Items.DWARVEN_SPIRIT.get());
                    ingredient(TCOTS_Items.ALCOHEST.get());
                    ingredient(TCOTS_Items.WHITE_GULL.get());
                    ingredient(TCOTS_Items.VILLAGE_HERBAL.get());
                    ingredient(TCOTS_Items.CHERRY_CORDIAL.get());
                    ingredient(TCOTS_Items.MANDRAKE_CORDIAL.get());
                }

                //Herbs
                {
                    ingredient(TCOTS_Items.ALLSPICE.get());
                    ingredient(TCOTS_Items.ARENARIA.get());
                    ingredient(TCOTS_Items.CELANDINE.get());
                    ingredient(TCOTS_Items.BRYONIA.get());
                    ingredient(TCOTS_Items.CROWS_EYE.get());
                    ingredient(TCOTS_Items.VERBENA.get());
                    ingredient(TCOTS_Items.HAN_FIBER.get());
                    ingredient(TCOTS_Items.PUFFBALL.get());
                    ingredient(TCOTS_Items.SEWANT_MUSHROOMS.get());
                    ingredient(TCOTS_Items.ERGOT_SEEDS.get());
                }

                //Mutagens
                {
                    ingredient(TCOTS_Items.GRAVE_HAG_MUTAGEN.get());
                    ingredient(TCOTS_Items.WATER_HAG_MUTAGEN.get());
                    ingredient(TCOTS_Items.FOGLET_MUTAGEN.get());
                    ingredient(TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get());
                    ingredient(TCOTS_Items.TROLL_MUTAGEN.get());
                }
            }
        }

        //Spawn Eggs
        {
            eggItem(TCOTS_Items.DROWNER_SPAWN_EGG.get());
            eggItem(TCOTS_Items.ROTFIEND_SPAWN_EGG.get());
            eggItem(TCOTS_Items.GRAVE_HAG_SPAWN_EGG.get());
            eggItem(TCOTS_Items.WATER_HAG_SPAWN_EGG.get());
            eggItem(TCOTS_Items.FOGLET_SPAWN_EGG.get());
            eggItem(TCOTS_Items.GHOUL_SPAWN_EGG.get());
            eggItem(TCOTS_Items.ALGHOUL_SPAWN_EGG.get());
            eggItem(TCOTS_Items.SCURVER_SPAWN_EGG.get());
            eggItem(TCOTS_Items.DEVOURER_SPAWN_EGG.get());
            eggItem(TCOTS_Items.BLOEDZUIGER_SPAWN_EGG.get());
            eggItem(TCOTS_Items.GRAVEIR_SPAWN_EGG.get());
            eggItem(TCOTS_Items.BULLVORE_SPAWN_EGG.get());

            eggItem(TCOTS_Items.NEKKER_SPAWN_EGG.get());
            eggItem(TCOTS_Items.NEKKER_WARRIOR_SPAWN_EGG.get());
            eggItem(TCOTS_Items.CYCLOPS_SPAWN_EGG.get());
            eggItem(TCOTS_Items.ROCK_TROLL_SPAWN_EGG.get());
            eggItem(TCOTS_Items.ICE_TROLL_SPAWN_EGG.get());
            eggItem(TCOTS_Items.FOREST_TROLL_SPAWN_EGG.get());
            eggItem(TCOTS_Items.ICE_GIANT_SPAWN_EGG.get());
        }

        //Misc & Bolts
        {
            basicItem(TCOTS_Items.ALCHEMY_BOOK.get());
            basicItem(TCOTS_Items.ALCHEMY_FORMULA.get());
            basicItem(TCOTS_Items.CURED_MONSTER_LEATHER.get());
            basicItem(TCOTS_Items.HERBAL_MIXTURE.get());
            basicItem(TCOTS_Items.WATER_HAG_MUD_BALL.get());
            basicItem(TCOTS_Items.WITCHER_BESTIARY.get());

            basicItem(TCOTS_Items.BASE_BOLT.get());
            basicItem(TCOTS_Items.BLUNT_BOLT.get());
            basicItem(TCOTS_Items.PRECISION_BOLT.get());
            basicItem(TCOTS_Items.EXPLODING_BOLT.get());
            basicItem(TCOTS_Items.BROADHEAD_BOLT.get());
        }
    }

    public void complexBlock(final Block block) {
        final ResourceLocation id = Objects.requireNonNull(TCOTS_Registries.BLOCKS.getRegistrar().getId(block));

        withExistingParent(id.toString(), ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "block/" + id.getPath() +"_inventory"));
    }

    protected void eggItem(final Item eggItem) {
        getBuilder(
                Objects.requireNonNull(TCOTS_Registries.ITEMS.getRegistrar().getId(eggItem)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    public void monsterDrop(final Item item) {
        final ResourceLocation id = Objects.requireNonNull(TCOTS_Registries.ITEMS.getRegistrar().getId(item));

        getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/monster_drop/" + id.getPath()));
    }

    public void weapon(final Item item, final boolean hasRPGVariant) {
        final ResourceLocation id = Objects.requireNonNull(TCOTS_Registries.ITEMS.getRegistrar().getId(item));

        getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/handheld"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/weapon/" + id.getPath()));

        if(hasRPGVariant){
            getBuilder(id +"_rpg")
                    .parent(new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("witcher_rpg", "item/witcher_sword_model")))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/weapon/" + id.getPath() +"_rpg"));
        }
    }

    public void armor(final Item item) {
        final ResourceLocation id = Objects.requireNonNull(TCOTS_Registries.ITEMS.getRegistrar().getId(item));

        getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/armor/" + id.getPath()));
    }

    public void alchemy(final Item item, final String subfolder) {
        final ResourceLocation id = Objects.requireNonNull(TCOTS_Registries.ITEMS.getRegistrar().getId(item));

        getBuilder(id.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "item/alchemy/"+subfolder+"/" + id.getPath()));
    }

    public void ingredient(final Item item) {
        alchemy(item, "ingredient");
    }

    public void oil(final Item item) {
        alchemy(item, "oil");
    }

    public void bomb(final Item item) {
        alchemy(item, "bomb");
    }

    public void potion(final Item item) {
        alchemy(item, "potion");
    }

    public void decoction(final Item item) {
        alchemy(item, "potion/decoction");
    }
}