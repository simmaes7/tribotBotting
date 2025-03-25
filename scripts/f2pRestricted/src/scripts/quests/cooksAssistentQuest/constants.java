package scripts.quests.cooksAssistentQuest;

import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;

public class constants {
    public static final Area cooksKitchen= Area.fromRadius(new WorldTile(3209, 3213, 0),1);
    public static final String[] COOK_DIALOGUE = {"What's wrong?", "Yes."};
}
