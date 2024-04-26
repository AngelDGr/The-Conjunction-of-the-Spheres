package TCOTS.world;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TCOTS_DamageTypes {
    public static final RegistryKey<DamageType> POTION_TOXICITY = registerKey("toxicity");

    public static void boostrap(Registerable<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(POTION_TOXICITY, new DamageType("potionToxicity", 0.1f));
    }
    public static RegistryKey<DamageType> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(TCOTS_Main.MOD_ID, name));
    }

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    public static DamageSource toxicityDamage(World world) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(POTION_TOXICITY));
    }

}
