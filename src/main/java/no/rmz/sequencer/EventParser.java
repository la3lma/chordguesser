package no.rmz.sequencer;

import no.rmz.eventgenerators.JitterPreventionFailureException;
import no.rmz.eventgenerators.ParsedEvent;

public interface EventParser {

    ParsedEvent parse(String s) throws JitterPreventionFailureException;
}
