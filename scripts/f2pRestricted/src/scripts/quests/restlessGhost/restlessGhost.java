package scripts.quests.restlessGhost;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.query.Query;

import java.util.Random;

public class restlessGhost {
    static Random random = new Random();
    static int mean = 3000;
    public void execute() {
        
        while (!Bank.isNearby()){
            GlobalWalking.walkToBank();
            waitForAnimation(2000);
            Log.info("Walking to bank");
        }
        Bank.ensureOpen();
        waitRandom(900,1400);
        Bank.depositEquipment();
        waitRandom(900,1400);
        Bank.depositInventory();
        waitRandom(900,1400);
        Bank.close();
        
        while(!constants.Father_AereckArea.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Father_AereckArea.getRandomTile());
            Log.info("walking to father Aereck");
            waitForAnimation(800);
        }
        if(talkToNPC("Father Aereck") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle(constants.Father_Aereck_Dialogue)){
                Log.info("handling the convo");
                Waiting.wait(100);
            }
            Log.info("outside of if fpr aereck convo");
        }
        
         
        while (!constants.Father_UrhneyArea.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Father_Urhney_Door_Area_Enter);
            Log.info("walking to father urhney");
            waitForAnimation(1000);
        }
        
         
        constants.Father_Urhney_Door_Area_Enter.click();
        waitForAnimation(800);
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(i->i.click("Open"));
        waitRandom(900,1400);
        while (!Inventory.contains("Ghostspeak amulet")){
            if(talkToNPC("Father Urhney") && Waiting.waitUntil(ChatScreen::isOpen)){
                Waiting.waitNormal(600, 90);
                while (ChatScreen.isOpen()){
                    ChatScreen.handle(constants.Father_Urhney_Dialogue);
                    Log.info("talking with father urhney");
                }
                Waiting.wait(3000);
            }
        }
        
         
         
         
        Query.inventory().nameEquals("Ghostspeak amulet").forEach(click ->click.click("Wear"));
        waitRandom(900,1400);
        while (!constants.Lumbridge_Graveyard.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Lumbridge_Graveyard.getRandomTile());
            waitForAnimation(2000);
        }
        constants.Lumbridge_Graveyard_Door_Entry.click();
        waitRandom(900,1400);
        Query.gameObjects().nameEquals("Door").maxDistance(2).findBestInteractable().ifPresent(i->i.click("Open"));
        waitRandom(900,1400);
        while (!Query.npcs().nameEquals("Restless ghost").isAny()){
            Query.gameObjects().nameEquals("Coffin").findBestInteractable().ifPresent(i->i.click("Open"));
            waitForAnimation(800);
        }
        if(talkToNPC("Restless ghost") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.isOpen()){
                ChatScreen.handle(constants.Restless_Ghost_Dialogue);
                Waiting.waitNormal(700,10);
            }
        } 
        
        while (constants.Altar.distance() > 2){
            GlobalWalking.walkTo(constants.Altar);
            waitForAnimation(800);
            Log.info("Walking to altar");
        }
        while (!Inventory.contains("Ghost's skull")){
            Query.gameObjects().nameEquals("Altar").findBestInteractable().ifPresent(i->i.click("Search"));
            Log.info("Searching altar");
            waitRandom(2000,4000);
        }
        
        while (!constants.Lumbridge_Graveyard.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Lumbridge_Graveyard.getRandomTile());
            waitForAnimation(800);
        }
        constants.Lumbridge_Graveyard_Door_Entry.click();
        waitForAnimation(800);
        Query.gameObjects().nameEquals("Door").maxDistance(2).findBestInteractable().ifPresent(i->i.click("Open"));
        waitRandom(900,1400);
        
        
         
        Query.gameObjects().nameEquals("Coffin").findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation(700);
        while (!inCutScene()){
            Query.inventory().nameEquals("Ghost's skull").forEach(i->i.click("Use"));
            Query.gameObjects().nameEquals("Coffin").findBestInteractable().ifPresent(i->i.click("Use"));
            waitForAnimation(700);
            
        }
        
        if (inCutScene()){{
            while (inCutScene()){
                Waiting.waitNormal(600, 90);
            }
        }}
        Keyboard.pressEscape();
        mean = random.nextInt(3000 - 2000 + 1) + 5000;
        Waiting.wait(mean);
        while (!Bank.isNearby()){
            GlobalWalking.walkToBank();
            waitForAnimation(800);
        }
        Bank.ensureOpen();
        waitRandom(900,1400);
        Bank.depositInventory();
        waitRandom(900,1400);
        Bank.depositEquipment();
        waitRandom(900,1400);
        Bank.close();
    }
    private static boolean talkToNPC(String name){
        return Query.npcs()
                .nameEquals(name)
                .findBestInteractable()
                .map(cook -> cook.interact("Talk-to"))
                .orElse(false);
    }
    private static void waitForAnimation(int delay){
        Waiting.wait(delay);
        while (MyPlayer.isAnimating()){
            Waiting.wait(2000);
        }
    }
    private static void waitRandom(int small, int big){
        mean = random.nextInt((big - small) + 1) + small;
        Waiting.wait(mean);
    }
    public static boolean inCutScene() {
        return GameState.getVarbit(12139) == 1;
    }
}
