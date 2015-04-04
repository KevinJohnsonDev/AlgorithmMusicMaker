package music.demo.pkg3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



/**
 *
 * @author Kevin
 */
public class NoteToProb {

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNumChordInProgression() {
        return numChordInProgression;
    }

    public void setNumChordInProgression(int numChordInProgression) {
        this.numChordInProgression = numChordInProgression;
    }

    public int getNumberOfInstances() {
        return numberOfInstances;
    }

    public void setNumberOfInstances(int numberOfInstances) {
        this.numberOfInstances = numberOfInstances;
    }
    private String note;
    int value;
    private int numChordInProgression;
    private int numberOfInstances;
    
    public NoteToProb(String s, int i,int j)
            {
              note = s;
              value = i;
              numChordInProgression = j;
            }
    public String toString()
    {
        return " " + note + " " + value + " "+ numChordInProgression;
    }
}
