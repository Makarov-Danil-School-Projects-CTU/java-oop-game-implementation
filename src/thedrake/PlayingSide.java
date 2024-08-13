package thedrake;

import java.io.PrintWriter;

/**
 * Enumerates the sides or teams that can play in the game.
 * This enum is used to distinguish between the two possible sides or teams a player can choose or be assigned to.
 */
public enum PlayingSide implements JSONSerializable {
    /**
     * Represents the orange side or team in the game.
     */
    ORANGE,

    /**
     * Represents the blue side or team in the game.
     */
    BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append(JSONStringValue(name()));
    }
}