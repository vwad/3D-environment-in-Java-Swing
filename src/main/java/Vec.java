import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vec {
    public List<Float> vec = new ArrayList<>();

    public Vec(float[] vec) {
        for (float f : vec) {
            this.vec.add(f);
        }
    }

    public float get(int index) {
        return vec.get(index);
    }
}
