
package nostr.base;

import lombok.Getter;

/**
 *
 * @author squirrel
 */
@Getter
public enum Command {
    AUTH("IN/OUT"),
    EVENT("IN/OUT"),
    REQ("OUT"),
    CLOSE("OUT"),
    CLOSED("IN"),
    NOTICE("IN"),
    EOSE("IN"),
    OK("IN");

    public static final String DIRECTION_IN = "IN";
    public static final String DIRECTION_OUT = "OUT";
    public static final String DIRECTION_IN_OUT = "IN/OUT";

    private final String direction;

    Command(String direction) {
        this.direction = direction;
    }

}
