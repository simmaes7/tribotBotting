package scripts.quests.ernestTheChicken;


import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.GroundItemQuery;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.GroundItem;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.security.Key;
import java.util.Random;

public class ernestTheChicken {
    static int mean = 3000;
    static Random random = new Random();
    public void execute(){
        
        GlobalWalking.walkToBank();
        Bank.ensureOpen();
        Bank.depositInventory();
        Bank.depositEquipment();
        Bank.withdraw("Jug of wine",10);
        Bank.close();
        
         
        Log.info("Walking to veronica now");
        while (!constants.Veronica_Area.containsMyPlayer()){
            Log.info("Entered the if loop");
            GlobalWalking.walkTo(constants.Veronica_Area.getRandomTile());
            waitRandom(700,1400);
            Log.info("if i entered if, i should have started walking");
        }
        if(talkToNPC("Veronica") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle(constants.Veronica_Dialogue)){
                Waiting.wait(100);
                Log.info("handling chatscreen now");
                if (!ChatScreen.isOpen()){
                    break;
                }
            }
        }
        
        
         
        
        Log.info("going to walk to the door");
        while (constants.Draynor_Manor_Large_Door_Tile.distance() >2){
            GlobalWalking.walkTo(constants.Draynor_Manor_Large_Door_Tile);
            if (MyPlayer.isAnimating()){
                Waiting.waitNormal(700,10);
            }
        }
        Log.info("arrived at the door");  
        if (Query.gameObjects().nameEquals("Large door").isVisible().findFirst().isEmpty()){
            Query.gameObjects().nameEquals("Large door").findBestInteractable().ifPresent(GameObject::adjustCameraTo);
        }
        Query.gameObjects().nameEquals("Large door").findBestInteractable().ifPresent(i->i.click("Open"));
        Log.info("Tried to open the door");
        Waiting.wait(3524);
        Log.info("Waiting for animation");
        while (constants.Poison.distance() > 2){
            GlobalWalking.walkTo(constants.Poison);
            waitForAnimation();
        }
        waitForAnimation();
        openDoor();
        waitForAnimation();
        while (!Inventory.contains("Poison")){
            while (!Query.groundItems().nameEquals("Poison").isAny()){
                Waiting.wait(60000);
            }
            Query.groundItems().nameEquals("Poison").findBestInteractable().ifPresent(i->i.click("Take"));
        }
        
         
        
        GlobalWalking.walkTo(constants.Downstairs_Staircase);
        waitForAnimation();
        Query.gameObjects().nameEquals("Staircase").findBestInteractable().ifPresent(i->i.click("Climb-up"));
        waitForAnimation();
        
         
        
        GlobalWalking.walkTo(constants.Fish_Food);
        waitForAnimation();
        while (!Inventory.contains("Fish food")){
            while (!Query.groundItems().nameEquals("Fish food").isAny()){
                Waiting.wait(60000);
            }
            Query.groundItems().nameEquals("Fish food").findBestInteractable().ifPresent(i->i.click("Take"));
        }

        waitForAnimation();
        Query.inventory().nameEquals("Fish food").forEach(i->i.click("Use"));
        Query.inventory().nameEquals("Poison").forEach(i->i.click("Use"));

        waitForAnimation();
        GlobalWalking.walkTo(constants.Draynor_Manor_Bookcase_Entry_Tile);
         
         
         
        
        Query.gameObjects().nameEquals("Bookcase").findBestInteractable().ifPresent(i->i.click("Search")); 
        Waiting.wait(7000);
        Query.gameObjects().nameEquals("Ladder").findBestInteractable().ifPresent(i->i.click("Climb-down"));
        waitForAnimation();
        
         
         
         
        Log.info("version 2");
        
         
        solvePuzzle();
        
         
        if (!Query.gameObjects().nameEquals("Lever").isAny()){
            Query.gameObjects().nameEquals("Lever").findBestInteractable().map(GameObject::adjustCameraTo);
        }
        Query.gameObjects().nameEquals("Lever").findBestInteractable().ifPresent(i->i.click("Pull"));
        
        pullLever("Lever");
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Spade);
        waitForAnimation();
        waitStopAnimation();
        if (!constants.Spade.isVisible()){
            constants.Spade.adjustCameraTo();
        }
        constants.Spade.click();
        waitForAnimation();
        waitStopAnimation();
        while (!Inventory.contains("Spade")){
            while (!Query.groundItems().nameEquals("Spade").isAny()){
                Waiting.wait(70000);
            }
            Query.groundItems().nameEquals("Spade").findBestInteractable().ifPresent(i->i.click("Take"));
        }
        waitForAnimation();
        waitStopAnimation();
        
        GlobalWalking.walkTo(constants.Compost_Heap);
        waitForAnimation();
        waitStopAnimation();
        while(!Inventory.contains("Key")){
            Query.gameObjects().nameEquals("Compost heap").findBestInteractable().ifPresent(i->i.click("Search"));
            waitForAnimation();
            waitStopAnimation();
        }
        GlobalWalking.walkTo(constants.Fountain);
        waitForAnimation();
        waitStopAnimation();
         
        Query.inventory().nameEquals("Poisoned fish food").forEach(i->i.click("Use"));
        Query.gameObjects().nameEquals("Fountain").findBestInteractable().ifPresent(i->i.click("Use"));
        waitForAnimation();
        waitStopAnimation();
        while (!Inventory.contains("Pressure gauge")){
            Query.gameObjects().nameEquals("Fountain").findBestInteractable().ifPresent(i->i.click("Search"));
            waitForAnimation();
            waitStopAnimation();
            ChatScreen.handle();
            waitRandom(700,1400);
        }
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Locked_Door);
        waitForAnimation();
        waitStopAnimation();
        constants.Locked_Door.click();
        waitForAnimation();
        waitStopAnimation();
        openDoor();
        waitForAnimation();
        waitStopAnimation();
        while (!Inventory.contains("Rubber tube")){
            if (MyPlayer.getCurrentHealth() < MyPlayer.getMaxHealth()){
                Query.inventory().nameEquals("Jug of wine").findFirst().ifPresent(i->i.click("Drink"));
            }
            while (!Query.groundItems().nameEquals("Rubber tube").isAny()){
                Waiting.wait(60000);
            }
            Query.groundItems().nameEquals("Rubber tube").findBestInteractable().ifPresent(i->i.click("Take"));
        }
        openDoor();
        waitForAnimation();
        waitStopAnimation();
        
        GlobalWalking.walkTo(constants.Professor_Oddenstein);
        waitForAnimation();
        waitStopAnimation();
        constants.Professor_Oddenstein.click();
        waitForAnimation();
        waitStopAnimation();
        openDoor();
        waitForAnimation();
        waitStopAnimation();
        if(talkToNPC("Professor Oddenstein") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle(constants.Professor_Oddenstein_Dialogue)){
                Waiting.wait(50);
            }
        }
        
         
        Waiting.wait(3456);
        if(talkToNPC("Professor Oddenstein") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle()){
                Waiting.wait(50);
            }
        }
        Waiting.wait(4823);
        ChatScreen.handle();
        Waiting.wait(2654);
        Keyboard.pressEscape();
        
         
        while (!Bank.isNearby()){
            GlobalWalking.walkToBank();
            waitForAnimation();
            waitStopAnimation();
        }
        waitForAnimation();
        waitStopAnimation();
        Bank.ensureOpen();
        waitRandom(900,1400);
        Bank.depositEquipment();
        waitRandom(900,1400);
        Bank.depositInventory();
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
    private static void openDoor(){
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
    }
    private static void solvePuzzle(){
        
        //lever B
        constants.Lever_B_Tile.click("Walk here");
        Log.info("tried to click the lever B");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever B");
        waitForAnimation();
        waitStopAnimation();
        //lever A
        GlobalWalking.walkTo(constants.Lever_A_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_A_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever A");
        waitForAnimation();
        waitStopAnimation();
        //lever D
        
        GlobalWalking.walkTo(constants.Enter_Door_1);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_1.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Lever_D_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_D_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever D");
        waitForAnimation();
        waitStopAnimation();
         
         
         
         
         
        //lever B
        GlobalWalking.walkTo(constants.Exit_Door_1);
        waitForAnimation();
        waitStopAnimation();
        constants.Exit_Door_1.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(2).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Lever_B_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_B_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever B");
        waitForAnimation();
        waitStopAnimation();
        //lever A
        GlobalWalking.walkTo(constants.Lever_A_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_A_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever A");
        waitForAnimation();
        waitStopAnimation();
        //lever E
        GlobalWalking.walkTo(constants.Enter_Door_3);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_3.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        
         
        GlobalWalking.walkTo(constants.Enter_Door_4);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_4.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Enter_Door_5);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_5.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
         
        GlobalWalking.walkTo(constants.Lever_E_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_E_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever E");
        waitForAnimation();
        waitStopAnimation();
        //lever F
        GlobalWalking.walkTo(constants.Lever_F_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_F_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever F");
        waitForAnimation();
        waitStopAnimation();
        
         
        //lever C
        
        GlobalWalking.walkTo(constants.Enter_Door_6);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_6.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Enter_Door_7);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_7.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        
         
        
        GlobalWalking.walkTo(constants.Lever_C_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_C_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever C");
        waitForAnimation();
        waitStopAnimation();
        
        //Lever E
        GlobalWalking.walkTo(constants.Exit_Door_7);
        waitForAnimation();
        waitStopAnimation();
        constants.Exit_Door_7.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Exit_Door_6);
        waitForAnimation();
        waitStopAnimation();
        constants.Exit_Door_6.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Lever_E_Tile);
        waitForAnimation();
        waitStopAnimation();
        constants.Lever_E_Tile.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        pullLever("Lever E");
        waitForAnimation();
        waitStopAnimation();
        //oil can
        GlobalWalking.walkTo(constants.Enter_Door_6);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_6.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Enter_Door_8);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_8.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Exit_Door_3);
        waitForAnimation();
        waitStopAnimation();
        constants.Exit_Door_3.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Enter_Door_9);
        waitForAnimation();
        waitStopAnimation();
        constants.Enter_Door_9.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Oil_can);
        waitForAnimation();
        waitStopAnimation();
        while (!Inventory.contains("Oil can")){
            while (!Query.groundItems().nameEquals("Oil can").isAny()){
                Waiting.wait(60000);
            }
            Query.groundItems().nameEquals("Oil can").findBestInteractable().ifPresent(i->i.click("Take"));
        }
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Exit_Door_9);
        waitForAnimation();
        waitStopAnimation();
        constants.Exit_Door_9.click("Walk here");
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Door").maxDistance(1).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation();
        waitStopAnimation();
        GlobalWalking.walkTo(constants.Exit_Puzzle);
        waitForAnimation();
        waitStopAnimation();
        Query.gameObjects().nameEquals("Ladder").findBestInteractable().ifPresent(i->i.click("Climb-up"));
        waitForAnimation();
        waitStopAnimation();
    }
    private static void pullLever(String lever){
        if (!Query.gameObjects().nameEquals(lever).isVisible().isAny()){
            Query.gameObjects().nameEquals(lever).findBestInteractable().map(GameObject::adjustCameraTo);
        }
        Query.gameObjects().nameEquals(lever).findBestInteractable().ifPresent(i->i.click("Pull"));
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
