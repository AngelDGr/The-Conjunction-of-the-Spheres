package TCOTS.items.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public record MonsterOilComponent(int groupId, int uses, int level, String oilName) implements TooltipAppender {

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
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
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



    private void OilNamer(Consumer<Text> value, int color){
        value.accept(ScreenTexts.EMPTY);
        MutableText OilName = (MutableText) Registries.ITEM.get(Identifier.of(this.oilName)).getName();
        OilName.styled(
                style -> style.withColor(color)
        );

        value.accept(OilName);
        value.accept(ScreenTexts.space().append(Text.translatable("tooltip.item.tcots-witcher.oils.uses", this.uses).formatted(Formatting.BLUE)));
    }
}
