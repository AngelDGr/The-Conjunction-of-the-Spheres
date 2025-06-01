package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MonsterNestModel extends GeoModel<MonsterNestBlockEntity> {
    @Override
    public ResourceLocation getModelResource(MonsterNestBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/monster_nest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MonsterNestBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/monster_nest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MonsterNestBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
