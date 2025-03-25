package nonsense.hamsterrun.env;


import java.awt.Point;

import nonsense.hamsterrun.BaseConfig;

public class Rat {

    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);

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


    //FIXME wrong - the mouse must move in VISIBLE susbteps, and only if they reach soem value,
    // it will reset anfd the mouse will move to the next square
    //in addition this seesm like bad design, as trasnfer between blocks will require maze as parameter
    public void moveMouseRight() {
        coordsInBaseBlock.x++;
    }

    public void moveMouseUp() {
        coordsInBaseBlock.y--;
    }

    public void moveMouseLeft() {
        coordsInBaseBlock.x--;
    }

    public void moveMouseDown() {
        coordsInBaseBlock.y++;
    }
}
