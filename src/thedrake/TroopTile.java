package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tile occupied by a troop on the game board.
 * This tile carries information about the troop, including playing team
 * and the current face orientation (avers or revers). This class implements the Tile interface,
 * indicating it is part of the game board structure.
 */
public class TroopTile implements Tile, JSONSerializable {
    // The troop occupying the tile
    private final Troop troop;
    // The side (team) the troop belongs to
    private final PlayingSide side;
    // The face orientation of the troop (avers or revers)
    private final TroopFace face;

    /**
     * Constructs a TroopTile with a specified troop, its side, and face orientation.
     *
     * @param troop The troop occupying the tile.
     * @param side  The side (team) the troop belongs to.
     * @param face  The face orientation of the troop on the tile.
     */
    public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    /**
     * Returns the side (team) of the troop.
     *
     * @return The playing side of the troop.
     */
    public PlayingSide side() {
        return side;
    }

    /**
     * Returns the face orientation of the troop on the tile.
     *
     * @return The current face orientation of the troop.
     */
    public TroopFace face() {
        return face;
    }

    /**
     * Returns the troop occupying the tile.
     *
     * @return The troop on the tile.
     */
    public Troop troop() {
        return troop;
    }

    /**
     * Indicates that the tile cannot be stepped on because it is occupied by a troop.
     *
     * @return false always, as the tile is occupied.
     */
    public boolean canStepOn() {
        return false;
    }

    /**
     * Indicates that the tile has a troop on it.
     *
     * @return true always, as the tile is occupied by a troop.
     */
    public boolean hasTroop() {
        return true;
    }

    /**
     * Generates a list of all possible moves for the troop located at the specified position.
     * This method considers the troop's current actions based on its face (avers or revers) and
     * evaluates each action to determine valid moves within the current game state.
     *
     * @param pos   The board position of the troop for which to generate moves.
     * @param state The current state of the game, used to evaluate move validity.
     * @return A list of {@link Move} objects representing all possible moves from the given position.
     */
    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {
        List<Move> moveList = new ArrayList<>();
        List<TroopAction> actions = troop.actions(face);

        for (TroopAction action : actions) {
            moveList.addAll(action.movesFrom(pos, side, state));
        }

        return moveList;
    }


    /**
     * Creates a new TroopTile with the troop's face orientation flipped.
     * This method is used to change the troop's face from avers to revers or vice versa.
     *
     * @return A new TroopTile with the troop's face orientation flipped.
     */
    public TroopTile flipped() {
        return new TroopTile(troop, side, face == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append('{').append(JSONKeyValue("troop"));
        troop().toJSON(writer);
        writer.append(',');

        writer.append(JSONKeyValue("side"));
        side().toJSON(writer);
        writer.append(',');

        writer.append(JSONKeyValue("face"));
        face().toJSON(writer);
        writer.append('}');
    }
}
