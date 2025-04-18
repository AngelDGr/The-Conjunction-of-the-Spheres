package TCOTS.entity.necrophages;

import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.particles.TCOTS_Particles_Fabric;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class ScurverEntity extends RotfiendEntity{
    public ScurverEntity(EntityType<? extends RotfiendEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 8;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.27f);
    }


    @Override
    protected void explode() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, null, null,
                    this.getX(), this.getY(), this.getZ(), (float)2.6, false, Level.ExplosionInteraction.MOB,
                    TCOTS_Particles_Fabric.ROTFIEND_BLOOD_EMITTER, TCOTS_Particles_Fabric.ROTFIEND_BLOOD_EMITTER, TCOTS_Sounds.ROTFIEND_BLOOD_EXPLOSION);
            this.discard();


            for (int i = 0; i <= 90; i += 18) { // Vertical spread from 0 to 90 degrees (half-sphere)
                for (int j = 0; j < 360; j += 30) { // Full horizontal spread
                    Projectile projectileEntity = new ScurverSpineEntity(this, this.level(), new ItemStack(TCOTS_Items_Fabric.SCURVER_SPINE), null);

                    // Calculate the direction vector for the current angles
                    double radianI = Math.toRadians(i+this.getRandom().nextIntBetweenInclusive(0,20));
                    double radianJ = Math.toRadians(j+this.getRandom().nextIntBetweenInclusive(0,20));

                    double x = Math.sin(radianI) * Math.cos(radianJ);
                    double y = Math.cos(radianI);
                    double z = Math.sin(radianI) * Math.sin(radianJ);

                    Vector3f vector3f = new Vector3f((float)x, (float)y, (float)z);

                    projectileEntity.shoot(vector3f.x(), vector3f.y(), vector3f.z(), 0.8f, 1.0f);

                    this.level().addFreshEntity(projectileEntity);
                }
            }


        }
    }

    //Sounds
    @Override
    protected SoundEvent getIdleSound() {
        return TCOTS_Sounds.SCURVER_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.SCURVER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound(RotfiendEntity rotfiend) {
        return TCOTS_Sounds.SCURVER_DEATH;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.SCURVER_LUNGE;
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.SCURVER_ATTACK;
    }

    @Override
    public SoundEvent getExplosionSound() {
        return TCOTS_Sounds.SCURVER_EXPLODING;
    }
}
