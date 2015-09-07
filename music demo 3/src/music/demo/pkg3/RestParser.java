/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class RestParser {
    private HashMap <String,Double> restCountsTotals;
    private ArrayList<HashMap <String,Double>> restCountByTrack;
    
    public RestParser(String MusicString)
    {
        restCountsTotals = new HashMap<>();
        restCountByTrack = new ArrayList<>();
    }
    private void CalculateRestsTotals(String MusicString)
    {
        int noteStart=0;
        double noteEnd;
        int nextNoteStart;
        double nextNoteEnd;
        double timeBetweenNotePlayedAndNextNoteInMilliseconds;
        int matchTracker =0;      
              
       Pattern pattern = Pattern.compile("@(\\d+)\\s+V\\d+\\s+[A-Ga]#?b?\\d+/(\\d+.\\d+)");
        Matcher matcher = pattern.matcher(MusicString);
        while (matcher.find())
        {
            if(matchTracker%2==0)
            {
          noteStart = Integer.parseInt(matcher.group(1));
          noteEnd = Double.parseDouble(matcher.group(2));
            }
            else
            {
                nextNoteStart =Integer.parseInt(matcher.group(1));
               nextNoteEnd = Double.parseDouble(matcher.group(2));
               timeBetweenNotePlayedAndNextNoteInMilliseconds = noteStart;
            }
            
          
          
            
        }
    }
}
