package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Image;

public class Carrot extends Vegetable {


    protected Image getSprite() {
        return SpritesProvider.carrot;
    }

    public int eat(Rat eater, World world) {
        if (eaten()) {
            return 0;
        }
        if (seed.nextBoolean()) {
            energy--;
            eater.adjustScore(200);
            return 200;
        } else {
            eater.setAction(RatActions.WALK);
            return 0;
        }
    }


}
