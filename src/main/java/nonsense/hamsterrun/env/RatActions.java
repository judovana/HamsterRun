package nonsense.hamsterrun.env;

import java.util.Random;

public enum RatActions {

    STAY, WALK, EAT, FALLING(false);

    public enum Direction {
        UP(0), RIGHT(1), DOWN(2), LEFT(3);

        private static final Random seed = new Random();
        private final int sprite;

        Direction(int sprite) {
            this.sprite = sprite;
        }

        public static Direction getRandom() {
            int random = seed.nextInt(4);
            return Direction.values()[random];
        }

        public int getSprite() {
            return sprite;
        }

        public Direction rotateCW() {
            switch (this) {
                case UP:
                    return RIGHT;
                case RIGHT:
                    return DOWN;
                case DOWN:
                    return LEFT;
                case LEFT:
                    return UP;
                default:
                    throw new RuntimeException("Can not rotate: " + this);
            }
        }

        public Direction rotateCCW() {
            switch (this) {
                case UP:
                    return LEFT;
                case RIGHT:
                    return UP;
                case DOWN:
                    return RIGHT;
                case LEFT:
                    return DOWN;
                default:
                    throw new RuntimeException("Can not rotate: " + this);
            }
        }
    }


    private final boolean interruptible;

    RatActions() {
        interruptible = true;
    }

    RatActions(boolean interruptible) {
        this.interruptible = interruptible;
    }

    public static boolean isStay(RatActions action) {
        return action == STAY;
    }

    public static boolean isWalk(RatActions action) {
        return action == WALK;
    }


    public boolean isInterruptible() {
        return interruptible;
    }
}
