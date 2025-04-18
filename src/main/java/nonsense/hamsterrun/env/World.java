package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.ratcontroll.RatsProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;

public class World implements Runnable {

    private static final Random seed = new Random();
    private static final int delayMs = 50;

    private final Thread repl;
    private final Maze maze;
    //FIXME, move to RatWithControls, so each rat cna have theirs view
    private int zoom = 64;
    private int worldAnim = 0;

    private final List<JComponent> repaintListeners = new ArrayList<>(2);
    private RatsProvider ratsProvider;

    public World(Maze maze) {
        this.maze = maze;
        allRatsSpread(true);
        this.repl = new Thread(this);
        repl.setDaemon(true);
        repl.start();


    }

    public void teleportMouse(Rat rat, boolean center, boolean forceAlone) {
        Point[] start;
        int aloneAttempts = 10;
        while (true) {
            if (aloneAttempts <= 0) {
                forceAlone = false;
            }
            if (forceAlone) {
                aloneAttempts--;
                List<Point> remotes = new ArrayList<>(getSquaresWithoutRatInNeighbourhood());
                if (remotes.isEmpty()) {
                    start = center ? maze.getSafeSpotInMiddle() : maze.getRandomSafeSpot();
                } else {
                    Collections.shuffle(remotes);
                    start = maze.getSafeSpotIn(remotes.get(0).y, remotes.get(0).x);
                }
            } else {
                start = center ? maze.getSafeSpotInMiddle() : maze.getRandomSafeSpot();
            }
            if (start[0].equals(rat.getCoordsInMaze())) {
                //necessary?
                continue;
            }
            boolean mouseOccupied = isMouseOcupied(rat, start);
            if (!mouseOccupied) {
                break;
            }
        }
        rat.setCoordsInMaze(start[0].y, start[0].x);
        rat.setCoordsInBaseBlock(start[1].y, start[1].x);
        System.out.println(rat.getCoordsInMaze() + " " + rat.getCoordsInBaseBlock());
    }

    public Set<Point> getSquaresWithoutRatInNeighbourhood() {
        Set<Point> result = new HashSet<>();
        for (int x = 0; x < maze.getHeight(); x++) {
            for (int y = 0; y < maze.getWidth(); y++) {
                boolean haveRat = false;
                for (Rat rat : getRats()) {
                    if (rat.getCoordsInMaze().equals(new Point(x, y))) {
                        haveRat = true;
                        break;
                    }
                }
                if (!haveRat) {
                    boolean haveRatOnNeighbour = false;
                    List<Point> neighbours = maze.getDirectNeighbours(x, y);
                    for (Point point : neighbours) {
                        for (Rat rat : getRats()) {
                            if (rat.getCoordsInMaze().equals(point)) {
                                haveRatOnNeighbour = true;
                                break;
                            }
                        }
                        if (haveRatOnNeighbour) {
                            break;
                        }
                    }
                    if (!haveRatOnNeighbour) {
                        result.add(new Point(x, y));
                    }
                }
            }
        }
        return result;
    }

    private List<Rat> getRats() {
        if (ratsProvider == null) {
            return new ArrayList(0);
        } else {
            return ratsProvider.getRats();
        }
    }

    public void allRatsSpread(boolean center) {
        for (Rat rat : getRats()) {
            teleportMouse(rat, center, false);
        }
    }

    private boolean isMouseOcupied(Rat currentMouse, Point[] start) {
        boolean mouseOcupied = false;
        for (Rat rat : getRats()) {
            //really ==
            if (rat == currentMouse) {
                if (new Point(start[0].y, start[0].x).equals(rat.getCoordsInMaze()) && new Point(start[1].y,
                        start[1].x).equals(rat.getCoordsInBaseBlock())) {
                    mouseOcupied = true;
                    System.out.println("Mouse clash!");
                    System.out.println(rat.getCoordsInMaze() + " " + rat.getCoordsInBaseBlock());
                }
            }
        }
        return mouseOcupied;
    }

    public void drawMap(Graphics2D g2d, Point center, boolean map, Rat selectedMouse) {
        drawMap(g2d, center, map, this.zoom, selectedMouse);
    }

    public BaseBlockNeigbours getBaseBlockNeigboursByUniversal(int x, int y) {
        return maze.getBaseBlockNeigbours(x / BaseConfig.getConfig().getBaseSize(), y / BaseConfig.getConfig().getBaseSize());
    }

    public void drawMap(Graphics2D g2d, Point center, boolean map, int zoomOverride, Rat selectedMouse) {
        Point leftUpCornerOfMaze = new Point(center.x - maze.getWidthInUnits(BaseConfig.getConfig()) / 2 * zoomOverride,
                center.y - maze.getHeightInUnits(BaseConfig.getConfig()) / 2 * zoomOverride);
        if (selectedMouse != null) {
            int xShift = -center.x + selectedMouse.getUniversalCoords().x * zoomOverride;
            int yShift = -center.y + selectedMouse.getUniversalCoords().y * zoomOverride;
            leftUpCornerOfMaze = new Point(-xShift, -yShift);
        }
        maze.drawMap(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoomOverride, BaseConfig.getConfig(), g2d, 1, map);
        maze.drawMap(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoomOverride, BaseConfig.getConfig(), g2d, 2, map);
        int i = -1;
        for (Rat rat : getRats()) {
            i++;
            g2d.setColor(new Color(0, 0, 250 - i * (250 / getRats().size())));
            boolean selected = false;
            if (rat.equals(selectedMouse)) {
                selected = true;
            }
            rat.draw(g2d, leftUpCornerOfMaze, zoomOverride, !map, selected);
        }
        maze.drawMap(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoomOverride, BaseConfig.getConfig(), g2d, 3, map);
        //debug texts
        //maze.drawMap(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoomOverride, BaseConfig.getConfig(), g2d, 4, map);
    }

    public void regenerateBlock(int x, int y) {
        maze.regenerate(x, y, BaseConfig.getConfig());
    }

    public void zoomIn() {
        zoom = zoom + Math.max(zoom / 2, 1);
    }

    public void zoomOut() {
        zoom = zoom - Math.max(1, zoom / 2);
        if (zoom <= 0) {
            zoom = 1;
        }
    }


    public void setMouseUp(Rat rat) {
        if (rat != null) {
            rat.setActionDirection(this, RatActions.WALK, RatActions.Direction.UP);
        }
    }

    public void setMouseLeft(Rat rat) {
        if (rat != null) {
            rat.setActionDirection(this, RatActions.WALK, RatActions.Direction.LEFT);
        }
    }

    public void setMouseDown(Rat rat) {
        if (rat != null) {
            rat.setActionDirection(this, RatActions.WALK, RatActions.Direction.DOWN);
        }
    }

    public void setMouseRight(Rat rat) {
        if (rat != null) {
            rat.setActionDirection(this, RatActions.WALK, RatActions.Direction.RIGHT);
        }
    }

    public void addRepaintListener(JComponent repaintListener) {
        this.repaintListeners.add(repaintListener);
    }

    boolean isEnterable(Point coord, int vx, int vy) {
        return isEnterable(coord.x + vx, coord.y + vy);
    }

    boolean isEnterable(int x, int y) {
        return isEnterable(new Point(x, y));
    }

    boolean isEnterable(Point coord) {
        BlockField bl = getMazeStatus(coord);
        if (bl == null || bl.isImpassable()) {
            return false;
        }
        for (Rat rat : getRats()) {
            if (rat.getUniversalCoords().equals(coord)) {
                return false;
            }
        }
        return true;
    }

    public BlockField getBlockField(Point coord) {
        BlockField bl = getMazeStatus(coord);
        if (bl == null) {
            return new BlockField(true, new Point(-1, -1), null);
        }
        return bl;
    }

    private BlockField getMazeStatus(Point coord) {
        BlockField bl = maze.getByUniversalCoord(coord);
        return bl;
    }

    public void run() {
        while (true) {
            //FIXME try catch all
            try {
                worldAnim++;
                if (worldAnim % 5 == 0) {
                    worldAnim = 0;
                    if (BaseConfig.getConfig().isKeepRegenerating()) {
                        regenerateAll();
                    }
                    for (Rat rat : getRats()) {
                        //FIXME move this to AI implementation, once setMouse..action is fixed
                        if (rat.isAi()) {
                            switch (seed.nextInt(20)) {
                                case 0:
                                    setMouseLeft(rat);
                                    break;
                                case 1:
                                    setMouseRight(rat);
                                    break;
                                case 2:
                                    setMouseUp(rat);
                                    break;
                                case 3:
                                    setMouseDown(rat);
                                    break;
                                default: //ok
                            }
                        }
                    }
                }
                Thread.sleep(delayMs);
                for (Rat rat : getRats()) {
                    rat.act(this);
                }
                for (JComponent repaintListener : repaintListeners) {
                    repaintListener.repaint();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void regenerateAll() {
        Set<Point> sqWithoutN = getSquaresWithoutRatInNeighbourhood();
        for (Point p : sqWithoutN) {
            maze.regenerate(p.y, p.x, BaseConfig.getConfig());
        }
    }

    public void setRatsProvider(RatsProvider ratsController) {
        this.ratsProvider = ratsController;
    }
}
