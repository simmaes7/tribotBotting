package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.util.Random;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "Woodcutting",
        name = "Lumbridge Woodcutter",
        description = "Chops trees outside Lumbridge, banks logs, and ensures you have a bronze axe."
)
public class LumbridgeWoodcutter implements TribotScript {
    public static final Area trees = Area.fromRadius(new WorldTile(3191,3219, 0), 4);
    public static final Area bank = Area.fromRadius(new WorldTile(3209,3220, 2), 2);
    Random random = new Random();
    int mean = 3000;
    
    @Override
    public void execute(@NotNull String s) {
        while (true){
            if (!trees.containsMyPlayer()){
                if (GlobalWalking.walkTo(trees.getRandomTile()) && Waiting.waitUntil(trees::containsMyPlayer)){
                    Waiting.waitNormal(600, 90);
                }
            }
            
            if (trees.containsMyPlayer()){
                if (Inventory.isFull()){
                    GlobalWalking.walkToBank();
                    Bank.ensureOpen();
                    Bank.depositInventory();
                    Waiting.waitNormal(mean,300);
                    Waiting.waitUntil(Inventory::isEmpty);
                    Bank.close();
                }
                if (!Inventory.isFull()){
                    Query.gameObjects().nameEquals("Tree").findBestInteractable().map(chop -> chop.click("Chop down"));
                    mean = random.nextInt(5000 - 1000 + 1) + 1000;
                    Waiting.waitNormal(mean,300);
                    Waiting.waitUntil(15000,() -> !MyPlayer.isAnimating());
                }
                else {
                    mean = random.nextInt(5000 - 1000 + 1) + 1000;
                    Waiting.waitNormal(mean,300);
                }
            }
            if (Inventory.isEmpty()){
                Query.gameObjects().nameEquals("Staircase").findBestInteractable().map(climb -> climb.click("Climb-down"));
                Waiting.waitNormal(mean,300);
                Query.gameObjects().nameEquals("Staircase").findBestInteractable().map(climb -> climb.click("Climb-down"));
                Waiting.waitNormal(mean,300);
            }
        }
    }
}
