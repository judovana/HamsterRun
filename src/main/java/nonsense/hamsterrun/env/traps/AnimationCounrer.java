package nonsense.hamsterrun.env.traps;

public class AnimationCounrer {

    public int anim;

    public int everyOdd() {
        return anim % 2;
    }

    public int mod(int i) {
        return anim % i;
    }

    public int modMap() {
        return anim % 15;
    }

    public int every10() {
        return anim % 10;
    }

    public int ignore() {
        return 0;
    }

    public void addLimited() {
        this.anim++;
        if (anim >= 100) {
            anim = 0;
        }
    }

    public boolean everyThird() {
        return anim % 3 == 0;
    }

    public void reset() {
        anim = 0;
    }
}
