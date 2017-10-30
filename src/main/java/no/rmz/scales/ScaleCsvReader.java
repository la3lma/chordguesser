package no.rmz.scales;

import au.com.bytecode.opencsv.CSVReader;
import static com.google.common.base.Preconditions.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class ScaleCsvReader {

    private final static String SCALE_CSV_RESOURCE_PATH = "/scaleCodingjan2011.csv";
    private final static int iAlternativeScaleNames_I = 0;
    private final static int iAscendingNotePositionsinScale_I = 1;
    private final static int iAugTriads_I = 2;
    private final static int iBinary12notes_I = 3;
    private final static int iComposer_I = 4;
    private final static int iContiguousNotes_I = 5;
    private final static int iDimTriads_I = 6;
    private final static int iDissonanceIndex_I = 7;
    private final static int iFlatmostNote_I = 8;
    private final static int iLandsIntervalSequence_I = 9;
    private final static int iLengthOfChain_I = 10;
    private final static int iMajorTriadsMinorTriads_I = 11;
    private final static int iNameOfScale_I = 12;
    private final static int iNoOfMissingNotesinChain_I = 13;
    private final static int iNoteNamesfromC_I = 14;
    private final static int iNotesInStepsOfFifths_I = 15;
    private final static int iNumberOfNotesInScale_I = 16;
    private final static int iNumberOfIntervals_I = 17;
    private final static int iPitchSetbinary_I = 18;
    private final static int iPitchSetNotation12edo_I = 20;
    private final static int iPositionOfTonic_I = 21;
    private final static int iReferenceUrl_I = 22;
    private final static int iScaleCodingSharpmostNote_I = 23;
    private final static int iTotalStepsBetweenAllIntervals_I = 24;
    private final static int iTuningTablefromTonicA_I = 25;
    private final static int iTuningTablefromTonicAb_I = 26;
    private final static int iTuningTablefromTonicB_I = 27;
    private final static int iTuningTablefromTonicBb_I = 28;
    private final static int iTuningTablefromTonicC_I = 29;
    private final static int iTuningTablefromTonicCSharp_I = 30;
    private final static int iTuningTablefromTonicD_I = 31;
    private final static int iTuningTablefromTonicE_I = 32;
    private final static int iTuningTablefromTonicEb_I = 33;
    private final static int iTuningTablefromTonicF_I = 34;
    private final static int iTuningTablefromTonicFSharp_I = 35;
    private final static int iTuningTablefromTonicG_I = 36;
    private final static int iTuningTablefromTonicGSharp_I = 37;
    private final static int iWorkExample_I = 38;

    private static String get(int i, final String s[]) {
        return (i < s.length) ? s[i] : null;
    }

    /**
     * Assign fields from the array we've just read to fields in the ScaleBean
     * we're populating. Unspecified fields in the source array will be set to
     * null in the bean.
     */
    private static void assign(final ScaleBean target, final String[] source) {
        target.alternativeScaleNames = get(iAlternativeScaleNames_I, source);
        target.ascendingNotePositionsinScale = get(iAscendingNotePositionsinScale_I, source);
        target.augTriads = get(iAugTriads_I, source);
        target.binary12notes = get(iBinary12notes_I, source);
        target.composer = get(iComposer_I, source);
        target.contiguousNotes = get(iContiguousNotes_I, source);
        target.dimTriads = get(iDimTriads_I, source);
        target.dissonanceIndex = get(iDissonanceIndex_I, source);
        target.flatmostNote = get(iFlatmostNote_I, source);
        target.landsIntervalSequence = get(iLandsIntervalSequence_I, source);
        target.lengthOfChain = get(iLengthOfChain_I, source);
        target.majorTriadsMinorTriads = get(iMajorTriadsMinorTriads_I, source);
        target.nameOfScale = get(iNameOfScale_I, source);
        target.noOfMissingNotesinChain = get(iNoOfMissingNotesinChain_I, source);
        target.noteNamesfromC = get(iNoteNamesfromC_I, source);
        target.notesInStepsOfFifths = get(iNotesInStepsOfFifths_I, source);
        target.numberOfNotesInScale = get(iNumberOfNotesInScale_I, source);
        target.numberOfIntervals = get(iNumberOfIntervals_I, source);
        target.pitchSetbinary = get(iPitchSetbinary_I, source);
        target.pitchSetNotation12edo = get(iPitchSetNotation12edo_I, source);
        target.positionOfTonic = get(iPositionOfTonic_I, source);
        target.referenceUrl = get(iReferenceUrl_I, source);
        target.scaleCodingSharpmostNote = get(iScaleCodingSharpmostNote_I, source);
        target.totalStepsBetweenAllIntervals = get(iTotalStepsBetweenAllIntervals_I, source);
        target.tuningTablefromTonicA = get(iTuningTablefromTonicA_I, source);
        target.tuningTablefromTonicAb = get(iTuningTablefromTonicAb_I, source);
        target.tuningTablefromTonicB = get(iTuningTablefromTonicB_I, source);
        target.tuningTablefromTonicBb = get(iTuningTablefromTonicBb_I, source);
        target.tuningTablefromTonicC = get(iTuningTablefromTonicC_I, source);
        target.tuningTablefromTonicCSharp = get(iTuningTablefromTonicCSharp_I, source);
        target.tuningTablefromTonicD = get(iTuningTablefromTonicD_I, source);
        target.tuningTablefromTonicE = get(iTuningTablefromTonicE_I, source);
        target.tuningTablefromTonicEb = get(iTuningTablefromTonicEb_I, source);
        target.tuningTablefromTonicF = get(iTuningTablefromTonicF_I, source);
        target.tuningTablefromTonicFSharp = get(iTuningTablefromTonicFSharp_I, source);
        target.tuningTablefromTonicG = get(iTuningTablefromTonicG_I, source);
        target.tuningTablefromTonicGSharp = get(iTuningTablefromTonicGSharp_I, source);
        target.workExample = get(iWorkExample_I, source);

    }

    
    
     public  static ChordAndScaleDatabase readChordAndScaleDatabaseFromResources() throws IOException {
        final ScaleCsvReader scr = new ScaleCsvReader();
        final List<ScaleBean> beanlist = scr.readScalesFromResourceCsv();

        final ChordAndScaleDatabase result = new ChordAndScaleDatabase();
        result.importAllScales(beanlist);
        return result;
    }
     public  List<ScaleBean> readScalesFromResourceCsv() throws FileNotFoundException, IOException {
         
         // XXX This is fairly inefficient parsing, but if more efficiency
         //     is needed, (that's not certain), hints can be found here:
         //         http://opencsv.sourceforge.net/apidocs/index.html
         //         http://opencsv.sourceforge.net/,
         
         
         final InputStream scaleCsvStream = this.getClass().getResourceAsStream(SCALE_CSV_RESOURCE_PATH);
         checkNotNull(scaleCsvStream, "scaleCsvStram can't be null. Probably couldn't locate scale csv resource " + SCALE_CSV_RESOURCE_PATH);
         final InputStreamReader inputStreamReader = new InputStreamReader(scaleCsvStream, "UTF-8");
         
         final CSVReader chords = new CSVReader(inputStreamReader);
         final List<String[]> allChordsAsArrays = chords.readAll();
         
         
         final List<ScaleBean> result = new ArrayList<>(allChordsAsArrays.size() - 1);
         allChordsAsArrays.subList(1, allChordsAsArrays.size()).stream().map((v) -> {
             final ScaleBean b = new ScaleBean();
             assign(b, v);
             return b;
         }).forEach((b) -> {
             result.add(b);
         });
         return result;
     }
}
