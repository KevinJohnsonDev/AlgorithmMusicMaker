/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUIInterface;

import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import music.demo.pkg3.Music;

/**
 *
 * @author Kevin
 */
public class StopButton  extends JPanel
                   implements   
        WindowListener
        
         {
   
    private final JButton b1;
    private Icon leftButtonIcon;
   
 
    
    public StopButton()
            {
     b1 = new JButton("stop");
    b1.setVerticalTextPosition(AbstractButton.CENTER);
    b1.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
            add(b1);
 b1.addActionListener(new java.awt.event.ActionListener() {
    @Override
    public void actionPerformed(java.awt.event.ActionEvent evt) {
                if(Music.getPlaying())
        {
            System.out.println(Music.getPlaying()+ "hello");
            Music m = Music.getInstance();
        m.stopMusic();
        }
    }
});

      // IageIcon leftButtonIcon = new ImageIcon("images/right.gif");


           
            }

   public void stateChanged(ChangeEvent e) {
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



    

