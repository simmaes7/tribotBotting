package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import scripts.antiban.*;

public class restricted implements TribotScript{
    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
    }

    @Override
    public void execute(@NotNull String s) {

    }
}
