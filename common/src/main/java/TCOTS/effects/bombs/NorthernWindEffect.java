package TCOTS.effects.bombs;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NorthernWindEffect extends MobEffect {
    public NorthernWindEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(final int duration, final int amplifier) {
        return true;
    }
}
