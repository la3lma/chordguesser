package no.rmz.experiments;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;
import org.jfugue.theory.ChordProgression;

public class HelloJfugue {

    public static void main(final String[] args) {
        final Player player = new Player();
        player.play("C D E F G A B");

        final ChordProgression cp
                = new ChordProgression("I7 I7 I7 I7 IV7 IV7 I7 I7 V7 IV7 I7 I7");
        player.play(cp.eachChordAs("$0i $1i $2i $0'6bi $3i $0'6bi $2i $1i"));

        final Pattern pattern = new ChordProgression("I IV V")
                .distribute("7%6")
                .allChordsAs("$0 $0 $0 $0 $1 $1 $0 $0 $2 $1 $0 $0")
                .eachChordAs("$0ia100 $1ia80 $2ia80 $3ia80 $4ia100 $3ia80 $2ia80 $1ia80")
                .getPattern()
                .setInstrument("Acoustic_Bass")
                .setTempo(100);
        new Player().play(pattern);
    }
}
