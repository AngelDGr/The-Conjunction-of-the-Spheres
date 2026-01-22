package mors.tcots.interfaces;

public interface LivingEntityMixinInterface {

    default int tcots$getKillCount() {
        return 0;
    }

    default void tcots$setKillCount(final int i) {

    }

    default void tcots$incrementKillCount() {

    }

    default int tcots$getKillCountdown() {
        return 0;
    }

    default void tcots$setKillCountdown(final int i) {

    }

    //NorthernWind
    default boolean tcots$isFrozen(){
        return false;
    }

    //Moon Dust
    default boolean tcots$hasSilverSplinters(){
        return false;
    }


    //Anchor
    default Object tcots$getAnchor(){
        return null;
    }

    default void tcots$setAnchor(final Object anchor){}

}
