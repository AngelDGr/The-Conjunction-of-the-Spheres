package neoforge.TCOTS.patchouli;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class ItemsInLine_CustomComponent implements ICustomComponent {
    private transient int x, y;


    private List<IVariable> items;
    private List<IVariable> tooltips;
    private transient List<ItemStack> items_list=new ArrayList<>();
    private transient List<String> tooltips_list=new ArrayList<>();
    private int index = 0;

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {

        int xAdded=28;
        int i =x+5;

        for (ItemStack stack: items_list){

            if(!stack.isEmpty())
                graphics.renderItem(stack, i, y+13);

            i=i+xAdded;
        }

        if(context.isAreaHovered(mouseX, mouseY, x+5, y+13, 16, 16) && !items_list.isEmpty()){
            if(!items_list.getFirst().isEmpty()){
                List<Component> tooltip= new ArrayList<>();
                addName(tooltip, 0);
                addTooltip(tooltip, 0);
                context.setHoverTooltipComponents(tooltip);
            }
        } else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded), y+13, 16, 16) && items_list.size()>1){
            if(!items_list.get(1).isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                addName(tooltip, 1);
                addTooltip(tooltip, 1);
                context.setHoverTooltipComponents(tooltip);
            }
        } else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded*2), y+13, 16, 16) && items_list.size()>2){
            if(!items_list.get(2).isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                addName(tooltip, 2);
                addTooltip(tooltip, 2);
                context.setHoverTooltipComponents(tooltip);
            }
        }else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded*3), y+13, 16, 16) && items_list.size()>3){
            if(!items_list.get(3).isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                addName(tooltip, 3);
                addTooltip(tooltip, 3);
                context.setHoverTooltipComponents(tooltip);
            }
        } else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded*4), y+13, 16, 16) && items_list.size()>4){
            if(!items_list.get(4).isEmpty()) {
                List<Component> tooltip = new ArrayList<>();
                addName(tooltip, 4);
                addTooltip(tooltip, 4);
                context.setHoverTooltipComponents(tooltip);
            }
        }
    }

    private void addName(List<Component> tooltip, int index){
        tooltip.add(Component.literal(items_list.get(index).getHoverName().getString()+":"));
    }

    private void addTooltip(List<Component> tooltip, int index){
        if(!tooltips_list.isEmpty() && !tooltips_list.get(index).isEmpty()){
            tooltip.add(Component.literal(tooltips_list.get(index)));
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {
        tooltips = lookup.apply(IVariable.wrap(index>0 ? "#tooltips"+index : "#tooltips", registries)).asList(registries);
        items = lookup.apply(IVariable.wrap(index>0 ? "#items"+index : "#items", registries)).asList(registries);

        for(IVariable variable: items){
            items_list.add(variable.as(ItemStack.class));
        }

        for(IVariable variable: tooltips){
            tooltips_list.add(variable.asString());
        }
    }
}
