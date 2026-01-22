package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.GhoulEntity;
import net.minecraft.resources.ResourceLocation;

public class GhoulModel extends DefaultedNecrophageModel<GhoulEntity> {

    public GhoulModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ghoul"), true);
    }

    @Override
    public ResourceLocation getTextureResource(final GhoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/monster/necrophage/ghoul/ghoul.png");
    }
}
