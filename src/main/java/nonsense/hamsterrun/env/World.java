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

    public void draw(Graphics2D g2d, Point center) {
        if (myMouse < 0 || myMouse >= rats.size()) {
            Point leftUpCornerOfMaze = new Point(center.x - maze.getWidthInUnits(BaseConfig.getConfig()) / 2 * zoom,
                    center.y - maze.getHeightInUnits(BaseConfig.getConfig()) / 2 * zoom);
            maze.drawMapLevel1(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoom, BaseConfig.getConfig(), g2d);
            int i = -1;
            for (Rat rat : rats) {
                i++;
                g2d.setColor(new Color(0, 0, 250 - i * (250 / rats.size())));
                Point coord = rat.getUniversalCoords();
                g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom, leftUpCornerOfMaze.y + coord.y * zoom, zoom, zoom);
            }
        } else {
            Point selectedMouse = rats.get(myMouse).getUniversalCoords();
            int xShift = -center.x + selectedMouse.x * zoom;
            int yShift = -center.y + selectedMouse.y * zoom;
            Point leftUpCornerOfMaze = new Point(-xShift, -yShift);
            maze.drawMapLevel1(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoom, BaseConfig.getConfig(), g2d);
            int i = -1;
            for (Rat rat : rats) {
                i++;
                g2d.setColor(new Color(0, 0, 250 - i * (250 / rats.size())));
                Point coord = rat.getUniversalCoords();
                g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom, leftUpCornerOfMaze.y + coord.y * zoom, zoom, zoom);
            }
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

    public void moveMouseUp(int i) {
        if (i >= 0 && i < rats.size()) {
            if (isEnterable(rats.get(i).getUniversalCoords(), 0, -1)) {
                rats.get(i).moveMouseUp();
            }
        }
    }

    public void moveMouseLeft(int i) {
        if (i >= 0 && i < rats.size()) {
            if (isEnterable(rats.get(i).getUniversalCoords(), -1, 0)) {
                rats.get(i).moveMouseLeft();
            }
        }
    }

    public void moveMouseDown(int i) {
        if (i >= 0 && i < rats.size()) {
            if (isEnterable(rats.get(i).getUniversalCoords(), 0, 1)) {
                rats.get(i).moveMouseDown();
            }
        }
    }

    public void moveMouseRight(int i) {
        if (i >= 0 && i < rats.size()) {
            if (isEnterable(rats.get(i).getUniversalCoords(), 1, 0)) {
                rats.get(i).moveMouseRight();
            }
        }
    }

    public void moveMyMouseRight() {
        moveMouseRight(myMouse);
    }

    public void moveMyMouseUp() {
        moveMouseUp(myMouse);
    }

    public void moveMyMouseLeft() {
        moveMouseLeft(myMouse);
    }

    public void moveMyMouseDown() {
        moveMouseDown(myMouse);
    }

    public void setMyMouse(int i) {
        myMouse = i;
    }

    public void setRepaintListener(JComponent repaintListener) {
        this.repaintListener = repaintListener;
    }

    private boolean isEnterable(Point coord, int vx, int vy) {
        return isEnterable(coord.x + vx, coord.y + vy);
    }

    private boolean isEnterable(int x, int y) {
        return isEnterable(new Point(x, y));
    }

    private boolean isEnterable(Point coord) {
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
                Thread.sleep(100);
                for (int m = 0; m < rats.size(); m++) {
                    if (m != myMouse) {
                        switch (seed.nextInt(6)) {
                            case 0:
                                moveMouseLeft(m);
                                break;
                            case 1:
                                moveMouseRight(m);
                                break;
                            case 2:
                                moveMouseUp(m);
                                break;
                            case 3:
                                moveMouseDown(m);
                                break;
                            default: //ok
                        }
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
