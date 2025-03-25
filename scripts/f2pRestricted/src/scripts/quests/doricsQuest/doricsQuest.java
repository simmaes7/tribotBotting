package scripts.quests.doricsQuest;

import org.checkerframework.checker.units.qual.K;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.security.Key;
import java.util.Random;

public class doricsQuest {
    static int mean = 3000;
    static Random random = new Random();
    public void execute() {
        while (!Bank.isNearby()){
            GlobalWalking.walkToBank();
            waitForAnimation(2000);
        }
        
        Bank.ensureOpen();
        waitRandom(900,1400);
        BankSettings.setNoteEnabled(false);
        waitRandom(900,1400);
        Bank.withdraw("Copper ore",4);
        waitRandom(900,1400);
        Bank.withdraw("Iron ore",2);
        waitRandom(900,1400);
        Bank.withdraw("Clay",6);
        waitRandom(900,1400);
        Bank.close();
        while (!constants.Dorics_House_Area.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Dorics_House_Area.getRandomTile());
            Waiting.wait(2000);
        }
        Waiting.waitUntil(MyPlayer::isAnimating);
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(i->i.click("Open"));
        Waiting.wait(2000);
        if(talkToNPC("Doric") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle(constants.Doric_Dialogue)){
                Waiting.wait(100);
                if (!ChatScreen.isOpen()){
                    break;
                }
            }
        }
        waitRandom(900,1400);
        Keyboard.pressEscape();
        if (ChatScreen.isOpen()){
            ChatScreen.handle();
        }
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
