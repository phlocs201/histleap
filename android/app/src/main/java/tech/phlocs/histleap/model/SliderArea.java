package tech.phlocs.histleap.model;

/**
 * Created by matsumura_kazuki on 2017/01/10.
 */

public class SliderArea {
    private int top;
    private int height;
    private int width;
    private int columnNumber;

    public SliderArea(int top, int height, int width, int columnNumber) {
        this.top = top;
        this.height = height;
        this.width = width;
        this.columnNumber = columnNumber;
    }

    public int getPosition(int x, int y) {
        if (y < top || y > top + height) {
            return -1;
        } else {
            return (x / (width / columnNumber));
        }
    }
}
