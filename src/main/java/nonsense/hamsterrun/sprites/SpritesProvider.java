package nonsense.hamsterrun.sprites;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
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
    public static BufferedImage[] trapdoor;
    public static BufferedImage[] twoWayTeleport = new BufferedImage[6];
    public static BufferedImage[] oneWayTeleport = new BufferedImage[6];
    public static BufferedImage[] fire = new BufferedImage[6];
    public static BufferedImage[] glow = new BufferedImage[6];
    public static BufferedImage[] whirlStay = new BufferedImage[2];
    public static BufferedImage[] whirlMove = new BufferedImage[2];
    public static List<BufferedImage> floor = new ArrayList<>();

    public static void load() throws IOException {

        for (String rat : KNOWN_RATS) {
            BufferedImage[] runBase = new BufferedImage[2];
            for (int x = 1; x <= 2; x++) {
                URL runUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/run" + x + ".png");
                runBase[x - 1] = ImageIO.read(runUrl);
            }
            BufferedImage[] fallBase = new BufferedImage[10];
            for (int x = 1; x <= 10; x++) {
                URL fallUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/fall" + x + ".png");
                fallBase[x - 1] = ImageIO.read(fallUrl);
            }

            URL situ = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/sit.png");
            BufferedImage sit = ImageIO.read(situ);
            ratSprites = new RatSpriteSet(runBase, sit, fallBase);
        }
        URL wall1u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/wall.png");
        wall = ImageIO.read(wall1u);
        URL okurkaU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/okurka.png");
        okurka = ImageIO.read(okurkaU);
        URL trapDoorUrl1 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/trapdoor1.png");
        URL trapDoorUrl2 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/trapdoor2.png");
        trapdoor = new BufferedImage[] {ImageIO.read(trapDoorUrl1), ImageIO.read(trapDoorUrl2)};
        for (int x = 1; x <= 5; x++) {
            URL teleUrl1 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/twoWayTeleport" + x + ".png");
            twoWayTeleport[x - 1] = ImageIO.read(teleUrl1);
            URL teleUrl2 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/oneWayTeleport" + x + ".png");
            oneWayTeleport[x - 1] = ImageIO.read(teleUrl2);
        }
        for (int x = 1; x <= 6; x++) {
            URL fireUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/fire" + x + ".png");
            fire[x - 1] = ImageIO.read(fireUrl);
        }
        for (int x = 1; x <= 4; x++) {
            URL glowUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/glow" + x + ".png");
            glow[x - 1] = ImageIO.read(glowUrl);
        }
        for (int x = 1; x <= 2; x++) {
            URL bladeUrl0 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/blades0" + x + ".png");
            whirlStay[x - 1] = ImageIO.read(bladeUrl0);
        }
        for (int x = 1; x <= 2; x++) {
            URL bladeUrl1 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/blades1" + x + ".png");
            whirlMove[x - 1] = ImageIO.read(bladeUrl1);
        }
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

    public static BufferedImage getTwoWayTeleport(int i) {
        return SpritesProvider.twoWayTeleport[i];
    }

    public static BufferedImage getOneWayTeleport(int i) {
        return SpritesProvider.oneWayTeleport[i];
    }
}
