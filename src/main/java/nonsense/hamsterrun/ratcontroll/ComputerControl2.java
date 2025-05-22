package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.Vegetable;
import nonsense.hamsterrun.env.traps.Water;

public class ComputerControl2 extends ComputerControl {

    @Override
    public String id() {
        return "pc2";
    }

    @Override
    public String toString() {
        return "chaos around 5 is really chaotic, above 50 is really appatic";
    }

    public void selfAct(Rat rat, World world) {
        if (world.getBlockField(rat.getUniversalCoords()).getItem() instanceof Vegetable) {
            rat.setAction(RatActions.EAT);
        } else if (rat.getAction().isInterruptible()) {
            rat.setAction(RatActions.WALK);
        }
        if (world.getBlockField(rat.getUniversalCoords()).getItem() instanceof Fire) {
            rat.setSpeed(rat.getSpeed() + 3);
        }
        if (world.getBlockField(rat.getUniversalCoords()).getItem() instanceof Water) {
            rat.setSpeed(10);
        }
        if (world.getBlockField(rat.getUniversalCoords()).getItem() instanceof Torturer) {
            rat.setSpeed(1);
        } else {
            if (RatsController.seed.nextInt(3) == 0) {
                rat.setSpeed(RatsController.seed.nextInt(3) + 1);
            }
        }
        niceMove(rat, world, chaos);
    }
}
