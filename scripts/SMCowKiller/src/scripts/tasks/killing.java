package scripts.tasks;

import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GroundItem;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.antiban.*;

import java.util.List;

public class killing implements task{
    private static boolean isKilled;
    public static final Area cowArea = Area.fromPolygon(new WorldTile(3265, 3255)
            , new WorldTile(3265, 3255)
            , new WorldTile(3265, 3296)
            , new WorldTile(3257, 3299)
            , new WorldTile(3241, 3298)
            , new WorldTile(3243, 3283)
            , new WorldTile(3253, 3274)
            , new WorldTile(3253, 3255)
            , new WorldTile(3264, 3255)
            
    );
    @Override
    public void execute() {
        if (!cowArea.containsMyPlayer()){
            GlobalWalking.walkTo(cowArea.getRandomTile());
            Waiting.waitUntil(cowArea::containsMyPlayer);
        }
        while (!Inventory.isFull()){
            killCow();
            lottery.execute(0.13, () -> miniBreak.fatigueLeave(1));
            Waiting.wait(fatigueResolver.getMilliseconds());
            lottery.execute(0.3, miniBreak::leave);
            while (!isKilled){
                lootFloor();
            }
        }
    }
    public static void killCow(){
        isKilled = false;
        if (!MyPlayer.isAnimating() && !MyPlayer.isMoving()){
            Query.npcs().nameEquals("Cow")
                    .isNotBeingInteractedWith()
                    .isVisible()
                    .findBestInteractable()
                    .ifPresent(i->i.click("Attack"));
        }
        Waiting.waitUntil(()->!MyPlayer.isAnimating());
        Waiting.waitNormal(1000,50);
        
        if (Query.groundItems().nameEquals("Cowhide").maxDistance(2).isAny());{
            isKilled = true;
        }
    }
    public static void lootFloor(){
        List<GroundItem> groundItems = Query.groundItems().isReachable().maxDistance(2).toList();
        for (GroundItem groundItem : groundItems){
            if (groundItem.getId() == 1739){
                while (Inventory.getCount("Cowhide") != Inventory.getCount("Cowhide")+1){
                    groundItem.interact("Take");
                    Waiting.waitNormal(700,50);
                    Waiting.waitUntil(()->!MyPlayer.isAnimating());
                }
                
            }
            if (groundItem.getId() == 526){
                while (Inventory.getCount("Cowhide") != Inventory.getCount("Cowhide")+1){
                    groundItem.interact("Take");
                    Waiting.waitNormal(700,50);
                    Waiting.waitUntil(()->!MyPlayer.isAnimating());
                }
            }
        }
    }
}
