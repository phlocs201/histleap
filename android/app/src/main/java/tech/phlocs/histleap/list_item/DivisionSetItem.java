package tech.phlocs.histleap.list_item;

import java.util.ArrayList;

import tech.phlocs.histleap.model.Division;

public class DivisionSetItem {

    private String name;
    private ArrayList<Division> divisions;
    private long id;
    private boolean isSelected;

    public DivisionSetItem(String name, ArrayList<Division> divisions, boolean isSelected) {
        this.name = name;
        this.divisions = divisions;
        this.isSelected = isSelected;
    }

    public DivisionSetItem() {}

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
