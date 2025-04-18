package TCOTS.items.concoctions;

import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.components.MonsterOilComponent;
import TCOTS.sounds.TCOTS_Sounds;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
    private final Component againstDescription;

    public WitcherMonsterOil_Base(Properties settings, MonsterOilType group, int uses, int level) {
        super(settings);

        group_id =group.getNumericID();
        againstDescription = Component.translatable("tooltip.tcots_witcher.oils", Component.translatable(group.getTranslationKey())).withStyle(ChatFormatting.GRAY);

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
    public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if(user.getMainHandItem().getItem() instanceof SwordItem || user.getMainHandItem().getItem() instanceof AxeItem){
            if (notHasSameOil(user.getMainHandItem())){
                ItemStack stack_Empty = new ItemStack(TCOTS_Items_Fabric.EMPTY_OIL);
                stack_Empty.set(TCOTS_Items_Fabric.REFILL_RECIPE, BuiltInRegistries.ITEM.getKey(this).toString());


                user.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1 );

                user.getMainHandItem().set(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT, MonsterOilComponent.of(group_id, getUses(), getLevel(), BuiltInRegistries.ITEM.getKey(this).toString()));

                if(!user.getAbilities().instabuild){
                    user.getOffhandItem().shrink(1);

                    //If the player inventory it's full
                    if(user.getInventory().getFreeSlot() == -1){
                        user.level().addFreshEntity(new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), stack_Empty));
                    } else{
                        user.addItem(stack_Empty);
                    }

                }
                return InteractionResultHolder.consume(user.getOffhandItem());
            }
        }

        return InteractionResultHolder.pass(user.getItemInHand(hand));

    }

    private boolean notHasSameOil(ItemStack stack){
        if(!stack.has(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT)){
            return true;
        }


        MonsterOilComponent monsterOil = stack.get(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT);
        //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
        return monsterOil == null || monsterOil.uses() != getUses() || monsterOil.groupId() != this.group_id || monsterOil.level() != getLevel();
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess cursorStackReference) {
        if (clickType != ClickAction.SECONDARY || slot.allowModification(player)) {
            return false;
        }


        if(otherStack.getItem() instanceof SwordItem || otherStack.getItem() instanceof AxeItem){
            if (notHasSameOil(otherStack)){
                ItemStack stack_Empty = new ItemStack(TCOTS_Items_Fabric.EMPTY_OIL);
                stack_Empty.set(TCOTS_Items_Fabric.REFILL_RECIPE, BuiltInRegistries.ITEM.getKey(this).toString());

                player.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1);

                otherStack.set(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT, MonsterOilComponent.of(group_id, getUses(), getLevel(), BuiltInRegistries.ITEM.getKey(this).toString()));

                stack.shrink(1);

                //If the player inventory it's full
                if(player.getInventory().getFreeSlot() == -1){
                    player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack_Empty));
                } else{
                    player.addItem(stack_Empty);
                }
            }
        }

        return true;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
        if (clickType != ClickAction.SECONDARY) {
            return false;
        }

        ItemStack itemStackInSlot = slot.getItem();
        if(itemStackInSlot.getItem() instanceof SwordItem || itemStackInSlot.getItem() instanceof AxeItem){
            if (notHasSameOil(itemStackInSlot)){

                ItemStack stack_Empty = new ItemStack(TCOTS_Items_Fabric.EMPTY_OIL);
                stack_Empty.set(TCOTS_Items_Fabric.REFILL_RECIPE, BuiltInRegistries.ITEM.getKey(this).toString());


                player.playSound(TCOTS_Sounds.OIL_APPLIED, 1,1 );

                itemStackInSlot.set(TCOTS_Items_Fabric.MONSTER_OIL_COMPONENT, MonsterOilComponent.of(group_id, getUses(), getLevel(), BuiltInRegistries.ITEM.getKey(this).toString()));

                stack.shrink(1);

                //If the player inventory it's full
                if(player.getInventory().getFreeSlot() == -1){
                    player.level().addFreshEntity(new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack_Empty));
                } else{
                    player.addItem(stack_Empty);
                }
            }
        }

        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
        //Against Necrophages:
        tooltip.add(this.againstDescription);
        //  +2 Attack Damage
        tooltip.add(CommonComponents.space().append(Component.translatable("tooltip.tcots_witcher.oils.attack", extraDamage)).withStyle(ChatFormatting.BLUE));
        //Duration:
        tooltip.add(Component.translatable("tooltip.tcots_witcher.oils.duration").withStyle(ChatFormatting.GRAY));
        //  60 Hits
        tooltip.add(CommonComponents.space().append(Component.translatable("tooltip.tcots_witcher.oils.uses", getUses()).withStyle(ChatFormatting.BLUE)));
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
            return "entity.tcots_witcher.group."+id;
        }

        public int getNumericID() {
            return numericID;
        }
    }
}
