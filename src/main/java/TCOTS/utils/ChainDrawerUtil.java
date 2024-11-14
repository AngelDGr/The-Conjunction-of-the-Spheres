package TCOTS.utils;

import TCOTS.TCOTS_Main;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LightType;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 Logic based in the mod <a href="https://github.com/legoatoom/ConnectibleChains/tree/main">Connectible Chains</a> by <a href="https://www.curseforge.com/members/legoatoom/projects">legoatoom</a>
 */
public class ChainDrawerUtil {

    private static final Identifier TEXTURE = Identifier.of("textures/block/chain.png");


    public static void renderChain(Entity fromEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Entity toEntity) {
        matrixStack.push();

        double lerpBodyAngle = (MathHelper.lerp(tickDelta, fromEntity.prevYaw, fromEntity.getBodyYaw()) * MathHelper.RADIANS_PER_DEGREE) + MathHelper.HALF_PI;
        Vec3d leashOffsetO = new Vec3d(0, 0, 0);
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffsetO.z + Math.sin(lerpBodyAngle) * leashOffsetO.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffsetO.z - Math.cos(lerpBodyAngle) * leashOffsetO.x;

        Vec3d dstPos = toEntity.getLeashPos(tickDelta);
        //The leash pos offset
        Vec3d leashOffset = getLeashOffset(fromEntity);
        matrixStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);

        double lerpOriginX = MathHelper.lerp(tickDelta, fromEntity.prevX, fromEntity.getX()) + xAngleOffset;
        double lerpOriginY = MathHelper.lerp(tickDelta, fromEntity.prevY, fromEntity.getY()) + leashOffset.y;
        double lerpOriginZ = MathHelper.lerp(tickDelta, fromEntity.prevZ, fromEntity.getZ()) + zAngleOffset;
        float xDif = (float)(dstPos.x - lerpOriginX);
        float yDif = (float)(dstPos.y - lerpOriginY);
        float zDif = (float)(dstPos.z - lerpOriginZ);

        VertexConsumer buffer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));

        // Now we gather light information for the chain. Since the chain is lighter if there is more light.
        BlockPos blockPosOfStart = BlockPos.ofFloored(fromEntity.getCameraPosVec(tickDelta));
        BlockPos blockPosOfEnd = BlockPos.ofFloored(toEntity.getCameraPosVec(tickDelta));
        int blockLightLevelOfStart = fromEntity.getWorld().getLightLevel(LightType.BLOCK, blockPosOfStart);
        int blockLightLevelOfEnd = toEntity.getWorld().getLightLevel(LightType.BLOCK, blockPosOfEnd);
        int skylightLevelOfStart = fromEntity.getWorld().getLightLevel(LightType.SKY, blockPosOfStart);
        int skylightLevelOfEnd = fromEntity.getWorld().getLightLevel(LightType.SKY, blockPosOfEnd);

        Vector3f chainVec =
                new Vector3f(
                        xDif,
                        yDif,
                        zDif);

        float angleY = -(float) Math.atan2(chainVec.z(), chainVec.x());
        matrixStack.multiply(new Quaternionf().rotateXYZ(0, angleY, 0));

        ChainModel model = ChainRenderer.buildModel(chainVec);
        model.render(buffer, matrixStack, blockLightLevelOfStart, blockLightLevelOfEnd, skylightLevelOfStart, skylightLevelOfEnd);

        matrixStack.pop();
    }


    @SuppressWarnings("unused")
    protected static Vec3d getLeashOffset(Entity entity) {
        return new Vec3d(0.0, 0.0, 0.0);
    }

    /**
 * The geometry is baked (converted to an efficient format) into vertex and uv arrays.
 * This prevents having to recalculate the model every frame.
 */
 public record ChainModel(float[] vertices, float[] uvs) {

    public static Builder builder(int initialCapacity) {
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
    public void render(VertexConsumer vertexConsumer, MatrixStack matrices, int bLight0, int bLight1, int sLight0, int sLight1) {
        Matrix4f modelMatrix = matrices.peek().getPositionMatrix();
        int count = vertices.length / 3;
        for (int i = 0; i < count; i++) {
            // divide by 2 because chain has 2 face sets
            float f = (i % (count / 2f)) / (count / 2f);
            int blockLight = (int) MathHelper.lerp(f, (float) bLight0, (float) bLight1);
            int skyLight = (int) MathHelper.lerp(f, (float) sLight0, (float) sLight1);
            int light = LightmapTextureManager.pack(blockLight, skyLight);

            vertexConsumer
                    .vertex(modelMatrix, vertices[i * 3], vertices[i * 3 + 1], vertices[i * 3 + 2])
                    .color(255, 255, 255, 255)
                    .texture(uvs[i * 2], uvs[i * 2 + 1])
                    .overlay(OverlayTexture.DEFAULT_UV)
                    .light(light)
                    // trial and error magic values that change the overall brightness of the chain
                    .normal(1, 0.35f, 0);
        }
    }

    public static class Builder {
        private final List<Float> vertices;
        private final List<Float> uvs;
        private int size;

        public Builder(int initialCapacity) {
            vertices = new ArrayList<>(initialCapacity * 3);
            uvs = new ArrayList<>(initialCapacity * 2);
        }

        public Builder vertex(Vector3f v) {
            vertices.add(v.x());
            vertices.add(v.y());
            vertices.add(v.z());
            return this;
        }

        public Builder uv(float u, float v) {
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

        private float[] toFloatArray(List<Float> floats) {
            float[] array = new float[floats.size()];
            int i = 0;

            for (float f : floats) {
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
    public static ChainModel buildModel(Vector3f chainVec) {
        float desiredSegmentLength = 1f / quality;
        int initialCapacity = (int) (2f * chainVec.lengthSquared() / desiredSegmentLength);
        ChainModel.Builder builder = ChainModel.builder(initialCapacity);

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
    public static void buildFaceVertical(ChainModel.Builder builder, Vector3f chainVec, float angle, UVRect uv) {
        chainVec.x = 0;
        chainVec.z = 0;

        float actualSegmentLength = 1f / quality;
        float chainWidth = (uv.x1() - uv.x0()) / 16 * CHAIN_SCALE;

        Vector3f normal = new Vector3f((float) Math.cos(Math.toRadians(angle)), 0, (float) Math.sin(Math.toRadians(angle)));
        normal.normalize(chainWidth);

        Vector3f vert00 = new Vector3f(
                -normal.x() / 2,
                0,
                -normal.z() / 2),
                vert01 = new Vector3f(vert00);

        Vector3f vert10 = new Vector3f(
                -normal.x() / 2,
                0,
                -normal.z() / 2),
                vert11 = new Vector3f(vert10);

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
    public static void buildFace(ChainModel.Builder builder, Vector3f chainVec, float angle, UVRect uv) {
        float actualSegmentLength, desiredSegmentLength = 1f / quality;
        float distance = chainVec.length(), distanceXZ = (float) Math.sqrt(Math.fma(chainVec.x(), chainVec.x(), chainVec.z() * chainVec.z()));
        // Original code used total distance between start and end instead of horizontal distance
        // That changed the look of chains when there was a big height difference, but it looks better.
        float wrongDistanceFactor = distance / distanceXZ;

        // 00, 01, 11, 11 refers to the X and Y position of the vertex.
        // 00 is the lower X and Y vertex. 10 Has the same y value as 00 but a higher x value.
        Vector3f vert00 = new Vector3f(), vert01 = new Vector3f(), vert11 = new Vector3f(), vert10 = new Vector3f();
        Vector3f normal = new Vector3f(), rotAxis = new Vector3f();

        float chainWidth = (uv.x1() - uv.x0()) / 16 * CHAIN_SCALE;
        //
        float uvv0, uvv1 = 0, gradient, x, y;
        Vector3f point0 = new Vector3f(), point1 = new Vector3f();
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
    private static float estimateDeltaX(float s, float k) {
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
    public static double drip2prime(double x, double d, double h) {
        double a = 7;
        double p1 = a * asinh((h / (2D * a)) * (1D / Math.sinh(d / (2D * a))));
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
}

/**
 * Specifies the uv coordinates that the renderer should use.
 * The chain texture has to be vertical for now.
 *
 * @implNote This is a leftover and serves no real function
 */
@Environment(EnvType.CLIENT)
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
