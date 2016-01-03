/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */
public class MusicAnalysisContainer {

    private static metaInformationParser MIP;
    private static ArrayList<TreeMap<String, Double>> MidiToNotePercentagesTotals;// Percentages of Notes in each track
    private static TreeMap<String, Double> MidiToNotePercentages; // Raw Total percentages not taking tracks into account
    private static HashMap<String, String> MidiToNotePatterns; //used for pattern creation
    private static ArrayList<ArrayList<String>> instrumentNames;
    private static TreeMap<String, NoteDurationCount> NoteToDurationsWithoutTracks; //Duration Probability Given Note without tracks in mind
    private static ArrayList<TreeMap<String, NoteDurationCount>> NotesToDurationsWithTracks; //key is NOTE+DURATION Including Decimal probability 
    private static DurationList DL;
    private static String MusicString;
    private static String listOfTempos;
    public MusicAnalysisContainer() {

    }

    //checks to see if all objects have values

    public boolean IsValidated() {

        boolean allset = true;
        for (Field f : this.getClass().getFields()) {
            f.setAccessible(true);
            try {
                if (f.get(this) == null) {
                    allset = false;
                    break;
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(MusicAnalysisContainer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return allset;
    }

    public static metaInformationParser getMIP() {
        return MIP;
    }

    public static void setMIP(metaInformationParser MIP) {
        MusicAnalysisContainer.MIP = MIP;
    }

    public static  ArrayList<TreeMap<String, Double>> getMidiToNotePercentagesTotals() {
        return MidiToNotePercentagesTotals;
    }

    public static void setMidiToNotePercentagesTotals( ArrayList<TreeMap<String, Double>> MidiToNotePercentagesTotals) {
        MusicAnalysisContainer.MidiToNotePercentagesTotals = MidiToNotePercentagesTotals;
    }

    public static TreeMap<String, Double> getMidiToNotePercentages() {
        return MidiToNotePercentages;
    }

    public static void setMidiToNotePercentages(TreeMap<String, Double> MidiToNotePercentages) {
        MusicAnalysisContainer.MidiToNotePercentages = MidiToNotePercentages;
    }

    public static HashMap<String, String> getMidiToNotePatterns() {
        return MidiToNotePatterns;
    }

    public static void setMidiToNotePatterns(HashMap<String, String> MidiToNotePatterns) {
        MusicAnalysisContainer.MidiToNotePatterns = MidiToNotePatterns;
    }

    public static TreeMap<String, NoteDurationCount> getNoteToDurationsWithoutTracks() {
        return NoteToDurationsWithoutTracks;
    }

    public static void setNoteToDurationsWithoutTracks(TreeMap<String, NoteDurationCount> NoteToDurationsWithoutTracks) {
        MusicAnalysisContainer.NoteToDurationsWithoutTracks = NoteToDurationsWithoutTracks;
    }

    public static ArrayList<TreeMap<String, NoteDurationCount>> getNotesToDurationsWithTracks() {
        return NotesToDurationsWithTracks;
    }

    public static void setNotesToDurationsWithTracks(ArrayList<TreeMap<String, NoteDurationCount>> NotesToDurationsWithTracks) {
        MusicAnalysisContainer.NotesToDurationsWithTracks = NotesToDurationsWithTracks;
    }

    public static DurationList getDL() {
        return DL;
    }

    public static void setDL(DurationList DL) {
        MusicAnalysisContainer.DL = DL;
    }

    public static String getMusicString() {
        return MusicString;
    }

    public static void setMusicString(String MusicString) {
        MusicAnalysisContainer.MusicString = MusicString;
    }

    public static String getListOfTempos() {
        return listOfTempos;
    }

    public static void setListOfTempos(String listOfTempos) {
        MusicAnalysisContainer.listOfTempos = listOfTempos;
    }

    public static ArrayList<ArrayList<String>> getInstrumentNames() {
        return instrumentNames;
    }

    public static void setInstrumentNames(ArrayList<ArrayList<String>> instrumentNames) {
        MusicAnalysisContainer.instrumentNames = instrumentNames;
    }
    
    
    
    
    
}

