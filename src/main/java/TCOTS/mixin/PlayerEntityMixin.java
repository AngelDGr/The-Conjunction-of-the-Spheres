package TCOTS.mixin;

import TCOTS.items.TCOTS_Items;
import TCOTS.potions.EmptyWitcherPotionItem;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Final
    @Shadow
    private PlayerInventory inventory;

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    private int potionTimer;

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectPotionTimer(CallbackInfo ci){
        if (this.isSleeping()) {
            if(this.potionTimer < 100) {
                ++this.potionTimer;
            }
        } else {

            if(potionTimer != 0){
                potionTimer=0;
            }
        }
    }

    @Inject(method = "wakeUp(ZZ)V", at = @At("TAIL"))
    private void injectPotionRefilling(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci){
        if(((inventory.getSlotWithStack(TCOTS_Items.ALCOHEST.getDefaultStack()) != -1) || (inventory.getSlotWithStack(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack()) != -1)) && potionTimer>90){

            int loopP=0;
            boolean refilled=false;
            int slot=0;
            if(inventory.getSlotWithStack(TCOTS_Items.ALCOHEST.getDefaultStack()) != -1){
                //Refill
                loopP = 0;
                slot = inventory.getSlotWithStack(TCOTS_Items.ALCOHEST.getDefaultStack());
            }
            else if(inventory.getSlotWithStack(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack()) != -1){
                loopP = 2;
                slot = inventory.getSlotWithStack(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack());
            }

            //Makes a loop across all the inventory
            for(int i=0; i<inventory.size(); i++){
                //If found an Empty Potion with NBT
                if(inventory.getStack(i).getItem() instanceof EmptyWitcherPotionItem && inventory.getStack(i).hasNbt()){
                    NbtCompound nbtCompoundI= inventory.getStack(i).getNbt();
                    //Checks if the NBT contains the "Potion" string
                    if(nbtCompoundI.contains("Potion")){
                        //Save the potion type
                        Item PotionI = Registries.ITEM.get(new Identifier(nbtCompoundI.getString("Potion")));
                        //Saves the count of empty bottles
                        int countI = inventory.getStack(i).getCount();

                        //Erases the slot
                        inventory.getStack(i).decrement(inventory.getStack(i).getCount());
                        //Put the potion in the slot
                        inventory.setStack(i,new ItemStack(PotionI, countI));

                        //Increases in 1
                        loopP=loopP+1;

                        //Put the refilled boolean in true
                        refilled=true;

                        //If it has already filled 4 slots, it stops
                        if(loopP > 3){
                            //Decrements the alcohol in inventory
                            inventory.getStack(slot).decrement(1);
                            //Play a sound
                            playSound(TCOTS_Sounds.POTION_REFILLED, 3.0f, 1.0f);
                            //Breaks the loop
                            break;
                        }
                    }
                }

                //If it doesn't fulfill all the maximum potions
                if(i == inventory.size() - 1 && refilled){
                    //Decrements the alcohol in inventory
                    inventory.getStack(slot).decrement(1);
                    //Play a sound
                    playSound(TCOTS_Sounds.POTION_REFILLED, 3.0f, 1.0f);
                }
            }
        }
    }
}
