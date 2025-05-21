package nonsense.hamsterrun.env.aliens;


import java.awt.Point;

import nonsense.hamsterrun.env.BlockField;
import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.env.traps.Teleport;
import nonsense.hamsterrun.sprites.SpritesProvider;

//going up+down xor left and down, removing energy from all it meets
//must be faster then rats... maybe different thread then repl? Maybe special % in repl?
public class Boulder extends MovingOne {

    private final int maxspeed;
    private Point lastCoord = new Point(-1, -1);
    private int lastCoordCounter = -1;
    private boolean firstCall = true;

    public Boulder() {
        this.maxspeed = seed.nextInt(2) + 4;
        this.anim = new AnimationCounrer(1000);
        anim.reset(seed.nextInt(SpritesProvider.getAlienSize(getSkin())));
    }

    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }

    @Override
    public boolean selfAct(World world) {
        if (firstCall) {
            firstCall = false;
            Teleport.OrientedList<BlockField> sides = Teleport.getNeighboursFields(getUniversalCoords(), world);
            if (sides.getUpField() != null && sides.getUpField().isPassable()) {
                this.direction = RatActions.Direction.UP;
            } else if (sides.getDownField() != null && sides.getDownField().isPassable()) {
                this.direction = RatActions.Direction.DOWN;
            } else if (sides.getRightField() != null && sides.getRightField().isPassable()) {
                this.direction = RatActions.Direction.RIGHT;
            } else if (sides.getLeftField() != null && sides.getLeftField().isPassable()) {
                this.direction = RatActions.Direction.LEFT;
            }
        }
        this.speed = maxspeed;
        this.action = RatActions.WALK;
        move(world);
        anim.addLimited();
        if (lastCoord.equals(getUniversalCoords())) {
            lastCoordCounter++;
        } else {
            lastCoord = getUniversalCoords();
            lastCoordCounter = 0;
        }
        if (lastCoordCounter > 2) {
            Teleport.OrientedList<BlockField> sides = Teleport.getNeighboursFields(getUniversalCoords(), world);
            boolean invert = false;
            if (direction == RatActions.Direction.UP && (sides.getUpField() == null || sides.getUpField().isImpassable())) {
                invert = true;
            }
            if (direction == RatActions.Direction.DOWN && (sides.getDownField() == null || sides.getDownField().isImpassable())) {
                invert = true;
            }
            if (direction == RatActions.Direction.LEFT && (sides.getLeftField() == null || sides.getLeftField().isImpassable())) {
                invert = true;
            }
            if (direction == RatActions.Direction.RIGHT && (sides.getRightField() == null || sides.getRightField().isImpassable())) {
                invert = true;
            }
            if (invert) {
                this.direction = this.direction.opposite();
                lastCoordCounter = 0;
            }
        }
        return true;
    }


    @Override
    protected void adjustSpeedBeforeActionDirection(RatActions.Direction futureDirection) {

    }


    @Override
    protected String getSkin() {
        return "boulder";
    }

    @Override
    public void interact(Rat rat) {
        playMainSoundFor(rat.getSounds());
        rat.adjustScore(-500);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.piskFire);
    }
}
