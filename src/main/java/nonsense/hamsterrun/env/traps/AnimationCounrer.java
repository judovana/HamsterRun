package nonsense.hamsterrun.env.traps;

public class AnimationCounrer {

    public int anim;

    public int everyOdd() {
        return anim % 2;
    }

    public int ignore() {
        return 0;
    }

    public void addLimited() {
        this.anim++;
        if (anim >= 10) {
            anim = 0;
        }
    }

    public boolean everyThird() {
        return anim % 3 == 0;
    }
}
