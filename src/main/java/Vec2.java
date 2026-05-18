public class Vec2 {
    public int x;
    public int y;

    public Vec2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Vec2 vec)) {
            return false;
        }

        return vec.x == x && vec.y == y;
    }

    public String toString() {
        return
                "x: " + x + "\n" +
                "y: " + y;
    }
}
