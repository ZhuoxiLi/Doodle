import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

class Point extends Point2D {
    private double x;
    private double y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX(){
        return x;
    }

    @Override
    public double getY(){
        return y;
    }

    @Override
    public void setLocation(double x, double y){
        this.x = x;
        this.y = y;
    }
}

class Line {
    private List<Point> line = new ArrayList<>();
    private Color color = Color.BLACK;
    private float thick = 2.0f;
    private List<Integer> xLine = new ArrayList<>();
    private List<Integer> yLine = new ArrayList<>();

    public void addPoint(int x, int y){
        Point p = new Point(x, y);
        xLine.add(x);
        yLine.add(y);
        line.add(p);
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setThick(float thick) {
        this.thick = thick;
    }

    public float getThick() {
        return thick;
    }

    public List<Integer> getxLine() {
        return xLine;
    }

    public List<Integer> getyLine() {
        return yLine;
    }

    public List<Point> getLine() { return line; }

    public void setxLine(List<Integer> x) {
        xLine = x;
    }

    public void setyLine(List<Integer> y) {
        yLine = y;
    }

    public void setLine(List<Point> l) { line = l; }

    public int size() {
        return line.size();
    }
}
