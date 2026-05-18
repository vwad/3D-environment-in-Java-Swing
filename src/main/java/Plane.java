class Plane {
    interface InsideTest {
        boolean test(Vec4 v);
    }

    interface IntersectFunc {
        Vec4 intersect(Vec4 v1, Vec4 v2);
    }

    public InsideTest inside;
    public IntersectFunc intersect;

    public Plane(InsideTest inside, IntersectFunc intersect) {
        this.inside = inside;
        this.intersect = intersect;
    }
}