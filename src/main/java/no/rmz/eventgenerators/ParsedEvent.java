package no.rmz.eventgenerators;


/**
 * Parse something as an event, and be able to tell if
 * it the parsing was successful or not.
 */
public interface ParsedEvent {

    boolean isValid();
    
    long getTimestamp();
    
}
