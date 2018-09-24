import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.List;
import java.awt.geom.Point2D;

public class Model {
    /** The observers that are watching this model for changes. */
    private List<Observer> observers;
    private List<Line> lines = new ArrayList<>();
    private Line curLine = null;
    private Color currentColour;
    private float currentThick;

    private double widthRatio = 1;
    private double heightRatio = 1;

    private ArrayList<Polygon>colorPalettes = new ArrayList<>();
    private ArrayList<Polygon>strokeChooser = new ArrayList<>();

    private int play = 0;
    int display_n = get_total_point();  //depends on ticks
    private int slider_value = 100;
    private int disable_allBotton = 1;

    public int getDisable_allBotton(){
        return disable_allBotton;
    }

    public void setSlider_value(int i) {
        slider_value = i;
        display_n = get_total_point() * i / 100;
        notifyObservers();
    }

    public int getSlider_value() {
        return slider_value;
    }


    /**
     * Create a new model.
     */
    Model() {
        this.observers = new ArrayList();
        currentColour = colors[0];
        currentThick = 2.0f;
    }

    public int get_total_point(){
        int n = 0;
        for(int i = 0; i < lines.size(); i++){
            n += lines.get(i).size();
        }
        return n;
    }

    public void setPlay(int x){
        play = x;
    }

    public void updatePlay(){

        setSlider_value(slider_value+play);

        if (slider_value == 0 || slider_value == 100) {
            play = 0;
        }
    }

    public int getPlay(){
        return play;
    }


    public List<Line> getLine(){
        return lines;
    }

    public void updateLines(double widthRatio, double heightRatio) {
        this.widthRatio *= widthRatio;
        this.heightRatio *= heightRatio;
//        System.out.println(widthRatio + " " + heightRatio);
    }

    public void mousePressed(){
        disable_allBotton = 0;
        //display_n = get_total_point();
        curLine = new Line();
        curLine.setColor(currentColour);
        curLine.setThick(currentThick);
        if(slider_value != 100) {
            //List<Line> new_lines = new ArrayList<>();
            int keep_points = (get_total_point() * slider_value / 100);
            //Line new_l = new Line();
                for (Line l : lines) {
                    if (keep_points <= 0) {
                        l.setLine(new ArrayList<>());
                        l.setxLine(new ArrayList<>());
                        l.setyLine(new ArrayList<>());
                    } else if (l.size() > keep_points) {
                        l.setLine(l.getLine().subList(0, keep_points - 1));
                        l.setxLine(l.getxLine().subList(0, keep_points - 1));
                        l.setyLine(l.getyLine().subList(0, keep_points - 1));
                        keep_points = 0;
                    } else {
                        keep_points -= l.size();
                    }

            }
        }

        lines.add(curLine);
        setSlider_value(100);
        notifyObservers();
        //playba
    }

    public void mouseReleased(){
        curLine = null;
    }

    public void mouseDragged(int x, int y){
        curLine.addPoint((int)(x/widthRatio), (int)(y/heightRatio));
        display_n = get_total_point();
        notifyObservers();
    }

    private Color [] colors = {
        new Color(0x80, 0xbd, 0x9e),
        new Color(0xf1, 0x8d, 0x9e),
        new Color(0xae, 0xbd, 0x38),
        new Color(0x76, 21, 32),
        new Color(0xff, 0xbb, 0x00),
        new Color(18, 54, 0x5e),
        new Color(102, 67, 75),
        new Color(0x00, 0x00, 0x00) // customized color default black
    };

    float [] thickness = {
            2.0f, 4.0f, 6.0f
    };

    public void checkColorChange(int x, int y, int button){
        for (int i = 0; i < colorPalettes.size(); i++) {
            if (colorPalettes.get(i).contains(x, y)) {
                if (button == MouseEvent.BUTTON1) {
                    currentColour = colors[i];
                } else {
                    currentColour = JColorChooser.showDialog(null, "Choose Customized Color", Color.WHITE);
                    colors[i] = currentColour;
                }
            }
        }
    }

    public void checkThickness(int x, int y){
        for(int i = 0; i < strokeChooser.size(); i++){
            if(strokeChooser.get(i).contains(x, y)){
                currentThick = thickness[i];
            }
        }
    }


    /**
     * Add an observer to be notified when this model changes.
     */
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    /**
     * Remove an observer from this model.
     */
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    /**
     * Notify all observers that the model has changed.
     */
    public void notifyObservers() {
        for (Observer observer: this.observers) {
            observer.update(this);
        }
    }

    public double getWidthRatio() {
        return widthRatio;
    }

    public double getHeightRatio() {
        return heightRatio;
    }

    public Color getCurrentColour() {
        return currentColour;
    }

    public ArrayList<Polygon> getColorPalettes() {
        return colorPalettes;
    }

    public ArrayList<Polygon> getStrokeChooser() {
        return strokeChooser;
    }

    public Color[] getColors() {
        return colors;
    }

    public void saveFile() {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Save File");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("*.doodle", "doodle"));
        jFileChooser.showSaveDialog(null);
        try {
            FileWriter fileWriter = new FileWriter(jFileChooser.getSelectedFile().getPath() + ".doodle");
            for (Line l : lines) {
                fileWriter.write(l.size());
                fileWriter.write(l.getColor().getRed());
                fileWriter.write(l.getColor().getGreen());
                fileWriter.write(l.getColor().getBlue());
                fileWriter.write((int) l.getThick());

                for (Point p: l.getLine()) {
                    fileWriter.write((int) (p.getX() > 0 ? p.getX() : 0));
                    fileWriter.write((int) (p.getY() > 0 ? p.getY() : 0));
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Wrong Happened");
        }
    }

    public void openFile() {
        if (!lines.isEmpty()) {
            int rv = JOptionPane.showConfirmDialog(null, "This will override current image, do you want to continue?");
            if (rv != 0) {
                return;
            }
        }

        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Open File");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("*.doodle", "doodle"));
        jFileChooser.showOpenDialog(null);
        try {
            FileReader fileReader = new FileReader(jFileChooser.getSelectedFile().getPath());
            lines.clear();
            while (true) {
                int num = fileReader.read();
                if (num == -1) break;

                Line l = new Line();
                int r = fileReader.read();
                int g = fileReader.read();
                int b = fileReader.read();
                int thick = fileReader.read();

                l.setColor(new Color(r,g,b));
                l.setThick(thick);

                for (int i = 0; i < num; i++) {
                    int x = fileReader.read();
                    int y = fileReader.read();
                    l.addPoint(x, y);
                }

                lines.add(l);
            }
            fileReader.close();
            setSlider_value(100);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Something Wrong Happened");
        }
    }

    public void clear() {
        int rv = JOptionPane.showConfirmDialog(null, "This will override current image, do you want to continue?");
        if (rv != 0) {
            return;
        }
        lines.clear();
    }

    public void exit() {
        int rv = JOptionPane.showConfirmDialog(null, "Any unsaved changes will be lost once exit, do you want to continue?");
        if (rv != 0) {
            return;
        }
        System.exit(0);
    }
}
