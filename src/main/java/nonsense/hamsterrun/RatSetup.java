package nonsense.hamsterrun;

import nonsense.hamsterrun.ratcontroll.RatsController;


public class RatSetup extends  VirtualRatSetup{

    final RatsController.RatControl control;


    public RatSetup(String def, int aiChaos, boolean display, String skin, RatsController.RatControl control) {
        super(def, aiChaos, display, skin, control.id());
        this.control = control;
    }

    //syntax control:skin:haveDisplay:aiModifier  eg k1:uhlicek:true  or pc:rat:false:10
    public static RatSetup parse(String def) {
        VirtualRatSetup virtualRat = VirtualRatSetup.parse(def);
        return new RatSetup(def, virtualRat.aiChaos, virtualRat.display, virtualRat.skin, VirtualRatSetup.stringToRatControl(def, virtualRat.controlDef));
    }

    @Override
    public String toString() {
        return control.id() + ":" + skin + ":" + display + ":" + aiChaos;
    }


}
