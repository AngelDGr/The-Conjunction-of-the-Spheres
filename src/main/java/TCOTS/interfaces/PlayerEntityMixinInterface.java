package TCOTS.interfaces;

import org.joml.Vector3f;

@SuppressWarnings("unused")
public interface PlayerEntityMixinInterface {

    default int theConjunctionOfTheSpheres$getMudInFace(){
        return 0;
    }

    default void theConjunctionOfTheSpheres$setMudInFace(int ticks){}

    default float theConjunctionOfTheSpheres$getMudTransparency(){
        return 0f;
    }

    //Toxicity

    default int theConjunctionOfTheSpheres$getNormalToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$setToxicity(int toxicity){}

    default int theConjunctionOfTheSpheres$getMaxToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$addMaxToxicity(int MaxToxicity){}

    default void theConjunctionOfTheSpheres$decreaseMaxToxicity(int MaxToxicity){}

    default void theConjunctionOfTheSpheres$setMaxToxicity(int MaxToxicity){}

    default void theConjunctionOfTheSpheres$setDecoctionToxicity(int DecoctionToxicity){}

    default int theConjunctionOfTheSpheres$getDecoctionToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$addToxicity(int toxicity,boolean decoction){}

    default int theConjunctionOfTheSpheres$getAllToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$decreaseToxicity(int toxicity, boolean decoction){}

    default boolean theConjunctionOfTheSpheres$toxicityOverThreshold(){return false;}

    //Witcher Eyes
    default boolean theConjunctionOfTheSpheres$getWitcherEyesActivated(){return false;}
    default void theConjunctionOfTheSpheres$setWitcherEyesActivated(boolean activate){}

    default boolean theConjunctionOfTheSpheres$getToxicityActivated(){return false;}
    default void theConjunctionOfTheSpheres$setToxicityActivated(boolean activate){}


    default Vector3f theConjunctionOfTheSpheres$getEyesPivot(){return new Vector3f();}
    default void theConjunctionOfTheSpheres$setEyesPivot(Vector3f vector3f){}


    default int theConjunctionOfTheSpheres$getEyeSeparation(){return 2;}
    default void theConjunctionOfTheSpheres$setEyeSeparation(int separation){}

    default int theConjunctionOfTheSpheres$getEyeShape(){return 0;}
    default void theConjunctionOfTheSpheres$setEyeShape(int shape){}
}
