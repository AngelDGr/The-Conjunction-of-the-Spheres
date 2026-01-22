package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.registry.TCOTS_Tags;
import mors.tcots.registry.TCOTS_Entities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TCOTS_EntityTagGenerator extends EntityTypeTagsProvider {

    public TCOTS_EntityTagGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, TCOTS_Main.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(final HolderLookup.@NotNull Provider arg) {
        this.tag(TCOTS_Tags.Entity.NECROPHAGES)
                .add(
                        TCOTS_Entities.Devourer(),
                        TCOTS_Entities.Bloedzuiger(),
                        TCOTS_Entities.GraveHag(),
                        TCOTS_Entities.Drowner(),
                        TCOTS_Entities.Ghoul(),
                        TCOTS_Entities.Alghoul(),
                        TCOTS_Entities.Foglet(),
                        TCOTS_Entities.Bullvore(),
                        TCOTS_Entities.WaterHag(),
                        TCOTS_Entities.Graveir(),
                        TCOTS_Entities.Rotfiend(),
                        TCOTS_Entities.Scurver());

        this.tag(TCOTS_Tags.Entity.OGROIDS)
                .add(
                        TCOTS_Entities.IceGiant(),
                        TCOTS_Entities.Nekker(),
                        TCOTS_Entities.NekkerWarrior(),
                        TCOTS_Entities.Cyclops(),
                        TCOTS_Entities.RockTroll(),
                        TCOTS_Entities.IceTroll(),
                        TCOTS_Entities.ForestTroll());



        this.tag(TCOTS_Tags.Entity.IGNITING_ENTITIES)
                .add(EntityType.BLAZE)
                .add(EntityType.FIREBALL)
                .add(EntityType.SMALL_FIREBALL)
                .add(EntityType.FIREWORK_ROCKET);

        this.tag(TCOTS_Tags.Entity.DIMERITIUM_REMOVAL)
                .add(EntityType.EVOKER_FANGS)
                .add(EntityType.AREA_EFFECT_CLOUD)
                .add(EntityType.SHULKER_BULLET);

        this.tag(TCOTS_Tags.Entity.DIMERITIUM_DAMAGE)
                .add(EntityType.END_CRYSTAL)
                .add(EntityType.VEX)
                .add(EntityType.ALLAY)
                .add(TCOTS_Entities.Fogling());

        this.tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(TCOTS_Entities.IceTroll());

        this.tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
                .add(TCOTS_Entities.IceTroll())
                .add(TCOTS_Entities.Cyclops())
                .add(TCOTS_Entities.IceGiant());

        this.tag(TCOTS_Tags.Entity.BOSS_TAG)
                .add(TCOTS_Entities.IceGiant());

        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(TCOTS_Entities.Drowner());

        this.tag(EntityTypeTags.SENSITIVE_TO_IMPALING)
                .add(TCOTS_Entities.Drowner());
    }
}