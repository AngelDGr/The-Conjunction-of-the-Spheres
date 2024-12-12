package TCOTS.mixin;

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
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MapState.class)
public abstract class MapStateMixin {
    @Shadow @Final
    Map<String, MapIcon> icons;

    @Shadow private int iconCount;

    @Shadow protected abstract void markIconsDirty();

    @Shadow @Final public byte scale;

    @Shadow @Final public int centerX;

    @Shadow @Final public int centerZ;

    @Shadow @Final public RegistryKey<World> dimension;

    @Shadow protected abstract void removeIcon(String id);

    @Inject(method = "update", at = @At("TAIL"))
    public void injectCustomIcons(PlayerEntity player, ItemStack stack, CallbackInfo ci){
        NbtCompound nbtCompound;
        if ((nbtCompound = stack.getNbt()) != null && nbtCompound.contains("CustomIcons", NbtElement.LIST_TYPE)) {
            NbtList nbtList = nbtCompound.getList("CustomIcons", NbtElement.COMPOUND_TYPE);
            for (int j = 0; j < nbtList.size(); ++j) {

                NbtCompound nbtCompound2 = nbtList.getCompound(j);

                if (this.icons.containsKey(nbtCompound2.getString("customId"))) continue;

                TCOTS_MapIcons.Type type = TCOTS_MapIcons.Type.byId(nbtCompound2.getByte("customType"));

                MapIcon mapIcon;
                MapIcon mapIcon2;

                String key = nbtCompound2.getString("customId");

                byte d;
                int i = 1 << this.scale;
                double rotation = nbtCompound2.getDouble("rot");
                float f = (float)(nbtCompound2.getDouble("x") - (double)this.centerX) / (float)i;
                float g = (float)(nbtCompound2.getDouble("z") - (double)this.centerZ) / (float)i;
                byte b = (byte)((double)(f * 2.0f) + 0.5);
                byte c = (byte)((double)(g * 2.0f) + 0.5);

                if (f >= -63.0f && g >= -63.0f && f <= 63.0f && g <= 63.0f) {
                    d = (byte)((rotation + (rotation < 0.0 ? -8.0 : 8.0)) * 16.0 / 360.0);
                    if (this.dimension == World.NETHER && player.getWorld() != null) {
                        int k = (int)(player.getWorld().getLevelProperties().getTimeOfDay() / 10L);
                        d = (byte)(k * k * 34187121 + k * 121 >> 15 & 0xF);
                    }
                } else {
                    this.removeIcon(key);
                    return;
                }

                if (!(mapIcon = new MapIcon(
                        MapIcon.Type.MANSION,
                        b,
                        c,
                        d,
                        null)).equals(mapIcon2 = this.icons.put(key, mapIcon))) {
                    if (mapIcon2 != null && mapIcon2.getType().shouldUseIconCountLimit()) {
                        --this.iconCount;
                    }
                    if (type.shouldUseIconCountLimit()) {
                        ++this.iconCount;
                    }

                    mapIcon.theConjunctionOfTheSpheres$setCustomIcon(type);
                    this.markIconsDirty();
                }
            }
        }
    }


    @SuppressWarnings("unused")
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
            if(icon!=null && icon.theConjunctionOfTheSpheres$customIcon()!=null){
                return 0;
            }
            return alpha;
        }

        @Inject(method = "draw", at = @At(value = "TAIL"))
        private void injectCustomIconDrawing(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, boolean hidePlayerIcons, int light, CallbackInfo ci){
            for(MapIcon mapIcon : this.state.getIcons()){
                if(mapIcon.theConjunctionOfTheSpheres$customIcon()!=null){

                    if (hidePlayerIcons && !mapIcon.isAlwaysRendered()) continue;
                    int k = 2;

                    matrices.push();
                    matrices.translate(0.0f + (float)mapIcon.getX() / 2.0f + 64.0f, 0.0f + (float)mapIcon.getZ() / 2.0f + 64.0f, -0.02f);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)(mapIcon.getRotation() * 360) / 16.0f));
                    matrices.scale(4.0f, 4.0f, 3.0f);
                    matrices.translate(-0.125f, 0.125f, 0.0f);

                    Matrix4f matrix4f2 = matrices.peek().getPositionMatrix();

                    VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getText(mapIcon.theConjunctionOfTheSpheres$customIcon().getTexture()));


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

    @SuppressWarnings("unused")
    @Mixin(MapIcon.class)
    public abstract static class MapIconMixin implements MapIconMixinInterface {
        @Unique
        @Nullable
        public TCOTS_MapIcons.Type TYPE;


        @Override
        public void theConjunctionOfTheSpheres$setCustomIcon(TCOTS_MapIcons.Type type) {
            TYPE=type;
        }

        @Override
        public TCOTS_MapIcons.Type theConjunctionOfTheSpheres$customIcon() {
            return TYPE;
        }
    }
}
