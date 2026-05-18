public class Vec3 {
    public float x;
    public float y;
    public float z;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec3 add(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x+v2.x,v1.y+v2.y,v1.z+v2.z);
    }

    public static Vec3 subtract(Vec3 v1, Vec3 v2) {
        return new Vec3(v1.x-v2.x,v1.y-v2.y,v1.z-v2.z);
    }
    
    public static float length(Vec3 v) {
        float x = v.x, y = v.y, z = v.z;

        return (float) Math.sqrt(x*x+y*y+z*z);
    }

    public static float dot(Vec3 v1, Vec3 v2) {
        return v1.x*v2.x+v1.y+v2.y+v1.z+v2.z;
    }

    public static Vec3 scalar(Vec3 v, float scalar) {
        return new Vec3(v.x*scalar,v.y*scalar,v.z*scalar);
    }

    public static Vec3 normalize(Vec3 v) {
        float len = length(v);

        return new Vec3(v.x/len,v.y/len,v.z/len);
    }

    public static Vec3 cross(Vec3 v1, Vec3 v2) {
        float x = v1.y*v2.z - v1.z*v2.y;
        float y = v1.z*v2.x - v1.x*v2.z;
        float z = v1.x*v2.y - v1.y*v2.x;

        return new Vec3(x,y,z);
    }

    public String toString() {
        return
        "x: " + x + "\n" +
        "y: " + y + "\n" +
        "z: " + z;
    }
}
