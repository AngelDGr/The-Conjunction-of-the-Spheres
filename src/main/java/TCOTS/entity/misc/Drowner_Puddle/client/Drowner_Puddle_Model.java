package TCOTS.entity.misc.Drowner_Puddle.client;

import TCOTS.entity.misc.Drowner_Puddle.Drowner_Puddle_Entity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
@Environment(EnvType.CLIENT)
public class Drowner_Puddle_Model<T extends Entity> extends SinglePartEntityModel<T> {
	private final ModelPart root;
	private final ModelPart puddle;
	public Drowner_Puddle_Model(ModelPart root) {
		this.root = root;
		this.puddle = root.getChild("puddle");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
//		modelPartData.addChild("puddle", ModelPartBuilder.create().uv(-32, 0).cuboid(-15.0F, 0.0F, -16.0F, 32.0F, 0.1F, 32.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
		modelPartData.addChild("puddle", ModelPartBuilder.create().uv(0, 0).cuboid(-15.0F, 0.0F, -16.0F, 32.0F, 0.1F, 32.0F), ModelTransform.NONE);
		return TexturedModelData.of(modelData, 32, 32);
	}

	@Override
	public ModelPart getPart() {return puddle;}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		this.puddle.yaw = headYaw * 0.017453292F;
		this.puddle.pitch = headPitch * 0.017453292F;
	}
}