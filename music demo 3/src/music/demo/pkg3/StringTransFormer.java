/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class StringTransFormer {
    private String ConvertedMusicString;
    public StringTransFormer(String musicString)
    {
      DurationList DL = MusicAnalysisContainer.getDL();
        StringBuilder sb = new StringBuilder(musicString);
        for(String s:DL.getDuration_To_Value().keySet())
        {
            
            int index =0;
            String searchStr = Double.toString(DL.getDuration_To_Value().get(s));

            while(sb.indexOf(searchStr,index) != -1)
            {
                int start =sb.indexOf(searchStr,index)-1;//for divisible sign ex : c4/0.25
                int end = start + searchStr.length()+1; //2 because of / and excluse end
                sb.replace(start, end, DL.getDuration_To_Abbreviation().get(s));
                index = start+DL.getDuration_To_Abbreviation().get(s).length();
            }
          
        }
        String s = sb.toString();
           Pattern pattern = Pattern.compile("\\d+(\\.\\^d+)[^\\s]+");
    Matcher matcher = pattern.matcher(s);
    int count =0;
    while (matcher.find() && count <10) {
        String subStr = "";
        double d = Double.parseDouble(s.substring(matcher.start(),matcher.end()));
         System.out.println(matcher.start()+ " " + matcher.end());
        System.out.println(s.substring(matcher.start(),matcher.end()));
        while(d>DL.getDoubleWholeNote())
        {
            subStr += "ww";
            d = d-DL.getDoubleWholeNote();
        }
        
       s= s.replace(s.substring(matcher.start(),matcher.end()), subStr);
       count++;
    }
        ConvertedMusicString = sb.toString();
    }

    public String getConvertedMusicString() {
        return ConvertedMusicString;
    }
    
}
