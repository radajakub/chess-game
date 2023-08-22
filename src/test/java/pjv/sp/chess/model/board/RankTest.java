package pjv.sp.chess.model.board;

import org.junit.Test;

import static org.junit.Assert.*;

public class RankTest {

    @Test
    public void testPreviousFirst() {
        assertEquals(Rank.ONE.getPrevious(), null);
    }

    @Test
    public void testPreviousMiddle() {
        assertEquals(Rank.THREE.getPrevious(), Rank.TWO);
    }

    @Test
    public void testPreviousLast() {
        assertEquals(Rank.EIGHT.getPrevious(), Rank.SEVEN);
    }

    @Test
    public void testNextFirst() {
        assertEquals(Rank.ONE.getNext(), Rank.TWO);
    }

    @Test
    public void testNextMiddle() {
        assertEquals(Rank.FOUR.getNext(), Rank.FIVE);
    }

    @Test
    public void testNextLast() {
        assertEquals(Rank.EIGHT.getNext(), null);
    }

    @Test
    public void testFindByLabelCorrect() {
        assertEquals(Rank.getRankOfLabel("5"), Rank.FIVE);
    }

    @Test
    public void testFindByLabelOutOfRange() {
        assertEquals(Rank.getRankOfLabel("0"), null);
    }

    @Test
    public void testFindByValueCorrect() {
        assertEquals(Rank.getRankOfValue(3), Rank.FIVE);
    }

    @Test
    public void testFindByValueOutOfRange() {
        assertEquals(Rank.getRankOfValue(69), null);
    }

}