package no.rmz.firebasetomidi;

import no.rmz.eventgenerators.ParsedEvent;

public final class FBMidiEvent implements ParsedEvent {

    public FBMidiEvent() {
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getTimestamp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
