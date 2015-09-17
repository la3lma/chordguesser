package no.rmz.sequencer;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 * Finds an IAC bus in an IAC de device, and returns an instance with an opened
 * midi device.
 */
public final class IacDeviceUtilities {

    /**
     * Utility class, no creator.
     */
    private IacDeviceUtilities() {
    }

    /**
     * Get a midi device ready to receive input.
     * @param devicename Name of midi device.
     * @return A MidiDevice instance, opened, ready to receive input.
     * @throws SequencerException if midi device is unavailable or has no receivers.
     */
    public static MidiDevice getMidiReceivingDevice(final String devicename) throws SequencerException {
        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (final MidiDevice.Info info : infos) {
            try {
                final MidiDevice device = MidiSystem.getMidiDevice(info);
                if (device.getMaxReceivers() != 0) {
                    final String deviceName = device.getDeviceInfo().getName();
                    if (devicename.equals(deviceName)) {
                        device.open();
                        return device;
                    }
                }
            } catch (MidiUnavailableException ex) {
                throw new SequencerException("Midi unavailable for  : " + devicename, ex);
            }
        }
        throw new SequencerException("Couldn't find midi device : " + devicename);
    }
}
