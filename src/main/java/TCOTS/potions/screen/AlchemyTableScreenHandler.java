package TCOTS.potions.screen;

import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.AlchemyTableRecipesRegister;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class AlchemyTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final AlchemyTableBlockEntity blockEntity;

    public AlchemyTableScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(6));
    }
    public Inventory getInventory() {
        return inventory;
    }
    public int getChangeCount() {
        return blockEntity.getChangeCount();
    }

    public AlchemyTableScreenHandler(int syncId, PlayerInventory playerInventory,
                                     BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(AlchemyTableRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, syncId);

        checkSize((Inventory) blockEntity, 6);

        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);

        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((AlchemyTableBlockEntity) blockEntity);

        //Ingredients
        this.addSlot(new Slot(inventory, 3, 31,  17));
        this.addSlot(new Slot(inventory, 1, 55,  17));
        this.addSlot(new Slot(inventory, 0, 79,  17));
        this.addSlot(new Slot(inventory, 2, 103, 17));
        this.addSlot(new Slot(inventory, 4, 127, 17));
        //Output
        this.addSlot(new PotionWitcherSlot(inventory, 5, 79, 58));

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

        addProperties(arrayPropertyDelegate);
    }


    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 62; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public int getBrewTime() {
        return this.propertyDelegate.get(0);
    }


    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }
    private final RecipeMatcher recipeFinder = new RecipeMatcher();
    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
    public DefaultedList<ItemStack> PlayerInventoryItems;
    public List<ItemStack> TotalInventoryItems=new ArrayList<>();
    public List<ItemStack> AlchemyTableInventoryItems = new ArrayList<>();

    public void CraftWithBook(AlchemyTableRecipe recipe, ServerPlayerEntity player){
        AlchemyTableInventoryItems.clear();
        for (int i=0;i<this.getInventory().size();i++){
            AlchemyTableInventoryItems.add(this.getInventory().getStack(i));}

        PlayerInventoryItems = player.getInventory().main;

        TotalInventoryItems.clear();
        TotalInventoryItems.addAll(PlayerInventoryItems);
        TotalInventoryItems.addAll(AlchemyTableInventoryItems);

        this.recipeFinder.clear();
        for(ItemStack stack: this.TotalInventoryItems){
            recipeFinder.addInput(stack, stack.getCount());
        }

        if (recipeFinder.match(recipe, null)) {

            List<ItemStack> itemStackList = recipe.returnItemStackWithQuantity();
            //To put ingredients in place
            for(int i=0; i < itemStackList.size(); i++){
                int count =  itemStackList.get(i).getCount();
                if(this.inventory.getStack(i) == ItemStack.EMPTY) {

                    ItemStack stack = player.getInventory().getStack(player.getInventory().getSlotWithStack(itemStackList.get(i))).copyWithCount(count);

                    player.getInventory().getStack(player.getInventory().getSlotWithStack(itemStackList.get(i))).decrement(count);

                    this.inventory.setStack(i, stack);
                }
                else{
                    ItemStack stackInsideSlot = this.inventory.getStack(i);

                    if(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot) != -1){
                        player.getInventory().insertStack(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot), stackInsideSlot);
                        this.inventory.setStack(i, ItemStack.EMPTY);

                        ItemStack stack = player.getInventory().getStack(player.getInventory().getSlotWithStack(itemStackList.get(i))).copyWithCount(count);

                        player.getInventory().getStack(player.getInventory().getSlotWithStack(itemStackList.get(i))).decrement(count);

                        this.inventory.setStack(i, stack);
                    } else if(player.getInventory().getEmptySlot()!=-1){
                        player.getInventory().insertStack(player.getInventory().getEmptySlot(), stackInsideSlot);
                        this.inventory.setStack(i, ItemStack.EMPTY);

                        ItemStack stack = player.getInventory().getStack(player.getInventory().getSlotWithStack(itemStackList.get(i))).copyWithCount(count);

                        player.getInventory().getStack(player.getInventory().getSlotWithStack(itemStackList.get(i))).decrement(count);

                        this.inventory.setStack(i, stack);
                    }
                }
            }

            for(int i=itemStackList.size(); i < 5; i++){
                ItemStack stackInsideSlot = this.inventory.getStack(i);
                if(this.inventory.getStack(i) != ItemStack.EMPTY){

                    if(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot) != -1){
                        player.getInventory().insertStack(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot), stackInsideSlot);
                        this.inventory.setStack(i, ItemStack.EMPTY);
                    }
                    else if(player.getInventory().getEmptySlot()!=-1){
                        player.getInventory().insertStack(player.getInventory().getEmptySlot(), stackInsideSlot);
                    }
                }
            }

            //To put the base in place
            if(this.inventory.getStack(5) == ItemStack.EMPTY) {

                ItemStack base = player.getInventory().getStack(player.getInventory().getSlotWithStack(recipe.getBaseItem())).copyWithCount(1);

                player.getInventory().getStack(player.getInventory().getSlotWithStack(recipe.getBaseItem())).decrement(1);

                this.inventory.setStack(5, base);
            }
            else{
                ItemStack stackInsideSlot = this.inventory.getStack(5);
                if(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot) != -1){

                    player.getInventory().insertStack(player.getInventory().getOccupiedSlotWithRoomForStack(stackInsideSlot), stackInsideSlot);
                    this.inventory.setStack(5, ItemStack.EMPTY);

                    ItemStack base = player.getInventory().getStack(player.getInventory().getSlotWithStack(recipe.getBaseItem())).copyWithCount(1);

                    player.getInventory().getStack(player.getInventory().getSlotWithStack(recipe.getBaseItem())).decrement(1);

                    this.inventory.setStack(5, base);
                }
                else if(player.getInventory().getEmptySlot()!=-1){
                    player.getInventory().insertStack(player.getInventory().getEmptySlot(), stackInsideSlot);

                    this.inventory.setStack(5, ItemStack.EMPTY);

                    ItemStack base = player.getInventory().getStack(player.getInventory().getSlotWithStack(recipe.getBaseItem())).copyWithCount(1);

                    player.getInventory().getStack(player.getInventory().getSlotWithStack(recipe.getBaseItem())).decrement(1);

                    this.inventory.setStack(5, base);
                }
            }
        }

        player.getInventory().markDirty();
    }

    public static class PotionWitcherSlot extends Slot {
        public PotionWitcherSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        @Override
        public int getMaxItemCount() {
            return 5;
        }

    }
}
