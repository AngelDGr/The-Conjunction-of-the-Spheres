package neoforge.TCOTS.patchouli;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class MonsterLoot_CustomComponent implements ICustomComponent {
    private transient int x, y;

    private String title = "Loot";
    private String color_title = "000000";
    private boolean single = false;

    private List<IVariable> items;
    private transient List<ItemStack> items_list=new ArrayList<>();

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        int colorInt = Integer.parseInt(color_title, 16);

        MutableComponent titleToRender = Component.literal(title).withStyle(style -> style.withColor(colorInt).withFont(Minecraft.UNIFORM_FONT).withBold(true));

        graphics.drawString(Minecraft.getInstance().font, titleToRender, x, y, -1, false);


        int xAdded=28;
        int i =x+5;

        for (ItemStack stack: items_list){

            graphics.renderItem(stack, i, y+13);

            i=i+xAdded;
        }


        if(context.isAreaHovered(mouseX, mouseY, x+5, y+13, 16, 16) && !items_list.isEmpty()){
            setTooltip(context,items_list.getFirst());
        } else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded), y+13, 16, 16) && items_list.size()>1){
            setTooltip(context,items_list.get(1));
        } else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded*2), y+13, 16, 16) && items_list.size()>2){
            setTooltip(context,items_list.get(2));
        }else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded*3), y+13, 16, 16) && items_list.size()>3){
            setTooltip(context,items_list.get(3));
        } else if(context.isAreaHovered(mouseX, mouseY, (x+5)+(xAdded*4), y+13, 16, 16) && items_list.size()>4){
            setTooltip(context,items_list.get(4));
        }
    }

    public void setTooltip(IComponentRenderContext context, ItemStack item){
        if(Minecraft.getInstance().player!=null){
            TooltipFlag.Default tooltipFlag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;


            context.setHoverTooltipComponents(item.getTooltipLines(Item.TooltipContext.EMPTY, Minecraft.getInstance().player, tooltipFlag));
        }
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {

        items = lookup.apply(IVariable.wrap(single ? "#mutagen": "#items", registries)).asList(registries);

        for(IVariable variable: items){
            if(!variable.as(ItemStack.class).isEmpty())
                items_list.add(variable.as(ItemStack.class));
        }
    }
}
