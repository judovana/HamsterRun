package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Image;

public class Cucumber extends Vegetable {


    protected Image getSprite() {
        return SpritesProvider.okurka;
    }

    public int eat(Rat eater, World world) {
        if (eaten()) {
            return 0;
        }
        energy--;
        eater.adjustScore(100);
        return 100;
    }

}
