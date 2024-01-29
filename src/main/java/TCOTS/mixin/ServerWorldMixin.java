package TCOTS.mixin;

import TCOTS.entity.necrophages.RotfiendEntity;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.map.MapState;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.entity.EntityLookup;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.minecraft.world.tick.QueryableTickScheduler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public class ServerWorldMixin extends World implements StructureWorldAccess {

    @Final
    @Shadow
    List<ServerPlayerEntity> players = Lists.newArrayList();

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "createExplosion", at = @At("HEAD"), cancellable = true)
    public void injectRotfiend(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir){
        if(entity instanceof RotfiendEntity) {
            Explosion explosion = this.createExplosion(entity, damageSource, behavior, x, y, z, power, createFire, explosionSourceType, false);
            if (!explosion.shouldDestroy()) {
                explosion.clearAffectedBlocks();
            }

            //InnerCode to differentiate the normal Explosions and RotfiendExplosions
             ObjectArrayList<BlockPos> affectedBlocks_InnerCode = new ObjectArrayList();

            for (int i = 0; i < 5; i++) {
                affectedBlocks_InnerCode.add(new BlockPos(0, 0, 0));
            }

            for (ServerPlayerEntity serverPlayerEntity : this.players) {
                if (!(serverPlayerEntity.squaredDistanceTo(x, y, z) < 4096.0)) continue;
                serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(x, y, z, power, affectedBlocks_InnerCode, explosion.getAffectedPlayers().get(serverPlayerEntity)));
            }
            cir.setReturnValue(explosion);
        }
    }

    @Shadow
    public long getSeed() {
        return 0;
    }

    @Shadow
    public ServerWorld toServerWorld() {
        return null;
    }

    @Shadow
    public void updateListeners(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Shadow
    public void playSound(@Nullable PlayerEntity except, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed) {

    }

    @Shadow
    public void playSoundFromEntity(@Nullable PlayerEntity except, Entity entity, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, long seed) {

    }

    @Shadow
    public String asString() {
        return null;
    }

    @Nullable
    @Shadow
    public Entity getEntityById(int id) {
        return null;
    }

    @Nullable
    @Shadow
    public MapState getMapState(String id) {
        return null;
    }

    @Shadow
    public void putMapState(String id, MapState state) {

    }

    @Shadow
    public int getNextMapId() {
        return 0;
    }

    @Shadow
    public void setBlockBreakingInfo(int entityId, BlockPos pos, int progress) {

    }

    @Shadow
    public Scoreboard getScoreboard() {
        return null;
    }

    @Shadow
    public RecipeManager getRecipeManager() {
        return null;
    }

    @Shadow
    protected EntityLookup<Entity> getEntityLookup() {
        return null;
    }

    @Shadow
    public QueryableTickScheduler<Block> getBlockTickScheduler() {
        return null;
    }

    @Shadow
    public QueryableTickScheduler<Fluid> getFluidTickScheduler() {
        return null;
    }

    @Shadow
    public ChunkManager getChunkManager() {
        return null;
    }

    @Shadow
    public void syncWorldEvent(@Nullable PlayerEntity player, int eventId, BlockPos pos, int data) {

    }

    @Shadow
    public void emitGameEvent(GameEvent event, Vec3d emitterPos, GameEvent.Emitter emitter) {

    }

    @Shadow
    public float getBrightness(Direction direction, boolean shaded) {
        return 0;
    }

    @Shadow
    public List<? extends PlayerEntity> getPlayers() {
        return null;
    }

    @Shadow
    public RegistryEntry<Biome> getGeneratorStoredBiome(int biomeX, int biomeY, int biomeZ) {
        return null;
    }

    @Shadow
    public FeatureSet getEnabledFeatures() {
        return null;
    }
}
