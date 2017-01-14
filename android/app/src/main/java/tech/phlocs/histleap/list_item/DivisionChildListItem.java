package tech.phlocs.histleap.list_item;

import java.util.ArrayList;

import tech.phlocs.histleap.model.Division;

public class DivisionChildListItem {

    private long id;
    private String name;
    private int start;
    private int end;

    public DivisionChildListItem(String name, int start, int end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }
    public DivisionChildListItem() {

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
