/*
 Gets all instruments from JFUGUE music string 
 */
package music.demo.pkg3;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class InstrumentParser {

    private ArrayList<ArrayList<String>> instrumentNames;

    public InstrumentParser(String MusicString) {
        instrumentNames = new ArrayList<>(17); // up to 16 tracks
        for (int i = 0; i < 17; i++) //allocate space for each track
        {
            instrumentNames.add(new ArrayList<>(17)); //17 instruments per track
        }
        populateInstrumentNames(MusicString);
        //removes nulls from instruments
        for (int i = 0; i < 17; i++) //allocate space for each track
        {
            instrumentNames.get(i).trimToSize(); //17 instruments per track
        }
                for(int i =0; i< instrumentNames.size();i++ )
        {
            ArrayList <String> sample = instrumentNames.get(i);
            for(String s : sample)
            {
            if (s != null) {
                System.out.println(i+" "+ s);
                
            }
            }
        }
        

    }
    public void populateInstrumentNames(String MusicString)
    {
                Pattern pattern = Pattern.compile("V(\\d+)\\s+I\\[(.*?)\\]");
        Matcher matcher = pattern.matcher(MusicString);
        while(matcher.find())
        {
           instrumentNames.get(Integer.parseInt(matcher.group(1))).add(matcher.group(2));
        }
        
    }

    public ArrayList<ArrayList<String>> getInstrumentNames() {
        return instrumentNames;
    }
    
}
