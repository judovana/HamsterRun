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
        for (int y = config.getGridSize() / 2 - 1; y >= 0; y--) {
            maze.maze[config.getGridSize() / 2][y] = BaseBlock.generateByNeighours(config, null,  maze.maze[config.getGridSize() / 2][y + 1], null, null);
        }
        for (int y = config.getGridSize() / 2 + 1; y < config.getGridSize(); y++) {
            maze.maze[config.getGridSize() / 2][y] = BaseBlock.generateByNeighours(config, maze.maze[config.getGridSize() / 2][y - 1], null, null, null);
        }
        for (int x = config.getGridSize() / 2 - 1; x >= 0; x--) {
            maze.maze[x] [config.getGridSize() / 2] = BaseBlock.generateByNeighours(config, null, null,  null, maze.maze[x + 1][config.getGridSize() / 2]);
        }
        for (int x = config.getGridSize() / 2 + 1; x < config.getGridSize(); x++) {
            maze.maze[x] [config.getGridSize() / 2]= BaseBlock.generateByNeighours(config, null, null, maze.maze[x - 1][config.getGridSize() / 2], null);
        }
        return maze;
    }

    public BufferedImage toImage(int zoom, BaseConfig config) {
        return Utils.toImage(maze, zoom, config);
    }


}
