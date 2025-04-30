package nonsense.hamsterrun.env.aliens;

import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.image.BufferedImage;

//moving chaotically aka ai mouse
//must be faster then rats... maybe different thread then repl? Maybe special % in repl?
public class SmallBats extends MovingOne {

    protected int chaos = seed.nextInt(30) + 10;

    public SmallBats() {
        this.speed = 3;
    }

    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }

    @Override
    public void selfAct(World world) {
        this.action = RatActions.WALK;
        ComputerControl.dummyMove(this, world, chaos);
        move(world);

    }

    @Override
    protected void adjustSpeedBeforeActionDirection() {

    }

    @Override
    protected BufferedImage getImageForAction(String skin) {
        return SpritesProvider.getMushroom(0);
    }

    @Override
    protected String getSkin() {
        return "bat";
    }
}
