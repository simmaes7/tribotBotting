package scripts.quests.romeoAndJuliet;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    public static final String[] Romeo_Dialogue_1 = {"Perhaps I could help to find her for you?", "Yes.","Ok, thanks."};
    public static final String[] Romeo_Dialogue_2 = {"Ok, thanks."};
    public static final String[] Apothecary_Dialogue = {"Talk about something else.","Talk about Romeo & Juliet."};

    public static final Area Romeo = Area.fromRadius(new WorldTile(3210, 3424,0),2);
    public static final Area Juliet = Area.fromRadius(new WorldTile(3158, 3426,1),2);
    public static final Area Father_Lawrence = Area.fromRadius(new WorldTile(3254, 3483,0),2);
    public static final Area Apothecary = Area.fromRadius(new WorldTile(3194, 3404,0),2);
    
    public static final WorldTile Romeo_Tile = new WorldTile(3208, 3425, 0);
    public static final WorldTile Juliet_Tile = new WorldTile(3158, 3426, 1);
    public static final WorldTile Father_Lawrence_Tile = new WorldTile(3254, 3482, 0);
    public static final WorldTile Apothecary_Tile = new WorldTile(3194, 3404, 0);
}
