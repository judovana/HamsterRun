package nonsense.hamsterrun;

import nonsense.hamsterrun.env.BlockField;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class UtilsTest {

    @Test
    public void clearTest() {
        BlockField[][] map = new BlockField[2][3];
        Utils.clear(map, true);
        Assertions.assertArrayEquals(new Boolean[]{true, true, true}, Arrays.stream(map[0]).map(a->a.isPassable()).toArray());
        Assertions.assertArrayEquals(new Boolean[]{true, true, true}, Arrays.stream(map[1]).map(a->a.isPassable()).toArray());

        map = new BlockField[3][2];
        Utils.clear(map, true);
        Assertions.assertArrayEquals(new Boolean[]{true, true},  Arrays.stream(map[0]).map(a->a.isPassable()).toArray());
        Assertions.assertArrayEquals(new Boolean[]{true, true},  Arrays.stream(map[1]).map(a->a.isPassable()).toArray());
        Assertions.assertArrayEquals(new Boolean[]{true, true},  Arrays.stream(map[2]).map(a->a.isPassable()).toArray());
    }

    @Test
    public void columnTest() {
        BlockField[][] map = new BlockField[2][2];
        Utils.clear(map, false);
        Utils.column(map, 0, true);
        Assertions.assertEquals(true, map[0][0].isPassable());
        Assertions.assertEquals(true, map[1][0].isPassable());
        Assertions.assertEquals(false, map[0][1].isPassable());
        Assertions.assertEquals(false, map[1][1].isPassable());
        Utils.column(map, 0, false);
        Utils.column(map, 1, true);
        Assertions.assertEquals(false, map[0][0].isPassable());
        Assertions.assertEquals(false, map[1][0].isPassable());
        Assertions.assertEquals(true, map[0][1].isPassable());
        Assertions.assertEquals(true, map[1][1].isPassable());
    }

    @Test
    public void rowTest() {
        BlockField[][] map = new BlockField[2][2];
        Utils.clear(map, false);
        Utils.row(map, 0, true);
        Assertions.assertEquals(true, map[0][0].isPassable());
        Assertions.assertEquals(true, map[0][1].isPassable());
        Assertions.assertEquals(false, map[1][1].isPassable());
        Assertions.assertEquals(false, map[1][0].isPassable());
        Utils.row(map, 0, false);
        Utils.row(map, 1, true);
        Assertions.assertEquals(false, map[0][0].isPassable());
        Assertions.assertEquals(false, map[0][1].isPassable());
        Assertions.assertEquals(true, map[1][1].isPassable());
        Assertions.assertEquals(true, map[1][0].isPassable());
    }

    @Test
    public void rowColumnTest() {
        BlockField[][] map = new BlockField[2][2];
        Utils.clear(map, false);
        Utils.row(map, 0, true);
        Utils.column(map, 0, true);
        Assertions.assertEquals(true, map[0][0].isPassable());
        Assertions.assertEquals(true, map[0][1].isPassable());
        Assertions.assertEquals(false, map[1][1].isPassable());
        Assertions.assertEquals(true, map[1][0].isPassable());
        Utils.clear(map, false);
        Utils.row(map, 1, true);
        Utils.column(map, 1, true);
        Assertions.assertEquals(false, map[0][0].isPassable());
        Assertions.assertEquals(true, map[0][1].isPassable());
        Assertions.assertEquals(true, map[1][1].isPassable());
        Assertions.assertEquals(true, map[1][0].isPassable());
    }
}