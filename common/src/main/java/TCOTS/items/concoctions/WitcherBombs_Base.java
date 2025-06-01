package TCOTS.items.concoctions;

import TCOTS.registry.TCOTS_Items;
import TCOTS.utils.EntitiesUtil;
import TCOTS.entity.misc.WitcherBombEntity;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WitcherBombs_Base extends Item {
    private final String bombId;
    private final int level;
    public WitcherBombs_Base(Properties settings, String bombId, int level) {
        super(settings);
        this.bombId=bombId;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player playerEntity, @NotNull InteractionHand hand) {
        ItemStack itemStack = playerEntity.getItemInHand(hand);
        //Launch the bomb
        if (!world.isClientSide) {
            WitcherBombEntity bombEntity = new WitcherBombEntity(world, playerEntity, bombId, level);
            playerEntity.getCooldowns().addCooldown(this, EntitiesUtil.isWearingManticoreArmor(playerEntity)? 10: 40);
            bombEntity.setItem(itemStack);
            bombEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), -20.0f, 0.8f, 1.0f);
            world.addFreshEntity(bombEntity);
        }
        playerEntity.awardStat(Stats.ITEM_USED.get(this));
        if (!playerEntity.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        //Gives player the powder
        //Select the powder
        ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_BOMB_POWDER);

        stack_Empty.set(DataComponents.MAX_STACK_SIZE, this.getDefaultMaxStackSize());

        stack_Empty.set(TCOTS_Items.RefillRecipe(), BuiltInRegistries.ITEM.getKey(this).toString());

        if(itemStack.getCount()==0){
            //If you spend all the bombs in the slot, and can't insert the stack in any other place other than your hand
            if(playerEntity.getInventory().getFreeSlot()==playerEntity.getInventory().selected
                    && playerEntity.getInventory().getSlotWithRemainingSpace(stack_Empty)==-1) {
                return InteractionResultHolder.sidedSuccess(stack_Empty, world.isClientSide());
            }
        }

        if (!playerEntity.getAbilities().instabuild) {
            //If the player inventories its full
            if (playerEntity.getInventory().getFreeSlot() == -1) {
                playerEntity.level().addFreshEntity(new ItemEntity(playerEntity.level(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), stack_Empty));
            } else {
                playerEntity.getInventory().add(stack_Empty);
            }
        }
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag type) {
        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancing_star") || Objects.equals(bombId, "samum")  ){
            tooltip.add(Component.translatable("tooltip.bomb.monster_nest.first").withStyle(ChatFormatting.GRAY,ChatFormatting.ITALIC));
            tooltip.add(Component.translatable("tooltip.bomb.monster_nest.second").withStyle(ChatFormatting.GRAY,ChatFormatting.ITALIC));
        }

        tooltip.add(Component.translatable(((Objects.equals(bombId, "moon_dust")) && getLevel() > 1) ? "tooltip."+bombId+".extra2" : "tooltip."+bombId+".first").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip."+bombId+".second").withStyle(ChatFormatting.GRAY));

        if((Objects.equals(bombId, "samum") && getLevel()>1) || (Objects.equals(bombId, "northern_wind") && getLevel()>1) || (Objects.equals(bombId, "moon_dust") && getLevel()>1)){
            tooltip.add(Component.translatable("tooltip."+bombId+".extra").withStyle(ChatFormatting.GRAY));
        }
    }
}
