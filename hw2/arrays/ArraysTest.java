package arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Darren Wang
 */

public class ArraysTest {

    @Test
    public void catenateTest() {
        int[] A = {1, 2, 3, 4, 5};
        int[] B = {6, 7, 8, 9, 61};
        int[] result = {1, 2, 3, 4, 5, 6, 7, 8, 9, 61};
        assertTrue(Utils.equals(result, Arrays.catenate(A, B)));
    }

    @Test
    public void removeTest() {
        int[] A = {1, 2, 3, 4, 5};
        int[] result = {1, 4, 5};
        assertTrue(Utils.equals(result, Arrays.remove(A, 1, 2)));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
