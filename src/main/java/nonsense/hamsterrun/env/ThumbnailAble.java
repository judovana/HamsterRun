package nonsense.hamsterrun.env;

import java.awt.Graphics2D;

public interface ThumbnailAble {

    void drawThumbnail(Graphics2D g2d, int size);

    void playMainSoundFor(SoundsBuffer rat);

    default void playSecondarySoundFor(SoundsBuffer rat) {
    }

    default void playTercialSoundFor(SoundsBuffer rat) {
    }
}
