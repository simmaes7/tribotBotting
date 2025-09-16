package scripts.interacting;

import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import scripts.Logger;

public class waiter {
    static Logger log = new Logger("Waiting");
    public static void waitForAnimation(){

        log.info("waiting for animation");
        do {
            Waiting.wait(100);
        } while (!MyPlayer.isAnimating());
    }
    public static void waitForNonAnimation(){
        log.info("waiting for animation to stop");
        do {
            Waiting.wait(1200);
        } while(MyPlayer.isAnimating());
    }
}
