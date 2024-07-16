package TCOTS.blocks.entity;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.screen.AlchemyTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class AlchemyTableBlockEntity extends BlockEntity implements GeoBlockEntity, ExtendedScreenHandlerFactory  {

//    private static final int INGREDIENT_SLOT_1  = 0;
//    private static final int INGREDIENT_SLOT_2  = 1;
//    private static final int INGREDIENT_SLOT_3  = 2;
//    private static final int INGREDIENT_SLOT_4  = 3;
//    private static final int INGREDIENT_SLOT_5  = 4;
//    private static final int BASE_POTION_SLOT   = 5;
//    private static final int OUTPUT_POTION_SLOT = 6;



    //Gecko stuff
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, pos, state);
    }

//    public int getChangeCount() {
//        return changeCount;
//    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state ->{
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }));
    }




    //Crafting stuff

//    public static void tick(@NotNull World world, BlockPos pos, BlockState state, AlchemyTableBlockEntity blockEntity) {
//
//        if(world.isClient()) {
//            return;
//        }
//
////        if(blockEntity.hasCraftableRecipe()) {
//////            blockEntity.increaseCraftProgress();
////            markDirty(world, pos, state);
////
////            blockEntity.putCraftingOutput();
////
//////            if(blockEntity.hasCraftingFinished()) {
//////                blockEntity.craftItem();
//////                blockEntity.resetProgress();
//////            }
////        } else {
//////            blockEntity.resetProgress();
////            blockEntity.setStack(OUTPUT_POTION_SLOT, ItemStack.EMPTY);
////        }
//
//    }

//    private void putCraftingOutput() {
//
//        SimpleInventory inventory = new SimpleInventory(this.size());
//
//        for (int i = 0; i < this.size(); i++) {
//            inventory.setStack(i, this.getStack(i));
//        }
//        Optional<RecipeEntry<AlchemyTableRecipe>> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager()
//                .getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, this.getWorld());
//
//        if (recipe.isEmpty()) return;
//
//
//        this.setStack(OUTPUT_POTION_SLOT, new ItemStack(recipe.get().value().getResult(null).getItem(), recipe.get().value().getResult(null).getCount()));
//    }

//    private int size(){
//        return 7;
//    }

//    private boolean hasCraftableRecipe() {
//        SimpleInventory inventory = new SimpleInventory(this.size());
//
//        for (int i = 0; i < this.size(); i++) {
//            inventory.setStack(i, this.getStack(i));
//        }
//
//        Optional<RecipeEntry<AlchemyTableRecipe>> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager().getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, getWorld());
//
//
//        return recipe.isPresent()
////                 && serverPlayer != null
////                 && serverPlayer.getRecipeBook().contains(recipe.get())
//                ;
//    }

//    private void craftItem() {
//        if(hasCraftableRecipe()) {
//            SimpleInventory inventory = new SimpleInventory(this.size());
//
//                for (int i = 0; i < this.size(); i++) {
//                    inventory.setStack(i, this.getStack(i));
//                }
//
//            Optional<RecipeEntry<AlchemyTableRecipe>> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager()
//                    .getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, this.getWorld());
//
//
//            if(this.getStack(INGREDIENT_SLOT_1) != null ){
//                this.setStack(INGREDIENT_SLOT_1, ItemStack.EMPTY);
//            }
//
//            if(this.getStack(INGREDIENT_SLOT_2) != null){
//                this.setStack(INGREDIENT_SLOT_2, ItemStack.EMPTY);
//            }
//
//            if(this.getStack(INGREDIENT_SLOT_3) != null){
//                this.setStack(INGREDIENT_SLOT_3, ItemStack.EMPTY);
//            }
//
//            if(this.getStack(INGREDIENT_SLOT_4) != null){
//                this.setStack(INGREDIENT_SLOT_4, ItemStack.EMPTY);
//            }
//
//            if(this.getStack(INGREDIENT_SLOT_5) != null){
//                this.setStack(INGREDIENT_SLOT_5, ItemStack.EMPTY);
//            }
//
//            if(this.getStack(BASE_POTION_SLOT) != null){
//                this.setStack(BASE_POTION_SLOT, ItemStack.EMPTY);
//            }
//            if(recipe.isPresent()) {
//                this.setStack(OUTPUT_POTION_SLOT, new ItemStack(recipe.get().value().getResult(null).getItem(), recipe.get().value().getResult(null).getCount()));
//                assert world != null;
//                world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
//            }
//        }
//    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.tcots-witcher.alchemy_table");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AlchemyTableScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(this.getWorld(), pos), this);
    }
}
