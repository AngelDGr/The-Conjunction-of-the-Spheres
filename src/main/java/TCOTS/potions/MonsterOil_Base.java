package TCOTS.potions;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonsterOil_Base extends Item {

    //TODO: Add tooltip about the oils to the swords
    //TODO: Add the Hanged Man's and Beast Oils

    //Levels:
        //Normal:   10% ---> 2.0 damage = 1.00 hearts                Smite I  : 2.5  damage
        //Enhanced: 25% ---> 4.0 damage = 2.00 hearts                Smite II : 5.0  damage
        //Superior: 50% ---> 6.0 damage = 3.00 hearts                Smite III: 7.5  damage
        //                                                           Smite IV : 10.0 damage
        //                                                           Smite V  : 12.5 damage

    EntityGroup group;
    String id_group;
    int group_value;
    int uses;

    String damageString;

    int level;

    public MonsterOil_Base(Settings settings, EntityGroup group, int uses, int level) {
        super(settings);
        this.group = group;
        if(group == TCOTS_Entities.NECROPHAGES){
            id_group = "Necrophages";
            this.group_value = 0;
        } else if (group == TCOTS_Entities.OGROIDS) {
            id_group = "Ogroids";
            this.group_value = 1;
        } else if(group == TCOTS_Entities.SPECTERS){
            id_group = "Specters";
            this.group_value = 2;
        } else if(group == TCOTS_Entities.VAMPIRES){
            id_group = "Vampires";
            this.group_value = 3;
        } else if(group == TCOTS_Entities.INSECTOIDS){
            id_group = "Insectoids";
            this.group_value = 4;
        } else if(group == TCOTS_Entities.BEASTS){
            id_group = "Beasts";
            this.group_value = 5;
        } else if(group == TCOTS_Entities.ELEMENTA){
            id_group = "Elementa";
            this.group_value = 6;
        }else if(group == TCOTS_Entities.CURSED_ONES){
            id_group = "Cursed Ones";
            this.group_value = 7;
        }else if(group == TCOTS_Entities.HYBRIDS){
            id_group = "Hybrids";
            this.group_value = 8;
        }else if(group == TCOTS_Entities.DRACONIDS){
            id_group = "Draconids";
            this.group_value = 9;
        }else if(group == TCOTS_Entities.RELICTS){
            id_group = "Relicts";
            this.group_value = 10;
        }else if(group == EntityGroup.ILLAGER){
            id_group = "Illagers & Villagers";
            this.group_value = 11;
        }
        else{
            id_group = "None";
            this.group_value=100;
        }


        if(level==1){
            damageString="+2";
        } else if (level==2) {
            damageString="+4";
        } else if (level==3) {
            damageString="+6";
        }else {
            damageString="Missigno";
        }

        this.uses=uses;
        this.level=level;

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        NbtCompound nbt;
        if(user.getMainHandStack().getItem() instanceof SwordItem || user.getMainHandStack().getItem() instanceof AxeItem){
            if(user.getMainHandStack().hasNbt()){
                nbt = user.getMainHandStack().getNbt();
                if (nbt.contains("Monster Oil")){
                    NbtCompound monsterOil= user.getMainHandStack().getSubNbt("Monster Oil");

                    //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
                    if(monsterOil.getInt("Uses")==uses && monsterOil.getInt("Id") == this.group_value && monsterOil.getInt("Level")==level){
                        return TypedActionResult.fail(user.getStackInHand(hand));
                    }
                }
            }

            ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
            stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());


            user.playSound(SoundEvents.ITEM_HONEYCOMB_WAX_ON, 1,1 );
            NbtCompound nbtOil= new NbtCompound();


            nbtOil.putInt("Id", group_value);
            nbtOil.putInt("Uses", uses);
            nbtOil.putInt("Level", level);

            user.getMainHandStack().getOrCreateNbt().put("Monster Oil", nbtOil);

            if(!user.getAbilities().creativeMode){
                user.getOffHandStack().decrement(1);
                user.giveItemStack(stack_Empty);
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
                if (nbt.contains("Monster Oil")){
                    NbtCompound monsterOil= otherStack.getSubNbt("Monster Oil");

                    //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
                    if(monsterOil.getInt("Uses")==uses && monsterOil.getInt("Id") == this.group_value && monsterOil.getInt("Level")==level){
                        return false;
                    }
                }
            }


            ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
            stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());

            player.playSound(SoundEvents.ITEM_HONEYCOMB_WAX_ON, 1,1);
            NbtCompound nbtOil= new NbtCompound();


            nbtOil.putInt("Id", group_value);
            nbtOil.putInt("Uses", uses);
            nbtOil.putInt("Level", level);

            otherStack.getOrCreateNbt().put("Monster Oil", nbtOil);

            stack.decrement(1);

            player.giveItemStack(stack_Empty);

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
                if (nbt.contains("Monster Oil")){
                    NbtCompound monsterOil= itemStackInSlot.getSubNbt("Monster Oil");

                    //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
                    if(monsterOil.getInt("Uses")==uses && monsterOil.getInt("Id") == this.group_value && monsterOil.getInt("Level")==level){
                        return false;
                    }
                }
            }


                ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
                stack_Empty.getOrCreateNbt().putString("Potion", Registries.ITEM.getId(this).toString());

                player.playSound(SoundEvents.ITEM_HONEYCOMB_WAX_ON, 1,1 );
                NbtCompound nbtOil= new NbtCompound();


                nbtOil.putInt("Id", group_value);
                nbtOil.putInt("Uses", uses);
                nbtOil.putInt("Level", level);

                itemStackInSlot.getOrCreateNbt().put("Monster Oil", nbtOil);

                stack.decrement(1);

                player.giveItemStack(stack_Empty);

            }

        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.tcots-witcher.oils", damageString, id_group).formatted(Formatting.GRAY));
        tooltip.add(Text.translatable("tooltip.tcots-witcher.oils.uses", uses).formatted(Formatting.GRAY));
    }
}
