package TCOTS.mixin;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.interfaces.PlayerEntityMixinInterface;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.items.concoctions.EmptyWitcherPotionItem;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.bombs.SamumBomb;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil_Fabric;
import TCOTS.world.TCOTS_DamageTypes;
import com.llamalad7.mixinextras.sugar.Local;
import org.jetbrains.annotations.NotNull;
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
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityMixinInterface {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectToxicity(CallbackInfoReturnable<AttributeSupplier.Builder> cir){
        cir.setReturnValue(cir.getReturnValue().add(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY));
    }

    @Final
    @Shadow
    Inventory inventory;

    //Mud Things
    @Unique
    private static final EntityDataAccessor<Integer> MUD_TICKS = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectMudTicks(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(MUD_TICKS, 0);
    }

    @Shadow public abstract void playSound(@NotNull SoundEvent sound, float volume, float pitch);

    @Shadow public abstract @NotNull HumanoidArm getMainArm();

    @Override
    public int theConjunctionOfTheSpheres$getMudInFace() {
        return this.entityData.get(MUD_TICKS);
    }

    @Override
    public void theConjunctionOfTheSpheres$setMudInFace(int ticks) {
        this.entityData.set(MUD_TICKS, ticks);
    }

    @Override
    public float theConjunctionOfTheSpheres$getMudTransparency() {
        return  (float) theConjunctionOfTheSpheres$getMudInFace()/100;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickMud(CallbackInfo ci){

        if(this.theConjunctionOfTheSpheres$getMudInFace() > 0 && this.isInWaterOrRain()){
            theConjunctionOfTheSpheres$setMudInFace(theConjunctionOfTheSpheres$getMudInFace() - 10);
        }
        else if(this.theConjunctionOfTheSpheres$getMudInFace() > 0){
            theConjunctionOfTheSpheres$setMudInFace(theConjunctionOfTheSpheres$getMudInFace() - 1);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectReadNBTMud(CompoundTag nbt, CallbackInfo ci){
        theConjunctionOfTheSpheres$setMudInFace(nbt.getInt("MudTicks"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWriteNBTMud(CompoundTag nbt, CallbackInfo ci){
        nbt.putInt("MudTicks", theConjunctionOfTheSpheres$getMudInFace());
    }

    //Oils
    @Unique
    private float oilDamageAdded = 0;

    @Inject(method = "attack", at = @At("HEAD"))
    private void injectMonsterOil(Entity target, CallbackInfo ci){

        if(target instanceof LivingEntity livingTarget){
            if(THIS.getMainHandItem().has(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT)){
                MonsterOilComponent monsterOil =THIS.getMainHandItem().get(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT);
                switch (Objects.requireNonNull(monsterOil).groupId()){
                    case 0:
                        if(EntitiesUtil_Fabric.isNecrophage(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 1:
                        if(EntitiesUtil_Fabric.isOgroid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 2:
                        if(EntitiesUtil_Fabric.isSpecter(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 3:
                        if(EntitiesUtil_Fabric.isVampire(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 4:
                        if(EntitiesUtil_Fabric.isInsectoid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 5:
                        if(EntitiesUtil_Fabric.isBeast(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 6:
                        if(EntitiesUtil_Fabric.isElementa(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 7:
                        if(EntitiesUtil_Fabric.isCursedOne(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 8:
                        if(EntitiesUtil_Fabric.isHybrid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 9:
                        if(EntitiesUtil_Fabric.isDraconid(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 10:
                        if(EntitiesUtil_Fabric.isRelict(livingTarget)) LevelOilAssigner(monsterOil);
                        break;

                    case 11:
                        if(EntitiesUtil_Fabric.isHumanoid(livingTarget)) LevelOilAssigner(monsterOil);
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
    private void OilUsesManager(Player player, MonsterOilComponent monsterOil){
        ItemStack weapon = player.getMainHandItem();

        MonsterOilComponent newMonsterOil= MonsterOilComponent.decreaseUse(monsterOil);
        weapon.set(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT, newMonsterOil);

        if(newMonsterOil.uses()==0){
            weapon.remove(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT);

            player.level().playSound(player, player.getX(), player.getY(), player.getZ(), TCOTS_Sounds.OIL_RAN_OUT, player.getSoundSource(), 1, 1);
        }
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
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
            TCOTS_Items_Fabric.ALCOHEST,

            TCOTS_Items_Fabric.VILLAGE_HERBAL,
            TCOTS_Items_Fabric.CHERRY_CORDIAL,
            TCOTS_Items_Fabric.MANDRAKE_CORDIAL,

            TCOTS_Items_Fabric.ICY_SPIRIT,
            TCOTS_Items_Fabric.DWARVEN_SPIRIT,
            TCOTS_Items_Fabric.WHITE_GULL
    );
    @Unique
    Player THIS = (Player) (Object) this;

    @Inject(method = "stopSleepInBed", at = @At("TAIL"))
    private void injectPotionRefilling(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci){

        boolean refilled=false;

        //Iterates the list with all the alcohols
        for(WitcherAlcohol_Base alcoholBase: list_alcohol){
            //Get if the player has some alcohol
            if(((inventory.findSlotMatchingItem(alcoholBase.getDefaultInstance()) != -1))
                    && !refilled
                    && potionTimer>90){
                int loopP = EntitiesUtil_Fabric.isWearingManticoreArmor(THIS)? alcoholBase.getRefillQuantity()+2 : alcoholBase.getRefillQuantity();

                int slot = inventory.findSlotMatchingItem(alcoholBase.getDefaultInstance());

                //Makes a loop across all the inventory
                for(int i=0; i<inventory.getContainerSize(); i++){
                    //If found an Empty Potion with NBT
                    if(inventory.getItem(i).getItem() instanceof EmptyWitcherPotionItem && inventory.getItem(i).has(TCOTS_Items_Fabric.REFILL_RECIPE)){
                        String refillItem= inventory.getItem(i).get(TCOTS_Items_Fabric.REFILL_RECIPE);
                        //Checks if the NBT contains the "Potion" string

                        if(refillItem!=null){
                            //Save the potion type
                            Item PotionI = BuiltInRegistries.ITEM.get(ResourceLocation.parse(refillItem));
                            //Saves the count of empty bottles
                            int countI = inventory.getItem(i).getCount();

                            //Erases the slot
                            inventory.getItem(i).shrink(inventory.getItem(i).getCount());
                            //Put the potion in the slot
                            inventory.setItem(i,new ItemStack(PotionI, countI));

                            //Increases in 1
                            loopP=loopP-1;

                            //Put the refilled boolean in true
                            refilled=true;

                            //If it has already filled the slots, it stops
                            if(loopP < 1){
                                //Decrements the alcohol in inventory
                                inventory.getItem(slot).shrink(1);
                                //Triggers the advancement
                                if(THIS instanceof ServerPlayer serverPlayer) TCOTS_Criteria.REFILL_CONCOCTION.trigger(serverPlayer);
                                //Play a sound
                                playSound(TCOTS_Sounds.POTION_REFILLED, 3.0f, 1.0f);
                                //Breaks the loop
                                break;
                            }
                        }
                    }

                    //If it doesn't fulfill all the maximum potions
                    if(i == inventory.getContainerSize() - 1 && refilled){
                        //Decrements the alcohol in inventory
                        inventory.getItem(slot).shrink(1);
                        //Triggers the advancement
                        if(THIS instanceof ServerPlayer serverPlayer) TCOTS_Criteria.REFILL_CONCOCTION.trigger(serverPlayer);
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
    @Inject(method = "eat", at = @At("TAIL"))
    private void injectMariborForestImprove(Level world, ItemStack stack, FoodProperties foodComponent, CallbackInfoReturnable<ItemStack> cir){
        Player THIS = (Player)(Object)this;
        if(this.hasEffect(TCOTS_Effects.MARIBOR_FOREST_EFFECT)){
            int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.MARIBOR_FOREST_EFFECT)).getAmplifier();
            if(stack.has(DataComponents.FOOD)){
                assert foodComponent != null;

                int foodQuantity= (int) (foodComponent.nutrition()*(0.25f+(0.25f*amplifier)));
                float saturationQuantity= foodComponent.saturation()*(0.25f+(0.25f*amplifier));

                if(foodQuantity < 1){
                    foodQuantity=1;
                }

                if(saturationQuantity < 0.1){
                    saturationQuantity=0.1f;
                }

                THIS.getFoodData().eat(foodQuantity, saturationQuantity);
            }
        }
    }


    //Toxicity Logic
    @Unique
    private static final EntityDataAccessor<Integer> TOXICITY = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> DECOCTION_TOXICITY = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> HUD_ACTIVE = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Float> HUD_TRANSPARENCY = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.FLOAT);


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectToxicityDataTracker(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(TOXICITY, 0);
        builder.define(DECOCTION_TOXICITY, 0);
        builder.define(HUD_ACTIVE, false);
        builder.define(HUD_TRANSPARENCY, 0.0f);
    }

    @Override
    public int theConjunctionOfTheSpheres$getNormalToxicity(){
        return this.entityData.get(TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setToxicity(int toxicity){
        this.entityData.set(TOXICITY,toxicity);
    }

    @Override
    public int theConjunctionOfTheSpheres$getMaxToxicity(){
        return (int) THIS.getAttributeValue(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY);
    }

    @Override
    public int theConjunctionOfTheSpheres$getDecoctionToxicity() {
        return this.entityData.get(DECOCTION_TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setDecoctionToxicity(int DecoctionToxicity) {
        this.entityData.set(DECOCTION_TOXICITY,DecoctionToxicity);
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
        return this.entityData.get(DECOCTION_TOXICITY)+this.entityData.get(TOXICITY);
    }

    @Override
    public boolean theConjunctionOfTheSpheres$toxicityOverThreshold() {
        float overdoseThreshold=(this.theConjunctionOfTheSpheres$getMaxToxicity()*0.75f);
        return this.theConjunctionOfTheSpheres$getAllToxicity() > overdoseThreshold;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectReadNBTToxicity(CompoundTag nbt, CallbackInfo ci){
        theConjunctionOfTheSpheres$setToxicity(nbt.getInt("Toxicity"));
        theConjunctionOfTheSpheres$setDecoctionToxicity(nbt.getInt("DecoctionToxicity"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWriteNBTToxicity(CompoundTag nbt, CallbackInfo ci){
        nbt.putInt("Toxicity", theConjunctionOfTheSpheres$getNormalToxicity());
        nbt.putInt("DecoctionToxicity",theConjunctionOfTheSpheres$getDecoctionToxicity());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectInTickDecreaseToxicityIfOverMaximum(CallbackInfo ci){
      if(this.theConjunctionOfTheSpheres$getAllToxicity() > this.theConjunctionOfTheSpheres$getMaxToxicity()){
          THIS.theConjunctionOfTheSpheres$setToxicity(Mth.clamp(
                  THIS.theConjunctionOfTheSpheres$getNormalToxicity(),
                  0,
                  THIS.theConjunctionOfTheSpheres$getMaxToxicity() - THIS.theConjunctionOfTheSpheres$getDecoctionToxicity()));
      }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickDecreaseToxicity(CallbackInfo ci){
        if (this.theConjunctionOfTheSpheres$getNormalToxicity()>0) {
            if(this.tickCount%40==0){
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

            //With 20hp makes 1hp damage
            if(this.tickCount%(damageableTicks)==0){
            this.hurt(TCOTS_DamageTypes.toxicityDamage(level()),THIS.getMaxHealth()*0.05f);
            }
        }
    }


    //SamumEffect
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean injectCriticalWithSamum(boolean value, @Local(argsOnly = true) Entity target){
        if(target instanceof LivingEntity entity){
            if(SamumBomb.checkSamumEffect(entity)){
                MobEffectInstance instance = entity.getEffect(TCOTS_Effects.SAMUM_EFFECT);
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
        if(this.hasEffect(TCOTS_Effects.WOLF_EFFECT)){
            //Wolf I:   -> 2.0f
            //Wolf II:  -> 2.5f
            //Wolf III: -> 3.0f

            int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.WOLF_EFFECT)).getAmplifier();
            return value + (0.5f + (amplifier*0.5f));
        }

        return value;
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float addExtraSwordDamageRook(float value){
        if(this.getMainHandItem().getItem() instanceof SwordItem && this.hasEffect(TCOTS_Effects.ROOK_EFFECT)){
            //Rook I:   -> +2
            //Rook II:  -> +3
            //Rook III: -> +4

            int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.ROOK_EFFECT)).getAmplifier();
            return value + (2 + (amplifier));
        }

        return value;
    }


    //Raven Armor
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float injectRavenBonusToDamage(float value, @Local(argsOnly = true) Entity target){
        return value + (
                EntitiesUtil_Fabric.isWearingRavensArmor(THIS)
                        && target instanceof LivingEntity livingTarget
                        && EntitiesUtil_Fabric.isMonster(livingTarget)? 2.0f : 0.0f);
    }


    //Moonblade
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float injectMoonBladeDamage(float value, @Local(argsOnly = true) Entity target){
        return value*(
                THIS.getMainHandItem().getItem() == TCOTS_Items_Fabric.MOONBLADE
                        && target instanceof LivingEntity livingTarget
                        && EntitiesUtil_Fabric.isMonster(livingTarget)? 1.5f : 1.0f);
    }

    //Winter's Blade
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float injectWintersBladeDamage(float value, @Local(argsOnly = true) Entity target){
        return value + (
                THIS.getMainHandItem().getItem() == TCOTS_Items_Fabric.WINTERS_BLADE
                && target instanceof LivingEntity livingTarget
                && livingTarget.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)?
                        2.0f
                        : 0.0f);
    }

    //Witcher Eyes
    @Unique
    private static final EntityDataAccessor<Boolean> EYES_ACTIVATE = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Vector3f> EYES_POSITION = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.VECTOR3);
    @Unique
    private static final EntityDataAccessor<Integer> EYES_SEPARATION = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> EYES_SHAPE = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);


    @Unique
    private static final EntityDataAccessor<Boolean> TOXICITY_ACTIVATE = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.BOOLEAN);


    @Override
    public boolean theConjunctionOfTheSpheres$getWitcherEyesActivated(){return this.entityData.get(EYES_ACTIVATE);}
    @Override
    public void theConjunctionOfTheSpheres$setWitcherEyesActivated(boolean activate){this.entityData.set(EYES_ACTIVATE, activate);}

    @Override
    public boolean theConjunctionOfTheSpheres$getToxicityActivated() {return this.entityData.get(TOXICITY_ACTIVATE);}

    @Override
    public void theConjunctionOfTheSpheres$setToxicityActivated(boolean activate) {this.entityData.set(TOXICITY_ACTIVATE, activate);}

    @Override
    public Vector3f theConjunctionOfTheSpheres$getEyesPivot(){return this.entityData.get(EYES_POSITION);}
    @Override
    public void theConjunctionOfTheSpheres$setEyesPivot(Vector3f vector3f){this.entityData.set(EYES_POSITION,vector3f);}

    @Override
    public int theConjunctionOfTheSpheres$getEyeSeparation(){return this.entityData.get(EYES_SEPARATION);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeSeparation(int separation){this.entityData.set(EYES_SEPARATION, separation);}

    @Override
    public int theConjunctionOfTheSpheres$getEyeShape(){return this.entityData.get(EYES_SHAPE);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeShape(int shape){this.entityData.set(EYES_SHAPE, shape);}

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectWitcherEyesData(SynchedEntityData.Builder builder, CallbackInfo ci){
        builder.define(EYES_ACTIVATE,   false);
        builder.define(EYES_POSITION,   new Vector3f(0,0,0));
        builder.define(EYES_SEPARATION, 2);
        builder.define(EYES_SHAPE,      0);
        builder.define(TOXICITY_ACTIVATE, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectReadNBTWitcherEyes(CompoundTag nbt, CallbackInfo ci){
        CompoundTag nbtEyes = getSubNbt("WitcherEyes", nbt);
        if(nbtEyes!=null) {
            this.theConjunctionOfTheSpheres$setWitcherEyesActivated(nbtEyes.getBoolean("Activated"));

            this.theConjunctionOfTheSpheres$setEyesPivot(new Vector3f(nbtEyes.getFloat("EyesX"), nbtEyes.getFloat("EyesY"), 0));

            this.theConjunctionOfTheSpheres$setEyeSeparation(nbtEyes.getInt("EyesSeparation"));

            this.theConjunctionOfTheSpheres$setEyeShape(nbtEyes.getInt("EyesShape"));
        }

        CompoundTag nbtToxicity = getSubNbt("ToxicityFace", nbt);

        if(nbtToxicity!=null){
            this.theConjunctionOfTheSpheres$setToxicityActivated(nbtToxicity.getBoolean("Activated"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWriteNBTWitcherEyes(CompoundTag nbt, CallbackInfo ci){
        CompoundTag nbtEyes = new CompoundTag();

        nbtEyes.putBoolean("Activated", this.theConjunctionOfTheSpheres$getWitcherEyesActivated());

        nbtEyes.putFloat("EyesX", this.theConjunctionOfTheSpheres$getEyesPivot().x);
        nbtEyes.putFloat("EyesY", this.theConjunctionOfTheSpheres$getEyesPivot().y);

        nbtEyes.putInt("EyesSeparation", this.theConjunctionOfTheSpheres$getEyeSeparation());
        nbtEyes.putInt("EyesShape", this.theConjunctionOfTheSpheres$getEyeShape());

        nbt.put("WitcherEyes", nbtEyes);

        CompoundTag nbtToxicity = new CompoundTag();

        nbtToxicity.putBoolean("Activated", this.theConjunctionOfTheSpheres$getToxicityActivated());

        nbt.put("ToxicityFace", nbtToxicity);
    }

    @SuppressWarnings("all")
    @Unique
    @Nullable
    private static CompoundTag getSubNbt(String key, CompoundTag compound) {
        if (compound == null || !compound.contains(key, Tag.TAG_COMPOUND)) {
            return null;
        }
        return compound.getCompound(key);
    }


    @Inject(method = "tick", at = @At("HEAD"))
    private void testing(CallbackInfo ci){

    }

}
