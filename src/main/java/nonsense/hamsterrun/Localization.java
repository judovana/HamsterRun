package nonsense.hamsterrun;


import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {

    private static Localization instance = new Localization();
    private ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static Localization get() {
        return instance;
    }

    public String get(String key) {
        return bundle.getString(key);
    }

    public String getHello() {
        return get("hello");
    }


    public void toUs() {
        bundle = ResourceBundle.getBundle("messages", Locale.US);
    }

    public void toCz() {
        bundle = ResourceBundle.getBundle("messages", Locale.forLanguageTag("cs-CZ"));
    }


    public String getMainTitle() {
        return get("mainTitle");
    }

    public String getWorldTitle() {
        return get("worldTitle");
    }

    public String getMiscTitle() {
        return get("miscTitle");
    }

    public String getItemsTitle() {
        return get("itemsTitle");
    }

    public String getBaseConfigLabel() {
        return get("baseConfiglabel");
    }

    public String getGridConfigLabel() {
        return get("gridConfiglabel");
    }

    public String getRegSpeedLabel() {
        return get("regSpeedLabel");
    }

    public String baseDensityMaxLabel() {
        return get("baseDensityMaxLabel");
    }

    public String baseDensityMinLabel() {
        return get("baseDensityMinLabel");
    }

    public String gridConnectivityMinLabel() {
        return get("gridConnectivityMinLabel");
    }

    public String gridConnectivityMaxLabel() {
        return get("gridConnectivityMaxLabel");
    }

    public String getRatsTitle() {
        return get("ratsTitle");
    }

    public String getAddMousesButton1() {
        return get("addMousesButton1");
    }

    public String getAddMousesButton2() {
        return get("addMousesButton2");
    }

    public String getColumnsLabel() {
        return get("columnsLabel");
    }

    public String getStartGame() {
        return get("startGame");
    }

    public String delayMs() {
        return get("delayMs");
    }

    public String getKeepRegenerating() {
        return get("keepRegenerating");
    }

    public String getRemoveMe() {
        return get("removeMe");
    }
}
