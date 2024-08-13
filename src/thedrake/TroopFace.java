package thedrake;


import java.io.PrintWriter;

/**
 * Represents the two faces of a troop in the game.
 * AVERS - The front side of the troop, typically showing the troop's primary, battle-ready stance.
 * REVERS - The back side of the troop, representing an alternate stance or capability.
 */
public enum TroopFace implements JSONSerializable {
    /**
     * The front side of the troop.
     */
    AVERS,

    /**
     * The back side of the troop.
     */
    REVERS;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append(JSONStringValue(name()));
    }
}

