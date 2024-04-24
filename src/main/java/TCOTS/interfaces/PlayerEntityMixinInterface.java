package TCOTS.interfaces;

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

    default int theConjunctionOfTheSpheres$getToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$setToxicity(int toxicity){}

    default int theConjunctionOfTheSpheres$getMaxToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$addMaxToxicity(int MaxToxicity){}

    default void theConjunctionOfTheSpheres$setMaxToxicity(int MaxToxicity){}

    default void theConjunctionOfTheSpheres$setDecoctionToxicity(int DecoctionToxicity){}

    default int theConjunctionOfTheSpheres$getDecoctionToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$addToxicity(int toxicity,boolean decoction){}

    default int theConjunctionOfTheSpheres$getAllToxicity(){return 0;}

    default void theConjunctionOfTheSpheres$decreaseToxicity(int toxicity, boolean decoction){}

    default float theConjunctionOfTheSpheres$getHudTransparency(){return 0;}

    default boolean theConjunctionOfTheSpheres$getHudActive(){return false;}

    default void theConjunctionOfTheSpheres$setHudTransparency(float transparency){}

    default void theConjunctionOfTheSpheres$setHudActive(boolean active){}
}
