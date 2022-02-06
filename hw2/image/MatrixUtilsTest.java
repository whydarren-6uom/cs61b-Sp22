package image;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  @author Darren Wang
 */

public class MatrixUtilsTest {

    @Test
    public void accumulateVerticalTest() {
        double[][] input = {{1000000, 1000000, 1000000, 1000000},
            {1000000, 75990, 30003, 1000000},
            {1000000, 30002, 103046, 1000000},
            {1000000, 29515, 38273, 1000000},
            {1000000, 73403, 35399, 1000000},
            {1000000, 1000000, 1000000, 1000000}};
        double[][] result = {{1000000, 1000000, 1000000, 1000000},
            {2000000, 1075990, 1030003, 2000000},
            {2075990, 1060005, 1133049, 2030003},
            {2060005, 1089520, 1098278, 2133049},
            {2089520, 1162923, 1124919, 2098278},
            {2162923, 2124919, 2124919, 2124919}};
        double[][] output = MatrixUtils.accumulateVertical(input);

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                assertEquals(output[i][j], result[i][j], 3);
            }
        }
    }

    @Test
    public void accumulateTest() {

        double[][] input = {{1000000, 1000000, 1000000, 1000000},
            {1000000, 75990, 30003, 1000000},
            {1000000, 30002, 103046, 1000000},
            {1000000, 29515, 38273, 1000000},
            {1000000, 73403, 35399, 1000000},
            {1000000, 1000000, 1000000, 1000000}};
        double[][] result = {{1000000, 1000000, 1000000, 1000000},
            {2000000, 1075990, 1030003, 2000000},
            {2075990, 1060005, 1133049, 2030003},
            {2060005, 1089520, 1098278, 2133049},
            {2089520, 1162923, 1124919, 2098278},
            {2162923, 2124919, 2124919, 2124919}};
        double[][] output = MatrixUtils.accumulate(input, MatrixUtils.Orientation.VERTICAL);

        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[0].length; j++) {
                assertEquals(output[i][j], result[i][j], 3);
            }
        }
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
