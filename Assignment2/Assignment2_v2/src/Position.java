public class Position implements Comparable {
    public int x;
    public int y;
    public int cost;
    public int totalCost;
    public int heuristic;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = 1;
    }

    public Position(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.totalCost = 0;
        this.heuristic = 0;
    }

    @Override
    public int compareTo(Object o) {
        return (this.cost + this.heuristic) - (((Position)o).cost + ((Position)o).heuristic);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") " + this.totalCost;
    }
}
