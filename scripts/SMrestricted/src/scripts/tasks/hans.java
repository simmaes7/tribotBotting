package scripts.tasks;

import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.walking.GlobalWalking;
import org.tribot.script.sdk.*;
import org.tribot.script.sdk.query.Query;
import scripts.constants;
import scripts.interacting.*;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class hans {
    public static boolean execute(){
        String chatMessage = "";
        if (constants.hansArea.getCenter().distance() > 4) {
            if (GlobalWalking.walkTo(constants.hansArea.getCenter()) && Waiting.waitUntil(() -> constants.hansArea.getCenter().distance() <= 4)) {
                Waiting.waitNormal(600, 90);
            }
        }
        while (!Query.npcs().nameEquals("Hans").isAny()){
            Waiting.waitNormal(600, 90);
        }
        talking.interactWithPerson("Hans","Age");
        while (!ChatScreen.isOpen()){
            Waiting.wait(200);
        }
        Optional<String> optionalMessage = ChatScreen.getMessage();
        if (optionalMessage.isPresent()) {
            chatMessage = optionalMessage.get();
        }
        int totalHours = convertToTotalHours(chatMessage);
        int questPoints = MyPlayer.getQuestPoints();
        int totalLevels = MyPlayer.getTotalLevel();

        // Return true if all conditions are met.
        return totalHours >= 20 && questPoints >= 10 && totalLevels >= 100;
    }

    public static int convertToTotalHours(String chatMessage) {
        String cleanedMessage = chatMessage.replaceAll("<br>", " ").trim();
        int days = 0, hours = 0;

        // Regex to match days and hours.
        Pattern dayPattern = Pattern.compile("(\\d+) days?");
        Pattern hourPattern = Pattern.compile("(\\d+) hours?");

        Matcher dayMatcher = dayPattern.matcher(cleanedMessage);
        Matcher hourMatcher = hourPattern.matcher(cleanedMessage);

        if (dayMatcher.find()) {
            days = Integer.parseInt(dayMatcher.group(1));
        }
        if (hourMatcher.find()) {
            hours = Integer.parseInt(hourMatcher.group(1));
        }

        // Convert days to hours and add hours.
        return (days * 24) + hours;
    }
}
