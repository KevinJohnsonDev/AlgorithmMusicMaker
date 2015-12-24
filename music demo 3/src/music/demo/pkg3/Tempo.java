/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

/**
 *
 * @author Kevin
 */
public class Tempo {
    
    private int time;
    private int bpm;
    public Tempo(int time, int bpm)
    {
        this.time = time;
        this.bpm = bpm;
    }

    public int getTime() {
        return time;
    }

    public int getBpm() {
        return bpm;
    }
        @Override
    public boolean equals(Object o){
        return o instanceof Tempo && ((Tempo)o).getBpm() == this.getBpm();
                
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.bpm;
        return hash;
    }
    
}
