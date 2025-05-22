package nonsense.hamsterrun.env.aliens;

import java.awt.Point;
import java.util.List;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.env.traps.Teleport;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.sprites.SpritesProvider;

//walk through wall, scaryying them away?
public class Ghost extends MovingOne {

    protected int chaos = seed.nextInt(30) + 10;
    protected boolean moving = false;
    private String currentSkin = "ghost1";

    public Ghost() {
        this.speed = seed.nextInt(6) + 2;
        this.anim = new AnimationCounrer(1000);
        anim.reset(seed.nextInt(SpritesProvider.getAlienSize(getSkin())));
    }

    @Override
    public boolean mustBeInCorridor() {
        return false;
    }

    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }

    protected void reallyMoveMouseRight(World world) {
        forceMouseRight();
    }

    protected void reallyMoveMouseUp(World world) {
        forceMouseUp();
    }

    protected void reallyMoveMouseLeft(World world) {
        forceMouseLeft();
    }

    protected void reallyMoveMouseDown(World world) {
        forceMouseDown();
    }

    @Override
    public boolean selfAct(World world) {
        if (getUniversalCoords().x < -world.getWidth() / 3 || getUniversalCoords().y < -world.getHeight() / 3
                || getUniversalCoords().x > world.getWidth() * 1.3 || getUniversalCoords().y > world.getHeight() * 1.3) {
            world.teleportMouse(this, false, false);
        }
        if (isMoving()) {
            this.action = RatActions.WALK;
        } else {
            this.action = RatActions.STAY;
        }
        ComputerControl.dummyMove(this, world, chaos);
        if (direction == RatActions.Direction.LEFT) {
            currentSkin = "ghost1";
        }
        if (direction == RatActions.Direction.RIGHT) {
            currentSkin = "ghost2";
        }
        if (isMoving()) {
            move(world);
        }
        try {
            scaryAway(world);
        }catch (Exception ex){
            //this may throw, if the block is regenerated on the fly
            //it do not harm, no action, and  jsut go on
        }
        anim.addLimited();
        return true;
    }

    private void scaryAway(World world) {
        List<Point> neighbours1 = Teleport.getPassableNeighboursForGivenPoint(getUniversalCoords(), world);
        for (Point p1: neighbours1) {
            for (Rat rat: world.getRats()){
                scared(p1, rat, 6);
            }
            List<Point> neighbours2 = Teleport.getPassableNeighboursForGivenPoint(p1, world);
            for (Point p2: neighbours2) {
                for (Rat rat: world.getRats()){
                    scared(p2, rat, 3);
                }
            }
        }
    }

    private static void scared(Point p, Rat rat, int val) {
        if (rat.getUniversalCoords().equals(p)){
            rat.direction = RatActions.Direction.getRandom();
            if (rat.action.isInterruptible()){
                rat.action = RatActions.WALK;
            }
            if (rat.speed < val) {
                rat.speed = val;
            }
        }
    }

    private boolean isMoving() {
        if (moving) {
            if (seed.nextInt(chaos) == 0) {
                moving = false;
            }
        } else {
            if (seed.nextInt(chaos) <= chaos / 2) {
                moving = true;
            }
        }
        return moving;
    }

    @Override
    protected void adjustSpeedBeforeActionDirection(RatActions.Direction futureDirection) {

    }

    @Override
    protected String getSkin() {
        return currentSkin;
    }

    @Override
    public void interact(Rat rat, World world) {
        playMainSoundFor(rat.getSounds());
        rat.adjustScore(-50);
        for (Rat rat2: world.getRats()) {
            rat2.removeKey();
        }
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.brbliFall);
    }

    @Override
    public void playSecondarySoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.brbliTele);
    }
}
