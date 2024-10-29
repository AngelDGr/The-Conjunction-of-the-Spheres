package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.items.maps.TCOTS_Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.TradeOffer;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.spawner.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class TestingMixin {

    @Inject(method = "use", at = @At("TAIL"))
    private void getTestingMap(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        if(user.getStackInHand(hand).isOf(Items.STICK)){
            if (!(user.getWorld() instanceof ServerWorld serverWorld)) {
                return;
            }

            TagKey<Structure> tagKey = TagKey.of(RegistryKeys.STRUCTURE, new Identifier(TCOTS_Main.MOD_ID, "on_ice_giant_maps"));

            BlockPos blockPos = serverWorld.locateStructure(tagKey, user.getBlockPos(), 100, true);
            if (blockPos != null) {
                ItemStack mapStack = FilledMapItem.createMap(serverWorld, blockPos.getX(), blockPos.getZ(), (byte)2, true, true);
                FilledMapItem.fillExplorationMap(serverWorld, mapStack);
//                TCOTS_Maps.addCustomIconsNbt(mapStack, blockPos, "+", MapIcon);
                MapState.addDecorationsNbt(mapStack, blockPos, "+", MapIcon.Type.BANNER_BLACK);
                mapStack.setCustomName(Text.literal("Testing"));

                user.getInventory().insertStack(mapStack);
            }
        }
    }

}
