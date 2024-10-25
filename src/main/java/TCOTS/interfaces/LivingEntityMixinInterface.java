package TCOTS.interfaces;

import TCOTS.entity.misc.AnchorProjectileEntity;

public interface LivingEntityMixinInterface {

    default int theConjunctionOfTheSpheres$getKillCount() {
        return 0;
    }

    default void theConjunctionOfTheSpheres$setKillCount(int i) {

    }

    default void theConjunctionOfTheSpheres$incrementKillCount() {

    }

    default int theConjunctionOfTheSpheres$getKillCountdown() {
        return 0;
    }

    default void theConjunctionOfTheSpheres$setKillCountdown(int i) {

    }

    //NorthernWind
    default boolean theConjunctionOfTheSpheres$isFrozen(){
        return false;
    }

    //Moon Dust
    default boolean theConjunctionOfTheSpheres$hasSilverSplinters(){
        return false;
    }


    //Anchor
    default AnchorProjectileEntity theConjunctionOfTheSpheres$getAnchor(){
        return null;
    }

    default void theConjunctionOfTheSpheres$setAnchor(AnchorProjectileEntity anchor){}

}
