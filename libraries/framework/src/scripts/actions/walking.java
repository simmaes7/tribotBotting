package scripts.actions;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

public class walking {
    public void walkToTile(WorldTile tile){
        while (tile.distance() > 5){
            if (GlobalWalking.walkTo(tile) && Waiting.waitUntil(()-> tile.distance() <=5)){
                Waiting.waitNormal(600,90);
            }
        }
    }
}
