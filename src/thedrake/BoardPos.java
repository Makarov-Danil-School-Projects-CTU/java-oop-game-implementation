package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific position on a game board with a defined dimension.
 * Implements the TilePos interface to provide methods for position calculations and comparisons.
 */
public class BoardPos implements TilePos, JSONSerializable {
    // The dimension of the game board. This is used to check if positions are valid within the board's bounds.
    private final int dimension;
    // The column index of this position.
    private final int i;
    // The row index of this position.
    private final int j;

    /**
     * Constructs a new BoardPos with specified dimensions and position indexes.
     *
     * @param dimension The dimension of the board (assumed to be a square).
     * @param i         The column index of the position.
     * @param j         The row index of the position.
     */
    public BoardPos(int dimension, int i, int j) {
        this.dimension = dimension;
        this.i = i;
        this.j = j;
    }

    // Returns the column index.
    @Override
    public int i() {
        return i;
    }

    // Returns the row index.
    @Override
    public int j() {
        return j;
    }

    // Converts the column index to a character label ('a', 'b', 'c', ...).
    @Override
    public char column() {
        return (char) ('a' + i);
    }

    // Converts the row index to a 1-based number.
    @Override
    public int row() {
        return j + 1;
    }

    /**
     * Calculates a new position based on column and row steps.
     * If the new position is outside the board dimensions, returns OFF_BOARD.
     *
     * @param columnStep The step size in the column direction.
     * @param rowStep    The step size in the row direction.
     * @return A new BoardPos or OFF_BOARD if the new position is invalid.
     */
    public TilePos step(int columnStep, int rowStep) {
        int newi = i + columnStep;
        int newj = j + rowStep;

        if ((newi >= 0 && newi < dimension) && (newj >= 0 && newj < dimension)) {
            return new BoardPos(dimension, newi, newj);
        }

        return TilePos.OFF_BOARD;
    }

    // Delegates to step(int, int) using an Offset2D's x and y as steps.
    @Override
    public TilePos step(Offset2D step) {
        return step(step.x, step.y);
    }

    /**
     * Generates a list of neighboring positions directly adjacent to this one.
     * Excludes positions that would be off the board.
     *
     * @return A list of valid neighboring BoardPos.
     */
    @Override
    public List<BoardPos> neighbours() {
        List<BoardPos> result = new ArrayList<>();
        TilePos pos = step(1, 0);
        if (pos != TilePos.OFF_BOARD) result.add((BoardPos) pos);

        pos = step(-1, 0);
        if (pos != TilePos.OFF_BOARD) result.add((BoardPos) pos);

        pos = step(0, 1);
        if (pos != TilePos.OFF_BOARD) result.add((BoardPos) pos);

        pos = step(0, -1);
        if (pos != TilePos.OFF_BOARD) result.add((BoardPos) pos);

        return result;
    }

    // Checks if another position is directly adjacent to this one.
    @Override
    public boolean isNextTo(TilePos pos) {
        if (pos == TilePos.OFF_BOARD) return false;

        if (this.i == pos.i() && Math.abs(this.j - pos.j()) == 1) return true;

        if (this.j == pos.j() && Math.abs(this.i - pos.i()) == 1) return true;

        return false;
    }

    // Calculates a new position adjusted for the playing side, potentially flipping the direction for one side.
    @Override
    public TilePos stepByPlayingSide(Offset2D dir, PlayingSide side) {
        return side == PlayingSide.BLUE ? step(dir) : step(dir.yFlipped());
    }

    // Hash code based on both column and row indexes.
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + i;
        result = prime * result + j;
        return result;
    }

    // Compares this position to another for equality based on column and row indexes.
    @Override
    public boolean equalsTo(int i, int j) {
        return this.i == i && this.j == j;
    }

    // Checks if this object is equal to another, based on the column and row indexes.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BoardPos other = (BoardPos) obj;
        if (i != other.i) return false;
        if (j != other.j) return false;
        return true;
    }

    // Provides a string representation of the position in a human-readable format, such as "a1".
    @Override
    public String toString() {
        return String.format("%c%d", column(), row());
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append(JSONStringValue(toString()));
    }
}