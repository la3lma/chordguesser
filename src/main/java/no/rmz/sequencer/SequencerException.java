package no.rmz.sequencer;


final class SequencerException extends Exception {

    public SequencerException(String string) {
        super(string);
    }

    public SequencerException(String msg, Throwable ex) {
        super(msg,ex);
    }
}
