package scripts.quests.doricsQuest;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    public static final WorldTile Dorics_House_WorldTile = new WorldTile(2947, 3451,0);
    public static final Area Dorics_House_Area = Area.fromRadius(new WorldTile(2947, 3451,0),2);
    public static final String[] Doric_Dialogue = {"I wanted to use your anvils.","Yes.", "Yes, I will get you the materials."};
}
