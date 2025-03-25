package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GroundItem;
import org.tribot.script.sdk.types.Player;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.util.List;
import java.util.Random;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "cowKilling",
        name = "cowKiller",
        description = "kills cows outside of lumbridge and banks the loot"
)
public class cowKiller implements TribotScript {
    public static final Area cowArea = Area.fromRadius(new WorldTile(3254,3282, 0),10);
    public static final Area stairArea = Area.fromRadius(new WorldTile(3205,3209, 0),2);
    public static final Area bank = Area.fromRadius(new WorldTile(3209,3220, 2), 2);
    Random random = new Random();
    int mean = 3000;
    
    
    
    @Override
    public void execute(@NotNull String s) {
        
        while (true) {
            if (!Inventory.isFull()) {
                // Ensure we're in the cow area first.
                if (!cowArea.containsMyPlayer()) {
                    if (GlobalWalking.walkTo(cowArea.getRandomTile()) && Waiting.waitUntil(cowArea::containsMyPlayer)) {
                        Waiting.waitNormal(600, 90);
                    }
                } else if (!isPlayerInCombat() && !MyPlayer.isAnimating()) {
                    // Attack the cow when not in combat
                    Query.npcs().isReachable().nameEquals("Cow").findBestInteractable().map(cow -> cow.click("Attack"));
                    Waiting.wait(3000);
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    lootWithinDistance(2);
                }
            } else {
                // Inventory is full: handle burying bones if any, and then move to banking
                if (Inventory.contains("Bones")) {
                    Query.inventory().nameEquals("Bones").stream().forEach(bury -> {
                        bury.click("Bury");
                        mean = random.nextInt(1000 - 700 + 1) + 700;
                        Waiting.wait(mean);
                    });
                } else if (!stairArea.containsMyPlayer()) {
                    // Walk to the stair area if not there already.
                    if (GlobalWalking.walkTo(stairArea.getRandomTile()) && Waiting.waitUntil(stairArea::containsMyPlayer)) {
                        Waiting.waitNormal(600, 90);
                    }
                } else {
                    // Once in the stair area, proceed with banking.
                    Query.gameObjects().nameEquals("Staircase").findBestInteractable().map(climb -> climb.click("Climb"));
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Query.gameObjects().nameEquals("Staircase").findBestInteractable().map(climb -> climb.click("Climb-up"));
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Waiting.waitUntil(() -> GlobalWalking.walkTo(bank.getRandomTile()));
                    Bank.ensureOpen();
                    Bank.depositInventory();
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Waiting.waitUntil(() -> Inventory.isEmpty());
                    Bank.close();
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Query.gameObjects().nameEquals("Staircase").findBestInteractable().map(climb -> climb.click("Climb-down"));
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Query.gameObjects().nameEquals("Staircase").findBestInteractable().map(climb -> climb.click("Climb-down"));
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                }
            }
        }

    }

    public boolean isPlayerInCombat() {
        return Query.npcs().isReachable().isMyPlayerInteractingWith().isAny()
                && Query.npcs().isReachable().isInteractingWithMe().isAny();
    }
    public void lootWithinDistance(int distance){
        List<GroundItem> groundItems = Query.groundItems().isReachable().maxDistance(distance).toList();
        for (GroundItem item : groundItems) {
            if (item.getId() == 1739){
                boolean clickedItem = item.interact("Take");
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
            }
            if (item.getId() == 526){
                boolean clickedItem = item.interact("Take");
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
            }
        }
        
    }
}

