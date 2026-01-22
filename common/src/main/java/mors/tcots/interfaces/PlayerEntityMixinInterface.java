package mors.tcots.interfaces;

import org.joml.Vector3f;

@SuppressWarnings("unused")
public interface PlayerEntityMixinInterface {

    default int tcots$$getMudInFace(){
        return 0;
    }

    default void tcots$setMudInFace(final int ticks){}

    default float tcots$getMudTransparency(){
        return 0f;
    }

    //Toxicity
    default int tcots$getNormalToxicity(){return 0;}

    default void tcots$setToxicity(final int toxicity){}

    default int tcots$setMaxToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$setMaxToxicity(final int MaxToxicity){}

    default void tcots$setDecoctionToxicity(final int DecoctionToxicity){}

    default int tcots$getDecoctionToxicity(){return 0;}

    default void tcots$addToxicity(final int toxicity, final boolean decoction){}

    default int tcots$getAllToxicity(){return 0;}

    default void tcots$decreaseToxicity(final int toxicity, final boolean decoction){}

    default boolean tcots$toxicityOverThreshold(){return false;}

    //Witcher Eyes
    default boolean tcots$getWitcherEyesActivated(){return false;}
    default void tcots$setWitcherEyesActivated(final boolean activate){}

    default boolean tcots$getToxicityActivated(){return false;}
    default void tcots$setToxicityActivated(final boolean activate){}


    default Vector3f tcots$getEyesPivot(){return new Vector3f();}
    default void tcots$setEyesPivot(final Vector3f vector3f){}


    default int tcots$getEyeSeparation(){return 2;}
    default void tcots$setEyeSeparation(final int separation){}

    default int tcots$getEyeShape(){return 0;}
    default void tcots$setEyeShape(final int shape){}

    default boolean tcots$getEyeMoves(){return false;}
    default void tcots$setEyeMoves(final boolean moves){}
}
