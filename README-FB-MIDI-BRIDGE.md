Firebase Midi Bridge
========

  ./fbmidibridge.java foobarbaz foobarbaz.json   path receiving-midi-device


... will work inside the foobarbaz firebase application, using the
foobarbaz.json security credentials to listen for incoming midi events
in the path.  Once the events are received, they are removed from
firebase, and transmitted as MIDI on the local OSX midi device called
virtual-midi-device