package scripts;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    public static final String[] Romeo_Dialogue_1 = {"Perhaps I could help to find her for you?", "Yes.","Ok, thanks."};
    public static final String[] Romeo_Dialogue_2 = {"Ok, thanks."};
    public static final String[] Apothecary_Dialogue = {"Talk about something else.","Talk about Romeo & Juliet."};

    public static final Area hansArea = Area.fromRadius(new WorldTile(3221, 3219, 0), 1);
    public static Area grandExchange = Area.fromRectangle(new WorldTile(3159, 3494), new WorldTile(3169, 3485));
    public static Area trees_above_grandExchange = Area.fromRectangle(new WorldTile(3198, 3520), new WorldTile(3242, 3508));
}
