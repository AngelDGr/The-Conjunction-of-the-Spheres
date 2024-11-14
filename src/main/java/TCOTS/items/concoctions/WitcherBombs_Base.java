package TCOTS.items.concoctions;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.TCOTS_Items;
import TCOTS.utils.EntitiesUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class WitcherBombs_Base extends Item {
    private final String bombId;
    private final int level;
    public WitcherBombs_Base(Settings settings, String bombId, int level) {
        super(settings);
        this.bombId=bombId;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        //Launch the bomb
        if (!world.isClient) {
            WitcherBombEntity bombEntity = new WitcherBombEntity(world, playerEntity, bombId, level);
            playerEntity.getItemCooldownManager().set(this, EntitiesUtil.isWearingManticoreArmor(playerEntity)? 10: 40);
            bombEntity.setItem(itemStack);
            bombEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), -20.0f, 0.8f, 1.0f);
            world.spawnEntity(bombEntity);
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!playerEntity.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        //Gives player the powder
        //Select the powder
        ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_BOMB_POWDER);

        stack_Empty.set(DataComponentTypes.MAX_STACK_SIZE, this.getMaxCount());

        stack_Empty.set(TCOTS_Items.REFILL_RECIPE, Registries.ITEM.getId(this).toString());

        if(itemStack.getCount()==0){
            //If you spend all the bombs in the slot, and can't insert the stack in any other place other than your hand
            if(playerEntity.getInventory().getEmptySlot()==playerEntity.getInventory().selectedSlot
                    && playerEntity.getInventory().getOccupiedSlotWithRoomForStack(stack_Empty)==-1) {
                return TypedActionResult.success(stack_Empty, world.isClient());
            }
        }

        if (!playerEntity.getAbilities().creativeMode) {
            //If the player inventories its full
            if (playerEntity.getInventory().getEmptySlot() == -1) {
                playerEntity.getWorld().spawnEntity(new ItemEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), stack_Empty));
            } else {
                playerEntity.getInventory().insertStack(stack_Empty);
            }
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancing_star") || Objects.equals(bombId, "samum")  ){
            tooltip.add(Text.translatable("tooltip.bomb.monster_nest.first").formatted(Formatting.GRAY,Formatting.ITALIC));
            tooltip.add(Text.translatable("tooltip.bomb.monster_nest.second").formatted(Formatting.GRAY,Formatting.ITALIC));
        }

        tooltip.add(Text.translatable(((Objects.equals(bombId, "moon_dust")) && getLevel() > 1) ? "tooltip."+bombId+".extra2" : "tooltip."+bombId+".first").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip."+bombId+".second").formatted(Formatting.GRAY));

        if((Objects.equals(bombId, "samum") && getLevel()>1) || (Objects.equals(bombId, "northern_wind") && getLevel()>1) || (Objects.equals(bombId, "moon_dust") && getLevel()>1)){
            tooltip.add(Text.translatable("tooltip."+bombId+".extra").formatted(Formatting.GRAY));
        }
    }
}
