/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

/**
 * This class contains all note durations in milliseconds 
 * @author Kevin
 */
public class MidiNoteDurations {
      private final double Whole_note;
    private final double Half_note;
    private final double Quarternote;
    private final double Eighth_note;
    private final double Sixteenth_note;
    private final double Dotted_Half_note;
    private final double Dotted_quarter_note;
    private final double Eighth_note_triplet;
    private final double Dotted_eighth_note;
    private final double Dotted_sixteenth_note;
    private final double Triplet_quarter_note;
    private final double Triplet_eighth_note;
  //  private final double[] Note_To_Durations;
    public MidiNoteDurations(int BPM)
    {
        this.Whole_note = 240 / BPM;
        this.Half_note = 120 / BPM;
        this.Quarternote = 60 / BPM;
        this.Eighth_note = 30 / BPM;
        this.Sixteenth_note = 15 / BPM;
        this.Dotted_Half_note = 180 / BPM;
        this.Dotted_quarter_note = 90 / BPM;
        this.Eighth_note_triplet = 45 / BPM;
        this.Dotted_eighth_note = 22.5 / BPM;
        this.Dotted_sixteenth_note = 40 / BPM;
        this.Triplet_quarter_note = 20 / BPM;
        this.Triplet_eighth_note = 10 / BPM;
        
    }
}
