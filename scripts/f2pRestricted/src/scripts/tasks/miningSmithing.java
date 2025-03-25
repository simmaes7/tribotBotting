package scripts.tasks;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.constants;

import java.util.Optional;
import java.util.Random;

public class miningSmithing {

    //for the random
    static int mean = 3000;
    static Random random = new Random();
    
    //areas
    public static final Area ironOreAreaBelow60 = Area.fromRadius(new WorldTile(3287, 3369, 0),2);
    public static final WorldTile ironTileBelow60 = new WorldTile(3175, 3366, 0);
    public static final Area ironOreAreaAbove60 = Area.fromRectangle(new WorldTile(3031, 9739,0),new WorldTile(3035, 9735,0));
    public static final Area tinOreArea = Area.fromRadius(new WorldTile(3284, 3363, 0),2);
    public static final Area GrandExchangeArea = Area.fromRadius(new WorldTile(3164, 3489, 0),6);
    public static final Area edgevil = Area.fromRadius(new WorldTile(3101, 3496, 0),2);
    public static Area miningArea;
    
    //variables
    String preferedPickaxe;
    String preferedOre;
    public void execute(){
        while (true){
            while (true){
                preferedOre = pickOre();
                preferedPickaxe = pickPickaxe();
                while (!Bank.isNearby()){
                    Options.setRunEnabled(true);
                    GlobalWalking.walkToBank();
                }
                Bank.ensureOpen();
                waitRandom(650,800);
                Bank.depositInventory();
                waitRandom(650,800);
                Bank.depositEquipment();
                waitRandom(650,800);
                if (Bank.getCount("Iron ore")>=280){
                    break;
                }
                if (!Bank.contains(preferedPickaxe)){
                    Bank.close();
                    waitRandom(650,800);
                    buyItemOnGE(preferedPickaxe,1,10);
                    waitRandom(650,800);
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
                        waitRandom(650,800);
                    }
                    //checkIfHopWorld();
                    mineOre(preferedOre);
                    waitForAnimation();
                    waitStopAnimation();
                }
            }
            if (getSmithingLevel()<15){
                Log.info("level is smaller then 15");
                Log.info("opening bank now");
                Bank.ensureOpen();
                waitRandom(600,900);
                Bank.depositInventory();
                waitRandom(600,900);
                Bank.depositEquipment();
                waitRandom(600,900);
                if (Bank.getCount("Bronze bar")< 193){
                    int amount = 193 - Bank.getCount("Bronze bar");
                    Bank.close();
                    waitRandom(600,900);
                    buyItemOnGE("Bronze bar",amount,3);
                }
                Bank.close();
                while (true){
                    Waiting.waitNormal(1000,20);
                    if (!Inventory.contains("Hammer")){
                        if (!Bank.isNearby()){
                            GlobalWalking.walkToBank();
                            waitRandom(600,900);
                        }
                        Bank.ensureOpen();
                        waitRandom(600,900);
                        if (!Bank.contains("Hammer")){
                            waitRandom(600,900);
                            Bank.close();
                            waitRandom(600,900);
                            buyItemOnGE("Hammer",1,5);
                            waitRandom(600,900);
                            Bank.ensureOpen();
                        }
                        waitRandom(600,900);
                        Bank.withdraw("Hammer",1);
                        waitRandom(600,900);
                    }
                    if (!Bank.contains("Bronze bar")){
                        Bank.close();
                        sellItemOnGe("Bronze dagger",-3);
                        break;
                    }
                    if (!Inventory.contains("Bronze bar")){
                        Bank.ensureOpen();
                        waitRandom(600,900);
                        Bank.withdrawAll("Bronze bar");
                        waitRandom(600,900);
                        Bank.close();
                    }
                    while (!constants.Anvil.containsMyPlayer()){
                        GlobalWalking.walkTo(constants.Anvil.getRandomTile());
                        waitRandom(600,900);
                        
                    }

                    Query.gameObjects().nameEquals("Anvil").findBestInteractable().ifPresent(i->{
                        i.adjustCameraTo();
                        i.click("Smith");
                    });
                    while (!isSmithingWindowOpen()){
                        Waiting.wait(50);
                    }

                    while (Inventory.contains("Bronze bar")){
                        if (ChatScreen.isOpen()){
                            ChatScreen.handle();
                        } else {
                            while (!isSmithingWindowOpen() && Inventory.contains("Bronze bar")){
                                Query.gameObjects().nameEquals("Anvil").findBestInteractable().ifPresent(i->{
                                    i.adjustCameraTo();
                                    i.click("Smith");
                                });
                                Waiting.wait(1400);
                            }
                            clickSmithingItem("Bronze dagger");
                            waitForAnimation();
                        }
                    }
                    GlobalWalking.walkToBank();
                    waitRandom(600,900);
                    Bank.ensureOpen();
                    waitRandom(600,900);
                    Bank.depositAll("Bronze dagger");
                }
            }
            if (Bank.getCount("Ring of forging")<2){
                Bank.close();
                buyItemOnGE("Ring of forging",2,5);
            }
            Bank.close();
            while (!edgevil.containsMyPlayer())
            {
                GlobalWalking.walkTo(edgevil.getRandomTile());
            }
            while (true){
                Options.setRunEnabled(true);
                Bank.ensureOpen();
                waitRandom(650,800);
                Bank.depositInventory();
                waitRandom(650,800);
                Bank.depositEquipment();
                waitRandom(650,800);
                if (!Bank.contains("Iron ore")){
                    Bank.close();
                    Waiting.waitNormal(900,50);
                    sellItemOnGe("Iron bar",-2);
                    break;
                }
                Bank.withdraw("Ring of forging",1);
                waitRandom(650,800);
                Bank.close();
                waitRandom(650,800);
                Query.inventory().nameEquals("Ring of forging").stream().forEach(i->i.click("Wear"));
                waitRandom(650,800);
                Bank.ensureOpen();
                waitRandom(650,800);
                Bank.withdraw("Iron ore",28);
                waitRandom(650,800);
                Bank.close();
                waitRandom(650,800);
                Query.gameObjects().nameEquals("Furnace").findBestInteractable().map(GameObject::adjustCameraTo);
                waitRandom(650,800);
                while (Inventory.contains("Iron ore")){
                    smeltItem();
                }
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
                .map(i-> i.click("Smelt"))
                .orElse(false);
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
    private static int getSmithingLevel(){
        return Skill.SMITHING.getActualLevel();
    }
    private static boolean clickSmithingItem(String itemToMake){
        return Query.widgets().inIndexPath(312).nameContains(itemToMake)
                .findFirst().filter(Widget::click)
                .map(w -> Waiting.waitUntilAnimating(2000))
                .orElse(false);
    }
    private static boolean isSmithingWindowOpen(){
        return Query.widgets().inIndexPath(312).isAny();
    }
    private static void checkIfHopWorld(){
        if (Query.players().tileEquals(ironTileBelow60).isAny()){
            hopWorld();
        }
    }
    private static void hopWorld(){
        Optional<World> targetWorld = Query.worlds().isNotCurrentWorld()
                .isNotDangerous()
                .isNonMembers()
                .isRequirementsMet()
                .isMainGame()
                .findRandom();

        if (targetWorld.isEmpty()) {
            Log.info("No suitable world found. Retrying...");
            Waiting.waitNormal(1000, 200); // Wait a bit before retrying
        }
        
        int newWorldId = targetWorld.get().getWorldNumber();
        WorldHopper.hop(newWorldId);
    }
}
