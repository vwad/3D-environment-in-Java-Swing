import java.rmi.UnexpectedException;

public class Matrix4 {
    public float[][] mat4;

    public Matrix4(float[][] mat4){
        try {
            if (mat4.length != 4) {
                throw new UnexpectedException("Incorrect length");
            }
            if (mat4[0].length != 4) {
                throw new UnexpectedException("Incorrect length");
            }
        }
        catch (UnexpectedException e) {
            e.printStackTrace();
        }

        this.mat4 = mat4;
    }

    public static Matrix4 multiply(Matrix4 m1,Matrix4 m2) {
        float[][] new_mat = new float[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++)
                    new_mat[i][j] += m1.mat4[i][k] * m2.mat4[k][j];
            }
        }

        return new Matrix4(new_mat);
    }

    public static Matrix4 perspective(float fovDegrees, float aspect,float near, float far) {
        float proj = (float) (1.0/(Math.tan(Math.toRadians(fovDegrees/2))));

        float dist = near-far;
        float A = (far+near)/dist;
        float B = (2f*far*near)/dist;

        return new Matrix4(new float[][]
                {
                        {proj/aspect,0,0,0},
                        {0,proj,0,0},
                        {0,0,A,B},
                        {0,0,-1,0}
                });
    }

    public static Matrix4 lookAt(Vec3 eye, Vec3 center, Vec3 up) {
        Vec3 f = Vec3.normalize(Vec3.subtract(eye,center));
        Vec3 s = Vec3.normalize(Vec3.cross(up,f));
        Vec3 u = Vec3.cross(f,s);

        Matrix4 rot = new Matrix4(new float[][]
                {
                        {s.x,s.y,s.z,0},
                        {u.x,u.y,u.z,0},
                        {-f.x,-f.y,-f.z,0},
                        {0,0,0,1}
                });

        Matrix4 transl = new Matrix4(new float[][]
                {
                        {1,0,0,-eye.x},
                        {0,1,0,-eye.y},
                        {0,0,1,-eye.z},
                        {0,0,0,1}
                });

        return Matrix4.multiply(rot,transl);
    }

    public static Vec4 multiplyByMat(Vec4 v, Matrix4 m) {
        float[][] mt = m.mat4;
        float x = mt[0][0] * v.x + mt[0][1] * v.y + mt[0][2] * v.z + mt[0][3] * v.w;
        float y = mt[1][0] * v.x + mt[1][1] * v.y + mt[1][2] * v.z + mt[1][3] * v.w;
        float z = mt[2][0] * v.x + mt[2][1] * v.y + mt[2][2] * v.z + mt[2][3] * v.w;
        float w = mt[3][0] * v.x + mt[3][1] * v.y + mt[3][2] * v.z + mt[3][3] * v.w;

        return new Vec4(x, y, z, w);
    }

    public static Vec4 multiplyByMat(float[] f, Matrix4 m) {
        Vec4 v = new Vec4(f[0],f[1],f[2],1.0f);
        float[][] mt = m.mat4;
        float x = mt[0][0] * v.x + mt[0][1] * v.y + mt[0][2] * v.z + mt[0][3] * v.w;
        float y = mt[1][0] * v.x + mt[1][1] * v.y + mt[1][2] * v.z + mt[1][3] * v.w;
        float z = mt[2][0] * v.x + mt[2][1] * v.y + mt[2][2] * v.z + mt[2][3] * v.w;
        float w = mt[3][0] * v.x + mt[3][1] * v.y + mt[3][2] * v.z + mt[3][3] * v.w;

        return new Vec4(x, y, z, w);
    }

    public static Vec4 project(float[] point, Matrix4 view) {
        Vec4 vertPos = new Vec4(point[0],point[1],point[2],1f);

        return multiplyByMat(vertPos,view);
    }

    public static Vec3 display(Vec4 v, float width, float height) {
        Vec3 p = new Vec3(v.x/v.w, v.y/v.w,v.z/v.w);
        int realX = (int) ((p.x + 1) * 0.5 * width);
        int realY = (int) ((1 - (p.y + 1) * 0.5) * height);

        return new Vec3(realX,realY,p.z);
    }

    private static void rotateHorizontally(float angle, float[][] points) {
        float cos = (float)(Math.cos(Math.toRadians(angle)));
        float sin =(float)(Math.sin(Math.toRadians(angle)));

        for (int i = 0; i<points.length;i++) {
            float[] point = points[i].clone();

            float x = point[0];
            float z = point[2];

            float newx = x * cos - z * sin;

            float newz = x * sin + z * cos;

            point[0] = newx;
            point[2] = newz;

            points[i] = point;
        }
    }
}
