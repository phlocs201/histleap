package tech.phlocs.histleap.model;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

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
        ArrayList<Integer> edgeYear = getEdgeYear();
        for (Spot spot: spots) {
            for (Event event: spot.getEventList()) {
                if (!((event.getStartYear() > edgeYear.get(1)) ||
                        (event.getEndYear() < edgeYear.get(0)))) {
                    filteredSpots.add(spot);
                    break;
                }
            }
        }
        return filteredSpots;
    }

    public ArrayList<Spot> getExistSpots(ArrayList<Spot> spots) {
        ArrayList<Spot> existSpots = new ArrayList<>();
        ArrayList<Integer> edgeYear = getEdgeYear();
        for (Spot spot: spots) {
            int startYear = spot.getEventList().get(0).getStartYear();
            int endYear = spot.getEventList().get(spot.getEventList().size() - 1).getEndYear();
            if (!((startYear > edgeYear.get(1)) || (endYear < edgeYear.get(0)))) {
                existSpots.add(spot);
            }
        }
        return existSpots;
    }

    public ArrayList<Integer> getEdgeYear() {
        return new ArrayList<>(
            Arrays.asList(
                getDivisions().get(getRange().get(0)).getStart(),
                getDivisions().get(getRange().get(1)).getEnd()
            )
        );
    }
}
