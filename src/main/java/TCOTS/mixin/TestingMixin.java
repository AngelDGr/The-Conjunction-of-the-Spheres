package TCOTS.mixin;

import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("all")
@Mixin(PatrolSpawner.class)
public class TestingMixin {

    @Unique
    PatrolSpawner THIS = (PatrolSpawner) (Object) this;

}
