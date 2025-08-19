package TCOTS.mixin;

import TCOTS.entity.TCOTS_EntityAttributes;
import TCOTS.interfaces.PlayerEntityMixinInterface;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.items.concoctions.EmptyWitcherPotionItem;
import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.bombs.SamumBomb;
import TCOTS.registry.*;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.MiscUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityMixinInterface {
    @SuppressWarnings("all")
    @Unique
    Player THIS = (Player) (Object) this;

    @Final
    @Shadow
    Inventory inventory;

    protected PlayerEntityMixin(final EntityType<? extends LivingEntity> entityType, final Level level) {
        super(entityType, level);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void injectToxicity(final CallbackInfoReturnable<AttributeSupplier.Builder> cir){
        cir.setReturnValue(cir.getReturnValue().add(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY));
    }

    //Oils
    @Unique
    private float tcots$oilDamageAdded = 0;

    @Inject(method = "attack", at = @At("HEAD"))
    private void injectMonsterOil(final Entity target, final CallbackInfo ci){

        if(target instanceof final LivingEntity livingTarget){
            if(THIS.getMainHandItem().has(TCOTS_Items.MonsterOilComponent())){
                final MonsterOilComponent monsterOil =THIS.getMainHandItem().get(TCOTS_Items.MonsterOilComponent());
                switch (Objects.requireNonNull(monsterOil).groupId()){
                    case 0:
                        if(EntitiesUtil.isNecrophage(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 1:
                        if(EntitiesUtil.isOgroid(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 2:
                        if(EntitiesUtil.isSpecter(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 3:
                        if(EntitiesUtil.isVampire(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 4:
                        if(EntitiesUtil.isInsectoid(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 5:
                        if(EntitiesUtil.isBeast(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 6:
                        if(EntitiesUtil.isElementa(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 7:
                        if(EntitiesUtil.isCursedOne(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 8:
                        if(EntitiesUtil.isHybrid(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 9:
                        if(EntitiesUtil.isDraconid(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 10:
                        if(EntitiesUtil.isRelict(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    case 11:
                        if(EntitiesUtil.isHumanoid(livingTarget)) tcots$LevelOilAssigner(monsterOil);
                        break;

                    default:
                        break;
                }

                tcots$OilUsesManager(THIS, monsterOil);
            }
        }
    }

    @Unique
    private void tcots$LevelOilAssigner(final MonsterOilComponent monsterOil){
        switch (monsterOil.level()){
            case 1:
                tcots$oilDamageAdded = 2f;
                break;
            case 2:
                tcots$oilDamageAdded = 4f;
                break;
            case 3:
                tcots$oilDamageAdded = 6f;
                break;
            default:
                break;
        }
    }

    @Unique
    private void tcots$OilUsesManager(final Player player, final MonsterOilComponent monsterOil){
        final ItemStack weapon = player.getMainHandItem();

        final MonsterOilComponent newMonsterOil= MonsterOilComponent.decreaseUse(monsterOil);
        weapon.set(TCOTS_Items.MonsterOilComponent(), newMonsterOil);

        if(newMonsterOil.uses()==0){
            weapon.remove(TCOTS_Items.MonsterOilComponent());

            player.level().playSound(player, player.getX(), player.getY(), player.getZ(), TCOTS_Sounds.getSoundEvent("oil_ran_out"), player.getSoundSource(), 1, 1);
        }
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float injectMonsterOilAttack(final float value, @Local(argsOnly = true) final Entity target){
        return value + tcots$oilDamageAdded;
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void ResetMultiplier(final Entity target, final CallbackInfo ci){
        tcots$oilDamageAdded = 0;
    }

    //SamumEffect
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean injectCriticalWithSamum(final boolean value, @Local(argsOnly = true) final Entity target){
        if(target instanceof final LivingEntity entity){
            if(SamumBomb.checkSamumEffect(entity)){
                final MobEffectInstance instance = entity.getEffect(TCOTS_Effects.SamumEffect());
                assert instance != null;
                final int amplifier = instance.getAmplifier();

                return value || amplifier > 1;
            }
        }

        return value;
    }

    //Wolf Effect
    @ModifyExpressionValue(method = "attack", at = @At(value = "CONSTANT", args = "floatValue=1.5"))
    private float injectExtraCriticalWolf(final float value){
        if(this.hasEffect(TCOTS_Effects.WolfEffect())){
            //Wolf I:   -> 2.0f
            //Wolf II:  -> 2.5f
            //Wolf III: -> 3.0f

            final int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.WolfEffect())).getAmplifier();
            return value + (0.5f + (amplifier*0.5f));
        }

        return value;
    }

    //Rook Effect
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float addExtraSwordDamageRook(final float value){
        if(this.getMainHandItem().getItem() instanceof SwordItem && this.hasEffect(TCOTS_Effects.RookEffect())){
            //Rook I:   -> +2
            //Rook II:  -> +3
            //Rook III: -> +4

            final int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.RookEffect())).getAmplifier();
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
    private float injectRavenBonusToDamage(final float value, @Local(argsOnly = true) final Entity target){
        return value + (
                EntitiesUtil.isWearingRavensArmor(THIS)
                        && target instanceof final LivingEntity livingTarget
                        && EntitiesUtil.isMonster(livingTarget)? 2.0f : 0.0f);
    }

    //Moonblade
    @ModifyArg(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            ordinal = 0))
    private float injectMoonBladeDamage(final float value, @Local(argsOnly = true) final Entity target){
        //+25% extra in the normal attack
        return value*(
                THIS.getMainHandItem().getItem() == TCOTS_Items.MOONBLADE.get()
                        && target instanceof final LivingEntity livingTarget
                        && EntitiesUtil.isMonster(livingTarget)? (1+MiscUtil.moonblade_bonus)
                        : 1.0f);
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            ordinal = 0))
    private float injectMoonBladeDamageSweep(final float value, @Local(argsOnly = true) final Entity target){
        //+10% extra in the sweep attack
        return value*(
                THIS.getMainHandItem().getItem() == TCOTS_Items.MOONBLADE.get()
                        && target instanceof final LivingEntity livingTarget
                        && EntitiesUtil.isMonster(livingTarget)? 1.10f : 1.0f);
    }

    //Winter's Blade
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float injectWintersBladeDamage(final float value, @Local(argsOnly = true) final Entity target){
        return value + (
                THIS.getMainHandItem().getItem() == TCOTS_Items.WINTERS_BLADE.get()
                        && target instanceof final LivingEntity livingTarget
                        && livingTarget.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)?
                        2.0f
                        : 0.0f);
    }

    //Mud Things
    @Unique
    private static final EntityDataAccessor<Integer> MUD_TICKS = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectMudTicks(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(MUD_TICKS, 0);
    }

    @Shadow public abstract void playSound(@NotNull SoundEvent sound, float volume, float pitch);

    @Shadow public abstract @NotNull HumanoidArm getMainArm();

    @Override
    public int theConjunctionOfTheSpheres$getMudInFace() {
        return this.entityData.get(MUD_TICKS);
    }

    @Override
    public void theConjunctionOfTheSpheres$setMudInFace(final int ticks) {
        this.entityData.set(MUD_TICKS, ticks);
    }

    @Override
    public float theConjunctionOfTheSpheres$getMudTransparency() {
        return  (float) theConjunctionOfTheSpheres$getMudInFace()/100;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickMud(final CallbackInfo ci){

        if(this.theConjunctionOfTheSpheres$getMudInFace() > 0 && this.isInWaterOrRain()){
            theConjunctionOfTheSpheres$setMudInFace(theConjunctionOfTheSpheres$getMudInFace() - 10);
        }
        else if(this.theConjunctionOfTheSpheres$getMudInFace() > 0){
            theConjunctionOfTheSpheres$setMudInFace(theConjunctionOfTheSpheres$getMudInFace() - 1);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectReadNBTMud(final CompoundTag nbt, final CallbackInfo ci){
        theConjunctionOfTheSpheres$setMudInFace(nbt.getInt("MudTicks"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWriteNBTMud(final CompoundTag nbt, final CallbackInfo ci){
        nbt.putInt("MudTicks", theConjunctionOfTheSpheres$getMudInFace());
    }


    //Refilling Alcohol
    @Unique
    private int tcots$potionTimer;

    @Unique
    List<WitcherAlcohol_Base> list_alcohol = Arrays.asList(
            TCOTS_Items.ALCOHEST.get(),

            TCOTS_Items.VILLAGE_HERBAL.get(),
            TCOTS_Items.CHERRY_CORDIAL.get(),
            TCOTS_Items.MANDRAKE_CORDIAL.get(),

            TCOTS_Items.ICY_SPIRIT.get(),
            TCOTS_Items.DWARVEN_SPIRIT.get(),
            TCOTS_Items.WHITE_GULL.get()
    );

    @Inject(method = "stopSleepInBed", at = @At("TAIL"))
    private void injectPotionRefilling(final boolean skipSleepTimer, final boolean updateSleepingPlayers, final CallbackInfo ci){

        boolean refilled=false;

        //Iterates the list with all the alcohols
        for(final WitcherAlcohol_Base alcoholBase: list_alcohol){
            //Get if the player has some alcohol
            if(((inventory.findSlotMatchingItem(alcoholBase.getDefaultInstance()) != -1))
                    && !refilled
                    && tcots$potionTimer >90){
                int loopP = EntitiesUtil.isWearingManticoreArmor(THIS)? alcoholBase.getRefillQuantity()+2 : alcoholBase.getRefillQuantity();

                final int slot = inventory.findSlotMatchingItem(alcoholBase.getDefaultInstance());

                //Makes a loop across all the inventory
                for(int i=0; i<inventory.getContainerSize(); i++){
                    //If found an Empty Potion with NBT
                    if(inventory.getItem(i).getItem() instanceof EmptyWitcherPotionItem && inventory.getItem(i).has(TCOTS_Items.RefillRecipe())){
                        final String refillItem= inventory.getItem(i).get(TCOTS_Items.RefillRecipe());
                        //Checks if the NBT contains the "Potion" string

                        if(refillItem!=null){
                            //Save the potion type
                            final Item PotionI = BuiltInRegistries.ITEM.get(ResourceLocation.parse(refillItem));
                            //Saves the count of empty bottles
                            final int countI = inventory.getItem(i).getCount();

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
                                if(THIS instanceof final ServerPlayer serverPlayer) TCOTS_Criteria.RefillConcoction().trigger(serverPlayer);
                                //Play a sound
                                playSound(TCOTS_Sounds.getSoundEvent("potion_refill"), 3.0f, 1.0f);
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
                        if(THIS instanceof final ServerPlayer serverPlayer) TCOTS_Criteria.RefillConcoction().trigger(serverPlayer);
                        //Play a sound
                        playSound(TCOTS_Sounds.getSoundEvent("potion_refill"), 3.0f, 1.0f);
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickSleepingPotion(final CallbackInfo ci){
        if (this.isSleeping()) {
            if(this.tcots$potionTimer < 100) {
                ++this.tcots$potionTimer;
            }
        } else {
            if(tcots$potionTimer != 0){
                tcots$potionTimer =0;
            }
        }
    }

    //Maribor Forest
    @Inject(method = "eat", at = @At("TAIL"))
    private void injectMariborForestImprove(final Level world, final ItemStack stack, final FoodProperties foodComponent, final CallbackInfoReturnable<ItemStack> cir){
        final Player THIS = (Player)(Object)this;
        if(this.hasEffect(TCOTS_Effects.MariborForestEffect())){
            final int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.MariborForestEffect())).getAmplifier();
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
    private static final EntityDataAccessor<Integer> tcots$TOXICITY = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Integer> tcots$DECOCTION_TOXICITY = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);
    @Unique
    private static final EntityDataAccessor<Boolean> tcots$HUD_ACTIVE = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Float> tcots$HUD_TRANSPARENCY = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.FLOAT);


    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectToxicityDataTracker(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(tcots$TOXICITY, 0);
        builder.define(tcots$DECOCTION_TOXICITY, 0);
        builder.define(tcots$HUD_ACTIVE, false);
        builder.define(tcots$HUD_TRANSPARENCY, 0.0f);
    }

    @Override
    public int theConjunctionOfTheSpheres$getNormalToxicity(){
        return this.entityData.get(tcots$TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setToxicity(final int toxicity){
        this.entityData.set(tcots$TOXICITY,toxicity);
    }

    @Override
    public int theConjunctionOfTheSpheres$getMaxToxicity(){
        return (int) THIS.getAttributeValue(TCOTS_EntityAttributes.GENERIC_WITCHER_MAX_TOXICITY);
    }

    @Override
    public int theConjunctionOfTheSpheres$getDecoctionToxicity() {
        return this.entityData.get(tcots$DECOCTION_TOXICITY);
    }

    @Override
    public void theConjunctionOfTheSpheres$setDecoctionToxicity(final int DecoctionToxicity) {
        this.entityData.set(tcots$DECOCTION_TOXICITY,DecoctionToxicity);
    }

    @Override
    public void theConjunctionOfTheSpheres$addToxicity(final int toxicity, final boolean decoction) {
        if(decoction){
            theConjunctionOfTheSpheres$setDecoctionToxicity(theConjunctionOfTheSpheres$getDecoctionToxicity()+toxicity);
        }
        else {
            theConjunctionOfTheSpheres$setToxicity(theConjunctionOfTheSpheres$getNormalToxicity()+toxicity);
        }
    }

    @Override
    public void theConjunctionOfTheSpheres$decreaseToxicity(final int toxicity, final boolean decoction) {
        if(decoction){
            theConjunctionOfTheSpheres$setDecoctionToxicity(theConjunctionOfTheSpheres$getDecoctionToxicity()-toxicity);
        }
        else {
            theConjunctionOfTheSpheres$setToxicity(theConjunctionOfTheSpheres$getNormalToxicity()-toxicity);
        }
    }

    @Override
    public int theConjunctionOfTheSpheres$getAllToxicity() {
        return this.entityData.get(tcots$DECOCTION_TOXICITY)+this.entityData.get(tcots$TOXICITY);
    }

    @Override
    public boolean theConjunctionOfTheSpheres$toxicityOverThreshold() {
        final float overdoseThreshold=(this.theConjunctionOfTheSpheres$getMaxToxicity()*0.75f);
        return this.theConjunctionOfTheSpheres$getAllToxicity() > overdoseThreshold;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectReadNBTToxicity(final CompoundTag nbt, final CallbackInfo ci){
        theConjunctionOfTheSpheres$setToxicity(nbt.getInt("Toxicity"));
        theConjunctionOfTheSpheres$setDecoctionToxicity(nbt.getInt("DecoctionToxicity"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWriteNBTToxicity(final CompoundTag nbt, final CallbackInfo ci){
        nbt.putInt("Toxicity", theConjunctionOfTheSpheres$getNormalToxicity());
        nbt.putInt("DecoctionToxicity",theConjunctionOfTheSpheres$getDecoctionToxicity());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectInTickDecreaseToxicityIfOverMaximum(final CallbackInfo ci){
        if(this.theConjunctionOfTheSpheres$getAllToxicity() > this.theConjunctionOfTheSpheres$getMaxToxicity()){
            THIS.theConjunctionOfTheSpheres$setToxicity(Mth.clamp(
                    THIS.theConjunctionOfTheSpheres$getNormalToxicity(),
                    0,
                    THIS.theConjunctionOfTheSpheres$getMaxToxicity() - THIS.theConjunctionOfTheSpheres$getDecoctionToxicity()));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectInTickDecreaseToxicity(final CallbackInfo ci){
        if (this.theConjunctionOfTheSpheres$getNormalToxicity()>0) {
            if(this.tickCount%40==0){
                this.theConjunctionOfTheSpheres$decreaseToxicity(1,false);
            }
        }


        final float dangerOverdoseThreshold=(this.theConjunctionOfTheSpheres$getMaxToxicity()*0.9f);
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
    private static final EntityDataAccessor<Boolean> EYES_MOVES = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.BOOLEAN);


    @Unique
    private static final EntityDataAccessor<Boolean> TOXICITY_ACTIVATE = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.BOOLEAN);


    @Override
    public boolean theConjunctionOfTheSpheres$getWitcherEyesActivated(){return this.entityData.get(EYES_ACTIVATE);}
    @Override
    public void theConjunctionOfTheSpheres$setWitcherEyesActivated(final boolean activate){this.entityData.set(EYES_ACTIVATE, activate);}

    @Override
    public boolean theConjunctionOfTheSpheres$getToxicityActivated() {return this.entityData.get(TOXICITY_ACTIVATE);}

    @Override
    public void theConjunctionOfTheSpheres$setToxicityActivated(final boolean activate) {this.entityData.set(TOXICITY_ACTIVATE, activate);}

    @Override
    public Vector3f theConjunctionOfTheSpheres$getEyesPivot(){return this.entityData.get(EYES_POSITION);}
    @Override
    public void theConjunctionOfTheSpheres$setEyesPivot(final Vector3f vector3f){this.entityData.set(EYES_POSITION,vector3f);}

    @Override
    public int theConjunctionOfTheSpheres$getEyeSeparation(){return this.entityData.get(EYES_SEPARATION);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeSeparation(final int separation){this.entityData.set(EYES_SEPARATION, separation);}

    @Override
    public int theConjunctionOfTheSpheres$getEyeShape(){return this.entityData.get(EYES_SHAPE);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeShape(final int shape){this.entityData.set(EYES_SHAPE, shape);}

    @Override
    public boolean theConjunctionOfTheSpheres$getEyeMoves(){return this.entityData.get(EYES_MOVES);}
    @Override
    public void theConjunctionOfTheSpheres$setEyeMoves(final boolean moves){this.entityData.set(EYES_MOVES, moves);}

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void injectWitcherEyesData(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(EYES_ACTIVATE,   false);
        builder.define(EYES_POSITION,   new Vector3f(0,0,0));
        builder.define(EYES_SEPARATION, 2);
        builder.define(EYES_SHAPE,      0);
        builder.define(EYES_MOVES,   true);
        builder.define(TOXICITY_ACTIVATE, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void injectReadNBTWitcherEyes(final CompoundTag nbt, final CallbackInfo ci){
        final CompoundTag nbtEyes = getSubNbt("WitcherEyes", nbt);
        if(nbtEyes!=null) {
            this.theConjunctionOfTheSpheres$setWitcherEyesActivated(nbtEyes.getBoolean("Activated"));

            this.theConjunctionOfTheSpheres$setEyesPivot(new Vector3f(nbtEyes.getFloat("EyesX"), nbtEyes.getFloat("EyesY"), 0));

            this.theConjunctionOfTheSpheres$setEyeSeparation(nbtEyes.getInt("EyesSeparation"));

            this.theConjunctionOfTheSpheres$setEyeShape(nbtEyes.getInt("EyesShape"));

            this.theConjunctionOfTheSpheres$setEyeMoves(nbtEyes.getBoolean("Moves"));
        }

        final CompoundTag nbtToxicity = getSubNbt("ToxicityFace", nbt);

        if(nbtToxicity!=null){
            this.theConjunctionOfTheSpheres$setToxicityActivated(nbtToxicity.getBoolean("Activated"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void injectWriteNBTWitcherEyes(final CompoundTag nbt, final CallbackInfo ci){
        final CompoundTag nbtEyes = new CompoundTag();

        nbtEyes.putBoolean("Activated", this.theConjunctionOfTheSpheres$getWitcherEyesActivated());

        nbtEyes.putFloat("EyesX", this.theConjunctionOfTheSpheres$getEyesPivot().x);
        nbtEyes.putFloat("EyesY", this.theConjunctionOfTheSpheres$getEyesPivot().y);

        nbtEyes.putInt("EyesSeparation", this.theConjunctionOfTheSpheres$getEyeSeparation());
        nbtEyes.putInt("EyesShape", this.theConjunctionOfTheSpheres$getEyeShape());

        nbtEyes.putBoolean("Moves", this.theConjunctionOfTheSpheres$getEyeMoves());

        nbt.put("WitcherEyes", nbtEyes);

        final CompoundTag nbtToxicity = new CompoundTag();

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
}
