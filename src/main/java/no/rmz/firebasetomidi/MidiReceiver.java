package no.rmz.firebasetomidi;

import javax.sound.midi.ShortMessage;

interface MidiReceiver {

    public void put(ShortMessage msg);
}
