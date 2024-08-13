package thedrake;

import java.io.PrintWriter;
import java.util.List;

/**
 * Represents a troop in the game.
 * A troop has a name and can have different positions (pivots) for its front (avers) and back (revers) sides.
 */
public class Troop implements JSONSerializable {
    // Name of the troop
    private final String name;
    // The pivot point when the troop is facing front (avers)
    private final Offset2D aversPivot;
    // The pivot point when the troop is facing back (revers)
    private final Offset2D reversPivot;

    private final List<TroopAction> aversActions, reverseActions;

    /**
     * Constructs a Troop with specified name, avers pivot, and revers pivot.
     *
     * @param name        Name of the troop.
     * @param aversPivot  The pivot position for the troop's avers side.
     * @param reversPivot The pivot position for the troop's revers side.
     * @param aversActions Just avers actions
     * @param reverseActions Just reverse actions
     */
    public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> aversActions, List<TroopAction> reverseActions) {
        this.name = name;
        this.aversPivot = aversPivot;
        this.reversPivot = reversPivot;
        this.aversActions = aversActions;
        this.reverseActions = reverseActions;
    }


    /**
     * Constructs a Troop with the same pivot for both avers and revers sides.
     *
     * @param name  Name of the troop.
     * @param pivot The pivot position for both the troop's avers and revers sides.
     * @param aversActions Just avers actions
     * @param reverseActions Just reverse actions
     */
    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reverseActions) {
        this.name = name;
        this.reversPivot = this.aversPivot = pivot;
        this.aversActions = aversActions;
        this.reverseActions = reverseActions;
    }


    /**
     * Constructs a Troop with a default pivot of [1,1] for both avers and revers sides.
     *
     * @param name Name of the troop.
     */
    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reverseActions) {
        this.name = name;
        // Default pivot position is set to [1,1]
        this.aversPivot = new Offset2D(1, 1);
        this.reversPivot = new Offset2D(1, 1);
        this.aversActions = aversActions;
        this.reverseActions = reverseActions;
    }

    /**
     * Decides which skills a troop has.
     *
     * @param face The face (avers or revers) of the troop.
     * @return Actions for the specified face
     */
    public List<TroopAction> actions(TroopFace face) {
        return face == TroopFace.AVERS ? aversActions : reverseActions;
    }


    /**
     * Returns the name of the troop.
     *
     * @return The name of the troop.
     */
    public String name() {
        return name;
    }

    /**
     * Returns the pivot position based on the specified face of the troop.
     *
     * @param face The face (avers or revers) of the troop.
     * @return The pivot position for the specified face.
     */
    public Offset2D pivot(TroopFace face) {
        return face == TroopFace.AVERS ? aversPivot : reversPivot;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append(JSONStringValue(name()));
    }
}
