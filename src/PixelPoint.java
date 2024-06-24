public class PixelPoint {
    private int x;
    private int y;

    public PixelPoint(int x, int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "PixelPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
