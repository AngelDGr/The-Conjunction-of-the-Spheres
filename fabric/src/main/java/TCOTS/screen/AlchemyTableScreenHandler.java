package TCOTS.screen;

import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class AlchemyTableScreenHandler extends RecipeBookMenu<AlchemyTableRecipe.AlchemyTableInventory, AlchemyTableRecipe> {
    private final StackedContents recipeFinder = new StackedContents();

    public final SimpleContainer inputInventory = new SimpleContainer(6){

        @Override
        public @NotNull ItemStack getItem(int slot) {
            if (slot >= this.getContainerSize()) {
                return ItemStack.EMPTY;
            }
            return this.items.get(slot);
        }

        @Override
        public @NotNull ItemStack removeItemNoUpdate(int slot) {
            return ContainerHelper.takeItem(this.items, slot);
        }

        @Override
        public @NotNull ItemStack removeItem(int slot, int amount) {
            ItemStack itemStack = ContainerHelper.removeItem(this.items, slot, amount);
            if (!itemStack.isEmpty()) {
                AlchemyTableScreenHandler.this.slotsChanged(this);
            }
            return itemStack;
        }

        @Override
        public void setItem(int slot, @NotNull ItemStack stack) {
            super.setItem(slot, stack);
            AlchemyTableScreenHandler.this.slotsChanged(this);
        }

        @Override
        public void clearContent() {
            this.items.clear();
        }
    };

    private final AlchemyTableResultInventory resultInventory = new AlchemyTableResultInventory();
    private final ContainerLevelAccess context;
    private final Player player;
    private final AlchemyTableBlockEntity blockEntity;

    public AlchemyTableScreenHandler(int syncId, Inventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, ContainerLevelAccess.NULL, playerInventory.player.level().getBlockEntity(pos));
    }

    public AlchemyTableBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public AlchemyTableScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context, BlockEntity blockEntity) {
        super(ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        this.context = context;
        this.blockEntity= (AlchemyTableBlockEntity) blockEntity;

        //Ingredients
        this.addSlot(new Slot(inputInventory, 3, 32,  26));
        this.addSlot(new Slot(inputInventory, 1, 56,  17));
        this.addSlot(new Slot(inputInventory, 0, 80,  26));
        this.addSlot(new Slot(inputInventory, 2, 104, 17));
        this.addSlot(new Slot(inputInventory, 4, 128, 26));
        //Base
        this.addSlot(new Slot(inputInventory, 5, 80, 56){
            @Override
            public int getMaxStackSize() {
                return 5;
            }
        });
        //Output
        this.addSlot(new PotionOutputSlot(playerInventory.player, resultInventory, inputInventory, 6, 80, 85));


        //Add Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 107 + i * 18));
            }
        }

        //Add Player Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 165));
        }

    }
    @Override
    public void fillCraftSlotsStackedContents(@NotNull StackedContents finder) {

    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        this.context.execute(
                (world, pos) ->
                        updateResult(this, world, this.player, new AlchemyTableRecipe.AlchemyTableInventory(
                                this.inputInventory.getItem(0),
                                this.inputInventory.getItem(1),
                                this.inputInventory.getItem(2),
                                this.inputInventory.getItem(3),
                                this.inputInventory.getItem(4),
                                this.inputInventory.getItem(5)
                        ), this.resultInventory));
    }

    @Override
    public void clearCraftingContent() {
        this.inputInventory.clearContent();
        this.resultInventory.clearContent();
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.context.execute((world, pos) -> this.clearContainer(player, this.inputInventory));
    }


    @Override
    public boolean recipeMatches(RecipeHolder<AlchemyTableRecipe> recipe) {
        return recipe.value().matches(new AlchemyTableRecipe.AlchemyTableInventory(
                this.inputInventory.getItem(0),
                this.inputInventory.getItem(1),
                this.inputInventory.getItem(2),
                this.inputInventory.getItem(3),
                this.inputInventory.getItem(4),
                this.inputInventory.getItem(5)
        ),

                this.player.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 0;
    }

    @Override
    public int getGridWidth() {
        return 1;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 6;
    }

    @Override
    public @NotNull RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            //If it's the result slot
            if (slotIndex == 6) {
                this.context.execute((world, pos) -> itemStack2.getItem().onCraftedBy(itemStack2, world, player));

                if (!this.moveItemStackTo(itemStack2, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemStack2, itemStack);

            } else if (
                    //If the slots are in the inventory, tries to put them in the slots of the crafting screen
                    slotIndex >= 7 && slotIndex < 43 ?
                    !this.moveItemStackTo(itemStack2, 0, 6, false)
                    //If the slots are outside hotbar
                    && (slotIndex < 34 ? !this.moveItemStackTo(itemStack2, 34, 43, false)
                    : !this.moveItemStackTo(itemStack2, 7, 34, false))
                    : !this.moveItemStackTo(itemStack2, 7, 43, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, itemStack2);
            if (slotIndex == 0) {
                player.drop(itemStack2, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return AlchemyTableScreenHandler.stillValid(this.context, player, TCOTS_Blocks_Fabric.ALCHEMY_TABLE);
    }

    /**
    Used when clicking a button in the recipe book widget
     */
    @Override
    public void handlePlacement(boolean craftAll, @NotNull RecipeHolder<?> recipe, ServerPlayer player) {
        List<ItemStack> TotalInventoryItems = new ArrayList<>();

        //Mix the inventory the stacks in the player inventory and ScreenHandler inventory
        TotalInventoryItems.addAll(player.getInventory().items);
        TotalInventoryItems.addAll(this.inputInventory.items);

        this.recipeFinder.clear();
        for(ItemStack stack: TotalInventoryItems){
            recipeFinder.accountStack(stack, stack.getCount());
        }

        if (recipeFinder.canCraft(recipe.value(), null) && recipe.value() instanceof AlchemyTableRecipe alchemyRecipe) {
            List<ItemStack> ingredientsStacksList = alchemyRecipe.returnItemStackWithQuantity();
            //Empties the table inventory
            for(int i=0; i < 6; i++){
                ItemStack stackInsideSlot = this.inputInventory.getItem(i);
                if(this.inputInventory.getItem(i) != ItemStack.EMPTY){
                    if(player.getInventory().getSlotWithRemainingSpace(stackInsideSlot) != -1){
                        player.getInventory().add(player.getInventory().getSlotWithRemainingSpace(stackInsideSlot), stackInsideSlot);
                        this.inputInventory.setItem(i, ItemStack.EMPTY);
                    }
                    else if(player.getInventory().getFreeSlot()!=-1){
                        player.getInventory().add(player.getInventory().getFreeSlot(), stackInsideSlot);
                        this.inputInventory.setItem(i, ItemStack.EMPTY);
                    }
                    this.inputInventory.setChanged();
                }
            }

            //To put ingredients in place
            for(int i=0; i < ingredientsStacksList.size(); i++){
                int slotWithIngredient = player.getInventory().findSlotMatchingItem(ingredientsStacksList.get(i));
                int quantity =  ingredientsStacksList.get(i).getCount();

                if(this.inputInventory.getItem(i) == ItemStack.EMPTY) {

                    ItemStack stack = player.getInventory().getItem(slotWithIngredient).copyWithCount(quantity);

                    player.getInventory().getItem(player.getInventory().findSlotMatchingItem(ingredientsStacksList.get(i))).shrink(quantity);

                    this.inputInventory.setItem(i, stack);
                }
            }

            int slotWithBase = player.getInventory().findSlotMatchingItem(alchemyRecipe.getBaseItem());
            int quantity     = alchemyRecipe.getBaseItem().getCount();
            //To put the base in place
            if(this.inputInventory.getItem(5) == ItemStack.EMPTY) {

                ItemStack base = player.getInventory().getItem(slotWithBase).copyWithCount(quantity);

                player.getInventory().getItem(slotWithBase).shrink(quantity);

                this.inputInventory.setItem(5, base);
            }
        }

        player.getInventory().setChanged();
    }

    protected void updateResult(AbstractContainerMenu handler, Level world, Player player, AlchemyTableRecipe.AlchemyTableInventory craftingInventory, AlchemyTableResultInventory resultInventory) {

        if (world.isClientSide) {
            return;
        }
        ServerPlayer serverPlayerEntity = (ServerPlayer)player;
        ItemStack itemStack = ItemStack.EMPTY;

        if(world.getServer() == null) return;
        Optional<RecipeHolder<AlchemyTableRecipe>> optional = world.getServer().getRecipeManager().getRecipeFor(
                ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE,
                craftingInventory, world);
        if (optional.isPresent()) {
            ItemStack itemStack2;
            RecipeHolder<AlchemyTableRecipe> recipeEntry = optional.get();
            AlchemyTableRecipe craftingRecipe = recipeEntry.value();
            if(player instanceof ServerPlayer serverPlayer){

                if (serverPlayer.getRecipeBook().contains(recipeEntry) && (itemStack2 = craftingRecipe.assemble(craftingInventory, world.registryAccess())).isItemEnabled(world.enabledFeatures())) {
                    itemStack = itemStack2;
                }
            }

        }

        resultInventory.setItem(6, itemStack);
        handler.setRemoteSlot(6, itemStack);
        serverPlayerEntity.connection.send(new ClientboundContainerSetSlotPacket(handler.containerId, handler.incrementStateId(), 6, itemStack));
    }

    protected static class PotionOutputSlot extends Slot {
        private final SimpleContainer input;
        private final Player player;
        private int amount;
        public PotionOutputSlot(Player player, AlchemyTableResultInventory result, SimpleContainer input, int index, int x, int y) {
            super(result, index, x, y);
            this.player = player;
            this.input = input;
        }

        @Override
        public int getMaxStackSize() {
            return 5;
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }

        @Override
        public @NotNull ItemStack remove(int amount) {
            if (this.hasItem()) {
                this.amount += Math.min(amount, this.getItem().getCount());
            }
            return super.remove(amount);
        }

        @Override
        protected void onQuickCraft(@NotNull ItemStack stack, int amount) {
            this.amount += amount;
            this.checkTakeAchievements(stack);
        }

        @Override
        protected void onSwapCraft(int amount) {
            this.amount += amount;
        }

        @Override
        protected void checkTakeAchievements(@NotNull ItemStack stack) {
            Optional<RecipeHolder<AlchemyTableRecipe>> optional = Optional.empty();
            if(player.level().getServer()!=null) {
                optional =
                        player.level().getServer().getRecipeManager()
                                .getRecipeFor(ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE, new AlchemyTableRecipe.AlchemyTableInventory(
                                        this.input.getItem(0),
                                        this.input.getItem(1),
                                        this.input.getItem(2),
                                        this.input.getItem(3),
                                        this.input.getItem(4),
                                        this.input.getItem(5)
                                ), player.level());
            }

            if (this.amount > 0) {
                stack.onCraftedBy(this.player.level(), this.player, this.amount);
                optional.ifPresent(
                        alchemyTableRecipeRecipeEntry -> player.triggerRecipeCrafted(alchemyTableRecipeRecipeEntry, this.input.getItems())
                );
            }
            this.amount = 0;
        }

        @Override
        public void onTake(Player player, @NotNull ItemStack stack) {
            this.checkTakeAchievements(stack);
            this.input.setItem(0, ItemStack.EMPTY);
            this.input.setItem(1, ItemStack.EMPTY);
            this.input.setItem(2, ItemStack.EMPTY);
            this.input.setItem(3, ItemStack.EMPTY);
            this.input.setItem(4, ItemStack.EMPTY);
            this.input.setItem(5, ItemStack.EMPTY);
            player.level().levelEvent(LevelEvent.SOUND_BREWING_STAND_BREW, player.blockPosition(), 0);
        }
    }

    protected static class AlchemyTableResultInventory implements Container {
        private final NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);
        @Override
        public int getContainerSize() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            for (ItemStack itemStack : this.stacks) {
                if (itemStack.isEmpty()) continue;
                return false;
            }
            return true;
        }

        @Override
        public @NotNull ItemStack getItem(int slot) {
            return this.stacks.get(0);
        }

        @Override
        public @NotNull ItemStack removeItem(int slot, int amount) {
            return ContainerHelper.takeItem(this.stacks, 0);
        }

        @Override
        public @NotNull ItemStack removeItemNoUpdate(int slot) {
            return ContainerHelper.takeItem(this.stacks, 0);
        }

        @Override
        public void setItem(int slot, @NotNull ItemStack stack) {
            this.stacks.set(0, stack);
        }

        @Override
        public void setChanged() {
        }

        @Override
        public boolean stillValid(@NotNull Player player) {
            return true;
        }

        @Override
        public void clearContent() {
            this.stacks.clear();
        }
    }
}
