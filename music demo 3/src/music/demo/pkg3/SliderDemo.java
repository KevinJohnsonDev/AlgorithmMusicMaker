package music.demo.pkg3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.NumberFormatter;
import org.jfugue.*;
/*
 * SliderDemo.java requires all the files in the images/doggy
 * directory.
 */

public class SliderDemo extends JPanel
        implements ActionListener,
        WindowListener,
        ChangeListener {
    public Music music;
    private Pattern randNotes;
    private static final int tempo_MIN = 30;
    private static final int tempo_MAX = 180;
    private static final int tempo_INIT = 70;    //initial frames per second
    public static int slidernum;
  public static JFormattedTextField textField;
    int delay;
    Timer timer;
    boolean frozen = false;
  
    JLabel picture;

    public SliderDemo() {
          slidernum =  70;
  
        Music music = new Music();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        delay = 1000 / tempo_INIT;

        //Create the label.
        JLabel sliderLabel = new JLabel("BPM ", JLabel.CENTER);
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        java.text.NumberFormat numberFormat = java.text.NumberFormat.getIntegerInstance();
        NumberFormatter formatter = new NumberFormatter(numberFormat);
        formatter.setMinimum(new Integer(tempo_MIN));
        formatter.setMaximum(new Integer(tempo_MAX));

        //    textField.addPropertyChangeListener((PropertyChangeListener) this); //may not work
        JSlider setTempo = new JSlider(JSlider.HORIZONTAL,
                tempo_MIN, tempo_MAX, tempo_INIT);
     

        setTempo.addChangeListener(this);
        //Turn on labels at major tick marks.
        setTempo.setMajorTickSpacing(20);
        setTempo.setMinorTickSpacing(5);
        setTempo.setPaintTicks(true);
        setTempo.setPaintLabels(true);
        setTempo.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0));
        Font boxFont = new Font("Serif", Font.ITALIC, 15);
        setTempo.setFont(boxFont);

        JPanel labelAndTextField = new JPanel(); //use FlowLayout

        textField = new JFormattedTextField(formatter);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setValue(new Integer(tempo_INIT));
        textField.setColumns(3); //get some space
        textField.getInputMap().put(KeyStroke.getKeyStroke(
                KeyEvent.VK_ENTER, 0),
                "check"); //delete this ge
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("ENTER key pressed");

                    if (!textField.isEditValid()) { //The text is invalid.
                        Toolkit.getDefaultToolkit().beep();
                        ImageIcon icon = createImageIcon("images/erroricon.png",
                                "error icon");
                        String message = "Number must be between 30 and 180";
                        JOptionPane.showMessageDialog(new JFrame(), message, "Input Error",
                                JOptionPane.ERROR_MESSAGE, icon);
                        // textField.setValue(new Integer(tempo_INIT));
                    } else {
                        try {                    //The text is valid,
                            textField.commitEdit();
                            setTempo.setValue((int) textField.getValue());
                            //so use it.
                        } catch (ParseException exc) {
                            System.out.println("Commit edit exception " + exc.getMessage());
                        }
                    }

                }

            }
        });

        labelAndTextField.add(sliderLabel);
        labelAndTextField.add(textField);

        //Put everything together.
        add(sliderLabel);
        add(labelAndTextField);
        add(setTempo);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//Where the components are created:
        //Set up a timer that calls this object's action handler.
        timer = new Timer(delay, this);
        timer.setInitialDelay(delay * 7); //We pause animation twice per cycle
        //by restarting the timer
        timer.setCoalesce(true);

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

    public void actionPerformed(ActionEvent e) // this is for slider not for textbox
    {

    }

    /**
     * Listen to the slider.
     */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            slidernum = (int) source.getValue();
                if (frozen) {
                    textField.setValue(slidernum);
                    setSliderNum(slidernum);
  
                } else {
                    textField.setValue(slidernum);

                }

            }
        }
    

    public void propertyChange(PropertyChangeEvent e) {
        if ("value".equals(e.getPropertyName())) {
            Number value = (Number) e.getNewValue();
     //   if (setTempo != null && value != null) {
            //      setTempo.setValue(value.intValue());
            // }
        }
    }

    //Called when the Timer fires.
    protected void updateNotes() {

    }
    public static int getSliderNum()
    {
        return slidernum;
    }
        public static void setSliderNum(int i)
    {
        slidernum = i;
    }

    /**
     * Update the label to display the image for the current frame.
     */
    protected void updatePicture(int frameNum) {

    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
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
    public static String getTextFieldValue()
    {
        return textField.getText();
    }
    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */

}
