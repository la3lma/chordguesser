package no.rmz.chordguesser;


// XXX Presently this should only be used to get a bunch of representative
//     midi events and have them printed out in a nice manner, then
//     those events should be harvested into a proper set of unit tests
//     that sends note on/off events to a ToneListener, which in turn
//     updates a PolyphonicState, which in turn triggers some
//     chord and scale detection routines.
public class ChordGuesser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final PolyphonicState ps = new PolyphonicState();
        
       final  ToneListener tl = new ToneListener() {

            public void noteOff(int i) {
                System.out.println("noteOff: " + i);
                ps.noteOff(i);
            }

            public void noteOn(int i) {
                System.out.println("noteOn: " + i);
                ps.noteOn(i);
            }
        };
     
        final MidiHandler mh = new MidiHandler(tl);
        
        mh.run();
    }
}
