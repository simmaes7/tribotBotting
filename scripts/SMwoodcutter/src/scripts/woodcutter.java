package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.antiban.*;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "SMwoodcutting",
        name = "SMwoodcutting",
        description = "cuts oak wood for a certain amount of time"
)
public class woodcutter implements TribotScript{
    public static Area oak_trees = Area.fromRectangle(new WorldTile(3171, 3407), new WorldTile(3159, 3422));
    private boolean stop = false;
    private double stopChance = 0.05;
    private final Logger log = new Logger("Woodcutter");

    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
        config.setBreakHandlerEnabled(false);
        config.setRandomsAndLoginHandlerEnabled(false);
    }
    
    
    @Override
    public void execute(@NotNull String s) {
        Camera.setZoomPercent(0);
        //setup();
        while (!stop){
            while (!Bank.isNearby()){
                lottery.execute(0.04, miniBreak::shortBreak);
                GlobalWalking.walkToBank();
                log.debug("Walking to bank");
            }
            Bank.ensureOpen();
            Bank.depositInventory();
            miniBreak.maybeMicroBreak(0.12);
            miniBreak.maybeMediumBreak(0.05);
            miniBreak.maybeLongBreak(0.02);
            Bank.close();
            if (maybeStopSession()){
                log.info("stopped the session");
            }
            else{
                log.info("stopchance is "+ stopChance);
            }
            if (oak_trees.getCenter().distance() > 3){
                if (GlobalWalking.walkTo(oak_trees.getCenter()) && Waiting.waitUntil(()-> oak_trees.getCenter().distance() <= 3)){
                    Waiting.waitNormal(600, 90);
                }
            }
            /*
            if (Bank.isNearby()){
                log.debug("walking to trees now");
                lottery.execute(0.04, miniBreak::shortBreak);
                GlobalWalking.walkTo(oak_trees.getRandomTile());
            }
             */
            while (!Inventory.isFull()){
                log.debug("Cutting inventory");
                woodCutting();
            }
        }
        Login.logout();
    }

    public void cutTree(){
        Query.gameObjects()
                .nameEquals("Oak tree")
                .filter(oak_trees::contains)
                .isReachable()
                .findClosest()
                .ifPresent(tree -> tree.click("Chop down"));
    }
    public void woodCutting(){
        lottery.execute(0.35, miniBreak::microBreak);
        cutTree();
        lottery.execute(0.15, miniBreak::microBreak);
        waitForAnimation();
        lottery.execute(0.08, miniBreak::shortBreak);
        waitForNonAnimation();
        lottery.execute(0.40, miniBreak::microBreak);
        lottery.execute(0.05, miniBreak::shortBreak);
    }
    public void waitForAnimation(){
        log.info("waiting for animation");
        do {
            Waiting.wait(100);
        } while (!MyPlayer.isAnimating());
    }
    public void waitForNonAnimation(){
        log.info("waiting for animation to stop");
        do {
            Waiting.wait(1200);
        } while(MyPlayer.isAnimating());
    }
    public void setup(){
        while(!Bank.isNearby()){
            GlobalWalking.walkToBank();
        }
        Bank.ensureOpen();
        Bank.depositInventory();
        Bank.depositEquipment();
        
        while (!Inventory.contains("Iron axe")){
            Bank.withdraw("Iron axe",1);
            Waiting.wait(1000);
        }
        log.debug("i withdrew the axe");
        Bank.close();
        while (!Equipment.contains("Iron axe")){
            wearItem("Iron axe");
            Waiting.wait(1000);
        }
        log.debug("i tried to wield the axe");
    }
    public void wearItem(String itemName){
        Query.inventory().nameEquals(itemName).stream().forEach(i->i.click("Wield"));
    }
    private boolean maybeStopSession() {
        // Try to stop with the current chance
        lottery.execute(stopChance, () -> stop = true);

        // Increase chance by 5% each time (cap at 100%)
        stopChance = Math.min(1.0, stopChance + 0.05);
        return stop;
    }
}
