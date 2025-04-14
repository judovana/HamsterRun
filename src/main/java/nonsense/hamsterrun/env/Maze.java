package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {

    private static final Random seed = new Random();

    private final BaseBlock[][] maze;

    public Maze(int gridSize) {
        this.maze = new BaseBlock[gridSize][gridSize];
    }

    public static Maze generate(BaseConfig config) {
        Maze maze = new Maze(config.getGridSize());
        //this is handled separately for readability and ability to not generate middle three N times, otherwise it could be in main loop
        maze.maze[config.getGridSize() / 2][config.getGridSize() / 2] = BaseBlock.generateMiddle(config, new Point(config.getGridSize() / 2, config.getGridSize() / 2));
        for (int c = 0; c <= config.getGridSize() / 2; c++) {//diagonal mover
            if (c == 0) {
                //MAIN CROSS
                //this is handled separately for readability and ability to not generate middle three N times
                for (int y = 0; y < config.getGridSize() / 2; y++) {
                    //note, that in current impl it do not matter whether the neighbour is on left or right.. so it is slightly ignored here
                    int yy = config.getGridSize() / 2 - y - 1;
                    //left half
                    maze.maze[config.getGridSize() / 2][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[config.getGridSize() / 2][yy + 1], null, null, new Point(config.getGridSize() / 2, yy));
                    int yyy = config.getGridSize() / 2 + y + 1;
                    //right half
                    maze.maze[config.getGridSize() / 2][yyy] = BaseBlock.generateByNeighbours(config, maze.maze[config.getGridSize() / 2][yyy - 1], null, null, null, new Point(config.getGridSize() / 2, yyy));
                }
                for (int x = 0; x < config.getGridSize() / 2; x++) {
                    //note, that in current impl it do not matter whether the neighbour is on up or down.. so it is slightly ignored here
                    int xx = config.getGridSize() / 2 - x - 1;
                    //up half
                    maze.maze[xx][config.getGridSize() / 2] = BaseBlock.generateByNeighbours(config, null, null, null, maze.maze[xx + 1][config.getGridSize() / 2], new Point(xx, config.getGridSize() / 2));
                    int xxx = config.getGridSize() / 2 + x + 1;
                    //down half
                    maze.maze[xxx][config.getGridSize() / 2] = BaseBlock.generateByNeighbours(config, null, null, maze.maze[xxx - 1][config.getGridSize() / 2], null, new Point(xxx, config.getGridSize() / 2));
                }
            } else {
                //the FOUR QUADRANTS
                //note, that in current impl it do not matter whether the neighbour is on left or right.. so it is slightly ignored here
                for (int i = 0; i <= config.getGridSize() / 2 - c; i++) {
                    //left up
                    {
                        //left
                        int xx = config.getGridSize() / 2 - c;
                        int yy = config.getGridSize() / 2 - c - i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx + 1][yy], new Point(xx, yy));
                        //the corner would be generated twice, but its "just before" added neighbour is not counted.. that can make bad result!
                        if (i != 0) {
                            //up
                            xx = config.getGridSize() / 2 - c - i;
                            yy = config.getGridSize() / 2 - c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx + 1][yy], new Point(xx, yy));
                        }
                    }
                    // right up
                    {
                        //right
                        int xx = config.getGridSize() / 2 - c;
                        int yy = config.getGridSize() / 2 + c + i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx + 1][yy], new Point(xx, yy));
                        //the corner would be generated twice, but its "just before" added neighbour is not counted.. that can make bad result!
                        if (i != 0) {
                            //up
                            xx = config.getGridSize() / 2 - c - i;
                            yy = config.getGridSize() / 2 + c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx + 1][yy], new Point(xx, yy));
                        }

                    }
                    //left down
                    {
                        //left
                        int xx = config.getGridSize() / 2 + c;
                        int yy = config.getGridSize() / 2 - c - i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx - 1][yy], new Point(xx, yy));
                        //the corner would be generated twice, but its "just before" added neighbour is not counted.. that can make bad result!
                        if (i != 0) {
                            //down
                            xx = config.getGridSize() / 2 + c + i;
                            yy = config.getGridSize() / 2 - c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx - 1][yy], new Point(xx, yy));
                        }
                    }
                    //right down
                    {
                        //right
                        int xx = config.getGridSize() / 2 + c;
                        int yy = config.getGridSize() / 2 + c + i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx - 1][yy], new Point(xx, yy));
                        if (i != 0) {
                            //down
                            xx = config.getGridSize() / 2 + c + i;
                            yy = config.getGridSize() / 2 + c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx - 1][yy], new Point(xx, yy));
                        }
                    }
                }
            }
        }
        return maze;
    }

    public BufferedImage toImage(int zoom, BaseConfig config, boolean map) {
        return Utils.toImage(this, zoom, config, map);
    }


    public void drawMap(int userx, int usery, int zoom, BaseConfig config, Graphics2D g2d, int level, boolean map) {
        //delete all
        if (level == 1) {
            g2d.setColor(new Color(0, 0, 0, 255));
            g2d.fillRect(userx - zoom, usery - zoom, zoom * maze[0].length * BaseConfig.getConfig().getBaseSize() + 2 * zoom, zoom * maze.length * BaseConfig.getConfig().getBaseSize() + 2 * zoom);
        }
        for (int x = 0; x < maze.length; x++) {
            for (int y = 0; y < maze[x].length; y++) {
                if (maze[x][y] != null) {
                    int coordx = y * config.getBaseSize() * zoom + userx;
                    int coordy = x * config.getBaseSize() * zoom + usery;
                    BaseBlockNeigbours neighbours = getBaseBlockNeigbours(y, x);
                    maze[x][y].drawMap(coordx, coordy, zoom, g2d, level, map, neighbours);
                }

            }
        }
    }

    public BaseBlockNeigbours getBaseBlockNeigbours(int xx, int yy) {
        //swap from visual to data
        int x = yy;
        int y = xx;
        BaseBlockNeigbours neighbours = new BaseBlockNeigbours(x, y, maze[x][y]);
        if (x > 0) {
            neighbours.setUp(maze[x - 1][y], x - 1, y);
        }
        if (x < maze.length - 1) {
            neighbours.setDown(maze[x + 1][y], x + 1, y);
        }
        if (y > 0) {
            neighbours.setLeft(maze[x][y - 1], x, y - 1);
        }
        if (y < maze[0].length - 1) {
            neighbours.setRight(maze[x][y + 1], x, y + 1);
        }
        return neighbours;
    }


    public void regenerate(int x, int y, BaseConfig config) {
        maze[x][y] = BaseBlock.generateByNeighbours(config,
                (y > 0) ? maze[x][y - 1] : null,
                (y < config.getGridSize() - 1) ? maze[x][y + 1] : null,
                (x > 0) ? maze[x - 1][y] : null,
                (x < config.getGridSize() - 1) ? maze[x + 1][y] : null,
                new Point(x, y)
        );

    }

    public int getWidth() {
        return maze[0].length;
    }

    public int getHeight() {
        return maze.length;
    }

    public int getWidthInUnits(BaseConfig cfg) {
        return getWidth() * cfg.getBaseSize();
    }

    public int getHeightInUnits(BaseConfig cfg) {
        return getHeight() * cfg.getBaseSize();
    }

    public Point[] getRandomSafeSpot() {
        int x = -1;
        int y = -1;
        do {
            x = seed.nextInt(maze.length);
            y = seed.nextInt(maze[x].length);
        } while (maze[x][y] == null);
        return getSafeSpotIn(x, y);
    }

    public Point[] getSafeSpotIn(int x, int y) {
        return new Point[]{new Point(x, y), maze[x][y].getRandomSafeSpot()};
    }

    public Point[] getSafeSpotInMiddle() {
        return new Point[]{
                new Point(maze.length / 2, maze[maze.length / 2].length / 2),
                maze[maze.length / 2][maze[maze.length / 2].length / 2].getRandomSafeSpot()};
    }

    public BlockField getByUniversalCoord(Point coord) {
        int x1 = coord.y / BaseConfig.getConfig().getBaseSize();
        int y1 = coord.x / BaseConfig.getConfig().getBaseSize();
        if (x1 < 0 || x1 >= maze.length) {
            return null;
        }
        if (y1 < 0 || y1 >= maze[0].length) {
            return null;
        }
        int x2 = coord.y % BaseConfig.getConfig().getBaseSize();
        int y2 = coord.x % BaseConfig.getConfig().getBaseSize();
        return maze[x1][y1].get(x2, y2);
    }

    public List<Point> getDirectNeighbours(int x, int y) {
        List<Point> result = new ArrayList<>();
        if (x > 0) {
            result.add(new Point(x - 1, y));
        }
        if (x < getHeight() - 1) {
            result.add(new Point(x + 1, y));
        }
        if (y > 0) {
            result.add(new Point(x, y - 1));
        }
        if (y < getWidth() - 1) {
            result.add(new Point(x, y + 1));
        }
        return result;
    }
}
