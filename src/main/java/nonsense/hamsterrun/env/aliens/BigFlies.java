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
public class BigFlies extends MovingOne {

    protected int chaos = seed.nextInt(5) + 10;
    protected boolean moving = false;

    public BigFlies() {
        this.speed = seed.nextInt(2)+2;
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
        if (seed.nextInt(chaos)==0) {
            moving = !moving;
        }
        return moving;
    }

    @Override
    protected void adjustSpeedBeforeActionDirection() {

    }


    @Override
    protected String getSkin() {
        return "bigFlies";
    }

    @Override
    public void interact(Rat rat) {
        if (rat.getAction() == RatActions.STAY || rat.getAction() == RatActions.EAT){
            rat.getSounds().addToHarmQueue(SoundsBuffer.brbliTunel);
            rat.setActionDirection(RatActions.WALK, RatActions.Direction.getRandom());
            rat.speed=relativeSizes;
        } if (rat.getAction() == RatActions.WALK){
            rat.getSounds().addToHarmQueue(SoundsBuffer.brbliTunel);
            rat.setActionDirection(RatActions.WALK, rat.direction);
            rat.speed=relativeSizes;

        }
    }
}
