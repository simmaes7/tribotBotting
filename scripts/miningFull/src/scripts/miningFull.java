package scripts;

import org.jetbrains.annotations.NotNull;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.frameworks.behaviortree.nodes.ConditionalNode;
import org.tribot.script.sdk.script.TribotScriptManifest;
import org.tribot.script.sdk.script.TribotScript;
import org.tribot.script.sdk.types.Area;
import org.tribot.script.sdk.types.World;
import org.tribot.script.sdk.types.WorldTile;
import org.tribot.script.sdk.query.*;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.walking.WalkState;

import java.util.Optional;
import java.util.Random;

@TribotScriptManifest(
        author = "Simon Maes",
        category = "FullMining",
        name = "mining",
        description = "picks the right thing to mine"
)
public class miningFull implements TribotScript {
    
    public static final WorldTile ironOreArea = new WorldTile(3175, 3367, 0);
    public static final WorldTile tinOreArea = new WorldTile(3284, 3363, 0);
    public static final WorldTile adamantiteOreArea = new WorldTile(3286, 3423, 0);
    public static final WorldTile runiteOreArea = new WorldTile(3285, 3423, 0);
    public static final Area ironOreArea60 = Area.fromRectangle(new WorldTile(3031, 9739,0),new WorldTile(3035, 9735,0));
    WorldTile miningArea;
    String preferedPickaxe;
    String preferedOre;
    Random random = new Random();
    int mean = 3000;
    @Override
    public void execute(@NotNull String s) {
        while (true){
            preferedOre = pickOre();
            preferedPickaxe = pickPickaxe();
            if (Inventory.isFull()) {
                GlobalWalking.walkToBank();
                Bank.ensureOpen();
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.depositInventory();
                Bank.withdraw(preferedPickaxe, 1);
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
                Bank.close();
                Options.setRunEnabled(true);
            } else {
                if (!Inventory.contains(preferedPickaxe)) {
                    GlobalWalking.walkToBank();
                    Bank.ensureOpen();
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Bank.depositInventory();
                    Bank.withdraw(preferedPickaxe, 1);
                    mean = random.nextInt(1000 - 700 + 1) + 700;
                    Waiting.wait(mean);
                    Bank.close();
                }
                GlobalWalking.walkTo(miningArea);
                if (!MyPlayer.isAnimating() && !MyPlayer.isMoving()) {
                    mineOre(preferedOre);
                    Waiting.waitUntil(2500, MyPlayer::isAnimating);
                }
                mean = random.nextInt(1000 - 700 + 1) + 700;
                Waiting.wait(mean);
            }
            
        }
    }
    private String pickPickaxe(){
        int level = Skill.MINING.getActualLevel();
        String pickaxe = "Iron pickaxe";
        if (level >= 1 && level <= 5){
            pickaxe = "Iron pickaxe";
        } else if(level >= 6 && level <= 10){
            pickaxe = "Steel pickaxe";
        } else if(level >= 11 && level <= 20){
            pickaxe = "Black pickaxe";
        } else if(level >= 21 && level <= 30){
            pickaxe = "Mithril pickaxe";
        } else if(level >= 31 && level <= 40){
            pickaxe = "Adamant pickaxe";
        } else if(level >= 41){
            pickaxe = "Rune pickaxe";
        }
        return pickaxe;
    }
    private String pickOre(){
        int level = Skill.MINING.getActualLevel();
        String ore;
        miningArea = tinOreArea;
        if (level >= 1 && level <= 14){
            ore = "Tin rocks";
            miningArea = tinOreArea;
        } else if(level >= 15 && level <= 59){
            ore = "Iron rocks";
            miningArea = ironOreArea;
        } else if(level >= 60 && level <= 84){
            ore = "Iron rocks";
            miningArea = ironOreArea60.getCenter();
        } else {
            ore = "Rune rocks";
            miningArea = runiteOreArea;
        }
        return ore;
    }
    private boolean mineOre(String oreName){
        return Query.gameObjects().isReachable()
                .maxDistance(3)
                .nameEquals(oreName)
                .findBestInteractable()
                .map(i->i.click("Mine"))
                .orElse(false);
    }
    private void takePickaxeFromBank(String pickaxe){
        Bank.ensureOpen();
        Bank.depositInventory();
        mean = random.nextInt(1000 - 700 + 1) + 700;
        Waiting.wait(mean);
        Bank.withdraw(pickaxe,1);
        mean = random.nextInt(1000 - 700 + 1) + 700;
        Waiting.wait(mean);
        Bank.close();
    }
    public static void hopToRandomWorld() {
        Optional<World> targetWorld = Query.worlds()
                .isNonMembers() // Ensure it's a non-members world
                .isNotDangerous() // Avoid PvP/dangerous worlds
                .isRequirementsMet()
                .isNotCurrentWorld()// Ensure the player meets the world’s requirements
                .findRandom();

        if (targetWorld.isEmpty()) {
            System.out.println("No suitable world found. Retrying...");
            Waiting.waitNormal(1000, 200); // Wait a bit before retrying
        }

        int newWorldId = targetWorld.get().getWorldNumber();

        WorldHopper.hop(newWorldId);
        System.out.println("Successfully hopped to world " + newWorldId);
        Waiting.waitNormal(3000, 1000); // Wait after hopping to avoid spammy behavior
        return; // Exit after a successful hop
    }
    
}
