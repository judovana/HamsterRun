package nonsense.hamsterrun.env;

import nonsense.hamsterrun.BaseConfig;

public class Maze {


    private final BaseBlock[][] maze;

    public Maze(int gridSize) {
        this.maze = new BaseBlock[gridSize][gridSize];
    }

    public static Maze generate(BaseConfig config) {
        Maze maze = new Maze(config.getGridSize());
        maze.maze[config.getGridSize() / 2][config.getGridSize() / 2] = BaseBlock.generateMiddle(config);
        for (int x = config.getGridSize() / 2 - 1; x >= 0; x--) {
            maze.maze[x][config.getGridSize() / 2] = BaseBlock.generateByNeighours(config, null, maze.maze[x + 1][config.getGridSize() / 2], null, null);
        }
        for (int x = config.getGridSize() / 2 + 1; x > config.getGridSize(); x++) {
            maze.maze[x][config.getGridSize() / 2] = BaseBlock.generateByNeighours(config, null, maze.maze[x - 1][config.getGridSize() / 2], null, null);
        }
        return maze;
    }


}
