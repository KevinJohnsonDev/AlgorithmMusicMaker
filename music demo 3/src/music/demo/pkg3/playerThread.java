/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfugue.Pattern;
import org.jfugue.Player;

/**
 *
 * @author Kevin
 */
class playerThread implements Runnable // plays midi using jfugue Player
{

    public static Player player;
    private static boolean on;
    private Pattern pt;

    public playerThread() {
        on = true;
    }

    public static void setOn(Boolean tf) {
        on = tf;
        System.out.println(on);
    }

    public void setPattern(Pattern p) {
        pt = p;
    }

    @Override
    public void run() {
        System.out.println(on);
        if (on) {
            if (player == null) {
                System.out.println("case1");
                player = new Player();

            } else {
                System.out.println("case2");
                player = null;
                player = new Player();

            }

            try {
                player.play(pt); // takes a couple mins or more to execute
                
                Music.playing=false;

            } catch (Exception exception) {
                System.out.println("Playercannotplayinrun");
                    String message = "Ready for more music";
                           JOptionPane.showMessageDialog(new JFrame(), message, "Ready",
                                JOptionPane.ERROR_MESSAGE);
                           Music.playing = true;
            }
        } else {
            System.out.println("Turned off");
        }
    }

}
