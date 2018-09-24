import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class saveButtonListener implements ActionListener {
    Model m;

    public saveButtonListener(Model model) {
        m = model;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        m.saveFile();
    }
}

class loadButtonListener implements ActionListener {
    Model m;

    public loadButtonListener(Model model) {
        m = model;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        m.openFile();
    }
}

class newButtonListener implements ActionListener {
    Model m;

    public newButtonListener(Model model) {
        m = model;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        m.clear();
    }
}

class exitButtonListener implements ActionListener {
    Model m;

    public exitButtonListener(Model model) {
        m = model;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        m.exit();
    }
}
public class Main {
    public static void main(String[] args) {
        Model model = new Model();

        JFrame frame = new JFrame("Doodle Program");


        // Hook up this observer so that it will be notified when the model
        // changes.
        //this.model = model;
        model.notifyObservers();
        //model.addObserver(this);
        JPanel panel = new JPanel(new BorderLayout());


        CanvasView Canvas = new CanvasView(model);
        model.addObserver(Canvas);
        Canvas.setPreferredSize(new Dimension(600, 400));

        ColourPaletteView Color = new ColourPaletteView(model);
        model.addObserver(Color);
        Color.setPreferredSize(new Dimension(150, 400));

        playbarView playbar = new playbarView(model);
        model.addObserver(playbar);
        playbar.setPreferredSize(new Dimension(800, 60));

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                int totalWidth = frame.getWidth();
                int totalHeight = frame.getHeight();

                int playbarWidth = totalWidth;
                int playbarHeight = totalHeight / 10;

                int paletteWidth = 150;
                int paletteHeight = totalHeight - playbarHeight;

                int canvasWidth = totalWidth - paletteWidth;
                int canvasHeight = totalHeight - playbarHeight;

                Canvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
                Color.setPreferredSize(new Dimension(paletteWidth, paletteHeight));
                playbar.setPreferredSize(new Dimension(playbarWidth, playbarHeight));
                model.notifyObservers();
            }
        });

        frame.add(panel);

        panel.add(Canvas, BorderLayout.CENTER);
        panel.add(Color, BorderLayout.LINE_START);
        panel.add(playbar, BorderLayout.PAGE_END);

        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu fileMenu = new JMenu("File");
        menubar.add(fileMenu);

        JMenuItem newButton = new JMenuItem("New Doodle");
        newButton.addActionListener(new newButtonListener(model));
        fileMenu.add(newButton);

        JMenuItem saveButton = new JMenuItem("Save...");
        saveButton.addActionListener(new saveButtonListener(model));
        fileMenu.add(saveButton);

        JMenuItem loadButton = new JMenuItem("Load...");
        loadButton.addActionListener(new loadButtonListener(model));
        fileMenu.add(loadButton);

        JMenuItem exitButton = new JMenuItem("Exit");
        exitButton.addActionListener(new exitButtonListener(model));
        fileMenu.add(exitButton);


        frame.setMinimumSize(new Dimension(400, 300));
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
