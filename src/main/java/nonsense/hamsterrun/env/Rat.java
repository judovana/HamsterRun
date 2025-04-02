package nonsense.hamsterrun.env;


import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import nonsense.hamsterrun.BaseConfig;
import nonsense.hamsterrun.env.traps.AnimationCounrer;
import nonsense.hamsterrun.env.traps.InvisibleTrapDoor;
import nonsense.hamsterrun.env.traps.Tunnel;
import nonsense.hamsterrun.env.traps.Vegetable;
import nonsense.hamsterrun.sprites.SpritesProvider;

public class Rat {

    private static final Random seed = new Random();
    private AnimationCounrer anim = new AnimationCounrer();

    private Point coordsInBaseBlock = new Point(-1, -1);
    private Point coordsInMaze = new Point(-1, -1);
    private RatActions action = RatActions.STAY;
    private RatActions.Direction direction = RatActions.Direction.UP;
    private Point relativeCoordInSquare = new Point(0, 0);
    private static final int relativeSizes = 5;

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

    public Point getCoordsInBaseBlock() {
        return coordsInBaseBlock;
    }

    public Point getCoordsInMaze() {
        return coordsInMaze;
    }

    public void setCoordsInBaseBlock(Point coordsInBaseBlock) {
        this.coordsInBaseBlock = coordsInBaseBlock;
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
        return new Point(coordsInMaze.x * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.x,
                coordsInMaze.y * BaseConfig.getConfig().getBaseSize() + coordsInBaseBlock.y);
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
    public void draw(Graphics2D g2d, Point leftUpCornerOfMaze, int zoom, boolean useInplaceSubMovement) {
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
            BufferedImage img = getImageForAction();
            g2d.drawImage(img, leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, zoom, zoom, null);
        } else {
            g2d.fillRect(leftUpCornerOfMaze.x + coord.x * zoom + relativeShift.x, leftUpCornerOfMaze.y + coord.y * zoom + relativeShift.y, zoom, zoom);
        }
    }

    private BufferedImage getImageForAction() {
        if (RatActions.isStay(action) || action == RatActions.EAT) {
            return SpritesProvider.ratSprites.getSit(direction.getSprite(), anim.ignore());
        } else if (RatActions.isWalk(action)) {
            return SpritesProvider.ratSprites.getRun(direction.getSprite(), anim.everyOdd());
        } else if (action == RatActions.FALLING) {
            return SpritesProvider.ratSprites.getFall(direction.getSprite(), anim.every10());
        } else {
            throw new RuntimeException("Unknown acction " + action);
        }
    }

    private void moveMouseRight(World world) {
        relativeCoordInSquare.x++;
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
        relativeCoordInSquare.y--;
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
        relativeCoordInSquare.x--;
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
        relativeCoordInSquare.y++;
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
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            this.action = RatActions.EAT;
        } else {
            this.action = RatActions.STAY;
        }
    }

    public void setActionDirection(World world, RatActions action, RatActions.Direction direction) {
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
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof InvisibleTrapDoor) {
            if (this.action != RatActions.FALLING) {
                this.anim.reset();
                this.action = RatActions.FALLING;
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
        }
    }

    private void moveInDirection(World world) {
        if (world.getBlockField(this.getUniversalCoords()).getItem() instanceof Tunnel) {
            if (seed.nextInt(20) == 0) {
                direction = RatActions.Direction.getRandom();
            }
        }
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
        if (world.getBlockField(getUniversalCoords()).getItem() instanceof Vegetable) {
            if (anim.everyThird()) {
                direction = direction.rotateCW();
                boolean eaten = ((Vegetable) world.getBlockField(getUniversalCoords()).getItem()).eat();
                if (eaten) {
                    world.getBlockField(getUniversalCoords()).clear();
                }
            }
        } else {
            action = RatActions.STAY;
        }
    }

    private void fall(World world) {
        if (anim.anim == 10) {
            anim.reset();
            world.teleportMouse(this, false, true);
            direction = RatActions.Direction.getRandom();
            action = RatActions.STAY;
        } else {
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

}
