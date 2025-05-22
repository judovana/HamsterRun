package nonsense.hamsterrun.env.aliens;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.sprites.SpritesProvider;

//moving chaotically aka ai mouse
//must be faster then rats... maybe different thread then repl? Maybe special % in repl?
public class SmallBats extends MovingOne {

    protected int chaos = seed.nextInt(30) + 10;
    protected boolean moving = false;

    public SmallBats() {
        this.speed = seed.nextInt(4) + 3;
        this.anim = new AnimationCounrer(1000);
        anim.reset(seed.nextInt(SpritesProvider.getAlienSize(getSkin())));
    }

    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }

    @Override
    public boolean selfAct(World world) {
        this.action = RatActions.WALK;
        ComputerControl.dummyMove(this, world, chaos);
        if (isMoving()) {
            move(world);
        }
        anim.addLimited();
        return true;
    }

    private boolean isMoving() {
        if (seed.nextInt(chaos) == 0) {
            moving = !moving;
        }
        return moving;
    }

    @Override
    protected void adjustSpeedBeforeActionDirection(RatActions.Direction futureDirection) {

    }


    @Override
    protected String getSkin() {
        return "smallBats";
    }

    @Override
    public void interact(Rat rat, World world) {
        playMainSoundFor(rat.getSounds());
        rat.adjustScore(-10);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.piskMuch);
    }


}
