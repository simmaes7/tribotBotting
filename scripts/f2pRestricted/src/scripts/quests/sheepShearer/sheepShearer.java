package scripts.quests.sheepShearer;


import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.security.Key;
import java.util.Objects;
import java.util.Random;

public class sheepShearer {
    static int mean = 3000;
    static Random random = new Random();
    public void execute(){
        while (!Bank.isNearby()){
            GlobalWalking.walkToBank();
            Waiting.wait(2000);
        }
        GlobalWalking.walkToBank();
        waitRandom(700,1400);
        Bank.ensureOpen();
        waitRandom(700,1400);
        Bank.depositEquipment();
        waitRandom(700,1400);
        Bank.depositInventory();
        waitRandom(700,1400);
        BankSettings.setNoteEnabled(false);
        waitRandom(700,1400);
        Bank.withdraw("Ball of wool",20);
        waitRandom(700,1400);
        Bank.close();
        
         
        while (!constants.Fred_The_Farmer_First_Gate.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Fred_The_Farmer_First_Gate.getRandomTile());
            Waiting.wait(2000);
        }
        waitForAnimation(2000);
        constants.Gate.click();
        waitForAnimation(2000);
        Query.gameObjects().nameEquals("Gate").maxDistance(2).findBestInteractable().ifPresent(i->i.click("Open"));
        waitForAnimation(2000);
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(i->{
            i.adjustCameraTo();
            i.click("Open");
        });
        waitForAnimation(2000);
        if(talkToNPC("Fred the Farmer") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle(constants.Fred_The_Farmer_Dialogue)){
                Waiting.wait(100);
                if (!ChatScreen.isOpen()){
                    break;
                }
            }
        }
        waitRandom(900,1400);
        Keyboard.pressEscape();
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
}
