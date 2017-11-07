package no.rmz.firebasetomidi;

import no.rmz.eventgenerators.ParsedEvent;

/**
 * For now, this essentially only encodes key down/key up events. Don't yet know
 * how to parse other events (knobs, wheels etc.)
 */
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
