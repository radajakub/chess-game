package pjv.sp.chess.model.board;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileTest {

    @Test
    public void testPreviousFirst() {
        assertEquals(File.A.getPrevious(), null);
    }

    @Test
    public void testPreviousMiddle() {
        assertEquals(File.C.getPrevious(), File.B);
    }

    @Test
    public void testPreviousLast() {
        assertEquals(File.H.getPrevious(), File.G);
    }

    @Test
    public void testNextFirst() {
        assertEquals(File.A.getNext(), File.B);
    }

    @Test
    public void testNextMiddle() {
        assertEquals(File.D.getNext(), File.E);
    }

    @Test
    public void testNextLast() {
        assertEquals(File.H.getNext(), null);
    }

    @Test
    public void testFindByValueCorrect() {
        assertEquals(File.getFileOfValue(3), File.D);
    }

    @Test
    public void testFindByValueOutOfRange() {
        assertEquals(File.getFileOfValue(10), null);
    }

    @Test
    public void testFindByLabelCorrect() {
        assertEquals(File.getFileOfLabel("b"), File.B);
    }

    @Test
    public void testFindByLabelOutOfRange() {
        assertEquals(File.getFileOfLabel("q"), null);
    }

}