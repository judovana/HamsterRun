package nonsense.hamsterrun;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BaseConfigTest {


    public static BaseConfigTest baseConfig = new BaseConfigTest();


    @Test
    public void getDensityTest() {
        BaseConfig config = new BaseConfig();
        for(int x = 0; x < 1000; x++) {
            int d = config.getDensity();
            Assertions.assertTrue(d>=config.baseDensityMin);
            Assertions.assertTrue(d<=config.baseDensityMax);
        }
        config.baseDensityMin = 2;
        config.baseDensityMax = 2;
        for(int x = 0; x < 1000; x++) {
            int d = config.getDensity();
            Assertions.assertEquals(2, d);
        }
    }

    @Test
    public void getConnectivityTest() {
        BaseConfig config = new BaseConfig();
        for(int x = 0; x < 1000; x++) {
            int d = config.getConnectivity();
            Assertions.assertTrue(d>=config.gridConnectivityMin);
            Assertions.assertTrue(d<=config.gridConnectivityMax);
        }
        config.gridConnectivityMin = 2;
        config.gridConnectivityMax = 2;
        for(int x = 0; x < 1000; x++) {
            int d = config.getConnectivity();
            Assertions.assertEquals(2, d);
        }
    }
}
