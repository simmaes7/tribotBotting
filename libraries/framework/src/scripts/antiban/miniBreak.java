package scripts.antiban;

import lombok.Getter;
import org.tribot.script.sdk.Login;
import org.tribot.script.sdk.MyPlayer;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.input.Mouse;
import org.tribot.script.sdk.interfaces.Character;
import org.tribot.script.sdk.util.TribotRandom;
import scripts.Logger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class miniBreak {
    /**
     * -- GETTER --
     *  Check if a MiniBreak is currently active
     */
    @Getter
    private static boolean active = false;
    private static final Random RAND = new Random();
    private static boolean lunchTaken = false;


    /**
     * Leave based on mean/sd optional parameters
     */
    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(v, max));
    }

    public static void leave(int mean, int sd) {
        int milliseconds = TribotRandom.normal(mean, sd);
        active = true;

        new Logger("MiniBreak").info("Leaving screen for " + milliseconds + "ms");

        Mouse.leaveScreen();
        Waiting.wait(milliseconds);

        active = false;
    }

    // Overload with defaults (mean=15000, sd=2000)
    public static void leave() {
        leave(15_000, 2_000);
    }

    /**
     * A fatigue based leave of the screen, influenced by runtime / time of day
     */
    public static void fatigueLeave(int multiplier) {
        int milliseconds = fatigueResolver.getMilliseconds() * multiplier;
        active = true;

        new Logger("MiniBreak").info("Leaving screen for " + milliseconds + "ms");

        Mouse.leaveScreen();
        Waiting.wait(milliseconds);

        active = false;
    }

    // Overload with default multiplier=1
    public static void fatigueLeave() {
        fatigueLeave(1);
    }

    /**
     * On screen pause, the mouse does NOT leave the screen, but simply wait until our next move
     */
    public static void pause(int mean, int sd) {
        active = true;

        int milliseconds = TribotRandom.normal(mean, sd);
        Waiting.wait(milliseconds);

        active = false;
    }

    // Overload with defaults (mean=1500, sd=4300)
    public static void pause() {
        pause(1_500, 4_300);
    }

    /**
     * A fatigue based pausing time, we do NOT leave the screen, but simply wait until our next move
     */
    public static void fatiguePause(int multiplier) {
        active = true;

        int milliseconds = fatigueResolver.getMilliseconds() * multiplier;
        Waiting.wait(milliseconds);

        active = false;
    }

    // Overload with default multiplier=1
    public static void fatiguePause() {
        fatiguePause(1);
    }

    /**
     * A very short, human-like micro pause
     * <p> 
     * This simulates natural, tiny hesitations humans make when deciding their next action.
     * Uses a log-normal distribution to create realistic, right-skewed timings
     * with most pauses between 80–300 ms.
     * <p> 
     */
    public static void microBreak(){
        active = true;

        final double medianMs = 160.0;
        final double p95Ms    = 300.0;       // rare but still "micro"
        final double mu       = Math.log(medianMs);
        final double sigma    = (Math.log(p95Ms) - mu) / 1.645; // 95th percentile of Normal is ~+1.645σ

        // --- 2) Sample log-normal (Java's nextGaussian() gives N(0,1)) ---
        double z  = RAND.nextGaussian();
        double x  = Math.exp(mu + sigma * z);       // raw sample (ms), right-skewed
        int ms    = (int)Math.round(x);

        // --- 3) Soft fatigue scaling from your resolver (kept subtle) ---
        int baseline = Math.max(300, fatigueResolver.getMilliseconds()); // avoid tiny denominator effects
        double softScale = Math.sqrt(baseline / 850.0);  // 850 ≈ default mid of your baseline
        ms = (int)Math.round(ms * softScale);

        // --- 4) Clamp to the intended micro range ---
        ms = clamp(ms, 80, 300);

        new Logger("MiniBreak-microBreak").info("Leaving screen for " + ms + "ms");
        Mouse.leaveScreen();
        Waiting.wait(ms);

        active = false;
    }
    public static void maybeMicroBreak(double chance){
        lottery.execute(chance,miniBreak::microBreak);
    }
    /**
     * A short human-like break (≈0.5–3 s).
     * <p>
     * Duration is sampled from a log-normal distribution (median≈1200 ms, p95≈3000 ms)
     * and softly scaled by current fatigue (sqrt(baseline/850)). We use
     * {@code lottery.shouldExecute(0.30)} to decide whether to leave the screen.
     * No multi-segment logic here—single, simple break.
     */
    public static void shortBreak(){
        active = true;
        final double medianMs = 1_200.0;             // typical quick refocus
        final double p95Ms    = 3_000.0;             // long side of "short"
        final double mu       = Math.log(medianMs);
        final double sigma    = (Math.log(p95Ms) - mu) / 1.645; // 95th percentile ≈ +1.645σ

        // --- 2) Sample log-normal (positive, right-skewed) ---
        double z  = RAND.nextGaussian();             // ~N(0,1)
        double x  = Math.exp(mu + sigma * z);        // log-normal ms
        int ms    = (int) Math.round(x);

        // --- 3) Soft fatigue scaling; sqrt keeps the effect subtle ---
        int baseline = Math.max(300, fatigueResolver.getMilliseconds());
        double softScale = Math.sqrt(baseline / 850.0); // 850 ≈ baseline midpoint of your resolver
        ms = (int) Math.round(ms * softScale);

        // --- 4) Clamp to intended range (safety) ---
        ms = clamp(ms, 500, 3_000);

        // --- 5) Decide whether to leave screen using lottery.shouldExecute ---

        new Logger("MiniBreak-shortBreak").info("Leaving screen for " + ms + "ms");
        Mouse.leaveScreen();
        Waiting.wait(ms);

        active = false;
    }
    public static void maybeShortBreak(double chance){
        lottery.execute(chance,miniBreak::shortBreak);
    }
    /**
     * A medium human-like break (≈5–20 s), generally leaving the screen.
     * <p>
     * Duration is drawn from a log-normal distribution (median≈10 s, p95≈20 s)
     * and softly scaled by current fatigue using sqrt(baseline/850). Whether the
     * mouse leaves the screen is decided via {@code lottery.shouldExecute(0.80)}.
     * Single-segment only (no splitting).
     */
    public static void mediumBreak() {
        active = true;

        // --- 1) Log-normal parameters from median & 95th percentile ---
        final double medianMs = 10_000.0;            // typical medium break
        final double p95Ms    = 20_000.0;            // long tail end
        final double mu       = Math.log(medianMs);
        final double sigma    = (Math.log(p95Ms) - mu) / 1.645; // 95th ≈ +1.645σ

        // --- 2) Sample log-normal (positive, right-skewed) ---
        double z  = RAND.nextGaussian();             // ~N(0,1)
        double x  = Math.exp(mu + sigma * z);        // log-normal ms
        int ms    = (int) Math.round(x);

        // --- 3) Soft fatigue scaling (kept subtle) ---
        int baseline = Math.max(300, fatigueResolver.getMilliseconds());
        double softScale = Math.sqrt(baseline / 850.0); // 850 ≈ baseline midpoint
        ms = (int) Math.round(ms * softScale);

        // --- 4) Clamp to intended range ---
        ms = clamp(ms, 5_000, 20_000);

        new Logger("MiniBreak-mediumBreak").info("Leaving screen for " + ms + "ms");
        Mouse.leaveScreen();
        Waiting.wait(ms);

        active = false;
    }
    public static void maybeMediumBreak(double chance){
        lottery.execute(chance,miniBreak::mediumBreak);
    }
    /**
     * A rare long break (≈30–120 s), almost always leaving the screen.
     * <p>
     * Duration is drawn from a log-normal distribution (median≈60 s, p95≈120 s)
     * and softly scaled by current fatigue using sqrt(baseline/850). Whether the
     * mouse leaves the screen is decided via {@code lottery.shouldExecute(0.95)}.
     * Single-segment only.
     */
    public static void longBreak() {
        active = true;

        // --- 1) Log-normal parameters from median & 95th percentile ---
        final double medianMs = 60_000.0;            // typical long step-away
        final double p95Ms    = 120_000.0;           // long tail end
        final double mu       = Math.log(medianMs);
        final double sigma    = (Math.log(p95Ms) - mu) / 1.645; // 95th ≈ +1.645σ

        // --- 2) Sample log-normal (positive, right-skewed) ---
        double z  = RAND.nextGaussian();             // ~N(0,1)
        double x  = Math.exp(mu + sigma * z);        // log-normal ms
        int ms    = (int) Math.round(x);

        // --- 3) Soft fatigue scaling (kept subtle) ---
        int baseline = Math.max(300, fatigueResolver.getMilliseconds());
        double softScale = Math.sqrt(baseline / 850.0); // 850 ≈ resolver midpoint
        ms = (int) Math.round(ms * softScale);

        // --- 4) Clamp to intended range ---
        ms = clamp(ms, 30_000, 120_000);

        new Logger("MiniBreak-longBreak").info("Leaving screen for " + ms + "ms");
        Mouse.leaveScreen();
        Waiting.wait(ms);

        active = false;
    }
    public static void maybeLongBreak(double chance){
        lottery.execute(chance,miniBreak::longBreak);
    }
    public static void maybeLunchBreak(){
        if (!lunchTaken){
            int currentHour = runtimeTracker.currentHour();
            int currentMinute = runtimeTracker.currentMinute();
            if (currentHour >= 12 && currentHour < 14){
                lottery.execute(0.02, miniBreak::lunchBreak);
            } else{
                int targetMinute = ThreadLocalRandom.current().nextInt(60) + 1; // 0–9
                if  (currentMinute >= targetMinute){
                    miniBreak.lunchBreak();
                }
            }
        }
    }
    public static void lunchBreak(){
        active = true;
        int ms = java.util.concurrent.ThreadLocalRandom.current()
                .nextInt(1_800_000, 7_200_001); // [1,800,000 .. 7,200,000]
        Login.logout();
        Mouse.leaveScreen();
        Waiting.wait(ms);
        Login.login();
        active = false;


        lunchTaken = true;
    }
}
