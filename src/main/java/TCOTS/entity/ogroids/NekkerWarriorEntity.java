package TCOTS.entity.ogroids;

import TCOTS.entity.goals.*;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

public class NekkerWarriorEntity extends NekkerEntity implements GeoEntity {
    //xTODO: Add mutagen
    //xTODO: Add custom sounds
    //TODO: Add bestiary entry
    //TODO: Add decoction
    //TODO: Add loot_table
    //TODO: Add spawn
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public NekkerWarriorEntity(EntityType<? extends NekkerEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.4f)
                .add(EntityAttributes.GENERIC_ARMOR, 2f);
    }

    @Override
    protected void initGoals() {

        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new LungeAttackGoal(this, 150, 1.8, 5, 40));

        //Returns to ground
        this.goalSelector.add(3, new ReturnToGroundGoal_Excavator(this));

        //Attack
        this.goalSelector.add(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 2400));


        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, NekkerEntity.class).setGroupRevenge());
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //Sounds

    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGroundDataTracker()) {
            return TCOTS_Sounds.NEKKER_WARRIOR_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.NEKKER_WARRIOR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.NEKKER_WARRIOR_DEATH;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.NEKKER_WARRIOR_LUNGE;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.NEKKER_WARRIOR_ATTACK;
    }
}
