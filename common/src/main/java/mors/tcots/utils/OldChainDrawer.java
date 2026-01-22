package mors.tcots.utils;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class OldChainDrawer {

    /**
     * If I am honest I do not really know what is happening here most of the time, most of the code was 'inspired' by
     * the {@link net.minecraft.client.renderer.entity.LeashKnotRenderer}.
     * Many variables therefore have simple names. I tried my best to comment and explain what everything does.
     *
     * @param fromEntity             The origin Entity
     * @param tickDelta              Delta tick
     * @param matrixStack               The render matrix stack.
     * @param vertexConsumerProvider The VertexConsumerProvider, whatever it does.
     * @param toEntity               The entity that we connect the chain to, this can be a {PlayerEntity} or a {ChainKnotEntity}.
     */
    private void createChainLine(final Entity fromEntity, final float tickDelta, final PoseStack matrixStack, final MultiBufferSource vertexConsumerProvider, final Entity toEntity) {

        final double lerpBodyAngle = (Mth.lerp(tickDelta, fromEntity.yRotO, fromEntity.getVisualRotationYInDegrees()) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
        final Vec3 leashOffset = new Vec3(0, 0, 0);
        final double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        final double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;

        final Vec3 ropeGripPosition = toEntity.getRopeHoldPosition(tickDelta);

        final double lerpOriginX = Mth.lerp(tickDelta, fromEntity.xo, fromEntity.getX()) + xAngleOffset;
        final double lerpOriginY = Mth.lerp(tickDelta, fromEntity.yo, fromEntity.getY()) + leashOffset.y;
        final double lerpOriginZ = Mth.lerp(tickDelta, fromEntity.zo, fromEntity.getZ()) + zAngleOffset;

        final float lerpDistanceX = (float) (ropeGripPosition.x - lerpOriginX);
        final float lerpDistanceY = (float) (ropeGripPosition.y - lerpOriginY);
        final float lerpDistanceZ = (float) (ropeGripPosition.z - lerpOriginZ);
        //Create offset based on the location. Example that a line that does not travel in the x then the xOffset will be 0.
        final float v = Mth.invSqrt(lerpDistanceX * lerpDistanceX + lerpDistanceZ * lerpDistanceZ) * 0.025F / 2;
        final float xOffset = lerpDistanceZ * v;
        final float zOffset = lerpDistanceX * v;

        final VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.leash());

        matrixStack.pushPose(); // We push here to start new I think.
        matrixStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

        // Now we gather light information for the chain. Since the chain is lighter if there is more light.
        final BlockPos entityEyePos = BlockPos.containing(fromEntity.getEyePosition(tickDelta));
        final BlockPos holderEyePos = BlockPos.containing(toEntity.getEyePosition(tickDelta));
        final int entityBlockLight = getBlockLight(fromEntity, entityEyePos);
        final int holderBlockLight = toEntity.isOnFire() ? 15 : toEntity.level().getBrightness(LightLayer.BLOCK, holderEyePos);
        final int entitySkyLight = fromEntity.level().getBrightness(LightLayer.SKY, entityEyePos);
        final int holderSkyLight = fromEntity.level().getBrightness(LightLayer.SKY, holderEyePos);

        final float distance = toEntity.distanceTo(fromEntity);
        final Matrix4f matrix4f = matrixStack.last().pose();

        //This number specifies the number of pixels on the chain.
        chainDrawer(distance, vertexConsumer, matrix4f, lerpDistanceX, lerpDistanceY, lerpDistanceZ,
                entityBlockLight,
                holderBlockLight,
                entitySkyLight,
                holderSkyLight,
                xOffset, zOffset);

        matrixStack.popPose();
    }

    /**
     * This method is the big drawer of the chain.
     */
    @SuppressWarnings("DuplicatedCode")
    private void chainDrawer(final float distance, final VertexConsumer vertexConsumer, final Matrix4f matrix4f,
                             final float lerpDistanceX, final float lerpDistanceY, final float lerpDistanceZ,
                             final int blockLightLevelOfStart, final int blockLightLevelOfEnd,
                             final int skylightLevelOfStart, final int skylightLevelOfEnd,
                             final float xOffset, final float zOffset) {

        //Can you see the chain here?
        final List<Integer> topLineA;
        List<Integer> middleLineA;
        List<Integer> bottomLineA;
        List<Integer> topLineB;
        List<Integer> middleLineB;
        final List<Integer> bottomLineB;
        topLineA    = Arrays.asList(   1, 2, 3,       6, 7, 8, 9,         12, 13, 14);
        middleLineA = Arrays.asList(   1,    3,       6,       9,         12,     14);
        bottomLineA = Arrays.asList(   1, 2, 3,       6, 7, 8, 9,         12, 13, 14);

        topLineB    = Arrays.asList(0, 1,    3, 4, 5, 6,       9, 10, 11, 12,     14, 15);
        middleLineB = Arrays.asList(   1,    3,       6,       9,         12,     14    );
        bottomLineB = Arrays.asList(0, 1,    3, 4, 5, 6,       9, 10, 11, 12,     14, 15);

        final int length = (int) Math.floor(distance * 48); //This number specifies the number of pixels on the chain.

        // LightLevel Stuff
        final float s = (float) skylightLevelOfEnd / (length - 1);
        final int t = (int) Mth.lerp(s, (float) blockLightLevelOfStart, (float) blockLightLevelOfEnd);
        final int u = (int) Mth.lerp(s, (float) skylightLevelOfStart, (float) skylightLevelOfEnd);
        final int pack = LightTexture.pack(t, u);

        for (int step = 0; step < length; ++step) {
            final float startStepFraction = ((float) step / (float) length);
            final float endStepFraction = ((float) (step + 1) / (float) length);
            final float startDrip = (float) drip2(startStepFraction * distance, distance, lerpDistanceY);
            final float endDrip = (float) drip2(endStepFraction * distance, distance, lerpDistanceY);

            final float startRootX = lerpDistanceX * startStepFraction;
            final float startRootZ = lerpDistanceZ * startStepFraction;
            final float endRootX = lerpDistanceX * endStepFraction;
            final float endRootZ = lerpDistanceZ * endStepFraction;
            final float[] rotateStartEnd = rotator(startRootX - endRootX, (startDrip - endDrip), startRootZ - endRootZ);
            final float v1 = (rotateStartEnd[3] != 1.0F) ? 1.0F : -1.0F;
            float R, G, B;

            final float rotate0 = rotateStartEnd[0];
            final float rotate1 = rotateStartEnd[1];
            final float rotate2 = rotateStartEnd[2];
            // First Line
            final float chainHeight = 0.0125F;
            if (topLineA.contains(step % 16)) {
                final Vector3f startA;
                Vector3f endA;
                Vector3f startB;
                final Vector3f endB;
                startA = new Vector3f(
                        startRootX - rotate0 + xOffset,
                        chainHeight + rotate1 + startDrip,
                        startRootZ - rotate2 - zOffset
                );
                startB = new Vector3f(
                        startRootX - (rotate0 - xOffset) * 3,
                        chainHeight + rotate1 * 3 + startDrip,
                        startRootZ - (rotate2 + zOffset) * 3
                );
                endA = new Vector3f(
                        endRootX - rotate0 + xOffset,
                        chainHeight + rotate1 + endDrip,
                        endRootZ - rotate2 - zOffset
                );
                endB = new Vector3f(
                        endRootX - (rotate0 - xOffset) * 3,
                        chainHeight + rotate1 * 3 + endDrip,
                        endRootZ - (rotate2 + zOffset) * 3
                );
                R = 0.16F;
                G = 0.17F;
                B = 0.21F;
                renderPixel(startA, startB, endA, endB, vertexConsumer, matrix4f, pack, R, G, B);
            }
            if (middleLineA.contains(step % 16)) {
                final Vector3f startA;
                Vector3f endA;
                Vector3f startB;
                final Vector3f endB;
                startA = new Vector3f(
                        startRootX + rotate0 + xOffset,
                        chainHeight - rotate1 + startDrip,
                        startRootZ + rotate2 - zOffset
                );
                startB = new Vector3f(
                        startRootX - rotate0 - xOffset,
                        chainHeight + rotate1 + startDrip,
                        startRootZ - rotate2 + zOffset
                );
                endA = new Vector3f(
                        endRootX + rotate0 + xOffset,
                        chainHeight - rotate1 + endDrip,
                        endRootZ + rotate2 - zOffset
                );
                endB = new Vector3f(
                        endRootX - rotate0 - xOffset,
                        chainHeight + rotate1 + endDrip,
                        endRootZ - rotate2 + zOffset
                );
                R = 0.12F * 0.7F;
                G = 0.12F * 0.7F;
                B = 0.17F * 0.7F;
                renderPixel(startA, startB, endA, endB, vertexConsumer, matrix4f, pack, R, G, B);
            }
            if (bottomLineA.contains(step % 16)) {
                final Vector3f startA;
                Vector3f endA;
                Vector3f startB;
                final Vector3f endB;
                startA = new Vector3f(
                        startRootX + (rotate0 - xOffset) * 3,
                        chainHeight - rotate1 * 3 + startDrip,
                        startRootZ + (rotate2 + zOffset) * 3
                );
                startB = new Vector3f(
                        startRootX + rotate0 - xOffset,
                        chainHeight - rotate1 + startDrip,
                        startRootZ + rotate2 + zOffset
                );
                endA = new Vector3f(
                        endRootX + (rotate0 - xOffset) * 3,
                        chainHeight - rotate1 * 3 + endDrip,
                        endRootZ + (rotate2 + zOffset) * 3
                );
                endB = new Vector3f(
                        endRootX + rotate0 - xOffset,
                        chainHeight - rotate1 + endDrip,
                        endRootZ + rotate2 + zOffset
                );
                R = 0.16F;
                G = 0.17F;
                B = 0.21F;
                renderPixel(startA, startB, endA, endB, vertexConsumer, matrix4f, pack, R, G, B);
            }
            // Second Line
            if (topLineB.contains(step % 16)) {
                final Vector3f startA;
                Vector3f endA;
                Vector3f startB;
                final Vector3f endB;
                startA = new Vector3f(
                        startRootX - (rotate0 * v1) - xOffset,
                        chainHeight + rotate1 + startDrip,
                        startRootZ - rotate2 + zOffset
                );
                startB = new Vector3f(
                        startRootX - ((rotate0 * v1) + xOffset) * 3,
                        chainHeight + rotate1 * 3 + startDrip,
                        startRootZ - (rotate2 - zOffset) * 3
                );
                endA = new Vector3f(
                        endRootX - (rotate0 * v1) - xOffset,
                        chainHeight + rotate1 + endDrip,
                        endRootZ - rotate2 + zOffset
                );
                endB = new Vector3f(
                        endRootX - ((rotate0 * v1) + xOffset) * 3,
                        chainHeight + rotate1 * 3 + endDrip,
                        endRootZ - (rotate2 - zOffset) * 3
                );
                R = 0.16F * 0.8F;
                G = 0.17F * 0.8F;
                B = 0.21F * 0.8F;
                renderPixel(startA, startB, endA, endB, vertexConsumer, matrix4f, pack, R, G, B);
            }
            if (middleLineB.contains(step % 16)) {
                final Vector3f startA;
                Vector3f endA;
                Vector3f startB;
                final Vector3f endB;
                startA = new Vector3f(
                        startRootX + (rotate0 * v1) - xOffset,
                        chainHeight - rotate1 + startDrip,
                        startRootZ + rotate2 + zOffset
                );
                startB = new Vector3f(
                        startRootX - (rotate0 * v1) + xOffset,
                        chainHeight + rotate1 + startDrip,
                        startRootZ - rotate2 - zOffset
                );
                endA = new Vector3f(
                        endRootX + (rotate0 * v1) - xOffset,
                        chainHeight - rotate1 + endDrip,
                        endRootZ + rotate2 + zOffset
                );
                endB = new Vector3f(
                        endRootX - (rotate0 * v1) + xOffset,
                        chainHeight + rotate1 + endDrip,
                        endRootZ - rotate2 - zOffset
                );
                R = 0.12F;
                G = 0.12F;
                B = 0.17F;
                renderPixel(startA, startB, endA, endB, vertexConsumer, matrix4f, pack, R, G, B);
            }
            if (bottomLineB.contains(step % 16)) {
                final Vector3f startA;
                Vector3f endA;
                Vector3f startB;
                final Vector3f endB;
                startA = new Vector3f(
                        startRootX + ((rotate0 * v1) + xOffset) * 3,
                        chainHeight - rotate1 * 3 + startDrip,
                        startRootZ + (rotate2 - zOffset) * 3
                );
                startB = new Vector3f(
                        startRootX + (rotate0 * v1) + xOffset,
                        chainHeight - rotate1 + startDrip,
                        startRootZ + rotate2 - zOffset
                );
                endA = new Vector3f(
                        endRootX + ((rotate0 * v1) + xOffset) * 3,
                        chainHeight - rotate1 * 3 + endDrip,
                        endRootZ + (rotate2 - zOffset) * 3
                );
                endB = new Vector3f(
                        endRootX + (rotate0 * v1) + xOffset,
                        chainHeight - rotate1 + endDrip,
                        endRootZ + rotate2 - zOffset
                );
                R = 0.16F * 0.8F;
                G = 0.17F * 0.8F;
                B = 0.21F * 0.8F;
                renderPixel(startA, startB, endA, endB, vertexConsumer, matrix4f, pack, R, G, B);
            }
        }
    }

    /**
     * Draw a pixel with 4 vector locations and the other information.
     */
    private static void renderPixel(final Vector3f startA, final Vector3f startB, final Vector3f endA, final Vector3f endB,
                                    final VertexConsumer vertexConsumer, final Matrix4f matrix4f, final int lightPack,
                                    final float R, final float G, final float B) {
        vertexConsumer
                .addVertex(matrix4f, startA.x(), startA.y(), startA.z())
                .setColor(R, G, B, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(1, 0.35f, 0)

                .setUv(0, (float)1/16)

                .setLight(lightPack);

        vertexConsumer
                .addVertex(matrix4f, startB.x(), startB.y(), startB.z())
                .setColor(R, G, B, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(1, 0.35f, 0)

                .setUv((float)2/16, (float)1/16)

                .setLight(lightPack);

        vertexConsumer
                .addVertex(matrix4f, endB.x(), endB.y(), endB.z())
                .setColor(R, G, B, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(1, 0.35f, 0)

                .setUv(0, (float)3/16)

                .setLight(lightPack);

        vertexConsumer
                .addVertex(matrix4f, endA.x(), endA.y(), endA.z())
                .setColor(R, G, B, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(1, 0.35f, 0)

                .setUv((float)2/16, (float)3/16)

                .setLight(lightPack);

    }


    /**
     * For geogebra:
     * a = 9
     * h = 0
     * d = 5
     * p1 = a * asinh( (h / (2*a)) * 1 / sinh(d / (2*a)) )
     * p2 = -a * cosh( (2*p1 - d) / (2*a) )
     * f(x) = p2 + a * cosh( (2*x + 2*p1 - d) / (2*a) )
     *
     * @param x from 0 to d
     * @param d length of the chain
     * @param h height at x=d
     * @return y
     */
    public static double drip2(final double x, final double d, final double h) {
        double a = 20;
        a = a + (d * 0.3);
        final double p1 = a * asinh((h / (2D * a)) * (1D / Math.sinh(d / (2D * a))));
        final double p2 = -a * Math.cosh((2D * p1 - d) / (2D * a));
        return p2 + a * Math.cosh((((2D * x) + (2D * p1)) - d) / (2D * a));
    }

    private static double asinh(final double x) {
        return Math.log(x + Math.sqrt(x * x + 1.0));
    }

    /**
     * Fancy math, it deals with the rotation of the x,y,z coordinates based on the direction of the chain. So that
     * in every direction the pixels look the same size.
     *
     */
    private static float[] rotator(final double x, final double y, final double z) {
        final double x2 = x * x;
        final double z2 = z * z;
        final double zx = Math.sqrt(x2 + z2);
        final double arc1 = Math.atan2(y, zx);
        final double arc2 = Math.atan2(x, z);
        final double d = Math.sin(arc1) * 0.0125F;
        final float y_new = (float) (Math.cos(arc1) * 0.0125F);
        final float z_new = (float) (Math.cos(arc2) * d);
        float x_new = (float) (Math.sin(arc2) * d);
        float v = 0.0F;
        if (zx == 0.0F) {
            x_new = z_new;
            v = 1.0F;
        }
        return new float[]{x_new, y_new, z_new, v};
    }


    protected int getBlockLight(@NotNull final Entity entity, final BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.level().getBrightness(LightLayer.BLOCK, pos);
    }
}
