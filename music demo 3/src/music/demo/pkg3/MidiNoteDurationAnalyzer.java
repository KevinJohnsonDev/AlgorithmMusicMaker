/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;
//s.substring(0,1).matches("[0-9]")

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
//Analyzes the musicString From MidiFileCorrector to get total per voice
public class MidiNoteDurationAnalyzer {

    private double largestChange; //tracks the largest change in case of revisioning purposes
    private double totalNotesPerVoice; //used to get averages of Note durations
    private final String musicString;
    private final TreeMap<String, NoteDurationCount> NoteToDurationsWithoutTracks;
    private final ArrayList<TreeMap<String, NoteToProb>> ListOfNotesWithVoices;
    private final ArrayList<TreeMap<String, NoteDurationCount>> NotesToDurationsWithTracks; //key is NOTE+DURATION Including Decimal 
    /*
     Accepts Midi Translated MusicString as Parameter
     Corrects MusicString to proper music Theory levels and Contains Values of Notes
    
     */

    public MidiNoteDurationAnalyzer(String musicString, ListOfNotes LOM) {
        this.musicString = musicString;
        DurationList DL = new DurationList(musicString);
        MusicAnalysisContainer.setDL(DL);
        double[] listOfDurations = DL.getNoteToDurations();

        NotesToDurationsWithTracks = new ArrayList<>();
        NoteToDurationsWithoutTracks = new TreeMap<>();
        for (int i = 0; i < listOfDurations.length; i++) {
            for (String listOfNotesWithoutTrack : LOM.getListOfNotesWithoutTrack().keySet()) {
                NoteToDurationsWithoutTracks.put(listOfNotesWithoutTrack + "/" + listOfDurations[i], new NoteDurationCount(listOfNotesWithoutTrack, listOfDurations[i]));
            }
            //
            //  for (int j = 0; j < LOM.getListOfNotesWithoutTrack().length; j++) {
            //  NoteToDurationsWithoutTracks.put(LOM.getListOfNotesWithoutTrack()[j]+listOfDurations[i], new NoteDurationCount(LOM.getListOfNotesWithoutTrack()[j], listOfDurations[i]));
        }
        ListOfNotesWithVoices = LOM.getListOfNotesWithVoices();
        for (int numVoices = 0; numVoices < ListOfNotesWithVoices.size(); numVoices++) {
            TreeMap<String, NoteToProb> T = ListOfNotesWithVoices.get(numVoices);
            NotesToDurationsWithTracks.add(new TreeMap<>());
            for (String s : T.keySet()) {
                for (int i = 0; i < listOfDurations.length; i++) {
                    NotesToDurationsWithTracks.get(numVoices).put(s + "/" + listOfDurations[i], new NoteDurationCount(s, listOfDurations[i]));
                }
            }
        }

        initializeNotesToDurationsWithoutTrack(musicString);
        initializeNotesToDurationsWithTracks(musicString);
    }

    private void initializeNotesToDurationsWithoutTrack(String musicString) {
        DurationList DL = new DurationList(musicString);
        double[] ArrayOfDurations = DL.getNoteToDurations();
        double highestDuration = getMaxDuration(ArrayOfDurations);
        Pattern pattern = Pattern.compile("V\\d+\\s+([A-Ga-g]#?b?d?\\d+)/(\\d+.\\d+)");
        Matcher matcher = pattern.matcher(musicString);
        while (matcher.find()) {
            boolean flag = false;
            String note = matcher.group(1);
            double duration = Double.parseDouble(matcher.group(2));
            double roundToNote = 100; //for rounding durations
            double DurationToPick = 100;

            for (int i = 0; i < ArrayOfDurations.length; i++) {
                double value = ArrayOfDurations[i];
                double difference;  //find difference and go down until closest to actual note is found

                difference = Math.abs(duration - value);
                if (difference > highestDuration) {
                    flag = true;
                    addorIncrementUnknownhHigherDurationToMap(difference, matcher.start(2),matcher.end(2));
                    break;
                } else if (difference < roundToNote) {

                    roundToNote = difference;
                    DurationToPick = value;

                }

            }
            if (!flag) {
                note = note + "/" + DurationToPick;
                double count = NoteToDurationsWithoutTracks.get(note).getCount();
                NoteToDurationsWithoutTracks.get(note).setCount(count + 1);
            }

        }

//        LinkedList<String> ll = new LinkedList();
//        for (String s : NoteToDurationsWithoutTracks.keySet()) {
//            if (NoteToDurationsWithoutTracks.get(s).getCount() == 0.0) {
//
//                ll.add(s);
//            }
//        }
//        for (int i = 0; i < ll.size(); i++) {
//            NoteToDurationsWithoutTracks.remove(ll.get(i));
//        }
        removeZeroValueEntriesFromMap(NoteToDurationsWithoutTracks);

        setProbabilityDivideByTotalsWithoutTracks();

    }

    public double getMaxDuration(double[] ArrayOfDurations) {
        double max = ArrayOfDurations[0];
        for (int j = 1; j < ArrayOfDurations.length; j++) {
            if (Double.isNaN(ArrayOfDurations[j])) {
                return Double.NaN;
            }
            if (ArrayOfDurations[j] > max) {
                max = ArrayOfDurations[j];
            }
        }

        return max;
    }

    public void addorIncrementUnknownhHigherDurationToMap(double duration, int startIndex, int endIndex)  {
        String s;
        
        System.out.println("triggerd " + musicString.substring(startIndex, endIndex) + "/" + duration);
         s = musicString.substring(startIndex, endIndex) + "/" + duration;
        if (NoteToDurationsWithoutTracks.containsKey(s)) {
            NoteDurationCount n = NoteToDurationsWithoutTracks.get(s);
            n.setCount(n.getCount() + 1);
        } else {
            NoteToDurationsWithoutTracks.put(s, new NoteDurationCount(s, duration));
        }

    }
    
       public void addorIncrementUnknownhHigherDurationToMapWithTrackNumber(double duration, int startIndex, int endIndex, int track) {
        String s;
        
        System.out.println("triggerd " + musicString.substring(startIndex, endIndex) + "/" + duration);
         s = musicString.substring(startIndex, endIndex) + "/" + duration;

        if (NotesToDurationsWithTracks.get(track).containsKey(s)) {
            NoteDurationCount n = NotesToDurationsWithTracks.get(track).get(s);
            n.setCount(n.getCount() + 1);
        } else {
            NotesToDurationsWithTracks.get(track).put(s, new NoteDurationCount(s, duration));
        }

    }

    public void setProbabilityDivideByTotalsWithoutTracks() {
        TreeMap<String, Integer> totalOfEachNote = getTotalsForEachNoteWithoutTracks(); //get totals for each note ignores duration
        for (String note : NoteToDurationsWithoutTracks.keySet()) {
            if (Character.isDigit(note.charAt(4))) // sharp or flat c#6/
            {
                NoteToDurationsWithoutTracks.get(note).setCount(NoteToDurationsWithoutTracks.get(note).getCount() / totalOfEachNote.get(note.substring(0, 3)) * 100.0);
            } else //regular Note ex: c5/0.0
            {
                NoteToDurationsWithoutTracks.get(note).setCount(NoteToDurationsWithoutTracks.get(note).getCount() / totalOfEachNote.get(note.substring(0, 2)) * 100.0);
            }

        }
    }

    //THIS IS A LOT BETTER THEN THE MIDINOTEPROBABILITYANALYZERS METHOD, WE CAN DO ALL IN ONE SWOOP REMEMBER THIS FOR LATER ON
//THIS LOOKS BUGGED AS HELL
    public TreeMap<String, Integer> getTotalsForEachNoteWithoutTracks() {
        int count=0;
        
          
            String[] NOTE_NAMES_SHARPS_AND_FLATS = {"Cb", "C", "C#", "Db", "D", "D#", "Eb", "E", "E#", "Fb", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B", "B#"};
            TreeMap<String, Integer> Totals = new TreeMap<>();
            for (String s : NOTE_NAMES_SHARPS_AND_FLATS) {
                for (int octave = 0; octave < 9; octave++) {
                    Pattern pattern = Pattern.compile("V+\\d+"  + "\\s+(" + s + octave + ")/(\\d+.\\d+)");
                    Matcher matcher = pattern.matcher(musicString);
                    while (matcher.find()) {
                        count++;
                    }
                

                Totals.put(s+octave, count);
                   count = 0;
                }            
            }
            return Totals;
        }
    

    public void setProbabilityDivideByTotalsWithTracks(TreeMap<String, NoteDurationCount> noteList, int voiceNum) {
        TreeMap<String, Integer> totalOfEachNote = getTotalsForEachNoteWithTracks(voiceNum);
        for (String note : noteList.keySet()) {

            if (Character.isDigit(note.charAt(4))) //Sharp or flat note c#3/ vs c3/0
            {
                noteList.get(note).setCount(noteList.get(note).getCount() / totalOfEachNote.get(note.substring(0, 3)) * 100);
            } else // Regular note B,C
            {
                noteList.get(note).setCount(noteList.get(note).getCount() / totalOfEachNote.get(note.substring(0, 2)) * 100);
            }
        }

    }
    //THIS IS A LOT BETTER THEN THE MIDINOTEPROBABILITYANALYZERS METHOD, WE CAN DO ALL IN ONE SWOOP REMEMBER THIS FOR LATER ON

//    public TreeMap<String, Integer> getTotalsForEachNoteWithTracks(int voiceNum) {
//        String voice = "V" + voiceNum;
//        String[] NOTE_NAMES_SHARPS_AND_FLATS = {"Cb", "C", "C#", "Db", "D", "D#", "Eb", "E", "E#", "F#", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B", "B#"};
//        TreeMap<String, Integer> Totals = new TreeMap<>();
//        for (String s : NOTE_NAMES_SHARPS_AND_FLATS) {
//            int voiceIndex = 0;
//            int lastIndex = 0;
//            int count = 0;
//            while (voiceIndex != -1 && lastIndex != -1) {
//                voiceIndex = musicString.indexOf(voice, lastIndex);
//                lastIndex = musicString.indexOf(s, voiceIndex);
//
//                if (lastIndex != -1) {
//                    count++;
//                    lastIndex += s.length();
//                }
//
//            }
//            Totals.put(s, count);
//            count = 0;
//        }
//        return Totals;
//    }
    public TreeMap<String, Integer> getTotalsForEachNoteWithTracks(int voiceNum) {
        int count=0;
        
            String[] NOTE_NAMES_SHARPS_AND_FLATS = {"Cb", "C", "C#", "Db", "D", "D#", "Eb", "E", "E#", "Fb", "F", "F#", "Gb", "G", "G#", "Ab", "A", "A#", "Bb", "B", "B#"};
            TreeMap<String, Integer> Totals = new TreeMap<>();
            for (String s : NOTE_NAMES_SHARPS_AND_FLATS) {
                for (int octave = 0; octave < 9; octave++) {
                    Pattern pattern = Pattern.compile("V+" + voiceNum + "\\s+(" + s + octave + ")/(\\d+.\\d+)");
                    Matcher matcher = pattern.matcher(musicString);
                    while (matcher.find()) {
                        count++;
                    }
                

                Totals.put(s+octave, count);
                   count = 0;
                }            
            }
            return Totals;
        }
        //    private String[] getNextNote(String MusicString, int noteIndex)
        //    {
        //        String returnVar[] = new String[2];
        //        
        //    }
    private void initializeNotesToDurationsWithTracks(String musicString) {
        DurationList DL = new DurationList(musicString);
        double[] ArrayOfDurations = DL.getNoteToDurations();
        double highestDuration = getMaxDuration(ArrayOfDurations);
        Pattern pattern = Pattern.compile("V(\\d+)\\s+([A-Ga-g]#?b?\\d+)/(\\d+.\\d+)"); //Voice Note Duration
        Matcher matcher = pattern.matcher(musicString);
        while (matcher.find()) {
            boolean flag = false;
            String note = matcher.group(2);
            double duration = Double.parseDouble(matcher.group(3));
            double roundToNote = 100; //for rounding durations
            double DurationToPick = 100;

            for (int i = 0; i < ArrayOfDurations.length; i++) {
                double value = ArrayOfDurations[i];
                double difference;  //find difference and go down until closest to actual note is found

                difference = Math.abs(duration - value);
                if (difference > highestDuration) {
                    flag = true;
                    addorIncrementUnknownhHigherDurationToMapWithTrackNumber(difference, matcher.start(3),matcher.end(3),Integer.parseInt(matcher.group(1)) );
                    break;
                } else if (difference < roundToNote) {

                    roundToNote = difference;
                    DurationToPick = value;

                }

            }
            if (!flag) {
                note = note + "/" + DurationToPick;
                TreeMap<String, NoteDurationCount> t = NotesToDurationsWithTracks.get(Integer.parseInt(matcher.group(1)));
                // errror confirmed not counting  System.out.println(musicString.substring(voiceIndex, musicString.indexOf(" ", voiceIndex)) + " "+  musicString.substring(noteIndex, startIndex - 1));
   
                t.get(note).setCount(t.get(note).getCount() + 1);
            }

        }

        int voiceNum = 0;
        for (TreeMap<String, NoteDurationCount> voice : NotesToDurationsWithTracks) {
            removeZeroValueEntriesFromMap(voice);
            setProbabilityDivideByTotalsWithTracks(voice, voiceNum);
            voiceNum++;
        }

    }

    public void removeZeroValueEntriesFromMap(TreeMap<String, NoteDurationCount> mapToRemoveEntries) {
        LinkedList<String> ll = new LinkedList();
        for (String s : mapToRemoveEntries.keySet()) {
            if (mapToRemoveEntries.get(s).getCount() == 0.0) {

                ll.add(s);
            }
        }
        for (int i = 0; i < ll.size(); i++) {
            mapToRemoveEntries.remove(ll.get(i));
        }

    }

    public double getLargestChange() {
        return largestChange;
    }

    public void setLargestChange(double largestChange) {
        this.largestChange = largestChange;
    }

    public double getTotalNotesPerVoice() {
        return totalNotesPerVoice;
    }

    public void setTotalNotesPerVoice(double totalNotesPerVoice) {
        this.totalNotesPerVoice = totalNotesPerVoice;
    }

    public TreeMap<String, NoteDurationCount> getNoteToDurationsWithoutTracks() {
        return NoteToDurationsWithoutTracks;
    }

    public ArrayList<TreeMap<String, NoteDurationCount>> getNotesToDurationsWithTracks() {
        return NotesToDurationsWithTracks;
    }

}


/*Garbage methods No Longer used 
 //    private void initializeNotesToDurationsWithoutTrack(String musicString) {
 //
 //        DurationList DL = new DurationList(musicString);
 //        double[] ArrayOfDurations = DL.getNoteToDurations();
 //
 //        int startIndex = 0;
 //        int endIndex = 0;
 //        double highestDuration = getMaxDuration(ArrayOfDurations);
 //        int total = 0;
 //        int noteIndex = startIndex;
 //        //get Note then get duration
 //        //368a40 @2880 V0 F5/0.016094420600858368a30 @3120 V0 D5/0.016094420600858368a30 @5760 V0 C7/0.016094420600858368a80 @6000 V0 G6/0.016094420600858368a80 @6240 
 //
 //        while (musicString.indexOf("/", startIndex) != -1) {
 //            boolean flag = false;
 //            boolean containsD = false;
 //            total++;
 //            startIndex = musicString.indexOf("/", startIndex) + 1; //every / 
 //             if(musicString.indexOf("a", startIndex) < musicString.indexOf("d", startIndex) )
 //            {
 //                if(musicString.indexOf("a", startIndex) != -1)
 //                {
 //                     endIndex = musicString.indexOf("a", startIndex);
 //                }
 //            }
 //            else 
 //            {
 //                if(musicString.indexOf("d", startIndex) == -1)
 //                {
 //                    endIndex = musicString.indexOf("a", startIndex);
 //                }
 //                else
 //                {
 //                     endIndex = musicString.indexOf("d", startIndex);
 //                }
 //            }
 //          
 //            noteIndex = startIndex - 1;
 //            double roundToNote = 100;
 //            double DurationToPick = 100;
 //
 //            for (int i = 0; i < ArrayOfDurations.length; i++) {
 //                double value = ArrayOfDurations[i];
 //                double difference;  //find difference and go down until closest to actual note is found
 //
 //                difference = Math.abs(Double.parseDouble(musicString.substring(startIndex, endIndex)) - value);
 //                if (difference > highestDuration) {
 //                    flag = true;
 //                    addorIncrementUnknownhHigherDurationToMap(difference, startIndex);
 //                    break;
 //                }
 //               else if (difference < roundToNote) {
 //
 //                    roundToNote = difference;
 //                    DurationToPick = value;
 //
 //                }
 //
 //            }
 //            if (!flag) {
 //                if (musicString.charAt(startIndex - 3) == '#' || musicString.charAt(startIndex - 3) == 'b') //accounting for sharp
 //                {
 //                    noteIndex -= 3; //Accounting for Number and / EX : V11 B#5/0.025387263339070567a114
 //                } else {
 //                    noteIndex -= 2;
 //                }
 //                String note = musicString.substring(noteIndex, startIndex - 1) + "/" + DurationToPick;
 //                double count = NoteToDurationsWithoutTracks.get(note).getCount();
 //                NoteToDurationsWithoutTracks.get(note).setCount(++count);
 //            }
 //            startIndex = startIndex + 1;
 //        }
 //        //REMOVE ALL KEYS THAT HAVE 1 (not exist)
 //        LinkedList<String> ll = new LinkedList();
 //        for(String s: NoteToDurationsWithoutTracks.keySet())
 //        {
 //            if(NoteToDurationsWithoutTracks.get(s).getCount()==0.0)
 //            {
 //
 //               ll.add(s);
 //           
 //            }
 //            else
 //            {
 //            }
 //        }
 //        for(int i = 0; i<ll.size(); i++)
 //        {
 //            NoteToDurationsWithoutTracks.remove(ll.get(i));
 //        }
 //        
 //        setProbabilityDivideByTotalsWithoutTracks(NoteToDurationsWithoutTracks);
 //
 //    }
 */
//          private void initializeNotesToDurationsWithTracks(String musicString) {
//
//        DurationList DL = new DurationList(musicString);
//        double[] ArrayOfDurations = DL.getNoteToDurations();
//        int startIndex = 0;
//        int endIndex = 0;
//        int noteIndex = startIndex;
//        int voiceIndex = 0;
//        //get Note then get duration
//        //get duration
//        while (musicString.indexOf("/", startIndex) != -1) {
//
//            startIndex = musicString.indexOf("/", startIndex) + 1;
//            noteIndex = startIndex - 1;//every / 
//            if (musicString.indexOf("a", startIndex) < musicString.indexOf("d", startIndex)) {
//                if (musicString.indexOf("a", startIndex) != -1) {
//                    endIndex = musicString.indexOf("a", startIndex);
//                }
//            } else {
//                if (musicString.indexOf("d", startIndex) == -1) {
//                    endIndex = musicString.indexOf("a", startIndex);
//                } else {
//                    endIndex = musicString.indexOf("d", startIndex);
//                }
//            }
//
//            double roundToNote = 100;
//            double DurationToPick = 100;
//
//            for (int i = 0; i < ArrayOfDurations.length; i++) {
//                double value = ArrayOfDurations[i];
//                double difference;  //find difference and go down until closest to actual note is found
//
//                difference = Math.abs(Double.parseDouble(musicString.substring(startIndex, endIndex)) - value); //assumes duration is less then a second
//                if (difference < roundToNote) {
//                    roundToNote = difference;
//                    DurationToPick = value;
//                }
//            }
//            if (musicString.charAt(startIndex - 3) == '#' || musicString.charAt(startIndex - 3) == 'b') //accounting for sharp
//            {
//                noteIndex -= 3; //Accounting for Number and / EX : V1 B#5/0.025387263339070567a114
//            } else {
//                noteIndex -= 2;
//            }
//            if (musicString.charAt(noteIndex - 4) == 'V') //2 number voice
//            {
//                voiceIndex = noteIndex - 3;
//            } else {
//                voiceIndex = noteIndex - 2;
//            }
//
//            TreeMap<String, NoteDurationCount> t = NotesToDurationsWithTracks.get(Integer.parseInt(musicString.substring(voiceIndex, musicString.indexOf(" ", voiceIndex))));
//            // errror confirmed not counting  System.out.println(musicString.substring(voiceIndex, musicString.indexOf(" ", voiceIndex)) + " "+  musicString.substring(noteIndex, startIndex - 1));
//            String note = musicString.substring(noteIndex, startIndex - 1) + "/" + DurationToPick;
//            t.get(note).setCount(t.get(note).getCount() + 1);
//            startIndex = endIndex + 1;
//            //Set COUNT/TOTAL AFTER LOOP
//
//        }
//        int voiceNum = 0;
//        for (TreeMap<String, NoteDurationCount> voice : NotesToDurationsWithTracks) {
//            setProbabilityDivideByTotalsWithTracks(voice, voiceNum);
//            voiceNum++;
//        }
//
//    }
