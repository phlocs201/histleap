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

    public Slider(ArrayList<Division> divisions) {
        ArrayList<Integer> iRange = new ArrayList<>();
        iRange.add(0);
        iRange.add(divisions.size());
        this.divisions = divisions;
        this.range = iRange;
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

    public int getEdgeYear(int $edge) {
        int index = getRange().get($edge);
        Division division = getDivisions().get(index);
        return ($edge == 0 ? division.getStart() : division.getEnd());
    }
}
