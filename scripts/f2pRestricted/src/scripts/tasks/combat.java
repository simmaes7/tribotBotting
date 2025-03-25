package scripts.tasks;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.GrandExchangeOffer;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class combat {
    public static final Area GrandExchangeArea = Area.fromRadius(new WorldTile(3164, 3489, 0),6);
    static int mean = 3000;
    static Random random = new Random();
    static String preferredWeapon;
    static String preferredArmour;
    
    public void execute(){
        while (true) {
            if (!Bank.isNearby()){
                GlobalWalking.walkToBank();
                waitRandom(900,1400);
            }
            preferredWeapon = pickWeapon();
            preferredArmour = pickArmour();
            Bank.ensureOpen();
            Map<String, Integer> itemsToBuy = new HashMap<>();
            if (!Bank.contains(preferredWeapon) && Equipment.contains(preferredArmour)){
               itemsToBuy.put(preferredWeapon, 1);
            }
            if (!Bank.contains("Amulet of strength") && Equipment.contains("Amulet of strength")){
                itemsToBuy.put("Amulet of strength", 1);
            }
            if (!Bank.contains(preferredArmour + " full helm") && Equipment.contains(preferredArmour + " full helm")){
                itemsToBuy.put(preferredArmour + " full helm", 1);
            }
            if (!Bank.contains(preferredArmour + " kiteshield") && Equipment.contains(preferredArmour + " kiteshield")){
                itemsToBuy.put(preferredArmour + " kiteshield", 1);
            }
            if (!Bank.contains(preferredArmour + " platebody") && Equipment.contains(preferredArmour + " platebody")){
                itemsToBuy.put(preferredArmour + " platebody", 1);
            }
            if (!Bank.contains(preferredArmour + " platelegs") && Equipment.contains(preferredArmour + " platelegs")){
                itemsToBuy.put(preferredArmour + " platelegs", 1);
            }
            Bank.close();
            if (!itemsToBuy.isEmpty()){
                for (Map.Entry<String, Integer> entry : itemsToBuy.entrySet()) {
                    buyItemOnGE(entry.getKey(), entry.getValue(),5);
                }
            }
            
        }
    }
    private static String pickWeapon(){
        int level = Skill.ATTACK.getActualLevel();
        
        if (level <=4){
            return "Iron scimitar";
        } else if (level >5 && level <=9){
            return "Steel scimitar";
        } else if (level >9 && level <=19){
            return "Black scimitar";
        } else if (level >19 &&  level <=29){
            return "Mithril scimitar";
        } else if (level >29 && level <=39){
            return "Adamant scimitar";
        } else{
            return "Rune scimitar";
        }
    }
    private static String pickArmour(){
        int level = Skill.ATTACK.getActualLevel();
        if (level <=4){
            return "Iron";
        } else if (level >5 && level <=9){
            return "Steel";
        } else if (level >9 && level <=19){
            return "Black";
        } else if (level >19 &&  level <=29){
            return "Mithril";
        } else if (level >29 && level <=39){
            return "Adamant";
        } else{
            return "Rune";
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
}
