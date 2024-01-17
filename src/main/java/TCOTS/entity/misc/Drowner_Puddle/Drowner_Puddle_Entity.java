package TCOTS.entity.misc.Drowner_Puddle;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.FallingBlockEntityRenderer;
import net.minecraft.client.render.entity.TntEntityRenderer;
import net.minecraft.client.render.entity.EndCrystalEntityRenderer;
import net.minecraft.client.render.entity.LeashKnotEntityRenderer;


public class Drowner_Puddle_Entity extends Entity {
    public Drowner_Puddle_Entity(EntityType<? extends Drowner_Puddle_Entity> type, World world) {
        super(type, world);
        this.updateWaterColor();

    }

    @Override
    protected void initDataTracker() {

    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 1)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }
    private int waterColor;

    private void updateWaterColor() {
        BlockPos blockPos = this.getBlockPos();
        this.waterColor = BiomeColors.getWaterColor(this.getWorld(), blockPos);
    }

    public int getWaterColor(){
        return this.waterColor;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
