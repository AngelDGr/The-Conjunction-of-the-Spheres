package TCOTS.items.concoctions;

import TCOTS.items.TCOTS_Items;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class WitcherMonsterOil_Base extends Item {
    //Levels:
        //Normal:   10% ---> 2.0 damage = 1.00 hearts                Smite I  : 2.5  damage
        //Enhanced: 25% ---> 4.0 damage = 2.00 hearts                Smite II : 5.0  damage
        //Superior: 50% ---> 6.0 damage = 3.00 hearts                Smite III: 7.5  damage
        //                                                           Smite IV : 10.0 damage
        //                                                           Smite V  : 12.5 damage

    private final int group_id;
    private final int uses;
    private final int extraDamage;
    private final int level;
    private final Text againstDescription;

    public WitcherMonsterOil_Base(Settings settings, MonsterOilType group, int uses, int level) {
        super(settings);

        group_id =group.getNumericID();
        againstDescription = Text.translatable("tooltip.tcots-witcher.oils", Text.translatable(group.getTranslationKey())).formatted(Formatting.GRAY);

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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.getMainHandStack().getItem() instanceof SwordItem || user.getMainHandStack().getItem() instanceof AxeItem){
            if (notHasSameOil(user.getMainHandStack())){
                ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
                stack_Empty.set(TCOTS_Items.REFILL_RECIPE, Registries.ITEM.getId(this).toString());


                user.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1 );

                user.getMainHandStack().set(TCOTS_Items.MONSTER_OIL_COMPONENT, MonsterOilComponent.of(group_id, getUses(), getLevel(), Registries.ITEM.getId(this).toString()));

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
        }

        return TypedActionResult.pass(user.getStackInHand(hand));

    }

    private boolean notHasSameOil(ItemStack stack){
        if(!stack.contains(TCOTS_Items.MONSTER_OIL_COMPONENT)){
            return true;
        }


        MonsterOilComponent monsterOil = stack.get(TCOTS_Items.MONSTER_OIL_COMPONENT);
        //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
        return monsterOil == null || monsterOil.uses() != getUses() || monsterOil.groupId() != this.group_id || monsterOil.level() != getLevel();
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType != ClickType.RIGHT || slot.canTakePartial(player)) {
            return false;
        }


        if(otherStack.getItem() instanceof SwordItem || otherStack.getItem() instanceof AxeItem){
            if (notHasSameOil(otherStack)){
                ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
                stack_Empty.set(TCOTS_Items.REFILL_RECIPE, Registries.ITEM.getId(this).toString());

                player.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1);

                otherStack.set(TCOTS_Items.MONSTER_OIL_COMPONENT, MonsterOilComponent.of(group_id, getUses(), getLevel(), Registries.ITEM.getId(this).toString()));

                stack.decrement(1);

                //If the player inventory it's full
                if(player.getInventory().getEmptySlot() == -1){
                    player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), stack_Empty));
                } else{
                    player.giveItemStack(stack_Empty);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        }

        ItemStack itemStackInSlot = slot.getStack();
        if(itemStackInSlot.getItem() instanceof SwordItem || itemStackInSlot.getItem() instanceof AxeItem){
            if (notHasSameOil(itemStackInSlot)){

                ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL);
                stack_Empty.set(TCOTS_Items.REFILL_RECIPE, Registries.ITEM.getId(this).toString());


                player.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1 );

                itemStackInSlot.set(TCOTS_Items.MONSTER_OIL_COMPONENT, MonsterOilComponent.of(group_id, getUses(), getLevel(), Registries.ITEM.getId(this).toString()));

                stack.decrement(1);

                //If the player inventory it's full
                if(player.getInventory().getEmptySlot() == -1){
                    player.getWorld().spawnEntity(new ItemEntity(player.getWorld(), player.getX(), player.getY(), player.getZ(), stack_Empty));
                } else{
                    player.giveItemStack(stack_Empty);
                }
            }
        }

        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        //Against Necrophages:
        tooltip.add(this.againstDescription);
        //  +2 Attack Damage
        tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.oils.attack", extraDamage)).formatted(Formatting.BLUE));
        //Duration:
        tooltip.add(Text.translatable("tooltip.tcots-witcher.oils.duration").formatted(Formatting.GRAY));
        //  60 Hits
        tooltip.add(ScreenTexts.space().append(Text.translatable("tooltip.tcots-witcher.oils.uses", getUses()).formatted(Formatting.BLUE)));
    }

    public enum MonsterOilType {
        NECROPHAGES ("necrophages",0),
        OGROIDS ("ogroids",1),
        SPECTERS ("specters",2),
        VAMPIRES ("vampires",3),
        INSECTOIDS ("insectoids",4),
        BEASTS ("beasts",5),
        ELEMENTA ("elementa",6),
        CURSED_ONES ("cursed_ones",7),
        HYBRIDS ("hybrids",8),
        DRACONIDS ("draconids",9),
        RELICTS("relicts",10),
        HUMANOID("humanoids",11);
        private final String id;

        private final int numericID;

        MonsterOilType(String id, int numericID){
            this.id=id;
            this.numericID=numericID;
        }

        public String getTranslationKey() {
            return "entity.tcots-witcher.group."+id;
        }

        public int getNumericID() {
            return numericID;
        }
    }
}
