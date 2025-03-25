package scripts.quests.cooksAssistentQuest;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.security.Key;
import java.util.Arrays;


public class cooksAssistant {
    public void execute() {
        if (!Bank.isNearby()){
            GlobalWalking.walkToBank();
            Waiting.waitNormal(700,10);
        }
        Bank.ensureOpen();
        Waiting.waitNormal(700,10);
        Bank.withdraw("Egg",1);
        Waiting.waitNormal(700,10);
        Bank.withdraw("Bucket of milk",1);
        Waiting.waitNormal(700,10);
        Bank.withdraw("Pot of flour",1);
        Waiting.waitNormal(700,10);
        Bank.close();
        while (!constants.cooksKitchen.containsMyPlayer()){
            GlobalWalking.walkTo(constants.cooksKitchen.getRandomTile());
            Waiting.waitNormal(700,10);
            Log.info("We are in the kitchen");
        }
        if(talkToCook() && Waiting.waitUntil(()-> ChatScreen.isOpen())){
            Waiting.waitNormal(600, 90);
            if(ChatScreen.handle(constants.COOK_DIALOGUE)){
                Log.info("Successfully handed in quest");
                //YAY! This code will only execute if we successfully handle the Cook's chatscreen AND our ingredients are taken from our inventory
            }
        }
        Waiting.waitNormal(700,10);
        Keyboard.pressEscape();
        while (!ChatScreen.isOpen()){
            Waiting.waitNormal(700,10);
        }
        ChatScreen.handle();
    }
    private boolean talkToCook(){
        return Query.npcs()//The cook is an npc, so let's query those
                .nameEquals("Cook") //We only want npcs named cook
                .findBestInteractable() // Let's make tribot decide which egg to get
                .map(cook -> cook.interact("Talk-to")) //if there is a cook let's try to Talk-to him
                .orElse(false); // if there is not a cook, or Talking to him fails, let's return false
    }
}
