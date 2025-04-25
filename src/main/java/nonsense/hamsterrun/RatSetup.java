package nonsense.hamsterrun;

import nonsense.hamsterrun.ratcontroll.ComputerControl;
import nonsense.hamsterrun.ratcontroll.KeyboardControl1;
import nonsense.hamsterrun.ratcontroll.KeyboardControl2;
import nonsense.hamsterrun.ratcontroll.KeyboardControl3;
import nonsense.hamsterrun.ratcontroll.MouseControl;
import nonsense.hamsterrun.ratcontroll.RatsController;
import nonsense.hamsterrun.sprites.SpritesProvider;

import java.util.stream.Collectors;

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
                        control = new ComputerControl();
                    } else if (param.equalsIgnoreCase("k1")) {
                        control = new KeyboardControl1();
                    } else if (param.equalsIgnoreCase("k2")) {
                        control = new KeyboardControl2();
                    } else if (param.equalsIgnoreCase("k3")) {
                        control = new KeyboardControl3();
                    } else if (param.equalsIgnoreCase("m1")) {
                        control = new MouseControl();
                    } else {
                        throw new RuntimeException("unknown param in rat def: " + param + " in " + def);
                    }
                    break;
            }
        }
        return new RatSetup(def, aiChaos, display, skin, control);
    }

    @Override
    public String toString() {
        return control.id() + ":" + skin + ":" + display + ":" + aiChaos;
    }


}
