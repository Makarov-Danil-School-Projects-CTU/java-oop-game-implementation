package thedrake;

import java.util.ArrayList;
import java.util.List;

public class StrikeAction extends TroopAction {

    public StrikeAction(Offset2D offset) {
        super(offset);
    }

    public StrikeAction(int offsetX, int offsetY) {
        super(new Offset2D(offsetX, offsetY));
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> moves = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        if (state.canCapture(origin, target)) {
            moves.add(new CaptureOnly(origin, (BoardPos) target));
        }

        return moves;
    }
}