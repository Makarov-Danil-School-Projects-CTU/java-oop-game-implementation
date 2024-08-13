package thedrake;

public class Offset2D {
    // The x-coordinate of the offset.
    public final int x;
    // The y-coordinate of the offset.
    public final int y;

    /**
     * Constructs an Offset2D object with x and y coordinates.
     *
     * @param x The x-coordinate of the offset.
     * @param y The y-coordinate of the offset.
     */
    public Offset2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Checks if the offset equals the specified coordinates.
     *
     * @param x The x-coordinate to compare with this offset's x-coordinate.
     * @param y The y-coordinate to compare with this offset's y-coordinate.
     * @return true if both x and y coordinates match, false otherwise.
     */
    public boolean equalsTo(int x, int y) {
        return this.x == x && this.y == y;
    }

    /**
     * Creates a new Offset2D object with the same x-coordinate and the negated y-coordinate.
     *
     * @return A new Offset2D object with y-coordinate flipped.
     */
    public Offset2D yFlipped() {
        return new Offset2D(this.x, -this.y);
    }
}


