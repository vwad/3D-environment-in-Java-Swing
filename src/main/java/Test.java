import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,800);
        frame.setVisible(true);

        JPanel panel = new Panel();
        panel.setSize(800,800);
        frame.add(panel);
    }

    private static class Panel extends JPanel {

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.black);

            g2.drawRect(200,200,400,400);

            Vec4 from = new Vec4(0,200,1,200);
            Vec4 to = new Vec4(800,400,1,0);

            g2.drawLine((int) from.x, (int) from.y, (int) to.x, (int) to.y);

            liang_barsky(from,to);

            g2.setColor(Color.RED);

            g2.drawLine((int) from.x, (int) from.y, (int) to.x, (int) to.y);


        }

        private void liang_barsky(Vec4 p1, Vec4 p2) {
            float dx = p2.x - p1.x;
            float dy = p2.y - p1.y;
            Vec p = new Vec(new float[]{-dx, dx, -dy, dy});
            Vec q = new Vec(new float[]{
                    p1.x - 200,
                    600 - p1.x,
                    p1.y - 200,
                    600 -  p1.y,
            });
            float t_enter = 0.0f;
            float t_exit = 1.0f;

            for (int i = 0; i < p.vec.size();i++) {
                if (p.get(i) == 0) {
                    if (q.get(i) < 0) {
                        return;
                    }
                } else {
                    float t = q.get(i) / p.get(i);
                    if (p.get(i) < 0) {
                        if (t > t_enter) {
                            t_enter = t;
                        }
                    }
                    else {
                        if (t < t_exit) {
                            t_exit = t;
                        }
                    }
                }
            }

            if (t_enter > t_exit) {
                return;
            }

            float x1_clip = p1.x + t_enter * dx;
            float y1_clip = p1.y + t_enter * dy;
            float x2_clip = p1.x + t_exit * dx;
            float y2_clip = p1.y + t_exit * dy;

            p1.x = x1_clip;
            p1.y = y1_clip;

            p2.x = x2_clip;
            p2.y = y2_clip;
        }
    }
}
