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

    public void moveMyMouseRight() {
        if (myMouse >= 0 && myMouse < rats.size()) {
            rats.get(myMouse).moveMouseRight();
        }
    }

    public void moveMyMouseUp() {
        if (myMouse >= 0 && myMouse < rats.size()) {
            rats.get(myMouse).moveMouseUp();
        }
    }

    public void moveMyMouseLeft() {
        if (myMouse >= 0 && myMouse < rats.size()) {
            rats.get(myMouse).moveMouseLeft();
        }
    }

    public void moveMyMouseDown() {
        if (myMouse >= 0 && myMouse < rats.size()) {
            rats.get(myMouse).moveMouseDown();
        }
    }

    public void setMyMouse(int i) {
        myMouse = i;
    }

    public void setRepaintListener(JComponent repaintListener) {
        this.repaintListener = repaintListener;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(100);
                switch (seed.nextInt(6)) {
                    case 0:
                        moveMyMouseLeft();
                        break;
                    case 1:
                        moveMyMouseRight();
                        break;
                    case 2:
                        moveMyMouseUp();
                        break;
                    case 3:
                        moveMyMouseDown();
                        break;
                    default: //ok
                }
                if (repaintListener != null) {
                    repaintListener.repaint();
                }
            }catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }

    }
}
