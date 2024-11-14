package TCOTS.items.concoctions.recipes;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.HerbalMixture;
import TCOTS.items.TCOTS_Items;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HerbalTableRecipe implements Recipe<HerbalTableRecipe.HerbalTableInventory> {

        public record HerbalTableInventory(ItemStack herb, ItemStack potion)  implements RecipeInput {

            @Override
            public ItemStack getStackInSlot(int slot) {
                return switch (slot){
                    case 0 -> herb;
                    case 1 -> potion;
                    default -> throw new IllegalStateException("Unexpected value: " + slot);
                };
            }

            @Override
            public int getSize() {
                return 2;
            }
        }

    private final ItemStack herb;
    private final List<Identifier> EffectID = new ArrayList<>();
    private final int basePotion;

    private final int tickEffectTime;
    public static final String ID_STRING = "herbal_table";

    private final int badAmplifier;

    public HerbalTableRecipe(ItemStack herb, List<String> EffectID, int basePotion, int tickEffectTime, int badAmplifier){
        this.herb=herb;
        EffectID.forEach(s -> this.EffectID.add(Identifier.of(s)));
        this.basePotion=basePotion;
        this.tickEffectTime = tickEffectTime;
        this.badAmplifier=badAmplifier;
    }
    @Override
    public boolean matches(HerbalTableInventory inventory, World world) {
        if(world.isClient()) {
            return false;
        }

        ItemStack herb = this.getHerb();

        int basePotionId = this.getBasePotion();

        if(inventory.getStackInSlot(0).getItem() == herb.getItem()){
            if(!inventory.getStackInSlot(1).contains(DataComponentTypes.POTION_CONTENTS)){
                return false;
            }

            return inventory.getStackInSlot(1).get(DataComponentTypes.POTION_CONTENTS).potion().get().equals(
                    basePotionId ==1 ? Potions.MUNDANE :
                            basePotionId ==2?
                                    Potions.THICK:
                                    Potions.WATER);
        }

        return false;
    }

    public List<RegistryEntry<StatusEffect>> getEffects(){
        List<RegistryEntry<StatusEffect>> effectList = new ArrayList<>();

        this.EffectID.forEach(id ->
                effectList.add(Registries.STATUS_EFFECT.getEntry(Registries.STATUS_EFFECT.get(id))));

        return effectList;
    }

    public List<Identifier> getEffectID() {
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
    public ItemStack craft(HerbalTableInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        int herbCount = inventory.getStackInSlot(0).getCount();

        List<StatusEffectInstance> totalEffects = new ArrayList<>();

        this.getEffects().forEach(effect -> {
            if(!effect.value().isBeneficial())
            {
                totalEffects.add(new StatusEffectInstance(effect,  effect.value().isInstant()? 1: herbCount * this.getTickEffectTime(), this.badAmplifier));

            }else {
                totalEffects.add(new StatusEffectInstance(effect, effect.value().isInstant()? 1: herbCount * this.getTickEffectTime()));
            }
        });

        return HerbalMixture.writeEffects(this.getResult(lookup).copy(),  totalEffects);
    }

    @Override
    public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return new ItemStack(TCOTS_Items.HERBAL_MIXTURE);
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<HerbalTableRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(TCOTS_Blocks.HERBAL_TABLE);
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
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
                        Codecs.NON_EMPTY_STRING.listOf().fieldOf("effects")
                                        .forGetter(recipe ->
                                        {
                                            List<String> listStrings = new ArrayList<>();

                                            recipe.EffectID.forEach(id -> listStrings.add(id.toString()));

                                            return listStrings;
                                        }),

                        //Base potion (0=Water Bottle; 1=Mundane Potion; 2=Tick Potion;)
                        Codecs.NONNEGATIVE_INT.fieldOf("base_potion").orElse(0)
                                .forGetter(recipe -> recipe.basePotion),

                        Codecs.POSITIVE_INT.fieldOf("time").orElse(40)
                                .forGetter(recipe -> recipe.tickEffectTime),

                        Codecs.NONNEGATIVE_INT.fieldOf("amplifier").orElse(0)
                                .forGetter(recipe -> recipe.badAmplifier)

                ).apply(instance, HerbalTableRecipe::new));

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public MapCodec<HerbalTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, HerbalTableRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        public static final PacketCodec<RegistryByteBuf, HerbalTableRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                Serializer::write, Serializer::read
        );

        // Turns Recipe into PacketByteBuf
        public static void write(RegistryByteBuf buf, HerbalTableRecipe recipe) {

            buf.writeCollection(recipe.getEffectID(), (buff, id) -> buf.writeString(id.toString()));

            ItemStack.PACKET_CODEC.encode(buf, recipe.getHerb());

            buf.writeInt(recipe.getBasePotion());

            buf.writeInt(recipe.getTickEffectTime());

            buf.writeInt(recipe.getBadAmplifier());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        public static HerbalTableRecipe read(RegistryByteBuf buf) {
            // Make sure the read in the same order you have written!
            List<String> effects = buf.readList(PacketByteBuf::readString);

            ItemStack herb = ItemStack.PACKET_CODEC.decode(buf);

            int basePotion = buf.readInt();

            int tickEffectTime = buf.readInt();

            int badAmplifier = buf.readInt();

            return new HerbalTableRecipe(herb, effects, basePotion, tickEffectTime, badAmplifier);
        }
    }

}
