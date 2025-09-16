package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.script.ScriptConfig;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import scripts.antiban.*;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "SMwoodcutting",
        name = "SMrestricted",
        description = "makes accounts unrestricted"
)
public class restricted implements TribotScript {

    private final Logger log = new Logger("Restricted");

    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
        config.setBreakHandlerEnabled(false);
        config.setRandomsAndLoginHandlerEnabled(false);    }

    @Override
    public void execute(@NotNull String s) {

    }

}