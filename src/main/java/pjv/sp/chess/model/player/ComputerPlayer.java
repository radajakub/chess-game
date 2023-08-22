package pjv.sp.chess.model.player;

import pjv.sp.chess.model.Game;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Move;
import pjv.sp.chess.model.pieces.Piece;

import java.util.List;
import java.util.Random;

/**
 * ComputerPlayer is extension of player that from passed game returns next move
 * to make with pieces of his color. This one makes random valid moves.
 * @author Jakub Rada
 * @version 1.0
 * @see Player
 */
public class ComputerPlayer extends Player {

    /**
     * Randomizer to chooes random values
     */
    private final Random random = new Random();

    /**
     * Create new CompuerPlayer object
     * @param name Name of the player
     * @param surname Surname of the player
     * @param color Color of the player
     * @see Color
     */
    public ComputerPlayer(String name, String surname, Color color) {
        super(name, surname, color, true);
    }

    /**
     * Gets CompleteMove object that contains piece and move to be performed on the board
     * @param game Game to select move from
     */
    @Override
    public CompleteMove getCompleteMove(Game game) {
        List<Piece> pieces = game.getPiecesOfColor(this.color);
        int randomIndex;
        Piece piece;
        int moveCount = 0;
        List<Move> moves;
        do {
            randomIndex = this.random.nextInt(pieces.size());
            piece = pieces.get(randomIndex);
            moves = game.getMoves(piece);
            moveCount = moves.size();
        } while (moveCount == 0);
        randomIndex = this.random.nextInt(moves.size());
        return new CompleteMove(piece, moves.get(randomIndex));
    }

}