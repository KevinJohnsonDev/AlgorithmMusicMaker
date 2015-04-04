/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import GUIInterface.FileChooser;
import GUIInterface.SongButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NavigableMap;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import org.jfugue.Pattern;

/**
 *
 * @author KevinJohnson
 */
public class Music {

    private int actualTracksToMake;
    private static Music instance = null; //singleton for class
    private final static String[] listOfChordTypes = {"", "maj", "min", "aug", "dim", "dom7", "maj7", "min7", "sus4", "sus2", "maj6", "min6",
        "dom9", "maj9", "min9", "dim7", "add9", "min11", "dom11", "dom13", "min13", "maj13", "dom7<5", "dom7>5", "maj7<5", "maj7>5", "minmaj7",
        "dom7<5<9", "dom7<5>9", "dom7>5<9", "dom7>5>9"};//first blank for no chord case
    private static NoteProbability nProb; //singleton
    private static playerThread pthread;
    private static Thread threadPlyr;
    public static boolean playing;
    public static int voicenum;
    private static Pattern pattern;
    private ArrayList<NoteToProb> keysAsArray;
    private Pattern repeatPattern;
    private final Random random; // used multiple times no need for reinstation every time

    protected Music() {
        nProb = new NoteProbability();
        ArrayList<NoteToProb> keysAsArray2;
        repeatPattern = new Pattern();
        random = new Random();
    }

    public static Music getInstance() {
        if (instance == null) {
            instance = new Music();
        }
        return instance;
    }

    public void setrepeatPattern(Pattern p) {
        repeatPattern = p;
    }

    public void startMusic(int tempo) // pattern is a JFugue object which holds the generated music
    {
        playerThread.setOn(true);

        String s = "T" + tempo + " V" + voicenum + " " + music(30);
        String Chords = "V" + ++voicenum + " I[Trumpet] " + music(30);

        repeatPattern = new Pattern();
        repeatPattern.add(s);
        repeatPattern.add(Chords);
        FileChooser.setSelectedSong(repeatPattern);
        setrepeatPattern(repeatPattern);
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
        System.out.println(s);
        System.out.println(Chords);
        pthread.setPattern(repeatPattern);
        playing = true;
        threadPlyr.start();
        voicenum = 0;

    }

    public void startMusic(int tempo, SongButton songButton) // pattern is a JFugue object which holds the generated music
    {
        playerThread.setOn(true);

        String s = MusicPrep(30, songButton, 180);
        repeatPattern = new Pattern();
        repeatPattern.add(s);
        setrepeatPattern(repeatPattern);
        songButton.setMusic(repeatPattern); //gives button the pattern

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
        System.out.println(s);
        pthread.setPattern(repeatPattern);
        playing = true;
        threadPlyr.start();
        voicenum = 0;

    }

    public void startMusic(int tempo, int type, SongButton songButton) // pattern is a JFugue object which holds the generated music
    {
        playerThread.setOn(true);

        // s = createSongIgnoringTracks();
        System.out.println("BEGIN PATTERN");

        // System.out.println(s);
        if (type == 1) {
            repeatPattern = createSongIgnoringTracks();
        } else {
            System.out.println("Other PATTERN");
            repeatPattern = createSongWithTracks();
        }
        System.out.println(repeatPattern.toString());
        // repeatPattern.add(s);
        FileChooser.setSelectedSong(repeatPattern);
        setrepeatPattern(repeatPattern);
        songButton.setMusic(repeatPattern); //gives button the pattern
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
        pthread.setPattern(repeatPattern);
        playing = true;
        threadPlyr.start();
        voicenum = 0;

    }

    /**
     *
     */
    public void repeatMusic() {
        //turn the player off and delacocate current threads
        if (playing) {
            playerThread.setOn(false);
            playerThread.player.stop();
            threadPlyr = null;
            pthread = null;
            playing = false;
        } else {
            threadPlyr = null;
            pthread = null;
            playing = false;

        }
        //done allocation
        pthread = new playerThread();
        pthread.setPattern(repeatPattern);
        threadPlyr = new Thread(pthread);
        playerThread.setOn(true); //sets player to play
        playing = true;
        threadPlyr.start();
    }

    public void stopMusic() {

        playerThread.setOn(false);
        if (playing) {
            playerThread.player.stop();
            threadPlyr = null;
            pthread = null;
            playing = false;
        }
    }

    /**
     * @deprecated explanation of why it was deprecated
     */
    @Deprecated
    public String music(int noteNumArg) // for playbutton
    {

        HashMap<String, ArrayList<NoteToProb>> listOfProbs = null;
        String[] strings; //String array of keys
        NoteToProb pullNoteValue = null; // object used to pull values from notes
        int switcher = 0;
        int numofKeys = 0; // iteration through list var
        boolean rightpick = false;
        Random r = new Random(); // random note
        String returnStr = ""; //return string
        int picker; //random number
        try {

            nProb.generateTable();
            listOfProbs = nProb.getTable();
        } catch (IOException e) {
            System.out.println("Could not find file in noteprobability class ");
            System.exit(0);
        }

        strings = (String[]) listOfProbs.keySet().toArray(new String[listOfProbs.size()]); //this is the keyset as a string array;

        for (int i = 0; i < noteNumArg; i++) {
            picker = listOfProbs.keySet().size(); // get number of keys in hashtable
            picker = r.nextInt(picker); // pick random number key from hashtable
            keysAsArray = (ArrayList<NoteToProb>) listOfProbs.get(strings[picker]); // Gets arraylist from table by using String Array strings
            picker = r.nextInt(100); //random number 1-100 
            while (switcher != 100 && !rightpick) {
                switcher += keysAsArray.get(numofKeys).getValue();
                if (switcher >= picker) {
                    pullNoteValue = keysAsArray.get(numofKeys);
                    rightpick = true;

                }
                if (numofKeys != keysAsArray.size()) {
                    numofKeys++;
                }
            }
            //reset vars for beginning of loop Do Not TOUCH
            numofKeys = 0;
            switcher = 0;
            rightpick = false;
            //end reset add to string
            if (pullNoteValue.getNote().equals("Other")) {
                returnStr += processOther() + " ";
            } else {
                returnStr = returnStr + pullNoteValue.getNote() + " ";//gets random index of that arraylist 
            }
        }
        voicenum++;
        return returnStr;
    }

    /**
     * @deprecated explanation of why it was deprecated
     */
    @Deprecated
    public String MusicPrep(int noteNumArg, SongButton Sb, int tempo) //for songButton song button gets hashmap AND Song 
    {
        HashMap<String, ArrayList<NoteToProb>> listOfProbs = null;
        String returnStr = ""; //return string

        try {
            listOfProbs = new HashMap<>();// New Hashtable Object for each song button
            nProb.generateTable(); //generates the base table for the nProb object if not here returns null hashmap
            listOfProbs = nProb.getTable(); //
            Sb.setMap(listOfProbs);
        } catch (IOException e) {
            System.out.println("could not find file in noteprobability file ");
        }
        String[] s = {"", "[Trumpet]"}; // define later by input
        returnStr = MusicGenerator(30, listOfProbs, s, 180);
        return returnStr;
    }

    /**
     * @deprecated explanation of why it was deprecated
     */
    @Deprecated
    public String MusicGenerator(int NumNotesToGen, HashMap<String, ArrayList<NoteToProb>> HMap, String[] Instruments, int tempo) {
        NoteToProb pullNoteValue = null; // object used to pull values from notes
        String[] strings = (String[]) HMap.keySet().toArray(new String[HMap.size()]); //this is the keyset as a string array;
        int picker; //random number
        int switcher = 0;
        int voicenum = 0;
        int numofKeys = 0; // iteration through list var
        boolean rightpick = false;
        String returnStr = "T" + tempo + " ";
        for (int i = 0; i < Instruments.length; i++) {
            returnStr = returnStr + "V" + voicenum++ + " " + Instruments[i] + " ";
            for (int j = 0; j < NumNotesToGen; j++) {

                picker = HMap.keySet().size(); // get number of keys in hashtable
                picker = random.nextInt(picker); // pick random number key from hashtable
                keysAsArray = (ArrayList<NoteToProb>) HMap.get(strings[picker]); // Gets arraylist from table by using String Array strings
                picker = random.nextInt(100); //random number 1-100 
                while (switcher != 100 && !rightpick) {
                    switcher += keysAsArray.get(numofKeys).value;
                    if (switcher >= picker) {
                        pullNoteValue = keysAsArray.get(numofKeys);
                        rightpick = true;

                    }
                    if (numofKeys != keysAsArray.size()) {
                        numofKeys++;
                    }
                }
                //reset vars for beginning of loop Do Not TOUCH
                numofKeys = 0;
                switcher = 0;
                rightpick = false;
                //end reset add to string
                if (pullNoteValue.getNote().equals("Other")) {
                    returnStr += processOther() + " ";
                } else {
                    returnStr = returnStr + pullNoteValue.getNote() + " ";//gets random index of that arraylist 
                }
            }
        }
        return returnStr;
    }

    /**
     * @deprecated explanation of why it was deprecated
     */
    @Deprecated
    public String processOther() {
        Random r = new Random();
        int i = r.nextInt(31);
        String s = "";
        if (voicenum % 2 == 0) {
            s = "C";
        } else {
            s = "G";
        }
        s += listOfChordTypes[i];
        return s;
    }

    /**
     * @deprecated explanation of why it was deprecated
     */
    @Deprecated
    public static boolean getPlaying() {
        return playing;
    }

    public Pattern createSongIgnoringTracks() {
        String s = "T120 ";
        actualTracksToMake = 0;
        Pattern song = new Pattern();
        song.add(s);
        int trackTotal = MusicAnalysisContainer.getMIP().getNumberOfTracks();
        for (int trackNum = 0; trackNum < trackTotal; trackNum++) {
            if (trackNum == 9) {
                continue;
            }

            s = CreateTrack(trackNum) + " ";
            StringTransFormer STF = new StringTransFormer(s);
            s = STF.getConvertedMusicString();
            song.add(s);

        }

        return song;
    }

    public String CreateTrack(int trackNum) {
        if (MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(trackNum).isEmpty()) {
            return " ";
        }
        ArrayList<String> instrumentList = MusicAnalysisContainer.getMIP().getInstrumentNames().get(trackNum);
        StringBuilder track = new StringBuilder(1000); //initial size may grow
        int totalNotesToCreate = 30; //arbitrary number
        String rests = "R";
        for (int numberOfInstruments = 0; numberOfInstruments < instrumentList.size(); numberOfInstruments++) {
            String instrument = MusicAnalysisContainer.getMIP().getInstrumentNames().get(trackNum).get(numberOfInstruments);
            track.append(" I[").append(instrument).append("] ");
            for (int noteNum = 0; noteNum < totalNotesToCreate; noteNum++) {

                track.append(getRandomNoteIgnoringTracks());
            }
            if (instrument == null) {
                instrument = "PIANO";
            }
            if (instrument.equalsIgnoreCase("Pianl LH") || instrument.equalsIgnoreCase("Piano RH")) {
                instrument = "PIANO";
            }
        }
        return "V" + trackNum + track.toString() + " ";
    }

    public String getRandomNoteIgnoringTracks() {
        String note = "";
        String duration;
        Random rand = new Random();
        int randNumber = rand.nextInt(101); //random number 1-100;
        double total = 0;
        int index = 0;
        String[] listOfKeys = MusicAnalysisContainer.getMidiToNotePercentages().keySet().toArray(new String[MusicAnalysisContainer.getMidiToNotePercentages().size()]);
        while (total < randNumber && index < listOfKeys.length) {
            total += MusicAnalysisContainer.getMidiToNotePercentages().get(listOfKeys[index]);
            index++;
        }
        if (index != 0) {
            index--;//After loop number is number be decrimented to be at right index
        }
        int restOrNote = rand.nextInt(2);

        //After loop number is number be decrimented to be at right index
        note = listOfKeys[index];
        duration = getRandomDurationFromNoteIgnoringTracks(note);
        if (restOrNote == 1) {
            note = "R";
        }

        return note + "/" + duration + "a100 ";
    }

    public String getRandomDurationFromNoteIgnoringTracks(String note) {
        String returnString = "";
        Random rand = new Random();
        int randNumber = rand.nextInt(101); //random number 1-100;
        double total = 0;
        int index = 0;
        SortedMap<String, NoteDurationCount> noteToDurations = getByPreffix(MusicAnalysisContainer.getNoteToDurationsWithoutTracks(), note);
        String[] listOfKeys = noteToDurations.keySet().toArray(new String[noteToDurations.size()]);
        while (total < randNumber && index < listOfKeys.length) {
            total += MusicAnalysisContainer.getNoteToDurationsWithoutTracks().get(listOfKeys[index]).getCount();
            index++;
        }
        if (index != 0) {

            index--;//After loop number is number be decrimented to be at right index
        }
        if (noteToDurations.isEmpty()) {
            returnString = "";
        } else {
            returnString = Double.toString(noteToDurations.get(listOfKeys[index]).getDuration());
        }
        return returnString;
    }

    public SortedMap<String, NoteDurationCount> getByPreffix(
            NavigableMap<String, NoteDurationCount> myMap,
            String preffix) {
        return myMap.subMap(preffix, true, preffix + Character.MAX_VALUE, true);
    }

    public Pattern createSongWithTracks() {
        String s = MusicAnalysisContainer.getListOfTempos();
        actualTracksToMake = MusicAnalysisContainer.getMIP().getNumberOfTracks();
        Pattern song = new Pattern();
        song.add(s);

        int trackTotal = MusicAnalysisContainer.getMIP().getNumberOfTracks();
        for (int actualTracks = 0; actualTracks < trackTotal; actualTracks++) {
            if (MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(actualTracks).isEmpty() || MusicAnalysisContainer.getMIP().getInstrumentNames().get(actualTracks).isEmpty()) {
                    actualTracksToMake--;
            }
        }

            for (int trackNum = 0; trackNum < trackTotal; trackNum++) {
                if (trackNum == 5 || MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(trackNum).isEmpty() || MusicAnalysisContainer.getMIP().getInstrumentNames().get(trackNum).isEmpty()) {
                    continue;
                }

                s = CreateTrackWithTracks(trackNum) + " ";
                StringTransFormer STF = new StringTransFormer(s);
                s = STF.getConvertedMusicString();
                song.add(s);

            }
            return song;
        }

    

    public String CreateTrackWithTracks(int trackNum) {

        StringBuilder track = new StringBuilder(1000); //initial size may grow
        int totalNotesToCreate = 6; //arbitrary number
        ArrayList<String> instrumentList = MusicAnalysisContainer.getMIP().getInstrumentNames().get(trackNum);
        for (int numberOfInstruments = 0; numberOfInstruments < instrumentList.size(); numberOfInstruments++) {
            String instrument = MusicAnalysisContainer.getMIP().getInstrumentNames().get(trackNum).get(numberOfInstruments);
            track.append(" I[").append(instrument).append("] ");
            for (int noteNum = 0; noteNum < totalNotesToCreate; noteNum++) {

                track.append(getRandomNoteWithTracks(trackNum));
            }
        }

        return "V" + trackNum + track.toString() + " ";

    }

    public String getRandomNoteWithTracks(int trackNumber) {
        String note = "";
        String duration;
        Random rand = new Random();
        int randNumber = rand.nextInt(101); //random number 1-100;
        double total = 0;
        int index = 0;
        String[] listOfKeys = MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(trackNumber).keySet().toArray(new String[MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(trackNumber).size()]);
        while (total < randNumber && index < listOfKeys.length) {
            total += MusicAnalysisContainer.getMidiToNotePercentages().get(listOfKeys[index]);
            index++;
        }
        if (index != 0) {
            index--;//After loop number is number be decrimented to be at right index
        }

        //After loop number is number be decrimented to be at right index
        note = listOfKeys[index];
             if(actualTracksToMake > 1){
        duration = getRandomDurationFromNoteWithTracks(note, trackNumber);
             }
             else
             {
             duration =   getRandomDurationFromNoteIgnoringTracks(note); 
             }
        if(actualTracksToMake > 1)
        {
        int restOrNote = rand.nextInt(actualTracksToMake);
        if (restOrNote > actualTracksToMake/2) {
            note = "R";
        }
        }
        else
        {
              int restOrNote = rand.nextInt(8);
                    if (restOrNote > 5)  //3/8th chance of rest
                    {
            note = "R";
        }
        }

        return note + "/" + duration + "a100 ";
    }

    public String getRandomDurationFromNoteWithTracks(String note, int trackNumber) {
        String returnString = "";
        Random rand = new Random();
        int randNumber = rand.nextInt(101); //random number 1-100;
        double total = 0;
        int index = 0;
        //    SortedMap <String,NoteDurationCount> noteToDurations = getByPreffix( MusicAnalysisContainer.getNoteToDurationsWithoutTracks(),note);
        SortedMap<String, NoteDurationCount> noteToDurations = getByPreffix(MusicAnalysisContainer.getNotesToDurationsWithTracks().get(trackNumber), note);
        String[] listOfKeys = noteToDurations.keySet().toArray(new String[noteToDurations.size()]);
        while (total < randNumber && index < listOfKeys.length) {
            total += MusicAnalysisContainer.getNotesToDurationsWithTracks().get(trackNumber).get(listOfKeys[index]).getCount();
            index++;
        }
        if (index != 0) {
            index--;//After loop number is number be decrimented to be at right index
        }
        if (trackNumber == 5) {
            returnString = Double.toString(noteToDurations.get(listOfKeys[index]).getDuration() / 2);
        } else {
            returnString = Double.toString(noteToDurations.get(listOfKeys[index]).getDuration());
        }
        return returnString;
    }

}
