package TCOTS.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record MonsterOilComponent(int groupId, int uses, int level, String oilName) implements TooltipProvider {

    public static final Codec<MonsterOilComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Codec.INT.fieldOf("id").forGetter(MonsterOilComponent::groupId),
                            Codec.INT.fieldOf("uses").forGetter(MonsterOilComponent::uses),
                            Codec.INT.fieldOf("level").forGetter(MonsterOilComponent::level),
                            Codec.STRING.fieldOf("oilName").forGetter(MonsterOilComponent::oilName)
                    )
                    .apply(instance, MonsterOilComponent::new)
    );

    public static MonsterOilComponent of(int groupId, int uses, int oilLevel, String oilId){
        return new MonsterOilComponent(groupId, uses, oilLevel, oilId);
    }

    public static MonsterOilComponent decreaseUse(MonsterOilComponent monsterOilComponent){
        return new MonsterOilComponent(monsterOilComponent.groupId(), monsterOilComponent.uses()-1, monsterOilComponent.level(), monsterOilComponent.oilName());
    }

    @Override
    public int groupId() {
        return groupId;
    }

    public int level() {
        return level;
    }

    @Override
    public int uses() {
        return uses;
    }

    public String oilName() {
        return oilName;
    }



    //TODO: Add specific colors to each oil tooltip, update when add more oils
    @SuppressWarnings("all")
    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag type) {
        OilNamer(tooltip, switch (groupId){
            case 0 ->  0xb71520;
            case 1 ->  0xcc341d;
            case 2 ->  0xffffff;
            case 3 ->  0xffffff;
            case 4 ->  0xffffff;
            case 5 ->  0x411106;
            case 6 ->  0xffffff;
            case 7 ->  0xffffff;
            case 8 ->  0xffffff;
            case 9 ->  0xffffff;
            case 10 -> 0xffffff;
            case 11 -> 0x00752b;
            default -> 0xffffff;
        });
    }



    private void OilNamer(Consumer<Component> value, int color){
        value.accept(CommonComponents.EMPTY);
        MutableComponent OilName = (MutableComponent) BuiltInRegistries.ITEM.get(ResourceLocation.parse(this.oilName)).getDescription();
        OilName.withStyle(
                style -> style.withColor(color)
        );

        value.accept(OilName);
        value.accept(CommonComponents.space().append(Component.translatable("tooltip.item.tcots_witcher.oils.uses", this.uses).withStyle(ChatFormatting.BLUE)));
    }
}
