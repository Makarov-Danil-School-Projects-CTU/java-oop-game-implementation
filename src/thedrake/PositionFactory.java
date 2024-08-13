package thedrake;

/**
 * Factory class for creating BoardPos instances based on a specified board dimension.
 * This class ensures that all positions created are valid within the context of the given board size.
 */
public class PositionFactory {
    // The size of one side of the square game board.
    private final int dimension;

    /**
     * Constructs a PositionFactory with a specified board dimension.
     *
     * @param dimension The size of the board's sides; must be a positive number.
     * @throws IllegalArgumentException if the provided dimension is negative.
     */
    public PositionFactory(int dimension) {
        if (dimension < 0) throw new IllegalArgumentException("The dimension needs to be positive.");

        this.dimension = dimension;
    }

    /**
     * Returns the dimension of the board associated with this factory.
     *
     * @return The board dimension.
     */
    public int dimension() {
        return dimension;
    }

    /**
     * Creates a new BoardPos based on column and row indexes.
     *
     * @param i The column index (0-based).
     * @param j The row index (0-based).
     * @return A new BoardPos object representing the specified position on the board.
     */
    public BoardPos pos(int i, int j) {
        return new BoardPos(dimension, i, j);
    }

    /**
     * Creates a new BoardPos based on a column letter and a row number.
     *
     * @param column The column represented as a letter ('a', 'b', 'c', ...).
     * @param row    The row number (1-based).
     * @return A new BoardPos object representing the specified position on the board.
     */
    public BoardPos pos(char column, int row) {
        return pos(iFromColumn(column), jFromRow(row));
    }

    /**
     * Creates a new BoardPos from a string representation, such as "a1".
     *
     * @param pos The position string, with the first character as the column and the remaining part as the row number.
     * @return A new BoardPos object representing the specified position on the board.
     */
    public BoardPos pos(String pos) {
        return pos(pos.charAt(0), Integer.parseInt(pos.substring(1)));
    }

    /**
     * Converts a column letter to a 0-based column index.
     *
     * @param column The column letter ('a', 'b', 'c', ...).
     * @return The 0-based column index.
     */
    private int iFromColumn(char column) {
        return column - 'a';
    }

    /**
     * Converts a 1-based row number to a 0-based row index.
     *
     * @param row The row number (1-based).
     * @return The 0-based row index.
     */
    private int jFromRow(int row) {
        return row - 1;
    }
}
