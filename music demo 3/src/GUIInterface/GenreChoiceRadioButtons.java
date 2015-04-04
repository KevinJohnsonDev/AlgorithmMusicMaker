/*
*Radio box for genre choices
 */

package GUIInterface;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author Kevin
 */
public class GenreChoiceRadioButtons extends JPanel {
  private final   JRadioButton rock;
  private  final  JRadioButton classical;
   private final JRadioButton pop;
    
    public GenreChoiceRadioButtons()
    {
        ButtonGroup buttonGroup = new ButtonGroup();
         rock = new JRadioButton("Rock");
         classical = new JRadioButton("Classical");
         pop = new JRadioButton("Pop");
        //puts genres in group
        buttonGroup.add(pop);
        buttonGroup.add(rock);
        buttonGroup.add(classical);
        //appends radio buttons in object for display
        add(pop);
        add(rock);
        add(classical);

    }

    public JRadioButton getRock() {
        return rock;
    }

    public JRadioButton getClassical() {
        return classical;
    }

    public JRadioButton getPop() {
        return pop;
    }
    
    
}
