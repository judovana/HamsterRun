package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class World {

    private final Maze maze;
    private Point in = new Point(400, 400);
    private Point positionHelper = new Point(0, 0);
    private int zoom = 20;

    public World(Maze maze) {
        this.maze = maze;
        Point[] start = maze.getSafeSpotInMiddle();
        //FIXME this is wrong
        //start[1].setLocation(5,0);
        System.out.println(start[0] + " " + start[1]);
        positionHelper.x = -start[0].x * BaseConfig.getConfig().getBaseSize() * zoom
                - start[1].x * zoom + in.y;
        positionHelper.y = -start[0].y * BaseConfig.getConfig().getBaseSize() * zoom
                - start[1].y * zoom + in.x;

    }

    public void draw(Graphics2D g2d) {
        maze.drawMapLevel1(positionHelper.x, positionHelper.y, zoom, BaseConfig.getConfig(), g2d);
        g2d.setColor(Color.blue);
        g2d.fillRect(in.x, in.y, zoom, zoom);
    }

    public void regenerateBlock(int x, int y) {
        maze.regenerate(x, y, BaseConfig.getConfig());
    }

    public void zoomIn() {
        zoom++;
    }

    public void zoomOut() {
        zoom--;
        if (zoom <= 0) {
            zoom = 1;
        }
    }

    public void moveMyMouseRight() {
        positionHelper.x--;
    }

    public void moveMyMouseUp() {
        positionHelper.y++;
    }

    public void moveMyMouseLeft() {
        positionHelper.x++;
    }

    public void moveMyMouseDown() {
        positionHelper.y--;
    }
}
