package scripts.tasks;

import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.constants;

import java.util.HashMap;
import java.util.Map;

public class miningSmithingTask {
    
    //areas
    public static final Area ironOreAreaBelow60 = Area.fromRadius(new WorldTile(3175, 3367, 0),2);
    public static final Area ironOreAreaAbove60 = Area.fromRectangle(new WorldTile(3031, 9739,0),new WorldTile(3035, 9735,0));
    public static final Area tinOreArea = Area.fromRadius(new WorldTile(3284, 3363, 0),2);
    public static final Area GrandExchangeArea = Area.fromRadius(new WorldTile(3165, 3485, 0),1);
    public static final Area edgevil = Area.fromRadius(new WorldTile(3101, 3496, 0),2);
    public static Area miningArea;
    String preferedPickaxe;
    String preferedOre;
    
    
    
    private static Map<String, Integer> itemsToBuy = new HashMap<>();
    
    
    public void execute() {
        while (true){
            preferedOre = pickOre();
            preferedPickaxe = pickPickaxe();
            while (!Bank.isNearby()){
                Options.setRunEnabled(true);
                GlobalWalking.walkToBank();
            }
            Bank.ensureOpen();
            Waiting.waitNormal(1400,10);
            Bank.depositInventory();
            Waiting.waitNormal(1400,10);
            Bank.depositEquipment();
            Waiting.waitNormal(1400,10);
            if (Bank.getCount("Iron ore")>=4000){
                break;
            }
            if (!Bank.contains(preferedPickaxe)){
                Bank.close();
                buyItemOnGE(preferedPickaxe,1,5);
                Bank.ensureOpen();
            }
            Bank.withdraw(preferedPickaxe,1);
            Bank.close();
            while (!miningArea.containsMyPlayer()){
                GlobalWalking.walkTo(miningArea.getRandomTile());
                Waiting.waitNormal(1400,10);
            }
            while (!Inventory.isFull()){
                if (!miningArea.containsMyPlayer()){
                    GlobalWalking.walkTo(miningArea.getRandomTile());
                    Waiting.waitNormal(1400,10);
                }
                mineOre(preferedOre);
                waitForAnimation(2000);
            }
        }
        if (Bank.getCount("Ring of forging")<29){
            Bank.close();
            buyItemOnGE("Ring of forging",29,5);
        }
        Bank.close();
        while (!edgevil.containsMyPlayer())
        {
            GlobalWalking.walkTo(edgevil.getRandomTile());
        }
        while (true){
            Options.setRunEnabled(true);
            Bank.ensureOpen();
            Waiting.waitNormal(1400,50);
            Bank.depositInventory();
            Waiting.waitNormal(1400,50);
            Bank.depositEquipment();
            Waiting.waitNormal(1400,50);
            if (!Bank.contains("Iron ore")){
                Bank.close();
                Waiting.waitNormal(900,50);
                sellItemOnGe("Iron bar",-2);
                break;
            }
            Bank.withdraw("Ring of forging",1);
            Waiting.waitNormal(1400,50);
            Bank.close();
            Waiting.waitNormal(1400,50);
            Query.inventory().nameEquals("Ring of forging").stream().forEach(i->i.click("Wear"));
            Waiting.waitNormal(1400,50);
            Bank.ensureOpen();
            Waiting.waitNormal(1400,50);
            Bank.withdraw("Iron ore",28);
            Waiting.waitNormal(1400,50);
            Bank.close();
            Waiting.waitNormal(1400,50);
            Query.gameObjects().nameEquals("Furnace").findBestInteractable().map(GameObject::adjustCameraTo);
            Waiting.waitNormal(1400,50);
            while (Inventory.contains("Iron ore")){
                smeltItem();
            }
        }
    }
    private static void buyItemOnGE(String itemName, int amount, int priceAdjustment){
        org.tribot.script.sdk.GrandExchange.CreateOfferConfig config = org.tribot.script.sdk.GrandExchange.CreateOfferConfig.builder()
                .type(GrandExchangeOffer.Type.BUY)
                .slot(GrandExchangeOffer.Slot.TWO)// This is a buy offer
                .itemName(itemName)  // Item name
                .priceAdjustment(priceAdjustment)
                .quantity(amount)
                .interruptCondition(() -> false)  // No interrupt condition, we want this to run
                .build();
        while (!GrandExchangeArea.containsMyPlayer()){
            GlobalWalking.walkTo(GrandExchangeArea.getRandomTile());
        }
        Bank.ensureOpen();
        Waiting.waitNormal(700,50);
        Log.info("withdrawing all coins");
        Bank.withdrawAll("Coins");
        Waiting.waitNormal(700,50);
        Bank.close();
        Waiting.waitNormal(700,50);
        GrandExchange.open();
        Waiting.waitNormal(700,50);
        Waiting.waitUntil(GrandExchange::isOpen);
        GrandExchange.placeOffer(config);
        Waiting.wait(2000);
        GrandExchange.collectAll();
        Waiting.waitNormal(700,50);
        GrandExchange.close();
        Waiting.waitNormal(700,50);
        Bank.ensureOpen();
        Waiting.waitNormal(700,50);
        Bank.depositInventory();
        Waiting.waitNormal(700,50);
        Bank.close();
    }
    private static void sellItemOnGe(String itemName, int priceAdjustment){
        if (!GrandExchangeArea.containsMyPlayer()){
            GlobalWalking.walkTo(GrandExchangeArea.getRandomTile());
            Waiting.waitNormal(700,50);
        }
        GrandExchange.CreateOfferConfig config = GrandExchange.CreateOfferConfig.builder()
                .type(GrandExchangeOffer.Type.SELL)  // Change to SELL offer
                .slot(GrandExchangeOffer.Slot.ONE)  // Choose the slot for the offer
                .itemName(itemName)
                .priceAdjustment(priceAdjustment)
                .quantity(Inventory.getCount(itemName)) // Price per item (you can adjust based on the current market)
                .interruptCondition(() -> false)  // No interrupt condition, let the offer run until done
                .build();
        Bank.ensureOpen();
        Waiting.waitNormal(700,50);
        BankSettings.setNoteEnabled(true);
        Waiting.wait(1400);
        while (!Inventory.contains(itemName)){
            Bank.withdrawAll(itemName);
            Waiting.wait(1400);
        }
        BankSettings.setNoteEnabled(false);
        Waiting.waitNormal(700,50);
        Bank.close();
        Waiting.waitNormal(700,50);
        GrandExchange.open();
        Waiting.waitNormal(700,50);
        GrandExchange.placeOffer(config);
        Waiting.waitNormal(700,50);
        Waiting.wait(2000);
        GrandExchange.collectAll();
        Waiting.waitNormal(700,50);
        GrandExchange.close();
        Waiting.waitNormal(700,50);
        Bank.ensureOpen();
        Waiting.waitNormal(700,50);
        Bank.depositInventory();
    }
    private static String pickPickaxe(){
        int level = Skill.MINING.getActualLevel();
        String pickaxe = "Iron pickaxe";
        if (level >= 1 && level <= 5){
            pickaxe = "Iron pickaxe";
        } else if(level >= 6 && level <= 10){
            pickaxe = "Steel pickaxe";
        } else if(level >= 11 && level <= 20){
            pickaxe = "Black pickaxe";
        } else if(level >= 21 && level <= 30){
            pickaxe = "Mithril pickaxe";
        } else if(level >= 31 && level <= 40){
            pickaxe = "Adamant pickaxe";
        } else if(level >= 41){
            pickaxe = "Rune pickaxe";
        }
        return pickaxe;
    }
    private String pickOre(){
        int level = Skill.MINING.getActualLevel();
        String ore;
        miningArea = tinOreArea;
        if (level >= 1 && level <= 14){
            ore = "Tin rocks";
            miningArea = tinOreArea;
        } else if(level >= 15 && level <= 59){
            ore = "Iron rocks";
            miningArea = ironOreAreaBelow60;
        } else {
            ore = "Iron rocks";
            miningArea = ironOreAreaAbove60;
        }
        return ore;
    }
    private boolean mineOre(String oreName){
        return Query.gameObjects().isReachable()
                .maxDistance(3)
                .nameEquals(oreName)
                .findBestInteractable()
                .map(i->i.click("Mine"))
                .orElse(false);
    }
    private static void waitForAnimation(int delay){
        Waiting.wait(delay);
        while (MyPlayer.isAnimating()){
            Waiting.wait(2000);
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
}
