package TCOTS.items.armor;

import TCOTS.TCOTS_Main;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.util.Identifier;

public class WitcherHorseArmorItem extends HorseArmorItem {
    private final String armorTexture;
    private final String outerTexture;
    public WitcherHorseArmorItem(int bonus, String name, Settings settings) {
        super(bonus, name, settings);

        this.armorTexture="textures/models/horse_armor/horse_armor_" + name + ".png";
        this.outerTexture="textures/models/horse_armor/horse_armor_" + name + "_outer.png";
    }

    @Override
    public Identifier getEntityTexture() {
        return new Identifier(TCOTS_Main.MOD_ID,this.armorTexture);
    }

    public Identifier getOuterTexture(){
        return new Identifier(TCOTS_Main.MOD_ID, this.outerTexture);
    }

}
