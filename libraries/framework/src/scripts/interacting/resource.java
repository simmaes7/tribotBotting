package scripts.interacting;

import org.tribot.script.sdk.query.Query;
import org.tribot.script.sdk.types.Area;

import java.util.concurrent.locks.Condition;

public class resource {
    public static boolean cutTree(String tree){
        return  Query.gameObjects()
                .nameEquals(tree)
                .isReachable()
                .findClosest()
                .map(i -> i.click("Chop down"))
                .orElse(false);
    }
    public static boolean cutTreeArea(String tree, Area area){
        return  Query.gameObjects()
                .nameEquals(tree)
                .isReachable()
                .filter(area::contains)
                .findClosest()
                .map(i -> i.click("Chop down"))
                .orElse(false);
    }
}
