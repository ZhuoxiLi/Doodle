
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class View extends JFrame implements Observer {

    private Model model;

    /**
     * Create a new View.
     */
    public View(Model model) {
        // Set up the window.
        this.setTitle("一个只做冰激凌色的绘图软件");
        this.setMinimumSize(new Dimension(128, 128));
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Hook up this observer so that it will be notified when the model
        // changes.
        this.model = model;
        model.notifyObservers();  //TODO
        model.addObserver(this);
        JPanel panel = new JPanel(new BorderLayout());
        this.add(panel);

        CanvasView Canvas = new CanvasView(model);
        model.addObserver(Canvas);
        Canvas.setPreferredSize(new Dimension(600, 400));
        panel.add(Canvas, BorderLayout.CENTER);

        ColourPaletteView Color = new ColourPaletteView(model);
        model.addObserver(Color);
        Color.setPreferredSize(new Dimension(100,400));
        panel.add(Color, BorderLayout.WEST);

        setVisible(true);
    }

    /**
     * Update with data from the model.
     */
    public void update(Object observable) {
        // XXX Fill this in with the logic for updating the view when the model
        // changes.
//        System.out.println("Model changed!");
//        repaint();
    }
}
