package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Image;

//as cucumber, just drawn above
public class Grass extends Vegetable {


    protected Image getSprite() {
        return SpritesProvider.grass;
    }

    @Override
    protected float getMaxEnergy() {
        return 60;
    }

    protected int getStartEnergy() {
        return seed.nextInt(((int) (getMaxEnergy() / 2))) + (int) (getMaxEnergy() / 2);
    }

    @Override
    public boolean eaten() {
        return energy < 30;
    }

    public int eat(Rat eater, World world) {
        if (eaten()) {
            return 0;
        }
        energy--;
        eater.adjustScore(50);
        return 50;
    }

    @Override
    protected int targetLevel() {
        return 3;
    }
}
