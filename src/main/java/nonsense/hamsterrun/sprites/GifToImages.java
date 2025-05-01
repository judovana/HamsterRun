package nonsense.hamsterrun.sprites;

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

public class GifToImages {

    public static List<BufferedImage> decodeCatched(URL u) {
        try{
            return decode(u);
        } catch (IOException e) {
               e.printStackTrace();
               return new ArrayList<>();
        }
    }

    public static List<BufferedImage> decode(URL u) throws IOException {
        List<BufferedImage> result = new ArrayList<>();
        String[] imageatt = new String[]{
                "imageLeftPosition",
                "imageTopPosition",
                "imageWidth",
                "imageHeight"
        };

        ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream ciis = ImageIO.createImageInputStream(u.openStream());
        reader.setInput(ciis, false);

        int noi = reader.getNumImages(true);
        BufferedImage master = null;

        for (int i = 0; i < noi; i++) {
            BufferedImage image = reader.read(i);
            IIOMetadata metadata = reader.getImageMetadata(i);

            Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
            NodeList children = tree.getChildNodes();

            for (int j = 0; j < children.getLength(); j++) {
                Node nodeItem = children.item(j);

                if (nodeItem.getNodeName().equals("ImageDescriptor")) {
                    Map<String, Integer> imageAttr = new HashMap<>();

                    for (int k = 0; k < imageatt.length; k++) {
                        NamedNodeMap attr = nodeItem.getAttributes();
                        Node attnode = attr.getNamedItem(imageatt[k]);
                        imageAttr.put(imageatt[k], Integer.valueOf(attnode.getNodeValue()));
                    }

                    //if (i == 0) { //this seems wrong - it draws all iages to single master
                        master = new BufferedImage(imageAttr.get("imageWidth"), imageAttr.get("imageHeight"), BufferedImage.TYPE_INT_ARGB);
                    //}
                    master.getGraphics().drawImage(image, imageAttr.get("imageLeftPosition"), imageAttr.get("imageTopPosition"), null);
                }
            }
            result.add(master);
        }
        return result;
    }
}
