package nonsense.hamsterrun.env;


import java.awt.Graphics2D;
import java.awt.Point;

import nonsense.hamsterrun.BaseConfig;

public class Rat {

    public enum Actions {
        STAY/*FIXME needs stay left, right, up....*/, LEFT_NORMAL, RIGHT_NORMAL, UP_NORMAL, DOWN_NORMAL

    }

    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);
    private Actions action = Actions.STAY;
    private Point relativeCoordInSquare = new Point(0, 0);

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


    public void draw(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, boolean useInplaceSubMovement) {
        Point coord = getUniversalCoords();
        g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom, leftUpCornerOfMaze.y + coord.y * zoom, zoom, zoom);
    }

        private void moveMouseRight(World world) {
            relativeCoordInSquare.x++;
            //if run out of (-5 5) tenn  force and reset  relativeCoordInSquare
            reallyMoveMouseRight(world);
        }

        private void moveMouseUp(World world) {
            relativeCoordInSquare.y--;
            //if run out of (-5 5) tenn  force and reset  relativeCoordInSquare
            reallyMoveMouseUp(world);
        }

        private void moveMouseLeft(World world) {
            relativeCoordInSquare.x--;
            //if run out of (-5 5) tenn  force and reset  relativeCoordInSquare
            reallyMoveMouseLeft(world);
        }

        private void moveMouseDown(World world) {
            relativeCoordInSquare.y++;
            //if run out of (-5 5) tenn  force and reset  relativeCoordInSquare
            reallyMoveMouseDown(world);
        }

    public void setAction(World world, Actions action) {
        this.action = action;

    }

    public void act(World world) {
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
        }
    }
}
