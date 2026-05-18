import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;


public class MouseController implements KeyListener, MouseMotionListener, MouseListener  {
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean forward = false;
    private boolean back = false;
    
    private float yaw = 3.13f;
    private float pitch = 0.0f;
    private float mouseSensitivity = 0.005f;

    private int width, height;

    public MouseController(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vec3 updatePos(Vec3 pos, Vec3 upVec, Vec3 orient, float currSpeed) {
        
        if (up) {
            pos = Vec3.add(pos,Vec3.scalar(upVec,currSpeed));
        }
        if (down) {
            pos = Vec3.add(pos,Vec3.scalar(upVec,-currSpeed));
        }
        if (left) {
            pos = Vec3.add(pos,Vec3.scalar(Vec3.normalize(Vec3.cross(orient,upVec)),-currSpeed));
        }
        if (right) {
            pos = Vec3.add(pos,Vec3.scalar(Vec3.normalize(Vec3.cross(orient,upVec)),currSpeed));
        }
        if (forward) {
            pos = Vec3.add(pos,Vec3.scalar(Vec3.normalize(orient),-currSpeed));
        }
        if (back) {
            pos = Vec3.add(pos,Vec3.scalar(Vec3.normalize(orient),currSpeed));
        }

        return pos;
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
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                System.err.println("Failed to move cursor via hyprctl. Exit code: " + exitCode);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {forward = true;}
        if (e.getKeyCode() == KeyEvent.VK_S) {back = true;}
        if (e.getKeyCode() == KeyEvent.VK_A) {left = true;}
        if (e.getKeyCode() == KeyEvent.VK_D) {right = true;}
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {up = true;}
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {down = true;}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {forward = false;}
        if (e.getKeyCode() == KeyEvent.VK_S) {back = false;}
        if (e.getKeyCode() == KeyEvent.VK_A) {left = false;}
        if (e.getKeyCode() == KeyEvent.VK_D) {right = false;}
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {up = false;}
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {down = false;}
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

    }

    @Override
    public void mouseReleased(MouseEvent e) {

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
