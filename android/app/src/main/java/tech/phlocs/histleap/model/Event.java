package tech.phlocs.histleap.model;

/**
 * Created by matsumura_kazuki on 2017/01/07.
 */

public class Event {
    private long id;
    private int startYear;
    private String overview;

    public Event(int startYear, String overview) {
        this.startYear = startYear;
        this.overview = overview;
    }
    public Event() {

    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

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
