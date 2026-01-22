package mors.tcots.mixin;

import mors.tcots.registry.*;
import mors.tcots.interfaces.PlayerEntityMixinInterface;
import mors.tcots.items.armor.set.ArmorSet;
import mors.tcots.items.components.MonsterOilComponent;
import mors.tcots.items.concoctions.EmptyWitcherPotionItem;
import mors.tcots.items.concoctions.WitcherAlcohol_Base;
import mors.tcots.items.concoctions.bombs.SamumBomb;
import mors.tcots.utils.TCOTS_EntitiesUtil;
import mors.tcots.utils.TCOTS_Util;
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
    Player tcots$THIS = (Player) (Object) this;

    @Final
    @Shadow
    Inventory inventory;

    protected PlayerEntityMixin(final EntityType<? extends LivingEntity> entityType, final Level level) {
        super(entityType, level);
    }

    @Inject(method = "createAttributes", at = @At("RETURN"), cancellable = true)
    private static void tcots$addAttributes(final CallbackInfoReturnable<AttributeSupplier.Builder> cir){
        final AttributeSupplier.Builder builder = cir.getReturnValue();

        builder.add(TCOTS_EntityAttributes.WITCHER_MAX_TOXICITY, 100);

        builder.add(TCOTS_EntityAttributes.RESISTANCE_AGAINST_MONSTERS, 1);
        builder.add(TCOTS_EntityAttributes.DAMAGE_AGAINST_MONSTERS, 1);

        builder.add(TCOTS_EntityAttributes.BOMB_COOLDOWN, 1);
        builder.add(TCOTS_EntityAttributes.POTION_DRINK_TIME, 1);
        builder.add(TCOTS_EntityAttributes.EXTRA_ALCOHOL_REFILL, 0);

        cir.setReturnValue(builder);
    }

    //Oils
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float tcots$addOilDamage(final float value, @Local(argsOnly = true) final Entity target){
        //If isn't a living target
        if (!(target instanceof final LivingEntity livingTarget))
            return value;

        //If it doesn't have oil component
        final var stack = tcots$THIS.getMainHandItem();
        if (!stack.has(TCOTS_Items.MonsterOilComponent()))
            return value;

        //If its null
        final MonsterOilComponent oil = stack.get(TCOTS_Items.MonsterOilComponent());
        if (oil == null)
            return value;

        final float added =
                tcots$matchesGroup(oil.groupId(), livingTarget)?
                        tcots$levelOilAssigner(oil) : 0.0F;

        tcots$oilUsesManager(tcots$THIS, oil);
        return value + added;
    }

    @Unique
    private static boolean tcots$matchesGroup(final int groupId, final LivingEntity target) {
        return switch (groupId) {
            case 0 -> TCOTS_EntitiesUtil.isNecrophage(target);
            case 1 -> TCOTS_EntitiesUtil.isOgroid(target);
            case 2 -> TCOTS_EntitiesUtil.isSpecter(target);
            case 3 -> TCOTS_EntitiesUtil.isVampire(target);
            case 4 -> TCOTS_EntitiesUtil.isInsectoid(target);
            case 5 -> TCOTS_EntitiesUtil.isBeast(target);
            case 6 -> TCOTS_EntitiesUtil.isElementa(target);
            case 7 -> TCOTS_EntitiesUtil.isCursedOne(target);
            case 8 -> TCOTS_EntitiesUtil.isHybrid(target);
            case 9 -> TCOTS_EntitiesUtil.isDraconid(target);
            case 10 -> TCOTS_EntitiesUtil.isRelict(target);
            case 11 -> TCOTS_EntitiesUtil.isHumanoid(target);
            default -> false;
        };
    }

    @Unique
    private float tcots$levelOilAssigner(final MonsterOilComponent monsterOil){
        return switch (monsterOil.level()){
            case 1 -> 2f;
            case 2 -> 4f;
            case 3 -> 6f;
            default -> 0;
        };
    }

    @Unique
    private void tcots$oilUsesManager(final Player player, final MonsterOilComponent monsterOil){
        final ItemStack weapon = player.getMainHandItem();

        final MonsterOilComponent newMonsterOil= MonsterOilComponent.decreaseUse(monsterOil);
        weapon.set(TCOTS_Items.MonsterOilComponent(), newMonsterOil);

        if(newMonsterOil.uses()==0){
            weapon.remove(TCOTS_Items.MonsterOilComponent());
            player.level().playSound(player, player.getX(), player.getY(), player.getZ(), TCOTS_Sounds.getSoundEvent("oil_ran_out"), player.getSoundSource(), 1, 1);
        }
    }

    //SamumEffect
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean tcots$criticalWithSamum(final boolean value, @Local(argsOnly = true) final Entity target){
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
    private float tcots$extraCriticalWolf(final float value){
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
    private float tcots$extraSwordDamageRook(final float value){
        if(this.getMainHandItem().getItem() instanceof SwordItem && this.hasEffect(TCOTS_Effects.RookEffect())){
            //Rook I:   -> +2
            //Rook II:  -> +3
            //Rook III: -> +4

            final int amplifier = Objects.requireNonNull(this.getEffect(TCOTS_Effects.RookEffect())).getAmplifier();
            return value + (2 + (amplifier));
        }

        return value;
    }




    @ModifyArg(method = "attack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            ordinal = 0))
    private float tcots$damageAgainstMonstersApply(final float value, @Local(argsOnly = true) final Entity target){
        final var extraDamageAgainstMonsters = this.getAttribute(TCOTS_EntityAttributes.DAMAGE_AGAINST_MONSTERS);
        float extraDamage = 1;
        if (extraDamageAgainstMonsters != null &&
                target instanceof final LivingEntity livingTarget
                && TCOTS_EntitiesUtil.isMonster(livingTarget)){

            //Damage Against Monsters Attribute
            extraDamage = (float) extraDamageAgainstMonsters.getValue();

            //+25% if under 50% health from Raven Armor
            if(ArmorSet.RAVEN.hasFullBonus(this) && livingTarget.getHealth() < livingTarget.getMaxHealth()*0.5f){
                extraDamage = extraDamage+0.25f;
            }

            //Moonblade Bonus
            if(tcots$THIS.getMainHandItem().getItem() == TCOTS_Items.MOONBLADE.get()){
                extraDamage = extraDamage+TCOTS_Util.moonblade_bonus;
            }
        }

        return (value * extraDamage);
    }

    //Winter's Blade
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 1
            , slice = @Slice(
            from = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/core/Holder;)D", ordinal = 0),
            to = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F", ordinal = 0))
    )
    private float tcots$wintersBladeDamage(final float value, @Local(argsOnly = true) final Entity target){
        return value + (
                tcots$THIS.getMainHandItem().getItem() == TCOTS_Items.WINTERS_BLADE.get()
                        && target instanceof final LivingEntity livingTarget
                        && livingTarget.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)?
                        2.0f
                        : 0.0f);
    }

    //Mud Things
    @Unique
    private static final EntityDataAccessor<Integer> MUD_TICKS = SynchedEntityData.defineId(PlayerEntityMixin.class, EntityDataSerializers.INT);

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void tcots$mudTicks(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(MUD_TICKS, 0);
    }

    @Shadow public abstract void playSound(@NotNull SoundEvent sound, float volume, float pitch);

    @Shadow public abstract @NotNull HumanoidArm getMainArm();

    @Override
    public int tcots$$getMudInFace() {
        return this.entityData.get(MUD_TICKS);
    }

    @Override
    public void tcots$setMudInFace(final int ticks) {
        this.entityData.set(MUD_TICKS, ticks);
    }

    @Override
    public float tcots$getMudTransparency() {
        return  (float) tcots$$getMudInFace()/100;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tcots$inTickMud(final CallbackInfo ci){

        if(this.tcots$$getMudInFace() > 0 && this.isInWaterOrRain()){
            tcots$setMudInFace(tcots$$getMudInFace() - 10);
        }
        else if(this.tcots$$getMudInFace() > 0){
            tcots$setMudInFace(tcots$$getMudInFace() - 1);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void tcots$readNBTMud(final CompoundTag nbt, final CallbackInfo ci){
        tcots$setMudInFace(nbt.getInt("MudTicks"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void tcots$writeNBTMud(final CompoundTag nbt, final CallbackInfo ci){
        nbt.putInt("MudTicks", tcots$$getMudInFace());
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
    private void tcots$potionRefilling(final boolean skipSleepTimer, final boolean updateSleepingPlayers, final CallbackInfo ci){

        boolean refilled=false;

        //Iterates the list with all the alcohols
        for(final WitcherAlcohol_Base alcoholBase: list_alcohol){
            //Get if the player has some alcohol
            if(((inventory.findSlotMatchingItem(alcoholBase.getDefaultInstance()) != -1))
                    && !refilled
                    && tcots$potionTimer >90){

                final var extraSlotsAttribute = this.getAttribute(TCOTS_EntityAttributes.EXTRA_ALCOHOL_REFILL);
                int extraSlots = 0;
                if (extraSlotsAttribute != null) {
                    extraSlots = (int) extraSlotsAttribute.getValue();
                }

                int loopP = alcoholBase.getRefillQuantity()+ extraSlots;

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
                                if(tcots$THIS instanceof final ServerPlayer serverPlayer) TCOTS_Criteria.RefillConcoction().trigger(serverPlayer);
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
                        if(tcots$THIS instanceof final ServerPlayer serverPlayer) TCOTS_Criteria.RefillConcoction().trigger(serverPlayer);
                        //Play a sound
                        playSound(TCOTS_Sounds.getSoundEvent("potion_refill"), 3.0f, 1.0f);
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tcots$inTickSleepingPotion(final CallbackInfo ci){
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
    private void tcots$mariborForestImprove(final Level world, final ItemStack stack, final FoodProperties foodComponent, final CallbackInfoReturnable<ItemStack> cir){
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
    private void tcots$toxicityDataTracker(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(tcots$TOXICITY, 0);
        builder.define(tcots$DECOCTION_TOXICITY, 0);
        builder.define(tcots$HUD_ACTIVE, false);
        builder.define(tcots$HUD_TRANSPARENCY, 0.0f);
    }

    @Override
    public int tcots$getNormalToxicity(){
        return this.entityData.get(tcots$TOXICITY);
    }

    @Override
    public void tcots$setToxicity(final int toxicity){
        this.entityData.set(tcots$TOXICITY,toxicity);
    }

    @Override
    public int tcots$setMaxToxicity(){
        return (int) tcots$THIS.getAttributeValue(TCOTS_EntityAttributes.WITCHER_MAX_TOXICITY);
    }

    @Override
    public int tcots$getDecoctionToxicity() {
        return this.entityData.get(tcots$DECOCTION_TOXICITY);
    }

    @Override
    public void tcots$setDecoctionToxicity(final int DecoctionToxicity) {
        this.entityData.set(tcots$DECOCTION_TOXICITY,DecoctionToxicity);
    }

    @Override
    public void tcots$addToxicity(final int toxicity, final boolean decoction) {
        if(decoction){
            tcots$setDecoctionToxicity(tcots$getDecoctionToxicity()+toxicity);
        }
        else {
            tcots$setToxicity(tcots$getNormalToxicity()+toxicity);
        }
    }

    @Override
    public void tcots$decreaseToxicity(final int toxicity, final boolean decoction) {
        if(decoction)
            tcots$setDecoctionToxicity(tcots$getDecoctionToxicity()-toxicity);
        else
            tcots$setToxicity(tcots$getNormalToxicity()-toxicity);
    }

    @Override
    public int tcots$getAllToxicity() {
        return this.entityData.get(tcots$DECOCTION_TOXICITY)+this.entityData.get(tcots$TOXICITY);
    }

    @Override
    public boolean tcots$toxicityOverThreshold() {
        final float overdoseThreshold=(this.tcots$setMaxToxicity()*0.75f);
        return this.tcots$getAllToxicity() > overdoseThreshold;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void tcots$readNBTToxicity(final CompoundTag nbt, final CallbackInfo ci){
        tcots$setToxicity(nbt.getInt("Toxicity"));
        tcots$setDecoctionToxicity(nbt.getInt("DecoctionToxicity"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void tcots$writeNBTToxicity(final CompoundTag nbt, final CallbackInfo ci){
        nbt.putInt("Toxicity", tcots$getNormalToxicity());
        nbt.putInt("DecoctionToxicity", tcots$getDecoctionToxicity());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tcots$inTickDecreaseToxicityIfOverMaximum(final CallbackInfo ci){
        if(this.tcots$getAllToxicity() > this.tcots$setMaxToxicity()){
            tcots$THIS.tcots$setToxicity(Mth.clamp(
                    tcots$THIS.tcots$getNormalToxicity(),
                    0,
                    tcots$THIS.tcots$setMaxToxicity() - tcots$THIS.tcots$getDecoctionToxicity()));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tcots$inTickDecreaseToxicity(final CallbackInfo ci){
        if (this.tcots$getNormalToxicity()>0) {
            if(this.tickCount%40==0){
                this.tcots$decreaseToxicity(1,false);
            }
        }


        final float dangerOverdoseThreshold=(this.tcots$setMaxToxicity()*0.9f);
        if(this.tcots$toxicityOverThreshold()){
            //At 75%,  every 40 ticks
            //At 80%,  every 37.5 ticks
            //At 85%,  every 35.29 ticks
            int damageableTicks = (int) (30 * ((float) tcots$setMaxToxicity() / (float) tcots$getAllToxicity()));

            //At 90%,  every 11.11 ticks
            //At 100%, every 10 ticks
            if(tcots$getAllToxicity() > dangerOverdoseThreshold) {
                damageableTicks = (int) (10 * ((float) tcots$setMaxToxicity() / (float) tcots$getAllToxicity()));
            }

            //With 20hp makes 1hp damage
            if(this.tickCount%(damageableTicks)==0){
                this.hurt(TCOTS_DamageTypes.toxicityDamage(level()), tcots$THIS.getMaxHealth()*0.05f);
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
    public boolean tcots$getWitcherEyesActivated(){return this.entityData.get(EYES_ACTIVATE);}
    @Override
    public void tcots$setWitcherEyesActivated(final boolean activate){this.entityData.set(EYES_ACTIVATE, activate);}

    @Override
    public boolean tcots$getToxicityActivated() {return this.entityData.get(TOXICITY_ACTIVATE);}

    @Override
    public void tcots$setToxicityActivated(final boolean activate) {this.entityData.set(TOXICITY_ACTIVATE, activate);}

    @Override
    public Vector3f tcots$getEyesPivot(){return this.entityData.get(EYES_POSITION);}
    @Override
    public void tcots$setEyesPivot(final Vector3f vector3f){this.entityData.set(EYES_POSITION,vector3f);}

    @Override
    public int tcots$getEyeSeparation(){return this.entityData.get(EYES_SEPARATION);}
    @Override
    public void tcots$setEyeSeparation(final int separation){this.entityData.set(EYES_SEPARATION, separation);}

    @Override
    public int tcots$getEyeShape(){return this.entityData.get(EYES_SHAPE);}
    @Override
    public void tcots$setEyeShape(final int shape){this.entityData.set(EYES_SHAPE, shape);}

    @Override
    public boolean tcots$getEyeMoves(){return this.entityData.get(EYES_MOVES);}
    @Override
    public void tcots$setEyeMoves(final boolean moves){this.entityData.set(EYES_MOVES, moves);}

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void tcots$witcherEyesData(final SynchedEntityData.Builder builder, final CallbackInfo ci){
        builder.define(EYES_ACTIVATE,   false);
        builder.define(EYES_POSITION,   new Vector3f(0,0,0));
        builder.define(EYES_SEPARATION, 2);
        builder.define(EYES_SHAPE,      0);
        builder.define(EYES_MOVES,   true);
        builder.define(TOXICITY_ACTIVATE, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void tcots$readNBTWitcherEyes(final CompoundTag nbt, final CallbackInfo ci){
        final CompoundTag nbtEyes = tcots$getSubNbt("WitcherEyes", nbt);
        if(nbtEyes!=null) {
            this.tcots$setWitcherEyesActivated(nbtEyes.getBoolean("Activated"));

            this.tcots$setEyesPivot(new Vector3f(nbtEyes.getFloat("EyesX"), nbtEyes.getFloat("EyesY"), 0));

            this.tcots$setEyeSeparation(nbtEyes.getInt("EyesSeparation"));

            this.tcots$setEyeShape(nbtEyes.getInt("EyesShape"));

            this.tcots$setEyeMoves(nbtEyes.getBoolean("Moves"));
        }

        final CompoundTag nbtToxicity = tcots$getSubNbt("ToxicityFace", nbt);

        if(nbtToxicity!=null){
            this.tcots$setToxicityActivated(nbtToxicity.getBoolean("Activated"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void tcots$writeNBTWitcherEyes(final CompoundTag nbt, final CallbackInfo ci){
        final CompoundTag nbtEyes = new CompoundTag();

        nbtEyes.putBoolean("Activated", this.tcots$getWitcherEyesActivated());

        nbtEyes.putFloat("EyesX", this.tcots$getEyesPivot().x);
        nbtEyes.putFloat("EyesY", this.tcots$getEyesPivot().y);

        nbtEyes.putInt("EyesSeparation", this.tcots$getEyeSeparation());
        nbtEyes.putInt("EyesShape", this.tcots$getEyeShape());

        nbtEyes.putBoolean("Moves", this.tcots$getEyeMoves());

        nbt.put("WitcherEyes", nbtEyes);

        final CompoundTag nbtToxicity = new CompoundTag();

        nbtToxicity.putBoolean("Activated", this.tcots$getToxicityActivated());

        nbt.put("ToxicityFace", nbtToxicity);
    }

    @SuppressWarnings("all")
    @Unique
    @Nullable
    private static CompoundTag tcots$getSubNbt(String key, CompoundTag compound) {
        if (compound == null || !compound.contains(key, Tag.TAG_COMPOUND)) {
            return null;
        }
        return compound.getCompound(key);
    }
}
