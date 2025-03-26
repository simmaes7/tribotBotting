package scripts;
import org.jetbrains.annotations.NotNull;

import org.tribot.script.sdk.script.ScriptConfig;

import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;

import scripts.tasks.*;

@TribotScriptManifest(
        name = "Lumbridge Cow Killer",
        description = "Kills cows in Lumbridge and banks items.",
        category = "Combat",
        author = "YourName"
)
public class LumbridgeCowKiller implements TribotScript {
    banking banking = new banking();
    killing killing = new killing();
    @Override
    public void configure(@NotNull ScriptConfig config) {
        TribotScript.super.configure(config);
    }

    @Override
    public void execute(@NotNull String s) {
        banking.execute();
        killing.execute();
    }
}
