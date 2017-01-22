package tech.phlocs.histleap.model;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Slider {
    private boolean isSamePoint;
    private ArrayList<Integer> range;
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

        ArrayList<Integer> initialRange = new ArrayList<>();
        initialRange.add(0);
        initialRange.add(initialSlider.size() - 4);

        this.divisions = initialSlider;
        this.range = initialRange;
        this.isSamePoint = this.range.get(0).equals(this.range.get(1));
    }

    public ArrayList<Integer> getRange() {
        return range;
    }

    public void setRange(ArrayList<Integer> range) { this.range = range; }

    public void setRangeByPosition(int position) {
        if (position < range.get(0)) {
            range.set(0, position);
        } else if (position > range.get(1)) {
            range.set(1, position);
        } else {
            if ((position - range.get(0)) <= (range.get(1) - position)) {
                range.set(0, position);
            } else {
                range.set(1, position);
            }
        }
    }

    public ArrayList<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(ArrayList<Division> divisions) {
        this.divisions = divisions;
    }

    public ArrayList<Spot> getFilteredSpots(ArrayList<Spot> spots) {
        ArrayList<Spot> filteredSpots = new ArrayList<>();
        for (Spot spot: spots) {
            for (Event event: spot.getEventList()) {
                if (event.getStartYear() >= getDivisions().get(getRange().get(0)).getStart()
                        && event.getStartYear() <= getDivisions().get(getRange().get(1)).getEnd()) {
                    filteredSpots.add(spot);
                    break;
                }
            }
        }
        return filteredSpots;
    }
    public int getStartYear() {
        int startIndex = getRange().get(0);
        Division startDivision = getDivisions().get(startIndex);
        int startYear = startDivision.getStart();
        return startYear;
    }
    public int getEndYear() {
        int endIndex = getRange().get(1);
        Division endDivision = getDivisions().get(endIndex);
        int endYear = endDivision.getEnd();
        return endYear;
    }
}
