package no.rmz.firebasetomidi;

import no.rmz.eventgenerators.EventReceiver;
import no.rmz.eventgenerators.ParsedEvent;

// XXX This is messed up
public final class FbMidiEventListener implements EventReceiver {

    public void onFbMidiEvent(final FBMidiEvent event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void receive(ParsedEvent event) {
        onFbMidiEvent((FBMidiEvent) event);
    }
}
