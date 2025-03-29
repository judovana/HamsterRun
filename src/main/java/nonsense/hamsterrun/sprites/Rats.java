package nonsense.hamsterrun.sprites;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Rats {

    private static final List<String> KNOWN_RATS= List.of("rat");
    public static SpriteSet ratSprites;

    public static void load() throws IOException {
        for (String rat: KNOWN_RATS) {
            URL run1u = Rats.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/"+rat+"/run1.png");
            BufferedImage run1 = ImageIO.read(run1u);
            URL run2u = Rats.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/"+rat+"/run2.png");
            BufferedImage run2 = ImageIO.read(run2u);
            URL situ = Rats.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/"+rat+"/sit.png");
            BufferedImage sit = ImageIO.read(situ);
            ratSprites = new SpriteSet(run1, run2, sit);
        }

    }

}
