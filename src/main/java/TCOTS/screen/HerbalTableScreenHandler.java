package TCOTS.screen;

import TCOTS.items.concoctions.recipes.HerbalTableRecipe;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

import java.util.List;
import java.util.Optional;

public class HerbalTableScreenHandler extends ScreenHandler {

    private final SimpleInventory inputInventory = new SimpleInventory(2){
        @Override
        public void markDirty() {
            super.markDirty();
            HerbalTableScreenHandler.this.onContentChanged(this);
        }
    };
    private final HerbalTableResultInventory resultInventory = new HerbalTableResultInventory();
    private final ScreenHandlerContext context;
    private final PlayerEntity player;

    public HerbalTableScreenHandler(int syncId, PlayerInventory inventory) {
        this(syncId, inventory, ScreenHandlerContext.EMPTY);
    }

    public HerbalTableScreenHandler(int syncId, PlayerInventory playerInventory, final ScreenHandlerContext context) {
        super(ScreenHandlersAndRecipesRegister.HERBAL_TABLE_SCREEN_HANDLER, syncId);
        this.context=context;
        this.player = playerInventory.player;
        this.addSlot(new Slot(inputInventory, 0, 27,47){
            @Override
            public int getMaxItemCount() {
                return 5;
            }
        });
        this.addSlot(new Slot(inputInventory, 1, 76,47){
            @Override
            public int getMaxItemCount() {
                return 1;
            }

            @Override
            public boolean canInsert(ItemStack stack) {
                return isFitPotion(stack);
            }
        });

        this.addSlot(new MixtureOutputSlot(playerInventory.player, resultInventory, inputInventory,2,134,47));

        //Add Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }

        //Add Player Hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    private boolean isFitPotion(ItemStack stack){
        if(!stack.contains(DataComponentTypes.POTION_CONTENTS)){
            return false;
        }

        PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);

         return potionContents!=null &&
                 (potionContents.potion().get().equals(Potions.WATER)
                 || potionContents.potion().get().equals(Potions.THICK)
                 || potionContents.potion().get().equals(Potions.MUNDANE));
    }

    @Override
    protected boolean insertItem(ItemStack stackToInsert, int startIndex, int endIndex, boolean fromLast) {
        if(startIndex==0){
            if(stackToInsert.getCount() + this.slots.get(0).getStack().getCount() <=5 && !isFitPotion(stackToInsert)){
                return super.insertItem(stackToInsert, startIndex, endIndex, fromLast);
            } else {
                return false;
            }
        }

        return super.insertItem(stackToInsert, startIndex, endIndex, fromLast);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasStack()) {

            ItemStack stackInSlot = slot.getStack();
            itemStack = stackInSlot.copy();

            //If it's the result slot
            if (slotIndex == 2) {
                this.context.run((world, pos) -> stackInSlot.getItem().onCraftByPlayer(stackInSlot, world, player));

                if (!this.insertItem(stackInSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickTransfer(stackInSlot, itemStack);

            } else if (slotIndex != 1 && isFitPotion(stackInSlot) && !this.slots.get(1).hasStack()) {
                if(!this.insertItem(stackInSlot,1,2, true))
                    return ItemStack.EMPTY;

            } else if (
                    //If the slots are in the inventory, tries to put them in the slots of the crafting screen
                    slotIndex >= 3 && slotIndex < 39?
                    !this.insertItem(stackInSlot, 0, 1, false)
                    //If the slots are outside hotbar
                    && (slotIndex < 30 ? !this.insertItem(stackInSlot, 30, 39, false)
                    : !this.insertItem(stackInSlot, 3, 30, false))
                    : !this.insertItem(stackInSlot, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (stackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (stackInSlot.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, stackInSlot);
            if (slotIndex == 0) {
                player.dropItem(stackInSlot, false);
            }
        }
        return itemStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.context.run((world, pos) -> this.dropInventory(player, this.inputInventory));
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, pos) -> HerbalTableScreenHandler.updateResult(
                this,
                world,
                this.player,
                new HerbalTableRecipe.HerbalTableInventory(this.inputInventory.getStack(0),
                        this.inputInventory.getStack(1)) ,
                this.resultInventory));
    }

    protected static void updateResult(ScreenHandler handler, World world, PlayerEntity player, HerbalTableRecipe.HerbalTableInventory craftingInventory, HerbalTableResultInventory resultInventory) {
        if (world.isClient) {
            return;
        }

        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
        ItemStack outputItem = ItemStack.EMPTY;

        if (world.getServer() == null) return;

        //Crafting
        Optional<RecipeEntry<HerbalTableRecipe>> optional = world.getServer().getRecipeManager().getFirstMatch(ScreenHandlersAndRecipesRegister.HERBAL_TABLE, craftingInventory, world);
        if (optional.isPresent()) {
            ItemStack itemStack2;
            RecipeEntry<HerbalTableRecipe> recipeEntry = optional.get();
            HerbalTableRecipe craftingRecipe = recipeEntry.value();
            if(player instanceof ServerPlayerEntity){
                if ((itemStack2 = craftingRecipe.craft(craftingInventory, world.getRegistryManager())).isItemEnabled(world.getEnabledFeatures())) {
                    outputItem = itemStack2;
                }
            }
        }

        resultInventory.setStack(2, outputItem);
        handler.setPreviousTrackedSlot(2, outputItem);
        serverPlayerEntity.networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), 2, outputItem));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public boolean canInsertIntoSlot(Slot slot) {
        return slot.getIndex() != 2;
    }

    public static class MixtureOutputSlot extends Slot{
        private final Inventory input;
        private final PlayerEntity player;
        private int amount;

        public MixtureOutputSlot(PlayerEntity player, HerbalTableResultInventory result, SimpleInventory input, int index, int x, int y) {
            super(result, index, x, y);
            this.player = player;
            this.input=input;
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
            if (this.amount > 0) {
                stack.onCraftByPlayer(this.player.getWorld(), this.player, this.amount);
            }
            this.amount = 0;
        }

        @Override
        public void onTakeItem(PlayerEntity player, ItemStack stack) {
            this.onCrafted(stack);
            List<ItemStack> inventoryStacksList = List.of(this.input.getStack(0), this.input.getStack(1));

            for (int i = 0; i < inventoryStacksList.size(); ++i) {
                ItemStack itemStack = this.input.getStack(i);
                ItemStack itemStack2 = inventoryStacksList.get(i);
                if (!itemStack.isEmpty()) {
                    this.input.removeStack(i, this.input.getStack(i).getCount());
                    itemStack = this.input.getStack(i);
                }
                if (itemStack2.isEmpty()) continue;
                if (itemStack.isEmpty()) {
                    this.input.setStack(i, itemStack2);
                    continue;
                }
                if (ItemStack.areItemsAndComponentsEqual(itemStack, itemStack2)) {
                    itemStack2.increment(itemStack.getCount());
                    this.input.setStack(i, itemStack2);
                    continue;
                }
                if (this.player.getInventory().insertStack(itemStack2)) continue;
                this.player.dropItem(itemStack2, false);
            }

            player.getWorld().syncWorldEvent(WorldEvents.BREWING_STAND_BREWS, player.getBlockPos(), 0);
        }
    }

    public static class HerbalTableResultInventory implements Inventory {
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
