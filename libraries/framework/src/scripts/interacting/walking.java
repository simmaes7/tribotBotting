package scripts.interacting;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

public class walking {
    public static boolean walkingToTile(WorldTile tile,int distance) {
        // Check if we are further than the required distance
        if (tile.distance() > distance) {
            // Try to walk to the tile and wait until we're close enough
            if (GlobalWalking.walkTo(tile) && Waiting.waitUntil(() -> tile.distance() <= distance)) {
                Waiting.waitNormal(600, 90);
                return true; // Successfully walked close to the tile
            } else {
                return false; // Failed to walk to the tile
            }
        }
        return true; // Already within distance, no walking needed
    }
}
