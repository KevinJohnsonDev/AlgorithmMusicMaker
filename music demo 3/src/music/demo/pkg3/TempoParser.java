/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class TempoParser {

    private LinkedList<Tempo> listOfTempos;
    private LinkedList<DurationList2> durationList;

    public TempoParser(String musicString) {
        listOfTempos = new LinkedList<>();
        durationList = new LinkedList<>();
        setListOfTempos(musicString);
        setListOfDurations();
    }

    private void setListOfTempos(String musicString) {
        HashMap<String, String> duplicateChecker = new HashMap();
        Pattern pattern = Pattern.compile("@(\\d+)\\s*T(\\d+)");
        Matcher m = pattern.matcher(musicString);
        while (m.find()) {
            if (!duplicateChecker.containsKey(musicString.substring(m.start(1), m.end(1)))) {
                listOfTempos.add(new Tempo(Integer.parseInt(musicString.substring(m.start(1), m.end(1))),
                        Integer.parseInt(musicString.substring(m.start(2), m.end(2)))));
                duplicateChecker.put(musicString.substring(m.start(1), m.end(1)), musicString.substring(m.start(2), m.end(2)));
            }
        }
       Collections.sort(listOfTempos);
       listOfTempos.stream().forEach((t) -> {
           System.out.println("TIME" + t.getTime() + "; BPM" + t.getBpm());
        });
    }

    private void setListOfDurations() {
        listOfTempos.stream().map((t) -> new DurationList2(t.getBpm())).forEach((DL) -> {
            durationList.add(DL);
        });
    }

    public LinkedList<Tempo> getListOfTempos() {
        return listOfTempos;
    }

    public LinkedList<DurationList2> getDurationList() {
        return durationList;
    }



}
