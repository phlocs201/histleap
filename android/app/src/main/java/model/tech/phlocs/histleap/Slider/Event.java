package model.tech.phlocs.histleap.Slider;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Event {
    private int startYear;
    private String overview;

    public Event(int startYear, String overview) {
        this.startYear = startYear;
        this.overview = overview;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
