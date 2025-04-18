package TCOTS.items.weapons;

import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.MiscUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SwordWithTooltip extends SwordItem {
    private final List<MutableComponent> tooltip;
    private final MutableComponent littleDescription;

    public SwordWithTooltip(Tier toolMaterial, Properties settings, MutableComponent littleDescription, List<MutableComponent> tooltip) {
        super(toolMaterial, settings);
        this.tooltip=tooltip;

        this.littleDescription=littleDescription;
    }

    @SuppressWarnings("all")
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        if(FabricLoader.getInstance().isModLoaded("witcher_rpg")){
            if(stack.is(TCOTS_Items_Fabric.WINTERS_BLADE)){
                tooltip.add(Component.translatable("item.witcher_rpg.relic_witcher_swords.description_1").withStyle(ChatFormatting.RED));
            }
        }

        tooltip.add(this.littleDescription);

        MiscUtil.setSpecialTooltip(Component.translatable("tooltip.tcots_witcher.generic_tooltip.special_abilities"), stack, tooltip, this.tooltip);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);

        if(stack.is(TCOTS_Items_Fabric.WINTERS_BLADE)) {
            if (attacker.getRandom().nextIntBetweenInclusive(0, 5) == 1) {
                attacker.playSound(SoundEvents.GLASS_BREAK, 1.0f, 1.0f);
                if (target instanceof Player) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8 * 20, 6, false, true), attacker);
                } else if (target.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                    target.addEffect(new MobEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 2 * 20, 2, false, false), attacker);
                } else {
                    target.addEffect(new MobEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 4 * 20, 2, false, false), attacker);
                }
            }

            if(target.isOnFire()){
                target.clearFire();
            }
        }

        if(stack.is(TCOTS_Items_Fabric.DYAEBL)){
            if(attacker.getRandom().nextIntBetweenInclusive(0,5)==1){
                attacker.playSound(TCOTS_Sounds.BLACK_BLOOD_HIT, 1.0f, 1.0f);
                target.addEffect(new MobEffectInstance(TCOTS_Effects.BLEEDING, 10*20, 1, false, false, true));
            }
        }

        return result;
    }


}
