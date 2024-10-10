package TCOTS.mixin;

import TCOTS.items.TCOTS_Items;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "setModelPose", at = @At("TAIL"))
    private void hidePartsForArmor(AbstractClientPlayerEntity player, CallbackInfo ci){
        PlayerEntityModel<?> playerEntityModel = this.getModel();
        if((player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.RAVENS_ARMOR) || player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.MANTICORE_ARMOR) || player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.WARRIORS_LEATHER_JACKET))) {
            playerEntityModel.jacket.visible = false;
            playerEntityModel.leftSleeve.visible = MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
            playerEntityModel.rightSleeve.visible = MinecraftClient.getInstance().options.getPerspective().isFirstPerson();


            playerEntityModel.rightArm.visible=MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
            playerEntityModel.leftArm.visible=MinecraftClient.getInstance().options.getPerspective().isFirstPerson();
        }

        if((player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.RAVENS_TROUSERS) || player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.MANTICORE_TROUSERS) || player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.WARRIORS_LEATHER_TROUSERS))){
            playerEntityModel.leftPants.visible = false;
            playerEntityModel.rightPants.visible = false;
        }

        if (!player.isSpectator() && !(player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.RAVENS_ARMOR) || player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.MANTICORE_ARMOR) || player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.WARRIORS_LEATHER_JACKET))){
            playerEntityModel.rightArm.visible=true;
            playerEntityModel.leftArm.visible=true;

            playerEntityModel.jacket.visible = player.isPartVisible(PlayerModelPart.JACKET);
            playerEntityModel.leftSleeve.visible = player.isPartVisible(PlayerModelPart.LEFT_SLEEVE);
            playerEntityModel.rightSleeve.visible = player.isPartVisible(PlayerModelPart.RIGHT_SLEEVE);
        }

        if(!player.isSpectator() && !(player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.RAVENS_TROUSERS) || player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.MANTICORE_TROUSERS) || player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.WARRIORS_LEATHER_TROUSERS))){
            playerEntityModel.leftPants.visible = player.isPartVisible(PlayerModelPart.LEFT_PANTS_LEG);
            playerEntityModel.rightPants.visible = player.isPartVisible(PlayerModelPart.RIGHT_PANTS_LEG);
        }


    }
}
