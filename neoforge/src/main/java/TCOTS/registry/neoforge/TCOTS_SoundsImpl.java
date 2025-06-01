package TCOTS.registry.neoforge;

import TCOTS.TCOTS_Main;
import neoforge.TCOTS.TCOTS_Registries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class TCOTS_SoundsImpl {

    public static final Supplier<Holder.Reference<SoundEvent>> ROTFIEND_BLOOD_EXPLOSION = registerReference("rotfiend_blood_explosion");

    public static void initSounds() {
    }

    public static Holder.Reference<SoundEvent> getRotfiendBloodExplosion(){
        return ROTFIEND_BLOOD_EXPLOSION.get();
    }

    public static Supplier<Holder.Reference<SoundEvent>> registerReference(String id) {
        DeferredHolder<SoundEvent,SoundEvent> soundEventReference = TCOTS_Registries.SOUND_EVENTS.register(id,
                () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id)));
        return () -> (Holder.Reference<SoundEvent>) soundEventReference.getDelegate();
    }
}
