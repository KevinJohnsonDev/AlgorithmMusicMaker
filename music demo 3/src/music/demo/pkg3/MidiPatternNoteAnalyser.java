package music.demo.pkg3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfugue.Pattern;
import org.jfugue.Player;

/**
 *
 * @author Kevin
 * @param <T>
 */
public class MidiPatternNoteAnalyser {

    private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    
    private String musicString;
    private HashMap<String, ArrayList<NoteToProb>> balance;
    private HashMap<String, NoteToProb> MidiBalance;
    private ArrayList<NoteToProb> list;
    private HashMap<String, Double> MidiToNotePercentages;
    private NoteToProb FromFile;

    public MidiPatternNoteAnalyser() {
        this.balance = new HashMap<>(); // map to be returned and analyzed 
        this.list = new ArrayList<>(128); //number of notes in midi
        this.musicString = null;
        this.MidiBalance = new HashMap<>();
      //  this.MidiToNotePercentages = new HashMap<String, Double>();
    }

    /**
     *
     * @param fileName
     */
    public MidiPatternNoteAnalyser(String fileName) {
        this.balance = new HashMap<>(); // map to be returned and analyzed 
        this.list = new ArrayList<>(128); //number of notes in midi
        this.musicString = fileName;
        this.MidiBalance = new HashMap<>();
        this.MidiToNotePercentages = new HashMap<>();

    }

    public HashMap<String, ArrayList<NoteToProb>> getTable() {
        return this.balance;
    }
//Generates Table From MusicString passed to Constuctor
    public void generateTable()
            throws IOException {
       
     
            try {
                String delimiter = "%";//seperates notes from each other in table

                String content = new Scanner(new File("PRobs.txt")).useDelimiter("\\Z").next();
                if (this.musicString == null) {
                    content = new Scanner(new File("PRobs.txt")).useDelimiter("\\Z").next();
                } else {
                    content = new Scanner(new File((String) this.musicString)).useDelimiter("\\Z").next();
                }
                //why am i using a delimiter here too
                StringTokenizer st = new StringTokenizer(content);
                while (st.hasMoreElements()) {

                    String check = st.nextToken();

                    if (check.contains(delimiter)) {

                        balance.put(check.substring(0, check.indexOf('%')), list);
                        list = new ArrayList<>(128);
                        if (st.hasMoreTokens()) {
                            check = st.nextToken();
                        } else {
                            break;
                        }

                    }
                    FromFile = new NoteToProb(check, Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));

                    list.add(FromFile);
                }
//      test for keys and values proper
//            balance.keySet().stream().map((key) -> {
//                System.out.println("key : " + key);
//                return key;
//            }).forEach((key) -> {
//                System.out.println("value : " + balance.get(key));
//            });

            } catch (IOException e) {
                System.out.println(e);
            }
        

    }

    public void createInitialIndexes() {
        for (String noteList : NOTE_NAMES) {
            this.MidiBalance.put(noteList, new NoteToProb(noteList, 0, 3));
            this.MidiBalance.put(noteList, new NoteToProb(noteList, 0, 3));
        }
    }
//uses musicString to Generates table

    public void generateTableFromMusicString()
            throws IOException {
        createInitialIndexes();
        MusicStringCorrector MFA = new MusicStringCorrector(musicString);
        String Formatter = MFA.getFormattedMusicString();
        int startIndex = 0;
        int noteIndex;
        while (Formatter.indexOf("/", startIndex) != -1) {
            startIndex = Formatter.indexOf("/", startIndex); //every / 
            //    System.out.println("startIndex char: " + Formatter.substring(startIndex-2,startIndex+1)+ " " +startIndex);
            noteIndex = startIndex;

            if (Formatter.charAt(startIndex - 2) == '#' || Formatter.charAt(startIndex - 2) == 'b') //accounting for sharp
            {
                noteIndex -= 3; //Accounting for Number and / EX :  B5/0.025387263339070567a114
            } else {
                noteIndex -= 2;
            }
            //     System.out.println(noteIndex);
            //      System.out.println(unformatted.substring(noteIndex, startIndex-1));//exclude numbers for now
            FromFile = MidiBalance.get(Formatter.substring(noteIndex, startIndex - 1));
            //  System.out.println(FromFile.getNote());
            FromFile.setValue(FromFile.getValue() + 1);
            startIndex++;
            //          System.out.println(startIndex);
        }

        double totalNotes = 0.0;
        for (String noteList : NOTE_NAMES) {
            totalNotes += MidiBalance.get(noteList).getValue();
        }
        for (String noteList : NOTE_NAMES) {
            this.MidiToNotePercentages.put(noteList, (MidiBalance.get(noteList).getValue() / totalNotes) * 100);
        }
        System.out.println("Analysis Success");
    }

    public void ModifyTable(HashMap<String, ArrayList<NoteToProb>> h, String[] keysAsStringArray) //Assumes: Table has more then one entry, probabilities total to 100 
    {
        NoteToProb ntp;
        int picker = -4;
        int counter = 0;
        int checker = -3;
        int modificationVar;
        Integer keysetSize = h.keySet().size();
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            while (picker == checker) {
                picker = random.nextInt(keysetSize);
            }
            checker = picker; //used for second loop for different index 

            ArrayList<NoteToProb> ArrayListFromRandomKey = h.get(keysAsStringArray[picker]); // Pull ArrayList
            picker = random.nextInt(ArrayListFromRandomKey.size() - 1);
            ntp = ArrayListFromRandomKey.get(picker);
            modificationVar = random.nextInt(ntp.getValue());
            ntp.setValue(ntp.getValue() - modificationVar);
            while (modificationVar != 0) {
                picker = random.nextInt(ArrayListFromRandomKey.size() - 1);
                ntp = ArrayListFromRandomKey.get(picker);
                while (ntp.getValue() + modificationVar >= 50 && counter <= 3) {
                    picker = random.nextInt(ArrayListFromRandomKey.size() - 1);
                    ntp = ArrayListFromRandomKey.get(picker);
                    counter++;
                }
                counter = 0;
                if (ntp.getValue() + modificationVar >= 50) // If it is STILL above 50 for one of them CUT IT DOWN
                {
                    int cutter = random.nextInt(ntp.getValue());
                    modificationVar += cutter;
                    ntp.setValue(ntp.getValue() - modificationVar);
                    picker = random.nextInt(ArrayListFromRandomKey.size() - 1);
                    if (ArrayListFromRandomKey.get(picker).equals(ntp) && picker > 0) // get another one in the extremely unlikely case
                    {
                        ntp = ArrayListFromRandomKey.get(picker);
                    } else {
                        ntp = ArrayListFromRandomKey.get(picker);
                    }
                    if (modificationVar > 30) {
                        ntp.setValue(ntp.getValue() - modificationVar);
                        modificationVar = modificationVar / 2;
                    } else {
                        ntp.setValue(ntp.getValue() - modificationVar);
                        modificationVar = 0;
                    }
                }
            }
            picker = checker;
        }
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

    public HashMap<String, NoteToProb> getMidiBalance() {
        return MidiBalance;
    }

    public void setMidiBalance(HashMap<String, NoteToProb> MidiBalance) {
        this.MidiBalance = MidiBalance;
    }

    public ArrayList<NoteToProb> getList() {
        return list;
    }

    public void setList(ArrayList<NoteToProb> list) {
        this.list = list;
    }

    public HashMap<String, Double> getMidiToNotePercentages() {
        return MidiToNotePercentages;
    }

    public void setMidiToNotePercentages(HashMap<String, Double> MidiToNotePercentages) {
        this.MidiToNotePercentages = MidiToNotePercentages;
    }

    public NoteToProb getFromFile() {
        return FromFile;
    }

    public void setFromFile(NoteToProb FromFile) {
        this.FromFile = FromFile;
    }

}
