/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */
public class MusicStringCorrector {

    private double largestChange; //tracks the largest change in case of revisioning purposes
    private double totalNotesPerVoice; //used to get averages of Note durations
    private final double[] Note_To_Durations;
    private final String FormattedMusicString;
    private LinkedHashMap<String,Double> NoteToDurations = new LinkedHashMap<>();
    /*
     Accepts Midi Translated MusicString as Parameter
     Corrects MusicString to proper music Theory levels and Contains Values of Notes
    
     */

    public MusicStringCorrector(String s) {
        DurationList DL = new DurationList(s);
        this.Note_To_Durations = DL.getNoteToDurations();
        this.FormattedMusicString = FormatString(s);
    }

    private int getTempo(String s) {
        int start = s.indexOf('T');
        int end = s.indexOf(' ', start);
        return Integer.parseInt(s.substring(start + 1, end));
    }

    //Rounds all note values to respective notes A MUSICSTRING MUST BE PASSED FROM JFUGIE MIDI FILE 
    private String FormatString(String s) {
        StringBuilder Formatter = new StringBuilder(s);
        int startIndex = 0;
        int endIndex = 0;
        boolean flag=false;
        while (Formatter.indexOf("/", startIndex) != -1) {
            startIndex = Formatter.indexOf("/", startIndex) + 1; //every /
            if(Formatter.indexOf("a", startIndex) < Formatter.indexOf("d", startIndex) )
            {
                if(Formatter.indexOf("a", startIndex) != -1)
                {
                     endIndex = Formatter.indexOf("a", startIndex);
                }
                else
                {
                        endIndex = Formatter.indexOf("d", startIndex);
                }
            }
            else 
            {
                if(Formatter.indexOf("d", startIndex) == -1)
                {
                    endIndex = Formatter.indexOf("a", startIndex);
                }
                else
                {
                     endIndex = Formatter.indexOf("d", startIndex);
                }
            }
            

            double roundToNote = 100;
            double noteToPick = 100;
            for (int i =0; i<Note_To_Durations.length; i++) {
                double difference;  //find difference and go down until closest to actual note is found
                difference = Math.abs(Double.parseDouble(Formatter.substring(startIndex, endIndex)) - Note_To_Durations[i]);
                if (difference < roundToNote) {
                    roundToNote = difference;
                    noteToPick = Note_To_Durations[i];
                  ;
                }
            }
            if (endIndex != -1) {

                Formatter.replace(startIndex, endIndex, Double.toString(noteToPick));
            }


                startIndex = endIndex+1;

        }
        
        System.out.println(Formatter);
        
      
        return Formatter.toString();
    }
    public String getFormattedMusicString() {
        return FormattedMusicString;
    }
    @Override
public String toString() {
  StringBuilder sb = new StringBuilder();
  for (Field f : getClass().getDeclaredFields()) {
    sb.append(f.getName());
    sb.append(",");
  }
  sb.deleteCharAt(sb.length()-1);
  return sb.toString();
}

}
