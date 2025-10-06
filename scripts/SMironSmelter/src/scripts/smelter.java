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
        category = "ironSmelting",
        name = "Iron Smelter",
        description = "turns iron into iron bars using ring of forging"
)
public class smelter implements TribotScript{
    public static WorldTile edgeville= new WorldTile(3100, 3496);
    private boolean stop = false;
    private double stopChance = 0.7;
    private final Logger log = new Logger("Smelter");

    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
        config.setBreakHandlerEnabled(false);
        config.setRandomsAndLoginHandlerEnabled(false);
    }

    @Override
    public void execute(@NotNull String s) {
        int ironOreCount=0;
        setup();
        while (!stop){
            Bank.ensureOpen();
            if (!Equipment.contains("Ring of forging") || !Inventory.contains("Ring of forging")){
                if (Bank.contains("Ring of forging")){
                    Bank.withdraw("Ring of forging",1);
                    while (Inventory.contains("Ring of forging")){
                        Waiting.wait(100);
                    }
                    Equipment.equip("Ring of forging");
                } else{
                    stop = true;
                }
            }
            if (!stop){
                ironOreCount = Bank.getCount("Iron ore");
                if (ironOreCount <28){
                    stop = true;
                }
                while(!Inventory.isEmpty()){
                    Bank.depositInventory();
                    Bank.depositEquipment();
                }
            }

        }
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
        while(edgeville.distance()>4){
            GlobalWalking.walkTo(edgeville);
        }
        GlobalWalking.walkToBank();
        Bank.ensureOpen();
        Bank.depositInventory();
        Bank.depositEquipment();
        Bank.close();
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
