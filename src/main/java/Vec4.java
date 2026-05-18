public class Vec4 {
    public float x;
    public float y;
    public float z;
    public float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4 copy() {
        return new Vec4(x,y,z,w);
    }

    public String toString() {
        return
        "x: " + x + "\n" +
        "y: " + y + "\n" +
        "z: " + z + "\n" +
        "w: " + w;
    }
}
