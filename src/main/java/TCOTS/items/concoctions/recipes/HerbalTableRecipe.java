package TCOTS.items.concoctions.recipes;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.HerbalMixture;
import TCOTS.items.TCOTS_Items;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
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
    public Identifier getId() {
        return new Identifier(TCOTS_Main.MOD_ID, Registries.ITEM.getId(this.herb.getItem()).getPath()+"_herbal");
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

        return HerbalMixture.writeEffects(this.getOutput(registryManager).copy(),  totalEffects);
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
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
        public static final HerbalTableRecipe.Serializer INSTANCE = new HerbalTableRecipe.Serializer();

        //Json Reader
        @Override
        public HerbalTableRecipe read(Identifier id, JsonObject json) {
            //Read the herb necessary
            JsonObject herbStack = JsonHelper.getObject(json, "herb");
            Identifier idHerb = new Identifier(JsonHelper.getString(herbStack, "id"));
            ItemStack herb = new ItemStack(
                    Registries.ITEM.getOrEmpty(idHerb).orElseThrow(() -> new IllegalStateException("Herb: " + idHerb + " does not exist")),
                    herbStack.has("count")? JsonHelper.getInt(herbStack,"count") : 1
            );

            //Read the effects
            List<String> effects = getEffects(JsonHelper.getArray(json, "effects"));

            //Base potion (0=Water Bottle; 1=Mundane Potion; 2=Tick Potion;)
            int basePotion = JsonHelper.getInt(json, "base_potion", 0);

            int tickEffectTime = JsonHelper.getInt(json, "time", 40);

            int badAmplifier = JsonHelper.getInt(json, "amplifier", 0);

            return new HerbalTableRecipe(herb, effects, basePotion, tickEffectTime, badAmplifier);
        }

        private static DefaultedList<String> getEffects(JsonArray json) {
            DefaultedList<String> defaultedList = DefaultedList.of();

            for (int i = 0; i < json.size(); i++) {
                defaultedList.add(json.get(i).getAsString());
            }

            return defaultedList;
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
        public HerbalTableRecipe read(Identifier id,PacketByteBuf buf) {
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
