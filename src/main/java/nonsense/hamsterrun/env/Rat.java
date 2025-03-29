package nonsense.hamsterrun.env;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.sprites.Rats;

public class Rat {

    private static final Random seed = new Random();
    private int anim;

    public enum Actions {
        STAY()/*FIXME needs stay left, right, up....*/, LEFT_NORMAL(3), RIGHT_NORMAL(1), UP_NORMAL(0), DOWN_NORMAL(2);

        private final int sprite;

        Actions() {
            sprite = 0;
        }

        Actions(int i) {
            sprite = i;
        }
    }

    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);
    private Actions action = Actions.STAY;
    private Point relativeCoordInSquare = new Point(0, 0);
    private static final int relativeSizes = 5;

    public Rat() {
    }

    public Rat(Point world, Point block) {
        this.coordsInBaseBlock = block;
        this.coordsInMaze = world;
    }

    public Rat(int worldx, int worldy, int blockx, int blocky) {
        this.coordsInBaseBlock = new Point(blockx, blocky);
        this.coordsInMaze = new Point(worldx, worldy);
    }

    public Point getCoordsInBaseBlock() {
        return coordsInBaseBlock;
    }

    public Point getCoordsInMaze() {
        return coordsInMaze;
    }

    public void setCoordsInBaseBlock(Point coordsInBaseBlock) {
        this.coordsInBaseBlock = coordsInBaseBlock;
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
        return new Point(coordsInMaze.x * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.x,
                coordsInMaze.y * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.y);
    }


    private void forceMouseRight() {
        coordsInBaseBlock.x++;
    }

    private void forceMouseUp() {
        coordsInBaseBlock.y--;
    }

    private void forceMouseLeft() {
        coordsInBaseBlock.x--;
    }

    private void forceMouseDown() {
        coordsInBaseBlock.y++;
    }

    private void reallyMoveMouseRight(World world) {
        if (world.isEnterable(getUniversalCoords(), 1, 0)) {
            forceMouseRight();
        }
    }

    private void reallyMoveMouseUp(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, -1)) {
            forceMouseUp();
        }
    }

    private void reallyMoveMouseLeft(World world) {
        if (world.isEnterable(getUniversalCoords(), -1, 0)) {
            forceMouseLeft();
        }
    }

    private void reallyMoveMouseDown(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, 1)) {
            forceMouseDown();
        }
    }

    //useInplaceSubMovement - in map false, in game true
    public void draw(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, boolean useInplaceSubMovement) {
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
            BufferedImage img = getImageForAction();
            g2d.drawImage(img, leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, zoom, zoom, null);
        } else {
            g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, zoom, zoom);
        }
    }

    private BufferedImage getImageForAction() {
        if (action == Actions.STAY) {
            return Rats.ratSprites.getSit();
        } else {
            return Rats.ratSprites.getRun(action.sprite, anim % 2);
        }
    }

    private void moveMouseRight(World world) {
        relativeCoordInSquare.x++;
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

    private void moveMouseUp(World world) {
        relativeCoordInSquare.y--;
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

    private void moveMouseLeft(World world) {
        relativeCoordInSquare.x--;
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

    private void moveMouseDown(World world) {
        relativeCoordInSquare.y++;
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

    private void stop() {
        //todo honoour previous direction
        this.action = Actions.STAY;
    }

    public void setAction(World world, Actions action) {
        this.action = action;

    }

    public void act(World world) {
        this.anim++;
        if (anim >= 10) {
            anim = 0;
        }
        if (seed.nextInt(50) == 0) {
            this.stop();
        }
        switch (action) {
            case DOWN_NORMAL:
                moveMouseDown(world);
                break;
            case UP_NORMAL:
                moveMouseUp(world);
                break;
            case LEFT_NORMAL:
                moveMouseLeft(world);
                break;
            case RIGHT_NORMAL:
                moveMouseRight(world);
                break;
            //TODO there must be also STAY (to eg eat or so... Or maybe resolve it in stop?
        }
    }

}
