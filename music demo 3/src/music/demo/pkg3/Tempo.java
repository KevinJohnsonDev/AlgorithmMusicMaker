/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.Comparator;

/**
TODO SORT TEMPOS IN MUSICSTRINGCONVERTER.JAVA based on BPM.
 */
public class Tempo implements Comparable<Tempo>  {
    
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
        hash = 29 * hash + this.time * this.bpm;
        return hash;
    }

    @Override
    public int compareTo(Tempo tempo) {
        int comparedSize = tempo.getTime();
		if (this.time > comparedSize) {
			return 1;
		} else if (this.time == comparedSize) {
			return 0;
		} else {
			return -1;
		}
    }
    

}
    

