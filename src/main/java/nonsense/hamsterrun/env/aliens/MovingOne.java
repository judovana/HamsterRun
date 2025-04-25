package nonsense.hamsterrun.env.aliens;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;

import java.awt.Point;
import java.util.Random;

public abstract class MovingOne {

    protected static final Random seed = new Random();
    protected static final int MAGICAL_FALL_CHANCE = 6;
    protected static final int relativeSizes = 5;
    protected AnimationCounrer anim = new AnimationCounrer();
    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);
    protected RatActions action = RatActions.STAY;
    protected RatActions.Direction direction = RatActions.Direction.UP;
    protected Point relativeCoordInSquare = new Point(0, 0);
    protected int speed = 1; //can not go over relativeSizes*2

    public MovingOne() {
    }

    public MovingOne(Point world, Point block) {
        this.coordsInBaseBlock = block;
        this.coordsInMaze = world;
    }

    public MovingOne(int worldx, int worldy, int blockx, int blocky) {
        this.coordsInBaseBlock = new Point(blockx, blocky);
        this.coordsInMaze = new Point(worldx, worldy);
    }


    public Point getCoordsInBaseBlock() {
        return coordsInBaseBlock;
    }

    public void setCoordsInBaseBlock(Point coordsInBaseBlock) {
        this.coordsInBaseBlock = coordsInBaseBlock;
    }

    public Point getCoordsInMaze() {
        return coordsInMaze;
    }

    public void setCoordsInMaze(Point coordsInMaze) {
        this.coordsInMaze = coordsInMaze;
    }

    public void setCoordsInBaseBlock(int x, int y) {
        this.coordsInBaseBlock = new Point(x, y);
    }

    public void setCoordsInMaze(int x, int y) {
        this.coordsInMaze = new Point(x, y);
    }

    public Point getUniversalCoords() {
        return toUniversalCoords(coordsInMaze, coordsInBaseBlock);
    }

    public static Point toUniversalCoords(Point coordsInMaze, Point coordsInBaseBlock) {
        return new Point(coordsInMaze.x * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.x,
                coordsInMaze.y * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.y);
    }


    public void setUniversalCoords(Point target) {
        coordsInMaze.x = target.x / BaseConfig.getConfig().getBaseSize();
        coordsInMaze.y = target.y / BaseConfig.getConfig().getBaseSize();
        coordsInBaseBlock.x = target.x % BaseConfig.getConfig().getBaseSize();
        coordsInBaseBlock.y = target.y % BaseConfig.getConfig().getBaseSize();
    }


    protected void forceMouseRight() {
        coordsInBaseBlock.x++;
        if (coordsInBaseBlock.x >= BaseConfig.getConfig().getBaseSize()) {
            coordsInMaze.x++;
            coordsInBaseBlock.x = 0;
        }
    }

    protected void forceMouseUp() {
        coordsInBaseBlock.y--;
        if (coordsInBaseBlock.y < 0) {
            coordsInMaze.y--;
            coordsInBaseBlock.y = BaseConfig.getConfig().getBaseSize() - 1;
        }
    }

    protected void forceMouseLeft() {
        coordsInBaseBlock.x--;
        if (coordsInBaseBlock.x < 0) {
            coordsInMaze.x--;
            coordsInBaseBlock.x = BaseConfig.getConfig().getBaseSize() - 1;
        }
    }

    protected void forceMouseDown() {
        coordsInBaseBlock.y++;
        if (coordsInBaseBlock.y >= BaseConfig.getConfig().getBaseSize()) {
            coordsInMaze.y++;
            coordsInBaseBlock.y = 0;
        }
    }

    protected void reallyMoveMouseRight(World world) {
        if (world.isEnterable(getUniversalCoords(), 1, 0)) {
            forceMouseRight();
        }
    }

    protected void reallyMoveMouseUp(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, -1)) {
            forceMouseUp();
        }
    }

    protected void reallyMoveMouseLeft(World world) {
        if (world.isEnterable(getUniversalCoords(), -1, 0)) {
            forceMouseLeft();
        }
    }

    protected void reallyMoveMouseDown(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, 1)) {
            forceMouseDown();
        }
    }

    protected void moveMouseRight(World world) {
        relativeCoordInSquare.x += speed;
        if (relativeCoordInSquare.y < 0) {
            relativeCoordInSquare.y++;
        }
        if (relativeCoordInSquare.y > 0) {
            relativeCoordInSquare.y--;
        }
        if (relativeCoordInSquare.x >= relativeSizes) {
            relativeCoordInSquare.x = -relativeSizes;
            reallyMoveMouseRight(world);
        }
    }

    protected void moveMouseUp(World world) {
        relativeCoordInSquare.y -= speed;
        if (relativeCoordInSquare.x < 0) {
            relativeCoordInSquare.x++;
        }
        if (relativeCoordInSquare.x > 0) {
            relativeCoordInSquare.x--;
        }
        if (relativeCoordInSquare.y <= -relativeSizes) {
            relativeCoordInSquare.y = relativeSizes;
            reallyMoveMouseUp(world);
        }
    }

    protected void moveMouseLeft(World world) {
        relativeCoordInSquare.x -= speed;
        if (relativeCoordInSquare.y < 0) {
            relativeCoordInSquare.y++;
        }
        if (relativeCoordInSquare.y > 0) {
            relativeCoordInSquare.y--;
        }
        if (relativeCoordInSquare.x <= -relativeSizes) {
            relativeCoordInSquare.x = relativeSizes;
            reallyMoveMouseLeft(world);
        }
    }

    protected void moveMouseDown(World world) {
        relativeCoordInSquare.y += speed;
        if (relativeCoordInSquare.x < 0) {
            relativeCoordInSquare.x++;
        }
        if (relativeCoordInSquare.x > 0) {
            relativeCoordInSquare.x--;
        }
        if (relativeCoordInSquare.y >= relativeSizes) {
            relativeCoordInSquare.y = -relativeSizes;
            reallyMoveMouseDown(world);
        }
    }

    public void setAction(RatActions ratActions) {
        action = ratActions;
    }
    public RatActions getAction() {
        return action;
    }
}
