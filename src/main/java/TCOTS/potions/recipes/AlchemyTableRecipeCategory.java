package TCOTS.potions.recipes;

import net.minecraft.util.StringIdentifiable;

public enum AlchemyTableRecipeCategory implements StringIdentifiable
{
    POTIONS("potions"),
    BOMBS_OILS("bombs_oils"),
    DECOCTIONS("decoctions"),
    MISC("misc"),
    ALL("all");

    public static final StringIdentifiable.Codec<AlchemyTableRecipeCategory> CODEC;
    private final String id;

    private AlchemyTableRecipeCategory(String id) {
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
