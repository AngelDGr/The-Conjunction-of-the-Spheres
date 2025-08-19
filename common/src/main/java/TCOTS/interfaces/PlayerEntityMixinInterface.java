package TCOTS.interfaces;

import org.joml.Vector3f;

@SuppressWarnings("unused")
public interface PlayerEntityMixinInterface {

    default int theConjunctionOfTheSpheres$getMudInFace(){
        return 0;
    }

    default void theConjunctionOfTheSpheres$setMudInFace(final int ticks){}

    default float theConjunctionOfTheSpheres$getMudTransparency(){
        return 0f;
    }

    //Toxicity

    default int theConjunctionOfTheSpheres$getNormalToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$setToxicity(final int toxicity){}

    default int theConjunctionOfTheSpheres$getMaxToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$setMaxToxicity(final int MaxToxicity){}

    default void theConjunctionOfTheSpheres$setDecoctionToxicity(final int DecoctionToxicity){}

    default int theConjunctionOfTheSpheres$getDecoctionToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$addToxicity(final int toxicity, final boolean decoction){}

    default int theConjunctionOfTheSpheres$getAllToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$decreaseToxicity(final int toxicity, final boolean decoction){}

    default boolean theConjunctionOfTheSpheres$toxicityOverThreshold(){return false;}

    //Witcher Eyes
    default boolean theConjunctionOfTheSpheres$getWitcherEyesActivated(){return false;}
    default void theConjunctionOfTheSpheres$setWitcherEyesActivated(final boolean activate){}

    default boolean theConjunctionOfTheSpheres$getToxicityActivated(){return false;}
    default void theConjunctionOfTheSpheres$setToxicityActivated(final boolean activate){}


    default Vector3f theConjunctionOfTheSpheres$getEyesPivot(){return new Vector3f();}
    default void theConjunctionOfTheSpheres$setEyesPivot(final Vector3f vector3f){}


    default int theConjunctionOfTheSpheres$getEyeSeparation(){return 2;}
    default void theConjunctionOfTheSpheres$setEyeSeparation(final int separation){}

    default int theConjunctionOfTheSpheres$getEyeShape(){return 0;}
    default void theConjunctionOfTheSpheres$setEyeShape(final int shape){}

    default boolean theConjunctionOfTheSpheres$getEyeMoves(){return false;}
    default void theConjunctionOfTheSpheres$setEyeMoves(final boolean moves){}
}
