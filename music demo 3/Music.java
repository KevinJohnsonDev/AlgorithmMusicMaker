/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.Scanner;
import org.jfugue.Pattern;
import org.jfugue.Player;

/**
 *
 * @author KevinJohnson
 */
public class Music  {
    private static NoteProbability nProb; //singleton
    private static playerThread pthread;
    private static Thread threadPlyr;
   // private Player player;
    public static boolean playing;
    public static int voicenum;
    // private static Player player = new Player();
    private static Pattern pattern = new Pattern();

    
      public Music()
     {
         NoteProbability nProb = new NoteProbability();
     }
    
    public static void startMusic(int tempo) // pattern is a JFugue object which holds the generated music
    {
        
        System.out.println("active");
        playerThread.setOn(true);

 String s = "T"  + tempo + " V" + voicenum + " " + music(10);
  String Chords = "V" + ++voicenum + " I[Trumpet] " + music(10);
        Pattern t = new Pattern();
        t.add(s);
        t.add(Chords);
        if (pthread == null) {
            pthread = new playerThread();
        } else {
            pthread = null;
            pthread = new playerThread();
        }
        if (threadPlyr == null) {
            threadPlyr = new Thread(pthread);
        } else {
            threadPlyr = null;
            threadPlyr = new Thread(pthread);
        }
        
        pthread.setPattern(t);
        playing = true;
        threadPlyr.start();
        
 
 int active = Thread.activeCount();
          System.out.println("currently active threads: " + active);
    }

    public static void stopMusic() {
        
       playerThread.setOn(false);
       if( playing)
       {
           playerThread.player.stop();
           threadPlyr = null;
           pthread = null;
           playing = false;
       } else {
       }
    }
       
    public static String music(int noteNumArg) {
        int x;
        Scanner keyboard = new Scanner(System.in);
        String retNotes = "";

        int noteNum;
        char note = 'c';
        int C = 4; //common interval to increase notes by 
        int noteNumtracker = 0;
        noteNum = noteNumArg;//how  many notes you want to generate in your score
        noteNumtracker = 6;

        for (int count = 0; count <= noteNum; count++) {
            switch (noteNumtracker) {
                case 1:
                    note = 'e';
                    //spawn lowest e note
                    break;
                case 2:
                    note = 'f';
                    break;
                case 3:
                    note = 'g';
                    break;
                case 4:
                    note = 'a';
                    break;
                case 5:
                    note = 'b';
                    break;
                case 6:
                    note = 'c';
                    break;
                case 7:
                    note = 'd';
                    break;
                default:
                    noteNumtracker = noteNumtracker % 7;
                    switch (noteNumtracker) {
                        case 1:
                            note = 'e';
                            //spawn lowest e note
                            break;
                        case 2:
                            note = 'f';
                            break;
                        case 3:
                            note = 'g';
                            break;
                        case 4:
                            note = 'a';
                            break;
                        case 5:
                            note = 'b';
                            break;
                        case 6:
                            note = 'c';
                            break;
                        case 7:
                            note = 'd';
                            break;
                        default:
                            System.out.println("ERROR in MUSIC METHOD");

                    }
                    break;
            }
            x = (int) (Math.random() * 8);
            retNotes += note + "" + x + " ";

            noteNumtracker = noteNumtracker + C;

        }
        voicenum++;
        return retNotes;
    }



   public static boolean getPlaying()
   {
       return playing;
   }
    

}
