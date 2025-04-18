package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.MonsterNestItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MonsterNestItemModel  extends GeoModel<MonsterNestItem> {
    @Override
    public ResourceLocation getModelResource(MonsterNestItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/monster_nest.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MonsterNestItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/monster_nest.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MonsterNestItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
