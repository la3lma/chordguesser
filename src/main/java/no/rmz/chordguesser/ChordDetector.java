package no.rmz.chordguesser;


public class ChordDetector {
    private final PolyphonicState chordNotes;
    private final String longName;
    private final String shorthandName;
    private final int    baseTone;

    public ChordDetector(final String longName, final String shorthandName, int baseTone, final PolyphonicState chordNotes) {
        this.chordNotes = chordNotes;
        this.longName = longName;
        this.shorthandName = shorthandName;
        this.baseTone = baseTone;
    }
    
    
    
    
    
    
}
