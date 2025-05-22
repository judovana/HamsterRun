package nonsense.hamsterrun.ratcontroll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.traps.Teleport;

public class ComputerControl implements RatsController.RatControl {
    protected int chaos = RatsController.DEFAULT_CHAOS;
    private boolean display = false;
    private static final Map<MovingOne, Integer> decissionsOnCrossroadsSkipped = new HashMap<>(10);

    @Override
    public String id() {
        return "pc1";
    }

    @Override
    public String toString() {
        return "chaos around 5 is really chaotic, above 50 is really appatic";
    }

    public void selfAct(Rat rat, World world) {
        dummyMove(rat, world, chaos);
    }

    public static void niceMove(MovingOne rat, World world, int chaosFactor) {
        //note, this one is not solving stay/walk, it jsut changes direction
        //the handling of move/stay msut happen in different part, based on some logic
        Teleport.OrientedList<BlockField> neigh = Teleport.getNeighboursFields(rat.getUniversalCoords(), world);
        boolean mustChange = false;
        switch (rat.getDirection()) {
            case UP -> {
                if (neigh.getUpField() == null || neigh.getUpField().isImpassable()) {
                    mustChange = true;
                }
            }
            case DOWN -> {
                if (neigh.getDownField() == null || neigh.getDownField().isImpassable()) {
                    mustChange = true;
                }
            }
            case RIGHT -> {
                if (neigh.getRightField() == null || neigh.getRightField().isImpassable()) {
                    mustChange = true;
                }
            }
            case LEFT -> {
                if (neigh.getLeftField() == null || neigh.getLeftField().isImpassable()) {
                    mustChange = true;
                }
            }
        }
        List<RatActions.Direction> possibleDirections = new ArrayList<>(4);
        if (neigh.getUpField() != null && neigh.getUpField().isPassable()) {
            possibleDirections.add(RatActions.Direction.UP);
        }
        if (neigh.getDownField() != null && neigh.getDownField().isPassable()) {
            possibleDirections.add(RatActions.Direction.DOWN);
        }
        if (neigh.getRightField() != null && neigh.getRightField().isPassable()) {
            possibleDirections.add(RatActions.Direction.RIGHT);
        }
        if (neigh.getLeftField() != null && neigh.getLeftField().isPassable()) {
            possibleDirections.add(RatActions.Direction.LEFT);
        }
        Collections.shuffle(possibleDirections);
        if (!possibleDirections.isEmpty()) {
            if (mustChange) {
                rat.setDirection(possibleDirections.get(0));
            } else {
                if (RatsController.seed.nextInt(chaosFactor) == 0) {
                    //random change of direction
                    rat.setDirection(possibleDirections.get(0));
                } else {
                    //decision on crossroad
                    if (possibleDirections.size() >= 3) {
                        int skipsForThisRat = decissionsOnCrossroadsSkipped.getOrDefault(rat,0);
                        if (RatsController.seed.nextInt(chaosFactor) <= skipsForThisRat) {
                            rat.setDirection(possibleDirections.get(0));
                            skipsForThisRat = 0;
                        } else {
                            skipsForThisRat++;
                        }
                        decissionsOnCrossroadsSkipped.put(rat,skipsForThisRat);
                    }
                }
            }
        }
    }

    public static void dummyMove(MovingOne rat, World world, int chaosFactor) {
        switch (RatsController.seed.nextInt(chaosFactor)) {
            case 0:
                rat.setMouseLeft(world);
                break;
            case 1:
                rat.setMouseRight(world);
                break;
            case 2:
                rat.setMouseUp(world);
                break;
            case 3:
                rat.setMouseDown(world);
                break;
            default: //ok
        }
    }

    @Override
    public int getZoom() {
        return 64;
    }

    @Override
    public void zoomIn() {

    }

    @Override
    public void zoomOut() {

    }

    @Override
    public boolean isDisplay() {
        return display;
    }

    @Override
    public void setDisplay(Boolean aBoolean) {
        this.display = aBoolean;
    }

    @Override
    public void setChaos(int i) {
        this.chaos = i;
    }

    @Override
    public int getMap() {
        return 0;
    }
}
