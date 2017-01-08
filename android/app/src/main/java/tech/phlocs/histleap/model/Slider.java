package tech.phlocs.histleap.model;

import java.util.ArrayList;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Slider {
    private Range range;
    private ArrayList<Division> divisions;

    public Slider() {
        ArrayList<Division> initialSlider = new ArrayList<>();
        initialSlider.add(new Division("飛鳥時代", 680, 710));
        initialSlider.add(new Division("奈良時代", 710, 794));
        initialSlider.add(new Division("平安時代", 794, 1192));
        initialSlider.add(new Division("鎌倉時代", 1192, 1336));
        initialSlider.add(new Division("室町時代", 1336, 1573));
        initialSlider.add(new Division("安土桃山時代", 1573, 1603));
        initialSlider.add(new Division("江戸時代", 1603, 1868));
        initialSlider.add(new Division("明治時代", 1868, 1912));
        initialSlider.add(new Division("大正時代", 1912, 1926));
        initialSlider.add(new Division("昭和時代", 1926, 1989));
        initialSlider.add(new Division("平成時代", 1989, 2017));

        this.divisions = initialSlider;
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public ArrayList<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(ArrayList<Division> divisions) {
        this.divisions = divisions;
    }
}
