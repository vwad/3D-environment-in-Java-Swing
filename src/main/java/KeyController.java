
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyController implements KeyListener{
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private boolean forward = false;
    private boolean back = false;

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
    public void keyTyped(KeyEvent e) {
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
    
}
