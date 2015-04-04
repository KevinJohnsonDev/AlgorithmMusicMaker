/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import music.demo.pkg3.Music;
import music.demo.pkg3.NoteToProb;
import music.demo.pkg3.SliderDemo;
import org.jfugue.Pattern;
/**
 *
 * @author Kevin
 */
public class SongButton extends JPanel
        implements 
        WindowListener 
         {
    private Pattern p; 
    private HashMap<String, ArrayList<NoteToProb>> SongButtonMap;
    boolean frozen = false;
    int delay;
    JFormattedTextField textField;
    JLabel picture;
    private final JButton b1;
    private static int songInt =0;
    private Pattern songForButton;
    private SongButton thisButton;
   private int songButtonNumber;
    public SongButton() {
      
        this.SongButtonMap = new HashMap <String, ArrayList<NoteToProb>>();
        b1 = new JButton("Song" + ++songInt);
        songButtonNumber = songInt;
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        add(b1);
        b1.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                   if(!Music.getPlaying()) // if not playing
                   {
                    Music m = Music.getInstance();
                    m.startMusic(SliderDemo.getSliderNum(),songButtonNumber,thisButton);
                   }
                   else
                   {
                        ImageIcon icon = createImageIcon("images/erroricon.png",
                                "error icon");
                        String message = "Wait for music to Stop or Press Stop button";
                           JOptionPane.showMessageDialog(new JFrame(), message, "Wait Now",
                                JOptionPane.ERROR_MESSAGE, icon);
                   }
            }
        });
          thisButton = this;
    }
    public void setMusic(Pattern p){
        songForButton = p;
    }
    public void setMap(HashMap<String, ArrayList<NoteToProb>> h)
    {
        this.SongButtonMap = h;
    }
    protected ImageIcon createImageIcon(String path,
            String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    /**
     * Add a listener for window events.
     */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    //React to window events.
    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void propertyChange(PropertyChangeEvent e) {

    }

    /**
     * Listen to the slider.
     */
}

