package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Item;
import nonsense.hamsterrun.env.traps.Mushroom;
import nonsense.hamsterrun.env.traps.Relocator;
import nonsense.hamsterrun.env.traps.Teleport;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.Vegetable;
import nonsense.hamsterrun.ratcontroll.ScoreListener;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Rat {

    private static final Random seed = new Random();
    private static final int MAGICAL_FALL_CHANCE = 6;
    private static final int relativeSizes = 5;
    private AnimationCounrer anim = new AnimationCounrer();
    private SoundsBuffer sounds = new SoundsBuffer();
    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);
    private RatActions action = RatActions.STAY;
    private RatActions.OptionalmetaData actionMetaData = null;
    private RatActions.Direction direction = RatActions.Direction.UP;
    private Point relativeCoordInSquare = new Point(0, 0);
    private int speed = 1; //can not go over relativeSizes*2
    private int score = 1000;
    private String skin = "rat";
    private ScoreListener scoreListener;

    public Rat() {
    }

    public Rat(Point world, Point block) {
        this.coordsInBaseBlock = block;
        this.coordsInMaze = world;
    }

    public Rat(int worldx, int worldy, int blockx, int blocky) {
        this.coordsInBaseBlock = new Point(blockx, blocky);
        this.coordsInMaze = new Point(worldx, worldy);
    }

    public static Point toUniversalCoords(Point coordsInMaze, Point coordsInBaseBlock) {
        return new Point(coordsInMaze.x * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.x,
                coordsInMaze.y * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.y);
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public Point getCoordsInBaseBlock() {
        return coordsInBaseBlock;
    }

    public void setCoordsInBaseBlock(Point coordsInBaseBlock) {
        this.coordsInBaseBlock = coordsInBaseBlock;
    }

    public Point getCoordsInMaze() {
        return coordsInMaze;
    }

    public void setCoordsInMaze(Point coordsInMaze) {
        this.coordsInMaze = coordsInMaze;
    }

    public void setCoordsInBaseBlock(int x, int y) {
        this.coordsInBaseBlock = new Point(x, y);
    }

    public void setCoordsInMaze(int x, int y) {
        this.coordsInMaze = new Point(x, y);
    }

    public Point getUniversalCoords() {
        return toUniversalCoords(coordsInMaze, coordsInBaseBlock);
    }

    public void setUniversalCoords(Point target) {
        coordsInMaze.x = target.x / BaseConfig.getConfig().getBaseSize();
        coordsInMaze.y = target.y / BaseConfig.getConfig().getBaseSize();
        coordsInBaseBlock.x = target.x % BaseConfig.getConfig().getBaseSize();
        coordsInBaseBlock.y = target.y % BaseConfig.getConfig().getBaseSize();
    }


    private void forceMouseRight() {
        coordsInBaseBlock.x++;
        if (coordsInBaseBlock.x >= BaseConfig.getConfig().getBaseSize()) {
            coordsInMaze.x++;
            coordsInBaseBlock.x = 0;
        }
    }

    private void forceMouseUp() {
        coordsInBaseBlock.y--;
        if (coordsInBaseBlock.y < 0) {
            coordsInMaze.y--;
            coordsInBaseBlock.y = BaseConfig.getConfig().getBaseSize() - 1;
        }
    }

    private void forceMouseLeft() {
        coordsInBaseBlock.x--;
        if (coordsInBaseBlock.x < 0) {
            coordsInMaze.x--;
            coordsInBaseBlock.x = BaseConfig.getConfig().getBaseSize() - 1;
        }
    }

    private void forceMouseDown() {
        coordsInBaseBlock.y++;
        if (coordsInBaseBlock.y >= BaseConfig.getConfig().getBaseSize()) {
            coordsInMaze.y++;
            coordsInBaseBlock.y = 0;
        }
    }

    private void reallyMoveMouseRight(World world) {
        if (world.isEnterable(getUniversalCoords(), 1, 0)) {
            forceMouseRight();
        }
    }

    private void reallyMoveMouseUp(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, -1)) {
            forceMouseUp();
        }
    }

    private void reallyMoveMouseLeft(World world) {
        if (world.isEnterable(getUniversalCoords(), -1, 0)) {
            forceMouseLeft();
        }
    }

    private void reallyMoveMouseDown(World world) {
        if (world.isEnterable(getUniversalCoords(), 0, 1)) {
            forceMouseDown();
        }
    }

    //useInplaceSubMovement - in map false, in game true
    public void draw(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, boolean useInplaceSubMovement, boolean higlight) {
        Point coord = getUniversalCoords();
        Point relativeShift = new Point(0, 0);
        if (useInplaceSubMovement) {
            //it goes from (relativeSizes..0...-relativeSizes) (without the edges)
            //so for relativeSizes 1 the total to walk is 0
            //so for relativeSizes 2 the total to walk is 3 (1,0,-1)...
            //so for relativeSizes 5 the total to walk is 9 (4,3,2,1,0,-1,-2,-3,-4)...
            float relativeSizesCalc = relativeSizes * 2 - 1;
            float step = zoom / relativeSizesCalc;
            //so for relativeSizes 5 the it goes from 1 to 9 inclusive
            float relativeX = (relativeCoordInSquare.x) * step;
            float relativeY = (relativeCoordInSquare.y) * step;
            relativeShift.x = (int) relativeX;
            relativeShift.y = (int) relativeY;
            BufferedImage img = getImageForAction(skin);
            int usedZoom = zoom;
            if (action == RatActions.FALLING) {
                usedZoom = Math.max(1, zoom - (zoom / 50 + 1) * anim.anim);
            }
            g2d.drawImage(img, leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, usedZoom, usedZoom, null);
        } else {
            g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, zoom, zoom);
            if (higlight) {
                g2d.setColor(Color.red);
                g2d.drawOval(leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x - anim.modMap(), leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y - anim.modMap(), zoom + 2 * anim.modMap(), zoom + 2 * +anim.modMap());

            }
        }
    }

    private BufferedImage getImageForAction(String skin) {
        if (RatActions.isStay(action)) {
            return SpritesProvider.ratSprites.get(skin).getSit(direction.getSprite(), anim.ignore());
        } else if (action == RatActions.EAT) {
            return SpritesProvider.ratSprites.get(skin).getEat(direction.getSprite(), anim.ignore());
        } else if (RatActions.isWalk(action)) {
            return SpritesProvider.ratSprites.get(skin).getRun(direction.getSprite(), anim.mod(SpritesProvider.ratSprites.get(skin).getRuns()));
        } else if (action == RatActions.FALLING) {
            return SpritesProvider.ratSprites.get(skin).getFall(direction.getSprite(), anim.mod(SpritesProvider.ratSprites.get(skin).getFalls()));
        } else {
            throw new RuntimeException("Unknown action " + action);
        }
    }

    private void moveMouseRight(World world) {
        relativeCoordInSquare.x += speed;
        if (relativeCoordInSquare.y < 0) {
            relativeCoordInSquare.y++;
        }
        if (relativeCoordInSquare.y > 0) {
            relativeCoordInSquare.y--;
        }
        if (relativeCoordInSquare.x >= relativeSizes) {
            relativeCoordInSquare.x = -relativeSizes;
            reallyMoveMouseRight(world);
        }
    }

    private void moveMouseUp(World world) {
        relativeCoordInSquare.y -= speed;
        if (relativeCoordInSquare.x < 0) {
            relativeCoordInSquare.x++;
        }
        if (relativeCoordInSquare.x > 0) {
            relativeCoordInSquare.x--;
        }
        if (relativeCoordInSquare.y <= -relativeSizes) {
            relativeCoordInSquare.y = relativeSizes;
            reallyMoveMouseUp(world);
        }
    }

    private void moveMouseLeft(World world) {
        relativeCoordInSquare.x -= speed;
        if (relativeCoordInSquare.y < 0) {
            relativeCoordInSquare.y++;
        }
        if (relativeCoordInSquare.y > 0) {
            relativeCoordInSquare.y--;
        }
        if (relativeCoordInSquare.x <= -relativeSizes) {
            relativeCoordInSquare.x = relativeSizes;
            reallyMoveMouseLeft(world);
        }
    }

    private void moveMouseDown(World world) {
        relativeCoordInSquare.y += speed;
        if (relativeCoordInSquare.x < 0) {
            relativeCoordInSquare.x++;
        }
        if (relativeCoordInSquare.x > 0) {
            relativeCoordInSquare.x--;
        }
        if (relativeCoordInSquare.y >= relativeSizes) {
            relativeCoordInSquare.y = -relativeSizes;
            reallyMoveMouseDown(world);
        }
    }

    private void stop(World world) {
        speed = 1;
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            this.action = RatActions.EAT;
        } else {
            if (this.action != RatActions.STAY) {
                sounds.addToEatQueue(SoundsBuffer.piskWeird);
            }
            this.action = RatActions.STAY;
        }
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Mushroom) {
            ((Mushroom)(world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.sounds);
            world.swap(this);
            this.action = RatActions.STAY;
        }
    }

    public void setActionDirection(RatActions action, RatActions.Direction direction) {
        if (this.action == RatActions.WALK && this.direction == direction) {
            speed++;
            if (speed == MAGICAL_FALL_CHANCE - 1) {
                sounds.addToMoveQueue(SoundsBuffer.turbo);
            }
            if (speed >= relativeSizes * 2) {
                speed = relativeSizes * 2;
            }
        } else {
            speed = 1;
        }
        this.action = action;
        this.direction = direction;

    }

    public void act(World world) {
        anim.addLimited();
        int chanceToStop = 40;
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            chanceToStop = 10;
        }
        if (seed.nextInt(chanceToStop) == 0 && this.action.isInterruptible()) {
            this.stop(world);
        }
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof InvisibleTrapDoor && seed.nextInt(MAGICAL_FALL_CHANCE) + 1 > speed) {
            if (this.action != RatActions.FALLING) {
                this.anim.reset();
                this.action = RatActions.FALLING;
                ((InvisibleTrapDoor)(world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.sounds);
            }
        }
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Teleport && seed.nextInt(MAGICAL_FALL_CHANCE) + 1 > speed) {
            if (this.action != RatActions.FALLING) {
                this.anim.reset();
                this.action = RatActions.FALLING;
                ((Teleport)world.getBlockField(getUniversalCoords()).getItem()).playMainSoundFor(this.sounds);
            }
        }
        switch (action) {
            case WALK:
                moveInDirection(world);
                break;
            case EAT:
                eat(world);
                break;
            case FALLING:
                fall(world);
                break;
            case STAY:
                harm(world);
                break;
        }
    }

    private void moveInDirection(World world) {
        if (world.getBlockField(this.getUniversalCoords()).getItem() instanceof Tunnel) {
            if (seed.nextInt(BaseConfig.getConfig().getTunnelConfusioNfactor()) == 0) {
                direction = RatActions.Direction.getRandom();
                ((Tunnel)(world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.sounds);
            }
        }
        harm(world);
        switch (direction) {
            case DOWN:
                moveMouseDown(world);
                break;
            case UP:
                moveMouseUp(world);
                break;
            case LEFT:
                moveMouseLeft(world);
                break;
            case RIGHT:
                moveMouseRight(world);
                break;
        }
    }

    private void eat(World world) {
        speed = 1;
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            if (anim.everyThird()) {
                direction = direction.rotateCW();
                boolean eaten = ((Vegetable) world.getBlockField(getUniversalCoords()).getItem()).eat();
                if (eaten) {
                    world.getBlockField(getUniversalCoords()).clear();
                } else {
                    ((Vegetable)(world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.sounds);
                    adjustScore(100);
                    harm(world);
                }
            }
        } else {
            action = RatActions.STAY;
        }
    }

    private void fall(World world) {
        speed = 1;
        if (anim.anim == 10) {
            anim.reset();
            if (world.getBlockField(getUniversalCoords()).getItem() instanceof InvisibleTrapDoor) {
                ((InvisibleTrapDoor) world.getBlockField(getUniversalCoords()).getItem()).close();
            }
            if (world.getBlockField(getUniversalCoords()).getItem() instanceof Relocator) {
                ((Relocator) world.getBlockField(getUniversalCoords()).getItem()).relocate(world, this);
            }
            direction = RatActions.Direction.getRandom();
            action = RatActions.STAY;
        } else {
            if (world.getBlockField(getUniversalCoords()).getItem() instanceof InvisibleTrapDoor) {
                ((InvisibleTrapDoor) world.getBlockField(getUniversalCoords()).getItem()).open();
            }
            if (relativeCoordInSquare.x > 0) {
                relativeCoordInSquare.x--;
            }
            if (relativeCoordInSquare.x < 0) {
                relativeCoordInSquare.x++;
            }
            if (relativeCoordInSquare.y > 0) {
                relativeCoordInSquare.y--;
            }
            if (relativeCoordInSquare.y < 0) {
                relativeCoordInSquare.y++;
            }
        }
    }

    public RatActions getAction() {
        return action;
    }

    public int getScore() {
        return score;
    }

    public void adjustScore(int scoreIncDec) {
        this.score += scoreIncDec;
        if (this.score < 0) {
            score = 0;
        }
        if (scoreListener == null) {
            System.out.println(this.getScore() + " - " + this.toString() + ": " + this.score);
        } else {
            scoreListener.report(this, this.score);
        }
    }

    public void harm(World w) {
        if (this.getUniversalCoords().x < 0 || this.getUniversalCoords().y < 0) {
            return;
        }
        Item field = w.getBlockField(this.getUniversalCoords()).getItem();
        if (field instanceof Torturer) {
            adjustScore(-5 * speed * speed);
            if (speed == MAGICAL_FALL_CHANCE - 1) {
                ((Torturer)w.getBlockField(this.getUniversalCoords()).getItem()).playMainSoundFor(this.getSounds());
            } else {
                ((Torturer)w.getBlockField(this.getUniversalCoords()).getItem()).playSecondarySoundFor(this.getSounds());
            }
        }
        boolean burned = false;
        if (field instanceof Fire) {
            adjustScore(-20);
            if (!burned) {
                burned = true;
                ((Fire)field).playMainSoundFor(sounds);
            }
        }
        BaseBlockNeigbours neighBase1 = w.getBaseBlockNeigboursByUniversal(this.getUniversalCoords().x, this.getUniversalCoords().y);
        int xx = this.getCoordsInBaseBlock().y;
        int yy = this.getCoordsInBaseBlock().x;
        List<BlockField> possibleFires1 = new ArrayList<>(
                Arrays.asList(
                        neighBase1.getRightField(xx, yy),
                        neighBase1.getLeftField(xx, yy),
                        neighBase1.getDownField(xx, yy),
                        neighBase1.getUpField(xx, yy)));
        for (BlockField block1 : possibleFires1) {
            if (block1 != null) {
                if (block1.getItem() instanceof Fire) {
                    adjustScore(-5);
                    if (!burned) {
                        burned = true;
                        ((Fire)(block1.getItem())).playSecondarySoundFor(sounds);
                    }
                }
                BaseBlockNeigbours neighBase2 = w.getBaseBlockNeigboursByUniversal(block1.getUniversalCoords().y, block1.getUniversalCoords().x);
                int x = block1.getCoords().x;
                int y = block1.getCoords().y;
                List<BlockField> possibleFires2 = new ArrayList<>(
                        Arrays.asList(
                                neighBase2.getRightField(x, y),
                                neighBase2.getLeftField(x, y),
                                neighBase2.getDownField(x, y),
                                neighBase2.getUpField(x, y)));
                for (BlockField block2 : possibleFires2) {
                    if (block2 != null && block2.getItem() instanceof Fire) {
                        adjustScore(-1);
                        if (!burned) {
                            burned = true;
                            ((Fire)(block2.getItem())).playTercialSoundFor(sounds);
                        }
                    }
                }
            }
        }
    }

    public void setMouseUp() {
        setActionDirection(RatActions.WALK, RatActions.Direction.UP);
    }

    public void setMouseLeft() {
        setActionDirection(RatActions.WALK, RatActions.Direction.LEFT);
    }

    public void setMouseDown() {
        setActionDirection(RatActions.WALK, RatActions.Direction.DOWN);
    }

    public void setMouseRight() {
        setActionDirection(RatActions.WALK, RatActions.Direction.RIGHT);
    }

    public void disableSounds() {
        this.sounds = new SoundsBuffer.NoSound();
    }

    public void addScoreListener(ScoreListener scoreListener) {
        this.scoreListener = scoreListener;
    }

    public SoundsBuffer getSounds() {
        return sounds;
    }
}
