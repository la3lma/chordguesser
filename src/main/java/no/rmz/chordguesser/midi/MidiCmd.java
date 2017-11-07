package no.rmz.chordguesser.midi;

import static com.google.common.base.Preconditions.checkNotNull;

public enum MidiCmd {
    NOTE_ON("NOTE_ON"),
    NOTE_OFF("NOTE_OFF"),
    AFTER_TOUCH("AFTER_TOUCH"),
    CONTROL_CHANGE("CONTROL_CHANGE"),
    PROGRAM_PATCH_CHANGE("PROGRAM_PATCH_CHANGE"),
    CHANNEL_PRESSURE("CHANNEL_PRESSURE"),
    PITCH_WHEEL("PITCH_WHEEL");

    final String encoding;

    private MidiCmd(String encoding) {
        this.encoding = checkNotNull(encoding);
    }
}
