package mors.tcots.recipes;

import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.items.HerbalMixture;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HerbalTableRecipe implements Recipe<HerbalTableRecipe.HerbalTableInventory> {

        public record HerbalTableInventory(ItemStack herb, ItemStack potion)  implements RecipeInput {

            @Override
            public @NotNull ItemStack getItem(final int slot) {
                return switch (slot){
                    case 0 -> herb;
                    case 1 -> potion;
                    default -> throw new IllegalStateException("Unexpected value: " + slot);
                };
            }

            @Override
            public int size() {
                return 2;
            }
        }

    private final ItemStack herb;
    private final List<ResourceLocation> EffectID = new ArrayList<>();
    private final int basePotion;

    private final int tickEffectTime;
    public static final String ID_STRING = "herbal_table";

    private final int badAmplifier;

    public HerbalTableRecipe(final ItemStack herb, final List<String> EffectID, final int basePotion, final int tickEffectTime, final int badAmplifier){
        this.herb=herb;
        EffectID.forEach(s -> this.EffectID.add(ResourceLocation.parse(s)));
        this.basePotion=basePotion;
        this.tickEffectTime = tickEffectTime;
        this.badAmplifier=badAmplifier;
    }
    @Override
    public boolean matches(@NotNull final HerbalTableInventory inventory, final Level world) {
        if(world.isClientSide()) {
            return false;
        }

        final ItemStack herb = this.getHerb();

        final int basePotionId = this.getBasePotion();

        if(inventory.getItem(0).getItem() == herb.getItem()){
            if(!inventory.getItem(1).has(DataComponents.POTION_CONTENTS)){
                return false;
            }

            return inventory.getItem(1).get(DataComponents.POTION_CONTENTS).potion().get().equals(
                    basePotionId ==1 ? Potions.MUNDANE :
                            basePotionId ==2?
                                    Potions.THICK:
                                    Potions.WATER);
        }

        return false;
    }

    public List<Holder<MobEffect>> getEffects(){
        final List<Holder<MobEffect>> effectList = new ArrayList<>();

        this.EffectID.forEach(id ->
                effectList.add(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(BuiltInRegistries.MOB_EFFECT.get(id))));

        return effectList;
    }

    public List<ResourceLocation> getEffectID() {
        return EffectID;
    }

    public ItemStack getHerb() {
        return herb;
    }

    public int getBasePotion() {
        return basePotion;
    }

    public int getTickEffectTime() {
        return tickEffectTime;
    }

    public int getBadAmplifier() {
        return badAmplifier;
    }

    @Override
    public @NotNull ItemStack assemble(final HerbalTableInventory inventory, final HolderLookup.@NotNull Provider lookup) {
        final int herbCount = inventory.getItem(0).getCount();

        final List<MobEffectInstance> totalEffects = new ArrayList<>();

        this.getEffects().forEach(effect -> {
            if(!effect.value().isBeneficial())
            {
                totalEffects.add(new MobEffectInstance(effect,  effect.value().isInstantenous()? 1: herbCount * this.getTickEffectTime(), this.badAmplifier));

            }else {
                totalEffects.add(new MobEffectInstance(effect, effect.value().isInstantenous()? 1: herbCount * this.getTickEffectTime()));
            }
        });

        return HerbalMixture.writeEffects(this.getResultItem(lookup).copy(),  totalEffects);
    }

    @Override
    public @NotNull ItemStack getResultItem(final HolderLookup.@NotNull Provider registriesLookup) {
        return new ItemStack(TCOTS_Items.HERBAL_MIXTURE.get());
    }

    @Override
    public boolean canCraftInDimensions(final int width, final int height) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<HerbalTableRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(TCOTS_Blocks.HerbalTable());
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public static class Serializer implements RecipeSerializer<HerbalTableRecipe>{

        //Json Reader
        public static final MapCodec<HerbalTableRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        //Read the herb necessary
                        ItemStack.CODEC.fieldOf("herb")
                                        .forGetter(recipe -> recipe.herb),

                        //Read the effects
                        ExtraCodecs.NON_EMPTY_STRING.listOf().fieldOf("effects")
                                        .forGetter(recipe ->
                                        {
                                            List<String> listStrings = new ArrayList<>();

                                            recipe.EffectID.forEach(id -> listStrings.add(id.toString()));

                                            return listStrings;
                                        }),

                        //Base potion (0=Water Bottle; 1=Mundane Potion; 2=Tick Potion;)
                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("base_potion").orElse(0)
                                .forGetter(recipe -> recipe.basePotion),

                        ExtraCodecs.POSITIVE_INT.fieldOf("time").orElse(40)
                                .forGetter(recipe -> recipe.tickEffectTime),

                        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amplifier").orElse(0)
                                .forGetter(recipe -> recipe.badAmplifier)

                ).apply(instance, HerbalTableRecipe::new));

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull MapCodec<HerbalTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, HerbalTableRecipe> streamCodec() {
            return PACKET_CODEC;
        }

        public static final StreamCodec<RegistryFriendlyByteBuf, HerbalTableRecipe> PACKET_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read
        );

        // Turns Recipe into PacketByteBuf
        public static void write(final RegistryFriendlyByteBuf buf, final HerbalTableRecipe recipe) {

            buf.writeCollection(recipe.getEffectID(), (buff, id) -> buf.writeUtf(id.toString()));

            ItemStack.STREAM_CODEC.encode(buf, recipe.getHerb());

            buf.writeInt(recipe.getBasePotion());

            buf.writeInt(recipe.getTickEffectTime());

            buf.writeInt(recipe.getBadAmplifier());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        public static HerbalTableRecipe read(final RegistryFriendlyByteBuf buf) {
            // Make sure the read in the same order you have written!
            final List<String> effects = buf.readList(FriendlyByteBuf::readUtf);

            final ItemStack herb = ItemStack.STREAM_CODEC.decode(buf);

            final int basePotion = buf.readInt();

            final int tickEffectTime = buf.readInt();

            final int badAmplifier = buf.readInt();

            return new HerbalTableRecipe(herb, effects, basePotion, tickEffectTime, badAmplifier);
        }
    }

}
