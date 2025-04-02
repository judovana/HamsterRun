package nonsense.hamsterrun.sprites;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpritesProvider {

    private static final List<String> KNOWN_RATS = List.of("rat");
    public static RatSpriteSet ratSprites;
    public static BufferedImage wall;
    public static BufferedImage okurka;
    public static List<BufferedImage> floor = new ArrayList<>();

    public static void load() throws IOException {
        for (String rat : KNOWN_RATS) {
            URL run1u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/run1.png");
            BufferedImage run1 = ImageIO.read(run1u);
            URL run2u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/run2.png");
            BufferedImage run2 = ImageIO.read(run2u);
            URL situ = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/sit.png");
            BufferedImage sit = ImageIO.read(situ);
            ratSprites = new RatSpriteSet(run1, run2, sit);
        }
        URL wall1u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/wall.png");
        wall = ImageIO.read(wall1u);
        URL okurkaU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/okurka.png");
        okurka = ImageIO.read(okurkaU);
        URL floor1u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/floor.png");
        BufferedImage floorI = ImageIO.read(floor1u);
        for (int z = 1; z < 10; z++) {
            floor.add(multiply(floorI, z));
        }
    }

    public static BufferedImage getFloor(int zoom) {
        for (int i = 1; i < floor.size(); i++) {
            if (zoom < floor.get(i).getWidth() || zoom < floor.get(i).getHeight()) {
                return floor.get((i - 1));
            }
        }
        return floor.get(floor.size() - 1);
    }

    public static BufferedImage multiply(BufferedImage img, double zoom) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int neww = (int) Math.floor(w * zoom);
        int newh = (int) Math.floor(h * zoom);

        BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimg.createGraphics();
        for (int x = 0; x < Math.max(1, neww / w); x++) {
            for (int y = 0; y < Math.max(1, newh / h); y++) {
                g.drawImage(img, x * w, y * h, null);
            }
        }
        g.dispose();
        return bimg;
    }

    public static BufferedImage rotate(BufferedImage img, double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int neww = (int) Math.floor(w * cos + h * sin);
        int newh = (int) Math.floor(h * cos + w * sin);

        BufferedImage bimg = new BufferedImage(neww, newh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimg.createGraphics();

        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(img, null);
        g.dispose();
        return bimg;
    }

}
