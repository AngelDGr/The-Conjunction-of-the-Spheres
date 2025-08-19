package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Tags;
import TCOTS.registry.TCOTS_Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TCOTS_ItemTagGenerator extends ItemTagsProvider {



    public TCOTS_ItemTagGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, final CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, TCOTS_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {
        this.tag(TCOTS_Tags.DECAYING_FLESH)
                .add(Items.BEEF)
                .add(Items.PORKCHOP)
                .add(Items.MUTTON)
                .add(Items.CHICKEN)
                .add(Items.RABBIT);

        this.tag(TCOTS_Tags.MONSTER_BLOOD)
                .add(TCOTS_Items.ROTFIEND_BLOOD.get())
                .add(TCOTS_Items.GHOUL_BLOOD.get())
                .add(TCOTS_Items.BLOEDZUIGER_BLOOD.get());


        this.tag(ItemTags.FREEZE_IMMUNE_WEARABLES)
                .add(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get(), TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get(), TCOTS_Items.WARRIORS_LEATHER_JACKET.get())

                .add(TCOTS_Items.MANTICORE_BOOTS.get(), TCOTS_Items.MANTICORE_TROUSERS.get(), TCOTS_Items.MANTICORE_ARMOR.get())

                .add(TCOTS_Items.RAVENS_BOOTS.get(), TCOTS_Items.RAVENS_TROUSERS.get(), TCOTS_Items.RAVENS_ARMOR.get())

                .add(TCOTS_Items.TUNDRA_HORSE_ARMOR.get());

        this.tag(ItemTags.DYEABLE)
                .add(TCOTS_Items.KNIGHT_CROSSBOW.get());


        //Tags for Enchanting
        this.tag(ItemTags.SWORDS)
                .add(TCOTS_Items.GVALCHIR.get())
                .add(TCOTS_Items.MOONBLADE.get())
                .add(TCOTS_Items.DYAEBL.get())
                .add(TCOTS_Items.WINTERS_BLADE.get())
                .add(TCOTS_Items.ARDAENYE.get());

        this.tag(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(TCOTS_Items.KNIGHT_CROSSBOW.get());

        this.tag(ItemTags.CHEST_ARMOR)
                .add(TCOTS_Items.WARRIORS_LEATHER_JACKET.get(), TCOTS_Items.RAVENS_ARMOR.get(), TCOTS_Items.MANTICORE_ARMOR.get());

        this.tag(ItemTags.LEG_ARMOR)
                .add(TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get(), TCOTS_Items.RAVENS_TROUSERS.get(), TCOTS_Items.MANTICORE_TROUSERS.get());

        this.tag(ItemTags.FOOT_ARMOR)
                .add(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get(), TCOTS_Items.RAVENS_BOOTS.get(), TCOTS_Items.MANTICORE_BOOTS.get());

        this.tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(TCOTS_Items.GIANT_ANCHOR.get());

        this.tag(ItemTags.FLOWERS)
                .add(TCOTS_Items.ARENARIA.get())
                .add(TCOTS_Items.CELANDINE.get())
                .add(TCOTS_Items.BRYONIA.get())
                .add(TCOTS_Items.VERBENA.get())
                .add(TCOTS_Items.HAN_FIBER.get());

        //Extra tags for Witcher (More RPG Classes)
        this.tag(TCOTS_Tags.HERBS)
                .add(TCOTS_Items.ALLSPICE.get())
                .add(TCOTS_Items.ERGOT_SEEDS.get())

                .add(TCOTS_Items.ARENARIA.get())
                .add(TCOTS_Items.BRYONIA.get())
                .add(TCOTS_Items.CELANDINE.get())
                .add(TCOTS_Items.CROWS_EYE.get())
                .add(TCOTS_Items.HAN_FIBER.get())
                .add(TCOTS_Items.PUFFBALL.get())
                .add(TCOTS_Items.SEWANT_MUSHROOMS.get())
                .add(TCOTS_Items.VERBENA.get());

        this.tag(TCOTS_Tags.LOOT_COMMON)
                .add(TCOTS_Items.DROWNER_TONGUE.get())
                .add(TCOTS_Items.ROTFIEND_BLOOD.get())
                .add(TCOTS_Items.WATER_HAG_MUD_BALL.get())
                .add(TCOTS_Items.WATER_ESSENCE.get())
                .add(TCOTS_Items.FOGLET_TEETH.get())
                .add(TCOTS_Items.GHOUL_BLOOD.get())
                .add(TCOTS_Items.DEVOURER_TEETH.get())
                .add(TCOTS_Items.BLOEDZUIGER_BLOOD.get())
                .add(TCOTS_Items.CADAVERINE.get())
                .add(TCOTS_Items.NEKKER_EYE.get());

        this.tag(TCOTS_Tags.LOOT_UNCOMMON)
                .add(TCOTS_Items.DROWNER_BRAIN.get())
                .add(TCOTS_Items.SCURVER_SPINE.get())
                .add(TCOTS_Items.GRAVEIR_BONE.get())
                .add(TCOTS_Items.NEKKER_HEART.get());

        this.tag(TCOTS_Tags.LOOT_RARE)
                .add(TCOTS_Items.ALGHOUL_BONE_MARROW.get())
                .add(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get());

        this.tag(TCOTS_Tags.MUTAGENS)
                .add(TCOTS_Items.FOGLET_MUTAGEN.get())
                .add(TCOTS_Items.GRAVE_HAG_MUTAGEN.get())
                .add(TCOTS_Items.WATER_HAG_MUTAGEN.get())

                .add(TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get())
                .add(TCOTS_Items.TROLL_MUTAGEN.get());

    }
}