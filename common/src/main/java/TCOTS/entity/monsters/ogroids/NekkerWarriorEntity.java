package TCOTS.entity.monsters.ogroids;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.goals.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NekkerWarriorEntity extends NekkerEntity implements GeoEntity {
    //xTODO: Add mutagen
    //xTODO: Add custom sounds
    //xTODO: Add bestiary entry
    //xTODO: Add decoction
    //xTODO: Add loot_table
    //xTODO: Add spawn
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NekkerWarriorEntity(final EntityType<? extends NekkerEntity> entityType, final Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0f) //Amount of health that hurts you
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.28f)

                .add(Attributes.ATTACK_KNOCKBACK, 0.4f)
                .add(Attributes.ARMOR, 2f);
    }

    @Override
    protected void registerGoals() {

        //Emerge from ground
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(2, new LungeAttackGoal(this, 200, 2.0, 5, 50));

        //Returns to ground
        this.goalSelector.addGoal(3, new ReturnToGroundGoal_Excavator(this));

        //Attack
        this.goalSelector.addGoal(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 2400));

        this.goalSelector.addGoal(5, new ReturnToNestGoal(this, 0.75));

        this.goalSelector.addGoal(6, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.addGoal(7, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, NekkerEntity.class).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public boolean doHurtTarget(final @NotNull Entity target) {
        final boolean bl = super.doHurtTarget(target);
        if(target instanceof final Player player && player.isBlocking() && this.getRandom().nextInt()%10==0){
            player.disableShield();
        }
        return bl;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //Sounds

    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGround()) {
            return TCOTS_Sounds.getSoundEvent("nekker_warrior_idle");
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("nekker_warrior_hurt");
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("nekker_warrior_death");
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.getSoundEvent("nekker_warrior_lunge");
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("nekker_warrior_attack");
    }
}
