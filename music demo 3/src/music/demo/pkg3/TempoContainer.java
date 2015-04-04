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
public class TempoContainer {

    private String listOfTempos;

    public TempoContainer(String musicString) {
        listOfTempos = "";
        Pattern pattern = Pattern.compile("@(\\d+)\\s+T(\\d+)\\s+@\\d+\\s+");
        Matcher matcher = pattern.matcher(musicString);
        StringBuilder s = new StringBuilder();
        s.append(" ");

        while (matcher.find()) {
            if (Integer.parseInt(matcher.group(2)) > 1000) //to match the absurd
            {
                s.append("@").append(matcher.group(1)).append(" ").append("T").append((int)(Integer.parseInt(matcher.group(2)) / 8)).append(" "); //for the absurd
            } else if (Integer.parseInt(matcher.group(2)) > 500) //to match the absurd
            {
                 s.append("@").append(matcher.group(1)).append(" ").append("T").append((int)(Integer.parseInt(matcher.group(2)) / 4)).append(" "); //for the slightly less absurd
            } else if (Integer.parseInt(matcher.group(2)) > 400) //to match the absurd
            {
                    s.append("@").append(matcher.group(1)).append(" ").append("T").append((int)(Integer.parseInt(matcher.group(2)) / 3)).append(" "); //for the slightly less absurd
            } 
             else if (Integer.parseInt(matcher.group(2)) > 300) //to match the absurd
            {
                  s.append("@").append(matcher.group(1)).append(" ").append("T").append((int)(Integer.parseInt(matcher.group(2)) / 2.5)).append(" "); //for the slightly less absurd
            } 
                          else if (Integer.parseInt(matcher.group(1)) > 200) //to match the absurd
            {
                    s.append("@").append(matcher.group(1)).append(" ").append("T").append((int)(Integer.parseInt(matcher.group(2)) / 2)).append(" "); //for the slightly less absurd
            } 
            else {
               s.append("@").append(matcher.group(1)).append(" ").append("T").append(musicString.substring(matcher.start(), matcher.end()));
            }
        }
        listOfTempos = s.toString();
    }

    public void setListOfTempos(String listOfTempos) {
        this.listOfTempos = listOfTempos;
    }

    public String getListOfTempos() {
        return listOfTempos;
    }

}
