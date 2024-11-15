package TCOTS.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Modmenu(modId = "tcots-witcher")
@Config(name = "tcots_config", wrapperName = "TCOTS_Config")
public class TCOTS_OwOConfig {

    @Nest
    public NestedMonsterConfig monsters = new NestedMonsterConfig();

    public static class NestedMonsterConfig{
        public List<String> Necrophages = new ArrayList<>(List.of());
        public List<String> Ogroids = new ArrayList<>(List.of());
        public List<String> Beasts = new ArrayList<>(List.of());
        public List<String> Humanoids = new ArrayList<>(List.of());

        //Upcoming types
        public List<String> Specters = new ArrayList<>(List.of());
        public List<String> Vampires = new ArrayList<>(List.of());
        public List<String> Insectoids = new ArrayList<>(List.of());
        public List<String> Elementa = new ArrayList<>(List.of());
        public List<String> Hybrids = new ArrayList<>(List.of());
        public List<String> Cursed_Ones = new ArrayList<>(List.of());
        public List<String> Draconids = new ArrayList<>(List.of());
        public List<String> Relicts = new ArrayList<>(List.of());
    }

    @Nest
    public NestedHudConfig hud = new NestedHudConfig();

    @Sync(Option.SyncMode.INFORM_SERVER)
    public static class NestedHudConfig {
        @Sync(Option.SyncMode.INFORM_SERVER)
        public ANCHORS anchor = ANCHORS.LEFT_DOWN;
        @Sync(Option.SyncMode.INFORM_SERVER)
        public int Hud_Y=0;
        @Sync(Option.SyncMode.INFORM_SERVER)
        public int Hud_X=0;
    }

    @Nest
    public NestedWitcherEyesConfig witcher_eyes = new NestedWitcherEyesConfig();


    public static class NestedWitcherEyesConfig{

        @Sync(Option.SyncMode.INFORM_SERVER)
        @Hook
        public boolean activate=false;

        @Sync(Option.SyncMode.INFORM_SERVER)
        @Hook
        public EYE_SHAPE eyeShape = EYE_SHAPE.NORMAL;
        @Sync(Option.SyncMode.INFORM_SERVER)
        @Hook
        public EYE_SEPARATION eyeSeparation = EYE_SEPARATION.TWO;


        @Sync(Option.SyncMode.INFORM_SERVER)
        @Hook
        public int XEyePos=0;
        @Sync(Option.SyncMode.INFORM_SERVER)
        @Hook
        public int YEyePos=0;
    }

    public enum ANCHORS {
        CENTER_DOWN, CENTER_UP, RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP
    }

    public enum EYE_SHAPE {
        NORMAL, TALL, LONG, TALL_SHADOW, BIG
    }

    public enum EYE_SEPARATION {
        ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX
    }
}
