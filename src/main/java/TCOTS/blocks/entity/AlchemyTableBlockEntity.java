package TCOTS.blocks.entity;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.potions.recipes.AlchemyTableRecipe;
import TCOTS.screen.AlchemyTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Objects;
import java.util.Optional;

public class AlchemyTableBlockEntity extends LockableContainerBlockEntity implements GeoBlockEntity, ExtendedScreenHandlerFactory, ImplementedInventory  {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

    private static final int INGREDIENT_SLOT_1  = 0;
    private static final int INGREDIENT_SLOT_2  = 1;
    private static final int INGREDIENT_SLOT_3  = 2;
    private static final int INGREDIENT_SLOT_4  = 3;
    private static final int INGREDIENT_SLOT_5  = 4;
    private static final int OUTPUT_POTION_SLOT = 5;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 160;


    //Gecko stuff
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(TCOTS_Blocks.ALCHEMY_TABLE_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> AlchemyTableBlockEntity.this.progress;
                    case 1 -> AlchemyTableBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> AlchemyTableBlockEntity.this.progress = value;
                    case 1 -> AlchemyTableBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int size() {
                return 2;
            }
        };


    }
    private int changeCount;
    @Override
    public void markDirty() {
        ++changeCount;
        super.markDirty();
    }

    public int getChangeCount() {
        return changeCount;
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

    //Crafting stuff
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.tcots-witcher.alchemy_table");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("alchemy_table.progress", progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("alchemy_table.progress");
    }


    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new AlchemyTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }


    public static void tick(World world, BlockPos pos, BlockState state, AlchemyTableBlockEntity blockEntity) {

        if(world.isClient()) {
            return;
        }
            if(blockEntity.hasRecipe()) {
                blockEntity.increaseCraftProgress();
                markDirty(world, pos, state);

                if(blockEntity.hasCraftingFinished()) {
                    blockEntity.craftItem();
                    blockEntity.resetProgress();
                }
            } else {
                blockEntity.resetProgress();
            }

    }

    private boolean hasRecipe() {
        SimpleInventory inventory = new SimpleInventory(this.size());

        for (int i = 0; i < this.size(); i++) {
            inventory.setStack(i, this.getStack(i));
        }

        Optional<RecipeEntry<AlchemyTableRecipe>> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager().getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, getWorld());

        return recipe.isPresent();
    }

    private void craftItem() {
        if(hasRecipe()) {
        SimpleInventory inventory = new SimpleInventory(this.size());

            for (int i = 0; i < this.size(); i++) {
                inventory.setStack(i, this.getStack(i));
            }

        Optional<RecipeEntry<AlchemyTableRecipe>> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager()
                    .getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, this.getWorld());


        if(this.getStack(INGREDIENT_SLOT_1) != null ){
            this.setStack(INGREDIENT_SLOT_1, ItemStack.EMPTY);
        }

        if(this.getStack(INGREDIENT_SLOT_2) != null){
            this.setStack(INGREDIENT_SLOT_2, ItemStack.EMPTY);
        }

        if(this.getStack(INGREDIENT_SLOT_3) != null){
            this.setStack(INGREDIENT_SLOT_3, ItemStack.EMPTY);
        }

        if(this.getStack(INGREDIENT_SLOT_4) != null){
            this.setStack(INGREDIENT_SLOT_4, ItemStack.EMPTY);
        }

        if(this.getStack(INGREDIENT_SLOT_5) != null){
            this.setStack(INGREDIENT_SLOT_5, ItemStack.EMPTY);
        }

        if(this.getStack(OUTPUT_POTION_SLOT) != null){
            this.setStack(OUTPUT_POTION_SLOT, ItemStack.EMPTY);
        }
            if(recipe.isPresent()) {
                this.setStack(OUTPUT_POTION_SLOT, new ItemStack(recipe.get().value().getResult(null).getItem(), recipe.get().value().getResult(null).getCount()));
                assert world != null;
                world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
                this.resetProgress();
            }
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

    private void resetProgress() {
        this.progress = 0;
    }

}
