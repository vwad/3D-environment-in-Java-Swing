import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SutherlandHodgman {
    
    public static List<Vec4> clipFace(List<Vec4> face) {
        if (face.isEmpty()) return face;

        List<Plane> clippingPlanes = new ArrayList<>();

        clippingPlanes.add(new Plane(
                v -> v.z >= 0,
                (v1, v2) -> intersect(v1, v2, v -> v.z)
        ));
        clippingPlanes.add(new Plane(
                v -> v.z <= v.w,
                (v1, v2) -> intersect(v1, v2, v -> v.w - v.z)
        ));
        clippingPlanes.add(new Plane(
                v -> v.x >= -v.w,
                (v1, v2) -> intersect(v1, v2, v -> v.w + v.x)
        ));
        clippingPlanes.add(new Plane(
                v -> v.x <= v.w,
                (v1, v2) -> intersect(v1, v2, v -> v.w - v.x)
        ));
        clippingPlanes.add(new Plane(
                v -> v.y >= -v.w,
                (v1, v2) -> intersect(v1, v2, v -> v.w + v.y)
        ));
        clippingPlanes.add(new Plane(
                v -> v.y <= v.w,
                (v1, v2) -> intersect(v1, v2, v -> v.w - v.y)
        ));

        List<Vec4> outputList = new ArrayList<>(face);

        for (Plane plane : clippingPlanes) {
            if (outputList.isEmpty()) break;

            List<Vec4> inputList = new ArrayList<>(outputList);
            outputList.clear();

            for (int i = 0; i < inputList.size(); i++) {
                Vec4 current = inputList.get(i);
                Vec4 prev = inputList.get((i + inputList.size() - 1) % inputList.size());

                boolean currInside = plane.inside.test(current);
                boolean prevInside = plane.inside.test(prev);

                if (currInside) {
                    if (!prevInside) {
                        outputList.add(plane.intersect.intersect(prev, current));
                    }
                    outputList.add(current);
                } else if (prevInside) {
                    outputList.add(plane.intersect.intersect(prev, current));
                }
            }
        }
        return outputList;
    }

    private static Vec4 intersect(Vec4 v1, Vec4 v2, Function<Vec4, Float> distanceFunc) {
        float d1 = distanceFunc.apply(v1);
        float d2 = distanceFunc.apply(v2);
        float t = d1 / (d1 - d2);

        return new Vec4(
                v1.x + t * (v2.x - v1.x),
                v1.y + t * (v2.y - v1.y),
                v1.z + t * (v2.z - v1.z),
                v1.w + t * (v2.w - v1.w)
        );
    }
}
