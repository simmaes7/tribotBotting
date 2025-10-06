package scripts.tasks;

import okhttp3.internal.connection.Exchange;
import okhttp3.internal.http2.Http2ExchangeCodec;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.GrandExchangeOfferQuery;
import org.tribot.script.sdk.types.GrandExchangeOffer;
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
            if (walking.walkingToTile(constants.trees_above_grandExchange.getCenter(),4)){
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
        resource.cutTreeArea("Tree",constants.trees_above_grandExchange);
        miniBreak.maybeMicroBreak(0.15);
        waiter.waitForAnimation();
        miniBreak.maybeShortBreak(0.08);
        waiter.waitForNonAnimation();
        miniBreak.maybeMicroBreak(0.4);
        miniBreak.maybeShortBreak(0.05);
    }
    private static void end(){
        walking.walkingToTile(constants.grandExchange.getCenter(),5);
        Bank.ensureOpen();
        BankSettings.setNoteEnabled(true);
        Bank.withdrawAll("Logs");
        Bank.close();
        GrandExchange.open();
        GrandExchange.CreateOfferConfig config = GrandExchange.CreateOfferConfig.builder()
                .type(GrandExchangeOffer.Type.SELL)  // Change to SELL offer
                .slot(GrandExchangeOffer.Slot.ONE)  // Choose the slot for the offer
                .itemName("Logs")
                .priceAdjustment(-6)
                .quantity(Inventory.getCount("Logs")) // Price per item (you can adjust based on the current market)
                .interruptCondition(() -> false)  // No interrupt condition, let the offer run until done
                .build();
        GrandExchange.placeOffer(config);
    }
}
