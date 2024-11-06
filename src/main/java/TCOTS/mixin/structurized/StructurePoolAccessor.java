
package TCOTS.mixin.structurized;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;


/**
 Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>
 */
@Mixin(StructurePool.class)
public interface StructurePoolAccessor {
    @Accessor(value = "elements")
    ObjectArrayList<StructurePoolElement> getElements();

    @Accessor(value = "elementCounts")
    List<Pair<StructurePoolElement, Integer>> getElementCounts();

    @Accessor(value = "elementCounts")
    void setElementCounts(List<Pair<StructurePoolElement, Integer>> list);
}
