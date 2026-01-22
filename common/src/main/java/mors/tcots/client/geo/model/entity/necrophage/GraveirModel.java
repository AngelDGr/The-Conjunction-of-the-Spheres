package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.GraveirEntity;
import net.minecraft.resources.ResourceLocation;

public class GraveirModel extends DefaultedNecrophageModel<GraveirEntity> {

    public GraveirModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "graveir"), true);
    }

    @Override
    public ResourceLocation getTextureResource(final GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/monster/necrophage/graveir/graveir.png");
    }
}
