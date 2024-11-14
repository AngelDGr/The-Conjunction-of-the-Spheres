package TCOTS.items.weapons;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.MiscUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public class SwordWithTooltip extends SwordItem {
    private final List<MutableText> tooltip;
    private final MutableText littleDescription;

    public SwordWithTooltip(ToolMaterial toolMaterial, Settings settings, MutableText littleDescription, List<MutableText> tooltip) {
        super(toolMaterial, settings);
        this.tooltip=tooltip;

        this.littleDescription=littleDescription;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(this.littleDescription);

        MiscUtil.setSpecialTooltip(Text.translatable("tooltip.tcots-witcher.generic_tooltip.special_abilities"), stack, tooltip, this.tooltip);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.postHit(stack, target, attacker);

        if(stack.isOf(TCOTS_Items.WINTERS_BLADE)) {
            if (attacker.getRandom().nextBetween(0, 5) == 1) {
                attacker.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0f, 1.0f);
                if (target instanceof PlayerEntity) {
                    target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 8 * 20, 6, false, true), attacker);
                } else if (target.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                    target.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 2 * 20, 2, false, false), attacker);
                } else {
                    target.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.NORTHERN_WIND_EFFECT, 4 * 20, 2, false, false), attacker);
                }
            }

            if(target.isOnFire()){
                target.extinguish();
            }
        }

        if(stack.isOf(TCOTS_Items.DYAEBL)){
            if(attacker.getRandom().nextBetween(0,5)==1){
                attacker.playSound(TCOTS_Sounds.BLACK_BLOOD_HIT, 1.0f, 1.0f);
                target.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.BLEEDING, 10*20, 1, false, false, true));
            }
        }

        return result;
    }
}
