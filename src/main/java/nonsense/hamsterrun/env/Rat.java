package nonsense.hamsterrun.env;


import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.aliens.MovingOne;
import nonsense.hamsterrun.env.traps.ColorfullFlask;
import nonsense.hamsterrun.env.traps.Fire;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Item;
import nonsense.hamsterrun.env.traps.Mushroom;
import nonsense.hamsterrun.env.traps.Relocator;
import nonsense.hamsterrun.env.traps.Repa;
import nonsense.hamsterrun.env.traps.Salat;
import nonsense.hamsterrun.env.traps.Teleport;
import nonsense.hamsterrun.env.traps.Torturer;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.Vegetable;
import nonsense.hamsterrun.env.traps.Water;
import nonsense.hamsterrun.ratcontroll.ScoreListener;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rat extends MovingOne {

    private SoundsBuffer sounds = new SoundsBuffer();
    private SoundsBuffer snail = new SoundsBuffer.NoSound();
    private int score = 1000;
    private String skin = "rat";
    private ScoreListener scoreListener;

    public Rat(SoundsBuffer sounds) {
        this.setSounds(sounds);
    }

    public Rat() {
    }

    public Rat(Point world, Point block) {
        super(world, block);
    }

    public Rat(int worldx, int worldy, int blockx, int blocky) {
        super(worldx, worldy, blockx, blocky);
    }

    @Override
    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    @Override
    protected BufferedImage getImageForAction(String skin) {
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

    private void stop(World world) {
        speed = 1;
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            this.action = RatActions.EAT;
        } else {
            if (this.action != RatActions.STAY) {
                getSounds().addToEatQueue(SoundsBuffer.piskWeird);
            }
            this.action = RatActions.STAY;
        }
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Mushroom) {
            ((Mushroom) (world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.getSounds());
            world.swap(this);
            this.action = RatActions.STAY;
        }
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof ColorfullFlask) {
            ((ColorfullFlask) (world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.getSounds());
            this.skin = SpritesProvider.KNOWN_RATS.get(seed.nextInt(SpritesProvider.KNOWN_RATS.size()));
            this.action = RatActions.STAY;
        }
    }

    @Override
    public void adjustSpeedBeforeActionDirection(RatActions.Direction futureDirection) {
        if (this.action == RatActions.WALK && this.direction == futureDirection) {
            speed++;
            if (speed == MAGICAL_FALL_CHANCE - 1) {
                getSounds().addToMoveQueue(SoundsBuffer.turbo);
            }
            if (speed >= relativeSizes * 2) {
                speed = relativeSizes * 2;
            }
        } else {
            speed = 1;
        }
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
                ((InvisibleTrapDoor) (world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.getSounds());
            }
        }
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Teleport && seed.nextInt(MAGICAL_FALL_CHANCE) + 1 > speed) {
            if (this.action != RatActions.FALLING) {
                this.anim.reset();
                this.action = RatActions.FALLING;
                ((Teleport) world.getBlockField(getUniversalCoords()).getItem()).playMainSoundFor(this.getSounds());
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
        //logNeighbours(world);
        if (world.getBlockField(this.getUniversalCoords()).getItem() instanceof Tunnel) {
            if (seed.nextInt(BaseConfig.getConfig().getTunnelConfusionFactor()) == 0) {
                direction = RatActions.Direction.getRandom();
                ((Tunnel) (world.getBlockField(getUniversalCoords()).getItem())).playMainSoundFor(this.getSounds());
            }
        } else if (world.getBlockField(getUniversalCoords()).getItem() instanceof Repa && !((Repa) (world.getBlockField(getUniversalCoords()).getItem())).eaten()) {
            if (action != RatActions.EAT) {
                direction = RatActions.Direction.getRandom();
            }
        } else if (world.getBlockField(this.getUniversalCoords()).getItem() instanceof Water) {
            speed = 1;
            if (seed.nextInt(20) < 18) {
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
        harm(world);
        move(world);
    }

    private void logNeighbours(World world) {
        Point uc = getUniversalCoords();
        Teleport.OrientedList<BlockField> sides = Teleport.getNeighboursFields(new Point(uc.x, uc.y), world);
        if (sides.getUpField() != null && sides.getUpField().isPassable()) {
            //this.direction = RatActions.Direction.UP;
            System.out.println("up is free");
        } else {
            System.out.println("up is full");
        }
        if (sides.getDownField() != null && sides.getDownField().isPassable()) {
            //this.direction = RatActions.Direction.DOWN;
            System.out.println("down is free");
        } else {
            System.out.println("down is full");
        }
        if (sides.getRightField() != null && sides.getRightField().isPassable()) {
            //this.direction = RatActions.Direction.RIGHT;
            System.out.println("right is free");
        } else {
            System.out.println("right is full");
        }
        if (sides.getLeftField() != null && sides.getLeftField().isPassable()) {
            //this.direction = RatActions.Direction.LEFT;
            System.out.println("left is free");
        } else {
            System.out.println("left is full");
        }
    }

    private void eat(World world) {
        speed = 1;
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            Point origCoords = getUniversalCoords();
            if (anim.everyThird()) {
                direction = direction.rotateCW();
                ((Vegetable) world.getBlockField(getUniversalCoords()).getItem()).eat(this, world);
                if (((Vegetable) world.getBlockField(origCoords).getItem()).eaten()) {
                    world.getBlockField(origCoords).clear();
                } else {
                    ((Vegetable) (world.getBlockField(origCoords).getItem())).playMainSoundFor(this.getSounds());
                }
                harm(world);
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
            adjustScore(-5 * speed * speed * speed);
            if (speed == MAGICAL_FALL_CHANCE - 1) {
                ((Torturer) w.getBlockField(this.getUniversalCoords()).getItem()).playMainSoundFor(this.getSounds());
            } else {
                ((Torturer) w.getBlockField(this.getUniversalCoords()).getItem()).playSecondarySoundFor(this.getSounds());
            }
        }
        boolean burned = false;
        if (field instanceof Fire) {
            adjustScore(-20);
            if (!burned) {
                burned = true;
                ((Fire) field).playMainSoundFor(getSounds());
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
                        ((Fire) (block1.getItem())).playSecondarySoundFor(getSounds());
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
                            ((Fire) (block2.getItem())).playTercialSoundFor(getSounds());
                        }
                    }
                }
            }
        }
    }

    public void disableSounds() {
        this.setSounds(new SoundsBuffer.NoSound());
    }

    public void addScoreListener(ScoreListener scoreListener) {
        this.scoreListener = scoreListener;
    }

    public SoundsBuffer getSounds() {
        if ("sneci".equals(skin)) {
            return snail;
        } else {
            return sounds;
        }
    }

    public void setSounds(SoundsBuffer sounds) {
        if (sounds != null) {
            sounds.kill();
        }
        this.sounds = sounds;
    }

    @Override
     protected boolean returnOnSalat(World world) {
        if (action == RatActions.EAT
                && world.getBlockField(getUniversalCoords()).getItem() instanceof Salat
                && !((Salat) (world.getBlockField(getUniversalCoords()).getItem())).eaten()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean selfAct(World world) {
        throw new RuntimeException("Rat is moved by its controller");
    }

    @Override
    public void interact(Rat world) {
        throw new RuntimeException("Rat can not interact");
    }

    @Override
    protected boolean getMouseBlock() {
        //TODO, make this setup-able?
        return true;
    }

}
