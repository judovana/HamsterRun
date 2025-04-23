package nonsense.hamsterrun;

import java.util.stream.Collectors;

import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.sprites.SpritesProvider;

public class RatSetup {

    final String def;
    final int aiChaos;
    final boolean display;
    final String skin;
    final RatsController.RatControl control;


    public RatSetup(String def, int aiChaos, boolean display, String skin, RatsController.RatControl control) {
        this.def = def;
        this.aiChaos = aiChaos;
        this.display = display;
        this.skin = skin;
        this.control = control;
    }

    @Override
    public String toString() {
        return control.id()+":"+skin+":"+display+":"+aiChaos;
    }

    //syntax control:skin:haveDisplay:aiModifier  eg k1:uhlicek:true  or pc:rat:false:10
    public static RatSetup parse(String def) {
        int aiChaos = RatsController.DEFAULT_CHAOS;
        boolean display = false;
        String skin = null;
        RatsController.RatControl control = null;
        String[] rataParams = def.split(":");
        for (int i = 0; i < rataParams.length; i++) {
            String param = rataParams[i];
            switch (i) {
                case 3:
                    aiChaos = Integer.valueOf(param);
                    break;
                case 2:
                    display = Boolean.valueOf(param);
                    break;
                case 1:
                    if (SpritesProvider.KNOWN_RATS.contains(param)) {
                        skin = param;
                    } else {
                        throw new RuntimeException("Unknown sprite " + param + ". Available are " + SpritesProvider.KNOWN_RATS.stream().collect(
                                Collectors.joining(",")));
                    }
                    break;
                case 0:
                    if (param.equalsIgnoreCase("pc")) {
                        control = new RatsController.ComputerControl();
                    } else if (param.equalsIgnoreCase("k1")) {
                        control = new RatsController.KeyboardControl1();
                    } else if (param.equalsIgnoreCase("k2")) {
                        control = new RatsController.KeyboardControl2();
                    } else if (param.equalsIgnoreCase("k3")) {
                        control = new RatsController.KeyboardControl3();
                    } else if (param.equalsIgnoreCase("m1")) {
                        control = new RatsController.MouseControl();
                    } else {
                        throw new RuntimeException("unknown param in rat def: " + param + " in " + def);
                    }
                    break;
            }
        }
        return new RatSetup(def, aiChaos, display, skin, control);
    }


}
