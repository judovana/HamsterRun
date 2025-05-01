package nonsense.hamsterrun.env;

import nonsense.hamsterrun.sprites.GifToImages;
import nonsense.hamsterrun.sprites.SpritesProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GifToImagesTest {

    @Test
    public void decodeTest1() throws IOException {
        URL fallUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/aliens/boulder.gif");
        List<BufferedImage> r = GifToImages.decode(fallUrl);
        Assertions.assertEquals(2, r.size());
        for (BufferedImage img: r){
            Assertions.assertEquals(img.getWidth(),200);
            Assertions.assertEquals(img.getHeight(),200);
        }
        for (int i=1; i < r.size(); i++){
            Assertions.assertNotEquals(r.get(i-1), r.get(i));
        }
    }

    @Test
    public void decodeTest2() throws IOException {
        URL fallUrl = SpritesProvider.class.getClassLoader().getResource("nonsense/hamsterrun/sprites/aliens/bigBats.gif");
        List<BufferedImage> r = GifToImages.decode(fallUrl);
        Assertions.assertEquals(50, r.size());
        for (BufferedImage img: r){
            Assertions.assertEquals(img.getWidth(),800);
            Assertions.assertEquals(img.getHeight(),800);
        }
        for (int i=1; i < r.size(); i++){
            Assertions.assertNotEquals(r.get(i-1), r.get(i));
        }
    }
}
