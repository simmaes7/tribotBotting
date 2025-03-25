package scripts;


import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.WalkState;

import java.util.Random;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "ironSmelting",
        name = "Iron Smelter",
        description = "turns iron into iron bars using ring of forging"
)
public class ironSmelting implements TribotScript {
    Random random = new Random();
    int mean = 3000;
    @Override
    public void execute(@NotNull String s) {
        while (true){
            if(Inventory.contains("Iron ore")){
                if (!MyPlayer.isAnimating()) {
                    Waiting.waitUntil(1500, MyPlayer::isAnimating);//orig at 2500
                    if (!MyPlayer.isAnimating()) {
                        if(Query.gameObjects().nameEquals("Furnace").isVisible().isAny()){
                            smeltItem();
                            Waiting.waitUntil(6000, MyPlayer::isAnimating);
                        }else{
                            //we cant see the furnace so lets reposition the camera
                            Query.gameObjects().nameEquals("Furnace").findBestInteractable().map(GameObject::adjustCameraTo);
                            smeltItem();
                            Waiting.waitUntil(6000, MyPlayer::isAnimating);
                        }
                    }
                }
            }
            else if (!Inventory.contains("Iron ore") && Bank.isNearby()){
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.ensureOpen();
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.depositInventory();
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.depositEquipment();
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.withdraw("Ring of forging",1);
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.close();
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Query.inventory().nameEquals("Ring of forging").stream().forEach(i->i.click("Wear"));
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.ensureOpen();
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.withdraw("Iron ore",28);
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.close();
            }
        }
    }
    private boolean smeltItem() {
        if(clickFurnace()
                && Waiting.waitUntil(MakeScreen::isOpen)
                && Waiting.waitUntil(()->MakeScreen.make("Iron bar"))
                && Waiting.waitUntil(MyPlayer::isAnimating)) { return true; }
        else { return false; }
    }
    private boolean clickFurnace() {
        return Query.gameObjects().isReachable()
                .nameEquals("Furnace")
                .findBestInteractable()
                .map(i->i.click("Smelt"))
                .orElse(false);
    }
    private boolean depositAll() {
        if(Bank.ensureOpen()
                && Bank.depositInventory()
                && Waiting.waitUntil(Inventory::isEmpty)) { return true; }
        else { return false; }
    }
}
