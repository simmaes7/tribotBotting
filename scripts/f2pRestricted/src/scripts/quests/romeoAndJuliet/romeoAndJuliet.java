package scripts.quests.romeoAndJuliet;

import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.GameObject;
import org.tribot.script.sdk.types.Widget;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.security.Key;

public class romeoAndJuliet {
    public void execute(){
        
        while (!Bank.isNearby()){
            GlobalWalking.walkToBank();
        }
        Waiting.waitNormal(1400,50);
        Bank.ensureOpen();
        Waiting.waitNormal(1400,50);
        Bank.withdraw("Cadava berries",1);
        Waiting.waitNormal(1400,50);
        Bank.close();
        Waiting.waitNormal(1400,50);
        while (!constants.Romeo.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Romeo.getRandomTile());
            Waiting.waitNormal(1400,50);
            Log.info("Waiting");
        }
        if(talkToNPC("Romeo") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while(ChatScreen.handle(constants.Romeo_Dialogue_1)){
                Waiting.wait(100);
            }
        }
        
         
        while (!constants.Juliet.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Juliet.getRandomTile());
            Waiting.waitNormal(1400,50);
        }
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(GameObject::adjustCameraTo);
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(i->i.click("Open"));
        Waiting.wait(4000);
        if(talkToNPC("Juliet") && Waiting.waitUntil(ChatScreen::isOpen)){
            Log.info("I did something here i think");
            Waiting.waitNormal(600, 90);
            if (ChatScreen.isOpen()){
                ChatScreen.handle();
                Log.info("I am handling the chatscreen");
                Waiting.wait(100);
            }
        }
        Log.info("finished talking to juliet, going to give the letter now");
        while (!constants.Romeo.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Romeo_Tile);
            Waiting.waitNormal(1400,50);
        }
        Log.info("I tried walking");
        if(talkToNPC("Romeo") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            if (ChatScreen.isOpen()){
                ChatScreen.handle(constants.Romeo_Dialogue_2);
                Log.info("I am handling the chatscreen");
                Waiting.wait(100);
            }
        }


         
        while (!constants.Father_Lawrence.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Father_Lawrence_Tile);
            Waiting.waitNormal(1400,50);
        }
        if(talkToNPC("Father Lawrence") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while (ChatScreen.isOpen()){
                if (inCutScene()){
                    Waiting.waitUntil(() ->!inCutScene());
                }
                ChatScreen.handle();
                Log.info("I am handling the chatscreen");
                Waiting.wait(100);
            }
        }

        while (!constants.Apothecary.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Apothecary.getRandomTile());
            Waiting.waitNormal(1400,50);
        }
        if(talkToNPC("Apothecary") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            if (ChatScreen.isOpen()){
                ChatScreen.handle(constants.Apothecary_Dialogue);
                Log.info("I am handling the chatscreen");
                Waiting.wait(100);
            }
        }



        while (!constants.Juliet.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Juliet.getRandomTile());
            Waiting.waitNormal(1400,50);
        }
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(GameObject::adjustCameraTo);
        Query.gameObjects().nameEquals("Door").findBestInteractable().ifPresent(i->i.click("Open"));
        Waiting.wait(4000);
        GlobalWalking.walkTo(constants.Juliet_Tile);
        if(talkToNPC("Juliet") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while (ChatScreen.isOpen() || inCutScene()){
                ChatScreen.handle();
                Log.info("I am handling the chatscreen");
                Waiting.wait(100);
            }
        }
        
        Waiting.waitUntil(MyPlayer::isAnimating);

        while (!constants.Romeo.containsMyPlayer()){
            GlobalWalking.walkTo(constants.Romeo_Tile);
            Waiting.waitNormal(1400,50);
            Log.info("Waiting");
        }
        if(talkToNPC("Romeo") && Waiting.waitUntil(ChatScreen::isOpen)){
            Waiting.waitNormal(600, 90);
            while (ChatScreen.isOpen() || inCutScene()){
                ChatScreen.handle();
                Log.info("I am handling the chatscreen");
                Waiting.wait(3000);
            }
        }
        Waiting.wait(4036);
        Keyboard.pressEscape();
}
    private static boolean talkToNPC(String name){
        return Query.npcs()
                .nameEquals(name)
                .findBestInteractable()
                .map(cook -> cook.interact("Talk-to"))
                .orElse(false);
    }
    public static boolean inCutScene() {
        return GameState.getVarbit(542) == 1;
        //12139
        
    }
}
