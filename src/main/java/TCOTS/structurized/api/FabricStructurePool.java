package TCOTS.structurized.api;

import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.util.Identifier;

/**
 * <p>Library borrowed from <a href="https://github.com/fzzyhmstrs/structurized-reborn">Structurized Reborn</a> by <a href="https://modrinth.com/user/fzzyhmstrs">fzzyhmstrs</a>.
 *
 * <p>Represents a modifiable structure pool that has several helper methods for modders.
 */
@SuppressWarnings("unused")
public interface FabricStructurePool {
    /**
     * Adds a new {@link StructurePoolElement} to the {@link StructurePool}.
     * See the alternative {@link #addStructurePoolElement(StructurePoolElement, int)} for details.
     *
     * @param element the element to add
     */
    void addStructurePoolElement(StructurePoolElement element);

    /**
     * Adds a new {@link StructurePoolElement} to the {@link StructurePool}.
     *
     * @param element the element to add
     * @param weight  Minecraft handles weight by adding it that amount of times into a list.}
     */
    void addStructurePoolElement(StructurePoolElement element, int weight);

    /**
     * Gets the underlying structure pool.
     */
    StructurePool getUnderlyingPool();

    /**
     * Gets the identifier for the pool.
     */
    Identifier getId();
}