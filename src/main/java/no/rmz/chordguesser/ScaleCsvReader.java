package no.rmz.chordguesser;

import static com.google.common.base.Preconditions.*;
import au.com.bytecode.opencsv.CSVReader;
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
        target.AlternativeScaleNames = get(iAlternativeScaleNames_I, source);
        target.AscendingNotePositionsinScale = get(iAscendingNotePositionsinScale_I, source);
        target.AugTriads = get(iAugTriads_I, source);
        target.Binary12notes = get(iBinary12notes_I, source);
        target.Composer = get(iComposer_I, source);
        target.ContiguousNotes = get(iContiguousNotes_I, source);
        target.DimTriads = get(iDimTriads_I, source);
        target.DissonanceIndex = get(iDissonanceIndex_I, source);
        target.FlatmostNote = get(iFlatmostNote_I, source);
        target.LandsIntervalSequence = get(iLandsIntervalSequence_I, source);
        target.LengthOfChain = get(iLengthOfChain_I, source);
        target.MajorTriadsMinorTriads = get(iMajorTriadsMinorTriads_I, source);
        target.NameOfScale = get(iNameOfScale_I, source);
        target.NoOfMissingNotesinChain = get(iNoOfMissingNotesinChain_I, source);
        target.NoteNamesfromC = get(iNoteNamesfromC_I, source);
        target.NotesInStepsOfFifths = get(iNotesInStepsOfFifths_I, source);
        target.NumberOfNotesInScale = get(iNumberOfNotesInScale_I, source);
        target.NumberOfIntervals = get(iNumberOfIntervals_I, source);
        target.PitchSetbinary = get(iPitchSetbinary_I, source);
        target.PitchSetNotation12edo = get(iPitchSetNotation12edo_I, source);
        target.PositionOfTonic = get(iPositionOfTonic_I, source);
        target.ReferenceUrl = get(iReferenceUrl_I, source);
        target.ScaleCodingSharpmostNote = get(iScaleCodingSharpmostNote_I, source);
        target.TotalStepsBetweenAllIntervals = get(iTotalStepsBetweenAllIntervals_I, source);
        target.TuningTablefromTonicA = get(iTuningTablefromTonicA_I, source);
        target.TuningTablefromTonicAb = get(iTuningTablefromTonicAb_I, source);
        target.TuningTablefromTonicB = get(iTuningTablefromTonicB_I, source);
        target.TuningTablefromTonicBb = get(iTuningTablefromTonicBb_I, source);
        target.TuningTablefromTonicC = get(iTuningTablefromTonicC_I, source);
        target.TuningTablefromTonicCSharp = get(iTuningTablefromTonicCSharp_I, source);
        target.TuningTablefromTonicD = get(iTuningTablefromTonicD_I, source);
        target.TuningTablefromTonicE = get(iTuningTablefromTonicE_I, source);
        target.TuningTablefromTonicEb = get(iTuningTablefromTonicEb_I, source);
        target.TuningTablefromTonicF = get(iTuningTablefromTonicF_I, source);
        target.TuningTablefromTonicFSharp = get(iTuningTablefromTonicFSharp_I, source);
        target.TuningTablefromTonicG = get(iTuningTablefromTonicG_I, source);
        target.TuningTablefromTonicGSharp = get(iTuningTablefromTonicGSharp_I, source);
        target.WorkExample = get(iWorkExample_I, source);

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
