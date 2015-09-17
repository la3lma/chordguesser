package no.rmz.sequencer;

import org.jfugue.player.Player;
import org.jfugue.theory.ChordProgression;

public class HelloJfugue {

    public static void main(final String[] args) {
        final Player player = new Player();
        player.play("C D E F G A B");

        final ChordProgression cp = 
                new ChordProgression("I7 I7 I7 I7 IV7 IV7 I7 I7 V7 IV7 I7 I7");
        player.play(cp.eachChordAs("$0i $1i $2i $0'6bi $3i $0'6bi $2i $1i"));
    }
}
