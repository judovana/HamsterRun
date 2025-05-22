package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.aliens.BigBats;
import nonsense.hamsterrun.env.aliens.BigFlies;
import nonsense.hamsterrun.env.aliens.Boulder;
import nonsense.hamsterrun.env.aliens.Hawk;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.aliens.SmallBats;
import nonsense.hamsterrun.env.aliens.SmallFlies;
import nonsense.hamsterrun.env.traps.Cage;
import nonsense.hamsterrun.ratcontroll.RatsProvider;

import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class World implements Runnable {

    private static final Random seed = new Random();

    private final Thread repl;
    private final Maze maze;
    private final List<JComponent> repaintListeners = new ArrayList<>(2);
    private boolean live = true;
    private int worldAnim = 0;
    private RatsProvider ratsProvider;
    private List<MovingOne> aliens = Collections.synchronizedList(new ArrayList<MovingOne>());

    public World(Maze maze) {
        this.maze = maze;
        fillAliens();
        allRatsSpread(true);
        this.repl = new Thread(this);
        repl.setDaemon(true);
        repl.setName("world " + new Date());
        repl.start();


    }

    private void fillAliens() {
        //FIXME make it setup-able
        while (aliens.size() < BaseConfig.getConfig().getMaxAliens()) {
            MovingOne nw = getrandomAlien();
            teleportMouse(nw, false, true);
            aliens.add(nw);

        }
    }

    private MovingOne getrandomAlien() {
        List<ItemsWithBoundaries> recalcualted = ItemsWithBoundaries.recalculateToBoundaries(BaseConfig.getConfig().getAliensProbabilities());
        int i = seed.nextInt(recalcualted.get(recalcualted.size() - 1).upper);
        for (ItemsWithBoundaries item : recalcualted) {
            if (i >= item.lower && i < item.upper) {
                MovingOne alien = ItemsWithBoundaries.alienClassToItemCatched(item.clazz);
                return alien;
            }
        }
        return new Boulder();
    }

    public void teleportMouse(MovingOne rat, boolean center, boolean forceAlone) {
        Point[] start = new Point[]{new Point(-1, -1), new Point(-1, -1)};
        int aloneAttempts = 10;
        int totalAttempts = 100;
        while (totalAttempts > 0) {
            totalAttempts--;
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

    public List<Rat> getRats() {
        if (ratsProvider == null) {
            return new ArrayList(0);
        } else {
            return ratsProvider.getRats();
        }
    }

    public void allRatsSpread(boolean center) {
        allSpread(getRats(), center);
        for (MovingOne alien : aliens) {
            if (!alien.mustBeInCorridor()) {
                teleportMouse(alien, false, true);
            }
        }
    }

    public void allAliensSpread(boolean center) {
        allSpread(aliens, center);
    }

    public void allSpread(List<? extends MovingOne> rats, boolean center) {
        for (MovingOne rat : rats) {
            teleportMouse(rat, center, false);
        }
    }


    private boolean isMouseOcupied(MovingOne currentMouse, Point[] start) {
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


    public BaseBlockNeigbours getBaseBlockNeigboursByUniversal(int x, int y) {
        return maze.getBaseBlockNeigbours(x / BaseConfig.getConfig().getBaseSize(), y / BaseConfig.getConfig().getBaseSize());
    }

    public int getWidth() {
        return maze.getWidthInUnits(BaseConfig.getConfig());
    }

    public int getHeight() {
        return maze.getHeightInUnits(BaseConfig.getConfig());
    }

    public void drawMap(Graphics2D g2d, Point center, boolean map, int zoomOverride, Rat selectedMouse, boolean forceCenter) {
        Point leftUpCornerOfMaze = new Point(center.x - maze.getWidthInUnits(BaseConfig.getConfig()) / 2 * zoomOverride,
                center.y - maze.getHeightInUnits(BaseConfig.getConfig()) / 2 * zoomOverride);
        if (selectedMouse != null && !forceCenter) {
            int xShift = -center.x + selectedMouse.getUniversalCoords().x * zoomOverride + zoomOverride / 2;
            int yShift = -center.y + selectedMouse.getUniversalCoords().y * zoomOverride + zoomOverride / 2;
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
        i = -1;
        for (int x = 0; x < aliens.size(); x++) {
            MovingOne alien = aliens.get(x);
            i++;
            g2d.setColor(new Color(250 - i * (250 / aliens.size()), 250 - i * (250 / aliens.size()), 0));
            alien.draw(g2d, leftUpCornerOfMaze, zoomOverride, !map, false);
        }
        maze.drawMap(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoomOverride, BaseConfig.getConfig(), g2d, 3, map);
        //debug texts
        //maze.drawMap(leftUpCornerOfMaze.x, leftUpCornerOfMaze.y, zoomOverride, BaseConfig.getConfig(), g2d, 4, map);
    }

    public void regenerateBlock(int x, int y) {
        maze.regenerate(x, y, BaseConfig.getConfig());
    }

    public void addRepaintListener(JComponent repaintListener) {
        this.repaintListeners.add(repaintListener);
    }

    public boolean isEnterable(Point coord, int vx, int vy, boolean mouseBlock) {
        return isEnterable(coord.x + vx, coord.y + vy, mouseBlock);
    }

    boolean isEnterable(int x, int y, boolean mouseBlock) {
        return isEnterable(new Point(x, y), mouseBlock);
    }

    boolean isEnterable(Point coord, boolean mouseBlock) {
        BlockField bl = getMazeStatus(coord);
        if (bl == null || bl.isImpassable()) {
            return false;
        }
        if (mouseBlock) {
            for (Rat rat : getRats()) {
                if (rat.getUniversalCoords().equals(coord)) {
                    return false;
                }
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

    public void kill() {
        live = false;
        ratsProvider.kill();
    }

    public void run() {
        while (live) {
            //FIXME try catch all
            try {
                worldAnim++;
                if (worldAnim >= 1000) {
                    worldAnim = 0;
                }
                if (worldAnim % BaseConfig.getConfig().getRegSpeed() == 0) {
                    if (BaseConfig.getConfig().isKeepRegenerating()) {
                        regenerateAll();
                    }
                }
                if (worldAnim % 5 == 0) {
                    for (int x = 0; x< ratsProvider.getRats().size(); x++) {
                        Rat rat = ratsProvider.getRats().get(x);
                        if (this.getBlockField(rat.getUniversalCoords()).getItem() instanceof Cage){
                            ratsProvider.remove(rat);
                            x--;
                        }
                    }
                    for (Rat rat : getRats()) {
                        ratsProvider.getRatControl(rat).selfAct(rat, this);
                    }
                    for (int x = 0; x < aliens.size(); x++) {
                        MovingOne alien = aliens.get(x);
                        boolean survived = alien.selfAct(this);
                        if (survived) {
                            for (Rat rat : getRats()) {
                                if (alien.getUniversalCoords().equals(rat.getUniversalCoords())) {
                                    alien.interact(rat);
                                }
                            }
                        } else {
                            removeAlien(alien);
                            x--;
                        }
                    }
                    //this should not be customizable, as the regeneration of workd is hart beat
                    //without it, the key may never occure, even if this game is set up
                    for (int x = 0; x < aliens.size(); x++) {
                        if (aliens.get(x).mustBeInCorridor()) {
                            MovingOne alien = aliens.get(x);
                            if (!this.isEnterable(alien.getUniversalCoords(), false)) {
                                aliens.remove(x);
                                x--;
                                MovingOne newAlien = getrandomAlien();
                                aliens.add(newAlien);
                                Point[] futureCoords = maze.getSafeSpotIn(alien.getCoordsInMaze().x, alien.getCoordsInMaze().y);
                                newAlien.setCoordsInMaze(futureCoords[0]);
                                newAlien.setCoordsInBaseBlock(futureCoords[1]);
                            }
                        }
                    }
                }

                for (MovingOne alien : aliens) {
                    alien.unfilteredAct(this);
                }
                Thread.sleep(BaseConfig.getConfig().getDelayMs());
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

    public void swap(Rat rat) {
        ratsProvider.swap(rat);
    }

    public void removeAlien(MovingOne hawk) {
        aliens.remove(hawk);
        fillAliens();
    }
}
