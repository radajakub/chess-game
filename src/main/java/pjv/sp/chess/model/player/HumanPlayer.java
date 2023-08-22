package pjv.sp.chess.model.player;

import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.board.File;
import pjv.sp.chess.model.board.Rank;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.MoveType;
import pjv.sp.chess.model.pieces.Position;

/**
 * HumanPlayer is extension of Player that represents human who can physically click
 * on the screen.
 * @author Jakub Rada
 * @version 1.0
 * @see Player
 */
public class HumanPlayer extends Player {

    /**
     * Create new HumanPlayer object
     * @param name name of the player
     * @param surname surname of the player
     * @param color Color of the player
     * @see Color
     */
    public HumanPlayer(String name, String surname, Color color) {
        super(name, surname, color, false);
    }

    /**
     * This function is redundant and is not used, is here only to comply with the Player abstract class
     */
    @Override
    public CompleteMove getCompleteMove(Game game) {
        Position position = new Position(File.A, Rank.ONE);
        return new CompleteMove(new Empty(position), new Move(position, MoveType.NORMAL));
    }

}