package model.tech.phlocs.histleap.Slider;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Range {
    private Division startDivision;
    private Division endDivision;

    public Range(Division startDivision, Division endDivision) {
        this.startDivision = startDivision;
        this.endDivision = endDivision;
    }

    public Division getStartDivision() {
        return startDivision;
    }

    public void setStartDivision(Division startDivision) {
        this.startDivision = startDivision;
    }

    public Division getEndDivision() {
        return endDivision;
    }

    public void setEndDivision(Division endDivision) {
        this.endDivision = endDivision;
    }
}
