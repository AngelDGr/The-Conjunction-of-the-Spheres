package TCOTS.utils;

import TCOTS.mixin.ServerWorldAccessor;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.spawner.SpecialSpawner;

import java.util.ArrayList;
import java.util.List;

public class ServerWorldSpawnersUtil {
    public static void register(ServerWorld world, SpecialSpawner spawner) {
        List<SpecialSpawner> spawnerList = new ArrayList<>(((ServerWorldAccessor) world).getSpawners());
        spawnerList.add(spawner);
        ((ServerWorldAccessor) world).setSpawners(spawnerList);
    }
}
