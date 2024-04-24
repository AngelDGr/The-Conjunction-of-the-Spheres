package TCOTS.config;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.Nest;

@SuppressWarnings("unused")
@Modmenu(modId = "tcots-witcher")
@Config(name = "tcots_config", wrapperName = "TCOTS_Config")
public class TCOTS_OwOConfig {

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
