package scripts.tasks;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class fishing {
    public static final Area GrandExchangeArea = Area.fromRadius(new WorldTile(3164, 3489, 0),6);
    public static final Area smallFishingNetSpot = Area.fromRectangle(new WorldTile(3246, 3157,0), new WorldTile(3238, 3145,0));
    public static final Area flyFishingSpot = Area.fromRectangle(new WorldTile(3237, 3256,0), new WorldTile(3241, 3240,0));
    static int mean = 3000;
    static Random random = new Random();
    public void execute(){
        Area fishingArea;
        String task = pickFishingTask();
        int fishCount = 0;
        int currentFishCount = 0;
        Map<String, Integer> itemsToBuy = new HashMap<>();
        GlobalWalking.walkToBank();
        waitRandom(700,900);
        Bank.ensureOpen();
        waitRandom(700,900);
        Bank.depositInventory();
        waitRandom(700,900);
        Bank.depositEquipment();
        waitRandom(700,900);
        Log.info("Deposited Inventory");
        if (!checkIfBankHasItem("Small fishing net")){
            itemsToBuy.put("Small fishing net",1);
        }
        if (!checkIfBankHasItem("Fly fishing rod")){
            itemsToBuy.put("Fly fishing rod",1);
        }
        if (!checkIfBankHasItem("Feather")){
            itemsToBuy.put("Feather",5000);
        }
        Bank.close();
        if (!itemsToBuy.isEmpty()){
            Log.info("Walking to grand exchange");
            for (Map.Entry<String, Integer> entry : itemsToBuy.entrySet()) {
                buyItemOnGE(entry.getKey(), entry.getValue(),2);
            }
        }
        Bank.ensureOpen();
        if (task.equals("fly")){
            fishingArea = flyFishingSpot;

            Bank.withdraw("Fly fishing rod",1);
            Bank.withdrawAll("Feather");
            Waiting.waitNormal(1000,50);
            if (Bank.contains("Raw trout")){
                fishCount += Bank.getCount("Raw trout");
            }
            if (Bank.contains("Raw salmon")){
                fishCount += Bank.getCount("Raw salmon");
            }
        } else{
            fishingArea = smallFishingNetSpot;
            Bank.withdraw("Small fishing net",1);
            Waiting.waitNormal(1000,50);
            if (Bank.contains("Raw shrimp")){
                fishCount += Bank.getCount("Raw shrimp");
            }
            if (Bank.contains("Raw anchovies")){
                fishCount += Bank.getCount("Raw anchovies");
            }
        }

        Bank.close();
        while (true){
            if (task.equals("fly")){
                if (!Inventory.contains("Feather")){
                    GlobalWalking.walkToBank();
                    Bank.ensureOpen();
                    Bank.depositInventory();
                    break;
                }
            }
            if (!fishingArea.containsMyPlayer()){
                GlobalWalking.walkTo(fishingArea.getRandomTile());
            } else if (!Inventory.isFull()){
                if (task.equals("fly")){
                    fish(task);
                    waitForAnimation();
                    waitStopAnimation();
                }
                else{
                    fish(task);
                    waitForAnimation();
                    waitStopAnimation();
                }
            } else if (Inventory.isFull()){
                GlobalWalking.walkToBank();
                Bank.ensureOpen();
                if (task.equals("fly")){
                    Bank.depositAll("Raw salmon");
                    Bank.depositAll("Raw trout");
                    currentFishCount += Bank.getCount("Raw salmon");
                    currentFishCount += Bank.getCount("Raw trout");
                    Bank.close();
                    if (currentFishCount - fishCount >200){
                        break;
                    }
                } else{
                    Bank.depositAll("Raw anchovies");
                    Bank.depositAll("Raw shrimps");
                    currentFishCount += Bank.getCount("Raw shrimp");
                    currentFishCount += Bank.getCount("Raw anchovies");
                    Bank.close();
                    if (currentFishCount - fishCount >500){
                        break;
                    }
                }
            }
        }
    }
    private static String pickFishingTask() {
        int level = Skill.FISHING.getActualLevel();

        if (level <20){
            return "net";
        }
        else{
            return "fly";
        }
    }
    private static void waitForAnimation(){
        Waiting.wait(700);
        while (MyPlayer.isAnimating() || MyPlayer.isMoving()){
            Waiting.wait(700);
        }
    }
    private static void waitRandom(int small, int big){
        mean = random.nextInt((big - small) + 1) + small;
        Waiting.wait(mean);
    }
    private static void waitStopAnimation(){
        while (MyPlayer.isAnimating() || MyPlayer.isMoving()){
            Waiting.wait(700);
        }
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
        waitRandom(650,800);
        BankSettings.setNoteEnabled(true);
        waitRandom(650,800);
        while (!Inventory.contains(itemName)){
            Bank.withdrawAll(itemName);
            waitRandom(650,800);
        }
        BankSettings.setNoteEnabled(false);
        waitRandom(650,800);
        Bank.close();
        waitRandom(650,800);
        GrandExchange.open();
        waitRandom(650,800);
        GrandExchange.placeOffer(config);
        waitRandom(650,800);
        Waiting.wait(2000);
        GrandExchange.collectAll();
        waitRandom(650,800);
        GrandExchange.close();
        waitRandom(650,800);
        Bank.ensureOpen();
        waitRandom(650,800);
        Bank.depositInventory();
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
        waitRandom(650,800);
        Log.info("withdrawing all coins");
        Bank.withdrawAll("Coins");
        waitRandom(650,800);
        Bank.close();
        waitRandom(650,800);
        GrandExchange.open();
        waitRandom(650,800);
        Waiting.waitUntil(GrandExchange::isOpen);
        GrandExchange.placeOffer(config);
        Waiting.wait(2000);
        GrandExchange.collectAll();
        waitRandom(650,800);
        GrandExchange.close();
        waitRandom(650,800);
        Bank.ensureOpen();
        waitRandom(650,800);
        Bank.depositInventory();
        waitRandom(650,800);
        Bank.close();
    }
    private static void fish(String method) {
        String spot = method.equals("fly") ? "Rod Fishing spot" : "Fishing spot";
        String action = method.equals("fly") ? "Lure" : "Net";

        Query.npcs()
                .nameEquals(spot)
                .findBestInteractable()
                .ifPresent(i -> {
                    i.adjustCameraTo();  // Ensure it's visible on screen
                    i.click(action);     // Then click to fish
                });
    }
    private static boolean checkIfBankHasItem(String itemName){
        Bank.ensureOpen();
        if (Bank.contains(itemName)) {
            return true;
        }
        else {return false;}
    }
}

