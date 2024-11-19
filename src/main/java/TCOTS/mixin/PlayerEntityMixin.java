package TCOTS.mixin;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.interfaces.PlayerEntityMixinInterface;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.items.concoctions.EmptyWitcherPotionItem;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.bombs.SamumBomb;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.world.TCOTS_DamageTypes;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityMixinInterface {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectToxicity(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir){
        cir.setReturnValue(cir.getReturnValue().add(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY));
    }

    @Final
    @Shadow
    PlayerInventory inventory;

    //Mud Things
    @Unique
    private static final TrackedData<Integer> MUD_TICKS = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectMudTicks(DataTracker.Builder builder, CallbackInfo ci){
        builder.add(MUD_TICKS, 0);
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

    @Inject(method = "attack", at = @At("HEAD"))
    private void injectMonsterOil(Entity target, CallbackInfo ci){

        if(target instanceof LivingEntity livingTarget){
            if(THIS.getMainHandStack().contains(TCOTS_Items.MONSTER_OIL_COMPONENT)){
                MonsterOilComponent monsterOil =THIS.getMainHandStack().get(TCOTS_Items.MONSTER_OIL_COMPONENT);
                switch (Objects.requireNonNull(monsterOil).groupId()){
                    case 0:
                        if(EntitiesUtil.isNecrophage(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 1:
                        if(EntitiesUtil.isOgroid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 2:
                        if(EntitiesUtil.isSpecter(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 3:
                        if(EntitiesUtil.isVampire(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 4:
                        if(EntitiesUtil.isInsectoid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 5:
                        if(EntitiesUtil.isBeast(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 6:
                        if(EntitiesUtil.isElementa(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 7:
                        if(EntitiesUtil.isCursedOne(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 8:
                        if(EntitiesUtil.isHybrid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 9:
                        if(EntitiesUtil.isDraconid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 10:
                        if(EntitiesUtil.isRelict(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 11:
                        if(EntitiesUtil.isHumanoid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    default:
                        break;
                }

                OilUsesManager(THIS, monsterOil);
            }
        }
    }

    @Unique
    private void LevelOilAssigner(MonsterOilComponent monsterOil){
        switch (monsterOil.level()){
            case 1:
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

    @Unique
    private void OilUsesManager(PlayerEntity player, MonsterOilComponent monsterOil){
        ItemStack weapon = player.getMainHandStack();

        MonsterOilComponent newMonsterOil= MonsterOilComponent.decreaseUse(monsterOil);
        weapon.set(TCOTS_Items.MONSTER_OIL_COMPONENT, newMonsterOil);

        if(newMonsterOil.uses()==0){
            weapon.remove(TCOTS_Items.MONSTER_OIL_COMPONENT);

            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TCOTS_Sounds.OIL_RAN_OUT, player.getSoundCategory(), 1, 1);
        }
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectMonsterOilAttack(float value, @Local(argsOnly = true) Entity target){
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
    @Unique
    PlayerEntity THIS = (PlayerEntity) (Object) this;

    @Inject(method = "wakeUp(ZZ)V", at = @At("TAIL"))
    private void injectPotionRefilling(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci){

        boolean refilled=false;

        //Iterates the list with all the alcohols
        for(WitcherAlcohol_Base alcoholBase: list_alcohol){
            //Get if the player has some alcohol
            if(((inventory.getSlotWithStack(alcoholBase.getDefaultStack()) != -1))
                    && !refilled
                    && potionTimer>90){
                int loopP = EntitiesUtil.isWearingManticoreArmor(THIS)? alcoholBase.getRefillQuantity()+2 : alcoholBase.getRefillQuantity();

                int slot = inventory.getSlotWithStack(alcoholBase.getDefaultStack());

                //Makes a loop across all the inventory
                for(int i=0; i<inventory.size(); i++){
                    //If found an Empty Potion with NBT
                    if(inventory.getStack(i).getItem() instanceof EmptyWitcherPotionItem && inventory.getStack(i).contains(TCOTS_Items.REFILL_RECIPE)){
                        String refillItem= inventory.getStack(i).get(TCOTS_Items.REFILL_RECIPE);
                        //Checks if the NBT contains the "Potion" string

                        if(refillItem!=null){
                            //Save the potion type
                            Item PotionI = Registries.ITEM.get(Identifier.of(refillItem));
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

                            //If it has already filled the slots, it stops
                            if(loopP < 1){
                                //Decrements the alcohol in inventory
                                inventory.getStack(slot).decrement(1);
                                //Triggers the advancement
                                if(THIS instanceof ServerPlayerEntity serverPlayer) TCOTS_Criteria.REFILL_CONCOCTION.trigger(serverPlayer);
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
                        //Triggers the advancement
                        if(THIS instanceof ServerPlayerEntity serverPlayer) TCOTS_Criteria.REFILL_CONCOCTION.trigger(serverPlayer);
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
    private void injectMariborForestImprove(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir){
        PlayerEntity THIS = (PlayerEntity)(Object)this;
        if(this.hasStatusEffect(TCOTS_Effects.MARIBOR_FOREST_EFFECT)){
            int amplifier = Objects.requireNonNull(this.getStatusEffect(TCOTS_Effects.MARIBOR_FOREST_EFFECT)).getAmplifier();
            if(stack.contains(DataComponentTypes.FOOD)){
                assert foodComponent != null;

                int foodQuantity= (int) (foodComponent.nutrition()*(0.25f+(0.25f*amplifier)));
                float saturationQuantity= foodComponent.saturation()*(0.25f+(0.25f*amplifier));

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
    private static final TrackedData<Boolean> HUD_ACTIVE = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Float> HUD_TRANSPARENCY = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);


    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectToxicityDataTracker(DataTracker.Builder builder, CallbackInfo ci){
        builder.add(TOXICITY, 0);
        builder.add(DECOCTION_TOXICITY, 0);
        builder.add(HUD_ACTIVE, false);
        builder.add(HUD_TRANSPARENCY, 0.0f);
    }

    @Override
    public int theConjunctionOfTheSpheres$getNormalToxicity(){
        return this.dataTracker.get(TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setToxicity(int toxicity){
        this.dataTracker.set(TOXICITY,toxicity);
    }

    @Override
    public int theConjunctionOfTheSpheres$getMaxToxicity(){
        return (int) THIS.getAttributeValue(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY);
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
    public void theConjunctionOfTheSpheres$addToxicity(int toxicity,boolean decoction) {
        if(decoction){
            theConjunctionOfTheSpheres$setDecoctionToxicity(theConjunctionOfTheSpheres$getDecoctionToxicity()+toxicity);
        }
        else {
            theConjunctionOfTheSpheres$setToxicity(theConjunctionOfTheSpheres$getNormalToxicity()+toxicity);
        }
    }

    @Override
    public void theConjunctionOfTheSpheres$decreaseToxicity(int toxicity, boolean decoction) {
        if(decoction){
            theConjunctionOfTheSpheres$setDecoctionToxicity(theConjunctionOfTheSpheres$getDecoctionToxicity()-toxicity);
        }
        else {
            theConjunctionOfTheSpheres$setToxicity(theConjunctionOfTheSpheres$getNormalToxicity()-toxicity);
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
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectWriteNBTToxicity(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("Toxicity", theConjunctionOfTheSpheres$getNormalToxicity());
        nbt.putInt("DecoctionToxicity",theConjunctionOfTheSpheres$getDecoctionToxicity());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectInTickDecreaseToxicityIfOverMaximum(CallbackInfo ci){
      if(this.theConjunctionOfTheSpheres$getAllToxicity() > this.theConjunctionOfTheSpheres$getMaxToxicity()){
          THIS.theConjunctionOfTheSpheres$setToxicity(MathHelper.clamp(
                  THIS.theConjunctionOfTheSpheres$getNormalToxicity(),
                  0,
                  THIS.theConjunctionOfTheSpheres$getMaxToxicity() - THIS.theConjunctionOfTheSpheres$getDecoctionToxicity()));
      }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickDecreaseToxicity(CallbackInfo ci){
        if (this.theConjunctionOfTheSpheres$getNormalToxicity()>0) {
            if(this.age%40==0){
                this.theConjunctionOfTheSpheres$decreaseToxicity(1,false);
            }
        }


        float dangerOverdoseThreshold=(this.theConjunctionOfTheSpheres$getMaxToxicity()*0.9f);
        if(this.theConjunctionOfTheSpheres$toxicityOverThreshold()){
            //At 75%,  every 40 ticks
            //At 80%,  every 37.5 ticks
            //At 85%,  every 35.29 ticks
            int damageableTicks = (int) (30 * ((float) theConjunctionOfTheSpheres$getMaxToxicity() / (float) theConjunctionOfTheSpheres$getAllToxicity()));

            //At 90%,  every 11.11 ticks
            //At 100%, every 10 ticks
            if(theConjunctionOfTheSpheres$getAllToxicity() > dangerOverdoseThreshold) {
                damageableTicks = (int) (10 * ((float) theConjunctionOfTheSpheres$getMaxToxicity() / (float) theConjunctionOfTheSpheres$getAllToxicity()));
            }

            if(this.age%(damageableTicks)==0){
            this.damage(TCOTS_DamageTypes.toxicityDamage(getWorld()),1);
            }
        }
    }


    //SamumEffect
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean injectCriticalWithSamum(boolean value, @Local(argsOnly = true) Entity target){
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

    //Wolf & Rook Effects
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
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0),
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


    //Raven Armor
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectRavenBonusToDamage(float value, @Local(argsOnly = true) Entity target){
        return value + (
                EntitiesUtil.isWearingRavensArmor(THIS)
                        && target instanceof LivingEntity livingTarget
                        && EntitiesUtil.isMonster(livingTarget)? 2.0f : 0.0f);
    }


    //Moonblade
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectMoonBladeDamage(float value, @Local(argsOnly = true) Entity target){
        return value*(
                THIS.getMainHandStack().getItem() == TCOTS_Items.MOONBLADE
                        && target instanceof LivingEntity livingTarget
                        && EntitiesUtil.isMonster(livingTarget)? 1.5f : 1.0f);
    }

    //Winter's Blade
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectWintersBladeDamage(float value, @Local(argsOnly = true) Entity target){
        return value + (
                THIS.getMainHandStack().getItem() == TCOTS_Items.WINTERS_BLADE
                && target instanceof LivingEntity livingTarget
                && livingTarget.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)?
                        2.0f
                        : 0.0f);
    }

    //Witcher Eyes
    @Unique
    private static final TrackedData<Boolean> EYES_ACTIVATE = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Vector3f> EYES_POSITION = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.VECTOR3F);
    @Unique
    private static final TrackedData<Integer> EYES_SEPARATION = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> EYES_SHAPE = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);


    @Unique
    private static final TrackedData<Boolean> TOXICITY_ACTIVATE = DataTracker.registerData(PlayerEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);


    @Override
    public boolean theConjunctionOfTheSpheres$getWitcherEyesActivated(){return this.dataTracker.get(EYES_ACTIVATE);}
    @Override
    public void theConjunctionOfTheSpheres$setWitcherEyesActivated(boolean activate){this.dataTracker.set(EYES_ACTIVATE, activate);}

    @Override
    public boolean theConjunctionOfTheSpheres$getToxicityActivated() {return this.dataTracker.get(TOXICITY_ACTIVATE);}

    @Override
    public void theConjunctionOfTheSpheres$setToxicityActivated(boolean activate) {this.dataTracker.set(TOXICITY_ACTIVATE, activate);}

    @Override
    public Vector3f theConjunctionOfTheSpheres$getEyesPivot(){return this.dataTracker.get(EYES_POSITION);}
    @Override
    public void theConjunctionOfTheSpheres$setEyesPivot(Vector3f vector3f){this.dataTracker.set(EYES_POSITION,vector3f);}

    @Override
    public int theConjunctionOfTheSpheres$getEyeSeparation(){return this.dataTracker.get(EYES_SEPARATION);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeSeparation(int separation){this.dataTracker.set(EYES_SEPARATION, separation);}

    @Override
    public int theConjunctionOfTheSpheres$getEyeShape(){return this.dataTracker.get(EYES_SHAPE);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeShape(int shape){this.dataTracker.set(EYES_SHAPE, shape);}

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void injectWitcherEyesData(DataTracker.Builder builder, CallbackInfo ci){
        builder.add(EYES_ACTIVATE,   false);
        builder.add(EYES_POSITION,   new Vector3f(0,0,0));
        builder.add(EYES_SEPARATION, 2);
        builder.add(EYES_SHAPE,      0);
        builder.add(TOXICITY_ACTIVATE, false);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectReadNBTWitcherEyes(NbtCompound nbt, CallbackInfo ci){
        NbtCompound nbtEyes = getSubNbt("WitcherEyes", nbt);
        if(nbtEyes!=null) {
            this.theConjunctionOfTheSpheres$setWitcherEyesActivated(nbtEyes.getBoolean("Activated"));

            this.theConjunctionOfTheSpheres$setEyesPivot(new Vector3f(nbtEyes.getFloat("EyesX"), nbtEyes.getFloat("EyesY"), 0));

            this.theConjunctionOfTheSpheres$setEyeSeparation(nbtEyes.getInt("EyesSeparation"));

            this.theConjunctionOfTheSpheres$setEyeShape(nbtEyes.getInt("EyesShape"));
        }

        NbtCompound nbtToxicity = getSubNbt("ToxicityFace", nbt);

        if(nbtToxicity!=null){
            this.theConjunctionOfTheSpheres$setToxicityActivated(nbtToxicity.getBoolean("Activated"));
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectWriteNBTWitcherEyes(NbtCompound nbt, CallbackInfo ci){
        NbtCompound nbtEyes = new NbtCompound();

        nbtEyes.putBoolean("Activated", this.theConjunctionOfTheSpheres$getWitcherEyesActivated());

        nbtEyes.putFloat("EyesX", this.theConjunctionOfTheSpheres$getEyesPivot().x);
        nbtEyes.putFloat("EyesY", this.theConjunctionOfTheSpheres$getEyesPivot().y);

        nbtEyes.putInt("EyesSeparation", this.theConjunctionOfTheSpheres$getEyeSeparation());
        nbtEyes.putInt("EyesShape", this.theConjunctionOfTheSpheres$getEyeShape());

        nbt.put("WitcherEyes", nbtEyes);

        NbtCompound nbtToxicity = new NbtCompound();

        nbtToxicity.putBoolean("Activated", this.theConjunctionOfTheSpheres$getToxicityActivated());

        nbt.put("ToxicityFace", nbtToxicity);
    }

    @SuppressWarnings("all")
    @Unique
    @Nullable
    private static NbtCompound getSubNbt(String key, NbtCompound compound) {
        if (compound == null || !compound.contains(key, NbtElement.COMPOUND_TYPE)) {
            return null;
        }
        return compound.getCompound(key);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void testing(CallbackInfo ci){

    }

}
