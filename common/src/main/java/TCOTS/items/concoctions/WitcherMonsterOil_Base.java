package TCOTS.items.concoctions;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Items;
import TCOTS.items.components.MonsterOilComponent;

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
import org.jetbrains.annotations.NotNull;

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

    public WitcherMonsterOil_Base(final Properties settings, final MonsterOilType group, final int uses, final int level) {
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
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull final Level world, final Player user, @NotNull final InteractionHand hand) {
        if(user.getMainHandItem().getItem() instanceof SwordItem || user.getMainHandItem().getItem() instanceof AxeItem){
            if (notHasSameOil(user.getMainHandItem())){
                final ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL.get());
                stack_Empty.set(TCOTS_Items.RefillRecipe(), BuiltInRegistries.ITEM.getKey(this).toString());


                user.playSound(TCOTS_Sounds.getSoundEvent("oil_applied"), 1,1 );

                user.getMainHandItem().set(TCOTS_Items.MonsterOilComponent(), MonsterOilComponent.of(group_id, getUses(), getLevel(), BuiltInRegistries.ITEM.getKey(this).toString()));

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

    private boolean notHasSameOil(final ItemStack stack){
        if(!stack.has(TCOTS_Items.MonsterOilComponent())){
            return true;
        }


        final MonsterOilComponent monsterOil = stack.get(TCOTS_Items.MonsterOilComponent());
        //Check if it has full uses AND it is of the same Monster Oil and level, otherwise it can be replaced
        return monsterOil == null || monsterOil.uses() != getUses() || monsterOil.groupId() != this.group_id || monsterOil.level() != getLevel();
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull final ItemStack stack, @NotNull final ItemStack otherStack, @NotNull final Slot slot, @NotNull final ClickAction clickType, @NotNull final Player player, @NotNull final SlotAccess cursorStackReference) {
        if (clickType != ClickAction.SECONDARY || slot.allowModification(player)) {
            return false;
        }


        if(otherStack.getItem() instanceof SwordItem || otherStack.getItem() instanceof AxeItem){
            if (notHasSameOil(otherStack)){
                final ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL.get());
                stack_Empty.set(TCOTS_Items.RefillRecipe(), BuiltInRegistries.ITEM.getKey(this).toString());

                player.playSound(TCOTS_Sounds.getSoundEvent("oil_applied"), 1,1);

                otherStack.set(TCOTS_Items.MonsterOilComponent(), MonsterOilComponent.of(group_id, getUses(), getLevel(), BuiltInRegistries.ITEM.getKey(this).toString()));

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
    public boolean overrideStackedOnOther(@NotNull final ItemStack stack, @NotNull final Slot slot, @NotNull final ClickAction clickType, @NotNull final Player player) {
        if (clickType != ClickAction.SECONDARY) {
            return false;
        }

        final ItemStack itemStackInSlot = slot.getItem();
        if(itemStackInSlot.getItem() instanceof SwordItem || itemStackInSlot.getItem() instanceof AxeItem){
            if (notHasSameOil(itemStackInSlot)){

                final ItemStack stack_Empty = new ItemStack(TCOTS_Items.EMPTY_OIL.get());
                stack_Empty.set(TCOTS_Items.RefillRecipe(), BuiltInRegistries.ITEM.getKey(this).toString());


                player.playSound(TCOTS_Sounds.getSoundEvent("oil_applied"), 1,1 );

                itemStackInSlot.set(TCOTS_Items.MonsterOilComponent(), MonsterOilComponent.of(group_id, getUses(), getLevel(), BuiltInRegistries.ITEM.getKey(this).toString()));

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
    public void appendHoverText(@NotNull final ItemStack stack, @NotNull final TooltipContext context, final List<Component> tooltip, @NotNull final TooltipFlag type) {
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

        MonsterOilType(final String id, final int numericID){
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
