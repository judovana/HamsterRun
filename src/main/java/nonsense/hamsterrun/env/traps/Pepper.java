package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Image;
import java.awt.Point;
import java.util.Collections;
import java.util.List;

public class Pepper extends Vegetable {


    protected Image getSprite() {
        return SpritesProvider.pepper;
    }

    public int eat(Rat eater, World world) {
        if (eaten()) {
            return 0;
        }
        if (seed.nextInt(3) != 0) {
            energy--;
            eater.adjustScore(200);
            return 200;
        } else {
            eater.setAction(RatActions.STAY);
            List<Point> l = Teleport.getPassableNeighboursForGivenPoint(eater.getUniversalCoords(), world);
            if (!l.isEmpty()) {
                Collections.shuffle(l);
                eater.setUniversalCoords(l.get(0));
            }
            return 0;
        }
    }

}
