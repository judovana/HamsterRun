package nonsense.hamsterrun.env;

public enum RatActions {

    STAY, WALK;

    public enum Direction {
        UP(0), RIGHT(1), DOWN(2), LEFT(3);

        private final int sprite;

        Direction(int sprite) {
            this.sprite = sprite;
        }

        public int getSprite() {
            return sprite;
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
