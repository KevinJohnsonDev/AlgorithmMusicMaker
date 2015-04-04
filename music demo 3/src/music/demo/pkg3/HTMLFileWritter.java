/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package music.demo.pkg3;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Kevin
 */
public class HTMLFileWritter {

    public HTMLFileWritter() {
        generateUpToBodyHtmlSkeleton();

    }

    private void generateUpToBodyHtmlSkeleton() {
        PrintWriter writer = null;
         String[][] totals;
        try {
            writer = new PrintWriter("results.html", "UTF-8");
            writer.write("<!DOCTYPE html>");
            writer.println();
            writer.write("<html>");
            writer.println();
            writer.write("<head>");
            writer.println();
            writer.write("<meta charset=\"utf-8\" />");
            writer.println();
            writer.write("<title>Results of Analysis</title>");
            writer.println();
            writer.write("<script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-latest.min.js\"></script>");
            writer.println();
            writer.write("<!--[if lt IE 9]>");
            writer.println();
            writer.write("  <script src=\"http://html5shiv.googlecode.com/svn/trunk/html5.js\"></script>");
            writer.println();

            writer.write("  <![endif]-->");
            writer.println();
            writer.write("<style type=\"text/css\">");
            writer.println();
            writer.write(" header, section, footer, aside, nav, article, figure, audio, video, canvas  { display:block; }");
            writer.println();
            writer.write(" </style>");
            writer.println();
            writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\">");
            writer.println();
            writer.println(" </head>");
            writer.write("<body>");
            writer.println();
            writer.write("<div id=\"wrapper\">");
            writer.println();
            writer.write("<h1>");
            writer.println();
            writer.write("Analysis Results");
            writer.println();
            writer.write(" </h1>");
            writer.println();
            writer.write("<table class=\"resultsProb\">");
            writer.write("<tr><th> Note </th> <th> Totals</th>");
            int realTrackNumber = MusicAnalysisContainer.getMIP().getNumberOfTracks() + 1; //if only 1 
            for (int i = 0; i < MusicAnalysisContainer.getMIP().getNumberOfTracks(); i++) {
                if (checkIfTrackNotEmpty(i)) {
                    writer.write("<th> Track" + (i + 1) + "</th> ");
                } else {
                    realTrackNumber--;
                }
            }
            writer.write("</tr>");
            System.out.println(realTrackNumber);
            if(realTrackNumber == 3)
               
            {
                 totals = new String[MusicAnalysisContainer.getMidiToNotePercentages().keySet().size()][MusicAnalysisContainer.getMIP().getNumberOfTracks() + 7]; 
            }
            else
            {
           totals = new String[MusicAnalysisContainer.getMidiToNotePercentages().keySet().size()][MusicAnalysisContainer.getMIP().getNumberOfTracks() + 2]; //String Value in array first then tracks so for 1  
            }
            int counter = 0;
            int secondIndex = 2;
            for (String s : MusicAnalysisContainer.getMidiToNotePercentages().keySet()) //write to table structure NOTE | CHANCE
            {
                double d = Math.round(MusicAnalysisContainer.getMidiToNotePercentages().get(s) * 100);
                d = d / 100;

                totals[counter][0] = s;
                totals[counter][1] = Double.toString(d);
                counter++;

                //  writer.write("<tr><td>" + s + "</td> <td> " + d + "</td></tr>");
                //   writer.println();
            }

            for (int numnotes = 0; numnotes < MusicAnalysisContainer.getMidiToNotePercentages().keySet().size(); numnotes++) {
                for (int voice = 0; voice < MusicAnalysisContainer.getMidiToNotePercentagesTotals().size(); voice++) {

                    if (MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(voice).containsKey(totals[numnotes][0])) {
                        double d = Math.round(MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(voice).get(totals[numnotes][0]) * 100);
                        d = d / 100;
                        totals[numnotes][secondIndex] = Double.toString(d);
                    } else {
                        totals[numnotes][secondIndex] = "-";
                    }

                    secondIndex++;
                }
                secondIndex = 2;

            }

            counter = 0;
            secondIndex = 2;
            int numNotes = MusicAnalysisContainer.getMidiToNotePercentages().keySet().size();

            for (int i = 0; i < numNotes; i++) {

                writer.write("<tr><td>" + totals[counter][0] + "</td> <td> " + totals[counter][1] + "</td> ");
                for (int j = 0; j < (MusicAnalysisContainer.getMIP().getNumberOfTracks()); j++) {
                    if (!checkIfTrackNotEmpty(j)) {
                        secondIndex++;
                        continue;
                    }
                    if (totals[counter][secondIndex] != null) {
                        writer.write("<td>" + totals[counter][secondIndex] + "</td> ");
                    } else {

                        writer.write("<td>" + "-" + "</td> ");
                    }

                    secondIndex++;
                }
                counter++;
                secondIndex = 2;
                writer.write("</tr>");

            }
            writer.println("</table>");
            writer.println("<br/>");
            writer.write("<table class=\"resultsProb\">");

           DurationList DL = MusicAnalysisContainer.getDL();
          //  double[] orderedDurations = DL.getNoteToDurations();
     //       Arrays.sort(orderedDurations);
            writer.println("<tr>");
            writer.write("<th>" + "Note" + "</th>");
        //    for (int i = 0; i < orderedDurations.length; i++)
            for(String s : DL.getDuration_To_Abbreviation().keySet())
            {
                writer.write("<th>" + s + "</th>");
            }
            writer.println("</tr>");
            String currentNote = null;
            for (String s : MusicAnalysisContainer.getNoteToDurationsWithoutTracks().keySet()) {
                if (currentNote == null) {
                    currentNote = s.substring(0, s.indexOf("/"));
                    writer.println("<tr>");
                    writer.write("<td>" + currentNote + "</td>");
                    writeNoteToDuration(currentNote, writer);
                    writer.println("</tr>");
                } else if (currentNote.equals(s.substring(0, s.indexOf("/")))) {

                } else {
                    currentNote = s.substring(0, s.indexOf("/"));
                    writer.println("<tr>");
                    writer.write("<td>" + currentNote + "</td>");
                    writeNoteToDuration(currentNote, writer);
                    writer.println("</tr>");
                }
            }

            writer.println("</table>");
            writer.println("<br/>");
//           
               writer.println("</div>");

            writer.println("</body>");
               writer.println("</Html>");
        } catch (Exception e) {
            System.out.println(e);
            //catch any exceptions here
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
            }
        }

    }


    public void writeNoteToDuration(String Note, PrintWriter writer) {
        DurationList DL = MusicAnalysisContainer.getDL();
        double[] orderedDurations = DL.getNoteToDurations();
        Arrays.sort(orderedDurations);
        TreeMap t = MusicAnalysisContainer.getNoteToDurationsWithoutTracks();
        SortedMap<String, NoteDurationCount> onlyCertainNoteList = getByPreffix(t, Note);
        int orderedDurationCounter = 0;
        int previousCounter = 0;
        for (String s : onlyCertainNoteList.keySet()) {

            boolean doesNotContain = false;
            while (Math.round(Double.parseDouble(s.substring(s.indexOf("/") + 1, s.length() - 1)) * 10000) / 10000.0 != Math.round(orderedDurations[orderedDurationCounter] * 10000) / 10000.0) {
                if (orderedDurationCounter == orderedDurations.length - 1) {
                    System.out.println("does Not Exist:" + orderedDurationCounter);
                    doesNotContain = true;
                    break;
                }

                orderedDurationCounter++;

            }

            while (previousCounter < orderedDurationCounter) {

                writer.println("<td></td>");
                previousCounter++;

            }
            if (doesNotContain) {
                writer.println("<td></td>");
                previousCounter++;
            } else {
                writer.println("<td>" + (double) Math.round(onlyCertainNoteList.get(s).getCount() * 100) / 100 + "</td>");
                 previousCounter++;
            }

        }
        for (; previousCounter < orderedDurations.length; previousCounter++) {
            writer.println("<td></td>");
       
        }

    }

    private static SortedMap<String, Object> getByPreffix(
            NavigableMap<String, Object> myMap,
            String preffix) {
        return myMap.subMap(preffix, preffix + Character.MAX_VALUE);
    }

//returns true if not empty
    private boolean checkIfTrackNotEmpty(int trackNum) {
        boolean isEmpty;
        Pattern pattern = Pattern.compile("V" + trackNum + "\\s+([A-Ga]#?b?\\d+)");
        Matcher matcher = pattern.matcher(MusicAnalysisContainer.getMusicString());

        isEmpty = matcher.find();

        return isEmpty;
    }

}
//<!DOCTYPE html>
//<html>
//<head>
//  <meta charset="utf-8" />
//  <title>HTML5 basic skeleton</title>
//  <script type="text/javascript" src="http://code.jquery.com/jquery-latest.min.js"></script>
//  <!--[if lt IE 9]>
//    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
//  <![endif]-->
//  <style type="text/css">
//    header, section, footer, aside, nav, article, figure, audio, video, canvas  { display:block; }
//  </style>
//</head>
//<body>
//  <div id="wrapper">
//    <header>
//      Header
//    </header>
//    <nav>
//      Nav
//    </nav>
//    <section id="content">
//      <article>
//        Article
//      </article>
//    </section>
//    <aside>
//      Sidebar
//    </aside>
//    <footer>
//      Footer
//    </footer>
//  </div>   
//</body>
//</html>
