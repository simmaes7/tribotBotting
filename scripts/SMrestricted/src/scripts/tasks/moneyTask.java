package scripts.tasks;

import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.Logger;
import scripts.constants;
import scripts.interacting.*;
import scripts.antiban.*;


public class moneyTask {
    static Logger log = new Logger("moneyTask");
    public static void execute(){
        boolean completed = false;
        setup();
        while (!completed){
            if (walking.walkingToTile(4,constants.normalTreeArea.getCenter())){
                log.info("walked to trees");
            } else{log.info("Failed to walk to trees");}
            while (!Inventory.isFull()){
                woodCutting();
            }
            if (!Bank.isNearby()){
                GlobalWalking.walkToBank();
            }
            Bank.ensureOpen();
            Bank.depositInventory();
            if (Bank.getCount("Logs") > 300){
                completed = true;
            }
            Bank.close();
            miniBreak.maybeMicroBreak(0.2);
            miniBreak.maybeMediumBreak(0.1);
            miniBreak.maybeLongBreak(0.05);
        }

    }
    private static void setup(){
        if (!Bank.isNearby()){
            GlobalWalking.walkToBank();
        }
        Bank.ensureOpen();
        Bank.depositEquipment();
        Bank.depositInventory();
        while(Inventory.contains("Bronze axe")){
            Bank.withdraw("Bronze axe",1);
            Waiting.wait(600);
        }
        Bank.close();
    }
    public static void woodCutting(){
        miniBreak.maybeMicroBreak(0.35);
        resource.cutTreeArea("Tree",constants.normalTreeArea);
        miniBreak.maybeMicroBreak(0.15);
        waiter.waitForAnimation();
        miniBreak.maybeShortBreak(0.08);
        waiter.waitForNonAnimation();
        miniBreak.maybeMicroBreak(0.4);
        miniBreak.maybeShortBreak(0.05);
    }
}
