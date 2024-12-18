package TCOTS.screen;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlchemyTableScreenHandler extends AbstractRecipeScreenHandler<AlchemyTableRecipe.AlchemyTableInventory, AlchemyTableRecipe> {
    private final RecipeMatcher recipeFinder = new RecipeMatcher();

    public final SimpleInventory inputInventory = new SimpleInventory(6){

        @Override
        public ItemStack getStack(int slot) {
            if (slot >= this.size()) {
                return ItemStack.EMPTY;
            }
            return this.heldStacks.get(slot);
        }

        @Override
        public ItemStack removeStack(int slot) {
            return Inventories.removeStack(this.heldStacks, slot);
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            ItemStack itemStack = Inventories.splitStack(this.heldStacks, slot, amount);
            if (!itemStack.isEmpty()) {
                AlchemyTableScreenHandler.this.onContentChanged(this);
            }
            return itemStack;
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            super.setStack(slot, stack);
            AlchemyTableScreenHandler.this.onContentChanged(this);
        }

        @Override
        public void clear() {
            this.heldStacks.clear();
        }
    };

    private final AlchemyTableResultInventory resultInventory = new AlchemyTableResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;
    private final AlchemyTableBlockEntity blockEntity;

    public AlchemyTableScreenHandler(int syncId, PlayerInventory playerInventory, BlockPos pos) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, playerInventory.player.getWorld().getBlockEntity(pos));
    }

    public AlchemyTableBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public AlchemyTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, BlockEntity blockEntity) {
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
            public int getMaxItemCount() {
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
    public void populateRecipeFinder(RecipeMatcher finder) {

    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run(
                (world, pos) ->
                        updateResult(this, world, this.player, new AlchemyTableRecipe.AlchemyTableInventory(
                                this.inputInventory.getStack(0),
                                this.inputInventory.getStack(1),
                                this.inputInventory.getStack(2),
                                this.inputInventory.getStack(3),
                                this.inputInventory.getStack(4),
                                this.inputInventory.getStack(5)
                        ), this.resultInventory));
    }

    @Override
    public void clearCraftingSlots() {
        this.inputInventory.clear();
        this.resultInventory.clear();
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inputInventory));
    }


    @Override
    public boolean matches(RecipeEntry<AlchemyTableRecipe> recipe) {
        return recipe.value().matches(new AlchemyTableRecipe.AlchemyTableInventory(
                this.inputInventory.getStack(0),
                this.inputInventory.getStack(1),
                this.inputInventory.getStack(2),
                this.inputInventory.getStack(3),
                this.inputInventory.getStack(4),
                this.inputInventory.getStack(5)
        ),

                this.player.getWorld());
    }

    @Override
    public int getCraftingResultSlotIndex() {
        return 0;
    }

    @Override
    public int getCraftingWidth() {
        return 1;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 6;
    }

    @Override
    public RecipeBookCategory getCategory() {
        return null;
    }

    @Override
    public boolean canInsertIntoSlot(int index) {
        return index != this.getCraftingResultSlotIndex();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            //If it's the result slot
            if (slotIndex == 6) {
                this.context.run((world, pos) -> itemStack2.getItem().onCraftByPlayer(itemStack2, world, player));

                if (!this.insertItem(itemStack2, 7, 43, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(itemStack2, itemStack);

            } else if (
                    //If the slots are in the inventory, tries to put them in the slots of the crafting screen
                    slotIndex >= 7 && slotIndex < 43 ?
                    !this.insertItem(itemStack2, 0, 6, false)
                    //If the slots are outside hotbar
                    && (slotIndex < 34 ? !this.insertItem(itemStack2, 34, 43, false)
                    : !this.insertItem(itemStack2, 7, 34, false))
                    : !this.insertItem(itemStack2, 7, 43, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStack2);
            if (slotIndex == 0) {
                player.dropItem(itemStack2, false);
            }
        }
        return itemStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return AlchemyTableScreenHandler.canUse(this.context, player, TCOTS_Blocks.ALCHEMY_TABLE);
    }

    /**
    Used when clicking a button in the recipe book widget
     */
    @Override
    public void fillInputSlots(boolean craftAll, RecipeEntry<?> recipe, ServerPlayerEntity player) {
        List<ItemStack> TotalInventoryItems = new ArrayList<>();

        //Mix the inventory the stacks in the player inventory and ScreenHandler inventory
        TotalInventoryItems.addAll(player.getInventory().main);
        TotalInventoryItems.addAll(this.inputInventory.heldStacks);

        this.recipeFinder.clear();
        for(ItemStack stack: TotalInventoryItems){
            recipeFinder.addInput(stack, stack.getCount());
        }

        if (recipeFinder.match(recipe.value(), null) && recipe.value() instanceof AlchemyTableRecipe alchemyRecipe) {
            List<ItemStack> ingredientsStacksList = alchemyRecipe.returnItemStackWithQuantity();
            //Empties the table inventory
            for(int i=0; i < 6; i++){
                ItemStack stackInsideSlot = this.inputInventory.getStack(i);
                if(this.inputInventory.getStack(i) != ItemStack.EMPTY){
                    if(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot) != -1){
                        player.getInventory().insertStack(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot), stackInsideSlot);
                        this.inputInventory.setStack(i, ItemStack.EMPTY);
                    }
                    else if(player.getInventory().getEmptySlot()!=-1){
                        player.getInventory().insertStack(player.getInventory().getEmptySlot(), stackInsideSlot);
                        this.inputInventory.setStack(i, ItemStack.EMPTY);
                    }
                    this.inputInventory.markDirty();
                }
            }

            //To put ingredients in place
            for(int i=0; i < ingredientsStacksList.size(); i++){
                int slotWithIngredient = player.getInventory().getSlotWithStack(ingredientsStacksList.get(i));
                int quantity =  ingredientsStacksList.get(i).getCount();

                if(this.inputInventory.getStack(i) == ItemStack.EMPTY) {

                    ItemStack stack = player.getInventory().getStack(slotWithIngredient).copyWithCount(quantity);

                    player.getInventory().getStack(player.getInventory().getSlotWithStack(ingredientsStacksList.get(i))).decrement(quantity);

                    this.inputInventory.setStack(i, stack);
                }
            }

            int slotWithBase = player.getInventory().getSlotWithStack(alchemyRecipe.getBaseItem());
            int quantity     = alchemyRecipe.getBaseItem().getCount();
            //To put the base in place
            if(this.inputInventory.getStack(5) == ItemStack.EMPTY) {

                ItemStack base = player.getInventory().getStack(slotWithBase).copyWithCount(quantity);

                player.getInventory().getStack(slotWithBase).decrement(quantity);

                this.inputInventory.setStack(5, base);
            }
        }

        player.getInventory().markDirty();
    }

    protected void updateResult(ScreenHandler handler, World world, PlayerEntity player, AlchemyTableRecipe.AlchemyTableInventory craftingInventory, AlchemyTableResultInventory resultInventory) {

        if (world.isClient) {
            return;
        }
        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
        ItemStack itemStack = ItemStack.EMPTY;

        if(world.getServer() == null) return;
        Optional<RecipeEntry<AlchemyTableRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(
                ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE,
                craftingInventory, world);
        if (optional.isPresent()) {
            ItemStack itemStack2;
            RecipeEntry<AlchemyTableRecipe> recipeEntry = optional.get();
            AlchemyTableRecipe craftingRecipe = recipeEntry.value();
            if(player instanceof ServerPlayerEntity serverPlayer){

                if (serverPlayer.getRecipeBook().contains(recipeEntry) && (itemStack2 = craftingRecipe.craft(craftingInventory, world.getRegistryManager())).isItemEnabled(world.getEnabledFeatures())) {
                    itemStack = itemStack2;
                }
            }

        }

        resultInventory.setStack(6, itemStack);
        handler.setPreviousTrackedSlot(6, itemStack);
        serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 6, itemStack));
    }

    protected static class PotionOutputSlot extends Slot {
        private final SimpleInventory input;
        private final PlayerEntity player;
        private int amount;
        public PotionOutputSlot(PlayerEntity player, AlchemyTableResultInventory result, SimpleInventory input, int index, int x, int y) {
            super(result, index, x, y);
            this.player = player;
            this.input = input;
        }

        @Override
        public int getMaxItemCount() {
            return 5;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack takeStack(int amount) {
            if (this.hasStack()) {
                this.amount += Math.min(amount, this.getStack().getCount());
            }
            return super.takeStack(amount);
        }

        @Override
        protected void onCrafted(ItemStack stack, int amount) {
            this.amount += amount;
            this.onCrafted(stack);
        }

        @Override
        protected void onTake(int amount) {
            this.amount += amount;
        }

        @Override
        protected void onCrafted(ItemStack stack) {
            Optional<RecipeEntry<AlchemyTableRecipe>> optional = Optional.empty();
            if(player.getWorld().getServer()!=null) {
                optional =
                        player.getWorld().getServer().getRecipeManager()
                                .getFirstMatch(ScreenHandlersAndRecipesRegister.ALCHEMY_TABLE, new AlchemyTableRecipe.AlchemyTableInventory(
                                        this.input.getStack(0),
                                        this.input.getStack(1),
                                        this.input.getStack(2),
                                        this.input.getStack(3),
                                        this.input.getStack(4),
                                        this.input.getStack(5)
                                ), player.getWorld());
            }

            if (this.amount > 0) {
                stack.onCraftByPlayer(this.player.getWorld(), this.player, this.amount);
                optional.ifPresent(
                        alchemyTableRecipeRecipeEntry -> player.onRecipeCrafted(alchemyTableRecipeRecipeEntry, this.input.getHeldStacks())
                );
            }
            this.amount = 0;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            this.input.setStack(0, ItemStack.EMPTY);
            this.input.setStack(1, ItemStack.EMPTY);
            this.input.setStack(2, ItemStack.EMPTY);
            this.input.setStack(3, ItemStack.EMPTY);
            this.input.setStack(4, ItemStack.EMPTY);
            this.input.setStack(5, ItemStack.EMPTY);
            player.getWorld().syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, player.getBlockPos(), 0);
        }
    }

    protected static class AlchemyTableResultInventory implements Inventory {
        private final DefaultedList<ItemStack> stacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
        @Override
        public int size() {
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
        public ItemStack getStack(int slot) {
            return this.stacks.get(0);
        }

        @Override
        public ItemStack removeStack(int slot, int amount) {
            return Inventories.removeStack(this.stacks, 0);
        }

        @Override
        public ItemStack removeStack(int slot) {
            return Inventories.removeStack(this.stacks, 0);
        }

        @Override
        public void setStack(int slot, ItemStack stack) {
            this.stacks.set(0, stack);
        }

        @Override
        public void markDirty() {
        }

        @Override
        public boolean canPlayerUse(PlayerEntity player) {
            return true;
        }

        @Override
        public void clear() {
            this.stacks.clear();
        }
    }
}
