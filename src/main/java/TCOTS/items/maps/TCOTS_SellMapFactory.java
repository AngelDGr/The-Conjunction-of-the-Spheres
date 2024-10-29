package TCOTS.items.maps;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

public class TCOTS_SellMapFactory implements TradeOffers.Factory {
    private final int price;
    private final TagKey<Structure> structure;
    private final String nameKey;
    private final TCOTS_MapIcons.Type iconType;
    private final int maxUses;
    private final int experience;

    public TCOTS_SellMapFactory(int price, TagKey<Structure> structure, String nameKey, TCOTS_MapIcons.Type iconType, int maxUses, int experience) {
        this.price = price;
        this.structure = structure;
        this.nameKey = nameKey;
        this.iconType = iconType;
        this.maxUses = maxUses;
        this.experience = experience;
    }

    @Override
    @Nullable
    public TradeOffer create(Entity entity, Random random) {

        if (!(entity.getWorld() instanceof ServerWorld serverWorld)) {
            return null;
        }

        TagKey<Structure> tagKey = TagKey.of(RegistryKeys.STRUCTURE, new Identifier(TCOTS_Main.MOD_ID, "on_ice_giant_maps"));


        BlockPos blockPos = serverWorld.locateStructure(tagKey, entity.getBlockPos(), 100, true);
        if (blockPos != null) {
            ItemStack itemStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
            FilledMapItem.fillExplorationMap(serverWorld, itemStack);
            TCOTS_Maps.addCustomIconsNbt(itemStack, blockPos, "+", this.iconType);
            itemStack.setCustomName(Text.translatable(this.nameKey));
            return new TradeOffer(new ItemStack(Items.EMERALD, this.price), new ItemStack(Items.COMPASS), itemStack, this.maxUses, this.experience, 0.2f);
        }
        return null;
    }
}
