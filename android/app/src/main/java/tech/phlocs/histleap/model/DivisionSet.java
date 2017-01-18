package tech.phlocs.histleap.model;

import java.util.ArrayList;

public class DivisionSet {
    private String name;
    private ArrayList<Division> divisions;

    public DivisionSet(String name, ArrayList<Division> divisions) {
        this.name = name;
        this.divisions = divisions;
    }
    public DivisionSet() { }

    public ArrayList<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(ArrayList<Division> divisions) {
        this.divisions = divisions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
