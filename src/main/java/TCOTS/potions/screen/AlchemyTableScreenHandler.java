package TCOTS.potions.screen;

import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.potions.recipes.AlchemyTableRecipesRegister;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class AlchemyTableScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final AlchemyTableBlockEntity blockEntity;


    public AlchemyTableScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, inventory.player.getWorld().getBlockEntity(buf.readBlockPos()),
                new ArrayPropertyDelegate(6));
    }

    public AlchemyTableScreenHandler(int syncId, PlayerInventory playerInventory,
                                     BlockEntity blockEntity, PropertyDelegate arrayPropertyDelegate) {
        super(AlchemyTableRecipesRegister.ALCHEMY_TABLE_SCREEN_HANDLER, syncId);

        checkSize((Inventory) blockEntity, 6);

        this.inventory = ((Inventory) blockEntity);
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = arrayPropertyDelegate;
        this.blockEntity = ((AlchemyTableBlockEntity) blockEntity);


//        //Ingredients
//        this.addSlot(new Slot(inventory, 0, 31,  17));
//        this.addSlot(new Slot(inventory, 1, 55,  17));
//        this.addSlot(new Slot(inventory, 2, 79,  17));
//        this.addSlot(new Slot(inventory, 3, 103, 17));
//        this.addSlot(new Slot(inventory, 4, 127, 17));

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

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);  // Max Progress
        int progressArrowSize = 26; // This is the width in pixels of your arrow

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
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

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public static class PotionWitcherSlot extends Slot {
        public PotionWitcherSlot(Inventory inventory, int i, int j, int k) {
            super(inventory, i, j, k);
        }

        @Override
        public int getMaxItemCount() {
            return 1;
        }

    }

}
