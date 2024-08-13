package thedrake;

import java.util.ArrayList;
import java.util.List;

public class SlideAction extends TroopAction {

    public SlideAction(Offset2D offset) {
        super(offset);
    }

    public SlideAction(int offsetX, int offsetY) {
        super(new Offset2D(offsetX, offsetY));
    }

    @Override
    public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
        List<Move> moves = new ArrayList<>();
        TilePos target = origin.stepByPlayingSide(offset(), side);

        // Attempts to generate moves until reaching edge or encountering an obstacle
        while (target instanceof BoardPos) {
            if (state.canStep(origin, target)) {                        // Move case
                moves.add(new StepOnly(origin, (BoardPos) target));
            } else if (state.canCapture(origin, target)) {              // Capture case
                moves.add(new StepAndCapture(origin, (BoardPos) target));
                break;
            } else {                                                    // Nothing
                break;
            }

            // Next target position based on the current
            target = target.stepByPlayingSide(offset(), side);
        }

        return moves;
    }
}