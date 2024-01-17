package TCOTS.entity.misc.Drowner_Puddle.client;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.Drowner_Puddle.Drowner_Puddle_Entity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LeashKnotEntityModel;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class Drowner_Puddle_Renderer extends EntityRenderer<Drowner_Puddle_Entity> {

    //TODO: Try to make the texture transparent
    //TODO: Make the texture animated

    private static final Identifier TEXTURE = new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner_puddle.png");

    private final Drowner_Puddle_Model<Drowner_Puddle_Entity> model;

    public Drowner_Puddle_Renderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new Drowner_Puddle_Model(ctx.getPart(ModModelLayers.DROWNER_PUDDLE));
    }

    @Override
    public void render(Drowner_Puddle_Entity mobEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {

        //Take the water color from the biome
        int waterColor = BiomeColors.getWaterColor(mobEntity.getWorld(), new BlockPos((int)mobEntity.getX(), (int)mobEntity.getY(), (int)mobEntity.getZ()));
        float red = ((waterColor >> 16) & 255) / 255.0F;
        float green = ((waterColor >> 8) & 255) / 255.0F;
        float blue = (waterColor & 255) / 255.0F;

        matrixStack.push();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
//        this.model.setAngles(mobEntity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, red, green, blue, 0.8F);
        matrixStack.pop();

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(Drowner_Puddle_Entity entity) {
        return TEXTURE;
    }
}
