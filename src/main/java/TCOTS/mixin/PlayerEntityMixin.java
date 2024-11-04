package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.interfaces.PlayerEntityMixinInterface;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.concoctions.EmptyWitcherPotionItem;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.bombs.SamumBomb;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.world.TCOTS_DamageTypes;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FoodComponent;
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
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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

    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

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
        if(target instanceof LivingEntity livingTarget){
            if(thisObject.getMainHandStack().hasNbt()){
                NbtCompound nbt=thisObject.getMainHandStack().getNbt();

                assert nbt != null;
                if(nbt.contains("Monster Oil")){
                    NbtCompound monsterOil = thisObject.getMainHandStack().getSubNbt("Monster Oil");
                    assert monsterOil != null;
                    if(monsterOil.contains("Id") && monsterOil.contains("Level") && monsterOil.contains("Uses")){
                        switch (monsterOil.getInt("Id")){
                            case 0:
                                if(EntitiesUtil.isNecrophage(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 1:
                                if(EntitiesUtil.isOgroid(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 2:
                                if(EntitiesUtil.isSpecter(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 3:
                                if(EntitiesUtil.isVampire(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 4:
                                if(EntitiesUtil.isInsectoid(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 5:
                                if(EntitiesUtil.isBeast(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 6:
                                if(EntitiesUtil.isElementa(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 7:
                                if(EntitiesUtil.isCursedOne(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 8:
                                if(EntitiesUtil.isHybrid(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 9:
                                if(EntitiesUtil.isDraconid(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 10:
                                if(EntitiesUtil.isRelict(livingTarget)){
                                    LevelOilAssigner(monsterOil);
                                }
                                break;

                            case 11:
                                if(EntitiesUtil.isHumanoid(livingTarget)){
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
    public int theConjunctionOfTheSpheres$getNormalToxicity(){
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
    public void theConjunctionOfTheSpheres$decreaseMaxToxicity(int MaxToxicity){
        this.dataTracker.set(MAX_TOXICITY,theConjunctionOfTheSpheres$getMaxToxicity()-MaxToxicity);
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

        theConjunctionOfTheSpheres$setMaxToxicity(nbt.getInt("MaxToxicity"));
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void injectWriteNBTToxicity(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("Toxicity", theConjunctionOfTheSpheres$getNormalToxicity());
        nbt.putInt("DecoctionToxicity",theConjunctionOfTheSpheres$getDecoctionToxicity());

        nbt.putInt("MaxToxicity",theConjunctionOfTheSpheres$getMaxToxicity());
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


    //Raven Armor
    @Unique
    private float ravensArmorExtraDamage = 0;

    @Inject(method = "attack", at = @At("HEAD"))
    private void getIsMonsterForRavenBonus(Entity target, CallbackInfo ci){
        ravensArmorExtraDamage = EntitiesUtil.isWearingRavensArmor(THIS) && target instanceof LivingEntity livingTarget && EntitiesUtil.isMonster(livingTarget)? 2.0f : 0.0f;
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectRavenBonusToDamage(float value){
        return value + ravensArmorExtraDamage;
    }


    //Moonblade
    @Unique
    private float moonbladeExtraDamage = 0.0f;

    @Inject(method = "attack", at = @At("HEAD"))
    private void getIsMonsterMoonblade(Entity target, CallbackInfo ci){
        moonbladeExtraDamage = THIS.getMainHandStack().getItem() == TCOTS_Items.MOONBLADE && target instanceof LivingEntity livingTarget && EntitiesUtil.isMonster(livingTarget)? 1.5f : 1.0f;
    }
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 0
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;handleAttack(Lnet/minecraft/entity/Entity;)Z", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F", ordinal = 0))
    )
    private float injectMoonBladeDamage(float value){
        return value*moonbladeExtraDamage;
    }

    //Moonblade
    @Unique
    private float wintersBladeExtraDamage = 0.0f;

    @Inject(method = "attack", at = @At("HEAD"))
    private void getIsFireMonster(Entity target, CallbackInfo ci){
        wintersBladeExtraDamage = THIS.getMainHandStack().getItem() == TCOTS_Items.WINTERS_BLADE && target instanceof LivingEntity livingTarget && livingTarget.getType().isIn(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)? 2.0f : 0.0f;
    }
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    )
    private float injectWintersBladeDamage(float value){
        return value+wintersBladeExtraDamage;
    }


    //Giant Anchor
    @Inject(method = "tick", at = @At("HEAD"))
    private void injectRetrieveAnchor(CallbackInfo ci){
        if(!this.getWorld().isClient && this.getAttackCooldownProgress(0.0f) < 1.0){

            ItemStack stack = this.getStackInHand(Hand.MAIN_HAND);
            if (stack.isOf(TCOTS_Items.GIANT_ANCHOR) && GiantAnchorItem.wasLaunched(stack) ) {
                GiantAnchorItem.retrieveAnchor(this);
            }
        }
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


    @Override
    public boolean theConjunctionOfTheSpheres$getWitcherEyesActivated(){return this.dataTracker.get(EYES_ACTIVATE);}
    @Override
    public void theConjunctionOfTheSpheres$setWitcherEyesActivated(boolean activate){this.dataTracker.set(EYES_ACTIVATE, activate);}

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
    private void injectWitcherEyesData(CallbackInfo ci){
        this.dataTracker.startTracking(EYES_ACTIVATE,   false);
        this.dataTracker.startTracking(EYES_POSITION,   new Vector3f(0,0,0));
        this.dataTracker.startTracking(EYES_SEPARATION, 2);
        this.dataTracker.startTracking(EYES_SHAPE,      0);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void injectReadNBTWitcherEyes(NbtCompound nbt, CallbackInfo ci){
        NbtCompound nbtEyes = getSubNbt("WitcherEyes", nbt);
        if(nbtEyes!=null) {
            this.theConjunctionOfTheSpheres$setWitcherEyesActivated(nbtEyes.getBoolean("Activated"));

            //TODO: Remember, EyeY goes inverted: positive values goes up, negative values goes down
            this.theConjunctionOfTheSpheres$setEyesPivot(new Vector3f(nbtEyes.getFloat("EyesX"), nbtEyes.getFloat("EyesY"), 0));

            this.theConjunctionOfTheSpheres$setEyeSeparation(nbtEyes.getInt("EyesSeparation"));

            this.theConjunctionOfTheSpheres$setEyeShape(nbtEyes.getInt("EyesShape"));
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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initEyesValues(World world, BlockPos pos, float yaw, GameProfile gameProfile, CallbackInfo ci){

    }


    @SuppressWarnings("all")
    @Inject(method = "tick", at = @At("TAIL"))
    private void injectChangesInEyes(CallbackInfo ci){
        TCOTS_Main.CONFIG.witcher_eyes.subscribeToActivate(this::theConjunctionOfTheSpheres$setWitcherEyesActivated);

        TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeSeparation(eye_separation -> {
            this.theConjunctionOfTheSpheres$setEyeSeparation(eye_separation.ordinal());
        });

        TCOTS_Main.CONFIG.witcher_eyes.subscribeToEyeShape(eye_shape -> {
            this.theConjunctionOfTheSpheres$setEyeShape(eye_shape.ordinal());
        });

        TCOTS_Main.CONFIG.witcher_eyes.subscribeToXEyePos(xEyePos -> {
            this.theConjunctionOfTheSpheres$setEyesPivot(new Vector3f(xEyePos, -TCOTS_Main.CONFIG.witcher_eyes.YEyePos(), 0));
        });

        TCOTS_Main.CONFIG.witcher_eyes.subscribeToYEyePos(yEyePos -> {
            this.theConjunctionOfTheSpheres$setEyesPivot(new Vector3f(TCOTS_Main.CONFIG.witcher_eyes.XEyePos(), -yEyePos, 0));
        });
    }

}
