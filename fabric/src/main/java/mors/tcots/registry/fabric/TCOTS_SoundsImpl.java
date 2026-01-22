package mors.tcots.registry.fabric;

import mors.tcots.TCOTS_Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@SuppressWarnings("unused")
public class TCOTS_SoundsImpl {

    public static final Holder.Reference<SoundEvent> ROTFIEND_BLOOD_EXPLOSION = registerReference("entity.necrophage.rotfiend.blood_explosion");

    public static void initSounds() {
    }

    public static Holder.Reference<SoundEvent> getRotfiendBloodExplosion(){
        return ROTFIEND_BLOOD_EXPLOSION;
    }

    public static Holder.Reference<SoundEvent> registerReference(final String id) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id), SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id)));
    }
}
