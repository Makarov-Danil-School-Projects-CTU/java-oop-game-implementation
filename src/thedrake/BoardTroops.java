package thedrake;

import java.io.PrintWriter;
import java.util.*;

/**
 * Manages the troops on the game board for a specific playing side.
 * This includes placing, moving, flipping, and removing troops, as well as tracking the leader's position and the number of guards.
 */
public class BoardTroops implements JSONSerializable {
    // The playing side these troops belong to.
    private final PlayingSide playingSide;
    // Maps board positions to TroopTile instances, representing the troops' placements on the board.
    private final Map<BoardPos, TroopTile> troopMap;
    // The position of the leader on the board, initially set to OFF_BOARD.
    private final TilePos leaderPosition;
    // The number of guards placed on the board, initially zero.
    private final int guards;

    /**
     * Constructs a new BoardTroops instance for a given playing side with no initial troops.
     *
     * @param playingSide The playing side (e.g., ORANGE or BLUE).
     */
    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    /**
     * Constructs a new BoardTroops instance with a specified configuration of troops.
     *
     * @param playingSide    The playing side (e.g., ORANGE or BLUE).
     * @param troopMap       A map of board positions to TroopTile instances.
     * @param leaderPosition The initial position of the leader, or OFF_BOARD if not yet placed.
     * @param guards         The initial number of guards placed on the board.
     */
    public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
        this.playingSide = playingSide;
        this.troopMap = new HashMap<>(troopMap);
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    /**
     * Returns the TroopTile at a specified position on the board.
     *
     * @param pos The position to check for a troop.
     * @return An Optional containing the TroopTile at the specified position, or empty if no troop is present.
     */
    public Optional<TroopTile> at(TilePos pos) {
        return Optional.ofNullable(troopMap.get(pos));
    }

    /**
     * Returns the playing side of the troops managed by this instance.
     *
     * @return The playing side.
     */
    public PlayingSide playingSide() {
        return playingSide;
    }

    /**
     * Returns the current position of the leader on the board.
     *
     * @return The leader's position, or OFF_BOARD if the leader has not been placed.
     */
    public TilePos leaderPosition() {
        return leaderPosition;
    }

    /**
     * Returns the number of guards that have been placed on the board.
     *
     * @return The number of guards.
     */
    public int guards() {
        return this.guards;
    }

    /**
     * Checks whether the leader of the troops has been placed on the board.
     *
     * @return True if the leader has been placed, false otherwise.
     */
    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    /**
     * Determines if the current phase is placing guards, which occurs after the leader is placed but before all guards are placed.
     *
     * @return True if in the phase of placing guards, false otherwise.
     */
    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    /**
     * Returns the set of positions on the board that are occupied by troops.
     *
     * @return A set of BoardPos instances representing occupied positions.
     */
    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    /**
     * Places a troop on the board at the specified target position. Updates the leader position and guards count if necessary.
     *
     * @param troop  The troop to be placed on the board.
     * @param target The position on the board where the troop is to be placed.
     * @return A new BoardTroops instance reflecting the updated state after placing the troop.
     * @throws IllegalArgumentException if the target position is already occupied.
     */
    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (at(target).isPresent()) {
            throw new IllegalArgumentException("Tile already occupied");
        }

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(this.troopMap);
        newTroops.put(target, new TroopTile(troop, this.playingSide, TroopFace.AVERS));

        TilePos newLeaderPosition = isLeaderPlaced() ? this.leaderPosition : target;
        int newGuardsCount = this.guards;

        if (isPlacingGuards()) {
            newGuardsCount++;
        }

        return new BoardTroops(this.playingSide, newTroops, newLeaderPosition, newGuardsCount);
    }

    /**
     * Moves a troop from one position to another on the board and flips its face.
     *
     * @param origin The original position of the troop to move.
     * @param target The target position for the troop.
     * @return A new BoardTroops instance reflecting the updated state after the move.
     * @throws IllegalStateException    if moving troops is not currently allowed (e.g., during placing guards).
     * @throws IllegalArgumentException if the origin position is empty or the target position is occupied.
     */
    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException("You can't currently move troops during placing guards");

        if (!troopMap.containsKey(origin)) throw new IllegalArgumentException("An origin position is empty");

        if (troopMap.containsKey(target)) throw new IllegalArgumentException("A target position is occupied");

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile tile = newTroopMap.remove(origin);
        newTroopMap.put(target, tile.flipped());

        TilePos newLeaderPosition = leaderPosition.equals(origin) ? target : leaderPosition;

        return new BoardTroops(playingSide, newTroopMap, newLeaderPosition, this.guards);
    }

    /**
     * Flips the face of a troop at a specified position.
     *
     * @param origin The position of the troop to be flipped.
     * @return A new BoardTroops instance reflecting the updated state after the flip.
     * @throws IllegalStateException    if flipping troops is not currently allowed (e.g., before the leader is placed).
     * @throws IllegalArgumentException if the origin position is empty.
     */
    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) throw new IllegalStateException("Cannot move troops before the leader is placed");

        if (isPlacingGuards()) throw new IllegalStateException("Cannot move troops before guards are placed");

        if (!at(origin).isPresent()) throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, this.guards);
    }

    /**
     * Removes a troop from the board at the specified position.
     *
     * @param target The position from which the troop is to be removed.
     * @return A new BoardTroops instance reflecting the updated state after troop removal.
     * @throws IllegalStateException    if removing troops is not currently allowed (e.g., during placing guards).
     * @throws IllegalArgumentException if the target position is empty.
     */
    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced() || isPlacingGuards())
            throw new IllegalStateException("Cannot remove troops while placing leader or guards");

        if (!troopMap.containsKey(target)) throw new IllegalArgumentException("The position is empty");

        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.remove(target);

        TilePos newLeaderPosition = leaderPosition.equals(target) ? TilePos.OFF_BOARD : leaderPosition;

        return new BoardTroops(playingSide, newTroopMap, newLeaderPosition, guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append('{');

        // Serialize PlayingSide
        writer.append(JSONKeyValue("side"));

        playingSide().toJSON(writer);

        writer.append(',');

        // Serialize TilePos
        writer.append(JSONKeyValue("leaderPosition"));

        leaderPosition.toJSON(writer);

        writer.append(',');

        // Serialize guards
        writer.append(JSONKeyValue("guards")).append(Integer.toString(this.guards())).append(',');

        // Begin serialization of troopMap.
        writer.append(JSONKeyValue("troopMap")).append('{');

        List<BoardPos> positions = new ArrayList<>(this.troopPositions());
        positions.sort(Comparator.comparing(BoardPos::toString));

        // Serialize each BoardPos to TroopTile in the troop map.
        for (int i = 0; i < positions.size(); ++i) {
            // Serialize BoardPos
            positions.get(i).toJSON(writer);
            writer.append(':');

            // Serialize the associated TroopTile.
            at(positions.get(i)).get().toJSON(writer);

            // Add a comma separator
            if (i + 1 < positions.size()) {
                writer.append(',');
            }
        }

        writer.append('}').append('}');
    }
}
