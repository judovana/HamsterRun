package nonsense.hamsterrun.ratcontroll;

import nonsense.hamsterrun.env.Rat;

public class RatsController {

    private interface RatControl {

    }

    private static abstract class  KeyboardControl implements  RatControl {

    }

    private static  class  KeyboardControl1 extends KeyboardControl {

    }

    private static  class  KeyboardControl2 extends KeyboardControl {

    }

    private static  class  KeyboardControl3 extends KeyboardControl {

    }

    private static  class  MouseControl implements RatControl {

    }
    private static  class  ComputerControl implements RatControl {

    }

    private static class RatWithControls {
        private final Rat rat;
        private final RatControl ratControl;

        public RatWithControls(Rat rat, RatControl ratControl) {
            this.rat = rat;
            this.ratControl = ratControl;
        }
    }
}
