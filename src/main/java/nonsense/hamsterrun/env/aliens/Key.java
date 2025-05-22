package nonsense.hamsterrun.env.aliens;

import java.awt.Point;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.sprites.SpritesProvider;

//moving well, over maze, better then random mous
//maybe future pc2?-)
//rats collects keys together
//must be faster then rats... maybe different thread then repl? Maybe special % in repl?
public class Key extends MovingOne {
    protected int chaos = seed.nextInt(10) + 10;

    public Key() {
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
        this.action = RatActions.WALK;
        ComputerControl.niceMove(this, world, chaos);
        move(world);
        anim.addLimited();
        for (Rat rat : world.getRats()) {
            if (getUniversalCoords().equals(rat.getUniversalCoords())) {
                playMainSoundFor(rat.getSounds());
                for (Rat ratK : world.getRats()) {
                    ratK.addKey();
                }
                return false;
            }
        }
        return true;
    }


    @Override
    protected void adjustSpeedBeforeActionDirection(RatActions.Direction futureDirection) {

    }

    @Override
    protected String getSkin() {
        return "key";
    }

    @Override
    public void interact(Rat rat) {

    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.piskLong);
    }
}