package no.rmz.sequencer;


final class SequencerException extends Exception {

    SequencerException(String string) {
        super(string);
    }

    SequencerException(String msg, Throwable ex) {
        super(msg,ex);
    }
}
