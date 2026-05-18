
import java.util.HashSet;
import java.util.Set;
//for purpose of optimization, not sure if actually results in 4x speedup
public class VecSet {
    public Set<Float> vec = new HashSet<>();

    public VecSet(float[] vec) {
        for (float f : vec) {
            this.vec.add(f);
        }
    }
}
