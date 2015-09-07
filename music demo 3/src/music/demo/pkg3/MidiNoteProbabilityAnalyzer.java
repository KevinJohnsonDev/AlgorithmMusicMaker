package music.demo.pkg3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//add master for initial indexs 
//Master should be an arrayList and not a hashtable
//list.add(myObject);   
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class MidiNoteProbabilityAnalyzer {

    private String musicString;
    private HashMap<String, ArrayList<NoteToProb>> balance;
    //   private HashMap<String, HashMap<String, NoteToProb>> NoteProbabilityTableMaster;
    private TreeMap<String, NoteToProb> NoteProbabilityTable; //each individual table per voice
    private ArrayList<NoteToProb> list;
    private TreeMap<String, Double> MidiToNotePercentages;
    private ArrayList< TreeMap<String, Double>> MidiToNotePercentagesTotals;
    private NoteToProb FromFile;

    private ArrayList<TreeMap<String, NoteToProb>> NoteProbabilityTableMaster; //List of Voices connecting to their hashtable

    /**
     *
     * @param fileName
     * @param LOM
     */
    public MidiNoteProbabilityAnalyzer(String fileName, ListOfNotes LOM) {
        this.balance = new HashMap<>(); // map to be returned and analyzed 
        //   this.NoteProbabilityTableMaster = new HashMap<>();
        this.list = new ArrayList<>(128); //number of notes in midi
        this.musicString = fileName;
        this.NoteProbabilityTable = new TreeMap<>();
        this.MidiToNotePercentages = new TreeMap<>();
        this.MidiToNotePercentagesTotals = new ArrayList<>();

        this.NoteProbabilityTableMaster = LOM.getListOfNotesWithVoices();
    }

    public HashMap<String, ArrayList<NoteToProb>> getTable() {
        return this.balance;
    }

    public void putCountOfInstancesOfNotesInTable(String musicString) {
        int voiceNum;
        Pattern pattern = Pattern.compile("V(\\d+)\\s+([A-G]#?b?d?\\d+)");
        Matcher matcher = pattern.matcher(musicString);
        while (matcher.find()) {
            voiceNum = Integer.parseInt(matcher.group(1));
            NoteProbabilityTable = this.NoteProbabilityTableMaster.get(voiceNum);
            String note = (matcher.group(2));
            if (NoteProbabilityTable.containsKey(note)) {
                FromFile = NoteProbabilityTable.get(note);
                FromFile.setValue(FromFile.getValue() + 1);
            } else {
                System.out.println("did not have note in table");
            }
        }
    }

//uses musicString to Generates table
    public void generateTableFromMusicString()
            throws IOException {

        // System.out.println(Formatter);
        //Get the number Of Tracks and make voice search array
//        NoteProbabilityTableMaster = new ArrayList<>(NumberOfTracks);
//        for (int i = 0; i < NumberOfTracks; i++) {
//            NoteProbabilityTable = new HashMap<>();
//            NoteProbabilityTableMaster.add(NoteProbabilityTable);
//           
//        }
        // End Get Number of tracks and create blank hashtables
        //Create initial indexes in hashtable for notes
        // createInitialIndexes(musicString);
        //End Create
        //put counts
        putCountOfInstancesOfNotesInTable(musicString);
        //end counts

        //BEGIN GET TOTALS FOR EACH VOICE
        setTotalsForNoteProbability();
        //END GET TOTALS
        //For EAch Voice
        setTotalsForNoteProbabilityTracks();

        //   END GET TOTALS FOR EACH VOICE DIE IN A FIRE HOLY SHIT SORTING THIS was  A BITCH
        System.out.println("Analysis Success");
    }


//Without Tracks

    private void setTotalsForNoteProbability() {
        double totalNotes = 0.0;
        //GET TOTALS

        for (TreeMap<String, NoteToProb> voiceList : NoteProbabilityTableMaster) //takes in order of voices
        {

            NoteProbabilityTable = voiceList;
            for (String noteList : NoteProbabilityTable.keySet()) {
                totalNotes += NoteProbabilityTable.get(noteList).getValue();
            }

        }
        for (TreeMap<String, NoteToProb> voiceList : NoteProbabilityTableMaster) {
            NoteProbabilityTable = voiceList;
            for (String noteList : NoteProbabilityTable.keySet()) {
                if (this.MidiToNotePercentages.containsKey(noteList)) {
                    // System.out.println("Duplicate key");
                    double temp = this.MidiToNotePercentages.get(noteList) * 1.0;
                    temp += (NoteProbabilityTable.get(noteList).getValue() / totalNotes * 100.0);
                    this.MidiToNotePercentages.put(noteList, temp);
                    // MidiToNotePercentages.get(noteList).temp += ((NoteProbabilityTable.get(noteList).getValue() / totalNotes) * 100);
                    // this.MidiToNotePercentages.put(noteList, temp);
                    //   this.MidiToNotePercentages.put(noteList, ((NoteProbabilityTable.get(noteList).getValue() / totalNotes) * 100));
                } else {
                    this.MidiToNotePercentages.put(noteList, (NoteProbabilityTable.get(noteList).getValue() / totalNotes) * 100.0);
                }
            }
        }

    }

    private void setTotalsForNoteProbabilityTracks() {
        double totalNotes = 0;
        int counter = 0;
        TreeMap <String, Double> tempProbs;
        for (TreeMap<String, NoteToProb> voice : NoteProbabilityTableMaster) {
           
            totalNotes = 0.0;
            for (String noteList : voice.keySet()) {
                totalNotes += voice.get(noteList).getValue();
            }

          tempProbs = new TreeMap<>();
            for (String s :  voice.keySet()) {
                tempProbs.put(s, (voice.get(s).getValue() / totalNotes * 100.0));
            }

            MidiToNotePercentagesTotals.add(counter, tempProbs);
           
            counter++;
        }//
       
    }





    public int getNumberOfTracks() {
        return NoteProbabilityTableMaster.size();
    }

    private File getFile(String s) {
        return new File(s);
    }

    private File getFile(File s) {
        return s;
    }

    public String getFileName() {
        return musicString;
    }

    public void setFileName(String fileName) {
        this.musicString = fileName;
    }

    public HashMap<String, ArrayList<NoteToProb>> getBalance() {
        return balance;
    }

    public void setBalance(HashMap<String, ArrayList<NoteToProb>> balance) {
        this.balance = balance;
    }

    public TreeMap<String, NoteToProb> getNoteProbabilityTable() {
        return NoteProbabilityTable;
    }

    public void setNoteProbabilityTable(TreeMap<String, NoteToProb> MidiBalance) {
        this.NoteProbabilityTable = MidiBalance;
    }

    public ArrayList<NoteToProb> getList() {
        return list;
    }

    public void setList(ArrayList<NoteToProb> list) {
        this.list = list;
    }

    public TreeMap<String, Double> getMidiToNotePercentages() {
        return MidiToNotePercentages;
    }

    public void setMidiToNotePercentages(TreeMap<String, Double> MidiToNotePercentages) {
        this.MidiToNotePercentages = MidiToNotePercentages;
    }

    public NoteToProb getFromFile() {
        return FromFile;
    }

    public void setFromFile(NoteToProb FromFile) {
        this.FromFile = FromFile;
    }

    public ArrayList<TreeMap<String, Double>> getMidiToNotePercentagesTotals() {
        return this.MidiToNotePercentagesTotals;
    }

}

//Generates Table From MusicString passed to Constuctor

/* public void createInitialIndexes(String musicString) {
 int startIndex = 0;
 int noteIndex;
 String voice = "";
 int voiceStartIndex = 0;
 int voiceEndIndex = 0;
 int voiceNum;
 while (musicString.indexOf("/", startIndex) != -1) {
 startIndex = musicString.indexOf("/", startIndex);
 noteIndex = startIndex;

 //5 or 6 depending on V9/V10 Get voiceNumber
 if (musicString.charAt(startIndex - 2) == '#' || musicString.charAt(startIndex - 2) == 'b') //accounting for sharp
 {
 noteIndex -= 3; //Accounting for Number and / EX : V11 B#5/0.025387263339070567a114
 } else {
 noteIndex -= 2;
 }
 if ((musicString.charAt(noteIndex - 4)) == 'V') //CASE V_DOUBLE_DIGIT
 {
 //      System.out.println("Choice a");
 voiceStartIndex = noteIndex - 3;
 voiceEndIndex = noteIndex - 1;
 } else if (musicString.charAt(noteIndex - 3) == 'V') //single digit voice
 {
 //  System.out.println("Choice b");
 voiceStartIndex = noteIndex - 2;
 voiceEndIndex = noteIndex - 1;
 }
 voice = musicString.substring(voiceStartIndex, voiceEndIndex);
 // System.out.println(voice);

 voiceNum = Integer.parseInt(musicString.substring(voiceStartIndex, voiceEndIndex));

 //end Get Voice
 String note = musicString.substring(noteIndex, startIndex);
 NoteProbabilityTable = this.NoteProbabilityTableMaster.get(voiceNum);
 //      System.out.println("voiceNum: " + voiceNum);
 NoteProbabilityTable.put(note, new NoteToProb(note, 0, 3));

 startIndex++;
 }

 }
 */
//    private String[] listOfNotes() {
//        ArrayList<String> ListOfNotes = new ArrayList<>();
//        for (int i = 0; i < NoteProbabilityTableMaster.size(); i++) {
//            for (String s : NoteProbabilityTableMaster.get(i).keySet()) {
//
//                if (!ListOfNotes.contains(s)) {
//                    ListOfNotes.add(s);
//                }
//            }
//        }
//        return ListOfNotes.toArray(new String[ListOfNotes.size()]);
//    }
//    private String[] chooseArrayOfNotes(String s) {
//        String[] sharps_Or_Flats;
//        if (s.contains("b") && s.contains("#")) {
//            sharps_Or_Flats = NOTE_NAMES_SHARPS_AND_FLATS;
//
//        } else if (s.contains("b")) {
//            sharps_Or_Flats = NOTE_NAMES_FLATS;
//        } else {
//            sharps_Or_Flats = NOTE_NAMES_SHARPS;
//        }
//        return sharps_Or_Flats;
//
//    }