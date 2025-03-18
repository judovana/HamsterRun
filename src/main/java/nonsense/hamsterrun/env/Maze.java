package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.Utils;

import java.awt.image.BufferedImage;

public class Maze {


    private final BaseBlock[][] maze;

    public Maze(int gridSize) {
        this.maze = new BaseBlock[gridSize][gridSize];
    }

    public static Maze generate(BaseConfig config) {
        Maze maze = new Maze(config.getGridSize());
        maze.maze[config.getGridSize() / 2][config.getGridSize() / 2] = BaseBlock.generateMiddle(config);
        //main cross
        for (int y = 0; y < config.getGridSize()/2; y++) {
            int yy=config.getGridSize() / 2 - y - 1;
            //left half
            maze.maze[config.getGridSize() / 2][yy] = BaseBlock.generateByNeighours(config, null,  maze.maze[config.getGridSize() / 2][yy + 1], null, null);
            int yyy=config.getGridSize() / 2 + y + 1;
            //right half
            maze.maze[config.getGridSize() / 2][yyy] = BaseBlock.generateByNeighours(config, maze.maze[config.getGridSize() / 2][yyy - 1], null, null, null);
        }
        for (int x = 0; x < config.getGridSize()/2; x++) {
            int xx = config.getGridSize() / 2 - x - 1;
            //up half
            maze.maze[xx] [config.getGridSize() / 2] = BaseBlock.generateByNeighours(config, null, null,  null, maze.maze[xx + 1][config.getGridSize() / 2]);
            int xxx = config.getGridSize() / 2 + x + 1;
            //down half
            maze.maze[xxx] [config.getGridSize() / 2]= BaseBlock.generateByNeighours(config, null, null, maze.maze[xxx - 1][config.getGridSize() / 2], null);
        }
        return maze;
    }

    public BufferedImage toImage(int zoom, BaseConfig config) {
        return Utils.toImage(maze, zoom, config);
    }


}
