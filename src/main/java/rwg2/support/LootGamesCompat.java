package rwg2.support;

import net.minecraft.block.Block;

import eu.usrv.legacylootgames.blocks.DungeonBrick;
import ru.timeconqueror.lootgames.registry.LGBlocks;

/**
 * LootGames blocks behind a class of their own. Handing a LootGames block to a method that takes a {@link Block} makes
 * the JVM verifier load that block's class when it links the caller, so any class that does so directly crashes without
 * LootGames installed even if the code is never reached. Only call these when {@link Mods#LOOTGAMES} is set.
 */
final class LootGamesCompat {

    private LootGamesCompat() {}

    static Block puzzleMaster() {
        return LGBlocks.PUZZLE_MASTER;
    }

    static Block dungeonWall() {
        return LGBlocks.DUNGEON_WALL;
    }

    static int wallMetadata() {
        return DungeonBrick.Type.WALL.ordinal();
    }

    static int shieldedFloorMetadata() {
        return DungeonBrick.Type.FLOOR_SHIELDED.ordinal();
    }
}
