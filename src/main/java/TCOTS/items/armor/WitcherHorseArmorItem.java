package TCOTS.items.armor;

import TCOTS.TCOTS_Main;
import TCOTS.utils.MiscUtil;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitcherHorseArmorItem extends HorseArmorItem {
    private final List<MutableText> tooltip;
    private final String armorTexture;
    private final String outerTexture;
    public WitcherHorseArmorItem(int bonus, String name, Settings settings, List<MutableText> tooltip) {
        super(bonus, name, settings);

        this.armorTexture="textures/models/horse_armor/horse_armor_" + name + ".png";
        this.outerTexture="textures/models/horse_armor/horse_armor_" + name + "_outer.png";
        this.tooltip=tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(!this.tooltip.isEmpty())
            MiscUtil.setSpecialTooltip(Text.translatable("tooltip.tcots-witcher.generic_tooltip.special_abilities"), stack, tooltip, this.tooltip);
    }

    @Override
    public Identifier getEntityTexture() {
        return Identifier.of(TCOTS_Main.MOD_ID,this.armorTexture);
    }

    public Identifier getOuterTexture(){
        return Identifier.of(TCOTS_Main.MOD_ID, this.outerTexture);
    }


}
