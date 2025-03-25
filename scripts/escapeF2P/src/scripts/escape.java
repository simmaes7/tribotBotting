package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.script.TribotScriptManifest;
import scripts.tasks.miningSmithingTask;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "f2pRestricted",
        name = "escape f2p",
        description = "does variety of tasks to unrestrict the account"
)
public class escape implements TribotScript {
    miningSmithingTask miningSmithingTask = new miningSmithingTask();
    @Override
    public void execute(@NotNull String s) {
        miningSmithingTask.execute();
    }
}
