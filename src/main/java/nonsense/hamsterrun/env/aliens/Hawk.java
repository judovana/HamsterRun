package nonsense.hamsterrun.env.aliens;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.sprites.SpritesProvider;

//flying over screen, targeting rats, walls do not stop
public class Hawk extends MovingOne {

    public static final int MAX_FLY_AWAY = 3; //min is 2
    protected int chaos = seed.nextInt(18) + 1;
    private final int maxspeed = 1;
    private int skipCounter = 0;
    private Point vector = new Point(0, 0);

    public Hawk() {
        this.anim = new AnimationCounrer(1000);
        anim.reset(seed.nextInt(SpritesProvider.getAlienSize(getSkin())));
    }

    @Override
    public boolean mustBeInCorridor() {
        return false;
    }

    @Override
    protected boolean returnOnSalat(World world) {
        return false;
    }

    @Override
    public boolean selfAct(World world) {
        skipCounter++;
        //eagle is to strong, we are skipping Nth rounds - every 2nd here
        if (skipCounter % 2 == 0) {
            skipCounter = 0;
            return true;
        }
        this.speed = maxspeed;
        this.action = RatActions.WALK;
        anim.addLimited();
        fly();
        if (getUniversalCoords().x < -(MAX_FLY_AWAY-1)*world.getWidth()
                || getUniversalCoords().x > MAX_FLY_AWAY * world.getWidth()
                || getUniversalCoords().y < -(MAX_FLY_AWAY-1)*world.getHeight()
                || getUniversalCoords().y > MAX_FLY_AWAY * world.getHeight()) {
            return false;
        }
        //am on any rat not in tunnel? Stop
        for (Rat rat : world.getRats()) {
            if (getUniversalCoords().equals(rat.getUniversalCoords())) {
                if (world.getBlockField(getUniversalCoords()).getItem() instanceof Tunnel) {
                    return true;
                }
                vector = new Point(0, 0);
            }
        }
        //time to change target?
        if (seed.nextInt(chaos) == 0) {
            //any rats out of tunnels?
            List<Rat> a = new ArrayList<>(world.getRats());
            for (int x = 0; x < a.size(); x++) {
                if (world.getBlockField(a.get(x).getUniversalCoords()).getItem() instanceof Tunnel) {
                    a.remove(x);
                    x--;
                }
            }
            if (a.size() > 0) {
                Collections.shuffle(a);
                Rat target = a.get(0);
                int x = target.getUniversalCoords().x - getUniversalCoords().x;
                int y = target.getUniversalCoords().y - getUniversalCoords().y;
                int cgd = gcdSanitising(x, y);
                vector = new Point(x / cgd, y / cgd);
            } else {
                int x = 10 - seed.nextInt(20);
                int y = 10 - seed.nextInt(20);
                int cgd = gcdSanitising(x, y);
                vector = new Point(x / cgd, y / cgd);
            }
        }
        return true;
    }

    private void fly() {
        if (vector.x < 0) {
            forceMouseLeft(Math.abs(vector.x));
        } else {
            forceMouseRight(Math.abs(vector.x));
        }
        if (vector.y < 0) {
            forceMouseUp(Math.abs(vector.y));
        } else {
            forceMouseDown(Math.abs(vector.y));
        }
    }

    private static int gcdSanitising(int a, int b) {
        if (a == 0 || b == 0) {
            return 1;
        }
        return gcd(Math.abs(a), Math.abs(b));
    }

    /**
     * Returns greatest common divisor of the given numbers
     *
     * @param a number 1
     * @param b number 2
     * @return gcd(a, b)
     */
    private static int gcd(int a, int b) {
        if (a < 1 || b < 1) throw new IllegalArgumentException("a or b is less than 1");
        while (b != 0) {
            int tmp = a;
            a = b;
            b = tmp % b;
        }
        return a;
    }


    @Override
    protected void adjustSpeedBeforeActionDirection() {

    }


    @Override
    protected String getSkin() {
        return "falcon";
    }

    @Override
    public void interact(Rat rat) {
        rat.getSounds().addToHarmQueue(SoundsBuffer.piskMuch);
        rat.adjustScore(-2000);
    }
}
