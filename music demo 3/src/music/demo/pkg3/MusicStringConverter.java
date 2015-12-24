/*

 */
package music.demo.pkg3;

import java.util.HashMap;
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

    // V0 @0 T585 @0 T585 @26624 T585 @26880 T572 @27136 T567 @27392 T561 @27648 T550 
    //@27904 T544 @28160 T539 @28416 T533 @28928 T585 @59392 T585 @59648 T572 @59904 T567 @60160 T561 @60416 T550 
    //@60672 T544 @60928 T539 @61184 T533 @61696 T585 V1 @0 V0 X0=121 @0 V0 X32=0 @0 V0 I[Piano] 
    //@0 V0 X7=101 @0 V0 X10=64 @0 V0 X7=102 @0 V0 X7=110 @3 V0 X100=0 @3 V0 X101=0 
    //@3 V0 X100=0 @3 V0 X101=0 @4 V0 X6=12 @4 V0 X6=12 @5 V0 X38=0 @5 V0 X38=0 @10 V0 X7=102 @10 V0 X7=110 
    //@10 V0 C4/0.20512820512820512a57d0 @546 V0 G4/0.20512820512820512a51d0 @0 V0 B5/0.6153846153846154a61d0 
    //@0 V0 E6/0.6153846153846154a61d0 @0 V0 G6/0.6153846153846154a61d0 @10 V0 B6/0.6153846153846154a75d0 @105
    public MusicStringConverter(String musicString) {
        this.originalMusicString = musicString;
        listOfTempos = new LinkedList<>();
        durationList = new LinkedList<>();
        getListOfTempos();
        getListOfDurations();
        correctMusicString();

    }

    private void getListOfTempos() {
        HashMap<String, String> duplicateChecker = new HashMap();
        Pattern pattern = Pattern.compile("@(\\d+)\\s*T(\\d+)");
        Matcher m = pattern.matcher(originalMusicString);
        while (m.find()) {
            if (!duplicateChecker.containsKey(originalMusicString.substring(m.start(1), m.end(1)))) {
                listOfTempos.add(new Tempo(Integer.parseInt(originalMusicString.substring(m.start(1), m.end(1))),
                        Integer.parseInt(originalMusicString.substring(m.start(2), m.end(2)))));
                duplicateChecker.put(originalMusicString.substring(m.start(1), m.end(1)), originalMusicString.substring(m.start(2), m.end(2)));
            }

        }

    }

    private void getListOfDurations() {
        listOfTempos.stream().map((t) -> new DurationList2(t.getBpm())).forEach((DL) -> {
            durationList.add(DL);
        });
    }

    private void correctMusicString() {
        convertedMusicString = originalMusicString;
        Pattern pattern = Pattern.compile("@(\\d+)\\s+V\\d+\\s+[A-Ga]#?b?\\d+/(\\d+.\\d+)");
        Matcher matcher = pattern.matcher(originalMusicString);
        Tempo currentTempo = null;
        Tempo previousTempo = null;
        DurationList2 DL = null;

        while (matcher.find()) {
            String originalString = originalMusicString.substring(matcher.start(), matcher.end());
            int time = Integer.parseInt(originalMusicString.substring(matcher.start(1), matcher.end(1)));
            String strDuration = originalMusicString.substring(matcher.start(2), matcher.end(2));
            double duration = Double.parseDouble(strDuration);

            currentTempo = getTempo(time);

            if (!currentTempo.equals(previousTempo) || null == DL) {
                DL = new DurationList2(currentTempo.getBpm());
                
            }
            String abbreviation = replaceNote(duration, DL);
            if (null != abbreviation) {
                String replaceString = originalString.replace(strDuration, abbreviation);
                convertedMusicString = convertedMusicString.replace(originalString, replaceString);
            }
            previousTempo = currentTempo;
        }
        System.out.println(convertedMusicString);
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
                 System.out.println(noteDuration + " difference " + difference + " i" + i + " " + Note_To_Durations[i]);
            }
        }

        DL.getDuration_To_Abbreviation().get(DL.getValueDurationMap().get(noteToPick));
        return DL.getDuration_To_Abbreviation().get(DL.getValueDurationMap().get(noteToPick));

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
