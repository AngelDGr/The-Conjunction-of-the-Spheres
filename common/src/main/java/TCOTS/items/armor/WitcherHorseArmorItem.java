package TCOTS.items.armor;

import TCOTS.TCOTS_Main;
import TCOTS.utils.MiscUtil;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

@SuppressWarnings("all")
public class WitcherHorseArmorItem extends AnimalArmorItem {
    private final List<MutableComponent> tooltip;
    private final String armorTexture;
    private final String outerTexture;
    public WitcherHorseArmorItem(Holder<ArmorMaterial> material, String name, Item.Properties settings, List<MutableComponent> tooltip) {
        super(material, AnimalArmorItem.BodyType.EQUESTRIAN, false, settings);


        this.armorTexture="textures/models/horse_armor/horse_armor_" + name + ".png";
        this.outerTexture="textures/models/horse_armor/horse_armor_" + name + "_outer.png";
        this.tooltip=tooltip;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        if(!this.tooltip.isEmpty())
            MiscUtil.setSpecialTooltip(Component.translatable("tooltip.tcots_witcher.generic_tooltip.special_abilities"), stack, tooltip, this.tooltip);
    }

    @Override
    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,this.armorTexture);
    }

    public ResourceLocation getOuterTexture(){
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, this.outerTexture);
    }


}
