package model.tech.phlocs.histleap.Slider;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Division {
    private String name;
    private int start;
    private int end;

    public Division(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
