package mors.tcots.utils;

import mors.tcots.TCOTS_Main;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;

public class AttributeModifiersMapBuilder {
    Multimap<Holder<Attribute>, AttributeModifier> modifiers = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);

    AttributeModifiersMapBuilder(){}

    public static AttributeModifiersMapBuilder create(){
        return new AttributeModifiersMapBuilder();
    }

    public AttributeModifiersMapBuilder attribute(final Holder<Attribute> attributeHolder, final String id, final double value, final AttributeModifier.Operation operation, boolean condition){
        return condition? attribute(attributeHolder, id, value, operation): this;
    }

    public AttributeModifiersMapBuilder attribute(final Holder<Attribute> attributeHolder, final ResourceLocation id, final double value, final AttributeModifier.Operation operation, boolean condition){
        return condition? attribute(attributeHolder, id, value, operation): this;
    }

    public AttributeModifiersMapBuilder attribute(final Holder<Attribute> attributeHolder, final ResourceLocation id, final double value, final AttributeModifier.Operation operation){
        this.modifiers.put(
                attributeHolder,
                new AttributeModifier(id, value, operation)
        );

        return this;
    }

    public AttributeModifiersMapBuilder attribute(final Holder<Attribute> attributeHolder, final String id, final double value, final AttributeModifier.Operation operation){

        return attribute(attributeHolder, TCOTS_Main.id(id), value, operation);
    }

    public Multimap<Holder<Attribute>, AttributeModifier> getModifiers() {
        return modifiers;
    }
}
