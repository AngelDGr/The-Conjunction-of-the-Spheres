package TCOTS.items.concoctions.recipes;

import net.minecraft.util.StringIdentifiable;

@SuppressWarnings("deprecation")
public enum AlchemyTableRecipeCategory implements StringIdentifiable
{
    POTIONS("potions"),
    BOMBS_OILS("bombs_oils"),
    DECOCTIONS("decoctions"),
    MISC("misc");

    public static final StringIdentifiable.EnumCodec<AlchemyTableRecipeCategory> CODEC;

    private final String id;

    AlchemyTableRecipeCategory(String id) {
        this.id = id;
    }

    @Override
    public String asString() {
        return this.id;
    }

    static {
        CODEC = StringIdentifiable.createCodec(AlchemyTableRecipeCategory::values);
    }
}
