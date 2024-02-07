package TCOTS.blocks.entity;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.screen.AlchemyTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.jetbrains.annotations.Nullable;
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

public class AlchemyTableBlockEntity extends BlockEntity implements GeoBlockEntity, ExtendedScreenHandlerFactory, ImplementedInventory  {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(6, ItemStack.EMPTY);

    private static final int INGREDIENT_SLOT_1  = 0;
    private static final int INGREDIENT_SLOT_2  = 1;
    private static final int INGREDIENT_SLOT_3  = 2;
    private static final int INGREDIENT_SLOT_4  = 3;
    private static final int INGREDIENT_SLOT_5  = 4;
    private static final int OUTPUT_POTION_SLOT = 5;

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    //Gecko stuff
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

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
    public Text getDisplayName() {
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

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory Pinv, PlayerEntity player) {
        return new AlchemyTableScreenHandler(syncId, Pinv, this, this.propertyDelegate);
    }


    public void tick(World world, BlockPos pos, BlockState state) {
        if(world.isClient()) {
            return;
        }
//        if(isOutputSlotEmptyOrReceivable()) {
            if(this.hasRecipe()) {
                this.increaseCraftProgress();
                markDirty(world, pos, state);

                if(hasCraftingFinished()) {
                    this.craftItem();
                    this.resetProgress();
                }
            } else {
                this.resetProgress();
            }
//        } else {
//            this.resetProgress();
//            markDirty(world, pos, state);
//        }
    }

    private boolean hasRecipe() {
        SimpleInventory inventory = new SimpleInventory(this.size());

        for (int i = 0; i < this.size(); i++) {
            inventory.setStack(i, this.getStack(i));
        }

        Optional<AlchemyTableRecipe> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager().getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, getWorld());

        return recipe.isPresent();
    }


    private void craftItem() {
        if(hasRecipe()) {
        SimpleInventory inventory = new SimpleInventory(this.size());

            for (int i = 0; i < this.size(); i++) {
                inventory.setStack(i, this.getStack(i));
            }

        Optional<AlchemyTableRecipe> recipe = Objects.requireNonNull(this.getWorld()).getRecipeManager()
                    .getFirstMatch(AlchemyTableRecipe.Type.INSTANCE, inventory, this.getWorld());


        if(this.getStack(INGREDIENT_SLOT_1) != null ){
            this.removeStack(INGREDIENT_SLOT_1, recipe.get().getIngredientsCounts()[0]);
        }

        if(this.getStack(INGREDIENT_SLOT_2) != null){
            this.removeStack(INGREDIENT_SLOT_2, recipe.get().getIngredientsCounts()[1]);
        }

        if(this.getStack(INGREDIENT_SLOT_3) != null){
            this.removeStack(INGREDIENT_SLOT_3, recipe.get().getIngredientsCounts()[2]);
        }

        if(this.getStack(INGREDIENT_SLOT_4) != null){
            this.removeStack(INGREDIENT_SLOT_4, recipe.get().getIngredientsCounts()[3]);
        }

        if(this.getStack(INGREDIENT_SLOT_5) != null){
            this.removeStack(INGREDIENT_SLOT_5, recipe.get().getIngredientsCounts()[4]);
        }

        if(this.getStack(OUTPUT_POTION_SLOT) != null){
            this.removeStack(OUTPUT_POTION_SLOT, recipe.get().getBaseItem().getCount());
        }

            ItemStack result = new ItemStack(recipe.get().getOutput(null).getItem());

            this.setStack(OUTPUT_POTION_SLOT, new ItemStack(result.getItem(), getStack(OUTPUT_POTION_SLOT).getCount() + result.getCount()));
            assert world != null;
            world.syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, pos, 0);
            this.resetProgress();
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftProgress() {
        progress++;
    }

//    private boolean canInsertItemIntoOutputSlot(Item item) {
//        return this.getStack(OUTPUT_POTION_SLOT).getItem() == item || this.getStack(OUTPUT_POTION_SLOT).isEmpty();
//    }
//
//
//    private boolean canInsertAmountIntoOutputSlot(ItemStack result) {
//        return this.getStack(OUTPUT_POTION_SLOT).getCount() + result.getCount() <= getStack(OUTPUT_POTION_SLOT).getMaxCount();
//    }
//
//    private boolean isOutputSlotEmptyOrReceivable() {
//        return this.getStack(OUTPUT_POTION_SLOT).isEmpty() || this.getStack(OUTPUT_POTION_SLOT).getCount() < this.getStack(OUTPUT_POTION_SLOT).getMaxCount();
//    }

    private void resetProgress() {
        this.progress = 0;
    }


}