package TCOTS.effects.bombs;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BombEffectBase extends MobEffect {
    public BombEffectBase(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(final int duration, final int amplifier) {
        return true;
    }
}
