package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Maze {


    private final BaseBlock[][] maze;

    public Maze(int gridSize) {
        this.maze = new BaseBlock[gridSize][gridSize];
    }

    public static Maze generate(BaseConfig config) {
        Maze maze = new Maze(config.getGridSize());
        //this is handled separately for readability and ability to not generate middle three N times, otherwise it could be in main loop
        maze.maze[config.getGridSize() / 2][config.getGridSize() / 2] = BaseBlock.generateMiddle(config);
        for (int c = 0; c <= config.getGridSize() / 2; c++) {//diagonal mover
            if (c == 0) {
                //MAIN CROSS
                //this is handled separately for readability and ability to not generate middle three N times
                for (int y = 0; y < config.getGridSize() / 2; y++) {
                    //note, that in current impl it do not matter whether the neighbour is on left or right.. so it is slightly ignored here
                    int yy = config.getGridSize() / 2 - y - 1;
                    //left half
                    maze.maze[config.getGridSize() / 2][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[config.getGridSize() / 2][yy + 1], null, null);
                    int yyy = config.getGridSize() / 2 + y + 1;
                    //right half
                    maze.maze[config.getGridSize() / 2][yyy] = BaseBlock.generateByNeighbours(config, maze.maze[config.getGridSize() / 2][yyy - 1], null, null, null);
                }
                for (int x = 0; x < config.getGridSize() / 2; x++) {
                    //note, that in current impl it do not matter whether the neighbour is on up or down.. so it is slightly ignored here
                    int xx = config.getGridSize() / 2 - x - 1;
                    //up half
                    maze.maze[xx][config.getGridSize() / 2] = BaseBlock.generateByNeighbours(config, null, null, null, maze.maze[xx + 1][config.getGridSize() / 2]);
                    int xxx = config.getGridSize() / 2 + x + 1;
                    //down half
                    maze.maze[xxx][config.getGridSize() / 2] = BaseBlock.generateByNeighbours(config, null, null, maze.maze[xxx - 1][config.getGridSize() / 2], null);
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
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx + 1][yy]);
                        //the corner would be generated twice, but its "just before" added neighbour is not counted.. that can make bad result!
                        if (i != 0) {
                            //up
                            xx = config.getGridSize() / 2 - c - i;
                            yy = config.getGridSize() / 2 - c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx + 1][yy]);
                        }
                    }
                    // right up
                    {
                        //right
                        int xx = config.getGridSize() / 2 - c;
                        int yy = config.getGridSize() / 2 + c + i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx + 1][yy]);
                        //the corner would be generated twice, but its "just before" added neighbour is not counted.. that can make bad result!
                        if (i != 0) {
                            //up
                            xx = config.getGridSize() / 2 - c - i;
                            yy = config.getGridSize() / 2 + c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx + 1][yy]);
                        }

                    }
                    //left down
                    {
                        //left
                        int xx = config.getGridSize() / 2 + c;
                        int yy = config.getGridSize() / 2 - c - i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx - 1][yy]);
                        //the corner would be generated twice, but its "just before" added neighbour is not counted.. that can make bad result!
                        if (i != 0) {
                            //down
                            xx = config.getGridSize() / 2 + c + i;
                            yy = config.getGridSize() / 2 - c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy + 1], null, maze.maze[xx - 1][yy]);
                        }
                    }
                    //right down
                    {
                        //right
                        int xx = config.getGridSize() / 2 + c;
                        int yy = config.getGridSize() / 2 + c + i;
                        maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx - 1][yy]);
                        if (i != 0) {
                            //down
                            xx = config.getGridSize() / 2 + c + i;
                            yy = config.getGridSize() / 2 + c;
                            maze.maze[xx][yy] = BaseBlock.generateByNeighbours(config, null, maze.maze[xx][yy - 1], null, maze.maze[xx - 1][yy]);
                        }
                    }
                }
            }
        }
        return maze;
    }

    public BufferedImage toImage(int zoom, BaseConfig config) {
        return Utils.toImage(maze, zoom, config);
    }

    public void drawTo(Graphics2D g2d, int zoom, BaseConfig config, int userx, int usery) {
        Utils.drawTo(userx, usery, maze, zoom, config,  g2d);
    }


    public void regenerate(int x, int y, BaseConfig config) {
        System.out.println(x + " x " + y);
        maze[x][y] = BaseBlock.generateByNeighbours(config,
                (y>0)?maze[x][y-1]:null,
                (y<config.getGridSize()-1)?maze[x][y+1]:null,
                (x>0)?maze[x-1][y]:null,
                (x<config.getGridSize()-1)?maze[x+1][y]:null
                );

    }
}
