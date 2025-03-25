package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.input.Keyboard;
import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

import java.security.Key;

public class blueDye implements TribotScript {
    public static final Area aggieArea = Area.fromRadius(new WorldTile(3085, 3259,0),1);
    
    @Override
    public void execute(@NotNull String s) {
        Mouse.setSpeed(10000);
        while (Inventory.contains("Woad leaf")){
            if (Inventory.isFull()){
                GlobalWalking.walkToBank();
                goBank();
            } else {
                if (!aggieArea.containsMyPlayer()) {
                    GlobalWalking.walkTo(aggieArea.getRandomTile());
                } else {
                    while (!Inventory.isFull()){
                        Query.inventory().nameEquals("Woad leaf").findFirst().ifPresent(i->i.click("Use"));
                        Keyboard.typeString(" ");
                        Query.npcs().nameEquals("Aggie").findFirst().ifPresent(i->i.click("Use"));
                        Keyboard.typeString(" ");
                    }
                }
            }
        }
    }
    private static void goBank(){
        GlobalWalking.walkToBank();
        Bank.ensureOpen();
        Bank.depositAll("Blue dye");
        Waiting.waitNormal(400,20);
        Bank.close();
        Options.setRunEnabled(true);
    }
}
