package thedrake;

import java.io.PrintWriter;
import java.util.Optional;

public class GameState implements JSONSerializable {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(Board board, Army blueArmy, Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(Board board, Army blueArmy, Army orangeArmy, PlayingSide sideOnTurn, GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE) return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(TilePos pos) {
        Optional<TroopTile> orangeTroopTile = orangeArmy.boardTroops().at(pos);

        if (orangeTroopTile.isPresent()) {
            return orangeTroopTile.get();
        }

        Optional<TroopTile> blueTroopTile = blueArmy.boardTroops().at(pos);

        if (blueTroopTile.isPresent()) {
            return blueTroopTile.get();
        }

        return board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        // Game isn't in progress or off board or blue/orange is placing guards
        if (result != GameResult.IN_PLAY || origin == TilePos.OFF_BOARD || orangeArmy.boardTroops().isPlacingGuards() || blueArmy.boardTroops().isPlacingGuards()) {
            return false;
        }

        return armyOnTurn().boardTroops().at(origin).isPresent();
    }

    private boolean canStepTo(TilePos target) {
        if (target == TilePos.OFF_BOARD) {
            return false;
        }

        return result == GameResult.IN_PLAY && tileAt(target).canStepOn();
    }


    private boolean canCaptureOn(TilePos target) {
        if (target == TilePos.OFF_BOARD || result != GameResult.IN_PLAY) {
            return false;
        }

        return armyNotOnTurn().boardTroops().at(target).isPresent();
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        // Target position is valid or game is in progress or troops are left to place or can't step on a tile
        if (target == TilePos.OFF_BOARD || result != GameResult.IN_PLAY || armyOnTurn().stack().isEmpty() || !canStepTo(target)) {
            return false;
        }

        // The game starts
        if (!armyOnTurn().boardTroops().isLeaderPlaced()) {
            int lastRow = board.dimension();
            return target.row() == (sideOnTurn == PlayingSide.BLUE ? 1 : lastRow);
        }

        // Check whether guards are adjacent to the leader
        if (armyOnTurn().boardTroops().isPlacingGuards()) {
            return target.neighbours().contains(armyOnTurn().boardTroops().leaderPosition());
        }

        // Check whether guards are adjacent to any guard
        return target.neighbours().stream().anyMatch(pos -> armyOnTurn().boardTroops().at(pos).isPresent());
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(armyNotOnTurn(), armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target)) newResult = GameResult.VICTORY;

            return createNewGameState(armyNotOnTurn().removeTroop(target), armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target)) newResult = GameResult.VICTORY;

            return createNewGameState(armyNotOnTurn().removeTroop(target), armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(armyNotOnTurn(), armyOnTurn().placeFromStack(target), GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(armyNotOnTurn(), armyOnTurn(), GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(armyOnTurn(), armyNotOnTurn(), GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }
    @Override
    public void toJSON(PrintWriter writer) {
        writer.append('{');
        writer.append(JSONKeyValue("result"));

        result.toJSON(writer);

        writer.append(',');
        writer.append(JSONKeyValue("board"));

        board.toJSON(writer);

        writer.append(',');
        writer.append(JSONKeyValue("blueArmy"));

        blueArmy.toJSON(writer);

        writer.append(',');
        writer.append(JSONKeyValue("orangeArmy"));

        orangeArmy.toJSON(writer);

        writer.append('}');
    }
}
