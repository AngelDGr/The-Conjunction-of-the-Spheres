package TCOTS.utils;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class OldChainDrawer {

    /**
     * If I am honest I do not really know what is happening here most of the time, most of the code was 'inspired' by
     * the {@link net.minecraft.client.render.entity.LeashKnotEntityRenderer}.
     * Many variables therefore have simple names. I tried my best to comment and explain what everything does.
     *
     * @param fromEntity             The origin Entity
     * @param tickDelta              Delta tick
     * @param matrixStack               The render matrix stack.
     * @param vertexConsumerProvider The VertexConsumerProvider, whatever it does.
     * @param toEntity               The entity that we connect the chain to, this can be a {PlayerEntity} or a {ChainKnotEntity}.
     */
    private void createChainLine(Entity fromEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Entity toEntity) {

        double lerpBodyAngle = (MathHelper.lerp(tickDelta, fromEntity.prevYaw, fromEntity.getBodyYaw()) * MathHelper.RADIANS_PER_DEGREE) + MathHelper.HALF_PI;
        Vec3d leashOffset = new Vec3d(0, 0, 0);
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;

        Vec3d ropeGripPosition = toEntity.getLeashPos(tickDelta);

        double lerpOriginX = MathHelper.lerp(tickDelta, fromEntity.prevX, fromEntity.getX()) + xAngleOffset;
        double lerpOriginY = MathHelper.lerp(tickDelta, fromEntity.prevY, fromEntity.getY()) + leashOffset.y;
        double lerpOriginZ = MathHelper.lerp(tickDelta, fromEntity.prevZ, fromEntity.getZ()) + zAngleOffset;

        float lerpDistanceX = (float) (ropeGripPosition.x - lerpOriginX);
        float lerpDistanceY = (float) (ropeGripPosition.y - lerpOriginY);
        float lerpDistanceZ = (float) (ropeGripPosition.z - lerpOriginZ);
        //Create offset based on the location. Example that a line that does not travel in the x then the xOffset will be 0.
        float v = MathHelper.inverseSqrt(lerpDistanceX * lerpDistanceX + lerpDistanceZ * lerpDistanceZ) * 0.025F / 2;
        float xOffset = lerpDistanceZ * v;
        float zOffset = lerpDistanceX * v;

        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getLeash());

        matrixStack.push(); // We push here to start new I think.
        matrixStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

        // Now we gather light information for the chain. Since the chain is lighter if there is more light.
        BlockPos entityEyePos = BlockPos.ofFloored(fromEntity.getCameraPosVec(tickDelta));
        BlockPos holderEyePos = BlockPos.ofFloored(toEntity.getCameraPosVec(tickDelta));
        int entityBlockLight = getBlockLight(fromEntity, entityEyePos);
        int holderBlockLight = toEntity.isOnFire() ? 15 : toEntity.getWorld().getLightLevel(LightType.BLOCK, holderEyePos);
        int entitySkyLight = fromEntity.getWorld().getLightLevel(LightType.SKY, entityEyePos);
        int holderSkyLight = fromEntity.getWorld().getLightLevel(LightType.SKY, holderEyePos);

        float distance = toEntity.distanceTo(fromEntity);
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        //This number specifies the number of pixels on the chain.
        chainDrawer(distance, vertexConsumer, matrix4f, lerpDistanceX, lerpDistanceY, lerpDistanceZ,
                entityBlockLight,
                holderBlockLight,
                entitySkyLight,
                holderSkyLight,
                xOffset, zOffset);

        matrixStack.pop();
    }

    /**
     * This method is the big drawer of the chain.
     */
    @SuppressWarnings("DuplicatedCode")
    private void chainDrawer(float distance, VertexConsumer vertexConsumer, Matrix4f matrix4f,
                             float lerpDistanceX, float lerpDistanceY, float lerpDistanceZ,
                             int blockLightLevelOfStart, int blockLightLevelOfEnd,
                             int skylightLevelOfStart, int skylightLevelOfEnd,
                             float xOffset, float zOffset) {

        //Can you see the chain here?
        List<Integer> topLineA, middleLineA, bottomLineA, topLineB, middleLineB, bottomLineB;
        topLineA    = Arrays.asList(   1, 2, 3,       6, 7, 8, 9,         12, 13, 14);
        middleLineA = Arrays.asList(   1,    3,       6,       9,         12,     14);
        bottomLineA = Arrays.asList(   1, 2, 3,       6, 7, 8, 9,         12, 13, 14);

        topLineB    = Arrays.asList(0, 1,    3, 4, 5, 6,       9, 10, 11, 12,     14, 15);
        middleLineB = Arrays.asList(   1,    3,       6,       9,         12,     14    );
        bottomLineB = Arrays.asList(0, 1,    3, 4, 5, 6,       9, 10, 11, 12,     14, 15);

        int length = (int) Math.floor(distance * 48); //This number specifies the number of pixels on the chain.

        // LightLevel Stuff
        float s = (float) skylightLevelOfEnd / (length - 1);
        int t = (int) MathHelper.lerp(s, (float) blockLightLevelOfStart, (float) blockLightLevelOfEnd);
        int u = (int) MathHelper.lerp(s, (float) skylightLevelOfStart, (float) skylightLevelOfEnd);
        int pack = LightmapTextureManager.pack(t, u);

        for (int step = 0; step < length; ++step) {
            float startStepFraction = ((float) step / (float) length);
            float endStepFraction = ((float) (step + 1) / (float) length);
            float startDrip = (float) drip2(startStepFraction * distance, distance, lerpDistanceY);
            float endDrip = (float) drip2(endStepFraction * distance, distance, lerpDistanceY);

            float startRootX = lerpDistanceX * startStepFraction;
            float startRootZ = lerpDistanceZ * startStepFraction;
            float endRootX = lerpDistanceX * endStepFraction;
            float endRootZ = lerpDistanceZ * endStepFraction;
            float[] rotateStartEnd = rotator(startRootX - endRootX, (startDrip - endDrip), startRootZ - endRootZ);
            float v1 = (rotateStartEnd[3] != 1.0F) ? 1.0F : -1.0F;
            float R, G, B;

            float rotate0 = rotateStartEnd[0];
            float rotate1 = rotateStartEnd[1];
            float rotate2 = rotateStartEnd[2];
            // First Line
            float chainHeight = 0.0125F;
            if (topLineA.contains(step % 16)) {
                Vector3f startA, endA, startB, endB;
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
                Vector3f startA, endA, startB, endB;
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
                Vector3f startA, endA, startB, endB;
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
                Vector3f startA, endA, startB, endB;
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
                Vector3f startA, endA, startB, endB;
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
                Vector3f startA, endA, startB, endB;
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
    private static void renderPixel(Vector3f startA, Vector3f startB, Vector3f endA, Vector3f endB,
                                    VertexConsumer vertexConsumer, Matrix4f matrix4f, int lightPack,
                                    float R, float G, float B) {
        vertexConsumer
                .vertex(matrix4f, startA.x(), startA.y(), startA.z())
                .color(R, G, B, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(1, 0.35f, 0)

                .texture(0, (float)1/16)

                .light(lightPack);

        vertexConsumer
                .vertex(matrix4f, startB.x(), startB.y(), startB.z())
                .color(R, G, B, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(1, 0.35f, 0)

                .texture((float)2/16, (float)1/16)

                .light(lightPack);

        vertexConsumer
                .vertex(matrix4f, endB.x(), endB.y(), endB.z())
                .color(R, G, B, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(1, 0.35f, 0)

                .texture(0, (float)3/16)

                .light(lightPack);

        vertexConsumer
                .vertex(matrix4f, endA.x(), endA.y(), endA.z())
                .color(R, G, B, 1.0F)
                .overlay(OverlayTexture.DEFAULT_UV)
                .normal(1, 0.35f, 0)

                .texture((float)2/16, (float)3/16)

                .light(lightPack);

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
    public static double drip2(double x, double d, double h) {
        double a = 20;
        a = a + (d * 0.3);
        double p1 = a * asinh((h / (2D * a)) * (1D / Math.sinh(d / (2D * a))));
        double p2 = -a * Math.cosh((2D * p1 - d) / (2D * a));
        return p2 + a * Math.cosh((((2D * x) + (2D * p1)) - d) / (2D * a));
    }

    private static double asinh(double x) {
        return Math.log(x + Math.sqrt(x * x + 1.0));
    }

    /**
     * Fancy math, it deals with the rotation of the x,y,z coordinates based on the direction of the chain. So that
     * in every direction the pixels look the same size.
     *
     */
    private static float[] rotator(double x, double y, double z) {
        double x2 = x * x;
        double z2 = z * z;
        double zx = Math.sqrt(x2 + z2);
        double arc1 = Math.atan2(y, zx);
        double arc2 = Math.atan2(x, z);
        double d = Math.sin(arc1) * 0.0125F;
        float y_new = (float) (Math.cos(arc1) * 0.0125F);
        float z_new = (float) (Math.cos(arc2) * d);
        float x_new = (float) (Math.sin(arc2) * d);
        float v = 0.0F;
        if (zx == 0.0F) {
            x_new = z_new;
            v = 1.0F;
        }
        return new float[]{x_new, y_new, z_new, v};
    }


    protected int getBlockLight(@NotNull Entity entity, BlockPos pos) {
        if (entity.isOnFire()) {
            return 15;
        }
        return entity.getWorld().getLightLevel(LightType.BLOCK, pos);
    }
}
