package TCOTS.blocks.entity;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.screen.AlchemyTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AlchemyTableBlockEntity extends BlockEntity implements GeoBlockEntity, ExtendedScreenHandlerFactory<BlockPos> {

    @Override
    public BlockPos getScreenOpeningData(ServerPlayerEntity player) {
        return this.pos;
    }

    public record AlchemyBlockData(BlockPos pos) {
        public static final PacketCodec<RegistryByteBuf, BlockPos> PACKET_CODEC = PacketCodec.ofStatic(
                AlchemyBlockData::write,
                AlchemyBlockData::read
        );


        public static void write(RegistryByteBuf buf, BlockPos pos) {
            buf.writeBlockPos(pos);
        }


        public static BlockPos read(RegistryByteBuf buf) {
            return new BlockPos(buf.readBlockPos());
        }

    }

    //Gecko stuff
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, pos, state);
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
    public Text getDisplayName() {
        return Text.translatable("block.tcots-witcher.alchemy_table");
    }

    @Override
    public AbstractRecipeScreenHandler<AlchemyTableRecipe.AlchemyTableInventory, AlchemyTableRecipe> createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AlchemyTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.getWorld(), pos), this);
    }



}
