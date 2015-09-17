chordguesser
============

This probgram started out as a little utility to listen in on midi sequences to guess chords,
but has since been abandoned, then repurposed to send midi sequences to external synths, and
is now in a state of disarray.

Got some love from stackoverflow.com to fix a troublesome issue

    http://stackoverflow.com/questions/32591280/how-to-send-midi-from-a-java-program-to-an-iac-bus-on-osx?stw=2

... it turned out that I had just forgotten to RTFM, and consequently not
called open() on the IAC device.   

That fix opens a bunch of doors that I have been planning to walk through,
and I hope I will no be able to do so.  

Until I do this program will stay in a state of disarray, but when i'm done
it will be much better :-))


# The original comments for this program


A little utility that will (eventually) listen to MIDI sources and guess which chords are being played.

update:  The chordguesser thingy never did exactly what I wanted it to.  Some bugs I never had time
to remove.  However, there is now something else I want to try:

  * On the mac, set up a virtual midi device.
  * Send a sequence of midi output to that devie.
  * Route the midi signals to a virtual instrument.
  * Hear the pings.

Why?  Because I want to make algorithmic music.  In particular I want events that can be picked
up by monitoring (networks, the environment or whatever) to be transformed into MIDI signals
and then expressed as surfaces of sound.  This is something so cool that it just has to be done,
and these virtual MIDI thingys may be the entry point I need to get started. Anyway, I want to try!

