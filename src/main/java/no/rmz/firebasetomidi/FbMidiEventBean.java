package no.rmz.firebasetomidi;

public final class FbMidiEventBean {

    private String cmd;
    private int chan;
    private int note;
    private int strength;

    public FbMidiEventBean() {
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setChan(int chan) {
        this.chan = chan;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }


    public String getCmd() {
        return cmd;
    }

    public int getChan() {
        return chan;
    }

    public int getNote() {
        return note;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return "FbMidiEventBean{" + "cmd=" + cmd + ", chan=" + chan + ", note=" + note + ", strength=" + strength + '}';
    }
}
