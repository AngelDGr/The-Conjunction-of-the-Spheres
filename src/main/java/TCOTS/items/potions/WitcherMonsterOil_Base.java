package TCOTS.items.potions;

import TCOTS.entity.WitcherGroup;
import TCOTS.items.TCOTS_Items;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitcherMonsterOil_Base extends Item {
    //Levels:
        //Normal:   10% ---> 2.0 damage = 1.00 hearts                Smite I  : 2.5  damage
        //Enhanced: 25% ---> 4.0 damage = 2.00 hearts                Smite II : 5.0  damage
        //Superior: 50% ---> 6.0 damage = 3.00 hearts                Smite III: 7.5  damage
        //                                                           Smite IV : 10.0 damage
        //                                                           Smite V  : 12.5 damage

    private final int group_value;
    private final int uses;
    private final int extraDamage;
    private final int level;
    private final Text againstDescription;

    public WitcherMonsterOil_Base(Settings settings, EntityGroup group, int uses, int level) {
        super(settings);

        if(group instanceof WitcherGroup witcherGroup){
            group_value=witcherGroup.getNumericID();
            againstDescription =
                    Text.translatable("tooltip.tcots-witcher.oils", Text.translatable(witcherGroup.getTranslationKey())).formatted(Formatting.GRAY);
        } else if (group == EntityGroup.ILLAGER) {
            group_value=11;
            againstDescription =
                    Text.translatable("tooltip.tcots-witcher.oils", Text.translatable("entity.tcots-witcher.group.humanoids")).formatted(Formatting.GRAY);
        } else {
            group_value=100;
            againstDescription =
                    Text.translatable("tooltip.tcots-witcher.oils", "Missigno").formatted(Formatting.GRAY);
        }

        extraDamage = level*2;

        this.uses=uses;
        this.level=level;
    }

    public int getLevel() {
        return level;
    }

    public int getUses() {
        return uses;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return switch (getLevel()) {
            case 2, 3 -> Rarity.UNCOMMON;
            default -> Rarity.COMMON;
        };
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NbtCompound nbt;
        if(user.getMainHandStack().getItem() instanceof SwordItem || user.getMainHandStack().getItem() instanceof AxeItem){
            if(user.getMainHandStack().hasNbt()){
                nbt = user.getMainHandStack().getNbt();
                assert nbt != null;
                if (nbt.contains("Monster Oil")){
                    NbtCompound monsterOil= user.getMainHandStack().getSubNbt("Monster Oil");

                    //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
                    assert monsterOil != null;
                    if(monsterOil.getInt("Uses")==getUses() && monsterOil.getInt("Id") == this.group_value && monsterOil.getInt("Level")==getLevel()){
                        return TypedActionResult.fail(user.getStackInHand(hand));
                    }
                }
            }

            ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
            stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());

            user.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1 );
            NbtCompound nbtOil = new NbtCompound();


            nbtOil.putInt("Id", group_value);
            nbtOil.putInt("Uses", getUses());
            nbtOil.putInt("Level", getLevel());
            nbtOil.putString("Item", Registries.ITEM.getId(this).toString());

            user.getMainHandStack().getOrCreateNbt().put("Monster Oil", nbtOil);

            if(!user.getAbilities().creativeMode){
                user.getOffHandStack().decrement(1);

                //If the player inventory it's full
                if(user.getInventory().getEmptySlot() == -1){
                    user.getWorld().spawnEntity(new ItemEntity(user.getWorld(), user.getX(), user.getY(), user.getZ(), stack_Empty));
                } else{
                    user.giveItemStack(stack_Empty);
                }

            }


            return TypedActionResult.consume(user.getOffHandStack());
        }

        return TypedActionResult.pass(user.getStackInHand(hand));

    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || slot.canTakePartial(player)) {
            return false;
        }

        NbtCompound nbt;
        if(otherStack.getItem() instanceof SwordItem || otherStack.getItem() instanceof AxeItem){
            if(otherStack.hasNbt()){
                nbt = otherStack.getNbt();
                assert nbt != null;
                if (nbt.contains("Monster Oil")){
                    NbtCompound monsterOil= otherStack.getSubNbt("Monster Oil");

                    //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
                    assert monsterOil != null;
                    if(monsterOil.getInt("Uses")==getUses() && monsterOil.getInt("Id") == this.group_value && monsterOil.getInt("Level")==getLevel()){
                        return false;
                    }
                }
            }


            ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
            stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());

            player.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1);
            NbtCompound nbtOil= new NbtCompound();


            nbtOil.putInt("Id", group_value);
            nbtOil.putInt("Uses", getUses());
            nbtOil.putInt("Level", getLevel());
            nbtOil.putString("Item", Registries.ITEM.getId(this).toString());

            otherStack.getOrCreateNbt().put("Monster Oil", nbtOil);

            stack.decrement(1);

            //If the player inventory it's full
            if(player.getInventory().getEmptySlot() == -1){
                player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), stack_Empty));
            } else{
                player.giveItemStack(stack_Empty);
            }

        }
        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        }
        NbtCompound nbt;
        ItemStack itemStackInSlot = slot.getStack();

        if(itemStackInSlot.getItem() instanceof SwordItem || itemStackInSlot.getItem() instanceof AxeItem){
            if(itemStackInSlot.hasNbt()){
                nbt = itemStackInSlot.getNbt();
                assert nbt != null;
                if (nbt.contains("Monster Oil")){
                    NbtCompound monsterOil= itemStackInSlot.getSubNbt("Monster Oil");

                    //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
                    assert monsterOil != null;
                    if(monsterOil.getInt("Uses")==getUses() && monsterOil.getInt("Id") == this.group_value && monsterOil.getInt("Level")==getLevel()){
                        return false;
                    }
                }
            }


                ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
                stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());


                player.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1 );
                NbtCompound nbtOil= new NbtCompound();


                nbtOil.putInt("Id", group_value);
                nbtOil.putInt("Uses", getUses());
                nbtOil.putInt("Level", getLevel());
                nbtOil.putString("Item", Registries.ITEM.getId(this).toString());

                itemStackInSlot.getOrCreateNbt().put("Monster Oil", nbtOil);

                stack.decrement(1);

                //If the player inventory it's full
                if(player.getInventory().getEmptySlot() == -1){
                    player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), stack_Empty));
                } else{
                    player.giveItemStack(stack_Empty);
                }

            }

        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        //Against Necrophages:
        tooltip.add(this.againstDescription);
        //  +2 Attack Damage
        tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.oils.attack", extraDamage)).formatted(Formatting.BLUE));
        //Duration:
        tooltip.add(Text.translatable("tooltip.tcots-witcher.oils.duration").formatted(Formatting.GRAY));
        //  60 Hits
        tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.oils.uses", getUses()).formatted(Formatting.BLUE)));
    }
}
