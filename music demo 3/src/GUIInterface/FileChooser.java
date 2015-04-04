/*

 TO DO --> Make Analyzer Correct the file with an option in dialogue box.
 --> Make 2 other tables 1 should recognize patterns, the other should recognize full sequences for durations.

 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUIInterface;
//Does Not look at other directories atm

/**
 * *
 *
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * - Neither the name of Oracle or the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import music.demo.pkg3.HTMLFileWritter;
import music.demo.pkg3.MidiFileAnalyzer;
import org.jfugue.Pattern;
import org.jfugue.Player;
import music.demo.pkg3.MusicAnalysisContainer;
import music.demo.pkg3.PrettyPrintingMap;

/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
public class FileChooser extends JPanel
        implements ActionListener {

    private static Pattern selectedSong = null;
    private static File[] selectedFile = new File[10];
    private int instanceNumber;
    private static int instanceTracker;
    static private final String newline = "\n";
    JButton openButton, saveButton;
    ImageIcon icon;
    JFileChooser fc;
    private static MidiFileAnalyzer MFA;

    public FileChooser() {
        super(new BorderLayout());
        this.instanceNumber = instanceTracker;
        instanceTracker++;
        icon = createImageIcon("images/erroricon.png");
        String userDir = System.getProperty("user.home");
        fc = new JFileChooser(userDir);
        FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
                "Midi Files (Example.mid)", "mid");
        fc.setFileFilter(xmlfilter);
        fc.setDialogTitle("Open midi file");

        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        openButton = new JButton("Open a File...",
                createImageIcon("images/Open16.gif"));
        openButton.addActionListener(this);

        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...",
                createImageIcon("images/Save16.gif"));
        saveButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(FileChooser.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {

                // String nameOfFile =   fc.getSelectedFile().getName();
                String nameOfFile = fc.getSelectedFile().getAbsolutePath();
                if (nameOfFile.substring(nameOfFile.indexOf(".") + 1).equalsIgnoreCase("mid")) {
                    FileChooser.selectedFile[instanceNumber] = new File(fc.getSelectedFile().getAbsolutePath()); //Gets file from anywhere
                    //This is where a real application would open the file.
                    String message = "File Opened: " + FileChooser.selectedFile[instanceNumber].getName();
                    //This is where a real application would save the file.
                    JOptionPane.showMessageDialog(new JFrame(), message, "Opened",
                            JOptionPane.ERROR_MESSAGE, icon);
                    AnalyzeTrack();
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "Error: Must use Midi File (.mid extension)", "Bad Format",
                            JOptionPane.ERROR_MESSAGE, icon);
                }
            }
            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(FileChooser.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String fileName = null;
                if (fc.getSelectedFile().getAbsolutePath().contains(".")) {
                    fileName = fc.getSelectedFile().getAbsolutePath().substring(0, fc.getSelectedFile().getAbsolutePath().indexOf(".")) + ".mid";
                } else {
                    fileName = fc.getSelectedFile().getAbsolutePath() + ".mid";
                }
                if (selectedSong != null) {
                    try {
                        Player player = new Player();
                        Pattern pattern = selectedSong;
                        player.saveMidi(pattern, new File(fileName));
                        String message = "Save Successful";
                        //This is where a real application would save the file.
                        JOptionPane.showMessageDialog(new JFrame(), message, "SAVED",
                                JOptionPane.ERROR_MESSAGE, icon);
                    } catch (IOException ex) {
                        Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "No song was played. Please play a song before saving", "SAVED",
                            JOptionPane.ERROR_MESSAGE, icon);

                }

            }
        }
    }

    private void AnalyzeTrack() {
        try {
            MFA = new MidiFileAnalyzer(FileChooser.selectedFile[instanceNumber].getAbsolutePath());
            //Sample Output
            String message = "Save Successful";
   //         List<String> v = new ArrayList<>(MusicAnalysisContainer.getMidiToNotePercentages().keySet());
//            System.out.println("List Of TOTALS");
//            v.stream().filter((str) -> (MusicAnalysisContainer.getMidiToNotePercentages().get(str) != 0.0)).forEach((str) -> {
//                System.out.println(str + " " +  MusicAnalysisContainer.getMidiToNotePercentages().get(str));
//
//            });
//            System.out.println("END List Of TOTALS"); //Ouput of totals for notes not taking tracks or durations into account
            //invidual track info

//            MusicAnalysisContainer.getMidiToNotePercentagesTotals().size();
//            for (int voice = 0; voice < MusicAnalysisContainer.getMidiToNotePercentagesTotals().size(); voice++) {
//
//                for (String note : MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(voice).keySet()) {
//                    System.out.println("NOTE CHANCE : Voice" + voice + ":" + note + ":" + MusicAnalysisContainer.getMidiToNotePercentagesTotals().get(voice).get(note));
//                }
//
//            }
            System.out.println("Probibilities of Duration not taking tracks into consideration");
            System.out.println(new PrettyPrintingMap<>(MusicAnalysisContainer.getNoteToDurationsWithoutTracks()));
            System.out.println("END Probibilities of Duration not taking tracks into consideration");
            //   System.out.println(MusicAnalysisContainer.getNoteToDurationsWithoutTracks());
        } catch (InvalidMidiDataException e) {
            System.out.println("Was not able to Open or Find  Midi format in File in FileChooser.Java");
        } catch (IOException ex) {
            Logger.getLogger(FileChooser.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Probibilities of Duration  taking tracks into consideration");
        for (int i = 0; i < MusicAnalysisContainer.getNotesToDurationsWithTracks().size(); i++) {
            System.out.println(new PrettyPrintingMap<>(MusicAnalysisContainer.getNotesToDurationsWithTracks().get(i)));
        }
        System.out.println("END Probibilities of Duration  taking tracks into consideration");

           HTMLFileWritter HFW = new HTMLFileWritter();
        File resultHtml = new File("results.html");
        if (!Desktop.isDesktopSupported()) {
            JOptionPane.showMessageDialog(new JFrame(), "Cannot open results HTML With This Version Of Java Or Operating System", "Error",
                    JOptionPane.ERROR_MESSAGE, icon);
                    
        }
        else
        {
        Desktop desktop = Desktop.getDesktop();
        if (resultHtml.exists()) {
            try{
                   desktop.open(resultHtml); 
            }
            catch(IOException e)
            {
                JOptionPane.showMessageDialog(new JFrame(), "results.html was not saved in the proper location.", "Error",
                    JOptionPane.ERROR_MESSAGE, icon);
            }
        
        }
    }
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     *
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = FileChooser.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public File getSelectedFile() {
        return selectedFile[instanceNumber];
    }

    public static void setSelectedSong(Pattern s) {
        selectedSong = s;
    }

    public String getSelectedFileName() {
        return selectedFile[instanceNumber].getName();
    }

    public void setSelectedFile(File selectedFile) {
        FileChooser.selectedFile[instanceNumber] = selectedFile;
    }

    public JButton getOpenButton() {
        return openButton;
    }

    public void setOpenButton(JButton openButton) {
        this.openButton = openButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(JButton saveButton) {
        this.saveButton = saveButton;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public JFileChooser getFc() {
        return fc;
    }

    public void setFc(JFileChooser fc) {
        this.fc = fc;
    }

    public static MidiFileAnalyzer getMFA() {
        return MFA;
    }

}
