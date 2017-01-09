package tech.phlocs.histleap.list_item;

import java.util.ArrayList;

import tech.phlocs.histleap.model.Division;

public class DivisionListParentItem {

    private String name;
    private long id;
    private boolean isSelected;

    public DivisionListParentItem(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public DivisionListParentItem() {}

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
