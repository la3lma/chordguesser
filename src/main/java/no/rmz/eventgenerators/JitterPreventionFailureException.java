package no.rmz.eventgenerators;


public class JitterPreventionFailureException extends Exception {

    public JitterPreventionFailureException(final String txt) {
        super(txt);
    }

    public JitterPreventionFailureException(Throwable ex) {
        super(ex);
    }  
}
