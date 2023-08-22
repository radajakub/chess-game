package pjv.sp.chess.model;

import pjv.sp.chess.model.board.Board;
import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.player.ComputerPlayer;
import pjv.sp.chess.model.player.HumanPlayer;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

public class GameTest {

    private final Game game = new Game(
        "test",
        "test",
        new HumanPlayer("human", "player", Color.WHITE),
        new ComputerPlayer("computer", "player", Color.BLACK),
        new Board(false)
    );

    @Ignore
    @Test
    public void testNextRound() {
        this.game.setPlayerToPlay(Color.WHITE);
        int round = this.game.getRound();
        this.game.nextRound();
        assertEquals(game.getPlayerToPlay().getColor(), Color.BLACK);
        assertEquals(game.getRound(), round + 1);
    }

}