package TCOTS.interfaces;


import TCOTS.items.maps.TCOTS_MapIcons;

public interface MapIconMixinInterface {
    default TCOTS_MapIcons.Type theConjunctionOfTheSpheres$customIcon(){return null;}
    default void theConjunctionOfTheSpheres$setCustomIcon(TCOTS_MapIcons.Type type){}

}
