
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class CursorUpdater {
    private JFrame jf;

    public CursorUpdater(JFrame jf) {
        this.jf = jf;
    }

    public void hide() {
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(
            new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
            new Point(0, 0),
            "invisibleCursor"
        );
        jf.setCursor(invisibleCursor);
    }

    public void show() {
        jf.setCursor(Cursor.getDefaultCursor());
    }
}
