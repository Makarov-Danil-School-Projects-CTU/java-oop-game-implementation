package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an army in the game, including troops on the board, troops waiting to be deployed (the stack),
 * and captured enemy troops.
 */
public class Army implements JSONSerializable {
    // The troops currently on the board for this army.
    private final BoardTroops boardTroops;
    // The list of troops that have not yet been placed on the board.
    private final List<Troop> stack;
    // The list of enemy troops that have been captured by this army.
    private final List<Troop> captured;

    /**
     * Constructs an Army with a specific playing side and initial stack of troops.
     * Initially, there are no captured troops.
     *
     * @param playingSide The side (e.g., ORANGE or BLUE) this army belongs to.
     * @param stack       The initial list of troops available to be placed on the board.
     */
    public Army(PlayingSide playingSide, List<Troop> stack) {
        this(new BoardTroops(playingSide), stack, Collections.emptyList());
    }

    /**
     * Constructs an Army with specified board troops, stack of troops, and list of captured troops.
     *
     * @param boardTroops The BoardTroops instance managing the troops on the board.
     * @param stack       The list of troops available to be placed on the board.
     * @param captured    The list of captured enemy troops.
     */
    public Army(BoardTroops boardTroops, List<Troop> stack, List<Troop> captured) {
        this.boardTroops = boardTroops;
        this.stack = stack;
        this.captured = captured;
    }

    /**
     * Returns the playing side of this army.
     *
     * @return The playing side.
     */
    public PlayingSide side() {
        return boardTroops.playingSide();
    }

    /**
     * Provides access to the BoardTroops instance, which manages the troops on the board.
     *
     * @return The BoardTroops instance.
     */
    public BoardTroops boardTroops() {
        return boardTroops;
    }

    /**
     * Returns the list of troops currently waiting to be deployed from the stack.
     *
     * @return The stack of troops.
     */
    public List<Troop> stack() {
        return stack;
    }

    /**
     * Returns the list of enemy troops that have been captured by this army.
     *
     * @return The list of captured troops.
     */
    public List<Troop> captured() {
        return captured;
    }

    /**
     * Attempts to place the top troop from the stack onto the board at the specified target position.
     * Updates the stack and the board troops accordingly.
     *
     * @param target The position on the board where the troop is to be placed.
     * @return A new Army instance reflecting the updated state.
     * @throws IllegalArgumentException if the target position is OFF_BOARD.
     * @throws IllegalStateException    if the stack is empty or the target position is already occupied.
     */
    public Army placeFromStack(BoardPos target) {
        if (target == TilePos.OFF_BOARD)
            throw new IllegalArgumentException();

        if (stack.isEmpty())
            throw new IllegalStateException();

        if (boardTroops.at(target).isPresent())
            throw new IllegalStateException();

        List<Troop> newStack = new ArrayList<Troop>(
                stack.subList(1, stack.size()));

        return new Army(
                boardTroops.placeTroop(stack.get(0), target),
                newStack,
                captured);
    }

    /**
     * Moves a troop from one position to another on the board.
     *
     * @param origin The original position of the troop.
     * @param target The new position for the troop.
     * @return A new Army instance reflecting the updated state.
     */
    public Army troopStep(BoardPos origin, BoardPos target) {
        return new Army(boardTroops.troopStep(origin, target), stack, captured);
    }

    /**
     * Flips the troop at the specified position to its other side.
     *
     * @param origin The position of the troop to flip.
     * @return A new Army instance reflecting the updated state.
     */
    public Army troopFlip(BoardPos origin) {
        return new Army(boardTroops.troopFlip(origin), stack, captured);
    }

    /**
     * Removes a troop from the board at the specified position.
     *
     * @param target The position from which the troop is to be removed.
     * @return A new Army instance reflecting the updated state.
     */
    public Army removeTroop(BoardPos target) {
        return new Army(boardTroops.removeTroop(target), stack, captured);
    }

    /**
     * Adds an enemy troop to the list of captured troops.
     *
     * @param troop The enemy troop that has been captured.
     * @return A new Army instance reflecting the updated state.
     */
    public Army capture(Troop troop) {
        List<Troop> newCaptured = new ArrayList<Troop>(captured);
        newCaptured.add(troop);

        return new Army(boardTroops, stack, newCaptured);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append('{').append(JSONKeyValue("boardTroops"));

        boardTroops().toJSON(writer);

        writer.append(',').append(JSONKeyValue("stack")).append('[');

        for (int i = 0; i < stack.size(); i++) {
            stack.get(i).toJSON(writer);

            if (i + 1 < stack.size()) {
                writer.append(',');
            }
        }
        writer.append(']').append(',').append(JSONKeyValue("captured")).append('[');

        for (int i = 0; i < captured.size(); i++) {
            captured.get(i).toJSON(writer);
            if (i + 1 < captured.size()) {
                writer.append(',');
            }
        }

        writer.append(']').append('}');
    }
}
