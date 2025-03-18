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
        for (int c = 0; c <= config.getGridSize() / 2; c++) {//diagonal mover
            if (c == 0) {
                for (int y = c; y < config.getGridSize() / 2; y++) {
                    int yy = config.getGridSize() / 2 - y - 1;
                    //left half
                    maze.maze[config.getGridSize() / 2][yy] = BaseBlock.generateByNeighours(config, null, maze.maze[config.getGridSize() / 2][yy + 1], null, null);
                    int yyy = config.getGridSize() / 2 + y + 1;
                    //right half
                    maze.maze[config.getGridSize() / 2][yyy] = BaseBlock.generateByNeighours(config, maze.maze[config.getGridSize() / 2][yyy - 1], null, null, null);
                }
                for (int x = c; x < config.getGridSize() / 2; x++) {
                    int xx = config.getGridSize() / 2 - x - 1;
                    //up half
                    maze.maze[xx][config.getGridSize() / 2] = BaseBlock.generateByNeighours(config, null, null, null, maze.maze[xx + 1][config.getGridSize() / 2]);
                    int xxx = config.getGridSize() / 2 + x + 1;
                    //down half
                    maze.maze[xxx][config.getGridSize() / 2] = BaseBlock.generateByNeighours(config, null, null, maze.maze[xxx - 1][config.getGridSize() / 2], null);
                }
            } else {
                //warning, in all except 1, it is neigboutr with nulls
                //left up
                maze.maze[config.getGridSize() / 2 - c][config.getGridSize() / 2 - c] = BaseBlock.generateByNeighours(config, null, maze.maze[config.getGridSize() / 2 - c][config.getGridSize() / 2], null, maze.maze[config.getGridSize() / 2][config.getGridSize() / 2 - c]);
                //right up
                maze.maze[config.getGridSize() / 2 - c][config.getGridSize() / 2 + c] = BaseBlock.generateByNeighours(config, null, maze.maze[config.getGridSize() / 2 - c][config.getGridSize() / 2], null, maze.maze[config.getGridSize() / 2][config.getGridSize() / 2 + c]);
                //left down
                maze.maze[config.getGridSize() / 2 + c][config.getGridSize() / 2 - c] = BaseBlock.generateByNeighours(config, null, maze.maze[config.getGridSize() / 2 + c][config.getGridSize() / 2], null, maze.maze[config.getGridSize() / 2][config.getGridSize() / 2 - c]);
                //right down
                maze.maze[config.getGridSize() / 2 + c][config.getGridSize() / 2 + c] = BaseBlock.generateByNeighours(config, null, maze.maze[config.getGridSize() / 2 + c][config.getGridSize() / 2], null, maze.maze[config.getGridSize() / 2][config.getGridSize() / 2 + c]);
            }
        }
        return maze;
    }

    public BufferedImage toImage(int zoom, BaseConfig config) {
        return Utils.toImage(maze, zoom, config);
    }


}
