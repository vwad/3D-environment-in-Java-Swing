import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class PainterAlgorithm {
    private static final Comparator<List<Vec4>> comp = ((v1,v2)-> {
                float max_z1 = 0;
                float max_z2 = 0;

                for (Vec4 v : v1)
                    max_z1 += v.w;

                for (Vec4 v : v2)
                    max_z2 += v.w;

                return Float.compare(max_z2/v2.size(),max_z1/v1.size());
            });
    private static List<Vec4>[] faces = new ArrayList[GlobalConfig.LINE_COUNT];

    public static void draw(float[][] lines, float[][] points, Matrix4 view, float width, float height, Graphics2D g2) {
        IntStream.range(0, lines.length).parallel().forEach(a -> {
            float[] face = lines[a];
            List<Vec4> clipSpaceVertices = new ArrayList<>();
            for (int i = 0; i < face.length; i++) {
                Vec4 clipPos = Matrix4.multiplyByMat(points[(int) face[i]], view);
                clipSpaceVertices.add(clipPos);
            }

            List<Vec4> newFace = SutherlandHodgman.clipFace(clipSpaceVertices);
	    

	    faces[a] = newFace;
        });

        faces = Arrays.stream(faces).parallel().sorted(comp).toArray(List[]::new);

        for (List<Vec4> face : faces) {
            if(face.isEmpty())
                continue;

            List<Vec3> screenVertices = new ArrayList<>();
            for (Vec4 v : face) {
                Vec3 screen = Matrix4.display(v,width,height);
                screenVertices.add(screen);
            }

            g2.setColor(Color.red);
            drawPolygon(screenVertices,g2);

            g2.setColor(Color.black);
            for(int j = 0; j < 3; j++) {
                Vec4 v1 = face.get(j);
                Vec4 v2 = face.get((j + 1) % 3);
                g2.setStroke(new BasicStroke(Math.max(1/((v1.w + v2.w) / 2), 0.01f)));

                Vec3 p1 = screenVertices.get(j);
                Vec3 p2 = screenVertices.get((j + 1) % screenVertices.size());

                g2.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
            }
        }
    }


    private static void drawPolygon(List<Vec3> screenVertices, Graphics g2) {
        int[] x = new int[screenVertices.size()];
        int[] y = new int[screenVertices.size()];

        for (int i = 0; i < screenVertices.size(); i++) {
            Vec3 v = screenVertices.get(i);
            x[i] = (int) v.x;
            y[i] = (int) v.y;
        }
        g2.fillPolygon(x,y,screenVertices.size());
    }

}

