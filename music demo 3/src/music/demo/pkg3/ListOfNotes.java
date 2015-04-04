/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin List of Notes without Tracks List of Notes with Tracks returned
 * as HashMap Number of Tracks
 */
public class ListOfNotes {

    private TreeMap<String,NoteToProb> ListOfNotesWithoutTrack;
    private final ArrayList<TreeMap<String, NoteToProb>> ListOfNotesWithVoices; //List of Voices connecting to their hashtable
    private final int NumberOfTracks;

    public ListOfNotes(String musicString) {
        ListOfNotesWithoutTrack = new TreeMap<>();
        NumberOfTracks = MusicAnalysisContainer.getMIP().getNumberOfTracks();
        ListOfNotesWithVoices = new ArrayList<>();
        for (int i = 0; i < NumberOfTracks; i++) {
            ListOfNotesWithVoices.add(new TreeMap<>());
        }
        createlistOfNotesWithTrack(musicString);
        createlistOfNotesWithoutTracks(musicString);

    }



    private void createlistOfNotesWithTrack(String musicString) {
        TreeMap<String, NoteToProb> NoteProbabilityTable = new TreeMap<>();
               int voiceNum;
        Pattern pattern = Pattern.compile("V(\\d+)\\s+([A-Ga-g]#?b?\\d+)");
         Matcher matcher = pattern.matcher(musicString);
        while (matcher.find()) {
            voiceNum = Integer.parseInt(matcher.group(1));
            NoteProbabilityTable = this.ListOfNotesWithVoices.get(voiceNum);
            String note = (matcher.group(2));
            if (!NoteProbabilityTable.containsKey(note)) {
                 NoteProbabilityTable.put(note, new NoteToProb(note, 0, 3));
            }
        }
           
        }
 

    

    private void createlistOfNotesWithoutTracks(String musicString) {
        Pattern pattern = Pattern.compile("V\\d+\\s+([A-G]#?b?\\d+)");
        Matcher matcher = pattern.matcher(musicString);
                while (matcher.find()) {     
            String note = (matcher.group(1));
            if (!ListOfNotesWithoutTrack.containsKey(note)) {
                ListOfNotesWithoutTrack.put(note, new NoteToProb(note,0,3));
            }
        }
    }

    public TreeMap<String,NoteToProb> getListOfNotesWithoutTrack() {
        return ListOfNotesWithoutTrack;
    }

    public ArrayList<TreeMap<String, NoteToProb>> getListOfNotesWithVoices() {
        return ListOfNotesWithVoices;
    }

    public int getNumberOfTracks() {
        return NumberOfTracks;
    }

}

//      int startIndex = 0;
//        int noteIndex;
//        String voice = "";
//        int voiceStartIndex = 0;
//        int voiceEndIndex = 0;
//        int voiceNum;
//        while (musicString.indexOf("/", startIndex) != -1) {
//            startIndex = musicString.indexOf("/", startIndex);
//            noteIndex = startIndex;
//
//            //5 or 6 depending on V9/V10 Get voiceNumber
//            if (musicString.charAt(startIndex - 2) == '#' || musicString.charAt(startIndex - 2) == 'b') //accounting for sharp
//            {
//                noteIndex -= 3; //Accounting for Number and / EX : V11 B#5/0.025387263339070567a114
//            } else {
//                noteIndex -= 2;
//            }
//            if ((musicString.charAt(noteIndex - 4)) == 'V') //CASE V_DOUBLE_DIGIT
//            {
//                //      System.out.println("Choice a");
//                voiceStartIndex = noteIndex - 3;
//                voiceEndIndex = noteIndex - 1;
//            } else if (musicString.charAt(noteIndex - 3) == 'V') //single digit voice
//            {
//                //  System.out.println("Choice b");
//                voiceStartIndex = noteIndex - 2;
//                voiceEndIndex = noteIndex - 1;
//            }
//            voice = musicString.substring(voiceStartIndex, voiceEndIndex);
//            // System.out.println(voice);
//
//            voiceNum = Integer.parseInt(musicString.substring(voiceStartIndex, voiceEndIndex));
//
//            //end Get Voice
//            String note = musicString.substring(noteIndex, startIndex);
//            NoteProbabilityTable = this.ListOfNotesWithVoices.get(voiceNum);
//            //      System.out.println("voiceNum: " + voiceNum);
//            NoteProbabilityTable.put(note, new NoteToProb(note, 0, 3));
//
//            startIndex++;
//        }
