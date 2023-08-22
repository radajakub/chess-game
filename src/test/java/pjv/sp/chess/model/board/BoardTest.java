package pjv.sp.chess.model.board;

import pjv.sp.chess.model.pieces.Color;
import pjv.sp.chess.model.pieces.Empty;
import pjv.sp.chess.model.pieces.King;
import pjv.sp.chess.model.pieces.Knight;
import pjv.sp.chess.model.pieces.Pawn;
import pjv.sp.chess.model.pieces.Position;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.framework.junit.TestFXRule;

import static org.junit.Assert.*;

public class BoardTest extends ApplicationTest {

    @Rule public TestFXRule rule = new TestFXRule();

    // create standard board
    private Board board = new Board(false);

    @Ignore
    @Test
    public void testIsStandard() {
        assertEquals(this.board.getPiece(File.A, Rank.EIGHT).getClass(), Pawn.class);
        assertEquals(this.board.getPiece(File.A, Rank.EIGHT).getClass(), Color.BLACK);
        assertEquals(this.board.getPiece(File.E, Rank.EIGHT).getClass(), King.class);
        assertEquals(this.board.getPiece(File.E, Rank.EIGHT).getClass(), Color.BLACK);
    }

    @Ignore
    @Test
    public void testMove() {
        Position src = new Position(File.B, Rank.ONE);
        Position tgt = new Position(File.A, Rank.THREE);
        this.board.movePiece(src, tgt, 1);
        assertEquals(this.board.getPiece(src).getClass(), Empty.class);
        assertEquals(this.board.getPiece(tgt).getClass(), Knight.class);
        assertEquals(this.board.getPiece(tgt).getColor(), Color.WHITE);
    }

}