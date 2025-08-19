package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_DamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class TCOTS_DamageTypeTagsGenerator extends TagsProvider<DamageType> {

    public TCOTS_DamageTypeTagsGenerator(final PackOutput arg, final CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable final ExistingFileHelper existingFileHelper) {
        super(arg, Registries.DAMAGE_TYPE, completableFuture, TCOTS_Main.MOD_ID, existingFileHelper);
    }

    @Override
        protected void addTags(final HolderLookup.@NotNull Provider lookup) {
            this.tag(DamageTypeTags.BYPASSES_ARMOR)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.ALWAYS_KILLS_ARMOR_STANDS)
                    .add(TCOTS_DamageTypes.ANCHOR);

            this.tag(DamageTypeTags.IS_PROJECTILE)
                    .add(TCOTS_DamageTypes.ANCHOR);


            this.tag(DamageTypeTags.NO_KNOCKBACK)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.AVOIDS_GUARDIAN_THORNS)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.ALWAYS_TRIGGERS_SILVERFISH)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);

            this.tag(DamageTypeTags.PANIC_CAUSES)
                    .add(TCOTS_DamageTypes.POTION_TOXICITY)
                    .add(TCOTS_DamageTypes.BLEEDING)
                    .add(TCOTS_DamageTypes.CADAVERINE);
        }

    }