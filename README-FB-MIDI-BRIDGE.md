Firebase Midi Bridge
========

  ./fbmidibridge.jar  process foobarbaz foobarbaz.json   path receiving-midi-device

... will work inside the foobarbaz firebase application, using the
foobarbaz.json security credentials to listen for incoming midi events
in the path.  Once the events are received, they are removed from
firebase, and transmitted as MIDI on the local OSX midi device called
virtual-midi-device

 ./fbmidibridge.jar ls

...  will list all midi devices that are available as recipients of events.


Ideas:

* Add dropwizard into it
* Receive incoming HTTP post messages, so MIDI can be played using shell and curl.
* Add some UDP protocol that can receive incoming packages and play them direclty, this will make
  it possible to make relatively low latency setups with wifi-based
  instruments.