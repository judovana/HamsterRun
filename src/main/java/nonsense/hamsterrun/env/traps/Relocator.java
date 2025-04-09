package nonsense.hamsterrun.env.traps;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.World;


public interface Relocator {

    default void relocate(World world, Rat rat) {
        world.teleportMouse(rat, false, true);
    }
}
