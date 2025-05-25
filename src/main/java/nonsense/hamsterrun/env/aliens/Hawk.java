package nonsense.hamsterrun.env.aliens;

import nonsense.hamsterrun.env.Rat;
import nonsense.hamsterrun.env.RatActions;
import nonsense.hamsterrun.env.SoundsBuffer;
import nonsense.hamsterrun.env.World;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.env.traps.Grass;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//flying over screen, targeting rats, walls do not stop
//the only way how to get rid of it is to hide ALL rats in tunnels, it will fly away....
public class Hawk extends MovingOne {

    private int MAX_FLY_AWAY; //min is 2; with 3 was quite hard to get rid of some more chaotic hawks. 4.iompossible
    private int CHAOS_DET = 18;
    protected int chaos = seed.nextInt(CHAOS_DET) + 1;
    private final int maxspeed = 1;
    private int skipCounter = 0;
    private Point vector = new Point(0, 0);
    private Point targetCords;
    private Point lastPosition;

    public Hawk() {
        //the combination of small chaos (thus changing direction often)
        //and big fly away is very dangerous, nearly non playable
        if (chaos < CHAOS_DET / 2 + CHAOS_DET / 5) {
            MAX_FLY_AWAY = 2;
        } else {
            MAX_FLY_AWAY = 3;
        }
        //this.MAX_FLY_AWAY = 2; //3 is really hard, 2... bring :(
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
    public void drawMapExtension(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, World world) {
        super.drawMapExtension(g2d, leftUpCornerOfMaze, zoom, world);
        if (targetCords != null) {
            Point coord = getUniversalCoords();
            g2d.drawLine(leftUpCornerOfMaze.x + lastPosition.x * zoom + zoom / 2, leftUpCornerOfMaze.y + lastPosition.y * zoom + zoom / 2,
                    leftUpCornerOfMaze.x + coord.x * zoom + zoom / 2, leftUpCornerOfMaze.y + coord.y * zoom + zoom / 2);
        } else {
            Point coord = getUniversalCoords();
            Point vectorCoords = new Point(coord.x + vector.x, coord.y + vector.y);
            g2d.drawLine(leftUpCornerOfMaze.x + vectorCoords.x * zoom + zoom / 2, leftUpCornerOfMaze.y + vectorCoords.y * zoom + zoom / 2,
                    leftUpCornerOfMaze.x + coord.x * zoom + zoom / 2, leftUpCornerOfMaze.y + coord.y * zoom + zoom / 2);
        }
    }

    @Override
    public void draw(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, boolean useInplaceSubMovement, boolean higlight) {
        super.draw(g2d, leftUpCornerOfMaze, zoom, useInplaceSubMovement, higlight);
        Point coord = getUniversalCoords();
        if (targetCords != null && !targetCords.equals(coord)) {
            g2d.drawImage(SpritesProvider.getFalonShaadow(), leftUpCornerOfMaze.x + targetCords.x * zoom - zoom / 2, leftUpCornerOfMaze.y + targetCords.y * zoom - zoom / 2, 2 * zoom, 2 * zoom, null);
        }
        if (lastPosition != null && !lastPosition.equals(coord)) {
            //if current position is on rat (or some random?)
            //draw several hawks
            //from (leftUpCornerOfMaze.x + lastPosition.x * zoom + zoom / 2, leftUpCornerOfMaze.y + lastPosition.y * zoom + zoom / 2,
            //to    leftUpCornerOfMaze.x + coord.x * zoom + zoom / 2, leftUpCornerOfMaze.y + coord.y * zoom + zoom / 2);
            int xCoord1 = leftUpCornerOfMaze.x + lastPosition.x * zoom + zoom / 2;
            int yCoord1 = leftUpCornerOfMaze.y + lastPosition.y * zoom + zoom / 2;
            int xCoord2 = leftUpCornerOfMaze.x + coord.x * zoom + zoom / 2;
            int yCoord2 = leftUpCornerOfMaze.y + coord.y * zoom + zoom / 2;
            int fromX = Math.min(xCoord1, xCoord2);
            int fromY = Math.min(yCoord1, yCoord2);
            int toX = Math.max(xCoord1, xCoord2);
            int toY = Math.max(yCoord1, yCoord2);
            double steps = 10;
            double xDelta = (double) (toX - fromX) / 10d;
            double yDelta = (double) (toY - fromY) / 10d;
            BufferedImage img = getImageForAction(getSkin());
            for (int x = 0; x < steps; x++) {
                //BUGY
                //g2d.drawImage(img, leftUpCornerOfMaze.x + fromX + (int)((double)x*xDelta), leftUpCornerOfMaze.y + fromY + (int)((double)x*yDelta), zoom, zoom, null);
            }
        }
    }

    @Override
    //FIXME
    //draw SHADOW over targeted rat - TARGET field (if any)
    //from last place, to current place - draw line of eagles
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
        if (getUniversalCoords().x < -(MAX_FLY_AWAY - 1) * world.getWidth()
                || getUniversalCoords().x > MAX_FLY_AWAY * world.getWidth()
                || getUniversalCoords().y < -(MAX_FLY_AWAY - 1) * world.getHeight()
                || getUniversalCoords().y > MAX_FLY_AWAY * world.getHeight()) {
            return false;
        }
        //am on any rat not in tunnel? Stop
        for (Rat rat : world.getRats()) {
            if (getUniversalCoords().equals(rat.getUniversalCoords())) {
                if (world.getBlockField(getUniversalCoords()).getItem() instanceof Tunnel
                        || world.getBlockField(getUniversalCoords()).getItem() instanceof Grass) {
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
                if (world.getBlockField(a.get(x).getUniversalCoords()).getItem() instanceof Tunnel
                        || world.getBlockField(a.get(x).getUniversalCoords()).getItem() instanceof Grass) {
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
                targetCords = target.getUniversalCoords();
            } else {
                int x = 10 - seed.nextInt(20);
                int y = 10 - seed.nextInt(20);
                int cgd = gcdSanitising(x, y);
                vector = new Point(x / cgd, y / cgd);
                targetCords = null;
            }
        }
        return true;
    }

    private void fly() {
        lastPosition = getUniversalCoords();
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
    protected void adjustSpeedBeforeActionDirection(RatActions.Direction futureDirection) {

    }


    @Override
    protected String getSkin() {
        return "falcon";
    }

    @Override
    public void interact(Rat rat, World world) {
        playMainSoundFor(rat.getSounds());
        rat.adjustScore(-500);
    }

    @Override
    public void playMainSoundFor(SoundsBuffer rat) {
        rat.addToHarmQueue(SoundsBuffer.piskMuch);
    }
}
