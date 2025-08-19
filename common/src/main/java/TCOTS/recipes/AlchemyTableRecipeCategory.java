package TCOTS.recipes;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public enum AlchemyTableRecipeCategory implements StringRepresentable
{
    POTIONS("potions"),
    BOMBS_OILS("bombs_oils"),
    DECOCTIONS("decoctions"),
    MISC("misc");

    public static final StringRepresentable.EnumCodec<AlchemyTableRecipeCategory> CODEC;

    private final String id;

    AlchemyTableRecipeCategory(final String id) {
        this.id = id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.id;
    }

    static {
        CODEC = StringRepresentable.fromEnum(AlchemyTableRecipeCategory::values);
    }
}
