package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Image;

//so delicious, that once in process of eating, can not be left
public class Salat extends Vegetable {


    protected Image getSprite() {
        return SpritesProvider.salat;
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
