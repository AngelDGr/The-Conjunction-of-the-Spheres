package TCOTS.mixin;

import TCOTS.interfaces.MapStateMixinInterface;
import TCOTS.items.maps.TCOTS_MapIcons;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MapState.class)
public abstract class MapStateMixin implements MapStateMixinInterface {

    @Shadow protected abstract void addIcon(MapIcon.Type type, @Nullable WorldAccess world, String key, double x, double z, double rotation, @Nullable Text text);

    @Shadow @Final private Map<String, MapIcon> icons;

    @Inject(method = "update", at = @At("TAIL"))
    public void injectCustomIcons(PlayerEntity player, ItemStack stack, CallbackInfo ci){
        NbtCompound nbtCompound;
        if ((nbtCompound = stack.getNbt()) != null && nbtCompound.contains("CustomIcons", NbtElement.LIST_TYPE)) {
            NbtList nbtList = nbtCompound.getList("CustomIcons", NbtElement.COMPOUND_TYPE);
            for (int j = 0; j < nbtList.size(); ++j) {
                NbtCompound nbtCompound2 = nbtList.getCompound(j);
                if (this.icons.containsKey(nbtCompound2.getString("id"))) continue;
                this.addIcon(MapIcon.Type.byId(nbtCompound2.getByte("type")), player.getWorld(), nbtCompound2.getString("id"), nbtCompound2.getDouble("x"), nbtCompound2.getDouble("z"), nbtCompound2.getDouble("rot"), null);
            }
        }
    }
}
