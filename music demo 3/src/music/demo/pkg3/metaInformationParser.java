/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 *	Modified DumpReceiver.java
 *
 *	This file is part of jsresources.org
 */

/*
 * Copyright (c) 1999 - 2001 by Matthias Pfisterer
 * Copyright (c) 2003 by Florian Bomers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 private final double BeatsPerMillisecond;
 private int resolutionOfTrack;//AKA THE PPQ
 private double lengthOfTrack; // In Milliseconds
 private double BPMOfTrack;
 private final long CommonConverterUnit = 60000;
 private final int ConvertMicrotoMilli = 1000;
 private Sequence sequence;
 this.resolutionOfTrack = sequence.getResolution();
 this.lengthOfTrack = sequence.getMicrosecondLength() / ConvertMicrotoMilli;
 this.BPMOfTrack = (CommonConverterUnit / (resolutionOfTrack * lengthOfTrack)); //convert notes to beats 
 this.BeatsPerMillisecond = BPMOfTrack / CommonConverterUnit;
 */
package music.demo.pkg3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import org.jfugue.Instrument;
import org.jfugue.MidiParser;
import org.jfugue.Time;
import org.jfugue.Voice;

/**
 *
 * @author Kevin
 */
public final class metaInformationParser {

    //Assume 1 instance of each per track will be modified in future for strange other cases
    private String copyrightNotice;
    private String[] midiTempos;
    private String[] lyrics;
    //private String[] instrumentNames;
    private ArrayList<ArrayList<String>>instrumentNames;
    private String[] keySignitures;
    private String[] timeSignitures;
    private int numberOfTracks;
    private int numberOfTracksWithNotesExcludingDrums;//track 8 is drums

    public metaInformationParser(String FileName) throws InvalidMidiDataException, IOException {
        MidiParser mp = new MidiParser();
        Sequence sequence = MidiSystem.getSequence(new File(FileName));
        sequence = MidiSystem.getSequence(new File(FileName));
        numberOfTracks = sequence.getTracks().length + 20;
        midiTempos = new String[numberOfTracks];
        lyrics = new String[numberOfTracks];
      //  instrumentNames = new String[numberOfTracks];
        instrumentNames = new ArrayList<>(17); // up to 16 tracks
        for(int i =0; i < 17; i++) //allocate space for each track
        {
            instrumentNames.add(new ArrayList<>(17)); //17 instruments per track
        }
        keySignitures = new String[numberOfTracks];
        timeSignitures = new String[numberOfTracks];
        numberOfTracksWithNotesExcludingDrums = 0;
        int trackNumber = 0;

        for (Track track : sequence.getTracks()) {

          //  System.out.println("V" + trackNumber + ": size = " + track.size());
            // System.out.println();
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);

//                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof MetaMessage) {
                    decodeMessage((MetaMessage) message, trackNumber);
                    //String strMessage = decodeMessage((MetaMessage) message, trackNumber);
                    //  System.out.println("Voice" + trackNumber + ": @" + event.getTick() + " " + strMessage);
                }
                if (message instanceof ShortMessage) {
                    parseShortMessage((ShortMessage) message, trackNumber);
                }
            }
            trackNumber++;
            if (track.size() != 0 && trackNumber != 8) {
                numberOfTracksWithNotesExcludingDrums++;
            }
        }
        for(int i =0; i< instrumentNames.size();i++ )
        {
            ArrayList <String> sample = instrumentNames.get(i);
            for(String s : sample)
            {
            if (s != null) {
              //  System.out.println(i+ " " + s);
                
            }
            }
        }
        instrumentNames.trimToSize(); //removes nulls
               for(int i =0; i < 17; i++) //allocate space for each track
        {
            instrumentNames.get(i).trimToSize(); //17 instruments per track
        }
    }

    private void parseShortMessage(ShortMessage message, int trackNumber) {
        int track = message.getChannel();

        switch (message.getCommand()) {
            case ShortMessage.PROGRAM_CHANGE:                  // 0xC0, 192

                String instrument = new Instrument((byte) message.getData1()).getInstrumentName();
                String strMessage = instrument;
                addInstrument(strMessage, trackNumber);

                break;
        }
    }

    public String decodeMessage(MetaMessage message, int trackNumber) {
        String[] sm_astrKeySignatures = {"Cb", "Gb", "Db", "Ab", "Eb", "Bb", "F", "C", "G", "D", "A", "E", "B", "F#", "C#"};
        byte[] abMessage = message.getMessage();
        byte[] abData = message.getData();
        int nDataLength = message.getLength();
        String strMessage = null;
        //System.out.println("data array length: " + abData.length);

        switch (message.getType()) {
            case 0:
                int nSequenceNumber;
                if (abData.length == 0) {
                    nSequenceNumber = 0;
                } else {
                    nSequenceNumber = ((abData[0] & 0xFF) << 8) | (abData[1] & 0xFF);
                }
                strMessage = "Sequence Number: " + nSequenceNumber;
                break;

            case 1:
                String strText = new String(abData);
                strMessage = "Text Event: " + strText;
                break;

            case 2:
                String strCopyrightText = new String(abData);
                strMessage = "Copyright Notice: " + strCopyrightText;
                setCopyrightNotice(strCopyrightText);
                break;

            case 3:
                String strTrackName = new String(abData);
                strMessage = "Sequence/Track Name: " + strTrackName;

                break;

            case 4:
                System.out.println("set instrument");
                String strInstrumentName = new String(abData);
                strMessage = strInstrumentName;
                addInstrument(strMessage, trackNumber);

                break;

            case 5:
                String strLyrics = new String(abData);
                if (strLyrics.equals("\r\n")) {
                    strLyrics = "\\n";
                }
                strMessage = "Lyric: " + strLyrics;
                setLyrics(strMessage, trackNumber);
                break;

            case 6:
                String strMarkerText = new String(abData);
                strMessage = "Marker: " + strMarkerText;
                break;

            case 7:
                String strCuePointText = new String(abData);
                strMessage = "Cue Point: " + strCuePointText;
                break;

            case 0x20:
                int nChannelPrefix = abData[0] & 0xFF;
                strMessage = "MIDI Channel Prefix: " + nChannelPrefix;
                break;

//            case 0x2F:
//                strMessage = "End of Track";
//                break;
            case 0x51:
                int nTempo = ((abData[0] & 0xFF) << 16)
                        | ((abData[1] & 0xFF) << 8)
                        | (abData[2] & 0xFF);           // tempo in microseconds per beat
                float bpm = convertTempo(nTempo);
                // truncate it to 2 digits after dot
                bpm = (float) (Math.round(bpm * 100.0f) / 100.0f);
                strMessage = "Set Tempo: " + bpm + " bpm";
                setMidiTempos(strMessage, trackNumber);
                break;

//            case 0x54:
//                // System.out.println("data array length: " + abData.length);
//                strMessage = "SMTPE Offset: "
//                        + (abData[0] & 0xFF) + ":"
//                        + (abData[1] & 0xFF) + ":"
//                        + (abData[2] & 0xFF) + "."
//                        + (abData[3] & 0xFF) + "."
//                        + (abData[4] & 0xFF);
//                break;
            case 0x58:
                strMessage = "Time Signature: "
                        + (abData[0] & 0xFF) + "/" + (1 << (abData[1] & 0xFF))
                        + ", MIDI clocks per metronome tick: " + (abData[2] & 0xFF)
                        + ", 1/32 per 24 MIDI clocks: " + (abData[3] & 0xFF);
                setTimeSignitures(strMessage, trackNumber);
                break;

            case 0x59:
                String strGender = (abData[1] == 1) ? "minor" : "major";
                strMessage = "Key Signature: " + sm_astrKeySignatures[abData[0] + 7] + " " + strGender;
                setKeySignitures(strMessage, trackNumber);
                break;

            case 0x7F:
                // TODO: decode vendor code, dump data in rows
                String strDataDump = getHexString(abData);
                strMessage = "Sequencer-Specific Meta event: " + strDataDump;
                break;

            default:
                String strUnknownDump = getHexString(abData);
                strMessage = "unknown Meta event: " + strUnknownDump;
                break;

        }
        return strMessage;
    }

    private static float convertTempo(float value) {

        if (value <= 0) {
            value = 0.1f;
        }
        return 60000000.0f / value;
    }

    public static String getHexString(byte[] aByte) {
        char hexDigits[]
                = {'0', '1', '2', '3',
                    '4', '5', '6', '7',
                    '8', '9', 'A', 'B',
                    'C', 'D', 'E', 'F'};
        StringBuffer sbuf = new StringBuffer(aByte.length * 3 + 2);
        for (int i = 0; i < aByte.length; i++) {
            sbuf.append(' ');
            sbuf.append(hexDigits[(aByte[i] & 0xF0) >> 4]);
            sbuf.append(hexDigits[aByte[i] & 0x0F]);
            /*byte	bhigh = (byte) ((aByte[i] &  0xf0) >> 4);
             sbuf.append((char) (bhigh > 9 ? bhigh + 'A' - 10: bhigh + '0'));
             byte	blow = (byte) (aByte[i] & 0x0f);
             sbuf.append((char) (blow > 9 ? blow + 'A' - 10: blow + '0'));*/
        }
        return new String(sbuf);
    }

    // Nothing to do - JFugue doesn't use sysex messages
    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }

    public String[] getMidiTempos() {
        return midiTempos;
    }

    public void setMidiTempos(String midiTempos, int index) // only 1 tempo per track
    {
        if (this.midiTempos[index] == null) {
            this.midiTempos[index] = midiTempos;
        }
    }

    public String[] getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics, int index) {
        this.lyrics[index] = lyrics;
    }

    public ArrayList<ArrayList<String>> getInstrumentNames() {
        return instrumentNames;
    }
        public void addInstrument(String instrumentName, int track) {
            if(!instrumentName.equalsIgnoreCase("GM Device"))
              this.instrumentNames.get(track).add(instrumentName); 
    }

//    public void setInstrumentNames(String instrumentName, int index) {
//        this.instrumentNames[index] = instrumentNames;
//    }

    public String[] getKeySignitures() {
        return keySignitures;
    }

    public void setKeySignitures(String keySignitures, int index) {
        if (this.keySignitures == null) {
            this.keySignitures[index] = keySignitures;
        }
    }

    public String[] getTimeSignitures() {
        return timeSignitures;
    }

    public void setTimeSignitures(String timeSignitures, int index) {
        if (this.timeSignitures[index] == null) {
            this.timeSignitures[index] = timeSignitures;
        }
    }

    public int getNumberOfTracks() {
        return numberOfTracks;
    }

    public void setNumberOfTracks(int numberOfTracks) {
        this.numberOfTracks = numberOfTracks;
    }

    public int getNumberOfTracksWithNotesExcludingDrums() {
        return numberOfTracksWithNotesExcludingDrums;
    }

}
