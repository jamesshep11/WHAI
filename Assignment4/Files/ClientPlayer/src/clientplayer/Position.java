package clientplayer;

public class Position {

    private int col;
    private int row;
    private int value;
    private int playCount = 0; // Number of times this node was played (for bandit method)
    private double selectionProbability = 0; // For bandit method

    public Position(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public Position(int col, int row, int value) {
        this.col = col;
        this.row = row;
        this.value = value;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public double getSelectionProbability() {
        return selectionProbability;
    }

    public void setSelectionProbability(double selectionProbability) {
        this.selectionProbability = selectionProbability;
    }

    @Override
    public String toString() {
        return col + "," + row;
    }
}
