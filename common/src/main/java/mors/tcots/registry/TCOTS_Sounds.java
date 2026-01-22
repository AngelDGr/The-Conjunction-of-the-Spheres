package mors.tcots.registry;

import mors.tcots.TCOTS_Main;
import mors.tcots.TCOTS_Registries;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class TCOTS_Sounds {

    public static RegistrySupplier<SoundEvent> MONSTER_EMERGING = registerSoundEvent("entity.generic.monster_emerging");
    public static RegistrySupplier<SoundEvent> MONSTER_DIGGING = registerSoundEvent("entity.generic.monster_digging");
    public static RegistrySupplier<SoundEvent> WET_MONSTER_EMERGING = registerSoundEvent("entity.generic.wet_monster_emerging");
    public static RegistrySupplier<SoundEvent> WET_MONSTER_DIGGING = registerSoundEvent("entity.generic.wet_monster_digging");
    public static RegistrySupplier<SoundEvent> GROUND_PUNCH = registerSoundEvent("entity.generic.ground_punch");
    public static RegistrySupplier<SoundEvent> BIG_IMPACT = registerSoundEvent("entity.generic.big_impact");
    public static RegistrySupplier<SoundEvent> MEDIUM_IMPACT = registerSoundEvent("entity.generic.medium_impact");

    //Drowner
    public static RegistrySupplier<SoundEvent> DROWNER_ATTACK = registerSoundEvent("entity.necrophage.drowner.attack");
    public static RegistrySupplier<SoundEvent> DROWNER_HURT = registerSoundEvent("entity.necrophage.drowner.hurt");
    public static RegistrySupplier<SoundEvent> DROWNER_IDLE = registerSoundEvent("entity.necrophage.drowner.idle");
    public static RegistrySupplier<SoundEvent> DROWNER_DEATH = registerSoundEvent("entity.necrophage.drowner.death");
    public static RegistrySupplier<SoundEvent> WATERY_FOOTSTEP = registerSoundEvent("entity.necrophage.drowner.footstep");
    public static RegistrySupplier<SoundEvent> DROWNER_LUNGE = registerSoundEvent("entity.necrophage.drowner.lunge");


    //Rotfiend
    public static RegistrySupplier<SoundEvent> ROTFIEND_ATTACK = registerSoundEvent("entity.necrophage.rotfiend.attack");
    public static RegistrySupplier<SoundEvent> ROTFIEND_HURT = registerSoundEvent("entity.necrophage.rotfiend.hurt");
    public static RegistrySupplier<SoundEvent> ROTFIEND_IDLE = registerSoundEvent("entity.necrophage.rotfiend.idle");
    public static RegistrySupplier<SoundEvent> ROTFIEND_DEATH = registerSoundEvent("entity.necrophage.rotfiend.death");
    public static RegistrySupplier<SoundEvent> ROTFIEND_LUNGE = registerSoundEvent("entity.necrophage.rotfiend.lunge");
    public static RegistrySupplier<SoundEvent> ROTFIEND_EXPLODING = registerSoundEvent("entity.necrophage.rotfiend.exploding");


    //Grave Hag
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_ATTACK = registerSoundEvent("entity.necrophage.grave_hag.attack");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_HURT = registerSoundEvent("entity.necrophage.grave_hag.hurt");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_IDLE = registerSoundEvent("entity.necrophage.grave_hag.idle");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_DEATH = registerSoundEvent("entity.necrophage.grave_hag.death");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_RUN = registerSoundEvent("entity.necrophage.grave_hag.run");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_TONGUE_ATTACK = registerSoundEvent("entity.necrophage.grave_hag.tongue_attack");

    //Water Hag
    public static RegistrySupplier<SoundEvent> WATER_HAG_ATTACK = registerSoundEvent("entity.necrophage.water_hag.attack");
    public static RegistrySupplier<SoundEvent> WATER_HAG_HURT = registerSoundEvent("entity.necrophage.water_hag.hurt");
    public static RegistrySupplier<SoundEvent> WATER_HAG_IDLE = registerSoundEvent("entity.necrophage.water_hag.idle");
    public static RegistrySupplier<SoundEvent> WATER_HAG_DEATH = registerSoundEvent("entity.necrophage.water_hag.death");
    public static RegistrySupplier<SoundEvent> WATER_HAG_EMERGING = registerSoundEvent("entity.necrophage.water_hag.emerging");
    public static RegistrySupplier<SoundEvent> WATER_HAG_DIGGING = registerSoundEvent("entity.necrophage.water_hag.digging");
    public static RegistrySupplier<SoundEvent> WATER_HAG_MUD_BALL_LAUNCH = registerSoundEvent("entity.necrophage.water_hag.mud_ball_launch");
    public static RegistrySupplier<SoundEvent> WATER_HAG_MUD_BALL_HIT = registerSoundEvent("entity.necrophage.water_hag.mud_ball_hit");

    //Foglet
    public static RegistrySupplier<SoundEvent> FOGLET_ATTACK = registerSoundEvent("entity.necrophage.foglet.attack");
    public static RegistrySupplier<SoundEvent> FOGLET_HURT = registerSoundEvent("entity.necrophage.foglet.hurt");
    public static RegistrySupplier<SoundEvent> FOGLET_IDLE = registerSoundEvent("entity.necrophage.foglet.idle");
    public static RegistrySupplier<SoundEvent> FOGLET_DEATH = registerSoundEvent("entity.necrophage.foglet.death");
    public static RegistrySupplier<SoundEvent> FOGLET_FOG = registerSoundEvent("entity.necrophage.foglet.fog");
    public static RegistrySupplier<SoundEvent> FOGLET_FOGLING_DISAPPEAR = registerSoundEvent("entity.necrophage.foglet.fogling_disappear");

    //Ghoul
    public static RegistrySupplier<SoundEvent> GHOUL_ATTACK = registerSoundEvent("entity.necrophage.ghoul.attack");
    public static RegistrySupplier<SoundEvent> GHOUL_HURT = registerSoundEvent("entity.necrophage.ghoul.hurt");
    public static RegistrySupplier<SoundEvent> GHOUL_IDLE = registerSoundEvent("entity.necrophage.ghoul.idle");
    public static RegistrySupplier<SoundEvent> GHOUL_DEATH = registerSoundEvent("entity.necrophage.ghoul.death");
    public static RegistrySupplier<SoundEvent> GHOUL_LUNGES = registerSoundEvent("entity.necrophage.ghoul.lunge");
    public static RegistrySupplier<SoundEvent> GHOUL_SCREAMS = registerSoundEvent("entity.necrophage.ghoul.scream");
    public static RegistrySupplier<SoundEvent> GHOUL_REGEN = registerSoundEvent("entity.necrophage.ghoul.regen");

    //Alghoul
    public static RegistrySupplier<SoundEvent> ALGHOUL_ATTACK = registerSoundEvent("entity.necrophage.alghoul.attack");
    public static RegistrySupplier<SoundEvent> ALGHOUL_HURT = registerSoundEvent("entity.necrophage.alghoul.hurt");
    public static RegistrySupplier<SoundEvent> ALGHOUL_IDLE = registerSoundEvent("entity.necrophage.alghoul.idle");
    public static RegistrySupplier<SoundEvent> ALGHOUL_DEATH = registerSoundEvent("entity.necrophage.alghoul.death");
    public static RegistrySupplier<SoundEvent> ALGHOUL_LUNGES = registerSoundEvent("entity.necrophage.alghoul.lunge");
    public static RegistrySupplier<SoundEvent> ALGHOUL_SCREAMS = registerSoundEvent("entity.necrophage.alghoul.scream");
    public static RegistrySupplier<SoundEvent> ALGHOUL_REGEN = registerSoundEvent("entity.necrophage.alghoul.regen");
    public static RegistrySupplier<SoundEvent> ALGHOUL_SPIKES = registerSoundEvent("entity.necrophage.alghoul.spikes");

    //Scurver
    public static RegistrySupplier<SoundEvent> SCURVER_ATTACK = registerSoundEvent("entity.necrophage.scurver.attack");
    public static RegistrySupplier<SoundEvent> SCURVER_HURT = registerSoundEvent("entity.necrophage.scurver.hurt");
    public static RegistrySupplier<SoundEvent> SCURVER_IDLE = registerSoundEvent("entity.necrophage.scurver.idle");
    public static RegistrySupplier<SoundEvent> SCURVER_DEATH = registerSoundEvent("entity.necrophage.scurver.death");
    public static RegistrySupplier<SoundEvent> SCURVER_LUNGE = registerSoundEvent("entity.necrophage.scurver.lunge");
    public static RegistrySupplier<SoundEvent> SCURVER_EXPLODING = registerSoundEvent("entity.necrophage.scurver.exploding");

    //Devourer
    public static RegistrySupplier<SoundEvent> DEVOURER_ATTACK = registerSoundEvent("entity.necrophage.devourer.attack");
    public static RegistrySupplier<SoundEvent> DEVOURER_HURT = registerSoundEvent("entity.necrophage.devourer.hurt");
    public static RegistrySupplier<SoundEvent> DEVOURER_IDLE = registerSoundEvent("entity.necrophage.devourer.idle");
    public static RegistrySupplier<SoundEvent> DEVOURER_DEATH = registerSoundEvent("entity.necrophage.devourer.death");
    public static RegistrySupplier<SoundEvent> DEVOURER_JUMP = registerSoundEvent("entity.necrophage.devourer.jump");

    //Bloedzuiger
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_ATTACK = registerSoundEvent("entity.necrophage.bloedzuiger.attack");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_HURT = registerSoundEvent("entity.necrophage.bloedzuiger.hurt");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_IDLE = registerSoundEvent("entity.necrophage.bloedzuiger.idle");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_DEATH = registerSoundEvent("entity.necrophage.bloedzuiger.death");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_EXPLOSION = registerSoundEvent("entity.necrophage.bloedzuiger.exploding");

    //Graveir
    public static RegistrySupplier<SoundEvent> GRAVEIR_ATTACK = registerSoundEvent("entity.necrophage.graveir.attack");
    public static RegistrySupplier<SoundEvent> GRAVEIR_HURT = registerSoundEvent("entity.necrophage.graveir.hurt");
    public static RegistrySupplier<SoundEvent> GRAVEIR_IDLE = registerSoundEvent("entity.necrophage.graveir.idle");
    public static RegistrySupplier<SoundEvent> GRAVEIR_DEATH = registerSoundEvent("entity.necrophage.graveir.death");
    public static RegistrySupplier<SoundEvent> GRAVEIR_GROUND_PUNCH = registerSoundEvent("entity.necrophage.graveir.ground_punch");

    //Bullvore
    public static RegistrySupplier<SoundEvent> BULLVORE_ATTACK = registerSoundEvent("entity.necrophage.bullvore.attack");
    public static RegistrySupplier<SoundEvent> BULLVORE_HURT = registerSoundEvent("entity.necrophage.bullvore.hurt");
    public static RegistrySupplier<SoundEvent> BULLVORE_IDLE = registerSoundEvent("entity.necrophage.bullvore.idle");
    public static RegistrySupplier<SoundEvent> BULLVORE_DEATH = registerSoundEvent("entity.necrophage.bullvore.death");
    public static RegistrySupplier<SoundEvent> BULLVORE_CHARGE = registerSoundEvent("entity.necrophage.bullvore.charge");

    //Nekker
    public static RegistrySupplier<SoundEvent> NEKKER_ATTACK = registerSoundEvent("entity.ogroid.nekker.attack");
    public static RegistrySupplier<SoundEvent> NEKKER_HURT = registerSoundEvent("entity.ogroid.nekker.hurt");
    public static RegistrySupplier<SoundEvent> NEKKER_IDLE = registerSoundEvent("entity.ogroid.nekker.idle");
    public static RegistrySupplier<SoundEvent> NEKKER_DEATH = registerSoundEvent("entity.ogroid.nekker.death");
    public static RegistrySupplier<SoundEvent> NEKKER_LUNGE = registerSoundEvent("entity.ogroid.nekker.lunge");

    //Nekker Warrior
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_ATTACK = registerSoundEvent("entity.ogroid.nekker_warrior.attack");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_HURT = registerSoundEvent("entity.ogroid.nekker_warrior.hurt");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_IDLE = registerSoundEvent("entity.ogroid.nekker_warrior.idle");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_DEATH = registerSoundEvent("entity.ogroid.nekker_warrior.death");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_LUNGE = registerSoundEvent("entity.ogroid.nekker_warrior.lunge");

    //Cyclops
    public static RegistrySupplier<SoundEvent> CYCLOPS_ATTACK = registerSoundEvent("entity.ogroid.cyclops.attack");
    public static RegistrySupplier<SoundEvent> CYCLOPS_HURT = registerSoundEvent("entity.ogroid.cyclops.hurt");
    public static RegistrySupplier<SoundEvent> CYCLOPS_IDLE = registerSoundEvent("entity.ogroid.cyclops.idle");
    public static RegistrySupplier<SoundEvent> CYCLOPS_DEATH = registerSoundEvent("entity.ogroid.cyclops.death");
    public static RegistrySupplier<SoundEvent> CYCLOPS_PUNCH = registerSoundEvent("entity.ogroid.cyclops.punch");

    //Troll (Rock/Ice)
    public static RegistrySupplier<SoundEvent> TROLL_ATTACK = registerSoundEvent("entity.ogroid.troll.attack");
    public static RegistrySupplier<SoundEvent> TROLL_HURT = registerSoundEvent("entity.ogroid.troll.hurt");
    public static RegistrySupplier<SoundEvent> TROLL_IDLE = registerSoundEvent("entity.ogroid.troll.idle");
    public static RegistrySupplier<SoundEvent> TROLL_DEATH = registerSoundEvent("entity.ogroid.troll.death");
    public static RegistrySupplier<SoundEvent> TROLL_BLOCK_IMPACT = registerSoundEvent("entity.ogroid.troll.block_impact");
    public static RegistrySupplier<SoundEvent> TROLL_BLOCK_IMPACT_BREAK = registerSoundEvent("entity.ogroid.troll.block_impact_break");
    public static RegistrySupplier<SoundEvent> ROCK_PROJECTILE_IMPACT = registerSoundEvent("entity.ogroid.troll.rock_projectile_impact");
    public static RegistrySupplier<SoundEvent> ROCK_PROJECTILE_THROWS = registerSoundEvent("entity.ogroid.troll.rock_projectile_throws");
    public static RegistrySupplier<SoundEvent> TROLL_BARTER = registerSoundEvent("entity.ogroid.troll.barter");
    public static RegistrySupplier<SoundEvent> TROLL_FOLLOW = registerSoundEvent("entity.ogroid.troll.follow");
    public static RegistrySupplier<SoundEvent> TROLL_WAITING = registerSoundEvent("entity.ogroid.troll.waiting");
    public static RegistrySupplier<SoundEvent> TROLL_DISMISS = registerSoundEvent("entity.ogroid.troll.dismiss");
    public static RegistrySupplier<SoundEvent> TROLL_FURIOUS = registerSoundEvent("entity.ogroid.troll.furious");
    public static RegistrySupplier<SoundEvent> TROLL_GRUNT = registerSoundEvent("entity.ogroid.troll.grunt");

    //Ice Giant
    public static RegistrySupplier<SoundEvent> ICE_GIANT_ATTACK = registerSoundEvent("entity.ogroid.ice_giant.attack");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_HURT = registerSoundEvent("entity.ogroid.ice_giant.hurt");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_IDLE = registerSoundEvent("entity.ogroid.ice_giant.idle");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_DEATH = registerSoundEvent("entity.ogroid.ice_giant.death");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_PUNCH = registerSoundEvent("entity.ogroid.ice_giant.punch");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_CHARGE = registerSoundEvent("entity.ogroid.ice_giant.charge");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_SNORE = registerSoundEvent("entity.ogroid.ice_giant.snore");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_WAKE_UP = registerSoundEvent("entity.ogroid.ice_giant.wake_up");

    public static RegistrySupplier<SoundEvent> ANCHOR_CHAIN = registerSoundEvent("item.anchor.anchor_chain");
    public static RegistrySupplier<SoundEvent> ANCHOR_IMPACT = registerSoundEvent("item.anchor.anchor_impact");
    public static RegistrySupplier<SoundEvent> ANCHOR_THROW = registerSoundEvent("item.anchor.anchor_throw");

    //Misc
    public static RegistrySupplier<SoundEvent> POTION_REFILLED = registerSoundEvent("gui.alchemy.potion_refill");
    public static RegistrySupplier<SoundEvent> OIL_APPLIED = registerSoundEvent("gui.alchemy.oil_applied");
    public static RegistrySupplier<SoundEvent> OIL_RAN_OUT = registerSoundEvent("gui.alchemy.oil_ran_out");
    public static RegistrySupplier<SoundEvent> BLACK_BLOOD_HIT = registerSoundEvent("gui.alchemy.black_blood_hit");
    public static RegistrySupplier<SoundEvent> INGREDIENT_POPS = registerSoundEvent("block.generic.ingredient_pops");
    public static RegistrySupplier<SoundEvent> HERBALIST_WORKS = registerSoundEvent("entity.villager.work_herbalist");

    public static SoundEvent getSoundEvent(final String soundName) {
        return switch (soundName) {
            case "monster_emerging" -> MONSTER_EMERGING.get();
            case "monster_digging" -> MONSTER_DIGGING.get();
            case "ground_punch" -> GROUND_PUNCH.get();
            case "big_impact" -> BIG_IMPACT.get();
            case "medium_impact" -> MEDIUM_IMPACT.get();

            // Drowner Sounds
            case "drowner_attack" -> DROWNER_ATTACK.get();
            case "drowner_hurt" -> DROWNER_HURT.get();
            case "drowner_idle" -> DROWNER_IDLE.get();
            case "drowner_death" -> DROWNER_DEATH.get();
            case "drowner_footstep" -> WATERY_FOOTSTEP.get();
            case "drowner_lunge" -> DROWNER_LUNGE.get();
            case "drowner_emerging" -> WET_MONSTER_EMERGING.get();
            case "drowner_digging" -> WET_MONSTER_DIGGING.get();

            // Rotfiend Sounds
            case "rotfiend_attack" -> ROTFIEND_ATTACK.get();
            case "rotfiend_hurt" -> ROTFIEND_HURT.get();
            case "rotfiend_idle" -> ROTFIEND_IDLE.get();
            case "rotfiend_death" -> ROTFIEND_DEATH.get();
            case "rotfiend_lunge" -> ROTFIEND_LUNGE.get();
            case "rotfiend_exploding" -> ROTFIEND_EXPLODING.get();

            // Grave Hag Sounds
            case "grave_hag_attack" -> GRAVE_HAG_ATTACK.get();
            case "grave_hag_hurt" -> GRAVE_HAG_HURT.get();
            case "grave_hag_idle" -> GRAVE_HAG_IDLE.get();
            case "grave_hag_death" -> GRAVE_HAG_DEATH.get();
            case "grave_hag_run" -> GRAVE_HAG_RUN.get();
            case "grave_hag_tongue_attack" -> GRAVE_HAG_TONGUE_ATTACK.get();

            // Water Hag Sounds
            case "water_hag_attack" -> WATER_HAG_ATTACK.get();
            case "water_hag_hurt" -> WATER_HAG_HURT.get();
            case "water_hag_idle" -> WATER_HAG_IDLE.get();
            case "water_hag_death" -> WATER_HAG_DEATH.get();
            case "water_hag_emerging" -> WATER_HAG_EMERGING.get();
            case "water_hag_digging" -> WATER_HAG_DIGGING.get();
            case "water_hag_mud_ball_launch" -> WATER_HAG_MUD_BALL_LAUNCH.get();
            case "water_hag_mud_ball_hit" -> WATER_HAG_MUD_BALL_HIT.get();

            // Foglet Sounds
            case "foglet_attack" -> FOGLET_ATTACK.get();
            case "foglet_hurt" -> FOGLET_HURT.get();
            case "foglet_idle" -> FOGLET_IDLE.get();
            case "foglet_death" -> FOGLET_DEATH.get();
            case "foglet_fog" -> FOGLET_FOG.get();
            case "fogling_disappear" -> FOGLET_FOGLING_DISAPPEAR.get();

            // Ghoul Sounds
            case "ghoul_attack" -> GHOUL_ATTACK.get();
            case "ghoul_hurt" -> GHOUL_HURT.get();
            case "ghoul_idle" -> GHOUL_IDLE.get();
            case "ghoul_death" -> GHOUL_DEATH.get();
            case "ghoul_lunge" -> GHOUL_LUNGES.get();
            case "ghoul_scream" -> GHOUL_SCREAMS.get();
            case "ghoul_regen" -> GHOUL_REGEN.get();

            // Alghoul Sounds
            case "alghoul_attack" -> ALGHOUL_ATTACK.get();
            case "alghoul_hurt" -> ALGHOUL_HURT.get();
            case "alghoul_idle" -> ALGHOUL_IDLE.get();
            case "alghoul_death" -> ALGHOUL_DEATH.get();
            case "alghoul_lunge" -> ALGHOUL_LUNGES.get();
            case "alghoul_scream" -> ALGHOUL_SCREAMS.get();
            case "alghoul_regen" -> ALGHOUL_REGEN.get();
            case "alghoul_spikes" -> ALGHOUL_SPIKES.get();

            // Scurver Sounds
            case "scurver_attack" -> SCURVER_ATTACK.get();
            case "scurver_hurt" -> SCURVER_HURT.get();
            case "scurver_idle" -> SCURVER_IDLE.get();
            case "scurver_death" -> SCURVER_DEATH.get();
            case "scurver_lunge" -> SCURVER_LUNGE.get();
            case "scurver_exploding" -> SCURVER_EXPLODING.get();

            // Devourer Sounds
            case "devourer_attack" -> DEVOURER_ATTACK.get();
            case "devourer_hurt" -> DEVOURER_HURT.get();
            case "devourer_idle" -> DEVOURER_IDLE.get();
            case "devourer_death" -> DEVOURER_DEATH.get();
            case "devourer_jump" -> DEVOURER_JUMP.get();

            // Bloedzuiger Sounds
            case "bloedzuiger_attack" -> BLOEDZUIGER_ATTACK.get();
            case "bloedzuiger_hurt" -> BLOEDZUIGER_HURT.get();
            case "bloedzuiger_idle" -> BLOEDZUIGER_IDLE.get();
            case "bloedzuiger_death" -> BLOEDZUIGER_DEATH.get();
            case "bloedzuiger_exploding" -> BLOEDZUIGER_EXPLOSION.get();

            // Graveir Sounds
            case "graveir_attack" -> GRAVEIR_ATTACK.get();
            case "graveir_hurt" -> GRAVEIR_HURT.get();
            case "graveir_idle" -> GRAVEIR_IDLE.get();
            case "graveir_death" -> GRAVEIR_DEATH.get();
            case "graveir_ground_punch" -> GRAVEIR_GROUND_PUNCH.get();

            // Bullvore Sounds
            case "bullvore_attack" -> BULLVORE_ATTACK.get();
            case "bullvore_hurt" -> BULLVORE_HURT.get();
            case "bullvore_idle" -> BULLVORE_IDLE.get();
            case "bullvore_death" -> BULLVORE_DEATH.get();
            case "bullvore_charge" -> BULLVORE_CHARGE.get();

            // Nekker Sounds
            case "nekker_attack" -> NEKKER_ATTACK.get();
            case "nekker_hurt" -> NEKKER_HURT.get();
            case "nekker_idle" -> NEKKER_IDLE.get();
            case "nekker_death" -> NEKKER_DEATH.get();
            case "nekker_lunge" -> NEKKER_LUNGE.get();

            // Nekker Warrior Sounds
            case "nekker_warrior_attack" -> NEKKER_WARRIOR_ATTACK.get();
            case "nekker_warrior_hurt" -> NEKKER_WARRIOR_HURT.get();
            case "nekker_warrior_idle" -> NEKKER_WARRIOR_IDLE.get();
            case "nekker_warrior_death" -> NEKKER_WARRIOR_DEATH.get();
            case "nekker_warrior_lunge" -> NEKKER_WARRIOR_LUNGE.get();

            // Cyclops Sounds
            case "cyclops_attack" -> CYCLOPS_ATTACK.get();
            case "cyclops_hurt" -> CYCLOPS_HURT.get();
            case "cyclops_idle" -> CYCLOPS_IDLE.get();
            case "cyclops_death" -> CYCLOPS_DEATH.get();
            case "cyclops_punch" -> CYCLOPS_PUNCH.get();

            // Troll Sounds
            case "troll_attack" -> TROLL_ATTACK.get();
            case "troll_hurt" -> TROLL_HURT.get();
            case "troll_idle" -> TROLL_IDLE.get();
            case "troll_death" -> TROLL_DEATH.get();
            case "troll_block_impact" -> TROLL_BLOCK_IMPACT.get();
            case "troll_block_impact_break" -> TROLL_BLOCK_IMPACT_BREAK.get();
            case "rock_projectile_impact" -> ROCK_PROJECTILE_IMPACT.get();
            case "rock_projectile_throws" -> ROCK_PROJECTILE_THROWS.get();
            case "troll_follow" -> TROLL_FOLLOW.get();
            case "troll_waiting" -> TROLL_WAITING.get();
            case "troll_dismiss" -> TROLL_DISMISS.get();
            case "troll_furious" -> TROLL_FURIOUS.get();
            case "troll_grunt" -> TROLL_GRUNT.get();
            case "troll_bartering" -> TROLL_BARTER.get();

            // Ice Giant Sounds
            case "ice_giant_attack" -> ICE_GIANT_ATTACK.get();
            case "ice_giant_hurt" -> ICE_GIANT_HURT.get();
            case "ice_giant_idle" -> ICE_GIANT_IDLE.get();
            case "ice_giant_death" -> ICE_GIANT_DEATH.get();
            case "ice_giant_punch" -> ICE_GIANT_PUNCH.get();
            case "ice_giant_charge" -> ICE_GIANT_CHARGE.get();
            case "ice_giant_snore" -> ICE_GIANT_SNORE.get();
            case "ice_giant_wake_up" -> ICE_GIANT_WAKE_UP.get();
            case "anchor_chain" -> ANCHOR_CHAIN.get();
            case "anchor_impact" -> ANCHOR_IMPACT.get();
            case "anchor_throw" -> ANCHOR_THROW.get();

            // Miscellaneous Sounds
            case "potion_refill" -> POTION_REFILLED.get();
            case "oil_applied" -> OIL_APPLIED.get();
            case "oil_ran_out" -> OIL_RAN_OUT.get();
            case "ingredient_pops" -> INGREDIENT_POPS.get();
            case "black_blood_hit" -> BLACK_BLOOD_HIT.get();
            case "work_herbalist" -> HERBALIST_WORKS.get();
            default -> null;
        };
    }

    private static RegistrySupplier<SoundEvent> registerSoundEvent(final String name) {
        return TCOTS_Registries.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name)));
    }

    @ExpectPlatform
    public static void initSounds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Holder.Reference<SoundEvent> getRotfiendBloodExplosion(){
        throw new AssertionError();
    }
}
