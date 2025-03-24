package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class World {

    private final Maze maze;
    private Point in = new Point(10,10);

    public World(Maze maze) {
        this.maze = maze;
    }

    public void draw(int x, int y, int zoom, Graphics2D g2d) {
        g2d.setColor(Color.blue);
        g2d.fillRect(50,50, zoom, zoom);
    }
}
