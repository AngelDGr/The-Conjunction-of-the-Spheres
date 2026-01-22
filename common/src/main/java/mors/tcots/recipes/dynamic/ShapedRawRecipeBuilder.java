package mors.tcots.recipes.dynamic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ShapedRawRecipeBuilder implements DynamicRecipe {

    private ResourceLocation id;
    private String suffix;
    private final RecipeCategory category;
    private final ResourceLocation result;
    private final int count;
    private final Map<Character, Pair<String, ResourceLocation>> keys;
    private final List<String> pattern;
    private ShapedRawRecipeBuilder alternative=null;

    public ShapedRawRecipeBuilder(final RecipeCategory category, final Item result, final int count) {
        this(category, BuiltInRegistries.ITEM.getKey(result), count);
    }

    public ShapedRawRecipeBuilder(final RecipeCategory category, final ResourceLocation result, final int count){
        this.category=category;
        this.keys=new HashMap<>();
        this.pattern=new ArrayList<>();
        this.result=result;
        this.count=count;
    }

    public static ShapedRawRecipeBuilder shaped(final RecipeCategory recipeCategory, final Item result) {
        return new ShapedRawRecipeBuilder(recipeCategory, result, 1);
    }

    public static ShapedRawRecipeBuilder shaped(final RecipeCategory recipeCategory, final Item result, final int count) {
        return new ShapedRawRecipeBuilder(recipeCategory, result, count);
    }

    public ShapedRawRecipeBuilder pattern(final String string) {
        this.pattern.add(string);
        return this;
    }

    public ShapedRawRecipeBuilder define(final Character c, final Supplier<Item> item){
        this.keys.put(c, new Pair<>("item", BuiltInRegistries.ITEM.getKey(item.get())));
        return this;
    }

    public ShapedRawRecipeBuilder define(final Character c, final Item item){
        this.keys.put(c, new Pair<>("item", BuiltInRegistries.ITEM.getKey(item)));
        return this;
    }

    public ShapedRawRecipeBuilder define(final Character c, final TagKey<Item> tag){
        this.keys.put(c, new Pair<>("tag", tag.location()));
        return this;
    }

    public ShapedRawRecipeBuilder define(final Character c, final ResourceLocation tag, final boolean isTag){
        this.keys.put(c, new Pair<>(isTag? "tag": "item", tag));
        return this;
    }

    public ShapedRawRecipeBuilder define(final Character c, final ResourceLocation item){
        return define(c, item, false);
    }

    public ShapedRawRecipeBuilder defineDynamic(final Character c,
                                                final ResourceLocation standard, final boolean isTag,
                                                final ResourceLocation alternative, final boolean isTag2){
        this.keys.put(c, new Pair<>(isTag? "tag": "item", standard));

        if(this.alternative==null)
            this.alternative=this.copy();

        this.alternative.keys.put(c, new Pair<>(isTag2? "tag": "item", alternative));

        return this;
    }

    private ShapedRawRecipeBuilder copy() {
        ShapedRawRecipeBuilder copy =
                new ShapedRawRecipeBuilder(this.category, this.result, this.count);

        copy.pattern.addAll(this.pattern);
        copy.keys.putAll(this.keys);
        return copy;
    }

    public ShapedRawRecipeBuilder defineDynamic(final Character c, final ResourceLocation standard, final ResourceLocation alternative){
        return defineDynamic(c, standard, false, alternative, false);
    }

    public ShapedRawRecipeBuilder defineDynamic(final Character c, final Item standard, final ResourceLocation alternative){
        return defineDynamic(c, BuiltInRegistries.ITEM.getKey(standard), false, alternative, false);
    }

    public ShapedRawRecipeBuilder unlockedBy(final String string, final ResourceLocation arg, final Map<String, RecipeAdvancementJsonBuilder> advancements){

        advancements.put(id().getPath(),
                new RecipeAdvancementJsonBuilder(
                        string,
                        id(),
                        arg
                )
        );


        return this;
    }

    public ShapedRawRecipeBuilder unlockedBy(final String string, final Item arg, final Map<String, RecipeAdvancementJsonBuilder> advancements){
        return unlockedBy(string, BuiltInRegistries.ITEM.getKey(arg), advancements);
    }

    public ShapedRawRecipeBuilder unlockedByDynamic(final String string, final Item arg, final Map<String, RecipeAdvancementJsonBuilder> advancements){

        //Normal
        advancements.put(id().getPath()+"_standard",
                new RecipeAdvancementJsonBuilder(
                        string,
                        ResourceLocation.fromNamespaceAndPath(id().getNamespace(), id().getPath()+"_standard"),
                        BuiltInRegistries.ITEM.getKey(arg)
                )
        );

        //Alternative
        advancements.put(id().getPath()+"_"+this.suffix,
                new RecipeAdvancementJsonBuilder(
                        string,
                        ResourceLocation.fromNamespaceAndPath(id().getNamespace(), id().getPath()+"_"+this.suffix),
                        BuiltInRegistries.ITEM.getKey(arg)
                )
        );

        return this;
    }


    public void saveDynamic(final List<DynamicRecipe> recipes) {
        saveDynamic(recipes, suffix);
    }

    @Override
    public void saveDynamic(final List<DynamicRecipe> recipes, final String suffix) {
        final ResourceLocation standard  = ResourceLocation.fromNamespaceAndPath(this.id().getNamespace(), this.id().getPath()+"_standard");
        final ResourceLocation variation = ResourceLocation.fromNamespaceAndPath(this.id().getNamespace(), this.id().getPath()+"_"+suffix);

        this.save(recipes, standard);
        alternative.save(recipes, variation);
    }

    @Override
    public JsonObject export() {
        final JsonObject mainJson = new JsonObject();
        //Type
        mainJson.addProperty("type", "minecraft:crafting_shaped");

        //Category
        mainJson.addProperty("category", this.determineBookCategory(category).getSerializedName());

        //We create a new Json Element, and add our crafting pattern to it.
        final JsonArray jsonArray = new JsonArray();
        for(final String s : pattern) {
            jsonArray.add(s);
        }
        //Then we add the pattern to our json object.
        mainJson.add("pattern", jsonArray);

        //Next we need to define what the keys in the pattern are. For this we need different JsonObjects per key definition, and one main JsonObject that will contain all the defined keys.
        JsonObject individualKey; //Individual key
        final JsonObject keyList = new JsonObject(); //The main key object, containing all the keys

        for (final Character c : keys.keySet()) {
            individualKey = new JsonObject();
            individualKey.addProperty(keys.get(c).getA(), keys.get(c).getB().toString()); //This will create a key in the form "type": "input", where type is either "item" or "tag", and input is our input item.
            keyList.add(c + "", individualKey); //Then we add this key to the main key object.
            //This will add:
            //"#": { "tag": "c:copper_ingots" }
            //and after that
            //"|": { "item": "minecraft:sticks" }
            //and so on.
        }

        mainJson.add("key", keyList);

        //Result
        final JsonObject jsonobject = new JsonObject();
        jsonobject.addProperty("id", result.toString());
        jsonobject.addProperty("count", count);
        mainJson.add("result", jsonobject);

        return mainJson;
    }

    @Override
    public RecipeCategory category() { return this.category;}

    @Override
    public ResourceLocation id() { return this.id;}

    @Override
    public ShapedRawRecipeBuilder id(final ResourceLocation id) { this.id=id; return this;}

    public ShapedRawRecipeBuilder id(final ResourceLocation id, final String suffix) {  this.suffix=suffix; return id(id);}

}
