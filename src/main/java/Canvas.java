import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.List;

class CanvasController extends MouseAdapter {
    private Model model;
    CanvasController(Model model){
        this.model = model;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        model.mousePressed();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        model.mouseReleased();
    }

    @Override
    public void mouseDragged(MouseEvent e){
        model.mouseDragged(e.getX(), e.getY());
    }
}

class CanvasView extends JPanel implements Observer{

    private Model model;
    CanvasView(Model model){
        this.model = model;
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.addMouseListener(new CanvasController(model));
        this.addMouseMotionListener(new CanvasController(model));
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0 , getWidth(), getHeight());
        AffineTransform m = g2.getTransform();

        g2.scale(model.getWidthRatio(), model.getHeightRatio());
        int max_n = model.display_n; //number of points should be display

        for(Line line: model.getLine()){
            List<Integer> xLine = line.getxLine();
            List<Integer> yLine = line.getyLine();

            g2.setColor(line.getColor());
            g2.setStroke(new BasicStroke(line.getThick()));

            int len = line.size() > max_n ? max_n : line.size();

            int [] xLines = new int[len];
            int [] yLines = new int[len];
            for(int i = 0; i < len; i++){
                xLines[i] = xLine.get(i);
                yLines[i] = yLine.get(i);
            }
            g2.drawPolyline(xLines, yLines, len);

            max_n-=len;

        }

        if (model.getPlay() != 0) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdateTime >= model.get_total_point() / 10) {
                model.updatePlay();
                lastUpdateTime = currentTime;
            }
        }
        g2.setTransform(m);
        repaint();
    }

    private long lastUpdateTime = 0;

    private double cachedWidth = 0;
    private double cachedHeight = 0;
    @Override
    public void update(Object o){
        if (cachedWidth == 0) {
            cachedWidth = getWidth();
        }
        if (cachedHeight == 0) {
            cachedHeight = getHeight();
        }
        model.updateLines(getWidth()/cachedWidth, getHeight()/cachedHeight);
        cachedWidth = getWidth();
        cachedHeight = getHeight();

    }

}