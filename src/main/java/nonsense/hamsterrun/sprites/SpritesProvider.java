package nonsense.hamsterrun.sprites;

import nonsense.hamsterrun.BaseConfig;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpritesProvider {

    public static final List<String> KNOWN_RATS = List.of("virecek", "hnedulka", "brownie", "uhlicek", "sneci", "rat");
    public static final List<String> KNOWN_ALIENS = List.of("bigBats", "bigFlies", "boulder", "key", "smallBats", "smallFlies", "falcon", "cat1", "cat2", "ghost1", "ghost2");
    public static final List<String> KNOWN_FLOORS = List.of("floor", "fire");
    public static final Map<String, RatSpriteSet> ratSprites = new HashMap<>();
    public static final Map<String, List<BufferedImage>> alienSprites = new HashMap<>();
    public static BufferedImage wall;
    public static BufferedImage tunnelOpened;
    public static BufferedImage[] tunnelClosed = new BufferedImage[4];
    public static BufferedImage okurka;
    public static BufferedImage pepper;
    public static BufferedImage carrot;
    public static BufferedImage salat;
    public static BufferedImage repa;
    public static BufferedImage grass;
    public static BufferedImage gate;
    public static BufferedImage fs;
    public static BufferedImage[] trapdoor;
    public static BufferedImage[] twoWayTeleport = new BufferedImage[6];
    public static BufferedImage[] oneWayTeleport = new BufferedImage[6];
    public static BufferedImage[] allWayTeleport = new BufferedImage[6];
    public static BufferedImage[] fire = new BufferedImage[6];
    public static BufferedImage[] glow = new BufferedImage[6];
    public static BufferedImage[] whirlStay = new BufferedImage[2];
    public static BufferedImage[] whirlMove = new BufferedImage[2];
    public static BufferedImage[] houbicky = new BufferedImage[4];
    public static BufferedImage[] flask = new BufferedImage[3];
    public static BufferedImage[] water = new BufferedImage[4];
    public static List<BufferedImage> floor = new ArrayList<>();

    public static void load() throws IOException {

        for (String rat : KNOWN_RATS) {
            List<BufferedImage> runBase = new ArrayList<>(2);
            for (int x = 1; x <= 20; x++) {
                URL runUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/run" + x + ".png");
                if (runUrl == null) {
                    break;
                }
                runBase.add(ImageIO.read(runUrl));
            }
            List<BufferedImage> fallBase = new ArrayList<>(2);
            for (int x = 1; x <= 20; x++) {
                URL fallUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/fall" + x + ".png");
                if (fallUrl == null) {
                    break;
                }
                fallBase.add(ImageIO.read(fallUrl));
            }

            URL situ = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/sit.png");
            BufferedImage sit = ImageIO.read(situ);

            URL eatu = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + rat + "/eat.png");
            BufferedImage eat = null;
            if (eatu != null) {
                eat = ImageIO.read(eatu);
            }

            RatSpriteSet ratSprite = new RatSpriteSet(runBase.toArray(new BufferedImage[0]), sit, fallBase.toArray(new BufferedImage[0]), eat);
            ratSprites.put(rat, ratSprite);

        }
        URL tunnelCloseU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/tunnel1off.png");
        tunnelClosed[0] = ImageIO.read(tunnelCloseU);
        tunnelClosed[1] = rotate(tunnelClosed[0], 90);
        tunnelClosed[2] = rotate(tunnelClosed[0], 180);
        tunnelClosed[3] = rotate(tunnelClosed[0], 270);
        URL fsU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/aliens/falcon_shadow.png");
        fs = ImageIO.read(fsU);
        URL gateU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/gate.png");
        gate = ImageIO.read(gateU);
        URL tunnelU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/tunnelComposed.png");
        tunnelOpened = ImageIO.read(tunnelU);
        URL wall1u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/wall.png");
        wall = ImageIO.read(wall1u);
        URL salatU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/salaty.png");
        salat = ImageIO.read(salatU);
        URL grassU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/grass.png");
        grass = ImageIO.read(grassU);
        URL repaU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/repa.png");
        repa = ImageIO.read(repaU);
        URL pepperU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/pepper.png");
        pepper = ImageIO.read(pepperU);
        URL carrotU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/carrot.png");
        carrot = ImageIO.read(carrotU);
        URL okurkaU = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/okurka.png");
        okurka = ImageIO.read(okurkaU);
        URL trapDoorUrl1 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/trapdoor1.png");
        URL trapDoorUrl2 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/trapdoor2.png");
        trapdoor = new BufferedImage[]{ImageIO.read(trapDoorUrl1), ImageIO.read(trapDoorUrl2)};
        for (int x = 1; x <= 5; x++) {
            URL teleUrl1 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/twoWayTeleport" + x + ".png");
            twoWayTeleport[x - 1] = ImageIO.read(teleUrl1);
            URL teleUrl2 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/oneWayTeleport" + x + ".png");
            oneWayTeleport[x - 1] = ImageIO.read(teleUrl2);
            URL teleUrl3 = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/allWayTeleport" + x + ".png");
            allWayTeleport[x - 1] = ImageIO.read(teleUrl3);
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
        for (int x = 1; x <= 4; x++) {
            URL houbickyUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/houbicky" + x + ".png");
            houbicky[x - 1] = ImageIO.read(houbickyUrl);
        }
        for (int x = 3; x <= 4; x++) {
            URL waterUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/water" + x + ".png");
            water[x - 3] = ImageIO.read(waterUrl);
        }
        for (int x = 1; x <= 3; x++) {
            URL flaskUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/flask" + x + ".png");
            flask[x - 1] = ImageIO.read(flaskUrl);
        }

        recreateFloor();

        for (String alien : KNOWN_ALIENS) {
            URL au = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/aliens/" + alien + ".gif");
            List<BufferedImage> imgs = GifToImages.decodeCatched(au);
            alienSprites.put(alien, imgs);
        }
    }

    public static void recreateFloor() throws IOException {
        recreateFloor(BaseConfig.getConfig().getFloor());
    }

    private static void recreateFloor(String name) throws IOException {
        floor.clear();
        URL floor1u = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/" + name + ".png");
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

    public static BufferedImage getAllWayTeleport(int i) {
        return SpritesProvider.allWayTeleport[i];
    }

    public static BufferedImage getMushroom(int anim) {
        return houbicky[anim];
    }

    public static BufferedImage getAlien(String name, int anim) {
        return alienSprites.get(name).get(anim % getAlienSize(name));
    }

    public static int getAlienSize(String name) {
        return alienSprites.get(name).size();
    }

    public static BufferedImage getFalonShaadow() {
        return fs;
    }
}
