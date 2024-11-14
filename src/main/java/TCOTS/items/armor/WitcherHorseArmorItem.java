package TCOTS.items.armor;

import TCOTS.TCOTS_Main;
import TCOTS.utils.MiscUtil;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@SuppressWarnings("all")
public class WitcherHorseArmorItem extends AnimalArmorItem {
    private final List<MutableText> tooltip;
    private final String armorTexture;
    private final String outerTexture;
    public WitcherHorseArmorItem(RegistryEntry<ArmorMaterial> material, String name, Item.Settings settings, List<MutableText> tooltip) {
        super(material, AnimalArmorItem.Type.EQUESTRIAN, false, settings);


        this.armorTexture="textures/models/horse_armor/horse_armor_" + name + ".png";
        this.outerTexture="textures/models/horse_armor/horse_armor_" + name + "_outer.png";
        this.tooltip=tooltip;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
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
