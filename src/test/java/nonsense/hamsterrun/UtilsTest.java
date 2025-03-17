package nonsense.hamsterrun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilsTest {

    @Test
    public void clearTest() {
        int map[][] = new int[2][3];
        Utils.clear(map, 5);
        Assertions.assertArrayEquals(new int[]{5, 5, 5}, map[0]);
        Assertions.assertArrayEquals(new int[]{5, 5, 5}, map[1]);

        map = new int[3][2];
        Utils.clear(map, 5);
        Assertions.assertArrayEquals(new int[]{5, 5}, map[0]);
        Assertions.assertArrayEquals(new int[]{5, 5}, map[1]);
        Assertions.assertArrayEquals(new int[]{5, 5}, map[2]);
    }

    @Test
    public void columnTest() {
        int map[][] = new int[2][2];
        Utils.clear(map, 0);
        Utils.column(map, 0, 1);
        Assertions.assertEquals(1, map[0][0]);
        Assertions.assertEquals(1, map[1][0]);
        Assertions.assertEquals(0, map[0][1]);
        Assertions.assertEquals(0, map[1][1]);
        Utils.column(map, 0, 0);
        Utils.column(map, 1, 1);
        Assertions.assertEquals(0, map[0][0]);
        Assertions.assertEquals(0, map[1][0]);
        Assertions.assertEquals(1, map[0][1]);
        Assertions.assertEquals(1, map[1][1]);
    }

    @Test
    public void rowTest() {
        int map[][] = new int[2][2];
        Utils.clear(map, 0);
        Utils.row(map, 0, 1);
        Assertions.assertEquals(1, map[0][0]);
        Assertions.assertEquals(1, map[0][1]);
        Assertions.assertEquals(0, map[1][1]);
        Assertions.assertEquals(0, map[1][0]);
        Utils.row(map, 0, 0);
        Utils.row(map, 1, 1);
        Assertions.assertEquals(0, map[0][0]);
        Assertions.assertEquals(0, map[0][1]);
        Assertions.assertEquals(1, map[1][1]);
        Assertions.assertEquals(1, map[1][0]);
    }

    @Test
    public void rowColumnTest() {
        int map[][] = new int[2][2];
        Utils.clear(map, 0);
        Utils.row(map, 0, 1);
        Utils.column(map, 0, 1);
        Assertions.assertEquals(1, map[0][0]);
        Assertions.assertEquals(1, map[0][1]);
        Assertions.assertEquals(0, map[1][1]);
        Assertions.assertEquals(1, map[1][0]);
        Utils.clear(map, 0);
        Utils.row(map, 1, 1);
        Utils.column(map, 1, 1);
        Assertions.assertEquals(0, map[0][0]);
        Assertions.assertEquals(1, map[0][1]);
        Assertions.assertEquals(1, map[1][1]);
        Assertions.assertEquals(1, map[1][0]);
    }
}