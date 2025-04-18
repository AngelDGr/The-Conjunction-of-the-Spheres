package TCOTS.screen;

import TCOTS.items.concoctions.recipes.HerbalTableRecipe;
import TCOTS.items.concoctions.recipes.ScreenHandlersAndRecipesRegister;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import org.jetbrains.annotations.NotNull;

public class HerbalTableScreenHandler extends AbstractContainerMenu {

    private final SimpleContainer inputInventory = new SimpleContainer(2){
        @Override
        public void setChanged() {
            super.setChanged();
            HerbalTableScreenHandler.this.slotsChanged(this);
        }
    };
    private final HerbalTableResultInventory resultInventory = new HerbalTableResultInventory();
    private final ContainerLevelAccess context;
    private final Player player;

    public HerbalTableScreenHandler(int syncId, Inventory inventory) {
        this(syncId, inventory, ContainerLevelAccess.NULL);
    }

    public HerbalTableScreenHandler(int syncId, Inventory playerInventory, final ContainerLevelAccess context) {
        super(ScreenHandlersAndRecipesRegister.HERBAL_TABLE_SCREEN_HANDLER, syncId);
        this.context=context;
        this.player = playerInventory.player;
        this.addSlot(new Slot(inputInventory, 0, 27,47){
            @Override
            public int getMaxStackSize() {
                return 5;
            }
        });
        this.addSlot(new Slot(inputInventory, 1, 76,47){
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
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
        if(!stack.has(DataComponents.POTION_CONTENTS)){
            return false;
        }

        PotionContents potionContents = stack.get(DataComponents.POTION_CONTENTS);

         return potionContents!=null &&
                 (potionContents.potion().get().equals(Potions.WATER)
                 || potionContents.potion().get().equals(Potions.THICK)
                 || potionContents.potion().get().equals(Potions.MUNDANE));
    }

    @Override
    protected boolean moveItemStackTo(@NotNull ItemStack stackToInsert, int startIndex, int endIndex, boolean fromLast) {
        if(startIndex==0){
            if(stackToInsert.getCount() + this.slots.get(0).getItem().getCount() <=5 && !isFitPotion(stackToInsert)){
                return super.moveItemStackTo(stackToInsert, startIndex, endIndex, fromLast);
            } else {
                return false;
            }
        }

        return super.moveItemStackTo(stackToInsert, startIndex, endIndex, fromLast);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {

            ItemStack stackInSlot = slot.getItem();
            itemStack = stackInSlot.copy();

            //If it's the result slot
            if (slotIndex == 2) {
                this.context.execute((world, pos) -> stackInSlot.getItem().onCraftedBy(stackInSlot, world, player));

                if (!this.moveItemStackTo(stackInSlot, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stackInSlot, itemStack);

            } else if (slotIndex != 1 && isFitPotion(stackInSlot) && !this.slots.get(1).hasItem()) {
                if(!this.moveItemStackTo(stackInSlot,1,2, true))
                    return ItemStack.EMPTY;

            } else if (
                    //If the slots are in the inventory, tries to put them in the slots of the crafting screen
                    slotIndex >= 3 && slotIndex < 39?
                    !this.moveItemStackTo(stackInSlot, 0, 1, false)
                    //If the slots are outside hotbar
                    && (slotIndex < 30 ? !this.moveItemStackTo(stackInSlot, 30, 39, false)
                    : !this.moveItemStackTo(stackInSlot, 3, 30, false))
                    : !this.moveItemStackTo(stackInSlot, 3, 39, false)) {
                return ItemStack.EMPTY;
            }
            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stackInSlot.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stackInSlot);
            if (slotIndex == 0) {
                player.drop(stackInSlot, false);
            }
        }
        return itemStack;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.context.execute((world, pos) -> this.clearContainer(player, this.inputInventory));
    }

    @Override
    public void slotsChanged(@NotNull Container inventory) {
        this.context.execute((world, pos) -> HerbalTableScreenHandler.updateResult(
                this,
                world,
                this.player,
                new HerbalTableRecipe.HerbalTableInventory(this.inputInventory.getItem(0),
                        this.inputInventory.getItem(1)) ,
                this.resultInventory));
    }

    protected static void updateResult(AbstractContainerMenu handler, Level world, Player player, HerbalTableRecipe.HerbalTableInventory craftingInventory, HerbalTableResultInventory resultInventory) {
        if (world.isClientSide) {
            return;
        }

        ServerPlayer serverPlayerEntity = (ServerPlayer) player;
        ItemStack outputItem = ItemStack.EMPTY;

        if (world.getServer() == null) return;

        //Crafting
        Optional<RecipeHolder<HerbalTableRecipe>> optional = world.getServer().getRecipeManager().getRecipeFor(ScreenHandlersAndRecipesRegister.HERBAL_TABLE, craftingInventory, world);
        if (optional.isPresent()) {
            ItemStack itemStack2;
            RecipeHolder<HerbalTableRecipe> recipeEntry = optional.get();
            HerbalTableRecipe craftingRecipe = recipeEntry.value();
            if(player instanceof ServerPlayer){
                if ((itemStack2 = craftingRecipe.assemble(craftingInventory, world.registryAccess())).isItemEnabled(world.enabledFeatures())) {
                    outputItem = itemStack2;
                }
            }
        }

        resultInventory.setItem(2, outputItem);
        handler.setRemoteSlot(2, outputItem);
        serverPlayerEntity.connection.send(new ClientboundContainerSetSlotPacket(handler.containerId, handler.incrementStateId(), 2, outputItem));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public boolean canDragTo(Slot slot) {
        return slot.getContainerSlot() != 2;
    }

    public static class MixtureOutputSlot extends Slot{
        private final Container input;
        private final Player player;
        private int amount;

        public MixtureOutputSlot(Player player, HerbalTableResultInventory result, SimpleContainer input, int index, int x, int y) {
            super(result, index, x, y);
            this.player = player;
            this.input=input;
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
            if (this.amount > 0) {
                stack.onCraftedBy(this.player.level(), this.player, this.amount);
            }
            this.amount = 0;
        }

        @Override
        public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
            this.checkTakeAchievements(stack);
            List<ItemStack> inventoryStacksList = List.of(this.input.getItem(0), this.input.getItem(1));

            for (int i = 0; i < inventoryStacksList.size(); ++i) {
                ItemStack itemStack = this.input.getItem(i);
                ItemStack itemStack2 = inventoryStacksList.get(i);
                if (!itemStack.isEmpty()) {
                    this.input.removeItem(i, this.input.getItem(i).getCount());
                    itemStack = this.input.getItem(i);
                }
                if (itemStack2.isEmpty()) continue;
                if (itemStack.isEmpty()) {
                    this.input.setItem(i, itemStack2);
                    continue;
                }
                if (ItemStack.isSameItemSameComponents(itemStack, itemStack2)) {
                    itemStack2.grow(itemStack.getCount());
                    this.input.setItem(i, itemStack2);
                    continue;
                }
                if (this.player.getInventory().add(itemStack2)) continue;
                this.player.drop(itemStack2, false);
            }

            player.level().levelEvent(LevelEvent.SOUND_BREWING_STAND_BREW, player.blockPosition(), 0);
        }
    }

    public static class HerbalTableResultInventory implements Container {
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
