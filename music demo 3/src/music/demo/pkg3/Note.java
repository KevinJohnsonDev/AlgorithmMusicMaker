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
public class Note {

    private int count = 0;
    private String note;
    private String duration;
    private double totalProb;

    public Note(String note, String duration) {
        count++;
        this.duration = duration;
        this.note = note;

    }

    private enum Duration {

        ww, w, h, q, i, s, t, x, o
    };

    public void settotalProb(double total) {
        this.totalProb = total;
    }

    public int getCount() {
        return count;
    }

    public String getNote() {
        return note;
    }

    public String getDuration() {
        return duration;
    }

    public double getTotalProb() {
        return totalProb;
    }

}
