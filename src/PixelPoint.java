public class PixelPoint implements Comparable<PixelPoint> {
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "PixelPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public int compareTo(PixelPoint other) {
        return 0;
    }
}
