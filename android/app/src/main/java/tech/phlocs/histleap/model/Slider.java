package tech.phlocs.histleap.model;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Slider {
    private ArrayList<Integer> range;
    private ArrayList<Division> divisions;

    public Slider(ArrayList<Division> divisions) {
        ArrayList<Integer> iRange = new ArrayList<>();
        iRange.add(0);
        iRange.add(divisions.size() - 1);
        this.divisions = divisions;
        this.range = iRange;
    }

    public boolean isSamePoint() {
        return range.get(0).equals(range.get(1));
    }

    private boolean isReversePoint() {
        return (range.get(0) > range.get(1));
    }

    public ArrayList<Integer> getRange() {
        return range;
    }

    public void setRange(ArrayList<Integer> range) { this.range = range; }

    public void setRangeByPushPoint(int point) {
        if (point < range.get(0)) {
            range.set(0, point);
        } else if (point > range.get(1)) {
            range.set(1, point);
        } else {
            if ((point - range.get(0)) <= (range.get(1) - point)) {
                range.set(0, point);
            } else {
                range.set(1, point);
            }
        }
    }

    public void setRangeByScroll(int from, int to) {
        range.set(range.lastIndexOf(from), to);
        if (isReversePoint()) {
            int t = range.get(0);
            range.set(0, range.get(1));
            range.set(1, t);
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

    public int getEdgeYear(int $edge) {
        int index = getRange().get($edge);
        Division division = getDivisions().get(index);
        return ($edge == 0 ? division.getStart() : division.getEnd());
    }
}
