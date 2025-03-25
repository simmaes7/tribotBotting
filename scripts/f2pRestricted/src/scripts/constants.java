package scripts;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    //woodcutting
    public static final WorldTile trees1 = new WorldTile(3135, 3402, 0);
    public static final WorldTile trees2 = new WorldTile(3170, 3390, 0);
    public static final Area trees = Area.fromRectangle(trees1,trees2);
    
    //mining
    public static final Area ironOreArea = Area.fromRadius(new WorldTile(3287, 3370, 0),1);
    public static final Area tinOreArea = Area.fromRadius(new WorldTile(3284, 3363, 0),1);
    
    //fishing
    public static final Area smallFishingNetSpot = Area.fromRectangle(new WorldTile(3246, 3157,0), new WorldTile(3238, 3145,0));
    public static final Area flyFishingSpot = Area.fromRectangle(new WorldTile(3237, 3256,0), new WorldTile(3241, 3240,0));
    
    
    public static final Area edgevil = Area.fromRadius(new WorldTile(3101, 3496, 0),2);
    public static final Area GrandExchangeArea = Area.fromRadius(new WorldTile(3164, 3489, 0),5);
    public static final Area Anvil = Area.fromRectangle(new WorldTile(3185, 3429,0), new WorldTile(3189, 3425,0));
    
    
}
