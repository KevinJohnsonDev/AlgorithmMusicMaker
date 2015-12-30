/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.sound.midi.InvalidMidiDataException;
import static jdk.nashorn.internal.objects.NativeArray.map;
import org.jfugue.*;
//V0 @0 T581

/**
 *
 * @author Kevin
 */
public class MidiFileAnalyzer {

    //BPM = 600000/(PPQ *MS)
    //100/60/1000 = Beats per Millisecond 1/xms = (100/60/1000)/1ms
    //The formula is 60000 / (BPM * PPQ(resolution)) for milliseconds of track
    // BPM
    //if resolution is pulses per quarter and we know how many miliseconds there are 
    //   private static final int NOTE_ON = 0x90; //sequencer items, these never change
    //  private static final int NOTE_OFF = 0x80;
    private String musicString;//The MusicString that will be corrected by the MusicStringCorrector Object
    private String fileName;


    /**
     *
     * @param fileName
     * @throws InvalidMidiDataException
     * @throws IOException
     */
    public MidiFileAnalyzer(String fileName) throws InvalidMidiDataException, IOException {
        this.fileName = fileName;
        //   this.MidiToNotePercentages = this.analyzeTrack();
        analyzeTrack();

    }

//Takes MusicString From FileChooser and passes it to NoteProbability
    private void analyzeTrack() throws InvalidMidiDataException, IOException {
        //First we correct the String of Any errors
        MusicStringCorrector MFA = null;
        MusicStringConverter MFA2 = null;
        try {
            Player player = new Player();
            Pattern p = player.loadMidi(new File(fileName));

            MFA = new MusicStringCorrector(p.toString());
            musicString = MFA.getFormattedMusicString();
            TempoParser TP = new TempoParser(p.toString());
            MFA2 = new MusicStringConverter(musicString,TP.getListOfTempos(),TP.getDurationList());     
            System.out.println("KEVIN JOHNSON \n");


             MusicAnalysisContainer.setMIP(new metaInformationParser(fileName));
            

        } catch (IOException | InvalidMidiDataException e) {
            System.out.println(e);
        }
        //Get meta information such as tempo and instruments
        InstrumentParser IP = new InstrumentParser(musicString);
      //  TempoContainer tc = new TempoContainer(musicString);
        //Second we scan the probability
        ListOfNotes LOM = new ListOfNotes(musicString);
        MidiNoteProbabilityAnalyzer Np = new MidiNoteProbabilityAnalyzer(this.musicString, LOM);
       
        Np.generateTableFromMusicString();
  
        //Third we get the percentages per track and percentage per voice of each note
        MidiNoteDurationAnalyzer MDA = new MidiNoteDurationAnalyzer(musicString, LOM);
        //Set up Container
        MusicAnalysisContainer.setInstrumentNames(IP.getInstrumentNames());
   //     MusicAnalysisContainer.setListOfTempos(tc.getListOfTempos());
        MusicAnalysisContainer.setMidiToNotePercentages(Np.getMidiToNotePercentages());
        MusicAnalysisContainer.setMidiToNotePercentagesTotals(Np.getMidiToNotePercentagesTotals());

        MusicAnalysisContainer.setNoteToDurationsWithoutTracks(MDA.getNoteToDurationsWithoutTracks());
        MusicAnalysisContainer.setNotesToDurationsWithTracks(MDA.getNotesToDurationsWithTracks());
       
        MusicAnalysisContainer.setMusicString(musicString);
    }

//gets and sets

    


    public void setFileAdjustAllProperties(String s) throws InvalidMidiDataException, IOException {
        this.fileName = s;
        //deallocate for GC
        System.out.println("great success Opening File");
        System.out.println("Setting Done Beginning Analysis");
        analyzeTrack();
    }

    public String getMusicString() {
        return musicString;
    }

    public void setMusicString(String musicString) {
        this.musicString = musicString;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
