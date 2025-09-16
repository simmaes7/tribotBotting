package scripts.interacting;

import org.tribot.script.sdk.query.Query;

public class talking {
    public static boolean interactWithPerson(String person, String interact){
        return Query.npcs()
                .nameEquals(person)
                .findBestInteractable()
                .map(hans -> hans.interact(interact))
                .orElse(false);
    }
    public static boolean talkingToPerson(String person){
        return Query.npcs()
                .nameEquals("person")
                .findBestInteractable()
                .map(hans -> hans.interact("Age"))
                .orElse(false);
    }
}
