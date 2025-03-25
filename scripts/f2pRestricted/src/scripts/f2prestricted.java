package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.types.*;
import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.Waiting;

import scripts.quests.restlessGhost.restlessGhost;
import scripts.quests.doricsQuest.doricsQuest;
import scripts.quests.sheepShearer.sheepShearer;
import scripts.quests.ernestTheChicken.ernestTheChicken;
import scripts.quests.romeoAndJuliet.romeoAndJuliet;
import scripts.quests.cooksAssistentQuest.cooksAssistant;

import scripts.tasks.miningSmithing;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@TribotScriptManifest(
        author = "Simon Maes",
        category = "f2pRestricted",
        name = "f2pRestricted",
        description = "does variety of tasks to unrestrict the account"
)
public class f2prestricted implements TribotScript {
    List<Runnable> methods = new ArrayList<>();
    Area fishingArea;
    Area miningArea;
    String preferedPickaxe;
    String preferedOre;
    static Random random = new Random();
    static Random randomTaskPicker = new Random();
    static int mean = 3000;
    restlessGhost ghost = new restlessGhost();
    doricsQuest doricsQuest = new doricsQuest();
    sheepShearer sheepShearer = new sheepShearer();
    restlessGhost restlessGhost = new restlessGhost();
    ernestTheChicken ernestTheChicken = new ernestTheChicken();
    romeoAndJuliet romeoAndJuliet = new romeoAndJuliet();
    cooksAssistant cooksAssistant = new cooksAssistant();
    miningSmithing miningSmithing = new miningSmithing();

    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
        config.setBreakHandlerEnabled(false);
        config.setRandomsAndLoginHandlerEnabled(false);
    }
    @Override
    public void execute(@NotNull String s) {
        Camera.setZoomPercent(1);
        Waiting.wait(700);
        while (true) {
            miningSmithing.execute();
            /*
            if (meetsRequirements()){
                break;
            }
            while (!Bank.isNearby()){
                GlobalWalking.walkToBank();
                Log.info("Walking to bank.");
                waitForAnimation(2000);
            }
            Bank.ensureOpen();
            Waiting.waitNormal(1400,10);
            Bank.depositInventory();
            Waiting.waitNormal(1400,10);
            Bank.depositEquipment();
            Waiting.waitNormal(1400,10);
            Bank.close();
            getMoneyTask();
            buyQuestItems();
            
             
            if (Quest.DORICS_QUEST.getState() != Quest.State.COMPLETE){
                doricsQuest.execute();
            }
            if (Quest.SHEEP_SHEARER.getState() != Quest.State.COMPLETE){
                sheepShearer.execute();
            }
             
            if (Quest.THE_RESTLESS_GHOST.getState() != Quest.State.COMPLETE){
                restlessGhost.execute();
            }
            
             
            if (Quest.ROMEO__JULIET.getState() != Quest.State.COMPLETE){
                romeoAndJuliet.execute();
            }
            
             
            if (Quest.ERNEST_THE_CHICKEN.getState() != Quest.State.COMPLETE){
                ernestTheChicken.execute();
            }
            if (Quest.COOKS_ASSISTANT.getState() != Quest.State.COMPLETE){
                cooksAssistant.execute();
            }
            break;
            
             */
        }
    }
    public static void getMoneyTask(){
        while (true) {
            while (!Bank.isNearby()){
                GlobalWalking.walkToBank();
                waitForAnimation(2000);
            }
            Bank.ensureOpen();
            Waiting.waitNormal(1400,10);
            Bank.depositInventory();
            Waiting.waitNormal(1400,10);
            Bank.depositEquipment();
            Waiting.waitNormal(1400,10);
            if (Bank.getCount("Logs") > 300){
                Bank.close();
                Waiting.waitNormal(1400,10);
                sellItemOnGe("Logs",-5);
                break;
            }
            Bank.withdraw("Bronze axe",1);
            Waiting.waitNormal(1400,10);
            Bank.close();
            Waiting.waitNormal(1400,10);
            Query.inventory().nameEquals("Bronze axe").stream().forEach(axe ->axe.click("Wield"));
            Waiting.waitNormal(1400,10);
            while (!constants.trees.containsMyPlayer()){
                GlobalWalking.walkTo(constants.trees.getRandomTile());
                waitForAnimation(2000);
            }
            while (!Inventory.isFull()){
                while (!constants.trees.containsMyPlayer()){
                    GlobalWalking.walkTo(constants.trees.getRandomTile());
                    waitForAnimation(2000);
                }
                Query.gameObjects().nameEquals("Tree").findBestInteractable().map(chop -> chop.click("Chop down"));
                waitForAnimation(2000);
            }
        }
    }
    
    public static void fishingTask(){
        Area fishingArea;
        String task = pickFishingTask();
        int fishCount = 0;
        int currentFishCount = 0;
        Map<String, Integer> itemsToBuy = new HashMap<>();
        GlobalWalking.walkToBank();
        Bank.ensureOpen();
        Bank.depositInventory();
        Bank.depositInventory();
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
            fishingArea = constants.flyFishingSpot;
            
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
            fishingArea = constants.smallFishingNetSpot;
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
                    waitForAnimation(2000);
                }
                else{
                    fish(task);
                    waitForAnimation(2000);
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
                    if (currentFishCount - fishCount >500){
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

    public static boolean meetsRequirements() {
        String chatMessage = "";
        Area hansArea = Area.fromRadius(new WorldTile(3221, 3219, 0), 1);

        // If the player is not in Hans' area, walk there.
        if (!hansArea.containsMyPlayer()) {
            GlobalWalking.walkTo(hansArea.getRandomTile());
        } 
            while (chatMessage.isEmpty()){
                if (talkToHans() && Waiting.waitUntil(ChatScreen::isOpen)) {
                    Waiting.waitNormal(600, 90);
                    Optional<String> optionalMessage = ChatScreen.getMessage();
                    if (optionalMessage.isPresent()) {
                        chatMessage = optionalMessage.get();
                    }
                }
            }

        // If no chat message is found, we don't have valid time data.
        if (chatMessage.isEmpty()) {
            return false;
        }

        // Convert the chat message into total hours.
        int totalHours = convertToTotalHours(chatMessage);
        int questPoints = MyPlayer.getQuestPoints();
        int totalLevels = MyPlayer.getTotalLevel();

        // Return true if all conditions are met.
        return totalHours >= 20 && questPoints >= 10 && totalLevels >= 100;
    }

    // Converts the chat message into total hours.
    public static int convertToTotalHours(String chatMessage) {
        String cleanedMessage = chatMessage.replaceAll("<br>", " ").trim();
        int days = 0, hours = 0;

        // Regex to match days and hours.
        Pattern dayPattern = Pattern.compile("(\\d+) days?");
        Pattern hourPattern = Pattern.compile("(\\d+) hours?");

        Matcher dayMatcher = dayPattern.matcher(cleanedMessage);
        Matcher hourMatcher = hourPattern.matcher(cleanedMessage);

        if (dayMatcher.find()) {
            days = Integer.parseInt(dayMatcher.group(1));
        }
        if (hourMatcher.find()) {
            hours = Integer.parseInt(hourMatcher.group(1));
        }

        // Convert days to hours and add hours.
        return (days * 24) + hours;
    }

    // Interacts with Hans to check the account age.
    private static boolean talkToHans() {
        return Query.npcs()
                .nameEquals("Hans")
                .findBestInteractable()
                .map(hans -> hans.interact("Age"))
                .orElse(false);
    }
    private static String pickAxe(){
        int level = Skill.WOODCUTTING.getActualLevel();
        String pickaxe = "Iron axe";
        if (level >= 1 && level <= 5){
            pickaxe = "Iron axe";
        } else if(level >= 6 && level <= 10){
            pickaxe = "Steel axe";
        } else if(level >= 11 && level <= 20){
            pickaxe = "Black axe";
        } else if(level >= 21 && level <= 30){
            pickaxe = "Mithril axe";
        } else if(level >= 31 && level <= 40){
            pickaxe = "Adamant axe";
        } else if(level >= 41){
            pickaxe = "Rune axe";
        }
        return pickaxe;
    }
    private static boolean smeltItem() {
        if(clickFurnace()
                && Waiting.waitUntil(MakeScreen::isOpen)
                && Waiting.waitUntil(()->MakeScreen.make("Iron bar"))
                && Waiting.waitUntil(MyPlayer::isAnimating)) { return true; }
        else { return false; }
    }
    private static boolean clickFurnace() {
        return Query.gameObjects().isReachable()
                .nameEquals("Furnace")
                .findBestInteractable()
                .map(i->i.click("Smelt"))
                .orElse(false);
    }
    private static void takeBreak() {
        Random random = new Random();
        // Generate random time between small and big (in milliseconds)
        int minTime = 20 * 60 * 1000; // 20 minutes in milliseconds (1,200,000)
        int maxTime = 30 * 60 * 1000; // 30 minutes in milliseconds (1,800,000)
        int randomBreakTime = minTime + (int)(Math.random() * (maxTime - minTime));

        Login.logout();
        Waiting.wait(randomBreakTime);
        Login.login();
    }
    
    private static boolean talkToNPC(String name){
        return Query.npcs()//The cook is an npc, so let's query those
                .nameEquals(name) //We only want npcs named cook
                .findBestInteractable() // Let's make tribot decide which egg to get
                .map(cook -> cook.interact("Talk-to")) //if there is a cook let's try to Talk-to him
                .orElse(false); // if there is not a cook, or Talking to him fails, let's return false
    }
    private static void buyQuestItems(){
        GlobalWalking.walkTo(constants.GrandExchangeArea.getRandomTile());
        Bank.ensureOpen();
        Bank.withdrawAll("Coins");
        Bank.close();
        GrandExchange.open();
        buyItemOnGE("Ball of wool",20,5);
        buyItemOnGE("Clay",6,5);
        buyItemOnGE("Copper ore",4,5);
        buyItemOnGE("Iron ore",2,5);
        buyItemOnGE("Egg",1,5);
        buyItemOnGE("Bucket of milk",1,5);
        buyItemOnGE("Pot of flour",1,10);
        buyItemOnGE("Jug of wine",10,5);
        buyItemOnGE("Cadava berries",1,5);
        GrandExchange.close();
        Bank.ensureOpen();
        Bank.depositInventory();
        Bank.depositEquipment();
        Bank.close();
    }
    private static void buyItemOnGE(String itemName, int amount, int priceAdjustment){
        org.tribot.script.sdk.GrandExchange.CreateOfferConfig config = org.tribot.script.sdk.GrandExchange.CreateOfferConfig.builder()
                .type(GrandExchangeOffer.Type.BUY)
                .slot(GrandExchangeOffer.Slot.TWO)// This is a buy offer
                .itemName(itemName)  // Item name
                .quantity(1)  // Quantity to buy
                .priceAdjustment(priceAdjustment)
                .quantity(amount)
                .interruptCondition(() -> false)  // No interrupt condition, we want this to run
                .build();
        while (!constants.GrandExchangeArea.containsMyPlayer()) {
            GlobalWalking.walkTo(constants.GrandExchangeArea.getRandomTile());
            Waiting.wait(2000);
        }
        Bank.ensureOpen();
        Waiting.waitNormal(1400,50);
        Log.info("withdrawing all coins");
        Bank.withdrawAll("Coins");
        Waiting.waitNormal(1400,50);
        Bank.close();
        Waiting.waitNormal(1400,50);
        GrandExchange.open();
        Waiting.waitNormal(1400,50);
        Waiting.waitUntil(GrandExchange::isOpen);
        GrandExchange.placeOffer(config);
        Waiting.wait(2000);
        GrandExchange.collectAll();
        Waiting.waitNormal(1400,50);
        GrandExchange.close();
        Waiting.waitNormal(1400,50);
        Bank.ensureOpen();
        Waiting.waitNormal(1400,50);
        Bank.depositInventory();
        Waiting.waitNormal(1400,50);
        Bank.close();
    }
    private static void sellItemOnGe(String itemName, int priceAdjustment){
        while (!constants.GrandExchangeArea.containsMyPlayer()){
            GlobalWalking.walkTo(constants.GrandExchangeArea.getRandomTile());
            Waiting.wait(2000);
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
        Waiting.waitNormal(1400,50);
        BankSettings.setNoteEnabled(true);
        Waiting.wait(1400);
        while (!Inventory.contains(itemName)){
            Bank.withdrawAll(itemName);
            Waiting.wait(1400);
        }
        BankSettings.setNoteEnabled(false);
        Waiting.waitNormal(1400,50);
        Bank.close();
        Waiting.waitNormal(1400,50);
        GrandExchange.open();
        Waiting.waitNormal(1400,50);
        GrandExchange.placeOffer(config);
        Waiting.wait(2000);
        GrandExchange.collectAll();
        Waiting.waitNormal(1400,50);
        GrandExchange.close();
        Waiting.waitNormal(1400,50);
        Bank.ensureOpen();
        Waiting.waitNormal(1400,50);
        Bank.depositInventory();
    }
    private static boolean checkIfBankHasItem(String itemName){
        Bank.ensureOpen();
        if (Bank.contains(itemName)) {
            return true;
        } 
        else {return false;}
    }

    private static void waitForAnimation(int delay){
        Waiting.wait(delay);
        while (MyPlayer.isAnimating()){
            Waiting.wait(2000);
        }
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
    private static void waitRandom(int small, int big){
        mean = random.nextInt((big - small) + 1) + small;
        Waiting.wait(mean);
    }

}