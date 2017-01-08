package tech.phlocs.histleap.model;

public class EventInfo {
    private long id = 0;
    private String year = null;
    private String description = null;

    public long getId() {
        return id;
    }
    public String getYear() {
        return year;
    }
    public String getDescription() {
        return description;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
