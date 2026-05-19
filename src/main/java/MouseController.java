import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;


public class MouseController implements MouseMotionListener, MouseListener  {
    private float yaw = 3.13f;
    private float pitch = 0.0f;
    private float mouseSensitivity = GlobalConfig.MOUSE_SENSITIVITY;

    private int width, height;

    private CursorUpdater cu;

    public MouseController(int width, int height, CursorUpdater cu) {
        this.width = width;
        this.height = height;
        this.cu = cu;
    }

    public void moveMouse(Point p) {
        if (OSChecker.isWayland()) moveWayland(p);
        else move(p);
    }

    private void move(Point p) {
            GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        for (GraphicsDevice device: gs) {
            GraphicsConfiguration[] configurations =
                    device.getConfigurations();
            for (GraphicsConfiguration config: configurations) {
                Rectangle bounds = config.getBounds();
                if(bounds.contains(p)) {

                    Point b = bounds.getLocation();
                    Point s = new Point(p.x - b.x, p.y - b.y);

                    try {
                        Robot r = new Robot(device);
                        r.mouseMove(s.x, s.y);
                    } catch (AWTException es) {
                        es.printStackTrace();
                    }

                    return;
                }
            }
        }
    }

    private static void moveWayland(Point p) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "hyprctl", "dispatch", "movecursor", 
                    String.valueOf(p.x), String.valueOf(p.y)
                );
            Process process = pb.start();
            process.waitFor();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (e.getModifiersEx()== InputEvent.BUTTON1_DOWN_MASK) {
            int centerX = width / 2;
            int centerY = height / 2;

            int mouseDX = e.getX() - centerX;
            int mouseDY = e.getY() - centerY;

            yaw += mouseDX * mouseSensitivity;
            pitch -= mouseDY * mouseSensitivity;

            if (pitch > Math.toRadians(89)) pitch = (float)Math.toRadians(89);
            if (pitch < Math.toRadians(-89)) pitch = (float)Math.toRadians(-89);

            moveMouse(new Point(centerX, centerY));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int centerX = width / 2;
            int centerY = height / 2;
            moveMouse(new Point(centerX, centerY));
            cu.hide();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            cu.show();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
