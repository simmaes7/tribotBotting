package scripts.quests.sheepShearer;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    public static final Area Fred_The_Farmer_First_Gate = Area.fromRadius(new WorldTile(3188, 3282,0),1);
    public static final WorldTile Gate = new WorldTile(3188, 3280, 0);
    public static final String[] Fred_The_Farmer_Dialogue = {"I'm looking for a quest.","Yes."};
    
}
