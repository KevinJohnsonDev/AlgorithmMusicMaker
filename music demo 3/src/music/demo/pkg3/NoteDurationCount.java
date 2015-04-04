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
public class NoteDurationCount {
    private String note;
    private double duration;
    private double count;
   
    public NoteDurationCount(String note, double duration)
    {
        this.note = note;
        this.duration = duration;
        this.count = 0.0;
        
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }
    @Override
    public String toString()
    {
        return Double.toString(count);
    }
    
}
