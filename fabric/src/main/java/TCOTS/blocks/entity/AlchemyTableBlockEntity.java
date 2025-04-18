package TCOTS.blocks.entity;

import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.screen.AlchemyTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AlchemyTableBlockEntity extends BlockEntity implements GeoBlockEntity, ExtendedScreenHandlerFactory<BlockPos> {

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer player) {
        return this.worldPosition;
    }

    public record AlchemyBlockData(BlockPos pos) {
        public static final StreamCodec<RegistryFriendlyByteBuf, BlockPos> PACKET_CODEC = StreamCodec.of(
                AlchemyBlockData::write,
                AlchemyBlockData::read
        );


        public static void write(RegistryFriendlyByteBuf buf, BlockPos pos) {
            buf.writeBlockPos(pos);
        }


        public static BlockPos read(RegistryFriendlyByteBuf buf) {
            return new BlockPos(buf.readBlockPos());
        }

    }

    //Gecko stuff
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AlchemyTableBlockEntity(BlockEntityType<?> entityType,BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state ->{
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.tcots_witcher.alchemy_table");
    }

    @Override
    public RecipeBookMenu<AlchemyTableRecipe.AlchemyTableInventory, AlchemyTableRecipe> createMenu(int syncId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new AlchemyTableScreenHandler(syncId, playerInventory, ContainerLevelAccess.create(this.getLevel(), worldPosition), this);
    }



}
