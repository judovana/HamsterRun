package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;

public class World implements Runnable {

    private static final Random seed = new Random();
    private static final int delayMs = 50;

    private final Thread repl;
    private final Maze maze;
    private final List<Rat> rats = Arrays.asList(new Rat(), new Rat(), new Rat(), new Rat());
    private int myMouse = -1;
    private int zoom = 20;

    private JComponent repaintListener;

    public World(Maze maze) {
        this.maze = maze;
        allRatsSpread(true);
        this.repl = new Thread(this);
        repl.setDaemon(true);
        repl.start();


    }

    public void allRatsSpread(boolean center) {
        for (int x = 0; x < rats.size(); x++) {
            Point[] start = center ? maze.getSafeSpotInMiddle() : maze.getRandomSafeSpot();
            boolean mouseOcupied = false;
            for (int y = 0; y < rats.size(); y++) {
                if (y != x) {
                    if (new Point(start[0].y, start[0].x).equals(rats.get(y).getCoordsInMaze()) && new Point(start[1].y,
                            start[1].x).equals(rats.get(y).getCoordsInBaseBlock())) {
                        mouseOcupied = true;
                        System.out.println("Mouse clash!");
                        System.out.println(rats.get(y).getCoordsInMaze() + " " + rats.get(y).getCoordsInBaseBlock());
                    }
                }
            }
            if (mouseOcupied) {
                x--;
                continue;
            }
            rats.get(x).setCoordsInMaze(start[0].y, start[0].x);
            rats.get(x).setCoordsInBaseBlock(start[1].y, start[1].x);
            System.out.println(rats.get(x).getCoordsInMaze() + " " + rats.get(x).getCoordsInBaseBlock());
        }
    }

    public void drawMap(Graphics2D g2d, Point center) {
        Point leftUpCornerOfMaze = new Point(center.x - maze.getWidthInUnits(BaseConfig.getConfig()) / 2 * zoom,
                center.y - maze.getHeightInUnits(BaseConfig.getConfig()) / 2 * zoom);
        if (myMouse >= 0 && myMouse < rats.size()) {
            Point selectedMouse = rats.get(myMouse).getUniversalCoords();
            int xShift = -center.x + selectedMouse.x * zoom;
            int yShift = -center.y + selectedMouse.y * zoom;
            leftUpCornerOfMaze = new Point(-xShift, -yShift);
        }
        maze.drawMapLevel1(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoom, BaseConfig.getConfig(), g2d);
        int i = -1;
        for (Rat rat : rats) {
            i++;
            g2d.setColor(new Color(0, 0, 250 - i * (250 / rats.size())));
            rat.draw(g2d, leftUpCornerOfMaze, zoom, true);
        }
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

    public void setMyMouseRight() {
        setMouseRight(myMouse);
    }

    public void setMyMouseUp() {
        setMouseUp(myMouse);
    }

    public void setMyMouseLeft() {
        setMouseLeft(myMouse);
    }

    public void setMyMouseDown() {
        setMouseDown(myMouse);
    }

    public void setMyMouse(int i) {
        myMouse = i;
    }

    public void setMouseUp(int i) {
        if (i >= 0 && i < rats.size()) {
            rats.get(i).setActionDirection(this, RatActions.WALK, RatActions.Direction.UP);
        }
    }

    public void setMouseLeft(int i) {
        if (i >= 0 && i < rats.size()) {
            rats.get(i).setActionDirection(this, RatActions.WALK, RatActions.Direction.LEFT);
        }
    }

    public void setMouseDown(int i) {
        if (i >= 0 && i < rats.size()) {
            rats.get(i).setActionDirection(this, RatActions.WALK, RatActions.Direction.DOWN);
        }
    }

    public void setMouseRight(int i) {
        if (i >= 0 && i < rats.size()) {
            rats.get(i).setActionDirection(this, RatActions.WALK, RatActions.Direction.RIGHT);
        }
    }

    public void setRepaintListener(JComponent repaintListener) {
        this.repaintListener = repaintListener;
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
        for (Rat rat : rats) {
            if (rat.getUniversalCoords().equals(coord)) {
                return false;
            }
        }
        return true;
    }

    private BlockField getMazeStatus(Point coord) {
        BlockField bl = maze.getByUniversalCoord(coord);
        return bl;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(delayMs);
                for (int m = 0; m < rats.size(); m++) {
                    if (m != myMouse) {
                        switch (seed.nextInt(8)) {
                            case 0:
                                setMouseLeft(m);
                                break;
                            case 1:
                                setMouseRight(m);
                                break;
                            case 2:
                                setMouseUp(m);
                                break;
                            case 3:
                                setMouseDown(m);
                                break;
                            default: //ok
                        }
                    }
                    if (m >= 0 && m < rats.size()) {
                        rats.get(m).act(this);
                    }
                }
                if (repaintListener != null) {
                    repaintListener.repaint();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }
}
