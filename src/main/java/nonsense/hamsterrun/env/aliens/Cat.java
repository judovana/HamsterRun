package nonsense.hamsterrun.env.aliens;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.sprites.SpritesProvider;

//running through maze and hunting rats
public class Cat extends MovingOne {
    protected int chaos = seed.nextInt(30) + 10;
    protected boolean moving = false;
    private String currentSkin = "cat1";

    public Cat() {
        this.speed = seed.nextInt(5) + 3;
        this.anim = new AnimationCounrer(1000);
        anim.reset(seed.nextInt(SpritesProvider.getAlienSize(getSkin())));
    }


    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }


    @Override
    public boolean selfAct(World world) {
        if (isMoving()) {
            this.action = RatActions.WALK;
        } else {
            this.action = RatActions.STAY;
        }
        ComputerControl.niceMove(this, world, chaos);
        if (direction == RatActions.Direction.LEFT) {
            currentSkin = "cat2";
        }
        if (direction == RatActions.Direction.RIGHT) {
            currentSkin = "cat1";
        }
        if (isMoving()) {
            move(world);
        }
        anim.addLimited();
        return true;
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
    public void interact(Rat rat) {
        playMainSoundFor(rat.getSounds());
        rat.adjustScore(-500);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.piskMuch);
    }

}
