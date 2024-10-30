package TCOTS.mixin;

import TCOTS.interfaces.MapIconTypeMixinInterface;
import TCOTS.interfaces.MapIconMixinInterface;
import TCOTS.items.maps.TCOTS_MapIcons;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MapState.class)
public abstract class MapStateMixin {

    @Shadow protected abstract void addIcon(MapIcon.Type type, @Nullable WorldAccess world, String key, double x, double z, double rotation, @Nullable Text text);

    @Shadow @Final
    Map<String, MapIcon> icons;

    @Inject(method = "update", at = @At("TAIL"))
    public void injectCustomIcons(PlayerEntity player, ItemStack stack, CallbackInfo ci){
        NbtCompound nbtCompound;
        if ((nbtCompound = stack.getNbt()) != null && nbtCompound.contains("CustomIcons", NbtElement.LIST_TYPE)) {
            NbtList nbtList = nbtCompound.getList("CustomIcons", NbtElement.COMPOUND_TYPE);
            for (int j = 0; j < nbtList.size(); ++j) {

                NbtCompound nbtCompound2 = nbtList.getCompound(j);

                if (this.icons.containsKey(nbtCompound2.getString("id"))) continue;

                MapIcon.Type icon = MapIcon.Type.byId(nbtCompound2.getByte("type"));

                icon.theConjunctionOfTheSpheres$setCustomIcon(TCOTS_MapIcons.Type.byId(nbtCompound2.getByte("customType")));

                this.addIcon(
                        icon,
                        player.getWorld(), nbtCompound2.getString("id"),
                        nbtCompound2.getDouble("x"),
                        nbtCompound2.getDouble("z"),
                        nbtCompound2.getDouble("rot"),
                        null);
            }
        }
    }



    @Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
    public abstract static class MapTextureMixin {
        @Shadow private MapState state;
        @Unique
        private MapIcon icon;

        @ModifyVariable(method = "draw", at = @At(value = "LOAD", ordinal = 4), ordinal = 0)
        private MapIcon getIcon(MapIcon icon){
            this.icon=icon;

            return icon;
        }

        @ModifyArg(method = "draw",
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;",
                        ordinal = 4),
                index = 3)
        private int setTransparency1(int alpha){
            return setTransparency(alpha);
        }

        @ModifyArg(method = "draw",
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;",
                        ordinal = 5),
                index = 3)
        private int setTransparency2(int alpha){
            return setTransparency(alpha);
        }

        @ModifyArg(method = "draw",
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;",
                        ordinal = 6),
                index = 3)
        private int setTransparency3(int alpha){
            return setTransparency(alpha);
        }

        @ModifyArg(method = "draw",
                at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;",
                        ordinal = 7),
                index = 3)
        private int setTransparency4(int alpha){
            return setTransparency(alpha);
        }



        @Unique
        private int setTransparency(int alpha){
            if(icon!=null && icon.theConjunctionOfTheSpheres$hasCustomIcon()){
                return 0;
            }
            return alpha;
        }

        @Inject(method = "draw", at = @At(value = "TAIL"))
        private void injectCustomIconDrawing(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, boolean hidePlayerIcons, int light, CallbackInfo ci){
            for(MapIcon mapIcon : this.state.getIcons()){
                if(mapIcon.type().theConjunctionOfTheSpheres$customIcon()!=null){

                    if (hidePlayerIcons && !mapIcon.isAlwaysRendered()) continue;
                    int k = 2;

                    matrices.push();
                    matrices.translate(0.0f + (float)mapIcon.x() / 2.0f + 64.0f, 0.0f + (float)mapIcon.z() / 2.0f + 64.0f, -0.02f);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)(mapIcon.rotation() * 360) / 16.0f));
                    matrices.scale(4.0f, 4.0f, 3.0f);
                    matrices.translate(-0.125f, 0.125f, 0.0f);

                    Matrix4f matrix4f2 = matrices.peek().getPositionMatrix();

                    VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getText(mapIcon.type().theConjunctionOfTheSpheres$customIcon().getTexture()));


                    vertexConsumer2.vertex(matrix4f2, -1.0f, 1.0f, (float)k * -0.001f).color(255, 255, 255, 255)
                            .texture(0, 0)
                            .light(light).next();

                    vertexConsumer2.vertex(matrix4f2, 1.0f, 1.0f, (float)k * -0.001f).color(255, 255, 255, 255)
                            .texture(1, 0)
                            .light(light).next();

                    vertexConsumer2.vertex(matrix4f2, 1.0f, -1.0f, (float)k * -0.001f).color(255, 255, 255, 255)
                            .texture(1, 1)
                            .light(light).next();

                    vertexConsumer2.vertex(matrix4f2, -1.0f, -1.0f, (float)k * -0.001f).color(255, 255, 255, 255)
                            .texture(0, 1)
                            .light(light).next();


                    matrices.pop();
                }
            }
        }
    }

    @Mixin(MapIcon.Type.class)
    public abstract static class MapIconTypeTypeMixin implements MapIconTypeMixinInterface {

        @Unique
        @Nullable
        public TCOTS_MapIcons.Type TYPE;

        @Nullable
        @Override
        public TCOTS_MapIcons.Type theConjunctionOfTheSpheres$customIcon() {
            return TYPE;
        }

        @Override
        public void theConjunctionOfTheSpheres$setCustomIcon(@Nullable TCOTS_MapIcons.Type type) {
            this.TYPE=type;
        }
    }

    @Mixin(MapIcon.class)
    public abstract static class MapIconTypeMixin implements MapIconMixinInterface {

        @Shadow @Final private MapIcon.Type type;

        @Override
        public boolean theConjunctionOfTheSpheres$hasCustomIcon() {
            return type.theConjunctionOfTheSpheres$customIcon() != null;
        }
    }
}
