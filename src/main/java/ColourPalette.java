import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

class ColourPaletteController extends MouseAdapter{
    private Model model;
    public ColourPaletteController(Model model){
        this.model = model;
    }

    public void mouseClicked(MouseEvent e) {
        model.checkThickness(e.getX(), e.getY());
        model.checkColorChange(e.getX(), e.getY(), e.getButton());
    }
}

class ColourPaletteView extends JPanel implements Observer{
    Model model;
    private Polygon currentPalette;

    public ColourPaletteView(Model model){
        this.model = model;

        int border = 5;
        int totalWidth = 150;
        int length = (totalWidth - border * 3) / 2;
        for (int i = 0; i < 8; i++) {
            int topLeftX = border + (i%2) * (border + length);
            int topLeftY = border + (i/2) * (border + length);
            model.getColorPalettes().add(new Polygon(new int[] {topLeftX, topLeftX + length, topLeftX + length, topLeftX},
                new int[] {topLeftY, topLeftY, topLeftY + length, topLeftY + length}, 4));
        }

        int currentPaletteY = border + (border + length) * 4;
        int totalHeight = getHeight();
        currentPalette = new Polygon(new int[] {border, totalWidth - border, totalWidth - border, border},
            new int[] {currentPaletteY, currentPaletteY, totalHeight - border, totalHeight - border}, 4);
        System.out.println(totalHeight);

        //thickness
        int totalHeight_thick = totalHeight - border - length - border * 2;
        int chooserHeight = 60 / 3;
        for (int i = 3; i > 0; i--) {
            int topLeftX = border*3/2;
            int topLeftY = (totalHeight - border) - (i*chooserHeight);
            model.getStrokeChooser().add(new Polygon(new int[] {topLeftX, topLeftX + length*2, topLeftX + length*2, topLeftX},
                    new int[] {topLeftY, topLeftY, topLeftY + chooserHeight, topLeftY + chooserHeight}, 4));
        }

        Polygon p = new Polygon();
        p.reset();
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.addMouseListener(new ColourPaletteController(model));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int totalHeight = getHeight();

        for (int i = 0; i < model.getColors().length; i++) {
            g2.setColor(model.getColors()[i]);
            g2.fillPolygon(model.getColorPalettes().get(i));
        }

        // draw current palette panel
        g2.setColor(model.getCurrentColour());
        g2.fillPolygon(currentPalette);

        //thickness
        int line_posi = 3;
        for (int i = 0; i < 3; i++) {
            g2.setColor(Color.black);
            //g2.drawPolygon(model.getStrokeChooser().get(i));

            if(i == 0){
                g2.setColor(Color.lightGray);
            } else if(i == 1){
                g2.setColor(Color.GRAY);
            } else{
                g2.setColor(Color.darkGray);
            }

            //g2.setColor(Color.lightGray);
            g2.fillPolygon(model.getStrokeChooser().get(i));
            g2.setStroke(new BasicStroke(model.thickness[i]));
            g2.setColor(Color.BLACK);
            int border = 5;
            int totalWidth = 150;
            int length = (totalWidth - border * 3) / 2;
            int topLeftX = border*3/2 + 7;
            int topLeftY = (totalHeight - border) - (line_posi*15);
            line_posi--;

            g2.drawLine(topLeftX, topLeftY, topLeftX + length*2 -14, topLeftY);

        }

        repaint();
    }

    @Override
    public void update(Object observable) {
        int border = 5;
        int totalWidth = getWidth();
        int length = (totalWidth - border * 3) / 2;
        int currentPaletteY = border + (border + length) * 4;
        int totalHeight = getHeight();
        currentPalette = new Polygon(new int[] {border, totalWidth - border, totalWidth - border, border},
            new int[] {currentPaletteY, currentPaletteY, totalHeight - border, totalHeight - border}, 4);

        model.getStrokeChooser().clear();
        int chooserHeight = 60 / 3;
        for (int i = 3; i > 0; i--) {
            int topLeftX = border*3/2;
            int topLeftY = (totalHeight - border) - (i*chooserHeight);
            model.getStrokeChooser().add(new Polygon(new int[] {topLeftX, topLeftX + length*2, topLeftX + length*2, topLeftX},
                    new int[] {topLeftY, topLeftY, topLeftY + chooserHeight, topLeftY + chooserHeight}, 4));
        }
    }
}
