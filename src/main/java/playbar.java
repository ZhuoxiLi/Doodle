import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

class playController extends MouseAdapter{
    private Model model;
    public playController(Model model){
        this.model = model;
    }

    public void mouseClicked(MouseEvent e){
        //model.setSlider_value(0);
        if(model.getSlider_value() == 100) return;
        model.setPlay(1);
    }
}

class playBackController extends MouseAdapter{
    private Model model;
    public playBackController(Model model){
        this.model = model;
    }

    public void mouseClicked(MouseEvent e){
        //model.setSlider_value(100);
        if(model.getSlider_value() == 0) return;
        model.setPlay(-1);
    }
}

class startController extends MouseAdapter{
    private Model model;
    public startController(Model model){
        this.model = model;
    }

    public void mouseClicked(MouseEvent e){
        model.setSlider_value(0);
    }
}

class endController extends MouseAdapter{
    private Model model;
    public endController(Model model){
        this.model = model;
    }

    public void mouseClicked(MouseEvent e){
        model.setSlider_value(100);
    }
}

class sliderController implements ChangeListener {
    private Model model;
    private JSlider slider;
    public sliderController(Model model, JSlider slider){
        this.model = model;
        this.slider = slider;
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent){
        model.setSlider_value(slider.getValue());
    }
}

class playbarView extends JPanel implements Observer{

    private Model model;
    private JLabel play;
    private JLabel play_back;
    private JLabel start;
    private JLabel end;
    JSlider slider;

    playbarView(Model model){
        this.model = model;

        play_back = new JLabel(new ImageIcon("src/main/image/play_back.png"));
        play = new JLabel(new ImageIcon("src/main/image/play.png"));
        start = new JLabel(new ImageIcon("src/main/image/left_arrow.png"));
        end = new JLabel(new ImageIcon("src/main/image/right_arrow.png"));
        slider = new JSlider(JSlider.HORIZONTAL, 0 , 100, 100);

        play_back.addMouseListener(new playBackController(model));
        play.addMouseListener(new playController(model));
        slider.addChangeListener(new sliderController(model, slider));
        start.addMouseListener(new startController(model));
        end.addMouseListener(new endController(model));

        this.add(play_back);
        this.add(play);
        this.add(slider);
        this.add(start);
        this.add(end);

        this.setLayout(new FlowLayout(FlowLayout.LEFT, 80, 15));
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                setLayout(new FlowLayout(FlowLayout.LEFT, getWidth()/10, getHeight()/6));
            }
        });

    }

    @Override
    public void paintComponent(Graphics g){
        repaint();
    }

    @Override
    public void update(Object o){
        int value = model.getSlider_value();

        start.setEnabled(true);
        end.setEnabled(true);
        play.setEnabled(true);
        play_back.setEnabled(true);

        if(model.getDisable_allBotton() == 1){
            start.setEnabled(false);
            end.setEnabled(false);
            play.setEnabled(false);
            play_back.setEnabled(false);
        }

        if (value == 0) {
            start.setEnabled(false);
            play_back.setEnabled(false);
        }

        if (value == 100) {
            end.setEnabled(false);
            play.setEnabled(false);
        }
        slider.setValue(model.getSlider_value());
    }

}
