import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class InversePainter {
    private static final Queue <List<Vec4>> faces = new PriorityQueue<>((v1,v2)-> {
                float max_z1 = 0;
                float max_z2 = 0;

                for (Vec4 v : v1)
                    max_z1 += v.w/v1.size();

                for (Vec4 v : v2)
                    max_z2 += v.w/v2.size();

                return Float.compare(max_z2,max_z1);
            });

    public static void draw(float[][] lines, float[][] points, Matrix4 view, float width, float height, Graphics2D g2) {
        for (float[] face : lines) {

            List<Vec4> clipSpaceVertices = new ArrayList<>();
            for (int i = 0; i < face.length; i++) {
                Vec4 clipPos = Matrix4.project(points[(int) face[i]], view);
                clipSpaceVertices.add(clipPos);
            }

            List<Vec4> newFace = PolygonClipper.clipFace(clipSpaceVertices);

            faces.add(newFace);
        }

        while (!faces.isEmpty()) {
            List<Vec4> face = faces.poll();
            if(face.isEmpty())
                continue;

            List<Vec3> screenVertices = new ArrayList<>();
            for (Vec4 v : face) {
                Vec3 screen = Matrix4.display(v,width,height);
                screenVertices.add(screen);
            }

            Polygon p = new Polygon(screenVertices.stream().mapToInt(a -> (int) a.x).toArray(), screenVertices.stream().mapToInt(a -> (int) a.y).toArray(), screenVertices.size());
            g2.setColor(Color.red);
            g2.fillPolygon(p);

            g2.setColor(Color.black);
            for(int i = 0; i < 3; i++) {
                Vec4 v1 = face.get(i);
                Vec4 v2 = face.get((i + 1) % 3);
                g2.setStroke(new BasicStroke(Math.max(1/((v1.w + v2.w) / 2), 0.01f)));

                Vec3 p1 = screenVertices.get(i);
                Vec3 p2 = screenVertices.get((i + 1) % screenVertices.size());

                g2.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            }
        }
    }
}

