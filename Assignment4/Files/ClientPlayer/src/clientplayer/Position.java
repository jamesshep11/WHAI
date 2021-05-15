package clientplayer;

public class Position {

    private int col;
    private int row;
    private int value;

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

    @Override
    public String toString() {
        return col + "," + row;
    }
}
