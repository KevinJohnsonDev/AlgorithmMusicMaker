/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import music.demo.pkg3.SliderDemo;

/**
 *
 * @author Kevin
 */
public class MainPanel {

    public static FileChooser filePicker;
    public static Playbutton playbutton;
    public static StopButton stopbutton;
    public static RepeatButton repeatbutton;
    public static SongButton s1;
    public static SongButton s2;
    public static SongButton s3;

    public static void createAndShowGUI() {
        //Create and set up the window.
        filePicker = new FileChooser();

        stopbutton = new StopButton();
        playbutton = new Playbutton();
        repeatbutton = new RepeatButton();
//        playbutton.setVisible(false);
//        stopbutton.setVisible(false);
//        repeatbutton.setVisible(false);
        s1 = new SongButton();
        s2 = new SongButton();
        s3 = new SongButton();
        JFrame main = new JFrame("Midi Music Mix Alpha ");
        JPanel frame = new JPanel();
        JPanel results = new JPanel(new GridBagLayout());

        GenreChoiceRadioButtons gc = new GenreChoiceRadioButtons();

        SliderDemo animator = new SliderDemo();

        //Add content to the window.
        frame.add(filePicker, BorderLayout.NORTH);
        frame.add(animator, BorderLayout.NORTH);
        frame.add(stopbutton, BorderLayout.CENTER);
        frame.add(playbutton, BorderLayout.CENTER);
        frame.add(repeatbutton, BorderLayout.CENTER);
        frame.add(s1, BorderLayout.CENTER);
        frame.add(s2, BorderLayout.CENTER);
      //  frame.add(s3, BorderLayout.CENTER);
        //add content to results windowe

        frame.setLayout(new FlowLayout());
        frame.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.setVisible(true);
      //  frame.add(gc, BorderLayout.CENTER);
        //Display the window.

        //sample
        String[] columnNames = {"First Name", "Surname", "Country", "Event", "Place", "Time", "World Record"};

        Object[][] data = {{"César Cielo", "Filho", "Brazil", "50m freestyle", 1, "21.30", false}, {"Amaury", "Leveaux", "France", "50m freestyle", 2, "21.45", false}, {"Alain", "Bernard", "France", "50m freestyle", 3, "21.49", false}, {"Alain", "Bernard", "France", "100m freestyle", 1, "47.21", false}, {"Eamon", "Sullivan", "Australia", "100m freestyle", 2, "47.32", false}, {"Jason", "Lezak", "USA", "100m freestyle", 3, "47.67", false}, {"César Cielo", "Filho", "Brazil", "100m freestyle", 3, "47.67", false}, {"Michael", "Phelps", "USA", "200m freestyle", 1, "1:42.96", false}, {"Park", "Tae-Hwan", "South Korea", "200m freestyle", 2, "1:44.85", false}, {"Peter", "Vanderkaay", "USA", "200m freestyle", 3, "1:45.14", false}, {"Park", "Tae-Hwan", "South Korea", "400m freestyle", 1, "3:41.86", false}, {"Zhang", "Lin", "China", "400m freestyle", 2, "3:42.44", false}, {"Larsen", "Jensen", "USA", "400m freestyle", 3, "3:42.78", false}, {"Oussama", "Mellouli", "Tunisia", "1500m freestyle", 1, "14:40.84", false}, {"Grant", "Hackett", "Australia", "1500m freestyle", 2, "14:41.53", false}, {"Ryan", "Cochrane", "Canada", "1500m freestyle", 3, "14:42.69", false}, {"Aaron", "Peirsol", "USA", "100m backstroke", 1, "52.54", true}, {"Matt", "Grevers", "USA", "100m backstroke", 2, "53.11", false}, {"Arkady", "Vyatchanin", "Russia", "100m backstroke", 3, "53.18", false}, {"Hayden", "Stoeckel", "Australia", "100m freestyle", 3, "53.18", false}, {"Ryan", "Lochte", "USA", "200m backstroke", 1, "1:53.94", true}, {"Aaron", "Peirsol", "USA", "200m backstroke", 2, "1:54.33", false}, {"Arkady", "Vyatchanin", "Russia", "200m backstroke", 3, "1:54.93", false}, {"Kosuke", "Kitajima", "Japan", "100m breaststroke", 1, "58.91", true}, {"Alexander", "Dale Oen", "Norway", "100m breaststroke", 2, "59.20", false}, {"Hugues", "Duboscq", "France", "100m breaststroke", 3, "59.37", false}};
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());

        table.setAutoCreateRowSorter(true);
        table.setShowGrid(true);
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.add(table);
        tableScrollPane.setVisible(true);
        tableScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        GridBagConstraints resultConstraints = new GridBagConstraints();
        resultConstraints.weightx = 10.0;
        resultConstraints.fill = GridBagConstraints.HORIZONTAL;
        resultConstraints.insets = new Insets(20, 20, 30, 30);
        results.add(tableScrollPane, resultConstraints);

// results.add(tableScrollPane,  "wrap, span");
        //  results.add(table, BorderLayout.CENTER);
        main.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 0.9;
        main.add(frame, gbc);
        //main.
        //grid
        gbc.weighty = 0.1;
        gbc.gridy = 1;

    //    main.add(table,gbc);
        // main.add(results, gbc);
        //   main.add(tableScrollPane, gbc);
        //main.pack();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main.pack();
        main.setLocationRelativeTo(null);
        main.setVisible(true);
        //        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);

    }

    public void displayResults(String[] resultData, String[] results) {

    }
//    public static void hideButtons()
//    {
//    filePicker;
//   playbutton;
//   stopbutton;
//    repeatbutton;
//    s1;
//    s2;
//     s3;
//    }

    public static void enableButtons() {

    }
}
//  public static void createAndShowGUI() {
//        //Create and set up the window.
//        FileChooser filePicker = new FileChooser();
//        
//        StopButton stopbutton = new StopButton();
//        Playbutton playbutton = new Playbutton();
//        RepeatButton repeatbutton = new RepeatButton();
//        SongButton s1 = new SongButton();
//        SongButton s2 = new SongButton();
//        SongButton s3 = new SongButton();
//        JFrame frame = new JFrame("SliderDemo");
//        
//        GenreChoiceRadioButtons gc = new GenreChoiceRadioButtons();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        SliderDemo animator = new SliderDemo();
//
//        //Add content to the window.
//        frame.add(filePicker,BorderLayout.NORTH);
//        frame.add(animator, BorderLayout.NORTH);
//        frame.add(stopbutton, BorderLayout.CENTER);
//        frame.add(playbutton, BorderLayout.CENTER);
//        frame.add(repeatbutton, BorderLayout.CENTER);
//        frame.add(s1, BorderLayout.CENTER);
//        frame.add(s2, BorderLayout.CENTER);
//        frame.add(s3, BorderLayout.CENTER);
//
//        frame.setLayout(new FlowLayout());
//        frame.add(gc, BorderLayout.CENTER);
//        //Display the window.
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        
//
//    }
//}
