import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class Main extends JFrame{

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(Main::new);
    }

    private int barSize = GlobalConfig.BAR_SIZE;

    public Main() {
        setLayout(null);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(dim);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        CursorUpdater cu = new CursorUpdater(this);
        KeyController kc = new KeyController();
        MouseController mc = new MouseController(dim.width, dim.height,cu);
        addKeyListener(kc);
        addMouseMotionListener(mc);
        addMouseListener(mc);
        

        Model m = new Model();
        JPanel panel = new PaintPanel(m.getPoints(),m.getLines(),dim,mc,kc);
        add(panel);

        JPanel bar1 = new JPanel();
        bar1.setSize(dim.width,barSize);
        bar1.setBackground(Color.black);

        JPanel bar2 = new JPanel();
        bar2.setSize(dim.width,barSize);
        bar2.setBackground(Color.black);

        bar1.setLocation(0,0);
        bar2.setLocation(0,dim.height-barSize);

        add(bar1);
        add(bar2);

    }

    private class PaintPanel extends JPanel {

        private MouseController mc;

        private float[][] points;
        private float[][] lines;
        private float width;
        private float height;

        private Vec3 pos = new Vec3(0,0,2);
        private Vec3 upVec = new Vec3(0,1,0);
        private Vec3 orient = new Vec3(0,0,1);


        private PaintPanel(float[][] points, float[][] lines, Dimension dim, MouseController mc, KeyController kc) {
            this.points = points;
            this.lines = lines;
            this.width = dim.width;
            this.height = dim.height;
            this.mc = mc;


            setSize((int) width, (int) (height-barSize*2));
            setLocation(0,barSize);
            setBackground(Color.darkGray);

            int FPS = GlobalConfig.FPS;
            final long[] lastTime = {System.nanoTime()};
            Timer animation = new Timer(1000/FPS,a-> {

                long currentTime = System.nanoTime();

                float dt = (currentTime - lastTime[0]) / 1_000_000_000f;

                lastTime[0] = currentTime;

                float currSpeed = GlobalConfig.SPEED*dt;
                pos = kc.updatePos(pos, upVec, orient, currSpeed);

                repaint();
            });

            animation.start();

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;

            float pitch = mc.getPitch();
            float yaw = mc.getYaw();

            float lookX = -(float) (Math.cos(pitch) * Math.sin(yaw));
            float lookY = -(float) Math.sin(pitch);
            float lookZ = -(float) (Math.cos(pitch) * Math.cos(yaw));

            orient = new Vec3(lookX, lookY, lookZ);

            Vec3 look = Vec3.add(pos, orient);

            Matrix4 proj = Matrix4.perspective(GlobalConfig.FOV,width/height,GlobalConfig.NEAR,GlobalConfig.FAR);
            Matrix4 cam = Matrix4.lookAt(pos, look, upVec);
            Matrix4 view = Matrix4.multiply(proj,cam);
            
            PainterAlgorithm.draw(lines, points, view, width, height, g2);

        }

    }
}