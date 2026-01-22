package mors.tcots.utils;

import mors.tcots.registry.TCOTS_Effects;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LightLayer;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.platform.InputConstants;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class TCOTS_Util {
    @SuppressWarnings("all")
    public static float gvalchir_penetration = TCOTS_Util.isWitcherRPGLoaded()? 0.75f: 0.75f;
    public static float moonblade_bonus      = TCOTS_Util.isWitcherRPGLoaded()? 0.15f: 0.25f;

    /**
     Puts a dynamic tooltip to an item that it can be open with left alt
     @param stack The stack that it's going to have the tooltip
     @param mainTooltip The main tooltip to change
     @param bonusTooltip The tooltip that it's going to be added
     */
    public static void setSpecialTooltip(
            final ItemStack stack,
            final List<Component> mainTooltip,
            final List<MutableComponent> bonusTooltip,
            final TooltipFlag tooltipType
    ) {
        boolean mustContainExtraSpace = stack.getItem() instanceof SwordItem;


        // To not add the space if it contains "Allows Spell Casting"
        for (final Component component : mainTooltip) {
            if (component.getContents() instanceof final TranslatableContents translation) {
                if (translation.getKey().equals("spell.tooltip.host.proxy.spell")) {
                    mustContainExtraSpace = false;
                    break;
                }
            }
        }

        //Build special tooltip lines separately
        final List<Component> specialLines = new ArrayList<>();

        if (mustContainExtraSpace) {
            specialLines.add(CommonComponents.EMPTY);
        }

        if (Minecraft.getInstance() != null && (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_ALT))) {
            specialLines.addAll(bonusTooltip);
        } else {
            specialLines.add(Component.translatable(
                    "item.tcots_witcher.sword.tooltip.see_more", "Left Alt"
            ).withStyle(ChatFormatting.DARK_GRAY));
        }

        // Optional: Add empty line if enchanted and not a sword
        if (stack.isEnchanted() && !(stack.getItem() instanceof SwordItem)) {
            specialLines.add(CommonComponents.EMPTY);
        }

        // Find advanced tooltip start
        int found = 0;
        if (tooltipType.isAdvanced()) {
            final var searchedStyle = Component.literal("x")
                    .withStyle(ChatFormatting.DARK_GRAY)
                    .getStyle(); // From: ItemStack.java, advanced tooltip section

            int reverseIndex = mainTooltip.size();
            for (final var line : mainTooltip.reversed()) {
                --reverseIndex;
                final var style = line.getStyle();
                if (style != null) {
                    final boolean newFind = searchedStyle.getColor().equals(style.getColor());
                    if (found != 0 && !newFind) {
                        break;
                    } else if (newFind) {
                        found = reverseIndex;
                    }
                }
            }
        }

        // Insert Lines
        if (found <= 0) {
            mainTooltip.addAll(specialLines);
        } else {
            mainTooltip.addAll(found, specialLines);
        }
    }


    public static int getTimeInTicks(final int seconds){
        return seconds*20;
    }

    /**
    Get the enchantment level from a specific enchantment
     @param enchantment The Enchantment to check
     @param stack The stack to check
     */
    public static int getEnchantmentLevel(final ResourceKey<Enchantment> enchantment, final ItemStack stack) {
        int level = 0;
        final ItemEnchantments itemEnchantmentsComponent = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        for (final Holder<Enchantment> entry : itemEnchantmentsComponent.keySet()) {
            if (entry.is(enchantment)) {
                level = itemEnchantmentsComponent.getLevel(entry);
            }
        }

        return level;
    }

    public static boolean canHaveCatEffect(final LivingEntity player){

        final int lightBlock = player.level().getBrightness(LightLayer.BLOCK, player.blockPosition());
        final int lightSky   = player.level().getBrightness(LightLayer.SKY,   player.blockPosition());

        return player.hasEffect(TCOTS_Effects.CatEffect()) && !(player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (isNightTicks(player) && lightBlock <=4));
    }

    private static boolean isNightTicks(final LivingEntity player){
        final long time = player.level().getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }

//    /**
//     Detects if the player model is one Expressive model like the ones from <a href="https://modrinth.com/resourcepack/better-expressions">Better Expressions</a> or <a href="https://modrinth.com/resourcepack/tras-fresh-player">Fresh Moves</a>
//     */
//    public static boolean hasExpressiveModel(HumanoidModel<?> model){
//        return model.head.getAllParts().anyMatch(part->part.toString().contains("EMF_brows"))
//                && model.head.getAllParts().anyMatch(part->part.toString().contains("EMF_eyes"))
//                && model.head.getAllParts().anyMatch(part->part.toString().contains("EMF_extras"));
//    }

    /**
     Detects if the player model is one Expressive model like the ones from <a href="https://modrinth.com/resourcepack/better-expressions">Better Expressions</a> or <a href="https://modrinth.com/resourcepack/tras-fresh-player">Fresh Moves</a>
     */
    public static boolean hasExpressiveModel(final HumanoidModel<?> model){
        if(model.head.hasChild("EMF_head")){
            return model.head.getChild("EMF_head").hasChild("EMF_brows")
                    && model.head.getChild("EMF_head").hasChild("EMF_eyes")
                    && model.head.getChild("EMF_head").hasChild("EMF_extras");
        }

        return false;
    }

    public static boolean isWitcherRPGAccessories(){
        return isWitcherRPGLoaded() && (isTrinketsLoaded() || isAccessoriesLoaded());
    }

    /** In Fabric detects if <a href="https://www.curseforge.com/minecraft/mc-mods/witcher-rpg-class">Witcher (More RPG Classes)</a> is present, in NeoForge only returns false (because the mod is Fabric only)*/
    public static boolean isWitcherRPGLoaded(){
        return TCOTS_Util.isModLoaded("witcher_rpg");
    }

    public static boolean isAccessoriesLoaded(){
        return TCOTS_Util.isModLoaded("accessories");
    }

    public static boolean isTrinketsLoaded(){
        return TCOTS_Util.isModLoaded("trinkets");
    }

    @ExpectPlatform
    public static boolean isModLoaded(final String id){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isWearingAccessoryItem(final LivingEntity player, final Item medallion){
        throw new AssertionError();
    }
}
