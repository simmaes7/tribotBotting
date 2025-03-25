package scripts.quests.restlessGhost;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    public static final String[] Father_Aereck_Dialogue = {"I'm looking for a quest!", "Yes."};
    public static final String[] Father_Urhney_Dialogue = {"Father Aereck sent me to talk to you.","He's got a ghost haunting his graveyard."};
    public static final String[] Restless_Ghost_Dialogue = {"Yep, now tell me what the problem is."};
    public static final Area Father_AereckArea = Area.fromRadius(new WorldTile(3243, 3207,0),2);
    public static final Area Lumbridge_Graveyard = Area.fromRadius(new WorldTile(3249, 3193,0),2);
    public static final WorldTile Lumbridge_Graveyard_Door_Entry = new WorldTile(3246, 3193,0);
    public static final WorldTile Lumbridge_Graveyard_Door_Exit = new WorldTile(3247, 3193,0);
    public static final Area Father_UrhneyArea = Area.fromRadius(new WorldTile(3147, 3170,0),2);
    public static final WorldTile Father_Urhney_Door_Area_Enter = new WorldTile(3147, 3172,0);
    public static final WorldTile Father_Urhney_Door_Area_Exit = new WorldTile(3147, 3173,0);
    public static final WorldTile Wizard_Tower_First_Door_Enter_Area = new WorldTile(3109, 3167,0);
    public static final WorldTile Wizard_Tower_First_Door_Exit_Area = new WorldTile(3109, 3166,0);
    public static final WorldTile Wizard_Tower_Second_Door_Enter_Area = new WorldTile(3107, 3163,0);
    public static final WorldTile Wizard_Tower_Second_Door_Exit_Area = new WorldTile(3106, 3161,0);
    public static final WorldTile Wizard_Tower_Third_Door_Enter_Area = new WorldTile(3110, 9559,0);


    public static final WorldTile Altar = new WorldTile(3118, 9567,0);
}
