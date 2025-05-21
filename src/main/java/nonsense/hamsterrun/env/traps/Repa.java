package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Image;

//so hard, that can not be crossed until eaten
public class Repa extends Vegetable {

    private RatActions.Direction lock = null;

    protected Image getSprite() {
        return SpritesProvider.repa;
    }


    @Override
    protected float getMaxEnergy() {
        return 20;
    }

    @Override
    protected int getStartEnergy() {
        return seed.nextInt(((int) (getMaxEnergy() / 2))) + (int) (getMaxEnergy() / 2);
    }

    public int eat(Rat eater, World world) {
        if (eaten()) {
            return 0;
        }
        energy--;
        eater.adjustScore(150);
        return 150;
    }

}
