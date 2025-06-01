package TCOTS.registry;

import TCOTS.TCOTS_Main;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TCOTS_DamageTypes {

    public static final ResourceKey<DamageType> POTION_TOXICITY = registerKey("toxicity");
    public static final ResourceKey<DamageType> BLEEDING = registerKey("bleeding");
    public static final ResourceKey<DamageType> CADAVERINE = registerKey("cadaverine");
    public static final ResourceKey<DamageType> ANCHOR = registerKey("anchor");

    public static DamageSource toxicityDamage(Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(POTION_TOXICITY));
    }

    public static DamageSource bleedDamage(Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(BLEEDING));
    }

    public static DamageSource cadaverineDamage(Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(CADAVERINE));
    }

    public static DamageSource anchorDamage(Level world, Entity source, @Nullable Entity attacker) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ANCHOR), source, attacker);
    }

    public static void boostrapDamageTypes(@NotNull BootstrapContext<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(POTION_TOXICITY, new DamageType("potionToxicity", 0.1f));
        damageTypeRegisterable.register(BLEEDING, new DamageType("bleeding", 0.5f));
        damageTypeRegisterable.register(CADAVERINE, new DamageType("cadaverine", 0.5f));

        damageTypeRegisterable.register(ANCHOR, new DamageType("anchor", 0.1f));
    }


    public static ResourceKey<DamageType> registerKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key));
    }
}
