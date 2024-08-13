package thedrake;

import java.io.PrintWriter;
import java.util.List;

/**
 * Represents a position on the game board.
 * Provides methods to interact with and manipulate tile positions, including stepping to adjacent tiles,
 * getting neighbors, and checking proximity to other positions.
 */
public interface TilePos extends JSONSerializable {
    /**
     * A special constant representing a position outside the game board.
     * Attempting to use this position for board-related operations will result in UnsupportedOperationException.
     */
    public static final TilePos OFF_BOARD = new TilePos() {

        @Override
        public int i() {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public int j() {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public char column() {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public int row() {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public TilePos step(int columnStep, int rowStep) {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public TilePos step(Offset2D step) {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public List<TilePos> neighbours() {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public boolean isNextTo(TilePos pos) {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
            throw new UnsupportedOperationException("Position is off the board.");
        }

        @Override
        public boolean equalsTo(int i, int j) {
            return false; // OFF_BOARD cannot equal any on-board position.
        }

        @Override
        public String toString() {
            return "off-board"; // Human-readable representation for a position off the board.
        }

        @Override
        public void toJSON(PrintWriter writer) {
            writer.append('"').append(toString()).append('"');
        }
    };

    // Returns the column index of the position.
    public int i();

    // Returns the row index of the position.
    public int j();

    // Returns the column label ('a', 'b', 'c', ...) of the position.
    public char column();

    // Returns the row number of the position.
    public int row();

    // Returns a new position moved by the specified column and row steps.
    public TilePos step(int columnStep, int rowStep);

    // Returns a new position moved by the offset defined in the Offset2D object.
    public TilePos step(Offset2D step);

    // Returns a list of neighboring positions adjacent to this one.
    public List<? extends TilePos> neighbours();

    // Checks if the specified position is adjacent (next to) this position.
    public boolean isNextTo(TilePos pos);

    // Returns a new position moved by the specified offset, considering the playing side.
    public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side);

    // Checks if this position equals to the specified column and row indexes.
    public boolean equalsTo(int i, int j);
}
