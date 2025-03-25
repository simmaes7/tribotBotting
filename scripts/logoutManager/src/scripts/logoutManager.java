package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.walking.GlobalWalking;

public class logoutManager implements TribotScript {
    public static final Area ironOreArea = Area.fromRadius(new WorldTile(3285, 3365,0),6);
    
    
    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
        config.setBreakHandlerEnabled(false);
        config.setRandomsAndLoginHandlerEnabled(false);
    }

    @Override
    public void execute(@NotNull String s) {
        while (true){
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < 2 * 60 * 1000){
                if (!ironOreArea.containsMyPlayer()){
                    GlobalWalking.walkTo(ironOreArea.getRandomTile());
                } else{
                    GlobalWalking.walkToBank();
                }
            }
            logOutForOneMinute();
        }
    }
    private void logOutForOneMinute() {
        System.out.println("Logging out for 1 minute...");
        Login.logout();

        try {
            Thread.sleep(1 * 60 * 1000); // Wait for 1 minute
        } catch (InterruptedException e) {
            System.out.println("Sleep was interrupted.");
        }

        System.out.println("1 minute passed. Logging back in...");
        Login.login(); // Log back in automatically
    }
}
