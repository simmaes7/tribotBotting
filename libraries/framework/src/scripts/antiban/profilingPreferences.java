package scripts.antiban;

import lombok.Getter;

@Getter
public enum profilingPreferences {
    MIN_MOUSE_SPEED("org.wrscripts.experimental.v1.Antiban.Mouse.minimumSpeed"),
    MAX_MOUSE_SPEED("org.wrscripts.experimental.v1.Antiban.Mouse.maximumSpeed");

    public String key;

    profilingPreferences(String key) {
        this.key = key;
    }

}
