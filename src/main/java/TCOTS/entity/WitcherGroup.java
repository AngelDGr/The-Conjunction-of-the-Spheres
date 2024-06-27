package TCOTS.entity;

import net.minecraft.entity.EntityGroup;

public class WitcherGroup extends EntityGroup {

    private final String id;

    private final int numericID;

    public WitcherGroup(String id, int numericID){
        this.id=id;
        this.numericID=numericID;
    }

    public String getTranslationKey() {
        return "entity.tcots-witcher.group."+id;
    }

    public int getNumericID() {
        return numericID;
    }
}
