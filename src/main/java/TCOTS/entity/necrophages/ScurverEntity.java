package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.joml.Vector3f;

public class ScurverEntity extends RotfiendEntity{
    public ScurverEntity(EntityType<? extends RotfiendEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 8;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27f);
    }


    @Override
    protected void explode() {
        if (!this.getWorld().isClient) {
            this.dead = true;
            this.getWorld().createExplosion(this,
                    this.getX(), this.getY(), this.getZ(), 2.601234f, World.ExplosionSourceType.MOB);
            this.discard();


            for (int i = 0; i <= 90; i += 18) { // Vertical spread from 0 to 90 degrees (half-sphere)
                for (int j = 0; j < 360; j += 30) { // Full horizontal spread
                    ProjectileEntity projectileEntity = new ScurverSpineEntity(TCOTS_Entities.SCURVER_SPINE, this, this.getWorld());

                    // Calculate the direction vector for the current angles
                    double radianI = Math.toRadians(i+this.getRandom().nextBetween(0,20));
                    double radianJ = Math.toRadians(j+this.getRandom().nextBetween(0,20));

                    double x = Math.sin(radianI) * Math.cos(radianJ);
                    double y = Math.cos(radianI);
                    double z = Math.sin(radianI) * Math.sin(radianJ);

                    Vector3f vector3f = new Vector3f((float)x, (float)y, (float)z);

                    projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), 0.8f, 1.0f);

                    this.getWorld().spawnEntity(projectileEntity);
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
