package thedrake;

import java.io.PrintWriter;

/**
 * Represents the game board with a specified dimension. The board is composed of tiles.
 */
public class Board implements JSONSerializable {
    // A 2D array holding the tiles of the board.
    private final BoardTile[][] boardTile;
    // The dimension of the square board.
    private final int dimension;

    /**
     * Constructs a new Board with the given dimension. Initializes all tiles to be EMPTY.
     *
     * @param dimension The size of one side of the square board.
     */
    public Board(int dimension) {
        this.dimension = dimension;
        this.boardTile = new BoardTile[dimension][dimension];

        // Initialize all positions on the board as empty.
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                this.boardTile[i][j] = BoardTile.EMPTY;
    }

    /**
     * Returns the dimension of the board.
     *
     * @return The size of the board's sides.
     */
    public int dimension() {
        return dimension;
    }

    /**
     * Retrieves the tile at the specified position.
     *
     * @param pos The position of the tile to retrieve.
     * @return The tile at the given position, or null if the position is out of bounds.
     */
    public BoardTile at(TilePos pos) {
        if (pos.i() < 0 || pos.i() >= dimension || pos.j() < 0 || pos.j() >= dimension) return null;

        return boardTile[pos.i()][pos.j()];
    }

    /**
     * Creates a new Board instance with specified tiles updated, leaving other tiles unchanged.
     *
     * @param ats An array of TileAt, each specifying a position and the tile to place there.
     * @return A new Board instance with the specified tiles updated.
     */
    public Board withTiles(TileAt... ats) {
        Board newBoard = new Board(this.dimension);

        // Copy the current board configuration to the new board.
        for (int i = 0; i < this.dimension; i++)
            System.arraycopy(this.boardTile[i], 0, newBoard.boardTile[i], 0, this.dimension);

        // Update the specified tiles on the new board.
        for (TileAt at : ats)
            newBoard.boardTile[at.pos.i()][at.pos.j()] = at.tile;

        return newBoard;
    }

    /**
     * Creates a PositionFactory for this board, which can create positions within this board's dimensions.
     *
     * @return A new PositionFactory instance configured for this board's dimension.
     */
    public PositionFactory positionFactory() {
        return new PositionFactory(this.dimension);
    }

    /**
     * A helper class that represents a tile and its position on the board.
     */
    public static class TileAt {
        // The position of the tile on the board.
        public final BoardPos pos;
        // The tile to be placed at the position.
        public final BoardTile tile;

        /**
         * Constructs a TileAt with a specified position and tile.
         *
         * @param pos  The position of the tile.
         * @param tile The tile to be placed.
         */
        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.append('{');
        writer.append(JSONKeyValue("dimension")).append(Integer.toString(dimension())).append(",");
        writer.append(JSONKeyValue("tiles")).append('[');

        for (int i = 0; i < dimension(); ++i) {
            for (int j = 0; j < dimension(); ++j) {
                boardTile[j][i].toJSON(writer);

                if (i + 1 < dimension() || j + 1 < dimension()) {
                    writer.append(',');
                }
            }
        }

        writer.append(']').append('}');
    }

}
