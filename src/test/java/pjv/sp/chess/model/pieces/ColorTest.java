package pjv.sp.chess.model.pieces;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorTest {

    @Test
    public void getOppositeColorWhite() {
        assertEquals(Color.WHITE.getOppositeColor(), Color.BLACK);
    }

    @Test
    public void getOppositeColorBlack() {
        assertEquals(Color.BLACK.getOppositeColor(), Color.WHITE);
    }

    @Test
    public void getOppositeColorEmpty() {
        assertEquals(Color.EMPTY.getOppositeColor(), Color.EMPTY);
    }

}