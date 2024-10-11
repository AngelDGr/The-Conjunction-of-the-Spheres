package TCOTS.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;

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

    public static class NestedHudConfig {
        public ANCHORS anchor = ANCHORS.LEFT_UP;
        public int Hud_Y=0;
        public int Hud_X=0;

    }

    public enum ANCHORS {
        CENTER_DOWN, CENTER_UP, RIGHT_DOWN, RIGHT_UP, LEFT_DOWN, LEFT_UP
    }
}
