package scripts.tasks;

import
import org.tribot.script.sdk.Bank;
import org.tribot.script.sdk.Equipment;
import org.tribot.script.sdk.Inventory;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.walking.GlobalWalking;

public class moneyTask {
    boolean enoughLogs = false;
    public void execute(){
        //setup();
        while (!enoughLogs){
            Bank.ensureOpen();
            Bank.depositInventory();
            if (!Equipment.contains("Bronze axe") && !Inventory.contains("Bronze axe")){
                Bank.withdraw("Bronze axe",1);
                Bank.close();
                Equipment.equip("Bronze axe");
            }
            Bank.close();


            while (constants.trees_above_grandExchange.getCenter().distance() > 5){
                if (GlobalWalking.walkTo(constants.trees_above_grandExchange.getCenter()) && Waiting.waitUntil(()-> constants.trees_above_grandExchange.getCenter().distance() <=5)){
                    Waiting.waitNormal(600,90);
                }
            }
        }
    }
    public void setup(){
        walkToGE();
        Bank.ensureOpen();
        Bank.depositEquipment();
        Bank.depositInventory();
        Bank.close();
    }
    public void walkToGE(){
        while (constants.grandExchange.getCenter().distance() > 5){
            if (GlobalWalking.walkTo(constants.grandExchange.getCenter()) && Waiting.waitUntil(()-> constants.grandExchange.getCenter().distance() <=5)){
                Waiting.waitNormal(600,90);
            }
        }
    }
}
