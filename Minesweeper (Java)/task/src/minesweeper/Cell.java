package minesweeper;

public class Cell {
    // 0 -> Blank
    // 1 -> Mine
    private int type;

    private int surroundingBombs = 0; //p5

    private String displayValue;

    private boolean isExplored = false;

    //private String realValue;

    public void setType(int newType) {
        if (newType == 0 || newType == 1) {
            type = newType;
        }
    }

    public boolean getIsExplored() {
        try {
            return this.isExplored;
        }
        catch (Exception E) {
            //do nothing
            return true;
        }
    }

    public void setIsExplored(boolean input) {
        this.isExplored = input;
    }

    public Cell (int cellType, String display) {
        type = cellType;
        displayValue = display;
    }

    public int getType() {
        return type;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String display) {
        displayValue = display;
    }

    public int getSurroundingBombs() {
        return surroundingBombs;
    }

    public void setSurroundingBombs(int numBombs) {
        surroundingBombs = numBombs;
    }
}
