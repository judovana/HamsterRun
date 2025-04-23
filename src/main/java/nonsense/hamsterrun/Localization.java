package nonsense.hamsterrun;


import java.util.Locale;
import java.util.ResourceBundle;

public class Localization {

    private static Localization instance = new Localization();

    public static Localization get() {
        return instance;
    }

    private ResourceBundle bundle = ResourceBundle.getBundle("messages");

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


}
