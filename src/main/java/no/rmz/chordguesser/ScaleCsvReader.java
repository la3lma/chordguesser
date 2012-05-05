package no.rmz.chordguesser;

import java.io.FileNotFoundException;
import java.io.IOException;


public class ScaleCsvReader {

    /**
     * The fields we can read from the csv file.
     * 
     * Alternative Scale Names,Ascending Note Positions in Scale,Aug.
     * Triads,Binary 12notes 1&0,Composer,Contiguous Notes,Dim.
     * Triads,DissonanceIndex,Flatmost Note,L and s Interval
     * Sequence,LengthOfChain,Major Triads,Minor Triads,Name Of
     * Scale,No.OfMissing Notes in Chain,Note Names from
     * C,NotesInStepsOfFifths,Number Of Notes In Scale,NumberOfIntervals,Pitch
     * Set binary,PitchSet Notation 12 edo,PositionOfTonic,Reference
     * Url,ScaleCoding,Sharpmost Note,TotalStepsBetweenAllIntervals,TuningTable
     * from Tonic A,TuningTable from Tonic Ab,TuningTable from Tonic
     * B,TuningTable from Tonic Bb,TuningTable from Tonic C,TuningTable from
     * Tonic C#,TuningTable from Tonic D,TuningTable from Tonic E,TuningTable
     * from Tonic Eb,TuningTable from Tonic F,TuningTable from Tonic
     * F#,TuningTable from Tonic G,TuningTable from Tonic G#,WorkExample
     */
    public static void main(final String[] args) {
        try {
            
            // Check out http://opencsv.sourceforge.net/, use the bean reader
            // feature if possible

            // XXX Should be a resource instead
            final CsvReader chords = new CsvReader("data/scaleCodingjan2011.csv");

            chords.readHeaders();

            while (chords.readRecord()) {
                final String binaryNotes = chords.get("Binary 12notes 1&0");
                final String alternativeScaleName = chords.get("Alternative Scale Names");


                // perform program logic here
                System.out.println(binaryNotes + ":" + alternativeScaleName);
            }

            chords.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
