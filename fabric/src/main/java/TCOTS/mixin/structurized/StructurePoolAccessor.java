
package TCOTS.mixin.structurized;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>
 */
@Mixin(StructureTemplatePool.class)
public interface StructurePoolAccessor {

    @Accessor(value = "templates")
    ObjectArrayList<StructurePoolElement> getTemplates();

    @Accessor(value = "rawTemplates")
    List<Pair<StructurePoolElement, Integer>> getRawTemplates();

    @Mutable
    @Accessor(value = "rawTemplates")
    void setRawTemplates(List<Pair<StructurePoolElement, Integer>> list);
}
