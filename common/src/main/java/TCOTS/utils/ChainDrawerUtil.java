package TCOTS.utils;

import TCOTS.TCOTS_Main;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 Logic based in the mod <a href="https://github.com/legoatoom/ConnectibleChains/tree/main">Connectible Chains</a> by <a href="https://www.curseforge.com/members/legoatoom/projects">legoatoom</a>
 */
public class ChainDrawerUtil {

    private static final ResourceLocation TEXTURE = ResourceLocation.parse("textures/block/chain.png");


    public static void renderChain(final Entity fromEntity, final float tickDelta, final PoseStack matrixStack, final MultiBufferSource vertexConsumerProvider, final Entity toEntity) {
        matrixStack.pushPose();

        final double lerpBodyAngle = (Mth.lerp(tickDelta, fromEntity.yRotO, fromEntity.getVisualRotationYInDegrees()) * Mth.DEG_TO_RAD) + Mth.HALF_PI;
        final Vec3 leashOffsetO = new Vec3(0, 0, 0);
        final double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffsetO.z + Math.sin(lerpBodyAngle) * leashOffsetO.x;
        final double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffsetO.z - Math.cos(lerpBodyAngle) * leashOffsetO.x;

        final Vec3 dstPos = toEntity.getRopeHoldPosition(tickDelta);
        //The leash pos offset
        final Vec3 leashOffset = getLeashOffset(fromEntity);
        matrixStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

        final double lerpOriginX = Mth.lerp(tickDelta, fromEntity.xo, fromEntity.getX()) + xAngleOffset;
        final double lerpOriginY = Mth.lerp(tickDelta, fromEntity.yo, fromEntity.getY()) + leashOffset.y;
        final double lerpOriginZ = Mth.lerp(tickDelta, fromEntity.zo, fromEntity.getZ()) + zAngleOffset;
        final float xDif = (float)(dstPos.x - lerpOriginX);
        final float yDif = (float)(dstPos.y - lerpOriginY);
        final float zDif = (float)(dstPos.z - lerpOriginZ);

        final VertexConsumer buffer = vertexConsumerProvider.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));

        // Now we gather light information for the chain. Since the chain is lighter if there is more light.
        final BlockPos blockPosOfStart = BlockPos.containing(fromEntity.getEyePosition(tickDelta));
        final BlockPos blockPosOfEnd = BlockPos.containing(toEntity.getEyePosition(tickDelta));
        final int blockLightLevelOfStart = fromEntity.level().getBrightness(LightLayer.BLOCK, blockPosOfStart);
        final int blockLightLevelOfEnd = toEntity.level().getBrightness(LightLayer.BLOCK, blockPosOfEnd);
        final int skylightLevelOfStart = fromEntity.level().getBrightness(LightLayer.SKY, blockPosOfStart);
        final int skylightLevelOfEnd = fromEntity.level().getBrightness(LightLayer.SKY, blockPosOfEnd);

        final Vector3f chainVec =
                new Vector3f(
                        xDif,
                        yDif,
                        zDif);

        final float angleY = -(float) Math.atan2(chainVec.z(), chainVec.x());
        matrixStack.mulPose(new Quaternionf().rotateXYZ(0, angleY, 0));

        final ChainModel model = ChainRenderer.buildModel(chainVec);
        model.render(buffer, matrixStack, blockLightLevelOfStart, blockLightLevelOfEnd, skylightLevelOfStart, skylightLevelOfEnd);

        matrixStack.popPose();
    }


    @SuppressWarnings("unused")
    protected static Vec3 getLeashOffset(final Entity entity) {
        return new Vec3(0.0, 0.0, 0.0);
    }

    /**
 * The geometry is baked (converted to an efficient format) into vertex and uv arrays.
 * This prevents having to recalculate the model every frame.
 */
 public record ChainModel(float[] vertices, float[] uvs) {

    public static Builder builder(final int initialCapacity) {
        return new Builder(initialCapacity);
    }

    /**
     * Writes the model data to {@code buffer} and applies lighting.
     *
     * @param vertexConsumer   The target buffer.
     * @param matrices The transformation stack
     * @param bLight0  Block-light at the start.
     * @param bLight1  Block-light at the end.
     * @param sLight0  Sky-light at the start.
     * @param sLight1  Sky-light at the end.
     */
    public void render(final VertexConsumer vertexConsumer, final PoseStack matrices, final int bLight0, final int bLight1, final int sLight0, final int sLight1) {
        final Matrix4f modelMatrix = matrices.last().pose();
        final int count = vertices.length / 3;
        for (int i = 0; i < count; i++) {
            // divide by 2 because chain has 2 face sets
            final float f = (i % (count / 2f)) / (count / 2f);
            final int blockLight = (int) Mth.lerp(f, (float) bLight0, (float) bLight1);
            final int skyLight = (int) Mth.lerp(f, (float) sLight0, (float) sLight1);
            final int light = LightTexture.pack(blockLight, skyLight);

            vertexConsumer
                    .addVertex(modelMatrix, vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2])
                    .setColor(255, 255, 255, 255)
                    .setUv(uvs[i * 2], uvs[i * 2 + 1])
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    // trial and error magic values that change the overall brightness of the chain
                    .setNormal(1, 0.35f, 0);
        }
    }

    public static class Builder {
        private final List<Float> vertices;
        private final List<Float> uvs;
        private int size;

        public Builder(final int initialCapacity) {
            vertices = new ArrayList<>(initialCapacity * 3);
            uvs = new ArrayList<>(initialCapacity * 2);
        }

        public Builder vertex(final Vector3f v) {
            vertices.add(v.x());
            vertices.add(v.y());
            vertices.add(v.z());
            return this;
        }

        public Builder uv(final float u, final float v) {
            uvs.add(u);
            uvs.add(v);
            return this;
        }

        public void next() {
            size++;
        }

        public ChainModel build() {
            if (vertices.size() != size * 3) TCOTS_Main.LOGGER.error("Wrong count of vertices");
            if (uvs.size() != size * 2) TCOTS_Main.LOGGER.error("Wrong count of uvs");

            return new ChainModel(toFloatArray(vertices), toFloatArray(uvs));
        }

        private float[] toFloatArray(final List<Float> floats) {
            final float[] array = new float[floats.size()];
            int i = 0;

            for (final float f : floats) {
                array[i++] = f;
            }

            return array;
        }
    }
}


public static class ChainRenderer {
    /**
     * Changes the width of the chain but does not cause uv distortion.
     */
    private static final float CHAIN_SCALE = 1f;
    /**
     * How many mesh segments a chain is allowed to have.
     * This is to prevent extreme lag and the possibility of an infinite loop.
     */
    private static final int MAX_SEGMENTS = 2048;

    private static final float quality = 4f;

    /**
     * Generates a new baked chain model for the given vector.
     *
     * @param chainVec The vector from the chain start to the end
     * @return The generated model
     */
    public static ChainModel buildModel(final Vector3f chainVec) {
        final float desiredSegmentLength = 1f / quality;
        final int initialCapacity = (int) (2f * chainVec.lengthSquared() / desiredSegmentLength);
        final ChainModel.Builder builder = ChainModel.builder(initialCapacity);

        if (Float.isNaN(chainVec.x()) && Float.isNaN(chainVec.z())) {
            buildFaceVertical(builder, chainVec, 45, UVRect.DEFAULT_SIDE_A);
            buildFaceVertical(builder, chainVec, -45, UVRect.DEFAULT_SIDE_B);
        } else {
            buildFace(builder, chainVec, 45, UVRect.DEFAULT_SIDE_A);
            buildFace(builder, chainVec, -45, UVRect.DEFAULT_SIDE_B);
        }

        return builder.build();
    }

    /**
     * {@link #buildFace} does not work when {@code chainVec} is pointing straight up or down.
     */
    public static void buildFaceVertical(final ChainModel.Builder builder, final Vector3f chainVec, final float angle, final UVRect uv) {
        chainVec.x = 0;
        chainVec.z = 0;

        float actualSegmentLength = 1f / quality;
        final float chainWidth = (uv.x1() - uv.x0()) / 16 * CHAIN_SCALE;

        final Vector3f normal = new Vector3f((float) Math.cos(Math.toRadians(angle)), 0, (float) Math.sin(Math.toRadians(angle)));
        normal.normalize(chainWidth);

        final Vector3f vert00 = new Vector3f(
                -normal.x() / 2,
                0,
                -normal.z() / 2);
        final Vector3f vert01 = new Vector3f(vert00);

        final Vector3f vert10 = new Vector3f(
                -normal.x() / 2,
                0,
                -normal.z() / 2);
        final Vector3f vert11 = new Vector3f(vert10);

        float uvv0 = 0, uvv1 = 0;
        boolean lastIter = false;
        for (int segment = 0; segment < MAX_SEGMENTS; segment++) {
            if (vert00.y() + actualSegmentLength >= chainVec.y()) {
                lastIter = true;
                actualSegmentLength = chainVec.y() - vert00.y();
            }

            vert10.add(0, actualSegmentLength, 0);
            vert11.add(0, actualSegmentLength, 0);

            uvv1 += actualSegmentLength / CHAIN_SCALE;

            builder.vertex(vert00).uv(uv.x0() / 16f, uvv0).next();
            builder.vertex(vert01).uv(uv.x1() / 16f, uvv0).next();
            builder.vertex(vert11).uv(uv.x1() / 16f, uvv1).next();
            builder.vertex(vert10).uv(uv.x0() / 16f, uvv1).next();

            if (lastIter) break;

            uvv0 = uvv1;

            vert00.set(vert10);
            vert01.set(vert11);
        }
    }

    /**
     * Creates geometry from the origin to {@code chainVec} with the specified {@code angle}.
     * It uses an iterative approach meaning that it adds geometry until it's at the end or
     * has reached {@link #MAX_SEGMENTS}.
     * The model is always generated along the local X axis and curves along the Y axis.
     * This makes the calculation a lot simpler as we are only dealing with 2d coordinates.
     *
     * @param builder The target builder
     * @param chainVec       The end position in relation to the origin
     * @param angle   The angle of the face
     * @param uv      The uv bounds of the face
     */
    public static void buildFace(final ChainModel.Builder builder, final Vector3f chainVec, final float angle, final UVRect uv) {
        float actualSegmentLength;
        final float desiredSegmentLength = 1f / quality;
        final float distance = chainVec.length();
        final float distanceXZ = (float) Math.sqrt(Math.fma(chainVec.x(), chainVec.x(), chainVec.z() * chainVec.z()));
        // Original code used total distance between start and end instead of horizontal distance
        // That changed the look of chains when there was a big height difference, but it looks better.
        final float wrongDistanceFactor = distance / distanceXZ;

        // 00, 01, 11, 11 refers to the X and Y position of the vertex.
        // 00 is the lower X and Y vertex. 10 Has the same y value as 00 but a higher x value.
        final Vector3f vert00 = new Vector3f();
        Vector3f vert01 = new Vector3f();
        Vector3f vert11 = new Vector3f();
        final Vector3f vert10 = new Vector3f();
        final Vector3f normal = new Vector3f();
        final Vector3f rotAxis = new Vector3f();

        final float chainWidth = (uv.x1() - uv.x0()) / 16 * CHAIN_SCALE;
        //
        float uvv0, uvv1 = 0, gradient, x, y;
        final Vector3f point0 = new Vector3f();
        final Vector3f point1 = new Vector3f();
        Quaternionf rotator = new Quaternionf();

        // All of this setup can probably go, but I can't figure out
        // how to integrate it into the loop :shrug:
        point0.set(0, (float) drip2(0, distance, chainVec.y()), 0);
        gradient = (float) drip2prime(0, distance, chainVec.y());
        normal.set(-gradient, Math.abs(distanceXZ / distance), 0);
        normal.normalize();

        x = estimateDeltaX(desiredSegmentLength, gradient);
        gradient = (float) drip2prime(x * wrongDistanceFactor, distance, chainVec.y());
        y = (float) drip2(x * wrongDistanceFactor, distance, chainVec.y());
        point1.set(x, y, 0);

        rotAxis.set(point1.x() - point0.x(), point1.y() - point0.y(), point1.z() - point0.z());
        rotAxis.normalize();
        rotator.fromAxisAngleDeg(rotAxis, angle);


        normal.rotate(rotator);
        normal.normalize(chainWidth);
        vert10.set(point0.x() - normal.x() / 2, point0.y() - normal.y() / 2, point0.z() - normal.z() / 2);
        vert11.set(vert10);
        vert11.add(normal);


        actualSegmentLength = point0.distance(point1);

        // This is a pretty simple algorithm to convert the mathematical curve to a model.
        // It uses an incremental approach, adding segments until the end is reached.
        boolean lastIter = false;
        for (int segment = 0; segment < MAX_SEGMENTS; segment++) {
            rotAxis.set(point1.x() - point0.x(), point1.y() - point0.y(), point1.z() - point0.z());
            rotAxis.normalize();
            rotator = rotator.fromAxisAngleDeg(rotAxis, angle);

            // This normal is orthogonal to the face normal
            normal.set(-gradient, Math.abs(distanceXZ / distance), 0);
            normal.normalize();
            normal.rotate(rotator);
            normal.normalize(chainWidth);

            vert00.set(vert10);
            vert01.set(vert11);

            vert10.set(point1.x() - normal.x() / 2, point1.y() - normal.y() / 2, point1.z() - normal.z() / 2);
            vert11.set(vert10);
            vert11.add(normal);

            uvv0 = uvv1;
            uvv1 = uvv0 + actualSegmentLength / CHAIN_SCALE;

            builder.vertex(vert00).uv(uv.x0() / 16f, uvv0).next();
            builder.vertex(vert01).uv(uv.x1() / 16f, uvv0).next();
            builder.vertex(vert11).uv(uv.x1() / 16f, uvv1).next();
            builder.vertex(vert10).uv(uv.x0() / 16f, uvv1).next();

            if (lastIter) break;

            point0.set(point1);

            x += estimateDeltaX(desiredSegmentLength, gradient);
            if (x >= distanceXZ) {
                lastIter = true;
                x = distanceXZ;
            }

            gradient = (float) drip2prime(x * wrongDistanceFactor, distance, chainVec.y());
            y = (float) drip2(x * wrongDistanceFactor, distance, chainVec.y());
            point1.set(x, y, 0);

            actualSegmentLength = point0.distance(point1);
        }
    }

    /**
     * Estimate Δx based on current gradient to get segments with equal length
     * k ... Gradient
     * T ... Tangent
     * s ... Segment Length
     * <p>
     * T = (1, k)
     * <p>
     * Δx = (s * T / |T|).x
     * Δx = s * T.x / |T|
     * Δx = s * 1 / |T|
     * Δx = s / |T|
     * Δx = s / √(1^2 + k^2)
     * Δx = s / √(1 + k^2)
     *
     * @param s the desired segment length
     * @param k the gradient
     * @return Δx
     */
    private static float estimateDeltaX(final float s, final float k) {
        return (float) (s / Math.sqrt(1 + k * k));
    }


    /**
     * Derivative of drip2
     * For geogebra:
     * f'(x) = sinh( (2*x + 2*p1 - d) / (2*a) )
     *
     * @param x from 0 to d
     * @param d length of the chain
     * @param h height at x=d
     * @return gradient at x
     */
    public static double drip2prime(final double x, final double d, final double h) {
        final double a = 7;
        final double p1 = a * asinh((h / (2D * a)) * (1D / Math.sinh(d / (2D * a))));
        return Math.sinh((2 * x + 2 * p1 - d) / (2 * a));
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
}

/**
 * Specifies the uv coordinates that the renderer should use.
 * The chain texture has to be vertical for now.
 * This is a leftover and serves no real function
 */

public record UVRect(float x0, float x1) {
    /**
     * Default UV's for side A
     */
    public static final UVRect DEFAULT_SIDE_A = new UVRect(0, 3);
    /**
     * Default UV's for side B
     */
    public static final UVRect DEFAULT_SIDE_B = new UVRect(3, 6);
}

}
