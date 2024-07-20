package TCOTS.items.concoctions.recipes;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.HerbalMixture;
import TCOTS.items.TCOTS_Items;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class HerbalTableRecipe implements Recipe<SimpleInventory> {

    private final ItemStack herb;
    private final List<Identifier> EffectID = new ArrayList<>();
    private final int basePotion;

    private final int tickEffectTime;
    public static final String ID_STRING = "herbal_table";

    private final int badAmplifier;

    public HerbalTableRecipe(ItemStack herb, List<String> EffectID, int basePotion, int tickEffectTime, int badAmplifier){
        this.herb=herb;
        EffectID.forEach(s -> this.EffectID.add(new Identifier(s)));
        this.basePotion=basePotion;
        this.tickEffectTime = tickEffectTime;
        this.badAmplifier=badAmplifier;
    }
    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if(world.isClient()) {
            return false;
        }

        ItemStack herb = this.getHerb();

        int basePotion = this.getBasePotion();

        if(inventory.getStack(0).getItem() == herb.getItem()){
            if(basePotion == 1){
                return PotionUtil.getPotion(inventory.getStack(1)) == Potions.MUNDANE;
            } else if (basePotion == 2) {
                return PotionUtil.getPotion(inventory.getStack(1)) == Potions.THICK;
            } else {
                return PotionUtil.getPotion(inventory.getStack(1)) == Potions.WATER;
            }
        }

        return false;
    }

    public List<StatusEffect> getEffects(){
        List<StatusEffect> effectList = new ArrayList<>();

        this.EffectID.forEach(id -> effectList.add(Registries.STATUS_EFFECT.get(id)));

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
    public ItemStack craft(SimpleInventory inventory, DynamicRegistryManager registryManager) {
        int herbCount = inventory.getStack(0).getCount();

        List<StatusEffectInstance> totalEffects = new ArrayList<>();

        this.getEffects().forEach(effect -> {
            if(!effect.isBeneficial())
            {
                totalEffects.add(new StatusEffectInstance(effect,  effect.isInstant()? 1: herbCount * this.getTickEffectTime(), this.badAmplifier));

            }else {
                totalEffects.add(new StatusEffectInstance(effect, effect.isInstant()? 1: herbCount * this.getTickEffectTime()));
            }
        });

        return HerbalMixture.writeEffects(this.getResult(registryManager).copy(),  totalEffects);
    }

    @Override
    public ItemStack getResult(DynamicRegistryManager registryManager) {
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
        return HerbalTableRecipe.Type.INSTANCE;
    }

    public static class Type implements RecipeType<HerbalTableRecipe> {
        private Type() {}
        public static final HerbalTableRecipe.Type INSTANCE = new HerbalTableRecipe.Type();
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
        public static final Codec<HerbalTableRecipe> CODEC = RecordCodecBuilder.create(
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

        public static final HerbalTableRecipe.Serializer INSTANCE = new HerbalTableRecipe.Serializer();

        @Override
        public Codec<HerbalTableRecipe> codec() {
            return CODEC;
        }


        // Turns Recipe into PacketByteBuf
        @Override
        public void write(PacketByteBuf buf, HerbalTableRecipe recipe) {

            buf.writeCollection(recipe.getEffectID(), (buff, id) -> buf.writeString(id.toString()));

            buf.writeItemStack(recipe.getHerb());

            buf.writeInt(recipe.getBasePotion());

            buf.writeInt(recipe.getTickEffectTime());

            buf.writeInt(recipe.getBadAmplifier());
        }

        // Turns PacketByteBuf into Recipe(InGame)
        @Override
        public HerbalTableRecipe read(PacketByteBuf buf) {
            // Make sure the read in the same order you have written!
            List<String> effects = buf.readList(PacketByteBuf::readString);

            ItemStack herb = buf.readItemStack();

            int basePotion = buf.readInt();

            int tickEffectTime = buf.readInt();

            int badAmplifier = buf.readInt();

            return new HerbalTableRecipe(herb, effects, basePotion, tickEffectTime, badAmplifier);
        }
    }

}
