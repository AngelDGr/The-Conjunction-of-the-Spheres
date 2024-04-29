package TCOTS.items.potions;

import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.items.TCOTS_Items;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

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
    @Override
    public Rarity getRarity(ItemStack stack) {
        return switch (this.level) {
            case 1, 2 -> Rarity.UNCOMMON;
            default -> Rarity.COMMON;
        };
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        //Launch the bomb
        if (!world.isClient) {
            WitcherBombEntity bombEntity = new WitcherBombEntity(world, playerEntity, bombId, level);
            bombEntity.setItem(itemStack);
            bombEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), -20.0f, 0.5f, 1.0f);
            world.spawnEntity(bombEntity);
        }
        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!playerEntity.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        //Gives player the powder

        //Select the powder
        ItemStack stack_Empty = switch (this.getMaxCount()) {
            default -> new ItemStack(TCOTS_Items.EMPTY_BOMB_POWDER_2);
            case 3 -> new ItemStack(TCOTS_Items.EMPTY_BOMB_POWDER_3);
            case 4 -> new ItemStack(TCOTS_Items.EMPTY_BOMB_POWDER_4);
        };

        stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());

        if (!playerEntity.getAbilities().creativeMode) {

            //If the player inventories its full
            if(playerEntity.getInventory().getEmptySlot() == -1){
                playerEntity.getWorld().spawnEntity(new ItemEntity(playerEntity.getWorld(), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), stack_Empty));
            } else{
                playerEntity.getInventory().insertStack(stack_Empty);
            }
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }



    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {

        if(Objects.equals(bombId, "grapeshot") || Objects.equals(bombId, "dancingStar") || Objects.equals(bombId, "samum")  ){
            tooltip.add(Text.translatable("tooltip.bomb.monster_nest.first").formatted(Formatting.GRAY,Formatting.ITALIC));
            tooltip.add(Text.translatable("tooltip.bomb.monster_nest.second").formatted(Formatting.GRAY,Formatting.ITALIC));
        }

        tooltip.add(Text.translatable("tooltip."+bombId+".first").formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip."+bombId+".second").formatted(Formatting.GRAY));
    }
}
