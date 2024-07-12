package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.interfaces.PlayerEntityMixinInterface;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.potions.EmptyWitcherPotionItem;
import TCOTS.items.potions.TCOTS_Effects;
import TCOTS.items.potions.WitcherAlcohol_Base;
import TCOTS.items.potions.bombs.SamumBomb;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

//@Debug(export = true) // Enables exporting for the targets of this mixin
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityMixinInterface {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Final
    @Shadow
    private PlayerInventory inventory;

    //Mud Things
    @Unique
    private static final TrackedData<Integer> MUD_TICKS = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectMudTicks(CallbackInfo ci){
        this.dataTracker.startTracking(MUD_TICKS, 0);
    }

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow public abstract Arm getMainArm();

    @Override
    public int theConjunctionOfTheSpheres$getMudInFace() {
        return this.dataTracker.get(MUD_TICKS);
    }

    @Override
    public void theConjunctionOfTheSpheres$setMudInFace(int ticks) {
        this.dataTracker.set(MUD_TICKS, ticks);
    }

    @Override
    public float theConjunctionOfTheSpheres$getMudTransparency() {
        return  (float) theConjunctionOfTheSpheres$getMudInFace()/100;
    }



    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickMud(CallbackInfo ci){

        if(this.theConjunctionOfTheSpheres$getMudInFace() > 0 && this.isTouchingWaterOrRain()){
            theConjunctionOfTheSpheres$setMudInFace(theConjunctionOfTheSpheres$getMudInFace() - 10);
        }
        else if(this.theConjunctionOfTheSpheres$getMudInFace() > 0){
            theConjunctionOfTheSpheres$setMudInFace(theConjunctionOfTheSpheres$getMudInFace() - 1);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectReadNBTMud(NbtCompound nbt, CallbackInfo ci){
        theConjunctionOfTheSpheres$setMudInFace(nbt.getInt("MudTicks"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectWriteNBTMud(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("MudTicks", theConjunctionOfTheSpheres$getMudInFace());
    }

    //Oils
    @Unique
    private float oilDamageAdded = 0;

    @Unique
    private void OilCounter(PlayerEntity player, NbtCompound nbt){

        int Uses =  nbt.getInt("Uses");
        Objects.requireNonNull(player.getMainHandStack().getSubNbt("Monster Oil")).putInt("Uses", Uses-1);


        if(Objects.requireNonNull(player.getMainHandStack().getSubNbt("Monster Oil")).getInt("Uses") == 0){

            Objects.requireNonNull(player.getMainHandStack().getNbt()).remove("Monster Oil");
            player.playSound(TCOTS_Sounds.OIL_RAN_OUT, 1, 2);
        }
    }

    @Unique
    private void LevelOilAssigner(NbtCompound monsterOil){
        switch (monsterOil.getInt("Level")){
            case 1:
                //For every point it's 3 more damage
                //So 2/3 it's two of damage
                oilDamageAdded = 2f;
                break;
            case 2:
                oilDamageAdded = 4f;
                break;
            case 3:
                oilDamageAdded = 6f;
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
                NbtCompound nbt=thisObject.getMainHandStack().getNbt();

                assert nbt != null;
                if(nbt.contains("Monster Oil")){
                    NbtCompound monsterOil = thisObject.getMainHandStack().getSubNbt("Monster Oil");
                    assert monsterOil != null;
                    if(monsterOil.contains("Id") && monsterOil.contains("Level") && monsterOil.contains("Uses")){
                        switch (monsterOil.getInt("Id")){
                            case 0:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.NECROPHAGES || ((LivingEntity) target).getGroup() == EntityGroup.UNDEAD){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 1:
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.OGROIDS || target instanceof AbstractPiglinEntity){
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
                                if(((LivingEntity) target).getGroup() == TCOTS_Entities.BEASTS || target instanceof AnimalEntity || target instanceof RavagerEntity){
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
                                if(((LivingEntity) target).getGroup() == EntityGroup.ILLAGER || target instanceof MerchantEntity || target instanceof WitchEntity || target instanceof PlayerEntity){
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

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectMonsterOilAttack(float value){
        return value + oilDamageAdded;
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void ResetMultiplier(Entity target, CallbackInfo ci){
        oilDamageAdded = 0;
    }

    //Refilling Alcohol
    @Unique
    private int potionTimer;

    @Unique
    List<WitcherAlcohol_Base> list_alcohol = Arrays.asList(
            TCOTS_Items.ALCOHEST,

            TCOTS_Items.VILLAGE_HERBAL,
            TCOTS_Items.CHERRY_CORDIAL,
            TCOTS_Items.MANDRAKE_CORDIAL,

            TCOTS_Items.ICY_SPIRIT,
            TCOTS_Items.DWARVEN_SPIRIT,
            TCOTS_Items.WHITE_GULL
    );

    @Inject(method = "wakeUp(ZZ)V", at = @At("TAIL"))
    private void injectPotionRefilling(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci){

        boolean refilled=false;

        //Iterates the list with all the alcohols
        for(WitcherAlcohol_Base alcoholBase: list_alcohol){
            //Get if the player has some alcohol
            if(((inventory.getSlotWithStack(alcoholBase.getDefaultStack()) != -1))
                    && !refilled
                    && potionTimer>90){
                int loopP = alcoholBase.getRefillQuantity();

                int slot = inventory.getSlotWithStack(alcoholBase.getDefaultStack());

                //Makes a loop across all the inventory
                for(int i=0; i<inventory.size(); i++){
                    //If found an Empty Potion with NBT
                    if(inventory.getStack(i).getItem() instanceof EmptyWitcherPotionItem && inventory.getStack(i).hasNbt()){
                        NbtCompound nbtCompoundI= inventory.getStack(i).getNbt();
                        //Checks if the NBT contains the "Potion" string
                        assert nbtCompoundI != null;
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
                            loopP=loopP-1;

                            //Put the refilled boolean in true
                            refilled=true;

                            //If it has already filled 4 slots, it stops
                            if(loopP < 1){
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

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickSleepingPotion(CallbackInfo ci){
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

    //Maribor Forest
    @Inject(method = "eatFood", at = @At("TAIL"))
    private void injectMariborForestImprove(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        PlayerEntity THIS = (PlayerEntity)(Object)this;
        if(this.hasStatusEffect(TCOTS_Effects.MARIBOR_FOREST_EFFECT)){
            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.MARIBOR_FOREST_EFFECT)).getAmplifier();
            if(stack.getItem().isFood()){
                FoodComponent foodComponent = stack.getItem().getFoodComponent();
                assert foodComponent != null;

                int foodQuantity= (int) (foodComponent.getHunger()*(0.25f+(0.25f*amplifier)));
                float saturationQuantity= foodComponent.getSaturationModifier()*(0.25f+(0.25f*amplifier));

                if(foodQuantity < 1){
                    foodQuantity=1;
                }

                if(saturationQuantity < 0.1){
                    saturationQuantity=0.1f;
                }

                THIS.getHungerManager().add(foodQuantity, saturationQuantity);
            }
        }
    }


    //Toxicity Logic
    @Unique
    private static final TrackedData<Integer> TOXICITY = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> DECOCTION_TOXICITY = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> MAX_TOXICITY = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Boolean> HUD_ACTIVE = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Float> HUD_TRANSPARENCY = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectToxicityDataTracker(CallbackInfo ci){
        this.dataTracker.startTracking(TOXICITY, 0);
        this.dataTracker.startTracking(DECOCTION_TOXICITY, 0);
        this.dataTracker.startTracking(MAX_TOXICITY,100);
        this.dataTracker.startTracking(HUD_ACTIVE, false);
        this.dataTracker.startTracking(HUD_TRANSPARENCY, 0.0f);
    }

    @Override
    public int theConjunctionOfTheSpheres$getToxicity(){
        return this.dataTracker.get(TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setToxicity(int toxicity){
        this.dataTracker.set(TOXICITY,toxicity);
    }

    @Override
    public int theConjunctionOfTheSpheres$getMaxToxicity(){
        return this.dataTracker.get(MAX_TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setMaxToxicity(int MaxToxicity) {
        this.dataTracker.set(MAX_TOXICITY,MaxToxicity);
    }

    @Override
    public int theConjunctionOfTheSpheres$getDecoctionToxicity() {
        return this.dataTracker.get(DECOCTION_TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setDecoctionToxicity(int DecoctionToxicity) {
        this.dataTracker.set(DECOCTION_TOXICITY,DecoctionToxicity);
    }

    @Override
    public void theConjunctionOfTheSpheres$addMaxToxicity(int MaxToxicity){
        this.dataTracker.set(MAX_TOXICITY,theConjunctionOfTheSpheres$getMaxToxicity()+MaxToxicity);
    }

    @Override
    public void theConjunctionOfTheSpheres$addToxicity(int toxicity,boolean decoction) {
        if(decoction){
            theConjunctionOfTheSpheres$setDecoctionToxicity(theConjunctionOfTheSpheres$getDecoctionToxicity()+toxicity);
        }
        else {
            theConjunctionOfTheSpheres$setToxicity(theConjunctionOfTheSpheres$getToxicity()+toxicity);
        }
    }

    @Override
    public void theConjunctionOfTheSpheres$decreaseToxicity(int toxicity, boolean decoction) {
        if(decoction){
            theConjunctionOfTheSpheres$setDecoctionToxicity(theConjunctionOfTheSpheres$getDecoctionToxicity()-toxicity);
        }
        else {
            theConjunctionOfTheSpheres$setToxicity(theConjunctionOfTheSpheres$getToxicity()-toxicity);
        }
    }

    @Override
    public int theConjunctionOfTheSpheres$getAllToxicity() {
        return this.dataTracker.get(DECOCTION_TOXICITY)+this.dataTracker.get(TOXICITY);
    }

    @Override
    public boolean theConjunctionOfTheSpheres$toxicityOverThreshold() {
        float overdoseThreshold=(this.theConjunctionOfTheSpheres$getMaxToxicity()*0.75f);
        return this.theConjunctionOfTheSpheres$getAllToxicity() > overdoseThreshold;
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectReadNBTToxicity(NbtCompound nbt, CallbackInfo ci){
        theConjunctionOfTheSpheres$setToxicity(nbt.getInt("Toxicity"));
        theConjunctionOfTheSpheres$setDecoctionToxicity(nbt.getInt("DecoctionToxicity"));

        theConjunctionOfTheSpheres$setMaxToxicity(nbt.getInt("MaxToxicity"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectWriteNBTToxicity(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("Toxicity",theConjunctionOfTheSpheres$getToxicity());
        nbt.putInt("DecoctionToxicity",theConjunctionOfTheSpheres$getDecoctionToxicity());

        nbt.putInt("MaxToxicity",theConjunctionOfTheSpheres$getMaxToxicity());

    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickDecreaseToxicity(CallbackInfo ci){
        if (this.theConjunctionOfTheSpheres$getToxicity()>0) {
            if(this.age%40==0){
                this.theConjunctionOfTheSpheres$decreaseToxicity(1,false);
            }
        }


        float dangerOverdoseThreshold=(this.theConjunctionOfTheSpheres$getMaxToxicity()*0.9f);
        if(this.theConjunctionOfTheSpheres$toxicityOverThreshold()){
            int damageableTicks = (int) (50 * ((float) theConjunctionOfTheSpheres$getMaxToxicity() / (float) theConjunctionOfTheSpheres$getAllToxicity()));

            if(theConjunctionOfTheSpheres$getAllToxicity() > dangerOverdoseThreshold) {
                damageableTicks = (int) (20 * ((float) theConjunctionOfTheSpheres$getMaxToxicity() / (float) theConjunctionOfTheSpheres$getAllToxicity()));
            }
            if(this.age%(damageableTicks)==0){
            this.damage(TCOTS_DamageTypes.toxicityDamage(getWorld()),1);
            }

        }
    }


    //SamumEffect
    @Unique
    private Entity target;

    @Inject(method = "attack", at = @At("HEAD"))
    private void getTarget(Entity target, CallbackInfo ci){
        this.target=target;
    }


    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean injectCriticalWithSamum(boolean value){
        if(target instanceof LivingEntity entity){
            if(SamumBomb.checkSamumEffect(entity)){
                StatusEffectInstance instance = entity.getStatusEffect(TCOTS_Effects.SAMUM_EFFECT);
                assert instance != null;
                int amplifier = instance.getAmplifier();

                return value || amplifier > 1;
            }
        }



        return value;
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5f))
    private float injectExtraCriticalWolf(float value){
        if(this.hasStatusEffect(TCOTS_Effects.WOLF_EFFECT)){
            //Wolf I:   -> 2.0f
            //Wolf II:  -> 2.5f
            //Wolf III: -> 3.0f

            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.WOLF_EFFECT)).getAmplifier();
            return value + (0.5f + (amplifier*0.5f));
        }

        return value;
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float addExtraSwordDamageRook(float value){
        if(this.getMainHandStack().getItem() instanceof SwordItem && this.hasStatusEffect(TCOTS_Effects.ROOK_EFFECT)){
            //Rook I:   -> +2
            //Rook II:  -> +3
            //Rook III: -> +4

            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.ROOK_EFFECT)).getAmplifier();
            return value + (2 + (amplifier));
        }

        return value;
    }
}
