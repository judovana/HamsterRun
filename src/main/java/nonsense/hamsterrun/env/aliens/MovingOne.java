package nonsense.hamsterrun.env.aliens;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

public abstract class MovingOne {

    protected static final Random seed = new Random();
    protected static final int MAGICAL_FALL_CHANCE = 6;
    protected static final int relativeSizes = 5;
    protected AnimationCounrer anim = new AnimationCounrer();
    protected RatActions action = RatActions.STAY;
    protected RatActions.Direction direction = RatActions.Direction.getRandom();
    protected Point relativeCoordInSquare = new Point(0, 0);
    protected int speed = 1; //can not go over relativeSizes*2
    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);

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

    public static Point toUniversalCoords(Point coordsInMaze, Point coordsInBaseBlock) {
        return new Point(coordsInMaze.x * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.x,
                coordsInMaze.y * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.y);
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

    public void setUniversalCoords(Point target) {
        coordsInMaze.x = target.x / BaseConfig.getConfig().getBaseSize();
        coordsInMaze.y = target.y / BaseConfig.getConfig().getBaseSize();
        coordsInBaseBlock.x = target.x % BaseConfig.getConfig().getBaseSize();
        coordsInBaseBlock.y = target.y % BaseConfig.getConfig().getBaseSize();
    }


    protected void forceMouseRight() {
        forceMouseRight(1);
    }

    protected void forceMouseUp() {
        forceMouseUp(1);
    }

    protected void forceMouseLeft() {
        forceMouseLeft(1);
    }

    protected void forceMouseDown() {
        forceMouseDown(1);
    }

    protected void forceMouseRight(int i) {
        coordsInBaseBlock.x+=i;
        if (coordsInBaseBlock.x >= BaseConfig.getConfig().getBaseSize()) {
            coordsInMaze.x++;
            coordsInBaseBlock.x = 0;
        }
    }

    protected void forceMouseUp(int i) {
        coordsInBaseBlock.y-=i;
        if (coordsInBaseBlock.y < 0) {
            coordsInMaze.y--;
            coordsInBaseBlock.y = BaseConfig.getConfig().getBaseSize() - 1;
        }
    }

    protected void forceMouseLeft(int i) {
        coordsInBaseBlock.x-=i;
        if (coordsInBaseBlock.x < 0) {
            coordsInMaze.x--;
            coordsInBaseBlock.x = BaseConfig.getConfig().getBaseSize() - 1;
        }
    }

    protected void forceMouseDown(int i) {
        coordsInBaseBlock.y+=i;
        if (coordsInBaseBlock.y >= BaseConfig.getConfig().getBaseSize()) {
            coordsInMaze.y++;
            coordsInBaseBlock.y = 0;
        }
    }

    protected void reallyMoveMouseRight(World world) {
        if (world.isEnterable(getUniversalCoords(), 1, 0, getMouseBlock())) {
            forceMouseRight();
        }
    }

    protected void reallyMoveMouseUp(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, -1, getMouseBlock())) {
            forceMouseUp();
        }
    }

    protected void reallyMoveMouseLeft(World world) {
        if (world.isEnterable(getUniversalCoords(), -1, 0, getMouseBlock())) {
            forceMouseLeft();
        }
    }

    protected void reallyMoveMouseDown(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, 1, getMouseBlock())) {
            forceMouseDown();
        }
    }

    /**
     *
     * @return true, if movement of this member should be blocekd by rat
     */
    protected boolean getMouseBlock() {
        return false;
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

    public RatActions getAction() {
        return action;
    }

    public void setAction(RatActions ratActions) {
        action = ratActions;
    }


    public void setMouseUp(World world) {
        if (returnOnSalat(world)) {
            return;
        }
        setActionDirection(RatActions.WALK, RatActions.Direction.UP);
    }

    public void setMouseLeft(World world) {
        if (returnOnSalat(world)) {
            return;
        }
        setActionDirection(RatActions.WALK, RatActions.Direction.LEFT);
    }

    public void setMouseDown(World world) {
        if (returnOnSalat(world)) {
            return;
        }
        setActionDirection(RatActions.WALK, RatActions.Direction.DOWN);
    }

    public void setMouseRight(World world) {
        if (returnOnSalat(world)) {
            return;
        }
        setActionDirection(RatActions.WALK, RatActions.Direction.RIGHT);
    }

    public void setActionDirection(RatActions action, RatActions.Direction direction) {
        adjustSpeedBeforeActionDirection();
        this.action = action;
        this.direction = direction;
    }

    //useInplaceSubMovement - in map false, in game true
    public void draw(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, boolean useInplaceSubMovement, boolean higlight) {
        Point coord = getUniversalCoords();
        Point relativeShift = new Point(0, 0);
        if (useInplaceSubMovement) {
            //it goes from (relativeSizes..0...-relativeSizes) (without the edges)
            //so for relativeSizes 1 the total to walk is 0
            //so for relativeSizes 2 the total to walk is 3 (1,0,-1)...
            //so for relativeSizes 5 the total to walk is 9 (4,3,2,1,0,-1,-2,-3,-4)...
            float relativeSizesCalc = relativeSizes * 2 - 1;
            float step = zoom / relativeSizesCalc;
            //so for relativeSizes 5 the it goes from 1 to 9 inclusive
            float relativeX = (relativeCoordInSquare.x) * step;
            float relativeY = (relativeCoordInSquare.y) * step;
            relativeShift.x = (int) relativeX;
            relativeShift.y = (int) relativeY;
            BufferedImage img = getImageForAction(getSkin());
            int usedZoom = zoom;
            if (action == RatActions.FALLING) {
                usedZoom = Math.max(1, zoom - (zoom / 50 + 1) * anim.anim);
            }
            g2d.drawImage(img, leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, usedZoom, usedZoom, null);
        } else {
            g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, zoom, zoom);
            if (higlight) {
                g2d.setColor(Color.red);
                g2d.drawOval(leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x - anim.modMap(), leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y - anim.modMap(), zoom + 2 * anim.modMap(), zoom + 2 * +anim.modMap());

            }
        }
    }

    public void move(World world){
        switch (direction) {
            case DOWN:
                moveMouseDown(world);
                break;
            case UP:
                moveMouseUp(world);
                break;
            case LEFT:
                moveMouseLeft(world);
                break;
            case RIGHT:
                moveMouseRight(world);
                break;
        }
    }


    protected BufferedImage getImageForAction(String skin) {
        return SpritesProvider.getAlien(getSkin(), anim.anim);
    }

    public boolean mustBeInCorridor() {
        return true;
    }

    public abstract void interact(Rat rat);
    protected abstract boolean returnOnSalat(World world);
    public abstract void selfAct(World world);
    protected abstract void adjustSpeedBeforeActionDirection();
    protected abstract String getSkin();

    //usual act is only in some occurences of REPL, if you need each turn, use this
    //eg for super quick animations...
    public void unfilteredAct(World world) {
    }
}
