package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.EmptyWitcherPotionItem;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @Unique
    private int potionTimer;

    @Unique
    private float oilDamageAdded = 0;

    @Unique
    private void OilCounter(PlayerEntity player, NbtCompound nbt){

        int Uses =  nbt.getInt("Uses");
        player.getMainHandStack().getSubNbt("Monster Oil").putInt("Uses", Uses-1);


        player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_HIT, 1, 1);
        if(player.getMainHandStack().getSubNbt("Monster Oil").getInt("Uses") == 0){

            player.getMainHandStack().getNbt().remove("Monster Oil");
            player.playSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK, 1, 1);
        }
    }

    @Unique
    private void LevelOilAssigner(NbtCompound monsterOil){
        switch (monsterOil.getInt("Level")){
            case 1:
                //For every point it's 3 more damage
                //So 2/3 it's two of damage
                oilDamageAdded = (float) 2/3;
                break;
            case 2:
                oilDamageAdded = (float) 4/3;
                break;
            case 3:
                oilDamageAdded = (float) 6/3;
                break;
            default:
                break;
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void injectMonsterOil(Entity target, CallbackInfo ci){
        PlayerEntity thisObject = (PlayerEntity)(Object)this;
        if(target instanceof LivingEntity){
            if(thisObject.getMainHandStack().hasNbt()){
                float h = thisObject.getAttackCooldownProgress(0.5f);
                NbtCompound nbt=thisObject.getMainHandStack().getNbt();

                assert nbt != null;
                if(nbt.contains("Monster Oil")){
                    NbtCompound monsterOil = thisObject.getMainHandStack().getSubNbt("Monster Oil");
                    if(monsterOil.contains("Id") && monsterOil.contains("Level") && monsterOil.contains("Uses")){
                        switch (monsterOil.getInt("Id")){
                            case 0:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.NECROPHAGES || ((LivingEntity) target).getGroup() == EntityGroup.UNDEAD){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 1:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.OGROIDS){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 2:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.SPECTERS){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 3:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.VAMPIRES){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 4:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.INSECTOIDS || ((LivingEntity) target).getGroup() == EntityGroup.ARTHROPOD){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 5:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.BEASTS || target instanceof AnimalEntity){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 6:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.ELEMENTA || target instanceof GolemEntity){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 7:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.CURSED_ONES){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 8:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.HYBRIDS){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 9:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.DRACONIDS){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 10:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.RELICTS){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 11:
                                if(((LivingEntity) target).getGroup() == EntityGroup.ILLAGER || target instanceof MerchantEntity || target instanceof WitchEntity){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            default:
                                break;

                        }
                    }
                    OilCounter(thisObject, monsterOil);
                }
            }
        }
    }


    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 0)
    private float injectMonsterOilAttack(float value){
        return value + oilDamageAdded;
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void ResetMultiplier(Entity target, CallbackInfo ci){
        oilDamageAdded = 0;
    }

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
