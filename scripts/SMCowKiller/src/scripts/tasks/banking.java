package scripts.tasks;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.walking.GlobalWalking;

public class banking implements task{
    @Override
    public void execute() {
        if (!Bank.isNearby()) {
            GlobalWalking.walkToBank();
            Log.info("Walking to bank");
            Waiting.waitUntil(Bank::isNearby);
            
        }
        if (!Bank.isOpen()) {
            Bank.ensureOpen();
            Log.info("Opening Bank");
            Waiting.waitUntil(Bank::isOpen);
        }
        getArmor("Iron full helm");
        getArmor("Iron kiteshield");
        getArmor("Iron platebody");
        getArmor("Iron platelegs");
        getWeapon("Iron scimitar");
        Bank.depositInventory();
        Waiting.waitUntil(Inventory::isEmpty);
        Bank.close();
    }
    public void getArmor(String armourPiece){
        if (!Equipment.contains(armourPiece)&& !Inventory.contains(armourPiece)){
            if (!Bank.contains(armourPiece)){
                Log.error("No " + armourPiece + " in the bank or inventory or equipment");
            }
            else{
                Bank.withdraw(armourPiece,1);
                Waiting.waitUntil(()->Inventory.contains(armourPiece));
                Query.inventory().nameEquals(armourPiece).forEach(i->i.click("Equip"));
            }
            Bank.withdraw(armourPiece,1);
            Waiting.waitUntil(()->Inventory.contains(armourPiece));
        } else if (!Equipment.contains(armourPiece) && Inventory.contains(armourPiece)){
            Query.inventory().nameEquals(armourPiece).forEach(i->i.click("Equip"));
            Waiting.waitUntil(()->Equipment.contains(armourPiece));
        }
    }
    public void getWeapon(String weapon){
        if (!Equipment.contains(weapon)&& !Inventory.contains(weapon)){
            if (!Bank.contains(weapon)){
                Log.error("No " + weapon + " in the bank or inventory or equipment");
            }else{
                Bank.withdraw(weapon,1);
                Waiting.waitUntil(()->Inventory.contains(weapon));
                Query.inventory().nameEquals(weapon).forEach(i->i.click("Wield"));
            }
            Bank.withdraw(weapon,1);
            Waiting.waitUntil(()->Inventory.contains(weapon));
        } else if (!Equipment.contains(weapon) && Inventory.contains(weapon)){
            Query.inventory().nameEquals(weapon).forEach(i->i.click("Wield"));
            Waiting.waitUntil(()->Equipment.contains(weapon));
        }
    }
}
