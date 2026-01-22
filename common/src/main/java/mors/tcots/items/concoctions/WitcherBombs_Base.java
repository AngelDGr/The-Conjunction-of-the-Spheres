package mors.tcots.items.concoctions;

import mors.tcots.registry.TCOTS_EntityAttributes;
import mors.tcots.entity.misc.WitcherBombEntity;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.utils.tooltip.FontHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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

import java.util.List;
import java.util.Objects;

public class WitcherBombs_Base extends Item {
    private final String bombId;
    private final int level;

    public WitcherBombs_Base(final Properties settings, final String bombId, final int level) {
        super(settings);
        this.bombId=bombId;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(final Level world, final Player playerEntity, @NotNull final InteractionHand hand) {
        final ItemStack itemStack = playerEntity.getItemInHand(hand);
        //Launch the bomb
        if (!world.isClientSide) {
            final WitcherBombEntity bombEntity = new WitcherBombEntity(world, playerEntity, bombId, level);

            final var bombAttribute = playerEntity.getAttribute(TCOTS_EntityAttributes.BOMB_COOLDOWN);

            final int baseCooldown=40;

            double cooldown = 1;
            if (bombAttribute != null) cooldown = bombAttribute.getValue();

            final int finalCooldown = (int)(baseCooldown * cooldown);

            playerEntity.getCooldowns().addCooldown(this, Math.max(finalCooldown, 0));
            bombEntity.setItem(itemStack);
            bombEntity.shootFromRotation(playerEntity, playerEntity.getXRot(), playerEntity.getYRot(), -20.0f, 0.8f, 1.0f);
            world.addFreshEntity(bombEntity);
        }
        playerEntity.awardStat(Stats.ITEM_USED.get(this));
        if (!playerEntity.getAbilities().instabuild) {
            itemStack.consume(1, playerEntity);
        }

        //Gives player the powder
        //Select the powder
        final ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_BOMB_POWDER);

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
    @Environment(EnvType.CLIENT)
    public void appendHoverText(@NotNull final ItemStack stack, @NotNull final TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag type) {
        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancing_star") || Objects.equals(bombId, "samum")  ){
            tooltip.addAll(FontHelper.cutTextComponent(Component.translatable("item.tcots_witcher.bomb.tooltip.monster_nest"), FontHelper.Palette.ALL_GRAY_ITALIC));
        }

        final boolean hasPerLevelDescription = this.bombId.equals("samum") || this.bombId.equals("northern_wind") || this.bombId.equals("moon_dust");

        tooltip.addAll(FontHelper.cutTextComponent(Component.translatable("item.tcots_witcher.bomb.tooltip."+bombId +(hasPerLevelDescription ? getLevel(): "")), FontHelper.Palette.ALL_GRAY));
    }
}
