/*

 */
package music.demo.pkg3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Converts Music String from numerical format to JFUGUE format A40.145 = A4q
 *
 * @author Kevin
 */
public class MusicStringConverter {

    private String originalMusicString;
    private String convertedMusicString;
    private LinkedList<Tempo> listOfTempos;
    private LinkedList<DurationList2> durationList;


    public MusicStringConverter(String musicString,LinkedList<Tempo> listOfTempos, LinkedList<DurationList2> durationList) {
        originalMusicString = musicString;
        this.listOfTempos = listOfTempos;
        this.durationList = durationList;
        correctMusicString();
        removeDuplicateVoices();
    }




/*Should be static Method add more params*/
 //TODO make it so that chords only count in the same track, should not make chords outside of the current track
    private void correctMusicString() {
        convertedMusicString = originalMusicString;
        Pattern pattern = Pattern.compile("@(\\d+)\\s+(V\\d+\\s+)?[A-Ga]#?b?\\d+/(\\d+.\\d+)");
        Matcher matcher = pattern.matcher(originalMusicString);
        Tempo currentTempo = null;
        Tempo previousTempo = null;
        DurationList2 DL = null;
        int previousTime=-10;
        int time;
        String voice;
        String previousVoice = "";
        String replaceString = "";
        while (matcher.find()) {
            String originalString = originalMusicString.substring(matcher.start(), matcher.end());
            time = Integer.parseInt(originalMusicString.substring(matcher.start(1), matcher.end(1)));
            String strDuration = originalMusicString.substring(matcher.start(3), matcher.end(3));
            double duration = Double.parseDouble(strDuration);
            int timeNotePlayed = originalMusicString.substring(matcher.start(1), matcher.end(1)).length() + 1;
            voice = originalMusicString.substring(matcher.start(2), matcher.end(2));
                    
            currentTempo = getTempo(time);           

            if (!currentTempo.equals(previousTempo) || null == DL) {
                DL = new DurationList2(currentTempo.getBpm());
                
            }

            String abbreviation = replaceNote(duration, DL);
            if (null != abbreviation) {
                replaceString = originalString.replace(strDuration, abbreviation);
                convertedMusicString = convertedMusicString.replace(originalString, replaceString);
            }
            if(time == previousTime && voice.equals(previousVoice)){
                String timeReplaceString;
                timeReplaceString = "+" + replaceString.substring(timeNotePlayed);
                convertedMusicString =  convertedMusicString.replace(replaceString, timeReplaceString);
            }
            previousTempo = currentTempo;
            previousTime = time;
            previousVoice = voice;
        }
       // System.out.println(convertedMusicString);
    }

    private String replaceNote(double noteDuration, DurationList2 DL) {
        double[] Note_To_Durations = DL.getNoteToDurations();
        double roundToNote = 100;
        double noteToPick = 0;
         double difference; 
        for (int i = 0; i < Note_To_Durations.length; i++) {
            //find difference and go down until closest to actual note is found
            difference = Math.abs(noteDuration - Note_To_Durations[i]);
           
            if (difference < roundToNote) {
                roundToNote = difference;
                noteToPick = Note_To_Durations[i];
               //  System.out.println(noteDuration + " difference " + difference + " i" + i + " " + Note_To_Durations[i]);
            }
        }

        DL.getDuration_To_Abbreviation().get(DL.getValueDurationMap().get(noteToPick));
        return DL.getDuration_To_Abbreviation().get(DL.getValueDurationMap().get(noteToPick));

    }
    
    private void removeDuplicateVoices(){
        Pattern pattern = Pattern.compile("(V\\d+)");
        Matcher matcher = pattern.matcher(convertedMusicString);
        String voice = "";
        String previousVoice = "";
        StringBuilder sb = new StringBuilder(convertedMusicString);
        int offset = 0;
        while (matcher.find()) {
            voice = sb.substring(matcher.start(1)-offset, matcher.end(1)-offset);
           // System.out.println("VOICE " + voice);
            if(voice.equals(previousVoice)){
                int startIndex = matcher.start(1) - offset;
                int endIndex = matcher.end(1) - offset;
                
                sb.replace(startIndex,endIndex,"");  
                offset += endIndex - startIndex;
            }
                previousVoice = voice;
            
        }
        convertedMusicString = sb.toString();
        convertedMusicString = convertedMusicString.replaceAll("^ +| +$|( )+", "$1"); // clean up whitespace 
        System.out.println(convertedMusicString);
    }
    
    

    private Tempo getTempo(int time) {
        Tempo tempo = null;
        int previousTime = 0;
        for (Tempo t : listOfTempos) {
            if (t.getTime() > time && time > previousTime) //if the time is greater we have a start range
            {
                return tempo;
            }
            tempo = t;
            previousTime = tempo.getTime();
        }
        return tempo;
    }

}
