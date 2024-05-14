package TCOTS.items.potions.effects.bombs;

import net.minecraft.entity.effect.StatusEffectCategory;

public class DimeritiumBombEffect extends BombEffectBase{
    public DimeritiumBombEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
