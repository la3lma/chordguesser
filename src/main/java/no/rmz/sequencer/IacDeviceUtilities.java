package no.rmz.sequencer;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 *  Finds an IAC bus in an IAC de  device, and 
 *  returns an instance with an opened midi device.
 */
public final class IacDeviceUtilities {

    /**
     * Utility class, no creator.
     */
    private IacDeviceUtilities(){}

    public  static MidiDevice getMidiDevice(
            final String iacDeviceName,
            final String iacBusName) throws SequencerException {
        checkNotNull(iacBusName);
        checkNotNull(iacDeviceName);

        final MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : infos) {
            String desc = info.getDescription();
            String vendor = info.getVendor();
            String version = info.getVersion();
            final String name = info.getName();
            if (iacBusName.equals(name)) {
                final MidiDevice device;
                System.out.println(info.toString());
                try {
                    device = MidiSystem.getMidiDevice(info);
                    device.open();
                } catch (MidiUnavailableException ex) {
                    throw new SequencerException(
                            "Midi unavailable for device " + name, ex);
                }
               
                if (device.getMaxReceivers() != 0) { 
                    return device;
                } else {
                    throw new SequencerException("Couldn't get receiver for device " + device);
                }
            }
        }
        throw new SequencerException("Couldn't  find IAC midi device: "
                + iacDeviceName + ", "
                + iacBusName);
    }
    
    
    public static void listReceiverDevices() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice device = MidiSystem.getMidiDevice(info);
            if (device.getMaxReceivers() != 0) {
                final String deviceName = device.getDeviceInfo().getName();
                System.out.println(deviceName
                        + " has receivers");
                
                if ("Bus 1".equals(deviceName)) {
                    device.open();
                    HackedUpSequencer.startSequencer(device);
                }
            }
        }
    }
}
