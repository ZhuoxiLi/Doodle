import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.List;

class menuController extends MouseAdapter {
    private Model model;
    public menuController(Model model){
        this.model = model;
    }

}

class menuView extends JPanel implements Observer{

    private Model model;
    menuView(Model model){
        this.model = model;
        //this.addMouseListener(new CanvasController(model));
        //this.addMouseMotionListener(new CanvasController(model));
    }

    @Override
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.magenta);
        g2.fillRect(0, 0 ,getWidth(), getHeight());

        repaint();
    }

    @Override
    public void update(Object o){
//        repaint();
    }

}