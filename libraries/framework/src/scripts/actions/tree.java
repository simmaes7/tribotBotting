package scripts.actions;

import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.types.Area;

public class tree {
    public void cutTree(String name){
        Query.gameObjects()
                .nameEquals(name)
                .isReachable()
                .findClosest()
                .map(i->i.click("Chop down"));
    }
    public void cutTreeWithinArea(String name, Area area){
        Query.gameObjects()
                .nameEquals(name)
                .inArea(area)
                .isReachable()
                .findClosest()
                .map(i->i.click("Chop down"));
    }
}
