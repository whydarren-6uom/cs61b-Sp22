import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] input = { {1, 3, 4}, {1}, {5, 6, 7, 8}, {7, 9} };
        assertEquals(9, MultiArr.maxValue(input));
    }

    @Test
    public void testAllRowSums() {
        int[][] input = { {1, 3, 4}, {1}, {5, 6, 7, 8}, {7, 9} };
        int[] actual = MultiArr.allRowSums(input);
        int[] expected = { 8, 1, 26, 16 };
        for (int i=0;i<actual.length;i++) {
            assertEquals(expected[i], actual[i]);
        }
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
