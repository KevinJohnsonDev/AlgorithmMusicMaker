/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import music.demo.pkg3.Music;
import music.demo.pkg3.SliderDemo;

/**
 *
 * @author Kevin
 */
public class Playbutton extends JPanel
        implements 
        WindowListener  
         {

    boolean frozen = false;
    int delay;
    Timer timer;
    JFormattedTextField textField;
    JLabel picture;
    private Icon leftButtonIcon;
    private final JButton b1;



    public Playbutton() {
        b1 = new JButton("Past Work");
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        add(b1);
         
        b1.addActionListener(new java.awt.event.ActionListener() {
             @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                {
                    Music m = Music.getInstance();
                    m.startMusic(SliderDemo.getSliderNum());
                }
            }
        });
    }
    

   


    /**
     * Add a listener for window events. All Methods below affect how the button would look on event triggers, no functionality is implimented yet
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
