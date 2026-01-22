package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.registry.TCOTS_Sounds;
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

            addSound(TCOTS_Sounds.POTION_REFILLED, 1);

            addSpecificSound(TCOTS_Sounds.INGREDIENT_POPS, List.of(
                    "minecraft:item/sweet_berries/pick_from_bush1",
                    "minecraft:item/sweet_berries/pick_from_bush2"
            ));

            addSound(TCOTS_Sounds.BLACK_BLOOD_HIT, 3);
        }

        //Entity
        {
            //Generic
            {
                addSound(TCOTS_Sounds.MONSTER_EMERGING, 2);
                addSound(TCOTS_Sounds.MONSTER_DIGGING, 2);
                addSound(TCOTS_Sounds.GROUND_PUNCH, 2);

                addSound(TCOTS_Sounds.BIG_IMPACT, 2);
                addSound(TCOTS_Sounds.MEDIUM_IMPACT, 2);
            }

            //Necrophage
            {
                //Drowner
                {
                    addSound(TCOTS_Sounds.DROWNER_ATTACK, 2);
                    addSound(TCOTS_Sounds.DROWNER_HURT, 2);
                    addSound(TCOTS_Sounds.DROWNER_IDLE, 3);

                    addSound(TCOTS_Sounds.DROWNER_DEATH, 2);
                    addSound(TCOTS_Sounds.WATERY_FOOTSTEP, 5);
                    addSound(TCOTS_Sounds.DROWNER_LUNGE, 2);
                    addSound(TCOTS_Sounds.WET_MONSTER_EMERGING, 2);
                    addSound(TCOTS_Sounds.WET_MONSTER_DIGGING, 2);
                }

                //Rotfiend
                {
                    addSound(TCOTS_Sounds.ROTFIEND_ATTACK, 3);
                    addSound(TCOTS_Sounds.ROTFIEND_HURT, 3);
                    addSound(TCOTS_Sounds.ROTFIEND_IDLE, 4);
                    addSound(TCOTS_Sounds.ROTFIEND_DEATH, 2);
                    addSound(TCOTS_Sounds.ROTFIEND_LUNGE, 3);
                    addSound(TCOTS_Sounds.ROTFIEND_EXPLODING, 3);

                    addSpecificSound(
                            "entity.necrophage.rotfiend.blood_explosion",
                            "tcots_witcher.entity.necrophage.rotfiend.blood_explosion",
                            List.of("tcots_witcher:entity/necrophage/rotfiend/rotfiend_blood_explosion1"));
                }

                //Grave Hag
                {
                    addSound(TCOTS_Sounds.GRAVE_HAG_ATTACK, 3);
                    addSound(TCOTS_Sounds.GRAVE_HAG_HURT, 4);
                    addSound(TCOTS_Sounds.GRAVE_HAG_IDLE, 4);
                    addSound(TCOTS_Sounds.GRAVE_HAG_DEATH, 3);
                    addSound(TCOTS_Sounds.GRAVE_HAG_TONGUE_ATTACK, 3);
                    addSound(TCOTS_Sounds.GRAVE_HAG_RUN, 3);
                }

                //Water Hag
                {
                    addSound(TCOTS_Sounds.WATER_HAG_ATTACK, 3);
                    addSound(TCOTS_Sounds.WATER_HAG_HURT, 4);
                    addSound(TCOTS_Sounds.WATER_HAG_IDLE, 4);
                    addSound(TCOTS_Sounds.WATER_HAG_DEATH, 3);
                    addSound(TCOTS_Sounds.WATER_HAG_EMERGING, 2);
                    addSound(TCOTS_Sounds.WATER_HAG_DIGGING, 2);
                    addSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_LAUNCH, 3);
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
                    addSound(TCOTS_Sounds.FOGLET_ATTACK, 4);
                    addSound(TCOTS_Sounds.FOGLET_HURT, 3);
                    addSound(TCOTS_Sounds.FOGLET_IDLE, 4);
                    addSound(TCOTS_Sounds.FOGLET_DEATH, 3);
                    addSound(TCOTS_Sounds.FOGLET_FOG, 3);
                    addSound(TCOTS_Sounds.FOGLET_FOGLING_DISAPPEAR, 3);
                }

                //Ghoul
                {
                    addSound(TCOTS_Sounds.GHOUL_ATTACK, 3);
                    addSound(TCOTS_Sounds.GHOUL_HURT, 4);
                    addSound(TCOTS_Sounds.GHOUL_IDLE, 3);
                    addSound(TCOTS_Sounds.GHOUL_DEATH, 3);
                    addSound(TCOTS_Sounds.GHOUL_LUNGES, 3);
                    addSound(TCOTS_Sounds.GHOUL_SCREAMS, 3);
                    addSound(TCOTS_Sounds.GHOUL_REGEN, 1);
                }

                //Alghoul
                {
                    addSound(TCOTS_Sounds.ALGHOUL_ATTACK, 3);
                    addSound(TCOTS_Sounds.ALGHOUL_HURT, 4);
                    addSound(TCOTS_Sounds.ALGHOUL_IDLE, 3);
                    addSound(TCOTS_Sounds.ALGHOUL_DEATH, 3);
                    addSound(TCOTS_Sounds.ALGHOUL_LUNGES, 3);
                    addSound(TCOTS_Sounds.ALGHOUL_SCREAMS, 3);
                    addSpecificSound(TCOTS_Sounds.ALGHOUL_REGEN, List.of(
                            "tcots_witcher:entity/necrophage/ghoul/ghoul_regen1"
                    ));
                    addSound(TCOTS_Sounds.ALGHOUL_SPIKES, 2);
                }

                // Scurver
                {
                    addSpecificSound(TCOTS_Sounds.SCURVER_ATTACK, List.of(
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_attack1",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_attack2",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_attack3"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_HURT, List.of(
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_hurt1",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_hurt2",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_hurt3"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_IDLE, List.of(
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_idle1",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_idle2",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_idle3",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_idle4"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_DEATH, List.of(
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_death1",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_death2"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_LUNGE, List.of(
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_lunge1",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_lunge2",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_lunge3"
                    ));
                    addSpecificSound(TCOTS_Sounds.SCURVER_EXPLODING, List.of(
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_exploding1",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_exploding2",
                            "tcots_witcher:entity/necrophage/rotfiend/rotfiend_exploding3"
                    ));
                }

                //Devourer
                {
                    addSound(TCOTS_Sounds.DEVOURER_ATTACK, 3);
                    addSound(TCOTS_Sounds.DEVOURER_HURT, 3);
                    addSound(TCOTS_Sounds.DEVOURER_IDLE, 3);
                    addSound(TCOTS_Sounds.DEVOURER_DEATH, 3);
                    addSound(TCOTS_Sounds.DEVOURER_JUMP, 2);
                }

                //Bloedzuiger
                {
                    addSound(TCOTS_Sounds.BLOEDZUIGER_ATTACK, 3);
                    addSound(TCOTS_Sounds.BLOEDZUIGER_HURT, 4);
                    addSound(TCOTS_Sounds.BLOEDZUIGER_IDLE, 4);
                    addSound(TCOTS_Sounds.BLOEDZUIGER_DEATH, 3);
                    addSound(TCOTS_Sounds.BLOEDZUIGER_EXPLOSION, 2);
                }

                //Graveir
                {
                    addSound(TCOTS_Sounds.GRAVEIR_ATTACK, 3);
                    addSound(TCOTS_Sounds.GRAVEIR_HURT, 3);
                    addSound(TCOTS_Sounds.GRAVEIR_IDLE, 4);
                    addSound(TCOTS_Sounds.GRAVEIR_DEATH, 3);
                    addSound(TCOTS_Sounds.GRAVEIR_GROUND_PUNCH, 3);
                }

                //Bullvore
                {
                    addSound(TCOTS_Sounds.BULLVORE_ATTACK, 4);
                    addSound(TCOTS_Sounds.BULLVORE_HURT, 3);
                    addSound(TCOTS_Sounds.BULLVORE_IDLE, 4);
                    addSound(TCOTS_Sounds.BULLVORE_DEATH, 3);
                    addSound(TCOTS_Sounds.BULLVORE_CHARGE, 3);
                }
            }

            //Ogroid
            {
                //Nekker
                {
                    addSound(TCOTS_Sounds.NEKKER_ATTACK, 3);
                    addSound(TCOTS_Sounds.NEKKER_HURT, 3);
                    addSound(TCOTS_Sounds.NEKKER_IDLE, 4);
                    addSound(TCOTS_Sounds.NEKKER_DEATH, 2);
                    addSound(TCOTS_Sounds.NEKKER_LUNGE, 3);
                }

                //Nekker Warrior
                {
                    addSound(TCOTS_Sounds.NEKKER_WARRIOR_ATTACK, 3);
                    addSound(TCOTS_Sounds.NEKKER_WARRIOR_HURT, 3);
                    addSound(TCOTS_Sounds.NEKKER_WARRIOR_IDLE, 4);
                    addSound(TCOTS_Sounds.NEKKER_WARRIOR_DEATH, 2);
                    addSound(TCOTS_Sounds.NEKKER_WARRIOR_LUNGE, 3);
                }

                //Cyclops
                {
                    addSound(TCOTS_Sounds.CYCLOPS_ATTACK, 3);
                    addSound(TCOTS_Sounds.CYCLOPS_HURT, 3);
                    addSound(TCOTS_Sounds.CYCLOPS_IDLE, 3);
                    addSound(TCOTS_Sounds.CYCLOPS_DEATH, 3);
                    addSound(TCOTS_Sounds.CYCLOPS_PUNCH, 2);
                }

                //Troll
                {
                    addSound(TCOTS_Sounds.TROLL_ATTACK, 3);
                    addSound(TCOTS_Sounds.TROLL_HURT, 4);
                    addSound(TCOTS_Sounds.TROLL_IDLE, 3);
                    addSound(TCOTS_Sounds.TROLL_DEATH, 3);
                    addSpecificSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT, List.of(
                            "tcots_witcher:entity/ogroid/troll/troll_block_impact1",
                            "tcots_witcher:entity/ogroid/troll/troll_block_impact2"
                    ));
                    addSpecificSound(TCOTS_Sounds.TROLL_BLOCK_IMPACT_BREAK, List.of(
                            "tcots_witcher:entity/ogroid/troll/troll_block_impact1",
                            "tcots_witcher:entity/ogroid/troll/troll_block_impact2"
                    ));
                    addSound(TCOTS_Sounds.ROCK_PROJECTILE_IMPACT, 2);
                    addSpecificSound(TCOTS_Sounds.ROCK_PROJECTILE_THROWS, List.of(
                            "minecraft:random/bow"
                    ));

                    addSound(TCOTS_Sounds.TROLL_BARTER, 3);
                    addSound(TCOTS_Sounds.TROLL_FOLLOW, 4);
                    addSound(TCOTS_Sounds.TROLL_WAITING, 4);
                    addSound(TCOTS_Sounds.TROLL_DISMISS, 4);
                    addSound(TCOTS_Sounds.TROLL_GRUNT, 4);
                    addSound(TCOTS_Sounds.TROLL_FURIOUS, 4);

//                    addSound(TCOTS_Sounds.TROLL_LAUGH, 1);
                }

                // Ice Giant
                {
                    addSound(TCOTS_Sounds.ICE_GIANT_ATTACK, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_HURT, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_IDLE, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_DEATH, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_PUNCH, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_CHARGE, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_SNORE, 3);
                    addSound(TCOTS_Sounds.ICE_GIANT_WAKE_UP, 3);
                }
            }
        }

        //Item
        {
            addSound(TCOTS_Sounds.ANCHOR_CHAIN, 3);
            addSound(TCOTS_Sounds.ANCHOR_IMPACT, 4);
            addSound(TCOTS_Sounds.ANCHOR_THROW, 3);
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

    private void addSpecificSound(final String soundName, final String subtitle, final List<String> names) {
        final SoundDefinition definition = definition().subtitle("subtitles." +subtitle);

        for(final String name: names){
            definition.with(sound(name));
        }

        this.add(soundName, definition);
    }

//    private void addSound(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount, final String subfolder) {
//        addSound(sound, soundVariationAmount, "entity/", subfolder);
//    }
//
//    @SuppressWarnings("all")
//    private void addSoundItem(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount, final String subfolder) {
//        addSound(sound, soundVariationAmount, "item/", subfolder);
//    }

//    private void addSoundGui(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount) {
//        final SoundDefinition definition = definition().subtitle("subtitles." + TCOTS_Main.MOD_ID + "." + sound.getId().getPath());
//
//        for (int i = 1; i <= soundVariationAmount; i++)
//            definition.with(sound(TCOTS_Main.MOD_ID + ":" + "gui/" + sound.getId().getPath() + i));
//
//        this.add(sound.get(), definition);
//    }

//    private void addSound(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount, final String type, final String subfolder) {
//        final SoundDefinition definition = definition().subtitle("subtitles." + TCOTS_Main.MOD_ID + "." + sound.getId().getPath());
//
//        final String[] soundPathSplitted= sound.getId().getPath().split("\\.");
//
//        final String mobType  = soundPathSplitted[1];
//        final String soundType= soundPathSplitted[2];
//
//        final String finalPath=mobType+"_"+soundType;
//
//        for (int i = 1; i <= soundVariationAmount; i++)
//            definition.with(sound(TCOTS_Main.MOD_ID + ":" + type + subfolder + "/" + finalPath + i));
//
//        this.add(sound.get(), definition);
//    }

    private void addSound(final RegistrySupplier<SoundEvent> sound, final int soundVariationAmount) {
        final SoundDefinition definition = definition().subtitle("subtitles." + TCOTS_Main.MOD_ID + "." + sound.getId().getPath());

        final String[] soundPathSplitted= sound.getId().getPath().split("\\.");

        final String mainFolder  = soundPathSplitted[0]+"/";
        final StringBuilder rest= new StringBuilder();

        //This converts from [].[]. to []/[]/
        for (int i=1; i<soundPathSplitted.length; i++){
            if(i==1)
                rest.append(soundPathSplitted[i]);
            else if(mainFolder.equals("entity/") && i==(soundPathSplitted.length-1) && !soundPathSplitted[i - 1].equals("generic"))
                rest.append("/").append(soundPathSplitted[i - 1]).append("_").append(soundPathSplitted[i]);
            else
                rest.append("/").append(soundPathSplitted[i]);
        }

        for (int i = 1; i <= soundVariationAmount; i++)
            definition.with(sound(TCOTS_Main.MOD_ID + ":" + mainFolder + rest + i));

        this.add(sound.get(), definition);
    }
}
