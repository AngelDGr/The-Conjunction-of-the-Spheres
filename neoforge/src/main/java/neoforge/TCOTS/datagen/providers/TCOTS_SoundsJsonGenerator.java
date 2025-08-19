package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Sounds;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import java.util.List;

public class TCOTS_SoundsJsonGenerator extends SoundDefinitionsProvider {

    public TCOTS_SoundsJsonGenerator(final PackOutput output, final ExistingFileHelper helper) {
        super(output, TCOTS_Main.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {

        //Gui
        {
            addSpecificSound(TCOTS_Sounds.OIL_APPLIED, List.of(
                    "minecraft:item/honeycomb/wax_on1",
                    "minecraft:item/honeycomb/wax_on2",
                    "minecraft:item/honeycomb/wax_on3"
            ));

            addSpecificSound(TCOTS_Sounds.OIL_RAN_OUT, List.of(
                    "minecraft:item/axe/scrape1",
                    "minecraft:item/axe/scrape2",
                    "minecraft:item/axe/scrape3"
            ));

            addSoundGui(TCOTS_Sounds.POTION_REFILLED, 1);

            addSpecificSound(TCOTS_Sounds.INGREDIENT_POPS, List.of(
                    "minecraft:item/sweet_berries/pick_from_bush1",
                    "minecraft:item/sweet_berries/pick_from_bush2"
            ));

            addSoundGui(TCOTS_Sounds.BLACK_BLOOD_HIT, 3);
        }

        //Entity
        {
            //Common
            {
                addSoundEntity(TCOTS_Sounds.MONSTER_EMERGING, 2, "common");
                addSoundEntity(TCOTS_Sounds.MONSTER_DIGGING, 2, "common");
                addSoundEntity(TCOTS_Sounds.GROUND_PUNCH, 2, "common");

                addSoundEntity(TCOTS_Sounds.BIG_IMPACT, 2, "common");
                addSoundEntity(TCOTS_Sounds.MEDIUM_IMPACT, 2, "common");
            }

            //Necrophages
            {
                //Drowner
                {
                    addSoundEntity(TCOTS_Sounds.DROWNER_ATTACK, 2, "necrophages/drowner");
                    addSoundEntity(TCOTS_Sounds.DROWNER_HURT, 2, "necrophages/drowner");
                    addSoundEntity(TCOTS_Sounds.DROWNER_IDLE, 3, "necrophages/drowner");

                    addSoundEntity(TCOTS_Sounds.DROWNER_DEATH, 2, "necrophages/drowner");
                    addSoundEntity(TCOTS_Sounds.WATERY_FOOTSTEP, 5, "necrophages/drowner");
                    addSoundEntity(TCOTS_Sounds.DROWNER_LUNGE, 2,  "necrophages/drowner");
                    addSoundEntity(TCOTS_Sounds.DROWNER_EMERGING, 2,  "necrophages/drowner");
                    addSoundEntity(TCOTS_Sounds.DROWNER_DIGGING, 2,  "necrophages/drowner");
                }

                //Rotfiend
                {
                    addSoundEntity(TCOTS_Sounds.ROTFIEND_ATTACK, 3,"necrophages/rotfiend");
                    addSoundEntity(TCOTS_Sounds.ROTFIEND_HURT, 3,"necrophages/rotfiend");
                    addSoundEntity(TCOTS_Sounds.ROTFIEND_IDLE, 4,"necrophages/rotfiend");
                    addSoundEntity(TCOTS_Sounds.ROTFIEND_DEATH, 2,"necrophages/rotfiend");
                    addSoundEntity(TCOTS_Sounds.ROTFIEND_LUNGE, 3,"necrophages/rotfiend");
                    addSoundEntity(TCOTS_Sounds.ROTFIEND_EXPLODING, 3,"necrophages/rotfiend");

                    addSpecificSound(
                            "rotfiend_blood_explosion",
                            "tcots_witcher.rotfiend_blood_explosion",
                            List.of("tcots_witcher:entity/necrophages/rotfiend/rotfiend_blood_explosion1"));
                }

                //Grave Hag
                {
                    addSoundEntity(TCOTS_Sounds.GRAVE_HAG_ATTACK, 3, "necrophages/grave_hag");
                    addSoundEntity(TCOTS_Sounds.GRAVE_HAG_HURT, 4, "necrophages/grave_hag");
                    addSoundEntity(TCOTS_Sounds.GRAVE_HAG_IDLE, 4, "necrophages/grave_hag");
                    addSoundEntity(TCOTS_Sounds.GRAVE_HAG_DEATH, 3, "necrophages/grave_hag");
                    addSoundEntity(TCOTS_Sounds.GRAVE_HAG_TONGUE_ATTACK, 3, "necrophages/grave_hag");
                    addSoundEntity(TCOTS_Sounds.GRAVE_HAG_RUN, 3, "necrophages/grave_hag");
                }

                //Water Hag
                {
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_ATTACK, 3, "necrophages/water_hag");
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_HURT, 4, "necrophages/water_hag");
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_IDLE, 4, "necrophages/water_hag");
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_DEATH, 3, "necrophages/water_hag");
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_EMERGING, 2, "necrophages/water_hag");
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_DIGGING, 2, "necrophages/water_hag");
                    addSoundEntity(TCOTS_Sounds.WATER_HAG_MUD_BALL_LAUNCH, 3, "necrophages/water_hag");
                    addSpecificSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_HIT, List.of(
                            "minecraft:block/mud/step1",
                            "minecraft:block/mud/step2",
                            "minecraft:block/mud/step3",
                            "minecraft:block/mud/step4",
                            "minecraft:block/mud/step5",
                            "minecraft:block/mud/step6"
                    ));
                }

                //Foglet
                {
                    addSoundEntity(TCOTS_Sounds.FOGLET_ATTACK, 4, "necrophages/foglet");
                    addSoundEntity(TCOTS_Sounds.FOGLET_HURT, 3, "necrophages/foglet");
                    addSoundEntity(TCOTS_Sounds.FOGLET_IDLE, 4, "necrophages/foglet");
                    addSoundEntity(TCOTS_Sounds.FOGLET_DEATH, 3, "necrophages/foglet");
                    addSoundEntity(TCOTS_Sounds.FOGLET_FOG, 3, "necrophages/foglet");
                    addSoundEntity(TCOTS_Sounds.FOGLET_FOGLING_DISAPPEAR, 3, "necrophages/foglet");
                }

                //Ghoul
                {
                    addSoundEntity(TCOTS_Sounds.GHOUL_ATTACK, 3, "necrophages/ghoul");
                    addSoundEntity(TCOTS_Sounds.GHOUL_HURT, 4, "necrophages/ghoul");
                    addSoundEntity(TCOTS_Sounds.GHOUL_IDLE, 3, "necrophages/ghoul");
                    addSoundEntity(TCOTS_Sounds.GHOUL_DEATH, 3, "necrophages/ghoul");
                    addSoundEntity(TCOTS_Sounds.GHOUL_LUNGES, 3, "necrophages/ghoul");
                    addSoundEntity(TCOTS_Sounds.GHOUL_SCREAMS, 3, "necrophages/ghoul");
                    addSoundEntity(TCOTS_Sounds.GHOUL_REGEN, 1, "necrophages/ghoul");
                }

                //Alghoul
                {
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_ATTACK, 3, "necrophages/alghoul");
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_HURT, 4, "necrophages/alghoul");
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_IDLE, 3, "necrophages/alghoul");
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_DEATH, 3, "necrophages/alghoul");
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_LUNGES, 3, "necrophages/alghoul");
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_SCREAMS, 3, "necrophages/alghoul");
                    addSpecificSound(TCOTS_Sounds.ALGHOUL_REGEN, List.of(
                            "tcots_witcher:entity/necrophages/ghoul/ghoul_regen1"
                    ));
                    addSoundEntity(TCOTS_Sounds.ALGHOUL_SPIKES, 2, "necrophages/alghoul");
                }

                // Scurver
                {
                    addSpecificSound(TCOTS_Sounds.SCURVER_ATTACK, List.of(
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_attack1",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_attack2",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_attack3"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_HURT, List.of(
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_hurt1",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_hurt2",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_hurt3"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_IDLE, List.of(
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_idle1",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_idle2",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_idle3",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_idle4"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_DEATH, List.of(
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_death1",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_death2"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_LUNGE, List.of(
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_lunge1",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_lunge2",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_lunge3"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_EXPLODING, List.of(
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_exploding1",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_exploding2",
                            "tcots_witcher:entity/necrophages/rotfiend/rotfiend_exploding3"
                    ));
                }

                //Devourer
                {
                    addSoundEntity(TCOTS_Sounds.DEVOURER_ATTACK, 3, "necrophages/devourer");
                    addSoundEntity(TCOTS_Sounds.DEVOURER_HURT, 3, "necrophages/devourer");
                    addSoundEntity(TCOTS_Sounds.DEVOURER_IDLE, 3, "necrophages/devourer");
                    addSoundEntity(TCOTS_Sounds.DEVOURER_DEATH, 3, "necrophages/devourer");
                    addSoundEntity(TCOTS_Sounds.DEVOURER_JUMP, 2, "necrophages/devourer");
                }

                //Bloedzuiger
                {
                    addSoundEntity(TCOTS_Sounds.BLOEDZUIGER_ATTACK, 3, "necrophages/bloedzuiger");
                    addSoundEntity(TCOTS_Sounds.BLOEDZUIGER_HURT, 4, "necrophages/bloedzuiger");
                    addSoundEntity(TCOTS_Sounds.BLOEDZUIGER_IDLE, 4, "necrophages/bloedzuiger");
                    addSoundEntity(TCOTS_Sounds.BLOEDZUIGER_DEATH, 3, "necrophages/bloedzuiger");
                    addSoundEntity(TCOTS_Sounds.BLOEDZUIGER_EXPLOSION, 2, "necrophages/bloedzuiger");
                }

                //Graveir
                {
                    addSoundEntity(TCOTS_Sounds.GRAVEIR_ATTACK, 3, "necrophages/graveir");
                    addSoundEntity(TCOTS_Sounds.GRAVEIR_HURT, 3, "necrophages/graveir");
                    addSoundEntity(TCOTS_Sounds.GRAVEIR_IDLE, 4, "necrophages/graveir");
                    addSoundEntity(TCOTS_Sounds.GRAVEIR_DEATH, 3, "necrophages/graveir");
                    addSoundEntity(TCOTS_Sounds.GRAVEIR_GROUND_PUNCH, 3, "necrophages/graveir");
                }

                //Bullvore
                {
                    addSoundEntity(TCOTS_Sounds.BULLVORE_ATTACK, 4, "necrophages/bullvore");
                    addSoundEntity(TCOTS_Sounds.BULLVORE_HURT, 3, "necrophages/bullvore");
                    addSoundEntity(TCOTS_Sounds.BULLVORE_IDLE, 4, "necrophages/bullvore");
                    addSoundEntity(TCOTS_Sounds.BULLVORE_DEATH, 3, "necrophages/bullvore");
                    addSoundEntity(TCOTS_Sounds.BULLVORE_CHARGE, 3, "necrophages/bullvore");
                }
            }


            //Ogroids
            {
                //Nekker
                {
                    addSoundEntity(TCOTS_Sounds.NEKKER_ATTACK, 3, "ogroids/nekker");
                    addSoundEntity(TCOTS_Sounds.NEKKER_HURT, 3, "ogroids/nekker");
                    addSoundEntity(TCOTS_Sounds.NEKKER_IDLE, 4, "ogroids/nekker");
                    addSoundEntity(TCOTS_Sounds.NEKKER_DEATH, 2, "ogroids/nekker");
                    addSoundEntity(TCOTS_Sounds.NEKKER_LUNGE, 3, "ogroids/nekker");
                }

                //Nekker Warrior
                {
                    addSoundEntity(TCOTS_Sounds.NEKKER_WARRIOR_ATTACK, 3, "ogroids/nekker_warrior");
                    addSoundEntity(TCOTS_Sounds.NEKKER_WARRIOR_HURT, 3, "ogroids/nekker_warrior");
                    addSoundEntity(TCOTS_Sounds.NEKKER_WARRIOR_IDLE, 4, "ogroids/nekker_warrior");
                    addSoundEntity(TCOTS_Sounds.NEKKER_WARRIOR_DEATH, 2, "ogroids/nekker_warrior");
                    addSoundEntity(TCOTS_Sounds.NEKKER_WARRIOR_LUNGE, 3, "ogroids/nekker_warrior");
                }

                //Cyclops
                {
                    addSoundEntity(TCOTS_Sounds.CYCLOPS_ATTACK, 3, "ogroids/cyclops");
                    addSoundEntity(TCOTS_Sounds.CYCLOPS_HURT, 3, "ogroids/cyclops");
                    addSoundEntity(TCOTS_Sounds.CYCLOPS_IDLE, 3, "ogroids/cyclops");
                    addSoundEntity(TCOTS_Sounds.CYCLOPS_DEATH, 3, "ogroids/cyclops");
                    addSoundEntity(TCOTS_Sounds.CYCLOPS_PUNCH, 2, "ogroids/cyclops");
                }

                //Troll
                {
                    addSoundEntity(TCOTS_Sounds.TROLL_ATTACK, 3, "ogroids/troll");
                    addSoundEntity(TCOTS_Sounds.TROLL_HURT, 4, "ogroids/troll");
                    addSoundEntity(TCOTS_Sounds.TROLL_IDLE, 3, "ogroids/troll");
                    addSoundEntity(TCOTS_Sounds.TROLL_DEATH, 3, "ogroids/troll");
                    addSpecificSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT, List.of(
                            "tcots_witcher:entity/ogroids/troll/troll_block_impact1",
                            "tcots_witcher:entity/ogroids/troll/troll_block_impact2"
                    ));
                    addSpecificSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT_BREAK, List.of(
                            "tcots_witcher:entity/ogroids/troll/troll_block_impact1",
                            "tcots_witcher:entity/ogroids/troll/troll_block_impact2"
                    ));
                    addSoundEntity(TCOTS_Sounds.ROCK_PROJECTILE_IMPACT, 2, "ogroids/troll");
                    addSpecificSound(TCOTS_Sounds.ROCK_PROJECTILE_THROWS, List.of(
                            "minecraft:random/bow"
                    ));

                    addSoundEntity(TCOTS_Sounds.TROLL_FOLLOW, 4, "ogroids/troll_talk");
                    addSoundEntity(TCOTS_Sounds.TROLL_WAITING, 4, "ogroids/troll_talk");
                    addSoundEntity(TCOTS_Sounds.TROLL_DISMISS, 4, "ogroids/troll_talk");
                    addSoundEntity(TCOTS_Sounds.TROLL_GRUNT, 4, "ogroids/troll_talk");
                    addSoundEntity(TCOTS_Sounds.TROLL_FURIOUS, 4, "ogroids/troll_talk");
                    addSoundEntity(TCOTS_Sounds.TROLL_BARTERING, 3, "ogroids/troll_talk");
//                    addSoundEntity(TCOTS_Sounds.TROLL_LAUGH, 1, "ogroids/troll_talk");
                }

                // Ice Giant
                {
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_ATTACK, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_HURT, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_IDLE, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_DEATH, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_PUNCH, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_CHARGE, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_SNORE, 3, "ogroids/ice_giant");
                    addSoundEntity(TCOTS_Sounds.ICE_GIANT_WAKE_UP, 3, "ogroids/ice_giant");
                }
            }




        }

        //Item
        {
            addSoundItem(TCOTS_Sounds.ANCHOR_CHAIN, 3, "anchor");
            addSoundItem(TCOTS_Sounds.ANCHOR_IMPACT, 4, "anchor");
            addSoundItem(TCOTS_Sounds.ANCHOR_THROW, 3, "anchor");
        }

        addSpecificSound(TCOTS_Sounds.HERBALIST_WORKS, List.of(
                "minecraft:block/brewing_stand/brew1",
                "minecraft:block/brewing_stand/brew2"
        ));
    }

    private void addSpecificSound(final RegistrySupplier<SoundEvent> sound, final List<String> names) {
        final SoundDefinition definition = definition().subtitle("subtitles." + TCOTS_Main.MOD_ID + "." + sound.getId().getPath());

        for(final String name: names){
            definition.with(sound(name));
        }

        this.add(sound.get(), definition);
    }

    @SuppressWarnings("all")
    private void addSpecificSound(final String soundName, final String subtitle, final List<String> names) {
        final SoundDefinition definition = definition().subtitle("subtitles." +subtitle);

        for(final String name: names){
            definition.with(sound(name));
        }

        this.add(soundName, definition);
    }

    private void addSoundEntity(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount, final String subfolder) {
        addSound(sound, soundVariationAmount, "entity/", subfolder);
    }

    @SuppressWarnings("all")
    private void addSoundItem(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount, final String subfolder) {
        addSound(sound, soundVariationAmount, "item/", subfolder);
    }

    private void addSoundGui(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount) {
        final SoundDefinition definition = definition().subtitle("subtitles." + TCOTS_Main.MOD_ID + "." + sound.getId().getPath());

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(TCOTS_Main.MOD_ID + ":" + "gui/" + sound.getId().getPath() + i));

        this.add(sound.get(), definition);
    }

    private void addSound(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount, final String type, final String subfolder) {
        final SoundDefinition definition = definition().subtitle("subtitles." + TCOTS_Main.MOD_ID + "." + sound.getId().getPath());

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(TCOTS_Main.MOD_ID + ":" + type + subfolder + "/" + sound.getId().getPath() + i));

        this.add(sound.get(), definition);
    }
}
