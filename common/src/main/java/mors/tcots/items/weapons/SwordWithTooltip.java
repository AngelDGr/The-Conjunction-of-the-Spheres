package mors.tcots.items.weapons;

import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.registry.TCOTS_Items;
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
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SwordWithTooltip extends SwordItem {
    public final List<MutableComponent> tooltip;

    public SwordWithTooltip(final Tier toolMaterial, final Properties settings, final List<MutableComponent> tooltip) {
        super(toolMaterial, settings);
        this.tooltip=tooltip;
    }

    @Override
    public boolean hurtEnemy(@NotNull final ItemStack stack, @NotNull final LivingEntity target, @NotNull final LivingEntity attacker) {
        final boolean result = super.hurtEnemy(stack, target, attacker);

        if(stack.is(TCOTS_Items.WINTERS_BLADE.get())) {
            if (attacker.getRandom().nextIntBetweenInclusive(0, 5) == 1) {
                attacker.playSound(SoundEvents.GLASS_BREAK, 1.0f, 1.0f);
                if (target instanceof Player) {
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 8 * 20, 6, false, true), attacker);
                } else if (target.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                    target.addEffect(new MobEffectInstance(TCOTS_Effects.NorthernWindEffect(), 2 * 20, 2, false, false), attacker);
                } else {
                    target.addEffect(new MobEffectInstance(TCOTS_Effects.NorthernWindEffect(), 4 * 20, 2, false, false), attacker);
                }
            }

            if(target.isOnFire()){
                target.clearFire();
            }
        }

        if(stack.is(TCOTS_Items.DYAEBL.get())){
            if(attacker.getRandom().nextIntBetweenInclusive(0,5)==1){
                attacker.playSound(TCOTS_Sounds.getSoundEvent("black_blood_hit"), 1.0f, 1.0f);
                target.addEffect(new MobEffectInstance(TCOTS_Effects.Bleeding(), 10*20, 1, false, false, true));
            }
        }

        return result;
    }


}
