package TCOTS.world;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TCOTS_DamageTypes {
    public static final RegistryKey<DamageType> POTION_TOXICITY = registerKey("toxicity");
    public static final RegistryKey<DamageType> BLEEDING = registerKey("bleeding");
    public static final RegistryKey<DamageType> CADAVERINE = registerKey("cadaverine");

    public static final RegistryKey<DamageType> ANCHOR = registerKey("anchor");

    public static void boostrap(@NotNull Registerable<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(POTION_TOXICITY, new DamageType("potionToxicity", 0.1f));
        damageTypeRegisterable.register(BLEEDING, new DamageType("bleeding", 0.5f));
        damageTypeRegisterable.register(CADAVERINE, new DamageType("cadaverine", 0.5f));

        damageTypeRegisterable.register(ANCHOR, new DamageType("anchor", 0.1f));
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

    public static DamageSource bleedDamage(World world) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(BLEEDING));
    }

    public static DamageSource cadaverineDamage(World world) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(CADAVERINE));
    }

    public static DamageSource anchorDamage(World world, Entity source, @Nullable Entity attacker) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(ANCHOR), source, attacker);
    }

}
