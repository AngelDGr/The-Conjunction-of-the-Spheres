package TCOTS.blocks.entity;

import TCOTS.recipes.AlchemyTableRecipe;
import TCOTS.screen.AlchemyTableScreenHandler;
import dev.architectury.registry.menu.ExtendedMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
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

public class AlchemyTableBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider, ExtendedMenuProvider {

    //Gecko stuff
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AlchemyTableBlockEntity(final BlockEntityType<?> entityType, final BlockPos pos, final BlockState state) {
        super(entityType, pos, state);
    }


    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
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
    public RecipeBookMenu<AlchemyTableRecipe.AlchemyTableInventory, AlchemyTableRecipe> createMenu(final int syncId, @NotNull final Inventory playerInventory, @NotNull final Player player) {
        return new AlchemyTableScreenHandler(syncId, playerInventory, ContainerLevelAccess.create(this.getLevel(), worldPosition), this);
    }

    @Override
    public void saveExtraData(final FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }
}
